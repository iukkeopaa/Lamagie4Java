上一篇我们讲解了Redisson这个框架实现分布式锁的加锁以及解锁的核心流程，详细的剖析了他是如何做到可重入的以及互斥性是怎么实现的底层原理，在上篇文末我们也留了个知识没有讲解，那就是**锁续期**，这篇文章我会首先带着你复习下为什么需要锁续期，然后会深度剖析下Redisson是如何实现锁续期功能的，本篇文章不难，目的明确，就是玩转这个面试常被问到的知识点，接下来一起步入正题吧～

# 一、锁续期

在开始之前先来复习下为啥需要锁续期？之所以说是复习下，是因为一开始我们讲解过这个概念。

**概念**

举个例子：

如果线程1持有锁，但是业务代码没执行完，锁却过期了，这时候其他线程又能抢锁了，这时候就造成线程1和线程2同时持有了锁，同时执行了上锁的业务代码，造成了线程不安全。

问题知道了，那锁续期和这个有啥关系呢？锁续期就是专门用来解决此问题的，那肯定不用我们手写了，Redisson已经为我们实现好了，Redisson内部有个watchDog(看门狗)的机制，那这个watchDog是如何解决的呢？他的核心原理是啥？别着急，现在知道了为什么需要锁续期后，再来看下Redisson的watchDog技术手段是如何实现锁续期这个功能的。

**原理**

**核心工作流程是定时监测业务是否执行结束，没结束的话在看你这个锁是不是快到期了（超过锁的三分之一时间，比如设置的9s过期，现在还剩6s到期），那就重新续期。这样防止如果业务代码没执行完，锁却过期了所带来的线程不安全问题。**

核心原理就是上面这么一段话，但是具体的实现细节还需要看一下，比如他是怎么定时监测的？为每一个方法都单独开一个线程吗？等等细节问题，我们需要剖析，我们先来看一个问题，什么时候开启watchDog？肯定是加锁的时候，然后开个watchDog为这个持有锁的线程进行定时监测是否需要续期，所以我们先来回顾下怎么加锁的？（这些上一篇都详细的分析完了，此处不在过多的解释细节）

```ini
RLock lock = redisson.getLock("myLock");
lock.lock();
```

lock()干了啥？

```java
private void lock(long leaseTime, TimeUnit unit, boolean interruptibly) throws InterruptedException {
    long threadId = Thread.currentThread().getId();
    Long ttl = tryAcquire(-1, leaseTime, unit, threadId);
    // 加锁成功
    if (ttl == null) {
        return;
    }
    // 加锁失败，while(true)等待重试。
}
```

可以看到lock主要是请求`tryAcquire(-1, -1, null, threadId)`来完成加锁逻辑，然后判断加锁成功与否，失败的话就重试。目前还没发现watchDog的机制，那我们继续追下去，看看如何加锁的？

```csharp
private Long tryAcquire(long waitTime, long leaseTime, TimeUnit unit, long threadId) {
    return get(tryAcquireAsync(waitTime, leaseTime, unit, threadId));
}
​
// watchDog机制在这里
// -1， -1， null，threadId
private <T> RFuture<Long> tryAcquireAsync(long waitTime, long leaseTime, TimeUnit unit, long threadId) {
    RFuture<Boolean> ttlRemainingFuture;
    // 省略一些请求lua加锁代码。之前都分析过。
​
    ttlRemainingFuture.onComplete((ttlRemaining, e) -> {
        // ttlRemaining为true代表加锁成功。
        if (ttlRemaining == null) {
                // 接下来是什么鬼逻辑？
            // leaseTime == -1就scheduleExpirationRenewal开启看门狗续期，
            // leaseTime != -1就不续期，只是把internalLockLeaseTime时间变成传进来的时间。
            // 这里疑点重重：
            // 1.什么时候leaseTime != -1？
            // 2.不是所有的lock()方法都有看门狗机制？
            if (leaseTime != -1) {
                internalLockLeaseTime = unit.toMillis(leaseTime);
            } else {
                scheduleExpirationRenewal(threadId);
            }
        }
    });
    return ttlRemainingFuture;
}
```

简化为如下：

```scss
lock() {
    1.加锁逻辑
    
    2.watchDog逻辑
    if (leaseTime != -1) {
        internalLockLeaseTime = unit.toMillis(leaseTime);
    } else {
        scheduleExpirationRenewal(threadId);
    }    
}
```

可以发现核心流程很简单，就是每次加锁都判断是否需要开启watchDog，这就奇怪了，为什么还要判断呢？首先我们可以发现**好像不是所有的lock()都有看门狗，因为看到条件判断leaseTime == -1的时候才开启看门狗线程，不等于-1的时候就没有这个机制。** 那什么时候不等于-1呢？

