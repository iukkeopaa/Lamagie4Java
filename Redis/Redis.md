## Redis_6为什么引入了多线程

在Redis 6.0中引入多线程的主要原因是为了提高其网络处理的效率，尤其是在网络 /0 上。以前版本的Redis是纯
粹的单线程模式，这主要限制在网络请求的读写上。Redis本身的命令执行是非常快的，但在处理大量小请求时，
网络通信尤其是数据发送和接受的处理可能会成为瓶颈。

以下是Redis 6.0引入多线程的一些主要原因和优势:

1. 网络I/0瓶颈:在之前的纯单线程版本中，如果有非常多的小请求需要处理，网络I/0(数据包的解析和写入
等)可能会限制整体吞吐量。通过引入多线程，Redis能够并行化网络数据的读写，从而克服这一瓶颈
2. 提升带宽利用率:通过多线程处理，可以更好地利用网络带宽，尤其是在大数据量传输的情形下，能够显著减
少单线程造成的等待时间。
3. 降低延迟:并行处理网络请求可以减少请求排队的时间，从而降低请求的平均延迟，提高Redis在高并发场景
下的响应速度。
4. 充分利用多核CPU:现代服务器通常具有多核CPU架构，单线程模式无法充分利用多核的优势。引入多线程
后，可以更好地利用多核资源，提升Redis的整体吞吐量。
5. 略微改变的工作模型:Redis 6.0中，多线程主要用来处理请求的网络 V0，而实际的命令执行仍然在单线程
中进行。这种设计避免了多线程对数据一致性和命令原子性的影响，同时提升了网络处理的效率。

通过在保持核心单线程模型的情况下引入多线程来处理I/0，Redis 6.0能够在一定程度上提升性能，特别是在网络
通信方面，而不引入多线程代码中常见的数据并发问题。这个设计让Redis可以在拥有更多资源的现代硬件上发挥
更大潜力。

## Redis中key过期了一定会立即删除吗
在 Redis 中，key 过期了不一定会立即删除，Redis 采用了多种策略来处理过期 key 的删除，主要有以下三种策略，下面分别介绍：

### 定时删除



- **原理**：在设置 key 的过期时间时，同时创建一个定时器，当过期时间到达时，立即执行对该 key 的删除操作。
- 优缺点
    - **优点**：可以保证过期 key 能够被尽快删除，释放内存空间，使内存利用率较高。
    - **缺点**：每个设置了过期时间的 key 都需要创建一个定时器，会占用大量的 CPU 资源，尤其是在有大量过期 key 的情况下，会严重影响 Redis 的性能。
- **实际应用情况**：由于其对 CPU 资源的高消耗，Redis 并没有采用定时删除策略作为主要的过期 key 删除方式。

### 惰性删除



- **原理**：不主动删除过期 key，而是在每次访问一个 key 时，检查该 key 是否过期，如果过期则删除该 key 并返回空结果。
- 优缺点
    - **优点**：对 CPU 资源友好，只有在访问 key 时才进行过期检查和删除操作，不会额外消耗 CPU 去主动删除过期 key。
    - **缺点**：如果一个过期 key 一直没有被访问，它会一直占用内存，可能导致内存空间被大量无用的过期 key 占用，造成内存浪费。
- **实际应用情况**：Redis 采用了惰性删除策略作为过期 key 删除的一种方式。例如，当客户端执行 `GET`、`SET` 等命令访问某个 key 时，Redis 会先检查该 key 是否过期，如果过期就会将其删除。

### 定期删除



- **原理**：Redis 会每隔一段时间（默认是 100ms）随机抽取一部分设置了过期时间的 key 进行检查，将其中过期的 key 删除。
- 优缺点
    - **优点**：通过定期检查和删除过期 key，可以在一定程度上控制过期 key 占用的内存空间，避免内存过度浪费。同时，由于是随机抽取部分 key 进行检查，对 CPU 资源的消耗相对较小。
    - **缺点**：由于是随机抽取部分 key 进行检查，可能会有一些过期 key 没有被及时发现和删除，仍然会占用一定的内存空间。而且定期删除的频率和抽取的 key 数量等参数需要合理设置，如果设置不当，可能会导致内存清理不及时或 CPU 资源消耗过大。
- **实际应用情况**：Redis 将定期删除策略与惰性删除策略结合使用。定期删除可以在一定程度上弥补惰性删除可能导致的内存浪费问题，而惰性删除又可以减少定期删除对 CPU 资源的过度消耗。


## Redis为什么把所有数据都放内存

Redis将所有数据放到内存中的主要原因是为了提供高性能的读写操作。

以下是几个主要的原因:

1. **高速读写:**内存访问速度快，相比于磁盘和数据库，内存操作速度更快，能够更迅速地响应读写请求。将
数据存储在内存中可以大大缩短读写的延迟，提高系统的响应速度和吞量
2. **简单数据结构:**Redis使用简单的数据结构来存储数据，如字符串、列表、哈希、集合和有序集合等。这
些数据结构直接映射到内存，不需要进行复杂的数据转换和序列化操作，提高了读写效率。
3. **数据持久化:**尽管Redis将数据存储在内存中，但它也支持数据的持久化。通过使用RDB快照和AOF日志
两种方式，Redis可以将内存中的数据定期或实时写入磁盘，以保证数据的持久性和安全性
需要注意的是，由于内存容量有限，Redis的内存管理也是需要考虑的。通过设置合适的数据过期策略、内存淘汰
策略和最大内存限制等措施，可以在保证高性能的同时，有效地管理内存使用。同时，Redis也可以通过集群和分
片等方式来扩展内存容量和提高系统的可用性和性能。
## Redis为什么这么快

