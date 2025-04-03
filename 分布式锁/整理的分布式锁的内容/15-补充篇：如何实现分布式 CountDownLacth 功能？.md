上一篇我们分析了如何利用Zookeeper实现一把分布式限流工具，也就是Curator里的Semaphore，然后留了个作业：如何用Redis实现一把分布式限流工具？此篇我们介绍下与Semaphore有一丢丢相似的CountDownLacth该如何设计与实现，首先我会介绍下什么是CountDownLacth，然后我会利用Redis该如何实现CountDownLacth作为切入点进行讲解，最后同样我会留个作业：如何用Zookeeper来实现CountDownLacth？话不多说，我们先来看下到底什么是CountDownLacth？

# 一、什么是CountDownLacth

CountDownLacth，中文叫闭锁。

通俗来讲就是设置一个锁数量，然后必须达到这么多数量之后才能进行下面的执行，否则一直阻塞。和JDK下的CountDownLatch含义一样。举个例子，比如：我想让三个线程都获取锁后再执行某个方法，那就可以让其他线程干活，每干完一个就给锁数量减1，当锁数量为0的时候，主线程不在阻塞，继续工作，否则主线程一直阻塞住。单机的CountDownLacth（也就是JDK里的CountDownLacth）很常用，比如：参数验证的时候，同时让多个线程去负责不同的参数验证，都验证完后才释放主线程执行业务逻辑。分布式的CountDownLacth我还没有用过，但本篇作为补充篇单独拿出来讲是因为可以学习下其中的设计思想，这样结合之前所学的知识，会发现Redis实现分布式锁或者CountDownLacth等也不是很复杂，自己都能设计，因为你掌握了核心思想。

现在需求明白了，就是设计一个分布式的CountDownLacth，那我们该如何实现呢？

# 二、方案设计

在实现之前，首先我们需要针对需求进行方案设计，其实CountDownLacth并不复杂，我们可以总结为如下几点：

1. 首先需要一个方法进行参数配置，也就是配置**需要等待多少个线程执行完后，主逻辑才能继续执行。**

2. 其次我们需要提供一个可以让线程阻塞等待的api，比如如下：

   ```java
   public String test() {
       xxx;
       await();
       yyy;
   }
   ```

   这段逻辑意味着xxx可以被允许执行，但是执行到await()的时候就会被阻塞住，无法继续执行yyy，那yyy啥时候能执行呢？就是当执行xxx的线程**都**释放后，yyy就可以继续执行了。上面的都字加粗了，是因为执行xxx的线程是多个，也就是我们第1步的参数配置。

3. 线程都执行完后才可以继续执行yyy，那么怎么才能知道执行完了？所以需要提供一个方法，这个方法就给第1步的参数配置值减一，直到为0后，yyy就能执行了，我们称减一的这个方法为countDwon()。

现在方案已经有了，那我们逐个实现就好了，所以接下来进入源码实现环节～

# 三、源码剖析

我们先来剖析第一个关键点，也就是配置**需要等待多少个线程执行完后，主逻辑才能继续执行。** 这个参数怎么设计？我们称这个方法为**trySetCount()：**

核心就是这段lua：

```typescript
public RFuture<Boolean> trySetCountAsync(long count) {
    "if redis.call('exists', KEYS[1]) == 0 then "
        + "redis.call('set', KEYS[1], ARGV[2]); "
        + "redis.call('publish', KEYS[2], ARGV[1]); "
        + "return 1 "
    + "else "
        + "return 0 "
    + "end",
}
```

逻辑很简单，我们直接总结下：

* 首先调用`exists, KEYS[1]`命令看看锁资源是否存在。
* 如果没有锁就设置锁`set, KEYS[1], ARGV[1]`，ARGV\[1\]就是传进来的数字，也就是需要执行多少次countDown才能让await不阻塞
* 如果有锁资源则返回0，不允许重复设置锁数量

