在上一讲中，我们主要讲解了分布式锁是什么以及为什么需要分布式锁这两个话题，也提了一下实现分布式锁的核心设计方案以及目前有哪几种成熟的方案，但是每一种方案具体如何实现我们还不知道，此篇开始以及以后篇幅的核心主题就是具体的实现原理和细节了。

此篇先从最简单的开始讲起：MySQL如何实现一把分布式锁？本篇的讲解方式是会先讲明白原理，然后再讲具体的代码实现，最后来总结下MySQL作为分布式锁的优缺点，只有讲明白了原理才能更好的掌握代码实现，就好比没有需求（需求=原理），你怎么写代码？所以进入正题，我们看下MySQL作为分布式锁的实现原理是什么？

# 一、实现原理

一把锁应该具备哪些特点？最核心最基本的就是：**互斥性。**

什么是互斥性？上一篇刚讲过，这次在复习下。互斥性用一句话来描述的话就是：**不允许多个客户端同时执行一段代码。** 比如客户端A在执行method1(资源A)，这时候客户端B进来了，也在执行method1(资源A)，客户端A和客户端B同时执行。这就不具备互斥性，正常互斥的逻辑应该是客户端A执行method1(资源A)就相当于给资源A这个大门上了一把锁，当其他客户端再来的时候发现大门是锁着的，没有钥匙，只能等待。只有当客户端A释放锁（打开大门）后，其他客户端才能进去抢锁（钥匙）。

现在我们知道了什么是互斥性，那MySQL该如何实现这一特征呢？表该如何设计呢？

其实也很简单：**利用唯一索引**来实现，只要抢占锁了就插入一条记录，多客户端抢锁会互斥，互斥体现在插入重复数据会报唯一key的异常。简单总结下流程：

* 客户端1先来加锁，这时候没有其他客户端持有锁，所以加锁成功，也就是在表里insert一条数据成功。
* 这时候客户端2也来加锁，发现客户端1正在持有锁（表里有数据），肯定是不能加锁成功的，会返回key重复的异常（或者提前select一下看看有没有数据，有数据就代表有锁），这时候就进行重试加锁。

* 持有锁的线程执行完逻辑后需要释放锁，释放锁就直接把数据表里的记录删除就好了。这样其他客户端就能正常抢到锁了。

