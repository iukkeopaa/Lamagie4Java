上一篇我们讲的是实现分布式读写锁的核心思想以及核心原理，这篇文章我们会剖析Redisson是如何实现分布式读写锁的，我们将分析读锁和写锁分别是如何加锁解锁的以及读写锁组合起来的的三个特点：读读共享、读写互斥、写写互斥是如何实现的，当然我们还会剖析读写锁的WatchDog是怎么玩的，此篇文章和上一篇文章属于承上启下，上一篇讲思想，这一篇讲落地实现。我们先来看看Redisson分布式读写锁是如何加锁的？

# 一、加锁

分布式读写锁的加锁分为读锁加锁和写锁加锁两种，我们先来看看读锁如何加锁。

## 1\. 读锁加锁

原理我们上一篇都分析完了，再简单回顾下，其实就是三个核心：

0. 锁的结构采取hash类型来存储。
1. hash里要有个mode元素，取值为read或write，标记此把锁是读锁还是写锁，这个mode属性是实现读读共享、读写互斥、写写互斥必不可少的条件之一。
2. 因为读读共享的特性，所以一个hash里能有多把读锁，所以需要为每一把读锁单独维护过期时间，因此每把读锁都要单独创建一个key来维护过期时间，也就是我们上篇说的timeoutKey。

这三个核心上一篇剖析的淋漓尽致了，我们这里又重新拿出来回顾了一下。下面开始真正的Redisson分布式读写锁的读锁加锁流程。

在开始之前先想个问题：我们怎么知道当前有没有锁呢？可以直接获取hash结构里的mode元素，看看是否有值就知道有没有锁了，那为什么要获取mode呢？因为别忘了有读读共享的特点，我们拿出来mode不仅可以根据是否有值来判断是否有锁，还可以根据值来判断是读锁还是写锁，如果是读锁（read）那就可以继续加读锁，以此实现读读共享的特点。

如下是`org.redisson.RedissonReadLock#tryLockInnerAsync`的核心源码，也就是读锁加锁的核心源码段：

```ini
"local mode = redis.call('hget', KEYS[1], 'mode'); " +
"if (mode == false) then " +
  "redis.call('hset', KEYS[1], 'mode', 'read'); " +
  "redis.call('hset', KEYS[1], ARGV[2], 1); " +
  "redis.call('set', KEYS[2] .. ':1', 1); " +
  "redis.call('pexpire', KEYS[2] .. ':1', ARGV[1]); " +
  "redis.call('pexpire', KEYS[1], ARGV[1]); " +
  "return nil; " +
"end; " +
```

很简单，第一句lua就是判断有没有锁（没有mode的话就代表read和write都没有，这不就是无锁嘛），假设无锁，然后就执行了一堆lua命令，下面我们分段分析下这些lua，先来看前两个hset命令：

```arduino
'hset', KEYS[1], 'mode', 'read'
'hset', KEYS[1], ARGV[2], 1
```

这不就是如下吗？

```arduino
"readWriteLock": {
    "mode": read,
    "uuid:threadId1": 1
}
```

可以发现和我们上一篇讲的原理一模一样。就是锁放到hash里，增加mode标记是读锁还是写锁。

接下来再来看第后三个命令：

```arduino
'set', KEYS[2] .. ':1', 1
'pexpire', KEYS[2] .. ':1', ARGV[1]
'pexpire', KEYS[1], ARGV[1]
```

第一个set命令就是我们上一篇说的timeoutKey，为每把读锁单独维护过期时间用的，我们上面说了因为读读共享的特性，所以需要区分出来每把锁的过期时间，就单独搞了个string类型的key来表示单独的锁。最后两个pexpire命令就是续期了（设置超时时间），相当于：

```ruby
pexpire readWriteLock 30000
pexpire {readWriteLock}:uuid:threadId1:rwlock_timeout 30000
```

到这里读锁就分析完了，我们用一个简单的流程图还更好的说明下：

