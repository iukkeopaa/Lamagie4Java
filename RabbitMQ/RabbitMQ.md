## RabbitMQ ��Ϣ��ô���䣿

RabbitMQ ��һ������ǿ�����Ϣ�м��������Ϣ��������漰�������Ͳ��裬����Ϊ����ϸ���ܣ�

### �������



���˽���Ϣ�������֮ǰ������Ϥ RabbitMQ �ļ������������



- **�����ߣ�Producer��**�����𴴽��ͷ�����Ϣ�� RabbitMQ ��Ӧ�ó���
- **��������Exchange��**�����������߷��͵���Ϣ��������·�ɹ�����Ϣ·�ɵ�һ�����������С�
- **���У�Queue��**������Ϣ�Ļ����������ڴ洢��Ϣ��ֱ�������߽������ѡ�
- **�󶨣�Binding��**�������˽������Ͷ���֮��Ĺ�����ϵ��ͨ���󶨼���Binding Key����ָ����Ϣ��·�ɹ���
- **�����ߣ�Consumer��**���Ӷ����л�ȡ��������Ϣ��Ӧ�ó���

### ��Ϣ��������

#### 1. �����߷�����Ϣ



- ������Ӧ�ó���ͨ�� RabbitMQ �ͻ��˿⣨�� Java �е� RabbitMQ Java Client�����ӵ� RabbitMQ ��������
- �����ߴ�����Ϣ����Ϣͨ��������Ϣ�壨����������ݣ���һЩ���ԣ�����Ϣ�����ȼ�������ʱ��ȣ���
- �����߽���Ϣ���͵�ָ���Ľ���������ָ��һ��·�ɼ���Routing Key����·�ɼ����ڰ���������������ν���Ϣ·�ɵ����С�








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

        // ����������
        channel.exchangeDeclare(EXCHANGE_NAME, "direct");

        String message = "Hello, RabbitMQ!";
        String routingKey = "info";
        // ������Ϣ��������
        channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes());
        System.out.println(" [x] Sent '" + message + "'");

        channel.close();
        connection.close();
    }
}
```

#### 2. ���������ղ�·����Ϣ



- ���������յ������߷��͵���Ϣ��·�ɼ��󣬻������������ͺͰ󶨹�������������Ϣ·�ɵ���Щ���С�RabbitMQ �ṩ�˼��ֲ�ͬ���͵Ľ��������������У�
    - **ֱ����������Direct Exchange��**������·�ɼ��Ͱ󶨼��ľ�ȷƥ����·����Ϣ������󶨼���·�ɼ���ͬ����Ϣ�ͻᱻ·�ɵ���Ӧ�Ķ��С�
    - **���ν�������Fanout Exchange��**�������յ�����Ϣ�㲥��������֮�󶨵Ķ��У�������·�ɼ���
    - **���⽻������Topic Exchange��**��ͨ��·�ɼ��Ͱ󶨼���ģʽƥ����·����Ϣ���󶨼�����ʹ�� `*`��ƥ��һ�����ʣ��� `#`��ƥ������������ʣ�ͨ�����
    - **ͷ��������Headers Exchange��**��������Ϣ��ͷ������������·�ɣ�������·�ɼ���











```plaintext
# ��ֱ��������Ϊ�����󶨶��кͽ�����
channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "info");
```

#### 3. ��Ϣ�洢�ڶ�����



- ������������Ϣ·�ɵ�һ���������к󣬶��лὫ��Ϣ�洢������ֱ������������������Щ��Ϣ�����п�������һЩ���ԣ����Ƿ�־û�����Ϣ�Ƿ��� RabbitMQ ��������Ȼ���ڣ�����󳤶ȵȡ�










```plaintext
# ����һ���־û�����
channel.queueDeclare(QUEUE_NAME, true, false, false, null);
```

#### 4. �����߽��ղ�������Ϣ



- ������Ӧ�ó���ͨ�� RabbitMQ �ͻ��˿����ӵ� RabbitMQ ��������������һ���������С�
- ��������������Ϣʱ��RabbitMQ �Ὣ��Ϣ���͸����ĸö��е������ߣ�Ĭ��ģʽ�������������������Ӷ�������ȡ��Ϣ��ͨ�������ֶ�ȷ��ģʽ����
- �����߽��յ���Ϣ�󣬽�����Ӧ��ҵ�������� RabbitMQ ����ȷ����Ϣ��ACK������ʾ��Ϣ�Ѿ����ɹ���������������ڴ�����Ϣʱ�����쳣�����Է��ͷ�ȷ����Ϣ��NACK������ RabbitMQ ���´������Ϣ��








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

        // ��������
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [x] Received '" + message + "'");
            // �ֶ�ȷ����Ϣ
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
        };
        // ��ʼ������Ϣ������Ϊ�ֶ�ȷ��ģʽ
        channel.basicConsume(QUEUE_NAME, false, deliverCallback, consumerTag -> { });
    }
}
```





### ��Ϣ����Ŀɿ��Ա���



- **�־û�**�����Խ������������к���Ϣ������Ϊ�־û��������� RabbitMQ ��������������Ϣ���ᶪʧ��
- **ȷ�ϻ���**�������߿���ʹ�÷���ȷ�ϻ��ƣ�Publisher Confirms����ȷ����Ϣ�Ѿ��ɹ����͵� RabbitMQ �������������߿���ʹ���ֶ�ȷ��ģʽ��ȷ����Ϣ���ɹ�����
- **���Ŷ���**������Ϣ�޷�����������ʱ�����Խ��䷢�͵����Ŷ��У���������Ų�ʹ���


## RabbitMQ����Ϣ��ô�����

���� TCP ���ӵĴ��������ٿ����ϴ��Ҳ�������ϵͳ��Դ���ƣ����������ƿ�������� RabbitMQ ʹ���ŵ��ķ�ʽ���������ݡ��ŵ���Channel���������ߡ��������� RabbitMQ ͨ�ŵ��������ŵ��ǽ����� TCP �����ϵ��������ӣ���ÿ�� TCP �����ϵ��ŵ�����û�����ơ�����˵ RabbitMQ ��һ�� TCP �����Ͻ����ɰ���ǧ���ŵ����ﵽ����̴߳������ TCP ������̹߳���ÿ���ŵ��� RabbitMQ ����Ψһ�� ID����֤���ŵ�˽���ԣ�ÿ���ŵ���Ӧһ���߳�ʹ�á�
