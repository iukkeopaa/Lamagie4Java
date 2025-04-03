上一篇我们讲解了Redisson实现分布式读写锁的核心源码，接下来两篇会是Redis实现分布式锁的收尾篇，也就是讲解大名鼎鼎的红锁机制，在上一篇末尾也说了，在开始红锁之前，我们需要先学习下联锁，这是为什么呢？其实本质上红锁和联锁是不相关的，只是Redisson巧妙的运用了模板设计模式来让红锁和联锁共用同一套模板，所以联锁的代码搞懂了，那么就可以1分钟看懂红锁的源码，所以我们此篇先来铺垫下联锁，我会采取先讲解什么是联锁再讲解它的核心原理源码的方式来讲透联锁这个知识点。

接下来我们就先来看看什么是联锁？

# 一、什么是联锁

英文是：RedissonMultiLock，官方解释成**联锁**。

大白话说就是：如果一段代码里需要多次加锁，比如积分换礼品需要给积分扣减这个动作加锁，礼品库存扣减这个动作加锁。那可以怎么做？可以申请锁两次，然后lock()加锁两次，还要保证这两次lock()要么都成功要么都失败。太麻烦，所以MultiLock解决了此问题。它就是把多把锁合并成一个锁，然后它会保证所有锁都上锁成功才算成功，也就是**联锁就是多把锁合成一把锁，且它会保证这把锁的原子性。**

> 上面积分换礼品这个场景实际生产中可能会做成微服务而不是采取加联锁的方式，这里只是为了举例说明联锁的语义。

知道联锁是什么意思后，我们来看个Redisson官方的Demo：

```ini
RLock lock1 = redissonInstance1.getLock("lock1");
RLock lock2 = redissonInstance2.getLock("lock2");
RLock lock3 = redissonInstance3.getLock("lock3");
​
RedissonMultiLock lock = new RedissonMultiLock(lock1, lock2, lock3);
// 同时加锁：lock1 lock2 lock3
// 所有的锁都上锁成功才算成功。
lock.lock();
...
lock.unlock();
```

可以清晰的发现联锁的语义很简单，就是把多把锁（Demo里是lock1、lock2、lock3）合并成一把锁（lock），然后之后的加锁解锁都是合成后的那把锁（lock）来进行，我们盲猜一下它的实现原理，其实很简单就可以做到，搞个锁的集合，然后把没把锁都放到集合里，加锁的时候用循环去遍历，对不对？所以实现原理很简单，接下来一起验证下Redisson是不是这么做的。

# 二、核心源码

我们直接看源码，通过源码一起来验证下底层原理到底是不是搞个锁的集合，然后for循环进行遍历加锁解锁呢？通过上面的Demo开始看起，getLock就不看了，没什么东西。就是获取个锁对象，相当于给这个锁起个名字，从`new RedissonMultiLock()`构造器开始看起，看看它干了什么？

**构造器**

```arduino
// 所有锁的集合，加解锁的时候全靠遍历这个locks
final List<RLock> locks = new ArrayList<>();
​
// 比如：new RedissonMultiLock(lock1, lock2, lock3);
public RedissonMultiLock(RLock... locks) {
    // 不用解释了吧?都放到上面locks集合里
    this.locks.addAll(Arrays.asList(locks));
}
```