回答这个问题前我们可以先推测下-1是哪来的？是lock()入口默认带来的：

```csharp
@Override
public void lock() {
    try {
        // -1 !!!
        lock(-1, null, false);
    } catch (InterruptedException e) {
        throw new IllegalStateException();
    }
}
```

那我们是不是懂了？他绝对有带时间参数的lock()方法：

```csharp
@Override
public void lock(long leaseTime, TimeUnit unit) {
    try {
        lock(leaseTime, unit, false);
    } catch (InterruptedException e) {
        throw new IllegalStateException();
    }
}
```

**搜噶，原来用户加锁lock的时候可以自定义时间，这个时间是干嘛的？加锁成功后等待这个时间就过期，不管你业务是否执行完成，因为没有看门狗机制。比如你设置的1s，业务执行了2s，那中途锁过期了，不会续期，可能造成线程不安全。**

现在知道watchDog何时生效了，那继续看下他是怎么工作的吧～

上面可以发现续期的代码在这个方法里面：`scheduleExpirationRenewal(threadId);`，这个方法底层是靠`renewExpiration`来完成续期的，那一起来看看他干了些啥。

```scss
private void renewExpiration() {
    Timeout task = commandExecutor.getConnectionManager().newTimeout(new TimerTask() {
        @Override
        public void run(Timeout timeout) throws Exception {
            // 调用lua脚本进行续期
            RFuture<Boolean> future = renewExpirationAsync(threadId);
            future.onComplete((res, e) -> {
                // 报异常就移除key
                if (e != null) {
                    // 省略remove的代码
                }
                // 续期成功的话就调用自己，进行下一轮续期。
                if (res) {
                    renewExpiration();
                } else {
                    // 续期失败的话就取消续期，移除key等操作
                    cancelExpirationRenewal(null);
                }
            });
        }
        // 这里是个知识点，续期线程在过期时间达到三分之一的时候工作，比如9s过期时间，那么续期会在第3秒的时候工作，也就是还剩余6s的时候进行续期
    }, internalLockLeaseTime / 3, TimeUnit.MILLISECONDS);
    ee.setTimeout(task);
}
```

这段代码里有四个关键点：

* 续期核心lua脚本在renewExpirationAsync里
* 续期成功自己调用自己，也就是为下一次续期做准备
* 续期失败就取消续期，移除key等操作
* 续期的开始时间是超过过期时间的三分之一，比如9s过期时间，那么第3s的时候开始续期。

所以重点看下续期的lua源代码：

```typescript
protected RFuture<Boolean> renewExpirationAsync(long threadId) {
    return evalWriteAsync(getRawName(), LongCodec.INSTANCE, RedisCommands.EVAL_BOOLEAN,
            "if (redis.call('hexists', KEYS[1], ARGV[2]) == 1) then " +
                    "redis.call('pexpire', KEYS[1], ARGV[1]); " +
                    "return 1; " +
                    "end; " +
                    "return 0;",
            Collections.singletonList(getRawName()),
            internalLockLeaseTime, getLockName(threadId));
}
```

很简单，就是看当前线程有没有加锁`hexists, KEYS[1], ARGV[2]) == 1`，有加锁的话就代表业务线程还没执行完（也就是还没有释放锁），就给他的锁重新续期`pexpire', KEYS[1], ARGV[1]`，然后返回1，也就是true，没加锁的话返回0，也就是false。

那就是返回1就调用自己准备下一次续期：`renewExpiration();`，返回0就调用`cancelExpirationRenewal(null);`取消续期，删除key等操作。

总结为一张图：