![lock.png](https://p9-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/be89891c4a494efbbe838f18440b8e94~tplv-k3u1fbpfcp-jj-mark:1600:0:0:0:q75.image#?w=1220&h=464&s=39282&e=png&b=ffffff)

大概流程知道了，也很简单。但是你是不是有很多疑问？比如如下：

* 分布式锁的表怎么设计？唯一key怎么确定是哪个字段？
* 抢锁失败后要重试，怎么重试？
* 释放锁就是删除记录，那如何安全的释放锁？

下面一个个分析这四个问题。先来看第一个：**分布式锁的表怎么设计？唯一key怎么确定是哪个字段？**

## 1\. 表设计和唯一key

我们首先能想到的一种方式是：每个需要分布式锁的业务都单独建立一张表，比如订单业务需要分布式锁，那就搞个order\_lock表，核心字段：order\_id和user\_id。order\_id是唯一索引，不允许重复，如果有order\_id那就代表加锁状态，其他客户端来后如果并发insert同一个订单了，那肯定报唯一key冲突错误，这样就可以确保只有一个客户端持有锁了，表设计如下：

```sql
DROP TABLE IF EXISTS `order_lock`;
CREATE TABLE `order_lock` (
  `order_id` int NOT NULL,
  `user_id` int DEFAULT NULL,
  PRIMARY KEY (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

但是他有什么弊端？**不通用。** 每个需要分布式锁的业务都要单独创建一个lock表来存储锁状态，对于使用者来讲很不友好，那怎么设计成一张通用的分布式锁表呢？

我们想想都需要哪些字段？首先并发线程不安全是作用在同一个方法上的不同线程，对不对？那不就有了唯一字段：方法名了吗？但是不同项目的方法名又可能相同，比如都叫save，那怎么办？这不又多了一个项目id或者项目名称了吗？所以方法名和项目名是个组合唯一索引。SQL如下：

```sql
DROP TABLE IF EXISTS `common_lock`;
CREATE TABLE `common_lock` (
  `id` int NOT NULL,
  `method_name` varchar(30) DEFAULT NULL,
  `project_name` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `lock_key` (`project_name`,`method_name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

这样好像确实解决了我们的问题，但是存在问题，因为锁的不是方法，而是资源，比如不同线程都操作method1，但是参数（资源）不同，那是无所谓的，所以我们还差个资源ID。SQL如下：

```sql
DROP TABLE IF EXISTS `common_lock`;
CREATE TABLE `common_lock` (
  `id` int NOT NULL,
  `resource_id` int NOT NULL,
  `method_name` varchar(30) DEFAULT NULL,
  `project_name` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `lock_key` (`project_name`,`method_name`,`resource_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

目前为止基本功能实现了，但是不完善，为什么？因为我们想要的效果是支持重入，那怎么做可重入？可重入的关键在于需要一个字段存储重入次数，所以我们还少了个entry\_count字段，加上这个就可以了吗？他是哪个线程重入的？我们并不知道。所以还需要一个thread\_id字段。但是你想想这样就可以了么？现在都集群部署，不同机器的线程id是可以一样的，所以是不是还需要机器ip？所以一个相对完善的SQL如下：

```sql
DROP TABLE IF EXISTS `common_lock`;
CREATE TABLE `common_lock` (
  `id` int NOT NULL,
  `resource_id` int NOT NULL,
  `method_name` varchar(30) NOT NULL,
  `project_name` varchar(30) NOT NULL,
  `thread_id` int NOT NULL,
  `entry_count` int NOT NULL,
  `host_ip` varchar(30) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `lock_key` (`project_name`,`method_name`,`resource_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

还有没有可优化的空间？

我们第一感觉好像挺完美，但是你想想我们只想知道一个唯一key和这个key的重入次数，结果搞出来这么多字段，这样好吗？我个人认为不怎么好，还可以继续优化：将`resource_id`、`method_name`、`project_name`这三个字段设计成一个key，比如提供一个方法就叫getKey：

```scss
// 也可以考虑加密一下，缩短长度。但是建议可解密，这样可以反推出来是哪个项目哪个方法加的锁。
public String getKey() {
    return 
        new StringBuilder()
        .append(getProjectName())
        .append("_")
        .append(getMethodName())
        .append("_")
        .append(getResourceId())
        .toString();
}
```

然后SQL就变成了最终这个样子：

```sql
DROP TABLE IF EXISTS `common_lock`;
CREATE TABLE `common_lock` (
  `id` int NOT NULL,
  `lock_key` varchar(100) NOT NULL,
  `thread_id` int NOT NULL,
  `entry_count` int NOT NULL,
  `host_ip` varchar(30) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `lock_key` (`lock_key`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

我们考虑到了重入的情况，那重入的逻辑是怎样的呢？

**很简单，判断有没有锁，有锁，再继续判断是不是自己加的锁，是自己加的锁，那就update entry\_count字段+1就完事了。**

那如果不是自己加的锁呢？这时候就是锁已经被其他人持有了，我又来加锁，加锁失败了，我该怎么办？重试，那重试策略有哪些呢？

## 2\. 重试策略

常规的两种重试策略就是：**死循环一直重试和重试一定次数或者重试一定的时间后还是加锁失败的话就退出。**

看下一直重试策略的伪代码：

```kotlin
if (加锁失败) {
    while(true) {
        // 重试加锁
        if (重试成功) {
            return true;
        }
        // 休息一定时间
    }
}
```

那重试一定次数或者重试一定时间后还是失败就退出的代码该怎么写呢？这就太简单了，就是基于上面代码加一个重试次数字段，每次重试+1，判断达到次数后还没成功就`return false`。重试一定时间后退出和重试一定次数后退出的思路是一样的，记录下重试前的时间，然后每次重试都对比时间，看看是不是超出了用户自定义的时间，超出了就`return false`代表加锁失败。

现在加锁的核心逻辑基本已经搞定了，那方法执行完后怎么释放锁呢？

## 3\. 释放锁

直接删除这个key可行吗？肯定**不可行**！因为有锁重入的情况，比如我重入了2次，释放了1次锁，你就直接给我把锁删除了，那不就出问题了吗？所以需要先判断下锁重入次数，如果锁重入次数大于1，那就将其减1，如果等于1，那就直接delete这条数据。

锁释放后，其他客户端重试的时候就会进行加锁成功了。

![unlock.png](https://p1-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/448f1733de8849fabf43bc4490eca9cf~tplv-k3u1fbpfcp-jj-mark:1600:0:0:0:q75.image#?w=1354&h=670&s=68233&e=png&b=ffffff) 好了，到目前为止我们分析了MySQL作为分布式锁该如何设计、如何加锁、如何重试以及如何解锁。但是还有一个致命的问题可能发生，那就是**死锁**。

想一个场景：假设加锁成功，还没来得及释放系统就宕机了，当他再次恢复的时候发现锁还在，永远无法上锁成功，导致了死锁。这种情况怎么办？证明我们上面的设计还不是很严谨，存在巨大的bug！那该如何防止死锁呢？

## 4\. 防死锁

防死锁的处理方式也很简单，我们**搞个定时器定时检查那些由于意外宕机或者其他外界原因导致锁一直没释放的那些数据来防止死锁。**

那我怎么知道哪些锁数据是由于宕机或者其他原因导致的一直无法释放呢？

**可以记录下加锁时间，然后定时器定时去扫描超出n秒（可以在加锁的时候让用户自定义传递时间参数）后还未释放锁的数据，将其删除释放。**

但是这样又存在一个巨大的问题：如果我们的方法很耗时，就是没执行完，但是定时任务扫描的时候却认为我的锁超时了，给我释放了，这时候其他客户端又能成功上锁了，这不是出问题了吗？所以MySQL实现分布式锁简直太不可靠了，那这种能解决吗？肯定是可以解决的，解决方案就是单独起个线程进行定时续期，具体的细节我们在介绍Redis实现分布式锁的时候详细介绍，也就是大名鼎鼎的watchDog机制。

现在MySQL实现分布式锁的细节和原理以及可能出现的问题全部分析完毕了，最后在花点时间总结下他的优缺点。

# 二、优缺点

**优点：**

* 实现方式很简单，搞一张表，两个字段：唯一key和重入次数，然后操作的时候只涉及单表insert和delete以及重入的时候update重入次数+1即可。
* 不额外引入其他中间件，比如Redis、Zookeeper、Etcd等。直接MySQL就搞定了，如果用的其他数据库（比如Oracle等），也是一样的思路和逻辑。

**缺点：**

* 性能很低、支持的并发不高。因为每次加锁、解锁、重入的时候都要额外操作一次MySQL。最简单的场景加锁解锁不考虑重入，那还需要额外与MySQL交互两次呢。
* 线程不安全，虽然有办法解决死锁的问题，但是无法保证绝对的安全。也就是上面说的：方法没执行完，但是超出了设定时间，这时候定时器扫描的时候替我们释放锁了，其他客户端就又能加锁了，这时候其他客户端和我当前客户端同时拥有一把锁。
* 强制依赖MySQL，所以MySQL不能出现单点故障的问题，可搞两个MySQL数据库做同步。但是为了分布式锁搞n台MySQL有点不合适。
* 支持的锁种类有限，我们实现的是非公平锁，要想实现公平锁还需要搞个表来模拟队列，先来后到，多一张表就多一次与MySQL的交互，即使能做，但是如果实现读写锁呢？岂不是更麻烦了。

# 三、总结

本篇文章介绍的是基于MySQL实现的分布式锁的核心原理以及具体伪代码实现，可以发现MySQL实现分布式锁太简单了，核心原理可以总结为一句话：**基于MySQL的唯一索引在insert的时候会出现重复key的错误来实现。**

我们也分析了他的利弊，**MySQL实现性能上肯定是不如Redis和Zookeeper的**。所以我个人是不建议使用MySQL作为分布式锁来用的，如果项目对性能要求不高，也没有很高的并发量，但是有分布式锁的需求，还不想引入其他中间件（比如Redis、Zookeeper），那可以考虑采取MySQL来实现。

此篇很关键，一定要反复看，一定要掌握核心的设计思想，不管是Redis做分布式锁还是Zookeeper做分布式锁，他们的核心思想都大同小异，说简单点都是确定如何存储、如何确定唯一key、如何做到可重入和如何释放锁。源码不重要，掌握核心才是关键！