Redis之所以快速的原因主要包括以下几点:
1. **内存存储:**Redis将数据存储在内存中，实现了快速的读写操作。
2. **单线程模型:**Redis采用单线程处理请求，避免了多线程的竞争和上下文切换开销。
3. **高效的数据结构:**Redis内部使用了高效的数据结构，如哈希表、跳跃表等，提供了快速的数据访问和操 自
作。
4. **异步10:**Redis利用异步I0来处理网络请求，能够同时处理多个请求，提高并发性能。
5. **事件驱动架构:**Redis基于事件驱动的模型，通过事件循环机制处理请求和操作，提高系统的效率
6. **优化的操作:**Redis对常用操作进行了优化，如批量操作和管道技术，减少了网络通信开销。
## Redis到底支不支持事务啊_

### Redis 事务的实现方式



Redis 通过 `MULTI`、`EXEC`、`DISCARD` 和 `WATCH` 这几个命令来实现事务功能：



- **`MULTI`**：用于开启一个事务块。当客户端执行 `MULTI` 命令后，后续的命令不会立即执行，而是被放入一个队列中，直到执行 `EXEC` 命令。
- **`EXEC`**：执行事务队列中的所有命令。当客户端执行 `EXEC` 命令时，Redis 会按照命令在队列中的顺序依次执行这些命令，并将执行结果返回给客户端。
- **`DISCARD`**：取消当前事务。如果在执行 `EXEC` 之前执行 `DISCARD` 命令，事务队列中的所有命令都会被清空，事务被取消。
- **`WATCH`**：用于实现乐观锁机制。客户端可以使用 `WATCH` 命令监视一个或多个键，在执行 `EXEC` 之前，如果被监视的键被其他客户端修改，那么整个事务会被放弃，`EXEC` 命令返回空结果。

### Redis 事务的特性



- 原子性
    - 在传统数据库中，事务通常具有严格的原子性，即事务中的所有操作要么全部成功，要么全部失败。而 Redis 事务的原子性在一定程度上有所不同。
    - 如果事务队列中的某个命令在执行过程中出现错误（如语法错误），Redis 会继续执行事务队列中的其他命令，已经执行的命令不会回滚。例如：













```plaintext
MULTI
SET key1 value1
INVALID_COMMAND  # 这是一个无效命令
SET key2 value2
EXEC
```



在这个例子中，`INVALID_COMMAND` 会导致错误，但 `SET key1 value1` 和 `SET key2 value2` 仍然会被执行。不过，如果在执行 `EXEC` 之前出现错误（如语法错误），事务不会执行，保证了一定程度的原子性。



- 一致性
    - Redis 事务可以保证数据的一致性。在事务执行过程中，Redis 会按照顺序依次执行事务队列中的命令，不会被其他客户端的操作打断，从而保证了数据在事务执行前后的一致性。
- 隔离性
    - Redis 是单线程执行命令的，在执行事务期间，不会有其他客户端的命令插入到事务队列中执行，因此事务具有一定的隔离性。但需要注意的是，Redis 不支持像传统数据库那样的多级别隔离机制。
- 持久性
    - Redis 事务的持久性取决于 Redis 的持久化配置。如果使用的是 RDB 持久化，在事务执行过程中，如果 Redis 发生崩溃，可能会导致部分事务数据丢失；如果使用的是 AOF 持久化，并且配置为 `always` 同步，那么事务的持久性可以得到较好的保证。
## Redis如何高效安全的遍历所有key

### 1. `KEYS` 命令



- 原理及用法
    - `KEYS` 命令用于查找所有符合给定模式的 key。其基本语法为 `KEYS pattern`，其中 `pattern` 是一个支持通配符的字符串，例如 `*` 表示匹配任意数量的任意字符，`?` 表示匹配单个任意字符。例如，使用 `KEYS *` 可以返回 Redis 中所有的 key。









```plaintext
KEYS *
```



- 缺点
    - **阻塞问题**：`KEYS` 命令是一个阻塞操作，当 Redis 中 key 的数量非常大时，执行 `KEYS` 命令会占用大量的 CPU 时间，在命令执行期间，Redis 无法处理其他客户端的请求，这会导致 Redis 服务在一段时间内不可用，影响系统的正常运行。
    - **内存问题**：`KEYS` 命令会一次性返回所有匹配的 key，当匹配的 key 数量很多时，会占用大量的内存，可能导致 Redis 内存溢出或客户端内存不足。
- 适用场景
    - 由于 `KEYS` 命令的阻塞特性，它不适合在生产环境中频繁使用。一般只在开发、测试环境中，或者在 Redis 数据量较小且对性能要求不高的情况下使用。

### 2. `SCAN` 命令



- 原理及用法
    - `SCAN` 命令是 Redis 2.8 版本引入的用于迭代遍历 key 的命令，它采用渐进式迭代的方式，不会阻塞 Redis 服务器。`SCAN` 命令的基本语法为 `SCAN cursor [MATCH pattern] [COUNT count]`，其中 `cursor` 是迭代游标，初始值为 0；`MATCH pattern` 是可选参数，用于指定匹配的模式；`COUNT count` 也是可选参数，用于指定每次迭代返回的 key 数量的近似值。
    - 每次执行 `SCAN` 命令时，Redis 会返回一个新的游标和一批匹配的 key。客户端需要使用新的游标继续下一次迭代，直到游标返回 0 表示迭代结束。例如：







```plaintext
SCAN 0 MATCH user:* COUNT 100
```



这个命令从游标 0 开始，匹配所有以 `user:` 开头的 key，每次迭代大约返回 100 个 key。



- 优点
    - **非阻塞**：`SCAN` 命令采用渐进式迭代的方式，每次只处理一部分 key，不会像 `KEYS` 命令那样一次性处理所有 key，因此不会阻塞 Redis 服务器，不会影响其他客户端的请求处理。
    - **内存友好**：`SCAN` 命令每次只返回一部分匹配的 key，不会一次性占用大量的内存，避免了内存溢出的问题。