![multilock-构造器.png](https://p1-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/02dfed4012ec405b9da57a25d522850f~tplv-k3u1fbpfcp-jj-mark:1600:0:0:0:q75.image#?w=465&h=417&s=31463&e=png&b=ffffff)

很清晰，就是我们说的那样搞个锁的集合，然后通过构造器的方式把锁都放到集合里去。验证了我们猜想的第一点，那接下来再看看加锁的时候是不是遍历逐个加锁的呢？

**tryLock()**

```java
// 删减了部分代码

@Override
public boolean tryLock(long waitTime, long leaseTime, TimeUnit unit) throws InterruptedException {
    // 加锁成功的集合。
    List<RLock> acquiredLocks = new ArrayList<>(locks.size());
    // 好家伙，证实了我们上面的猜想，它就是for循环遍历locks然后逐个加锁，那我么继续看加锁是不是lock()呢？
    for (ListIterator<RLock> iterator = locks.listIterator(); iterator.hasNext();) {
        RLock lock = iterator.next();
        boolean lockAcquired;
        try {
            // 调用没把锁的tryLock方法进行加锁，也就是没把锁的底层lua脚本。
            lock.tryLock();
        } catch (RedisResponseTimeoutException e) {
           // ...
        }
        // ...
    }
    // ...
    return true;
}
```

上述代码是删减后的，我只保留了核心代码，一些计算时间的公式代码和一些判断逻辑我给删了，这样容易理解。

其实就是三部曲：

* 遍历locks（每一把锁），逐个执行锁的tryLock()方法，至于tryLock()底层是如何做的我们之前早就分析过了，就是lua脚本，此处不重复了。
* 如果加锁失败，则逐个释放锁，然后return false，外层while(true)收到false后就进行重试加锁。这部分代码我没贴出来，做为作业自己去看看。
* 如果加锁成功，且设置了锁的过期时间，则遍历锁逐个设置过期时间。如果没设置过期时间，则开启watchDog续期（各个锁自己的逻辑，之前文章讲过）。这部分代码也没贴出来，同样做为作业自己去看看。

![multilock-lock.png](https://p9-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/204c76e7d3f041d8990642f8019d7a79~tplv-k3u1fbpfcp-jj-mark:1600:0:0:0:q75.image#?w=1279&h=397&s=45517&e=png&b=00cc00)

很完美，就是我们猜想的那样，一个for循环遍历逐个加锁，失败的话全部都解锁，至于底层是怎么加锁的联锁并不关心，直接调用没把锁自己的lua脚本进行加锁。至于怎么重入的、watchDog是怎么工作的，这些东西联锁都不关心，它只是把各个锁给整合起来，加锁的lua细节和可重入等逻辑都是每个锁自己的，相当于联锁是个上层框架而已。

说了这么多，那解锁是如何实现的呢？我们再次盲猜下，解锁岂不是遍历locks这个锁的集合，然后逐个调用unlock不就行了？一起来看看是不是这么做的。

**unlock()**

```csharp
public void unlock() {
    List<RFuture<Void>> futures = new ArrayList<>(locks.size());
​
    // 遍历，逐个解锁，具体解锁的逻辑直接调用的各个锁自己的
    for (RLock lock : locks) {
        futures.add(lock.unlockAsync());
    }
}
```

太简单了！for循环遍历逐个解锁。完全和我们猜的一致。

再次强调下：**RedissonMultiLock就是个上层框架，不涉及到lua的操作，它就是把其他锁整合起来然后通过Java逻辑来一起加锁和解锁。locks是构造器里传递进来的锁，它其实就是调用每一个锁的unlockAsync方法，然后底层的lua都是各自的，RedissonMultiLock本身不涉及lua。**

![multilock-unlock.png](https://p9-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/397c4bfc423b4440a00e68b2c3049f96~tplv-k3u1fbpfcp-jj-mark:1600:0:0:0:q75.image#?w=955&h=283&s=37059&e=png&b=ffffff)

# 三、总结

一起来总结下联锁：

原理简单到爆，一句话就总结完了核心：**RedissonMultiLock就是个上层框架，负责把各个锁整合到一起，原理就是内部维护一个锁的集合，然后通过构造器的方式把各个锁放到集合里，加锁/解锁的时候就通过遍历集合来调用没把锁自己的lock/unlock方法，至于底层是如何实现互斥、可重入等逻辑细节都是每把锁自己实现的。**

或许你还会对我开头说的红锁和联锁的实现代码是模板设计模式，是同一个模板方法感到困惑，但是不急，下一篇我们就来剖析下红锁，看看它到底是怎么实现的，本篇文章看懂后，下一篇的实现代码会感到so easy！