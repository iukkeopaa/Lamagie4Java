#### 1. 引入依赖



如果你使用 Maven 项目，在 `pom.xml` 中添加 Redisson 依赖：



xml











```xml
<dependency>
    <groupId>org.redisson</groupId>
    <artifactId>redisson</artifactId>
    <version>3.17.6</version>
</dependency>
```

#### 2. 代码示例



java











```java
import org.redisson.Redisson;
import org.redisson.api.RSemaphore;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import java.util.concurrent.TimeUnit;

public class RedisSemaphoreExample {

    public static void main(String[] args) {
        // 创建 Redisson 客户端配置
        Config config = new Config();
        config.useSingleServer().setAddress("redis://127.0.0.1:6379");

        // 创建 Redisson 客户端
        RedissonClient redisson = Redisson.create(config);

        // 获取分布式 Semaphore 实例，指定名称
        RSemaphore semaphore = redisson.getSemaphore("mySemaphore");
        // 设置许可数量
        semaphore.trySetPermits(5);

        try {
            // 尝试获取许可，等待 10 秒
            if (semaphore.tryAcquire(1, 10, TimeUnit.SECONDS)) {
                try {
                    System.out.println("获取到许可，开始执行任务");
                    // 模拟任务执行
                    Thread.sleep(2000);
                } finally {
                    // 释放许可
                    semaphore.release();
                    System.out.println("任务执行完毕，释放许可");
                }
            } else {
                System.out.println("未能在规定时间内获取到许可");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // 关闭 Redisson 客户端
            redisson.shutdown();
        }
    }
}
```





#### 3. 代码解释



- **配置 Redisson 客户端**：使用 `Config` 类配置 Redisson 客户端连接到 Redis 服务器。
- **获取分布式 Semaphore 实例**：通过 `redisson.getSemaphore` 方法获取指定名称的分布式 Semaphore 实例。
- **设置许可数量**：使用 `trySetPermits` 方法设置 Semaphore 的许可数量。
- **获取许可**：使用 `tryAcquire` 方法尝试获取许可，可指定等待时间。
- **释放许可**：在任务执行完毕后，使用 `release` 方法释放许可。
- **关闭客户端**：最后关闭 Redisson 客户端。