- 缺点
    - **可能重复**：由于 `SCAN` 命令是渐进式迭代的，在迭代过程中，如果有新的 key 被添加或删除，可能会导致部分 key 被重复返回或遗漏。不过这种情况在大多数场景下是可以接受的。
- 适用场景
    - `SCAN` 命令适合在生产环境中使用，尤其是当 Redis 中 key 的数量非常大时，它可以在不影响 Redis 性能的前提下，高效安全地遍历所有 key。

### 3. 其他数据结构的遍历



- 如果是遍历哈希（Hash）、集合（Set）、有序集合（Sorted Set）等数据结构，可以使用对应的 `HSCAN`、`SSCAN`、`ZSCAN` 命令，它们的原理和 `SCAN` 命令类似，都是采用渐进式迭代的方式，避免阻塞 Redis 服务器。例如，遍历哈希表 `user:1` 的所有字段和值可以使用以下命令：









```plaintext
HSCAN user:1 0 COUNT 10
```


## Redis是单线程还是多线程

Redis 采用的是单线程模型。通常说得单线程，主要指的是 Redis 对外提供的键值存储服务的主要流程是单线程
的，即网络 I/0 和数据读写是由单个线程来完成的。这样设计可以避免多线程之间的竞争条件和锁开销，提高了
访问共享数据的效率。

然而，除了对外提供的键值存储服务，Redis 在某些功能上会使用额外的线程来执行，比如持久化、异步删除和集
群数据同步等。这些功能需要在后台执行，不参与主要的网络 V0 和数据处理。因此，严格来说，Redis 并不是完
全单线程。
## Redis的大Key问题如何解决

### 1. 拆分大 Key



- 拆分 Hash 类型大 Key
    - 对于 Hash 类型的大 Key，可以按照一定规则将其拆分成多个小的 Hash Key。例如，一个存储用户信息的大 Hash Key，包含了大量用户的详细信息，可以按照用户 ID 的范围进行拆分，每个小 Hash Key 只存储一部分用户的信息。
    - 示例代码（Python + Redis - Py）：



python











```python
import redis

r = redis.Redis(host='localhost', port=6379, db=0)

# 原大 Hash Key
big_hash_key = 'user_info'

# 拆分规则：按用户 ID 范围拆分
for i in range(10):
    start_id = i * 100
    end_id = (i + 1) * 100
    small_hash_key = f'user_info_{i}'
    for user_id in range(start_id, end_id):
        user_data = {f'field_{user_id}': f'value_{user_id}'}
        r.hmset(small_hash_key, user_data)
```







- 拆分 List 类型大 Key
    - 对于 List 类型的大 Key，可以将其拆分成多个小的 List Key。比如，一个存储大量消息的 List Key，可以按照时间范围或者消息类型进行拆分。
    - 示例代码（Python + Redis - Py）：



python











```python
import redis

r = redis.Redis(host='localhost', port=6379, db=0)

# 原大 List Key
big_list_key = 'messages'

# 拆分规则：按消息类型拆分
message_types = ['type1', 'type2', 'type3']
for message_type in message_types:
    small_list_key = f'messages_{message_type}'
    messages = r.lrange(big_list_key, 0, -1)
    for message in messages:
        if message_type in str(message):
            r.rpush(small_list_key, message)
```

### 2. 压缩数据



- 使用数据压缩算法
    - 对于一些文本类型的数据，可以使用压缩算法（如 Gzip、Snappy 等）对数据进行压缩后再存储到 Redis 中。在读取数据时，先进行解压缩操作。
    - 示例代码（Python + Redis - Py + Gzip）：



python











```python
import redis
import gzip

r = redis.Redis(host='localhost', port=6379, db=0)

data = "This is a large text data that needs to be compressed."
compressed_data = gzip.compress(data.encode())
key = 'compressed_data'
r.set(key, compressed_data)

# 读取数据并解压缩
stored_data = r.get(key)
decompressed_data = gzip.decompress(stored_data).decode()
print(decompressed_data)
```

### 3. 异步处理



- 使用异步线程或队列
    - 对于涉及大 Key 的操作（如删除大 Key），可以使用异步线程或消息队列来处理，避免阻塞 Redis 主线程。例如，使用 Python 的 `threading` 模块创建一个异步线程来删除大 Key。
    - 示例代码（Python + Redis - Py）：



python











```python
import redis
import threading

r = redis.Redis(host='localhost', port=6379, db=0)

def delete_big_key(key):
    r.delete(key)

big_key = 'big_key_to_delete'
thread = threading.Thread(target=delete_big_key, args=(big_key,))
thread.start()
```

### 4. 定期清理



- 设置过期时间
    - 对于一些临时数据或者有有效期的数据，为其设置合理的过期时间，让 Redis 自动清理这些数据，避免大 Key 长期占用内存。
    - 示例代码（Python + Redis - Py）：



python











```python
import redis

r = redis.Redis(host='localhost', port=6379, db=0)

key = 'temporary_data'
value = 'This is temporary data.'
r.set(key, value, ex=3600)  # 设置过期时间为 1 小时
```

### 5. 监控和预警



- 使用监控工具
    - 利用 Redis 自带的监控命令（如 `INFO`、`MEMORY USAGE` 等）或第三方监控工具（如 Redis - Exporter + Prometheus + Grafana）来实时监控 Redis 的内存使用情况和大 Key 的存在情况。当发现大 Key 或者内存使用超过阈值时，及时发出预警，以便及时处理。
    - 示例代码（使用 Redis - Py 获取内存使用信息）：



python











```python
import redis

r = redis.Redis(host='localhost', port=6379, db=0)
info = r.info()
memory_usage = info['used_memory']
print(f"Current Redis memory usage: {memory_usage} bytes")
```



通过以上方法，可以有效地解决 Redis 大 Key 问题，提高 Redis 的性能和稳定性。

## Redis的热Key问题如何解决

