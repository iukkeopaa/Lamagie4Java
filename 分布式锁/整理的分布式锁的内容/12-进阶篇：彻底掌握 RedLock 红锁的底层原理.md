​	上一篇我们讲解了联锁，通过本篇文章你就会明白我为什么要在讲红锁之前先讲解联锁，因为红锁的底层就是用的联锁的代码，当然，我不会直接讲解源码，我会先剖析下红锁的核心设计思想是什么，以及存在哪些注意事项和问题，最后我会用一个Redisson红锁的Demo来深入到红锁的底层源码。

话不多说，先来看下红锁的设计思想。

# 一、RedLock设计思想

在第一次讲解Redis实现分布式锁的文章中我们简单提及了红锁，但是并没有剖析其原理。是因为红锁非常重要，不论是面试中还是设计思想上都值得我们深度研究。

红锁，英文名RedLock，非常著名！是Redis实现分布式锁**相对最安全可靠**的一种手段。

**他的核心思路是：搞几个独立的Master且每个Master不能有Slave，比如5个。然后挨着个的加锁，只要超过一半以上（这里是`5 / 2 + 1 = 3`个）那就代表加锁成功，然后释放锁的时候也逐个释放。这样的好处在于：假设一台Master挂了的话，还有其他两台Master在持有锁，所以并不会影响到业务。完美解决了单点故障的问题，由于每个Master不会有Slave，所以也完美的解决了主从模式下主从同步延迟和数据不一致的问题。**

我们接下来分析下完整的加解锁细节流程：

* 获取当前的服务器时间（精确到毫秒，时间戳）

* 加锁的时候客户端向N个Master节点上获取锁（比如5个Master，那就在5个Master上申请加锁）

  > 值得注意的是：当向Redis请求获取锁时，客户端应该需要设置一个网络连接和响应超时时间，这么做的目的是为了防止某个Redis实例挂了后我们还在不断的获取锁，导致被阻塞的时间过长。也就是**获取锁的超时时间**要远远小于**锁的超时时间**。举个例子：例如你的锁过期时间为10秒，则超时时间应该在5-50毫秒之间。这样可以避免Redis已经挂掉的情况下，客户端还在一直傻傻的等待响应结果。如果客户端等待服务器端响应超时，那么客户端应立即尝试去下一个Redis实例请求获取锁。

* 只有在大多数节点（一般是【(n/2)+1】，半数以上）上获取到了锁，而且总的获取时间（总获取锁时间可以从当前时间减去步骤1记录的时间得出）小于锁的超时时间的情况下，认为锁获取成功了。也就是说总的获取锁时间超出了锁设置的超时时间，那么认为加锁失败。因为是操作多个节点，很可能前两个节点加锁成功了，但是第三个节点判断前两个加锁流程耗时太长，都超出了锁都过期时间了，那第三个节点即使加上锁也没有意义了，因为前两个实例上的锁都过期了。

* 如果锁获取成功了，锁的超时时间就是最初的锁超时时间减获取锁的总耗时时间（总耗时时间可以从当前时间减去步骤1记录的时间得出）

* 如果锁获取失败了，不管是因为获取成功的节点的数目没有过半，还是因为获取锁的耗时超过了锁的释放时间，都会将已经设置了锁的Master上将锁删除。

这五步一定要反复阅读，说实话，如果是首次接触的话，这五段逻辑不是很好理解，需要大家反复阅读，掌握其原理。

**需要注意两点：**

* Redis多个Master所在的机器时间必须同步。
* Redis红锁机器挂了的话要延迟启动1min（大于锁超时时间就行），因为：如果三台Master，写入2台成功了，加锁成功，但是挂了一个，还保留了一个Master可用，释放锁的时候自然挂了的那个不会执行del，当他瞬间再次启动的时候会发现锁还在（因为还没到过期时间），可能造成未知的问题。所以让Redis延迟启动。

**主要存在的问题：**

* 实现原理异常复杂，相信大家也看到了。
* 依然是不安全的加锁方式。比如：给5个Master都加了锁，失效时间是3s，但是因为加锁的时候可能因为网络抖动或者其他情况导致只给3台机器加完锁就到3s了，失效了。后面2台还没加锁呢，前面3个已经失效了。但是这时候其他线程又进行上锁发现前面3个无锁正常上锁，因为是过半原则，3个认为加锁成功。这就导致了两个线程同时加锁成功，前3个是后面线程的锁，后两个是最开始线程的锁，这不乱套了吗？线程也不安全了！
* 性能较低，因为它需要对每台Redis实例都进行建立连接，发送加锁命令。

有国外大佬提了两个问题推翻RedLock的绝对安全性，当然Redis作者肯定不认同他的说法，但又无法证实。感兴趣的可以搜搜看看，很好玩的。连接地址放到了文末。

原理以及每个细节我们都分析完了，再分析他的源码之前，我们先自己想想该如何实现一把红锁呢？