![redisson-watchdog.png](https://p1-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/de0f1f0e9ddf48ebba5ead9d67efe1ae~tplv-k3u1fbpfcp-jj-mark:1600:0:0:0:q75.image#?w=1598&h=1228&s=165260&e=png&b=f4b87b) 有没有其他问题呢？我们讲解了加锁的时候开一个watchDog来监听过期时间，然后选择性续期。那你有没有想过以下两个问题：

1. 如果业务代码执行完了，锁被释放了。由于有watchDog的存在，又给重新续期了，导致了类似死循环，死锁了的情况，怎么办？

2. watchDog我们看到了是加锁的时候会开启，那他底层是什么呢？每次请求都开一个线程来维护这把锁吗？是调度线程池吗？（这道题来源于我朋友面试【中关村科金】的真题）

下面来一一解答这两个问题，先来看第一个问题，**第一个问题其实很容易想到答案，直接在释放锁的时候取消watchDog就好了**，源码如下：

```arduino
@Override
public RFuture<Void> unlockAsync(long threadId) {
    // 省略解锁代码
    
    future.onComplete((opStatus, e) -> {
        // 解锁后不管成功与否，都要立即取消watchDog，防止重新续期。
        cancelExpirationRenewal(threadId);
    });
    return result;
}
```

上面的问题清晰明了，再来看第二个问题，第二个问题个人认为是个不错的问题，如果说是为每个请求的线程都额外开辟一个线程来维护watchDog，那不会对性能有影响吗？他是怎么调度执行的，是JDK的调度线程池吗？下面来彻底揭开它。

核心就是这行代码：

```java
Timeout task = commandExecutor.getConnectionManager().newTimeout(new TimerTask() {
    // ...
}
```

前面`commandExecutor.getConnectionManager()`就是通过构造器初始化的一个管理器，核心在后面`newTimeout(new TimerTask())`，我们先来看看TimeTask是啥？

```java
public interface TimerTask {
    void run(Timeout timeout) throws Exception;
}
```

啥也不是，普普通通的一个接口，里面有个run方法，所以相当于提交个run方法给`newTimeout()`了，那核心肯定在`newTimeout()`方法了。

```arduino
public Timeout newTimeout(TimerTask task, long delay, TimeUnit unit) {
    // 省略了其他代码
    timer.newTimeout(task, delay, unit);
}
```

timer是啥？是全局变量：`io.netty.util.HashedWheelTimer`,这下清晰了，是netty的时间轮，并不是调度线程池，简单看下原理，因为这是netty内容，不会过多涉及到，但是核心我会说明白，话不多说，看源码：

```arduino
public Timeout newTimeout(TimerTask task, long delay, TimeUnit unit) {
    // 省略了其他代码
    start();
​
    HashedWheelTimeout timeout = new HashedWheelTimeout(this, task, deadline);
    timeouts.add(timeout);
    return timeout;
}
```

可以看到两步：`start()`和`timeouts.add(timeout)`，先看看`timeouts`是个啥：

```ini
private final Queue<HashedWheelTimeout> timeouts = PlatformDependent.newMpscQueue();
```

他是一个用于装载`HashedWheelTimeout`的Queue，队列。而`HashedWheelTimeout`里面包含task，所以很清晰了，就是一个用于承载任务的时间轮容器，那`start()`是干嘛的呢？

```csharp
public void start() {
    switch (WORKER_STATE_UPDATER.get(this)) {
        case WORKER_STATE_INIT:
            if (WORKER_STATE_UPDATER.compareAndSet(this, WORKER_STATE_INIT, WORKER_STATE_STARTED)) {
                workerThread.start();
            }
            break;
        case WORKER_STATE_STARTED:
            break;
        case WORKER_STATE_SHUTDOWN:
            throw new IllegalStateException("cannot be started once stopped");
        default:
            throw new Error("Invalid WorkerState");
    }
​
    // ...
}
```

很清晰明了，如果线程没开启，那就开线程去执行，如果线程开启了，那就break，直接执行任务就好了，所以整个时间轮只会开启一个线程，并不会浪费资源。

上面都是netty的知识点，至少我们应该明白**Redisson的watchDog机制底层不是调度线程池，而是直接用的netty时间轮。**

最后做个收尾，总结下～

# 二、总结

本篇的知识点很明确，只有一个：Redisson如何实现的锁续期。他的流程并不复杂，只是细节往往容易忽略，比如释放锁的时候要取消锁续期，不能为每次请求都开个线程来维护锁过期时间，这样性能会很低，所以采取了时间轮的方式。从此也可看出Redisson等框架的开源作者的功底之深厚。

总结下本篇文章需要注意的几点：

* watchDog并不是全部lock都生效，而是lock没设置过期时间的那些锁才会开启watchDog续期，没设置过期时间的话默认采取的是watchDog的30s过期时间。如果调用lock(time,unit)是不会开启watchDog线程续期的，是有可能造成线程不安全的。
* 续期是段lua脚本，保证了原子性。
* 续期线程会在锁过期时间超过三分之一的时候执行。
* 主要采取的是netty的时间轮来完成调度的，而不是线程池。

目前为止Redis作为分布式锁的整个加锁/解锁以及其他必要核心关键点的实现我们全都剖析完成了，也可以说到目前为止对Redis是如何实现分布式锁的这个话题并不陌生，从原理到源码实现都分析的一清二楚了，这些在平时开发中大多数情况下都够用了，但这些也是每个开发者最基础的东西，并不是闪光点，我们都知道JDK的锁不光有ReentrantLock这种简单粗暴的互斥锁，他还有读写锁、公平锁甚至还有CountDownLatch和Semaphore这种“共享锁”，这些我们Redis同样能实现，所以接下来我们会分析这些高级进阶的“锁”，别忘了，还有大名鼎鼎的RedLock红锁没有分析，下一篇我们先从Redis如何实现公平锁开始分析。