热 Key 问题指的是在 Redis 中，某个 Key 被大量的请求频繁访问，导致该 Key 所在的节点负载过高，甚至可能影响整个 Redis 集群的性能和稳定性。以下是一些解决 Redis 热 Key 问题的常见方法：

### 1. 缓存预热



- **原理**：在系统启动或者业务低谷期，将可能成为热 Key 的数据提前加载到 Redis 中，避免在业务高峰期大量请求同时去查询数据库并生成缓存，从而减少对单个热 Key 的集中访问。
- **实现方式**：可以编写一个定时任务或者启动脚本，在合适的时间将数据批量写入 Redis。例如，使用 Python 和 Redis - Py 库：



python











```python
import redis

r = redis.Redis(host='localhost', port=6379, db=0)

# 模拟从数据库获取可能的热数据
hot_data = {
    'hot_key_1': 'value_1',
    'hot_key_2': 'value_2'
}

# 批量写入 Redis
for key, value in hot_data.items():
    r.set(key, value)
```

### 2. 复制热 Key



- **原理**：将热 Key 复制到多个 Redis 节点上，让多个节点共同承担对该热 Key 的访问压力。客户端在访问热 Key 时，随机选择一个节点进行访问。
- **实现方式**：在代码层面实现随机选择节点的逻辑。例如，使用 Python 和 Redis - Py 库连接多个 Redis 节点：



python











```python
import redis
import random

# 多个 Redis 节点的连接信息
redis_nodes = [
    {'host': 'node1', 'port': 6379},
    {'host': 'node2', 'port': 6379},
    {'host': 'node3', 'port': 6379}
]

# 随机选择一个节点
selected_node = random.choice(redis_nodes)
r = redis.Redis(host=selected_node['host'], port=selected_node['port'], db=0)

# 访问热 Key
hot_key = 'hot_key'
value = r.get(hot_key)
```

### 3. 本地缓存



- **原理**：在客户端应用程序中添加本地缓存（如 Guava Cache、Caffeine 等），对于频繁访问的热 Key，先从本地缓存中获取数据，如果本地缓存中没有，再去 Redis 中获取，并将数据存入本地缓存。这样可以减少对 Redis 中热 Key 的访问次数。
- **实现方式**：以 Java 和 Caffeine 为例：



java











```java
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import redis.clients.jedis.Jedis;

import java.util.concurrent.TimeUnit;

public class LocalCacheExample {
    private static final Cache<String, String> localCache = Caffeine.newBuilder()
           .expireAfterWrite(10, TimeUnit.MINUTES)
           .maximumSize(100)
           .build();

    public static String getHotKey(String key) {
        String value = localCache.getIfPresent(key);
        if (value == null) {
            Jedis jedis = new Jedis("localhost", 6379);
            value = jedis.get(key);
            if (value != null) {
                localCache.put(key, value);
            }
            jedis.close();
        }
        return value;
    }
}
```

### 4. 限流和熔断



- 限流
    - **原理**：对热 Key 的访问进行限流，控制单位时间内对该 Key 的请求数量，避免过多的请求集中在一个 Key 上，保护 Redis 节点不被压垮。
    - **实现方式**：可以使用令牌桶算法或者漏桶算法来实现限流。例如，使用 Redis 结合 Lua 脚本来实现简单的限流：



lua











```lua
-- 限流脚本
local key = KEYS[1]
local limit = tonumber(ARGV[1])
local current = tonumber(redis.call('get', key) or "0")
if current + 1 > limit then
    return 0
else
    redis.call('INCR', key)
    redis.call('expire', key, 1)
    return 1
end
```



在 Java 中调用该脚本：



java











```java
import redis.clients.jedis.Jedis;

public class RateLimiterExample {
    public static boolean isAllowed(Jedis jedis, String key, int limit) {
        String script = "local key = KEYS[1]\n" +
                "local limit = tonumber(ARGV[1])\n" +
                "local current = tonumber(redis.call('get', key) or \"0\")\n" +
                "if current + 1 > limit then\n" +
                "    return 0\n" +
                "else\n" +
                "    redis.call('INCR', key)\n" +
                "    redis.call('expire', key, 1)\n" +
                "    return 1\n" +
                "end";
        Object result = jedis.eval(script, 1, key, String.valueOf(limit));
        return (Long) result == 1;
    }
}
```



- 熔断
    - **原理**：当对热 Key 的访问出现异常（如响应时间过长、错误率过高）时，暂时切断对该 Key 的访问，避免影响其他业务。可以使用熔断器（如 Hystrix、Resilience4j 等）来实现。
    - **实现方式**：以 Java 和 Resilience4j 为例：



java











```java
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import redis.clients.jedis.Jedis;

import java.time.Duration;
import java.util.function.Supplier;

public class CircuitBreakerExample {
    public static String getHotKeyWithCircuitBreaker() {
        CircuitBreakerConfig config = CircuitBreakerConfig.custom()
               .failureRateThreshold(50)
               .waitDurationInOpenState(Duration.ofMillis(1000))
               .ringBufferSizeInHalfOpenState(10)
               .ringBufferSizeInClosedState(100)
               .build();
        CircuitBreakerRegistry registry = CircuitBreakerRegistry.of(config);
        CircuitBreaker circuitBreaker = registry.circuitBreaker("hotKeyCircuitBreaker");

        Supplier<String> supplier = CircuitBreaker.decorateSupplier(circuitBreaker, () -> {
            Jedis jedis = new Jedis("localhost", 6379);
            String value = jedis.get("hot_key");
            jedis.close();
            return value;
        });

        return supplier.get();
    }
}
```





### 5. 监控和预警