红锁是不是和我们上一篇讲解的联锁有很大的相似之处？红锁和联锁都是一堆小锁组成一把大锁，区别在于红锁允许失败，只要成功个数是(n/2)+1就行，而联锁不允许失败，必须全成功才行。

那我们再简单回忆一下联锁的原理，一言以蔽之就是：**搞个`List<锁> locks`来存储每把锁，然后加锁和解锁的时候都去遍历这个locks进行真正的加解锁操作，具体加解锁细节联锁并不关心，它只是上层框架，真正的加锁动作直接调用locks集合里锁的加锁api就好了。**

那就简单了，稍微学过设计模式的同学都知道有个很常用也很简单的模式叫**模板模式**，那我们是不是可以把这套加锁流程封装成模板方法，然后提供一个**允许失败个数**的抽象方法给子类实现，这样一来，联锁就允许失败0个，红锁就允许失败`锁总数 - (n/2)+1`个（也就是加锁成功数为半数以上）就行了。

那它到底是不是这么设计的呢？接下来我们通过一个官方Demo来深度剖析他的源码吧～

# 二、源码剖析

先来看个Demo，Demo和联锁的几乎一样，都是构造器传锁然后合成一把大锁。如下：

**Demo**

```JAVA
 RLock lock1 = redissonInstance1.getLock("lock1");
 RLock lock2 = redissonInstance2.getLock("lock2");
 RLock lock3 = redissonInstance3.getLock("lock3");
 ​
 RedissonRedLock lock = new RedissonRedLock(lock1, lock2, lock3);
 // 同时加锁：lock1 lock2 lock3
 // 红锁在大部分节点上加锁成功就算成功。
 lock.lock();
 ...
 lock.unlock();
```

接下来我们正式开始剖析红锁的源码，一起看下是不是和联锁用的是一个模板。

**源码** 先来看下红锁的完整类：

```JAVA
 public class RedissonRedLock extends RedissonMultiLock {
 ​
     public RedissonRedLock(RLock... locks) {
         super(locks);
     }
 ​
     @Override
     // 允许失败的数量
     protected int failedLocksLimit() {
         // 总数-允许成功数量（一半以上），也就是比如3把锁，那允许3-2=1个失败。
         return locks.size() - minLocksAmount(locks);
     }
     
     // 半数以上。比如3把锁，这个值就是3/2+1=2
     protected int minLocksAmount(final List<RLock> locks) {
         return locks.size()/2 + 1;
     }
 ​
     // 计算锁等待时间
     @Override
     protected long calcLockWaitTime(long remainTime) {
         return Math.max(remainTime / locks.size(), 1);
     }
     
     // 解锁
     @Override
     public void unlock() {
         unlockInner(locks);
     }
 ​
 }
```

哈哈哈。这么清爽？是的，这就是红锁的全部代码，但是看看继承关系，继承了RedissonMultiLock联锁，我们上一篇刚分析的联锁，简单来说就是多个小锁组成一把大锁，然后for循环逐个去加锁解锁，加锁过程中失败一个就全部解锁，重试。那我们RedLock继承他不就很简单了？让RedissonMultiLock支持个参数叫：允许失败的个数、超过多少把锁加锁成功就代表成功。不要求全部加锁成功。这不就行了吗？

所以提供了一个`failedLocksLimit()`方法，逻辑是：总数-允许成功数量（一半以上），那有这个方法来计算数量了，怎么把它用起来呢？联锁是直接遍历然后逐个加锁，不允许失败。那我们可不可以稍微改造一下，在遍历locks逐个加锁的时候，如果失败了，那么就判断**锁总数 - 加锁成功个数是不是等于配置的允许加锁失败的个数**，如果等于，那就直接`return true`返回加锁成功。

这里有几个问题：

1. 锁总数很简单，`locks.size();`就获取到了。那么加锁成功个数如何获取？我们在单独搞个地方存储，这里List就好了，比如叫：`List<RLock> acquiredLocks`

2. 锁总数和枷锁成功的个数全都有了，那允许失败的个数怎么获取？上面的`failedLocksLimit()`不就是吗？所以公式变成了如下： `locks.size() - acquiredLocks.size() == failedLocksLimit()`

所以加锁的源码如下（是在`RedissonMultiLock`里面的哦，是个模板类，上面介绍原理的时候介绍过了）：

```java
@Override
public boolean tryLock(long waitTime, long leaseTime, TimeUnit unit) throws InterruptedException {
    // 加锁成功的存储集合
    List<RLock> acquiredLocks = new ArrayList<>(locks.size());
    for (ListIterator<RLock> iterator = locks.listIterator(); iterator.hasNext();) {
        try {
           // 省略tryLock加锁的代码，上一篇讲解过
        } catch (RedisResponseTimeoutException e) {
           // 省略tryLock加锁失败的代码，上一篇讲解过
        }
		// 加锁成功
        if (lockAcquired) {
            // 放到acquiredLocks里
            acquiredLocks.add(lock);
        } else { // 加锁失败
            
            // 关键来了，这里判断了锁的总数和加锁成功数，比如3把锁，成功2个，那么3-2=1，正好，break，最后return true；
            // 和我们上面的分析吻合。
            if (locks.size() - acquiredLocks.size() == failedLocksLimit()) {
                break;
            }
        }
    }
    // 省略续期代码，上篇幅讲过。
    return true;
}
```

