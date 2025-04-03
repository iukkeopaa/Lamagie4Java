#### 1. 引入依赖



如果你使用 Maven 项目，在 `pom.xml` 中添加 Curator 依赖：



xml











```xml
<dependency>
    <groupId>org.apache.curator</groupId>
    <artifactId>curator-recipes</artifactId>
    <version>5.3.0</version>
</dependency>
<dependency>
    <groupId>org.apache.curator</groupId>
    <artifactId>curator-framework</artifactId>
    <version>5.3.0</version>
</dependency>
```

#### 2. 代码示例



java











```java
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessSemaphoreV2;
import org.apache.curator.framework.recipes.locks.Lease;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.Collection;

public class ZookeeperSemaphoreExample {

    public static void main(String[] args) {
        // 创建 Curator 客户端
        CuratorFramework client = CuratorFrameworkFactory.newClient(
                "localhost:2181",
                new ExponentialBackoffRetry(1000, 3)
        );
        client.start();

        // 创建分布式 Semaphore 实例，指定 Zookeeper 节点路径和许可数量
        InterProcessSemaphoreV2 semaphore = new InterProcessSemaphoreV2(client, "/mySemaphore", 5);

        try {
            // 尝试获取许可
            Collection<Lease> leases = semaphore.acquire(1);
            if (!leases.isEmpty()) {
                try {
                    System.out.println("获取到许可，开始执行任务");
                    // 模拟任务执行
                    Thread.sleep(2000);
                } finally {
                    // 释放许可
                    semaphore.returnLeases(leases);
                    System.out.println("任务执行完毕，释放许可");
                }
            } else {
                System.out.println("未能获取到许可");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭 Curator 客户端
            client.close();
        }
    }
}
```





#### 3. 代码解释



- **创建 Curator 客户端**：使用 `CuratorFrameworkFactory` 创建连接到 Zookeeper 的客户端。
- **创建分布式 Semaphore 实例**：通过 `InterProcessSemaphoreV2` 类创建指定 Zookeeper 节点路径和许可数量的分布式 Semaphore 实例。
- **获取许可**：使用 `acquire` 方法尝试获取许可，返回一个 `Lease` 集合。
- **释放许可**：在任务执行完毕后，使用 `returnLeases` 方法释放许可。
- **关闭客户端**：最后关闭 Curator 客户端。



分享



Redisson 除了实现分布式 Semaphore，还能实现哪些分布式锁？

Curator 如何实现分布式 Semaphore？

分布式 Semaphore 与其他分布式锁有什么区别？