- **原理**：使用监控工具（如 Redis - Exporter + Prometheus + Grafana）实时监控 Redis 的各项指标，包括热 Key 的访问频率、响应时间等。当发现某个 Key 的访问频率异常高时，及时发出预警，以便采取相应的措施。
- **实现方式**：配置 Prometheus 监控 Redis - Exporter 收集的 Redis 指标，使用 Grafana 进行可视化展示，并设置告警规则。例如，当某个 Key 的访问频率超过 1000 次 / 秒时，触发告警。



通过以上多种方法的综合使用，可以有效地解决 Redis 热 Key 问题，提高 Redis 系统的性能和稳定性。



## 如何用_Redis_统计用户访问量

## 说说Redis的持久化机制

Redis 是一个内存数据库，为了防止数据丢失，它提供了两种主要的持久化机制：RDB（Redis Database）和 AOF（Append - Only File），以下为你详细介绍这两种机制：

### RDB 持久化

#### 原理



RDB 持久化是将 Redis 在某个时间点上的数据集快照保存到磁盘文件中。这个过程是通过创建一个二进制文件（通常后缀为 `.rdb`）来实现的，文件中包含了 Redis 数据库的所有键值对信息。当 Redis 重启时，会自动加载这个 RDB 文件，将数据恢复到内存中。

#### 触发方式



- 手动触发

  ：可以使用



  ```
  SAVE
  ```



或



  ```
  BGSAVE
  ```



命令手动触发 RDB 持久化。

- `SAVE` 命令：会阻塞 Redis 服务器进程，直到 RDB 文件创建完成。在阻塞期间，Redis 不能处理其他客户端的请求。
- `BGSAVE` 命令：会派生出一个子进程来负责创建 RDB 文件，主进程可以继续处理客户端请求。子进程创建 RDB 文件完成后会向主进程发送信号。

- **自动触发**：可以在 Redis 配置文件（`redis.conf`）中设置自动触发 RDB 持久化的条件。例如：



plaintext











```plaintext
save 900 1
save 300 10
save 60 10000
```



上述配置表示：在 900 秒内如果有 1 个键被修改，或者在 300 秒内有 10 个键被修改，又或者在 60 秒内有 10000 个键被修改，就会自动触发 `BGSAVE` 操作。

#### 优缺点



- 优点
    - **文件紧凑**：RDB 文件是一个紧凑的二进制文件，占用空间小，适合用于备份和灾难恢复。
    - **恢复速度快**：由于 RDB 文件包含了某个时间点上的完整数据集，所以在 Redis 重启时，加载 RDB 文件恢复数据的速度比 AOF 方式快。
- 缺点
    - **数据安全性低**：RDB 持久化是定期或手动触发的，在两次持久化之间如果发生故障，可能会丢失部分数据。
    - **fork 操作开销大**：在执行 `BGSAVE` 时，需要通过 `fork` 系统调用创建子进程，当数据集较大时，`fork` 操作会消耗较多的系统资源和时间。

### AOF 持久化

#### 原理



AOF 持久化是将 Redis 服务器接收到的每一个写命令追加到一个文件（通常后缀为 `.aof`）的末尾。当 Redis 重启时，会按照文件中记录的命令顺序依次执行，从而恢复数据。

#### 触发方式



AOF 持久化默认是关闭的，需要在 Redis 配置文件中开启：



plaintext











```plaintext
appendonly yes
```



同时，可以配置 AOF 文件的同步策略，通过 `appendfsync` 参数设置，有以下三种选项：



- `always`：每个写命令执行完后，都立即将 AOF 缓冲区中的内容同步到磁盘。这种方式数据安全性最高，但性能开销也最大，因为每次写操作都要进行磁盘 I/O。
- `everysec`：每秒将 AOF 缓冲区中的内容同步到磁盘。这是默认的同步策略，在性能和数据安全性之间取得了较好的平衡，即使发生故障，最多只会丢失 1 秒钟的数据。
- `no`：由操作系统决定何时将 AOF 缓冲区中的内容同步到磁盘。这种方式性能最好，但数据安全性最低，在系统崩溃时可能会丢失较多的数据。

#### 优缺点



- 优点
    - **数据安全性高**：可以通过配置不同的同步策略，保证数据的安全性。尤其是使用 `always` 同步策略时，几乎不会丢失数据。
    - **可读性好**：AOF 文件是一个文本文件，包含了所有的写命令，方便查看和分析。
- 缺点
    - **文件体积大**：由于 AOF 文件记录了每一个写命令，随着时间的推移，文件会越来越大，占用更多的磁盘空间。
    - **恢复速度慢**：在 Redis 重启时，需要依次执行 AOF 文件中的所有命令来恢复数据，相比 RDB 方式，恢复速度较慢。

### 混合持久化（Redis 4.0 及以上）



Redis 4.0 引入了混合持久化的方式，结合了 RDB 和 AOF 的优点。在进行 AOF 重写时，将 RDB 的快照内容和增量的 AOF 日志合并到一个新的 AOF 文件中。这样，在 Redis 重启时，先加载 RDB 快照快速恢复大部分数据，再执行增量的 AOF 日志来恢复剩余的数据，既保证了数据的安全性，又提高了恢复速度。



要开启混合持久化，需要在 Redis 配置文件中添加以下配置：



plaintext











```plaintext
aof - use - rdb - preamble yes
```







综上所述，RDB 和 AOF 持久化机制各有优缺点，可以根据实际需求选择合适的持久化方式，也可以同时使用两种方式来提高数据的安全性和恢复效率。



## 说说你对Redis操作原子性的理解

Redis 的操作是原子性的，这是因为 Redis 的每个命令都是以单线程的方式执行的，整个命令的执行过程是不可
中断的，要么全部执行成功，要么全部执行失败。

在 Redis 中，每个命令都会被转换成一个或多个底层操作，这些操作会基于数据结构的特定实现来执行。比如，
对于字符串类型，获取一个键值对、设置一个键值对等操作都是原子性的。在执行这些底层操作时，Redis 会使用
些技术来保证原子性，主要包括以下两点:

1. Redis 使用单线程模型，避免了多线程之间的竞争条件和锁开销，从而保证了操作的原子性,
2. Redis 在执行一些复杂的操作时，比如事务、Lua 脚本等，会将多个底层操作打包成一个原子性操作，这些底
层操作要么全部执行成功，要么全部执行失败，在事务和 Lua 脚本中，Redis 同时支持回滚操作，即当一些
命令执行成功，后面的命令出错时，Redis 可以自动撤销已经执行的命令。

因此，Redis 的操作是原子性的，这得益于 Redis 单线程模型和底层操作的实现方式。这种原子性操作保证了
Redis 能够提供高效和可靠的服务。

## Redis持久化?式有哪些？有什么区别？


### RDB

RDB持久化是把当前进程数据?成快照保存到硬盘的过程，触发RDB持久化过程分为?动触发和?动触发。

RDB?件是?个压缩的?进制?件，通过它可以还原某个时刻数据库的状态。由于RDB?件是保存在硬盘上的，所
以即使Redis崩溃或者退出，只要RDB?件存在，就可以?它来恢复还原数据库的状态。

?动触发分别对应save和bgsave命令:

- save命令：阻塞当前Redis服务器，直到RDB过程完成为?，对于内存?较?的实例会造成?时间阻塞，线上
环境不建议使?。
- bgsave命令：Redis进程执?fork操作创建?进程，RDB持久化过程由?进程负责，完成后?动结束。阻塞只
发?在fork阶段，?般时间很短。


以下场景会?动触发RDB持久化：

1. 使?save相关配置，如“save m n”。表示m秒内数据集存在n次修改时，?动触发bgsave。
2. 如果从节点执?全量复制操作，主节点?动执?bgsave?成RDB?件并发送给从节点
3. 执?debug reload命令重新加载Redis时，也会?动触发save操作
4. 默认情况下执?shutdown命令时，如果没有开启AOF持久化功能则?动执?bgsave。
### AOF

AOF（append only file）持久化：以独??志的?式记录每次写命令， 重启时再重新执?AOF?件中的命令达到
恢复数据的?的。AOF的主要作?是解决了数据持久化的实时性，?前已经是Redis持久化的主流?式。

AOF的?作流程操作：命令写? （append）、?件同步（sync）、?件重写（rewrite）、重启加载 （load）

流程如下：
1. 所有的写?命令会追加到aof_buf（缓冲区）中。
2. AOF缓冲区根据对应的策略向硬盘做同步操作。
3. 随着AOF?件越来越?，需要定期对AOF?件进?重写，达到压缩 的?的。
4. 当Redis服务器重启时，可以加载AOF?件进?数据恢复。
## 常见的缓存更新策略有哪几种

### 1. 缓存失效策略（Cache-Aside Pattern）



- 原理
  - 这是最常用的缓存更新策略，读写操作都由应用程序负责与缓存和数据库交互。
  - **读操作**：应用程序首先尝试从 Redis 缓存中读取数据，如果缓存命中，则直接返回缓存数据；如果缓存未命中，则从数据库中读取数据，将数据存入 Redis 缓存，并返回给应用程序。
  - **写操作**：应用程序先更新数据库中的数据，然后删除 Redis 缓存中的对应数据。这样，下次读取该数据时，会因为缓存未命中而从数据库中读取最新数据并更新缓存。

- **优点**：实现简单，缓存和数据库的一致性较好，能够保证读到的是最新数据。
- **缺点**：应用程序需要处理缓存和数据库的交互逻辑，增加了代码复杂度；在高并发场景下，可能会出现缓存击穿问题。


### 为什么是删除缓存不是更新缓存

1. 对服务端资源造成浪费: 删除 cache 更加直接，这是因为 cache 中存放的一些数据需要服务端经过
   大量的计算才能得出，会消耗服务端的资源，是一笔不小的开销。如果频繁修改 db，就能会导致需要
   频繁更新 cache，而 cache 中的数据可能都没有被访问到。
2. 产生数据不一致问题:并发场景下，更新 cache 产生数据不一致性问题的概率会更大

### 可以先删除缓存再更新数据呢

不能 会导致数据库和缓存数据不一致


举例:请求1先写数据 A，请求2随后读数据 A的话，就很有可能产生数据不一致性的问题。这个过程可 自
以简单描述为:

1.请求 1先把 cache 中的 A数据删除;
2.请求 2 从 db 中读取数据;
3.请求 1 再把 db 中的 A 数据更新。


这就会导致请求 2 读取到的是旧值。

### 现在我们再来分析一下 Cache Aside Pattern 的缺陷,

缺陷 1:首次请求数据一定不在 cache 的问题

解决办法:可以将热点数据可以提前放入 cache 中。

缺陷 2:写操作比较频繁的话导致 cache 中的数据会被频繁被删除，这样会影响缓存命中率。

解决办法:

数据库和缓存数据强一致场景:更新 db 的时候同样更新 cache，不过我们需要加一个锁/分布式锁来
保证更新 cache 的时候不存在线程安全问题,

可以短暂地允许数据库和缓存数据不一致的场景:为缓存数据设置一个合理的过期时间。过期时间越
短，数据一致性越高，但缓存命中率越低。需要根据具体的业务场景和数据访问式来权衡。
### 2. 读穿 / 写穿策略（Read-Through/Write-Through Pattern）



- 原理
  - **读穿策略**：应用程序将读请求发送给缓存，缓存负责检查数据是否存在。如果存在则直接返回；如果不存在，缓存会从数据库中读取数据，将数据存入缓存并返回给应用程序。
  - **写穿策略**：应用程序将写请求发送给缓存，缓存先将数据写入数据库，然后更新缓存中的数据。整个过程对应用程序是透明的，应用程序只与缓存交互。