![redisson-countdownlatch-trysetcount.png](https://p9-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/ec81ae255dec415395b68bc64c1e7e97~tplv-k3u1fbpfcp-jj-mark:1600:0:0:0:q75.image#?w=683&h=323&s=15477&e=png&b=fefefe)

接下来我们在看第2步：**需要提供一个可以让线程阻塞等待的api，我们称这个方法为await()：**

核心代码如下：

```java
// 获取的锁数量
public long getCount() {
    return get(getCountAsync());
}
​
public void await() throws InterruptedException {
    try {
        // 只要锁数量大于0，也就是代表countDown次数还没达到设置的次数，那就一直阻塞。
        while (getCount() > 0) {
            future.getNow().getLatch().await();
        }
    } finally {
        unsubscribe(future);
    }
}
```

这个方法很简单，比如你调用了这个await()方法，如果锁数量还是大于0的那就一直死循环阻塞，你方法await下面的逻辑无法得到执行。

那什么时候锁数量减少呢？锁数量是你通过trySetCount()设置的，然后每countDown()一次就减少一把锁。当减少为0的时候这个await不在阻塞。如下图：

![redisson-countdownlatch-await.png](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/630f8e7c9da5425a8638cc80771507e3~tplv-k3u1fbpfcp-jj-mark:1600:0:0:0:q75.image#?w=482&h=309&s=8329&e=png&b=ffffff)

上面也提到了：每countDown()一次就减少一把锁。当减少为0的时候这个await不在阻塞，那**countDown()方法怎么实现呢？**

也很简单粗暴，直接给值减一即可，Redis数字减法是有现成api的，它就是`decr`。所以源码很简单了，如下：

```java
public RFuture<Void> countDownAsync() {
    "local v = redis.call('decr', KEYS[1]);" +
    "if v <= 0 then redis.call('del', KEYS[1]) end;" +
    "if v == 0 then redis.call('publish', KEYS[2], ARGV[1]) end;"
}
```

![redisson-countdownlatch-countdown.png](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/d90dc0b755354696b60b391c06439795~tplv-k3u1fbpfcp-jj-mark:1600:0:0:0:q75.image#?w=536&h=503&s=18613&e=png&b=fefefe)

# 四、总结

* 先调用trySetCountAsync设置锁数量，控制多少个线程释放后，主线程才能继续执行。
* 然后调用await阻塞住，如果锁数量为0 了那就释放阻塞，程序继续往下执行。如果大于0，那就等待countDown方法给锁数量-1，一直减到0为止。
* 最后countDown其实就是给设置的锁数量-1，减到0的时候，await就不在阻塞了。

最后再用通俗易懂的语言描述下闭锁：

想象成上课场景：比如一个班级10个学生，那就设置trySetCount(10)，然后老师点名，每点到一个，就countDown()一次，这样10个都到了后，锁数量就是0了，老师就能开始讲课了。

# 五、补充：和Semaphore区别

有很多同学都容易把CountDownLatch和Semaphore两个概念搞混淆，包括面试的过程中也有面试官喜欢问：聊聊CountDownLatch和Semaphore。

这里最后用简短的语言总结下：

Semaphore是信号量，可以做限流，限制n个线程并发，释放一个线程后就又能进来一个新的线程。

CountDownLatch是闭锁，带有阻塞的功能，必须等到n个线程都执行完后，被阻塞的线程才能继续往下执行。

也就是说：**信号量是释放一个线程后，其他线程就能继续抢锁工作了。而闭锁是释放一个线程后，其他线程也不能进来抢占，必须等设置闭锁线程数量的线程都执行完后才能重新抢锁，且闭锁带阻塞功能，线程都执行完后会让阻塞的线程得到执行。**

同样，最后我再留一个作业，那就是Zookeeper也能实现分布式CountDownLacth，所以这里的作业是大家自己想一想Zookeeper该如何实现一把分布式的CountDownLacth呢？