![读写锁-加读锁.png](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/0c708315472c4351a07f76299bf575d4~tplv-k3u1fbpfcp-jj-mark:1600:0:0:0:q75.image#?w=1090&h=652&s=81053&e=png&b=ffffff) 接下来再看看写锁是如何加锁的？

## 2\. 写锁加锁

写锁就太简单了，就是直接在hash结构里追加一个锁，然后加个mode=write的属性标记是写锁。原理我们在上一篇也讲解过了，直接看代码吧，实在是简单。

代码在`org.redisson.RedissonWriteLock#tryLockInnerAsync`中：

```ini
"local mode = redis.call('hget', KEYS[1], 'mode'); " +
"if (mode == false) then " +
      "redis.call('hset', KEYS[1], 'mode', 'write'); " +
      "redis.call('hset', KEYS[1], ARGV[2], 1); " +
      "redis.call('pexpire', KEYS[1], ARGV[1]); " +
      "return nil; " +
  "end; " +
```

很简单，就是判断有没有锁，没锁的话直接在redis的hash数据结构里hset一个，然后设置过期时间。

```arduino
"readWriteLock": {
    "mode": write,
    "uuid:threadId": 1
}
```

![读写锁-加写锁.png](https://p9-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/d7d50da9aa004015a20a247754213255~tplv-k3u1fbpfcp-jj-mark:1600:0:0:0:q75.image#?w=1088&h=474&s=53164&e=png&b=ffffff)

现在读锁和写锁的加锁流程大家已经一清二楚了，那他们组合到一起会有三个特点：读读共享、读写互斥、写写互斥。那这三个特点是如何实现的呢？下面就看来下～

# 二、读写组合

读写组合有三种，我们先从读读共享看起，看看读锁和读锁是如何做到共享的？

## 1\. 读读共享

回顾上文，**读读共享：** 我们有了mode这个字段，它代表锁的类型，那我们判断当前锁是不是读锁（mode=read），如果是读锁，那就是可共享的，如果再有读锁进来的话直接在hash里新增就完事了。这就是读读共享。

原理流程搞清楚了，接下来直接看源码：`org.redisson.RedissonReadLock#tryLockInnerAsync`

```ini
"if (mode == 'read') or (mode == 'write' and redis.call('hexists', KEYS[1], ARGV[3]) == 1) then " +
  "local ind = redis.call('hincrby', KEYS[1], ARGV[2], 1); " + 
  "local key = KEYS[2] .. ':' .. ind;" +
  "redis.call('set', key, 1); " +
  "redis.call('pexpire', key, ARGV[1]); " +
  "local remainTime = redis.call('pttl', KEYS[1]); " +
  "redis.call('pexpire', KEYS[1], math.max(remainTime, ARGV[1])); " +
  "return nil; " +
"end;" +
```

也不难吧？就是判断当前锁是不是读锁（`if (mode == 'read')`），如果是读锁，那就是可共享的，直接执行lua。如果不是读锁，是写锁的话，那就看这个写锁是不是自己(`or (mode == 'write' and redis.call('hexists', KEYS[1], ARGV[3]) == 1)`)，如果是自己的话，也执行lua。

lua的逻辑如下：

首先hincrby，这个命令属于hash数据结构，如果有key的话自增1，没有key的话会放到hash里，我们这里是读读共享的场景，所以假设2个不同的线程加锁，变成了如下：

```arduino
"readWriteLock": {
    "mode": read,
    "uuid:threadId1": 1,
    "uuid:threadId2": 1
}
```

然后`set key 1`和`pexpire key`就是我们说的那个读锁需要自己管理加锁时间的timeoutKey。最后给最外层的hashKey续期。

![读写锁-读读共享.png](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/f075bc1b325b4025b7a19287cbd0688b~tplv-k3u1fbpfcp-jj-mark:1600:0:0:0:q75.image#?w=1232&h=1116&s=138651&e=png&b=fdfdfd)

并不复杂，逻辑清晰易懂。我们在看下一个：读写互斥是如何实现的？

## 2\. 读写互斥

读写互斥这里是两种情况：

* 先加读锁，再加写锁是怎么互斥的？
* 先加写锁，再加读锁是怎么互斥的？

**先看第一种情况：**

先加读锁，再加写锁是怎么互斥的？我们看下加写锁的逻辑：

`org.redisson.RedissonWriteLock#tryLockInnerAsync`

```ini
"if (mode == 'write') then " +
 # 省略一堆逻辑
"end;"
"return redis.call('pttl', KEYS[1]);",
```

很明显了，只有锁是write的时候才会走逻辑，我们这里先加的读锁，是read，所以不符合if，直接return锁的剩余时间，所以加锁失败。然后外层判断返回值不是null就会等待重试（这个是之前讲过的了，和之前没区别）。

那为什么要判断是不是write呢？别忘了，我们的锁是可重入的，所以判断是不是有写锁，是的话会进一步判断是不是自己（重入）。

**在看第二种情况：**

先加写锁，再加读锁是怎么互斥的？我们看下加读锁的逻辑：

`org.redisson.RedissonReadLock#tryLockInnerAsync`

```ini
"if (mode == 'read') or (mode == 'write' and redis.call('hexists', KEYS[1], ARGV[3]) == 1) then " +
  # 省略一堆逻辑
"end;" +
"return redis.call('pttl', KEYS[1]);",
```

也很明显了，先判断是不是read，我们这里先加的写锁，是write，所以不符合。再看or的条件，是不是write，我们这里是write，符合。那是不是自己？明显不是，所以又是返回了锁的剩余时间，最外层等待重试，和之前逻辑一样了。

读写互斥的两种情况依然很清晰易懂，没有什么复杂的逻辑。

看到这里，大家肯定都能猜到写写互斥是怎么做的了，因为逻辑都差不多，都是根据mode来判断，一起来看下吧，看看写写互斥是怎么玩的？

## 3\. 写写互斥

自己想想也能想得明白，如果有写锁了且不是当前线程上的锁那不就是互斥嘛。

直接看源码吧：`org.redisson.RedissonWriteLock#tryLockInnerAsync`

```ini
"local mode = redis.call('hget', KEYS[1], 'mode'); " +
"if (mode == false) then " +
    # 加锁的逻辑
"end; " +
"if (mode == 'write') then " +
  "if (redis.call('hexists', KEYS[1], ARGV[2]) == 1) then " +
    # 可重入的逻辑
  "end; " +
"end;" +
"return redis.call('pttl', KEYS[1]);",
```

这是写锁的全部逻辑，就是判断有没有锁，没锁则加锁，有锁就判断是不是写锁，是写锁继续判断是不是自己，如果不是自己则返回锁剩余时间(`redis.call('pttl', KEYS[1])`)，然后外层会等待重试。如果是自己的话就代表是重入。

现在读读共享、读写互斥、写写互斥三个特点我们都分析完了，核心原理就是一句话：**有了锁类型（mode），那直接根据当前锁的类型是读锁还是写锁来控制是否可以共享即可。** 都是最基础的判断逻辑，没任何复杂逻辑。

到这里我们才叫真正的分析完了读锁和写锁的加锁流程，他们是如何加锁的以及是如何共享/互斥的都分析的很透彻了，但是我们知道Redis作为分布式锁的时候是需要配合watchDog的，那接下来我们看看读写锁的watchDog在Redisson里是如何实现的呢？

# 三、watchDog怎么工作的？

分布式读写锁的watchDog也分为两种：读锁的和写锁的。我们上一篇也分析过，写锁的watchDog很简单，和之前分析的普通互斥锁的watchDog的原理一模一样，所以此处不在重复性的去剖析，不清楚的可以看看前面几篇文章有一篇单独讲解watchDog的，此篇写锁的watchDog和之前那篇的实现原理是一模一样的。

我们把重点放到读锁的watchDog上，看看Redisson是如何实现的。

先回顾下上篇文章读锁实现watchDog的核心思想。

其实就一个关键点：**读锁的watchDog不仅要续期hashKey，还要单独为每个线程进行续期。** 也就是我们之前说的timeoutKey！一起看下源码吧：

`org.redisson.RedissonReadLock#renewExpirationAsync`

```ini
"redis.call('pexpire', KEYS[1], ARGV[1]); " +
 "if (redis.call('hlen', KEYS[1]) > 1) then " +
   "local keys = redis.call('hkeys', KEYS[1]); " + 
        "for n, key in ipairs(keys) do " + 
            "counter = tonumber(redis.call('hget', KEYS[1], key)); " + 
            "if type(counter) == 'number' then " + 
                "for i=counter, 1, -1 do " + 
                    "redis.call('pexpire', KEYS[2] .. ':' .. key .. ':rwlock_timeout:' .. i, ARGV[1]); " + 
                "end; " + 
            "end; " + 
    "end; " +
  "end; " +            
"return 1; " +
```

看起来很麻烦，其实核心很简单，就是上篇讲的原理那样，给两个key进行续期：一个是hash的key（`pexpire', KEYS[1], ARGV[1])`），一个是每个线程单独自己的timeoutKey（`pexpire', KEYS[2] .. ':' .. key .. ':rwlock_timeout:' .. i, ARGV[1]`）。

![读写锁-watchDog.png](https://p1-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/778e4b4d0f234e029fd35847693e86dc~tplv-k3u1fbpfcp-jj-mark:1600:0:0:0:q75.image#?w=750&h=338&s=34548&e=png&b=ffffff) watchDog讲完了，那么读写锁的锁重入是怎么做的？

# 四、可重入锁是怎么做的？

依然是分为两种情况：读锁和写锁。先来分析下写锁如何做到锁重入。

**写锁**

我们先结合之前所学的知识来想一想写锁要想实现可重入该怎么做？

首先**写互斥**，所以要想可重入，必须已有写锁且写锁是当前线程添加的，这时候才可以重入。重入的逻辑也很简单了，直接锁状态加1（也就是value+1），然后重新续期就完事了。

看下源码：`org.redisson.RedissonWriteLock#tryLockInnerAsync`

```ini
"if (mode == 'write') then " +
    "if (redis.call('hexists', KEYS[1], ARGV[2]) == 1) then " +
        "redis.call('hincrby', KEYS[1], ARGV[2], 1); " + 
        "local currentExpire = redis.call('pttl', KEYS[1]); " +
        "redis.call('pexpire', KEYS[1], currentExpire + ARGV[1]); " +
        "return nil; " +
    "end; " +
"end;" +
```

首先判断是不是有写锁，因为只能是有写锁且写锁是自己的情况才叫重入，所以再次判断写锁是不是自己（`redis.call('hexists', KEYS[1], ARGV[2]) == 1`），如果是自己的话就简单了，hincrby自增1且pexpire续期。比如如下：

```perl
"readWriteLock": {
    "mode": write,
    # 代表重入了1次。加锁成功是1，重入一次是1+1，也就是2
    "uuid:threadId": 2
}
```

写锁可重入分析完了，再来看看读锁是如何做到可重入的？

**读锁**

我们还是先来分析下如果让我们自己设计的话，读锁该怎么实现可重入？

首先读读共享、读写互斥。所以我们肯定要先判断是不是读锁，是读锁的话直接执行逻辑就完了，因为即使持有锁的线程不是当前线程也无所谓，打不了就新增一把读锁进去，读读共享的，这一点我们在上面分析过。如果不是读锁而是写锁，那我们就要判断持有写锁的线程是不是当前请求线程，如果是的话，那也允许加锁，相当于“锁降级”。所以就这么两种情况，下面是核心源码：

`org.redisson.RedissonReadLock#tryLockInnerAsync`

```ini
"if (mode == 'read') or (mode == 'write' and redis.call('hexists', KEYS[1], ARGV[3]) == 1) then " +
    "local ind = redis.call('hincrby', KEYS[1], ARGV[2], 1); " + 
    "local key = KEYS[2] .. ':' .. ind;" +
    "redis.call('set', key, 1); " +
    "redis.call('pexpire', key, ARGV[1]); " +
    "local remainTime = redis.call('pttl', KEYS[1]); " +
    "redis.call('pexpire', KEYS[1], math.max(remainTime, ARGV[1])); " +
    "return nil; " +
"end;" +
```

0. 判断当前持有锁的是不是读锁，因为写互斥，所以判断是不是read，是的话就自增1（当前线程没持有锁的话就新增一把锁进去，读读共享）且续期。比如如下：

```arduino
"readWriteLock": {
    "mode": read,
    "uuid:threadId1": 2,
    "uuid:threadId2": 1
}
```

2. 不是读锁的话就看看写锁是不是自己，是自己加的写锁的话也允许再加一把读锁，类似“锁降级”。比如如下：

```arduino
"readWriteLock": {
    "mode": write,
    "uuid:threadId1:write": 1,
    "uuid:threadId1": 1
}
```

**一言以蔽之：其实核心就是判断当前请求的线程和持有锁的线程是不是同一个，是的话就代表重入，重入的方式就是给value+1。**

到目前为止，分布式读写锁的加锁、互斥、共享、重入以及watchDog我们都分析完了，最后我们在分析下解锁，看看解锁需要经过哪些流程。

# 五、解锁

前面说了这么多了，大家肯定知道一点，就是不管是加锁还是watchDog还是重入等等，都需要考虑两点：读锁和写锁。因为读写锁是两把锁，所以必须单独分开考虑。解锁也不例外，也是分为两种：读锁解锁和写锁解锁。先来看看读锁解锁的流程。

## 1\. 读锁解锁

首先想一下：能直接del掉hash吗？

肯定不能，因为读读共享，直接del hash的话相当于释放了全部锁。所以我们需要删除hash里自己的那把锁，删之前肯定要看看自己是否加过锁，相当于安全检查嘛，你写业务代码还需要判断是不是存在呢，何况框架代码了。

**安全检查**

`org.redisson.RedissonReadLock#unlockInnerAsync`

```ini
"local mode = redis.call('hget', KEYS[1], 'mode'); " +
"if (mode == false) then " +
    "redis.call('publish', KEYS[2], ARGV[1]); " +
    "return 1; " +
"end; " +
"local lockExists = redis.call('hexists', KEYS[1], ARGV[2]); " +
"if (lockExists == 0) then " +
    "return nil;" +
"end; " +
```

先看看有没有锁，没锁的话直接return。有锁就再看看是不是自己（因为读读共享，所以可能有多个读锁的情况，所以需要看释放的时候是不是自己持有锁），如果自己没锁（`if (lockExists == 0)`），直接return。

比如目前有两把锁，如下：

```arduino
"readWriteLock": {
    "mode": read,
    "uuid:threadId1": 2,
    "uuid:threadId2": 3
}
```

但是你`uuid:threadId3`来释放锁了，哪有啊？你不能释放别人的啊，所以没找到锁，直接return。

好了，上面只是安全检查，接下来才是真正的删除操作，也就是真正的释放锁操作。

**续期/删除**

能直接del嘛？肯定不能，因为有锁重入的情况，如果发生重入的话，需要先给重入次数减1，直到没重入的时候才能del释放锁。直接看代码：

`org.redisson.RedissonReadLock#unlockInnerAsync`

```ini
"local counter = redis.call('hincrby', KEYS[1], ARGV[2], -1); " + 
"if (counter == 0) then " +
    "redis.call('hdel', KEYS[1], ARGV[2]); " + 
"end;" +
"redis.call('del', KEYS[3] .. ':' .. (counter+1)); " +
```

先给锁值减1，然后看是不是0了，是0就代表没重入了，可以删了。所以hdel从hash里删除。比如有如下锁：

```arduino
"readWriteLock": {
    "mode": read,
    "uuid:threadId1": 1,
    "uuid:threadId2": 3
}
```

假设要删除的是`uuid:threadId1`，那么hdel删除完成后就变成了：

```arduino
"readWriteLock": {
    "mode": read,
    "uuid:threadId2": 3
}
```

最后还有一个`del KEYS[3]`，也就是不管你还有没有重入次数，我都删除这个key，这个是啥？我们每次加读锁的时候不仅在hash里hset一个，还有个单独的timeoutKey来维护过期时间，这个key每重入一次都会生成一个，所以这里释放锁，那肯定要把这个移除掉。这个del就是删除这个timeoutKey的。

还没完，假设我们锁都释放完了，那就剩下如下结构了：

```arduino
"readWriteLock": {
    "mode": read
}
```

这样的是没任何意义的，所以我们也需要删除这个hash结构，所以也很简单，我们只需要判断hash里的元素是不是大于1就可以了，不大于1的话那肯定就剩下`mode:read`这个元素了，直接del hash就好了，代码如下：

```ini
"if (redis.call('hlen', KEYS[1]) > 1) then " +
    // ...
"end; " +
    
"redis.call('del', KEYS[1]); " +
"redis.call('publish', KEYS[2], ARGV[1]); " +
"return 1; ",
```

先hlen看长度是不是还大于1个，如果不大于1个了，那就是没任何锁了，直接删除hash的key，比如如下情况：

```arduino
"readWriteLock": {
    "mode": read
}
```

这情况肯定要`del KEYS[1]`，删除整个hash结构。因为锁都释放完了。

那如果hash里的元素大于1呢？那就代表还有其他锁，也就是读读共享嘛，你释放了一个，还有其他的，所以需要给hash结构续期，续期的值就是每把锁有效期的最大值。感兴趣的可以自己看看省略的那段代码，其实就是for循环遍历每一个timeoutKey（锁单独维护过期时长的那个key），**找到最大剩余过期时间，如果最大剩余过期时间大于0的，那就给hash的可以重新续期。** 就是干了这么一件事。

![读写锁-读锁释放.png](https://p9-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/1f28eeffe4194a1295dbc6ed8d88379b~tplv-k3u1fbpfcp-jj-mark:1600:0:0:0:q75.image#?w=854&h=1064&s=87437&e=png&b=ffffff) 接下来在看看本篇最后一个知识点：写锁如何解锁？

## 2\. 写锁解锁

写锁解锁思路和读锁解锁思路是一样的，都是两大部分：安全检查和续期/删除。直接看代码，这里跳过了安全检查的代码，直接看续期/删除部分。

**续期/删除**

`org.redisson.RedissonWriteLock#unlockInnerAsync`

```ini
"if (mode == 'write') then " +
    "local lockExists = redis.call('hexists', KEYS[1], ARGV[3]); " +
    "if (lockExists == 0) then " +
        "return nil;" +
    "else " +
        "local counter = redis.call('hincrby', KEYS[1], ARGV[3], -1); " +
        "if (counter > 0) then " +
            "redis.call('pexpire', KEYS[1], ARGV[2]); " +
            "return 0; " +
        "else " +
            "redis.call('hdel', KEYS[1], ARGV[3]); " +
            "if (redis.call('hlen', KEYS[1]) == 1) then " +
                "redis.call('del', KEYS[1]); " +
                "redis.call('publish', KEYS[2], ARGV[1]); " + 
            "else " +
                // has unlocked read-locks
                "redis.call('hset', KEYS[1], 'mode', 'read'); " +
            "end; " +
            "return 1; "+
        "end; " +
    "end; " +
"end; "
```

如果没有锁：

* 直接return nil。

如果有锁：

* 先给锁值-1，看看是不是大于0，大于0就代表锁重入，不能删除。重新续期。
* 如果不大于0，那就直接从hash里hdel删除掉这个锁。
* 删除锁后如果hash里没有其他锁了，那直接删除整个hash结构。
* 删除锁后如果hash里还有其他锁，那就把锁的模式换成read。

懵了？为啥互斥锁还需要看有没有其他锁？别忘了，前面刚说的，先加写锁，这时候允许自己再加一个读锁，比如如下：

```arduino
"readWriteLock": {
    "mode": write,
    "uuid:threadId1:write": 1,
    "uuid:threadId1": 1
}
```

写锁释放后，这时候hash里还有一把读锁存在，但是mode还是write，所以改变下mode：

```arduino
"readWriteLock": {
    "mode": read,
    "uuid:threadId1": 1
}
```

![读写锁-写锁释放.png](https://p9-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/160074c883ab42c3801de90131539c18~tplv-k3u1fbpfcp-jj-mark:1600:0:0:0:q75.image#?w=722&h=994&s=80032&e=png&b=ffffff)

# 六、总结

一定要先看上一篇文章，这两篇是承上启下的，上篇讲思想，这篇讲落地实现。简单总结下此篇的重点内容：

1. 我们先讲了读锁和写锁分别是如何加锁的，一句话概括：写锁加锁和之前讲的普通互斥锁是一样的，读锁加锁是允许共享的，所以多了个timeoutKey来维护每把共享锁的过期时间。
2. 我们又讲了三种组合方式（读读共享、读写互斥、写写互斥）是分别怎么实现的，一言以蔽之：有了锁类型（mode），那直接根据当前锁的类型是读锁还是写锁来控制是否可以共享即可。
3. 我们还讲了读写锁的watch以及可重入分别是怎么做的，写锁的和之前讲的普通互斥锁没区别，读锁的watchDog的核心在于需要给每个timeoutKey单独续期，而不能直接给hash续期，因为读读共享，一个hash里无数个锁，需要为每把共享锁单独维护过期时间。读锁的可重入也是一样的，hash里有多把锁，给每把锁的value自增1就是锁重入次数+1。
4. 最后我们讲解了解锁，大体流程就是判断还有没有锁，有锁的话就看看是不是重入，是重入就给重入次数减1，不是重入就删除这把锁（从hash里移除这个元素），如果删完后hash里没锁了，就删除hash结构，其中读锁还有个需要注意的细节就是读读共享，所以每次删除的时候都需要给hash续期为锁过期时间的最大值，防止其中途过期。

到目前为止我们对Redis如何实现分布式锁应该可以说手到擒来了，不管是公平锁还是读写锁，思想都一样，都是基于基础锁的增强，我们还有一个大名鼎鼎的锁没有讲解，那就是RedLock红锁，但是在讲解红锁之前我们还需要看个东西：联锁，因为他和红锁紧密相连，下一篇我们先来看看什么是联锁。