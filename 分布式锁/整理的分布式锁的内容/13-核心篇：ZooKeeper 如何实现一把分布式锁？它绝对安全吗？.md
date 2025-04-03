实现分布式锁的方式不仅有 Redis，还有 ZooKeeper，那 ZooKeeper 是如何实现分布式锁呢？相对于 Redis 来讲，ZooKeeper 实现分布式锁会更简单可靠，因为 ZooKeeper 无须像 Redis 那样开一个 WatchDog 去维护一个带有效期的锁状态变量，而是直接利用临时节点来完成。因此，接下来几篇我们还会重点分析下 ZooKeeper 是如何实现各种分布式锁的，你可以对比着来学习。

今天这篇文章我会按照这样的思路来讲解：先讲明白 ZooKeeper 的实现原理，也就是实现思想，然后带着思想去深入到代码，看看开源框架 Curator 的代码实现是否和我们的设计思想吻合，最后还会讨论“ZooKeeper 实现分布式锁真的安全吗？”这个经常会被问到的问题。

相信通过这篇文章，你不仅可以学习到 ZooKeeper 是如何实现分布式锁的，而且还可以学到分布式锁的核心设计思想以及设计理念，这才是最宝贵的东西。

# 一、实现原理

大家都说 ZooKeeper 实现分布式锁更简单可靠，那么 ZooKeeper 实现分布式锁的核心原理是什么呢？怎么就简单可靠了呢？我们接下来就带着这个疑问去贯穿全文。

**ZooKeeper实现分布式锁的核心原理是临时节点，更确切的说法是临时顺序节点。**

不过，这里需要先补充一个知识点，那就是 ZooKeeper 的节点客户端是如何和 ZooKeeper 服务端维持心跳的。

**ZooKeeper的节点是通过session心跳来续期的，比如客户端1创建了一个节点， 那么客户端1会和ZooKeeper服务器创建一个Session，通过这个Session的心跳来维持连接。如果ZooKeeper服务器长时间没收到这个Session的心跳，就认为这个Session过期了，也会把对应的节点删除。**

好了，知道了节点是怎么工作之后，我们再来看临时节点是什么。

临时节点类型的最大特性是：**当客户端宕机后，临时节点会随之消亡。**

想想 Redis 作为分布式锁的时候，为防止宕机死锁需要添加过期时间，为防止提前过期还要开启 watchDog 定时续期。太烦琐，越烦琐越不可靠。而 ZooKeeper 实现分布式锁就太简单了，直接利用临时节点。

**当客户端抢锁后就给这个客户端分配一个临时节点，只要没释放锁就一直持有这个临时节点，当释放锁后或者服务意外宕机时,临时节点都会被删除，这样其他客户端又能抢锁。** ZooKeeper 实现分布式锁就是这么简单。