- **优点**：应用程序代码简单，无需处理缓存和数据库的交互逻辑；缓存和数据库的一致性较高。
- **缺点**：缓存的实现复杂度较高，每次写操作都要同步更新数据库，会增加系统的响应时间和数据库的负载。

### 3. 写回策略（Write-Back Pattern）



- 原理
  - 应用程序将写请求发送给缓存，缓存立即更新数据，并标记该数据为 “脏数据”，而不立即将数据写入数据库。当缓存中的数据被替换或者达到一定条件（如缓存满了、定时触发等）时，缓存会将 “脏数据” 批量写入数据库。
  - 读操作与缓存失效策略类似，先从缓存中读取数据，如果缓存未命中则从数据库中读取并更新缓存。
- **优点**：写操作的响应速度快，因为不需要等待数据写入数据库；减少了数据库的写操作次数，降低了数据库的负载。
- **缺点**：缓存和数据库的一致性较差，在缓存中的 “脏数据” 还未写入数据库时，如果系统崩溃，可能会导致数据丢失；实现复杂度较高，需要处理 “脏数据” 的管理和同步问题。

### 4. 定时更新策略



- 原理
  - 定时任务定期从数据库中读取最新数据，更新 Redis 缓存中的对应数据。这种策略适用于数据更新频率较低、对数据实时性要求不高的场景。
















- **优点**：实现简单，对系统性能的影响较小。
- **缺点**：数据的实时性较差，在两次更新之间，缓存中的数据可能不是最新的。

## Redis 生产问题：内存满了怎么解决？

当 Redis 内存满了，会影响系统的正常运行，以下从调整内存配置、优化数据存储、清理过期数据、使用内存淘汰策略、持久化优化以及扩展集群等方面介绍解决办法：

### 1. 调整 Redis 内存配置



- 增加可用内存
  - 可以通过修改 Redis 配置文件 `redis.conf` 中的 `maxmemory` 参数来增加 Redis 实例的可用内存。例如，将其设置为更大的值，如 `maxmemory 4gb`，表示将 Redis 的最大内存使用量设置为 4GB。修改配置文件后，需要重启 Redis 服务使配置生效。
- 优化内存分配策略
  - Redis 提供了不同的内存分配器，如 jemalloc、tcmalloc 等。可以通过修改 `redis.conf` 中的 `malloc - lib` 参数来选择合适的内存分配器。例如，使用 `malloc - lib jemalloc` 来指定使用 jemalloc 作为内存分配器，不同的分配器在不同场景下可能有不同的性能表现。

### 2. 优化数据存储



- 使用高效的数据结构
  - 选择合适的数据结构可以显著减少内存使用。例如，对于存储对象类型的数据，如果使用 Hash 数据结构，相比为每个字段单独设置一个 Key - Value 对，可以节省大量内存。因为 Hash 数据结构在存储多个字段时，会有更紧凑的内存布局。
  - 示例代码（Python + Redis - Py）：




- 压缩数据
  - 对于一些文本类型的数据，可以使用压缩算法（如 Gzip、Snappy 等）对数据进行压缩后再存储到 Redis 中。在读取数据时，先进行解压缩操作。
  - 示例代码（Python + Redis - Py + Gzip）：



### 3. 清理过期数据



- 检查过期策略
  - 确保 Redis 的过期策略正常工作。Redis 采用惰性删除和定期删除相结合的策略来处理过期 Key。可以通过 `config get maxmemory - policy` 命令查看当前的过期策略配置。如果发现过期数据没有及时清理，可以检查是否存在配置问题。
- 手动清理
  - 使用 `SCAN` 命令遍历所有 Key，找出过期的 Key 并手动删除。例如，使用 Python 脚本结合 `SCAN` 命令来清理过期 Key：




### 4. 配置合理的内存淘汰策略



- 查看和修改策略

  - Redis 提供了多种内存淘汰策略，可以通过



    ```
    config get maxmemory - policy
    ```

     

    命令查看当前的策略，使用

     

    ```
    config set maxmemory - policy <policy>
    ```

     

    命令修改策略。常见的内存淘汰策略有：

    - `volatile - lru`：从设置了过期时间的 Key 中，移除最近最少使用的 Key。
    - `allkeys - lru`：从所有 Key 中，移除最近最少使用的 Key。
    - `volatile - random`：从设置了过期时间的 Key 中，随机移除一个 Key。
    - `allkeys - random`：从所有 Key 中，随机移除一个 Key。
    - `volatile - ttl`：从设置了过期时间的 Key 中，移除即将过期的 Key。

- 选择合适的策略

  - 根据业务需求选择合适的内存淘汰策略。如果业务对数据的时效性要求较高，可以选择 `volatile - ttl` 策略；如果希望优先淘汰不常用的数据，可以选择 `volatile - lru` 或 `allkeys - lru` 策略。

### 5. 持久化优化



- 调整持久化策略
  - 如果 Redis 开启了持久化功能（如 RDB 或 AOF），可以调整持久化策略以减少内存使用。例如，对于 RDB 持久化，可以减少自动触发 `BGSAVE` 的频率；对于 AOF 持久化，可以选择更合适的同步策略，如 `everysec` 而不是 `always`。
- AOF 重写
  - 定期执行 AOF 重写操作，以减少 AOF 文件的大小。可以使用 `BGREWRITEAOF` 命令手动触发 AOF 重写，也可以配置 Redis 自动触发 AOF 重写。

### 6. 扩展 Redis 集群



- 添加节点
  - 如果单个 Redis 实例的内存无法满足需求，可以考虑扩展 Redis 集群，添加更多的节点。通过集群分片的方式，将数据分散存储在多个节点上，从而增加整个集群的可用内存。
- 使用分布式缓存
  - 可以使用分布式缓存方案，如 Redis Cluster 或 Codis 等，将数据分布在多个 Redis 实例上，提高系统的可扩展性和内存容量。