![image-20211121203829250.png](https://p6-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/bd0d5053a6a9489fa5504a75e1913f28~tplv-k3u1fbpfcp-jj-mark:1600:0:0:0:q75.image#?w=1190&h=686&s=48368&e=png&b=ffffff) 那如果失败个数不等于允许失败个数，也就是上面的if条件不成立，这时候怎么办？这时候我们需要看允许失败的个数是不是0，如果是0，那就代表已经超出了允许失败的个数，也就是加锁失败。如果不是0，那就代表还在允许失败个数的范围内，所以不耽误，进行下一次循环加锁流程。

一起看下源码：

```scss
 int failedLocksLimit = failedLocksLimit();
 ​
 if (locks.size() - acquiredLocks.size() == failedLocksLimit()) {
     break;
 }
 // 如果允许发生失败的数为0就代表超出了允许失败次数了，代表此次加锁失败。
 if (failedLocksLimit == 0) {
     // 解锁
     unlockInner(acquiredLocks);
     // 如果超时了，肯定就return false返回失败了。
     if (waitTime == -1) {
         return false;
     }
     // 还原允许失败次数
     failedLocksLimit = failedLocksLimit();
     // 清空已加成功的锁
     acquiredLocks.clear();
     // 还原迭代器下标位
     while (iterator.hasPrevious()) {
         iterator.previous();
     }
 } else {
     // 还在允许失败个数的范围内，就允许失败数-1，进行下一次for循环
     failedLocksLimit--;
 }
```

![Redisson-RedLock.png](https://p9-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/48bcca10e44348879ff73b728d92303a~tplv-k3u1fbpfcp-jj-mark:1600:0:0:0:q75.image#?w=2904&h=2064&s=322624&e=png&a=1&b=fefefe)

一言以蔽之加锁流程：**设置允许加锁失败的个数，然后加锁的时候记录成功数和失败数，如果超出配置的允许失败个数，则加锁失败，释放全部锁，若加锁成功数达到配置的允许最小成功数，则直接break代表加锁成功。** 现在红锁的加锁流程已经分析的一清二楚了，我们也知道了加锁失败个数超出允许失败个数的话就会申请释放锁，那么释放锁是怎么释放的呢？很简单，就for循环遍历locks然后调用unlockAsync的方法，还是那句话：它只是个上层框架，真正的加锁解锁逻辑是锁集合里各个锁来完成的。

```scss
 protected void unlockInner(Collection<RLock> locks) {
     locks.stream()
         .map(RLockAsync::unlockAsync)
         .forEach(RFuture::awaitUninterruptibly);
 }
```

![Redisson-RedLock (1).png](https://p6-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/b00df01f3c6e4e2081eb04b9c043d7bf~tplv-k3u1fbpfcp-jj-mark:1600:0:0:0:q75.image#?w=2904&h=2064&s=345617&e=png&a=1&b=fefefe)

最后我们简单总结下~

# 三、总结

红锁的原理并不复杂，其源码更是简单，说简单点就是：

* 采取多个独立的Redis实例，每个实例只部署Master节点，不部署Slave节点，这样既解决了单点问题又解决了主从同步数据延迟和不一致的问题。
* 加锁和解锁的时候都向全部Redis实例发请求，如果加锁成功数=Redis实例数的一半以上，那就代表此次加锁成功。也就是过半机制。

其源码是最简单的，直接采取的模板设计模式，继承`RedissonMultiLock`然后重写两个方法：允许失败个数、允许加锁最小成功数。其他逻辑采取父类的模板方法。这种模式和思想很值得学习，模板设计模式是业务系统最常用的一种模式之一了。

最后我们还需要学习红锁的那些值得注意的点：

* 红锁的每台Redis实例所在的服务器时间必须一致。
* 红锁并非绝对100%安全可靠。只是在Redis实现分布式锁的方式中是最相对可靠的一种方式。
* 红锁的性能偏低，因为需要加锁多次，需要多次和Redis建立连接。

至此，Redis实现分布式锁的方式我们已经全部讲解完毕，不难发现，Redis的红锁是相对最安全可靠的一种实现方式了，但是它很复杂，性能也相对其他方式低下，那我们有没有和红锁一样安全可靠或者比它更安全可靠，并且实现原理简单易懂的方式呢？有！它就是Zookeeper，下一篇我们利用一篇文章来彻底掌握Zookeeper实现分布式锁的核心思想和核心源码。

# 四、参考文献

[红锁博弈论](https://martin.kleppmann.com/2016/02/08/how-to-do-distributed-locking.html "https://martin.kleppmann.com/2016/02/08/how-to-do-distributed-locking.html")