## RabbitMQ 消息怎么传输？

RabbitMQ 是一个功能强大的消息中间件，其消息传输过程涉及多个组件和步骤，以下为你详细介绍：

### 核心组件



在了解消息传输过程之前，先熟悉 RabbitMQ 的几个核心组件：



- **生产者（Producer）**：负责创建和发送消息到 RabbitMQ 的应用程序。
- **交换器（Exchange）**：接收生产者发送的消息，并根据路由规则将消息路由到一个或多个队列中。
- **队列（Queue）**：是消息的缓冲区，用于存储消息，直到消费者将其消费。
- **绑定（Binding）**：定义了交换器和队列之间的关联关系，通过绑定键（Binding Key）来指定消息的路由规则。
- **消费者（Consumer）**：从队列中获取并处理消息的应用程序。

### 消息传输流程

#### 1. 生产者发送消息



- 生产者应用程序通过 RabbitMQ 客户端库（如 Java 中的 RabbitMQ Java Client）连接到 RabbitMQ 服务器。
- 生产者创建消息，消息通常包含消息体（即具体的数据）和一些属性（如消息的优先级、过期时间等）。
- 生产者将消息发送到指定的交换器，并指定一个路由键（Routing Key）。路由键用于帮助交换器决定如何将消息路由到队列。








```java
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Producer {
    private static final String EXCHANGE_NAME = "direct_exchange";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        // 声明交换器
        channel.exchangeDeclare(EXCHANGE_NAME, "direct");

        String message = "Hello, RabbitMQ!";
        String routingKey = "info";
        // 发送消息到交换器
        channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes());
        System.out.println(" [x] Sent '" + message + "'");

        channel.close();
        connection.close();
    }
}
```

#### 2. 交换器接收并路由消息



- 交换器接收到生产者发送的消息和路由键后，会根据自身的类型和绑定规则来决定将消息路由到哪些队列。RabbitMQ 提供了几种不同类型的交换器，常见的有：
    - **直连交换器（Direct Exchange）**：根据路由键和绑定键的精确匹配来路由消息。如果绑定键和路由键相同，消息就会被路由到对应的队列。
    - **扇形交换器（Fanout Exchange）**：将接收到的消息广播到所有与之绑定的队列，不考虑路由键。
    - **主题交换器（Topic Exchange）**：通过路由键和绑定键的模式匹配来路由消息。绑定键可以使用 `*`（匹配一个单词）和 `#`（匹配零个或多个单词）通配符。
    - **头交换器（Headers Exchange）**：根据消息的头部属性来进行路由，而不是路由键。











```plaintext
# 以直连交换器为例，绑定队列和交换器
channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "info");
```

#### 3. 消息存储在队列中



- 当交换器将消息路由到一个或多个队列后，队列会将消息存储起来，直到有消费者来消费这些消息。队列可以设置一些属性，如是否持久化（消息是否在 RabbitMQ 重启后仍然存在）、最大长度等。










```plaintext
# 声明一个持久化队列
channel.queueDeclare(QUEUE_NAME, true, false, false, null);
```

#### 4. 消费者接收并处理消息



- 消费者应用程序通过 RabbitMQ 客户端库连接到 RabbitMQ 服务器，并订阅一个或多个队列。
- 当队列中有新消息时，RabbitMQ 会将消息推送给订阅该队列的消费者（默认模式），或者消费者主动从队列中拉取消息（通过设置手动确认模式）。
- 消费者接收到消息后，进行相应的业务处理，并向 RabbitMQ 发送确认消息（ACK），表示消息已经被成功处理。如果消费者在处理消息时出现异常，可以发送否定确认消息（NACK），让 RabbitMQ 重新处理该消息。








```java
import com.rabbitmq.client.*;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Consumer {
    private static final String QUEUE_NAME = "info_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        // 声明队列
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [x] Received '" + message + "'");
            // 手动确认消息
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
        };
        // 开始消费消息，设置为手动确认模式
        channel.basicConsume(QUEUE_NAME, false, deliverCallback, consumerTag -> { });
    }
}
```





### 消息传输的可靠性保障



- **持久化**：可以将交换器、队列和消息都设置为持久化，这样在 RabbitMQ 服务器重启后，消息不会丢失。
- **确认机制**：生产者可以使用发布确认机制（Publisher Confirms）来确保消息已经成功发送到 RabbitMQ 服务器；消费者可以使用手动确认模式来确保消息被成功处理。
- **死信队列**：当消息无法被正常消费时，可以将其发送到死信队列，方便后续排查和处理。


## RabbitMQ的消息怎么传输的

由于 TCP 链接的创建和销毁开销较大，且并发数受系统资源限制，会造成性能瓶颈，所以 RabbitMQ 使用信道的方式来传输数据。信道（Channel）是生产者、消费者与 RabbitMQ 通信的渠道，信道是建立在 TCP 链接上的虚拟链接，且每条 TCP 链接上的信道数量没有限制。就是说 RabbitMQ 在一条 TCP 链接上建立成百上千个信道来达到多个线程处理，这个 TCP 被多个线程共享，每个信道在 RabbitMQ 都有唯一的 ID，保证了信道私有性，每个信道对应一个线程使用。