![](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/cbee4ab40545477d9b4e7cd87dc33fe9~tplv-k3u1fbpfcp-jj-mark:1600:0:0:0:q75.image#?w=834&h=516&s=41545&e=png&b=ffffff)

那其他客户端竞争抢锁后发现有人加锁了，他们怎么办？**阻塞等待，并会开个监听器监听上一个节点，如果发现上一个节点释放了锁，那就立即得到通知，然后自己进行上锁。**

那我怎么知道我的上一个节点是谁呢？这就需要**顺序性**，比如第一个节点叫 lock\_01，第二个节点就自增变成 lock\_02，第三个节点就是 lock\_03，lock\_02 监听 lock\_01，lock\_03 监听 lock\_02，以此类推。所以说实现分布式锁的核心原理就是**临时顺序节点。**

![](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/df7a66fde26c441087b2f1d0f26edc70~tplv-k3u1fbpfcp-jj-mark:1600:0:0:0:q75.image#?w=1422&h=640&s=78086&e=png&b=ffffff)

那怎么释放锁呢？**直接删除临时顺序节点，简单粗暴。然后监听这个锁的客户端收到上一个节点释放的通知后,就会进行抢锁。**

![](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/59e0be563e7d441ea5c8e4f2889e5f6f~tplv-k3u1fbpfcp-jj-mark:1600:0:0:0:q75.image#?w=1586&h=284&s=44637&e=png&b=ffffff)

通过上述分析后，我们这就彻底搞清楚 ZooKeeper 实现分布式锁的核心原理以及简单可靠的原因了。接下来我们就“趁热打铁”再来介绍下它的代码实现。

# 二、代码实现

不过我们不会从 0 到 1 基于 ZooKeeper 手写一把分布式锁，这样做没意义，也浪费时间。ZooKeeper 已经有了很强大的客户端来支持分布式锁，那就是 Curator，所以这里我们就直接分析 Curator 是如何基于 ZooKeeper 来实现分布式锁的。

另外，需要说明的是：如何与 ZooKeeper 建立连接、ZooKeeper 客户端如何启动等相关代码我们暂不分析，这不是重点，毕竟我们不是讲解 ZooKeeper，而是讲解如何基于 ZooKeeper 来实现一把分布式锁，也就是用代码实现上面讲解的核心原理。

## 1\. 如何加锁

上面讲解实现原理的时候我们说过加锁的核心就是：**临时顺序节点。** 我们一起来看下是不是这样。

Curator 客户端上锁的入口是 acquire，如下：

```java
// org.apache.curator.framework.recipes.locks.InterProcessMutex#acquire(long, java.util.concurrent.TimeUnit)
public boolean acquire(long time, TimeUnit unit) throws Exception {
    return internalLock(time, unit);
}
```

很简单，没逻辑，直接调用的是`internalLock(time, unit);`，所以我们继续分析这个方法：

```java
// org.apache.curator.framework.recipes.locks.InterProcessMutex#internalLock
private boolean internalLock(long time, TimeUnit unit) throws Exception {
    // 获取当前线程
    Thread currentThread = Thread.currentThread();
    // 尝试加锁
    String lockPath = internals.attemptLock(time, unit, getLockNodeBytes());
    // 加锁成功的话就放到threadData里
    if ( lockPath != null ) {
        LockData newLockData = new LockData(currentThread, lockPath);
        threadData.put(currentThread, newLockData);
        return true;
    }
    return false;
}
```

（上面是我删减后的代码，删除了与我们这个小标题无关的代码，我们只看核心逻辑，分析其设计原理以及架构即可。）

我们看到其核心就两个：

* 尝试加锁；

* 成功的话构建`LockData`对象放到 threadData 里。

![](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/234503528418473b985958b060905980~tplv-k3u1fbpfcp-jj-mark:1600:0:0:0:q75.image#?w=2052&h=510&s=67517&e=png&b=ffffff)

那现在你可能会有疑问：尝试加锁是什么意思？它干了什么？加锁成功后会放入 threadData 里，这个对象是什么？接下来我们就一个一个去分析。

先看下**尝试加锁**的实现代码：

```java
// org.apache.curator.framework.recipes.locks.LockInternals#attemptLock
// 下面是删减后的代码
String attemptLock(long time, TimeUnit unit, byte[] lockNodeBytes) throws Exception {
    try {
        // 创建这个锁
        ourPath = driver.createsTheLock(client, path, localLockNodeBytes);
        // 这个方法这里先不关心，是多个client抢锁时互斥阻塞等待的代码
        hasTheLock = internalLockLoop(startMillis, millisToWait, ourPath);
    }
    catch ( KeeperException.NoNodeException e ) {
        //...
    }
    return null;
}
```

其实就是两个方法：创建锁和锁竞争就阻塞等待。

我们这里先分析创建锁的逻辑，阻塞等待的方法下面讲解互斥性的时候再分析。

```java
// org.apache.curator.framework.recipes.locks.StandardLockInternalsDriver#createsTheLock
// 下面代码是删减后的代码
public String createsTheLock(CuratorFramework client, String path, byte[] lockNodeBytes) throws Exception {
    return client
        .create()
        .creatingParentContainersIfNeeded()
        .withProtection()
        .withMode(CreateMode.EPHEMERAL_SEQUENTIAL)
        .forPath(path, lockNodeBytes);
}
```

看到这里，你应该就明白了吧，只需要关心一个方法：withMode。它里面传递的是`CreateMode.EPHEMERAL_SEQUENTIAL`，临时顺序节点！

所以我们说加锁的核心就是：**创建了个临时顺序节点。**

还有最后一个问题，那就是加锁成功后放入 threadData 里，threadData 是什么？它其实是个全局的 ConcurrentMap，Thread 作为 key，LockData 作为 value，也就是标识了一个 Thread 的锁对象。

```java
private final ConcurrentMap<Thread, LockData> threadData = Maps.newConcurrentMap();
```

![](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/62f6417aa5784677a4876719f66da9e8~tplv-k3u1fbpfcp-jj-mark:1600:0:0:0:q75.image#?w=1372&h=1054&s=99389&e=png&b=ffffff)

**这里小结一下：加锁就是请求ZooKeeper服务端创建一个临时顺序节点，然后把这个线程作为key、锁对象作为value放到ConcurrentHashMap中。**

那加的锁可重入吗？肯定可重入的，那它是如何做到可重入的？下面我们接着分析。

## 2\. 如何可重入

上面我们提到锁对象 LockData 就是存到 ConcurrentHashMap 里的 value，其实它就是锁重入的关键。我们先来剖析下这个对象：

```java
private static class LockData {
    // 锁所属的线程
    final Thread owningThread;
    // 临时顺序节点的路径
    final String lockPath;
    // ？？？
    final AtomicInteger lockCount = new AtomicInteger(1);
}
```

前两个很好理解了，那 lockCount 是什么呢？还是原子类 AtomicInteger 类型的。它就是锁重入次数，我们也看到了，默认是 1，代表加锁成功后就是 1，那按照理论来推的话，重入一次，这个值就该 +1，是不是这样呢？答案是肯定的，在上面讲解加锁代码的时候我们省略了下面一小段：

```java
// 根据当前线程获取锁对象，如果获取到了，那肯定是有锁的，这次是锁重入
LockData lockData = threadData.get(currentThread);
if ( lockData != null ) {
    // 锁重入的关键：其实就是我们上面说的那个AtomicInteger原子类自增1
    lockData.lockCount.incrementAndGet();
    return true;
}
```

![](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/64ebf1f7fcc647e5a4a80d7118873d5f~tplv-k3u1fbpfcp-jj-mark:1600:0:0:0:q75.image#?w=1636&h=1050&s=128242&e=png&b=ffffff)

## 3\. 如何互斥

什么叫互斥？也就是说 Client1 上锁成功后，其他 Client 在进行上锁的时候需要排队等待，不能同时存在两把锁，这就叫互斥。那 ZooKeeper 的客户端 Curator 是如何实现互斥的呢？

在上面讲解“尝试加锁”时，我们遗留了一个`internalLockLoop()`方法没说，其实这个方法就和互斥性紧密相关，所以把它放到了这里来讲。

```java
// org.apache.curator.framework.recipes.locks.LockInternals#internalLockLoop
// 下面代码是删减后的代码
private boolean internalLockLoop(long startMillis, Long millisToWait, String ourPath) throws Exception {
    boolean haveTheLock = false;
    try {
        while ((client.getState() == CuratorFrameworkState.STARTED) && !haveTheLock) {
            // 获取path下对应临时顺序节点，并按编号从小到大排序。底层采取的java.util.Comparator#compare来排序的
            List<String> children = getSortedChildren();
             // 获取当前线程创建的临时顺序节点名称
             String sequenceNodeName = ourPath.substring(basePath.length() + 1);
            // 这个方法底层就是判断当前节点编号是不是children里的第一个，是的话就能抢锁，不是的话就计算出上一个节点序号是谁，然后下面监听这个节点。（因为按照编号排序了，所以可以得出上一个节点是谁）
            PredicateResults predicateResults = driver.getsTheLock(client, children, sequenceNodeName, maxLeases);
            // 如果当前客户端就是持有锁的客户端，直接返回true
            if (predicateResults.getsTheLock() ) {
                haveTheLock = true;
            } else {
                // 如果没抢到锁，则监听上一个节点
                String  previousSequencePath = basePath + "/" + predicateResults.getPathToWatch();
                synchronized(this) {
                    try {
                        // 监听器，watcher下面分析
                        client.getData().usingWatcher(watcher).forPath(previousSequencePath);
                        // 重点在这了，wait()，等待。也就是说没抢到锁的话就开启监听器然后wait()等待。
                        wait();
                    } catch ( KeeperException.NoNodeException e )  {}
                }
            }
        }
    }
    return haveTheLock;
}
```

看上去这段代码稍微有点长，但其实逻辑很简单，可总结为如下：

* 查找到所有临时顺序节点（也就是全部抢锁的客户端），然后按照编号从小到大排序；

* 判断当前客户端是不是 children 里的第一个，不是的话就代表不能加锁，那就计算出上一个节点编号，然后开启一个 Watcher 监听这个节点（刚计算出来的上一个节点）；

* wait() 阻塞等待。

![](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/076aefd7c20b430784e2c1b1c6b2bf33~tplv-k3u1fbpfcp-jj-mark:1600:0:0:0:q75.image#?w=1828&h=964&s=120720&e=png&b=ffffff)

## 4\. 如何解锁

在分析如何解锁的代码之前我们首先需要知道解锁的时机有哪些。总结来说，解锁的时机有两个：

* 主动释放，也就是主动调用 API 来释放锁。
* 被动释放，比如服务意外宕机，这时候 ZooKeeper 会自动删除临时（顺序）节点，也就是自动释放了锁。

这两种情况我们只需要分析第一种，也就是主动释放的情况就好了，因为被动释放是意外情况，不受控制。所以接下来就一起分析下主动释放是如何做的，核心代码如下：

```java
// org.apache.curator.framework.recipes.locks.InterProcessMutex#release
// 下面代码是删减后的代码
public void release() throws Exception {
    // 获取当前线程
    Thread currentThread = Thread.currentThread();
    // 获取当前线程的锁对象，从ConcurrentHashMap里获取
    LockData lockData = threadData.get(currentThread);
    // 锁重入次数-1，然后看看是不是大于0，如果大于0那代表有锁重入，直接-1，不删除锁节点，因为没释放完全。
    int newLockCount = lockData.lockCount.decrementAndGet();
    if ( newLockCount > 0 ) {
        return;
    }
    try {
        // 如果锁重入次数为0了，那就释放锁
        internals.releaseLock(lockData.lockPath);
    }
    finally {
        // 释放完后从ConcurrentHashMap里移除
        threadData.remove(currentThread);
    }
}
```

整体逻辑很清晰：

* 获取当前线程的锁对象；

* 当前线程的锁重入次数 -1；

* 如果锁重入次数 -1 大于 0，那就是有锁重入，不能删除节点，锁重入次数 -1 就完事了；

* 如果锁重入次数等于 0，那就是锁释放完了，删除节点并从 ConcurrentHashMap 里移除就好了。

![](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/e40ae13eb79641d08c06c40ac1ac4c1f~tplv-k3u1fbpfcp-jj-mark:1600:0:0:0:q75.image#?w=1040&h=982&s=101973&e=png&b=ffffff)

大体流程知道了，那如何释放锁的？是不是我们之前说的直接删除临时节点就行了？

```java
// org.apache.curator.framework.recipes.locks.LockInternals#releaseLock
// 下面代码是删减后的代码
final void releaseLock(String lockPath) throws Exception {
    deleteOurPath(lockPath);
}

private void deleteOurPath(String ourPath) throws Exception {
    try {
        // 直接delete
        client.delete().guaranteed().forPath(ourPath);
    } catch ( KeeperException.NoNodeException e ) {}
}
```

这个很简单，直接 delete 节点。

那锁释放完了，其他客户端是不是能抢锁了？他们是怎么感知到我释放锁了呢？

**我们在客户端抢锁的时候注册一个监听器，监听上一个比他小的节点（因为是顺序节点，所以都带编号的，很容易就知道谁比我小一级），我们释放锁会执行delete操作，这个操作会通知监听器，那监听器干了啥？**

```java
// org.apache.curator.framework.recipes.locks.LockInternals#watcher
private final Watcher watcher = new Watcher() {
    @Override
    public void process(WatchedEvent event) {
        // 调postSafeNotify方法
        client.postSafeNotify(LockInternals.this);
    }
};

// org.apache.curator.framework.CuratorFramework#postSafeNotify
default CompletableFuture<Void> postSafeNotify(Object monitorHolder) {
    return this.runSafe(() -> {
        synchronized(monitorHolder) {
            // 重点在这里，notifyAll。通知所有等待（wait）的节点。
            monitorHolder.notifyAll();
        }
    });
}
```

监听器就干了一件事：`notifyAll()`，通知 wait 的节点。比如，我下一个节点 wait，所以当我释放了，下一个节点就会收到通知，然后他就有资格进入下一次 while 循环进行尝试加锁等逻辑。

![](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/b1814be9f93144abbb8a547da9be4078~tplv-k3u1fbpfcp-jj-mark:1600:0:0:0:q75.image#?w=1656&h=980&s=135745&e=png&b=ffffff)

## 5\. 小结

为了便于你更好地理解和记忆，这里我将一些关键点做一个简单的小结。

ZooKeeper 实现分布式锁的核心就是：**临时顺序节点**。这里我们主要说明下加锁流程和解锁流程。

**加锁流程：**

* 采取 LockData 对象来代表一个锁对象，里面包含锁重入次数，用 ConcurrentHashMap<Thread, LockData> 来维护；

* 锁重入的话就是在 LockData 里的原子类变量（lockCount）自增 1；

* 有人提前抢占锁了，其他客户端再抢占锁就会发生互斥，核心流程就是监听上一个节点（因为是临时顺序节点，所以通过排序可以知道上一个节点是谁，这样也保证了公平性）且调用 wait() 进入阻塞状态。

**解锁流程（主要说明主动释放锁流程）：**

* 先判断有没有锁重入，有的话就先重入次数 -1，没有的话就直接 delete 掉临时顺序节点；

* 锁释放完成后，阻塞排队的监听器会收到释放的通知，然后进行 notifyAll() 唤醒 wait() 阻塞等待的客户端。

# 三、ZooKeeper 实现分布式锁真的安全吗？

都说 ZooKeeper 实现分布式锁简单安全，“简单”我承认，但真的“安全”吗？**不，我个人认为它并不绝对安全。**

因为我们前面在讲解“ZooKeeper 的节点客户端是如何和 ZooKeeper 服务端维持心跳”时说了：

> ZooKeeper 的节点是通过 session 心跳来续期的，比如客户端 1 创建了一个节点， 那么客户端 1 会和 ZooKeeper 服务器创建一个 Session，通过这个 Session 的心跳来维持连接。如果 ZooKeeper 服务器长时间没收到这个 Session 的心跳就认为这个 Session 过期了，也会把对应的节点删除。

那你想想：如果发生了 FullGC，Stop The World 时间大于 Session 心跳时间了，那不就误删了吗？或者出现了网络抖动，抖动时间大于 Session 心跳时间，那不也把节点删除了吗？这样其他客户端不是又能加锁了吗？但**这样实际上有两把锁在一起执行**。

可能有的小伙伴会认为：这是网络问题，和 ZooKeeper 无关。仁者见仁，智者见智吧。我认为，只要同时两个客户端拥有同一把锁，那就是不安全的。

# 四、总结

本文很关键，它是 ZooKeeper 实现分布式锁的核心，其他（比如，非公平锁、不可重入锁等）都是基于此原理来完成的，所以你一定要重点掌握！

本文我们主要讲解了以下几个关键知识点。

* ZooKeeper 实现分布式锁的核心原理，我通常也称之为核心架构设计。

* Curator 实现加锁、可重入、互斥性以及解锁的方法。

* 最后还探讨了一个“ZooKeeper 实现分布式锁是否真的安全”的话题。

为了帮助你更好地理解，最后我们用一张图来总结我们整篇的思路和流程：

![](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/5f14b52da83b4348aa5ae62604652ee3~tplv-k3u1fbpfcp-jj-mark:1600:0:0:0:q75.image#?w=4662&h=3420&s=783908&e=png&a=1&b=ffdb66)

另外，我经常遇到有小伙伴有这样的疑问：本文所讲的锁是公平锁吗？这里我就额外补充一下，提前做一个答疑。毋庸置疑，这肯定是公平锁，因为它采取了**临时顺序节点**，按照编号排序，当前节点监听上一个节点，释放锁后下一个节点进行加锁，一个一个排队来，所以它是一个**公平可重入锁**。