## redis的6中淘汰策略

当 Redis 实例使用的内存达到 `maxmemory` 配置的上限时，为了继续提供服务，Redis 会根据配置的内存淘汰策略来删除一些键，以释放内存空间。Redis 提供了 6 种内存淘汰策略，下面为你详细介绍：

### 1. volatile-lru



- **含义**：从设置了过期时间的键中，移除最近最少使用（Least Recently Used，LRU）的键。LRU 算法的核心思想是，如果一个数据在最近一段时间内没有被访问，那么在未来它被访问的可能性也相对较低。
- **适用场景**：适用于缓存中存储的数据有过期时间，且希望优先淘汰那些长时间未被使用的过期数据的场景。例如，在电商系统中，对于商品的缓存信息设置了过期时间，使用 `volatile-lru` 策略可以及时清理掉长时间未被访问的商品缓存。

### 2. allkeys-lru



- **含义**：从所有键中，移除最近最少使用的键。与 `volatile-lru` 不同的是，它不区分键是否设置了过期时间，只根据键的使用频率来进行淘汰。
- **适用场景**：当缓存中的数据没有设置过期时间，或者需要对所有数据进行统一的 LRU 淘汰时，可以使用该策略。比如，在一个通用的缓存系统中，存储了各种类型的数据，没有对数据设置过期时间，使用 `allkeys-lru` 可以保证缓存中始终保留最近最常使用的数据。

### 3. volatile-random



- **含义**：从设置了过期时间的键中，随机移除一个键。这种策略不考虑键的使用频率、过期时间等因素，完全随机地选择一个设置了过期时间的键进行删除。
- **适用场景**：在对缓存数据的淘汰没有严格要求，只需要简单地释放一些内存空间，且数据有过期时间的情况下可以使用。例如，在一些对数据一致性要求不高的统计缓存场景中，可以使用 `volatile-random` 策略。

### 4. allkeys-random



- **含义**：从所有键中，随机移除一个键。与 `volatile-random` 类似，只是它的选择范围是所有键，而不仅仅是设置了过期时间的键。
- **适用场景**：适用于对缓存数据的淘汰没有特定规则，只需要快速释放内存空间的场景。比如，在一些临时缓存的场景中，数据的重要性相对较低，使用 `allkeys-random` 可以简单粗暴地释放内存。

### 5. volatile-ttl



- **含义**：从设置了过期时间的键中，移除即将过期的键。该策略会优先删除那些剩余存活时间最短的键。
- **适用场景**：当希望优先清理掉即将过期的数据，以减少过期数据占用的内存空间时，可以使用该策略。例如，在一个限时活动的缓存系统中，对于活动相关的缓存数据设置了过期时间，使用 `volatile-ttl` 可以及时清理掉即将过期的活动缓存。

### 6. noeviction



- **含义**：当内存达到上限时，不进行任何淘汰操作。如果客户端尝试执行会导致内存进一步增加的命令（如 `SET`、`LPUSH` 等），Redis 会直接返回错误，阻止新数据的写入，但仍然可以执行读取操作。
- **适用场景**：适用于对数据完整性要求极高，不允许数据被淘汰的场景。例如，在一些关键配置信息的缓存场景中，不希望因为内存不足而淘汰配置数据，使用 `noeviction` 策略可以保证数据的完整性。



可以通过 `config set maxmemory-policy ` 命令来动态修改 Redis 的内存淘汰策略，也可以在 `redis.conf` 配置文件中通过 `maxmemory-policy` 参数进行配置。

## 生产中的内存问题

1. config get maxmemory info maxmemory config set maxmemory 2gb config get maxmemory-policy
## 主从复制 
replicaof ip port 

## Redis 2.8 之前的 SYNC 方案

Redis 在 2.8 版本之前都是基于 SYNC 命令执行全量同步，整个步骤简化后是这样的:
1. slave 向 master 发送 SYNC 命令请求启动复制流程;
2. master收到 SYNC 命令之后执行 BGSAVE命令(子线程执行，不会阻塞主线程)生成 RDB 文件
(dump.rdb);
3. master 将生成的 RDB 文件发送给 slave;
4. slave 收到 RDB 文件之后就开始加载解析 RDB 同步更新本地数据;
5. 更新完成之后，slave 的状态相当于是 master执行 BGSAVE命令时的状态。master 会将 BGSAVE 命令之后
接受的写命令缓存起来，因为这部分写命令 slave 还未同步;
6. master 将自己缓存的这些写命令发送给 slave，slave 执行这些写命令同步 master 的最新状态;
7. slave 到这个时候已经完成全量复制，后续会通过和 master 维护的长连接来进行命令传播，同步最新的写命
## sential怎么选出master

slave 必须是在线状态才能参加新的 master 的选举，
筛选出所有在线的 slave 之后，通过下面 3个维度进
行最后的筛选(优先级依次降低):
1. slave 优先级:可以通过修改 slave-priority(redis.conf 中配置，Redis5.0 后该配置属性名
称被修改为 replica-priority)配置的值来手动设置slave 的优先级。 slave-priority 默认
值为 100，其值越小得分越高，越有机会成为 master。比如说假设有三个优先级分别为10,100,25
的 slave ，哨兵将选择优先级为 10的。不过，0是一个特殊的优先级值，如果一个slave的 slave
-priority 值为 0，代表其没有参加 master 选举的资格。如果没有优先级最高的，再判断复制进
度。
复制进度:Sentinel 总是希望选择出数据最完整(与旧 master 数据最接近)也就是复制进度最快的
2. slave 被提升为新的 master，复制进度越快得分也就越高。
runid(运行id):通常经过前面两轮筛选已经成功选出来了新的 master，万一真有多个 slave 的优先
3.
级和复制进度一样的话，那就 runid 小的成为新的 master，每个redis 节点启动时都有一个 40 字节
随机字符串作为运行 id。









