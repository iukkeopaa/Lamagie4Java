## ȫ��Ψһ ID Ӧ���߱�ʲô��������

1. ȫ��Ψһ�ԣ����ܳ����ظ��� ID �ţ���Ȼ��Ψһ��ʶ�������������Ҫ��
2. ���Ƶ������� MySQL InnoDB ������ʹ�õ��Ǿۼ����������ڶ��� RDBMS ʹ�� B-tree �����ݽṹ���洢�������ݣ���������ѡ����������Ӧ�þ���ʹ�������������֤д�����ܣ�
3. ������������֤��һ�� ID һ��������һ�� ID����������汾�š�IM ������Ϣ���������������
4. ��Ϣ��ȫ����� ID �������ģ������û�����ȡ�����ͷǳ��������ˣ�ֱ�Ӱ���˳������ָ�� URL ���ɣ�����Ƕ����ž͸�Σ���ˣ��������ֿ���ֱ��֪������һ��ĵ�����������һЩӦ�ó����£�����Ҫ ID �޹��򡢲�����



## UUID



### ���ɲ���

### 1. �汾 1������ʱ���



- **ԭ��**���汾 1 �� UUID �ǻ���ʱ����� MAC ��ַ���ɵġ������������� UUID ��ʱ�����Ϣ����ȷ�� 100 ���룬�Լ����ɸ� UUID �ļ������ MAC ��ַ��ͨ�����ʱ��� MAC ��ַ�����Ա�֤�ڲ�ͬʱ��Ͳ�ͬ����������ɵ� UUID ��Ψһ�ġ�
- **Java ʾ��**��



java











```java
import java.util.UUID;

public class UUIDVersion1Example {
    public static void main(String[] args) {
        // Java ��׼��û��ֱ�����ɰ汾 1 UUID �ķ������ɽ����������⣬�� Hibernate
        // ����ֻ��ʾ�� UUID ���ɶ���Ĵ���
        UUID uuid = UUID.randomUUID(); 
        System.out.println("���ɵ� UUID: " + uuid);
    }
}
```



- ��ȱ��

  ��

    - **�ŵ�**�����ڰ���ʱ����Ϣ�����������򣻾��нϸߵ�Ψһ�ԣ������ڷֲ�ʽϵͳ����ҪΨһ��ʶ�Ҷ�ʱ��˳����Ҫ��ĳ�����
    - **ȱ��**����¶�� MAC ��ַ������һ���İ�ȫ���գ����ɵ� UUID ����ʱ��������ܻᱻ�������������Ʋ�ϵͳ��ʹ��ʱ�����Ϣ��

### 2. �汾 2��DCE ��ȫ



- **ԭ��**���汾 2 �� UUID �ǻ��ڷֲ�ʽ���㻷����DCE����ȫģ�����ɵģ�����汾 1 ���ƣ���ʹ�õ����û� ID ���� ID ���� MAC ��ַ��
- **ʹ�ó���**����һЩ��Ҫ���ǰ�ȫ��Ȩ�޵ķֲ�ʽϵͳ��ʹ�ã�����ʵ��Ӧ���в���������
- **ע������**��Java ��׼����û��ֱ��֧�����ɰ汾 2 UUID �ķ�������Ҫʹ���ض��ĵ������⡣

### 3. �汾 3�����������ռ�



- **ԭ��**���汾 3 �� UUID ��ͨ�� MD5 ��ϣ�㷨���ɵġ�����Ҫһ�������ռ䣨ͨ������һ�� UUID����һ��������Ϊ���룬�������ռ��������Ϻ���� MD5 ��ϣ���㣬�õ� 128 λ�Ĺ�ϣֵ���ٽ���ת��Ϊ UUID ��ʽ��
- **Java ʾ��**��



java











```java
import java.util.UUID;

public class UUIDVersion3Example {
    public static void main(String[] args) {
        UUID namespace = UUID.randomUUID();
        String name = "exampleName";
        UUID uuid = UUID.nameUUIDFromBytes((namespace.toString() + name).getBytes());
        System.out.println("�汾 3 �� UUID: " + uuid);
    }
}
```



- ��ȱ��

  ��

    - **�ŵ�**��ֻҪ�����ռ��������ͬ�����ɵ� UUID ������ͬ�ģ���������Ҫȷ�������� UUID �ĳ�����������������
    - **ȱ��**������ʹ�õ��� MD5 ��ϣ�㷨������һ���Ĺ�ϣ��ͻ���ա�

### 4. �汾 4���������



- **ԭ��**���汾 4 �� UUID ����ȫ������ɵģ���ʹ���������������ͨ���ǰ�ȫ������������������� 128 λ���������Ȼ����ת��Ϊ UUID ��ʽ��
- **Java ʾ��**��



java











```java
import java.util.UUID;

public class UUIDVersion4Example {
    public static void main(String[] args) {
        UUID uuid = UUID.randomUUID();
        System.out.println("�汾 4 �� UUID: " + uuid);
    }
}
```



- ��ȱ��

  ��

    - **�ŵ�**�������ٶȿ죬ʹ�÷��㣬�����ڴ������ҪΨһ��ʶ����Ψһ��Ҫ���Ǽ����ϸ�ĳ�����
    - **ȱ��**��������������ɵģ��������κ����������Ϣ���޷��� UUID �����Ʋ�����ɵ�ʱ�䡢�ص����Ϣ��

### 5. �汾 5�����������ռ䣨SHA - 1��



- **ԭ��**���汾 5 �� UUID ��汾 3 ���ƣ�Ҳ�ǻ��������ռ���������ɵģ���ʹ�õ��� SHA - 1 ��ϣ�㷨��SHA - 1 �� MD5 ����ȫ����ϣ��ͻ�ĸ��ʸ��͡�
- **Java ʾ��**��



java











```java
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public class UUIDVersion5Example {
    public static UUID nameUUIDFromNamespaceAndString(UUID namespace, String name) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(toBytes(namespace));
            md.update(name.getBytes(StandardCharsets.UTF_8));
            byte[] sha1Bytes = md.digest();
            sha1Bytes[6] &= 0x0f;  /* clear version        */
            sha1Bytes[6] |= 0x50;  /* set to version 5     */
            sha1Bytes[8] &= 0x3f;  /* clear variant        */
            sha1Bytes[8] |= 0x80;  /* set to IETF variant  */
            return fromBytes(sha1Bytes);
        } catch (NoSuchAlgorithmException e) {
            throw new InternalError("SHA-1 not supported", e);
        }
    }

    private static byte[] toBytes(UUID uuid) {
        long msb = uuid.getMostSignificantBits();
        long lsb = uuid.getLeastSignificantBits();
        byte[] buffer = new byte[16];

        for (int i = 0; i < 8; i++) {
            buffer[i] = (byte) (msb >>> 8 * (7 - i));
        }
        for (int i = 8; i < 16; i++) {
            buffer[i] = (byte) (lsb >>> 8 * (7 - i));
        }
        return buffer;
    }

    private static UUID fromBytes(byte[] data) {
        long msb = 0;
        long lsb = 0;
        assert data.length == 16 : "data must be 16 bytes in length";
        for (int i = 0; i < 8; i++) {
            msb = (msb << 8) | (data[i] & 0xff);
        }
        for (int i = 8; i < 16; i++) {
            lsb = (lsb << 8) | (data[i] & 0xff);
        }
        return new UUID(msb, lsb);
    }

    public static void main(String[] args) {
        UUID namespace = UUID.randomUUID();
        String name = "exampleName";
        UUID uuid = nameUUIDFromNamespaceAndString(namespace, name);
        System.out.println("�汾 5 �� UUID: " + uuid);
    }
}
```







- ��ȱ��

  ��

    - **�ŵ�**����汾 3 ��ȣ�ʹ�� SHA - 1 �㷨�����˹�ϣ��ͻ�ķ��գ�ͬʱ����ȷ���ԣ������ڶ�Ψһ�ԺͰ�ȫ��Ҫ��ϸߵĳ�����
    - **ȱ��**�����ɹ�����Ը��ӣ������Ե��ڰ汾 3��
### ��Ȼ UUID ���ɷ��㣬��������û���������ģ�����ʹ������Ҳ��һЩȱ��

�����ڴ洢��UUID̫����16�ֽ�128λ��ͨ����36���ȵ��ַ�����ʾ���ܶೡ�������á�
��Ϣ����ȫ������MAC��ַ����UUID���㷨���ܻ����MAC��ַй¶����¶ʹ���ߵ�λ�á�
��MySQL���������������Ϊ���ݿ���������InnoDB�����£�UUID�������Կ��ܻ���������λ��Ƶ���䶯������Ӱ�����ܣ����Բ��� Mysql ����ԭ�� B+����֪ʶ


## ���ݿ�����

���ݿ�ˮƽ��֣����ó�ʼֵ����ͬ����������

![img.png](img.png)


### ȱ��Ҳ�����ԣ�
������ǿ����DB����DB�쳣ʱ����ϵͳ�����á���Ȼ�������Ӹ��ƿ��Ծ����ܵ����ӿ����ԣ���������һ������������������Ա�֤��
�����л�ʱ�Ĳ�һ�¿��ܻᵼ���ظ����š����о���ID��������ƿ�������ڵ�̨MySQL�Ķ�д���ܡ�
Ԥ���ֶ�Ҫ����ҵ�����

## �������ݿ�ĺŶ�ģʽ

�Ŷ�ģʽ�ǵ��·ֲ�ʽID������������ʵ�ַ�ʽ֮һ���Ŷ�ģʽ�������Ϊ�����ݿ������Ļ�ȡ����ID��ÿ�δ����ݿ�ȡ��һ���Ŷη�Χ������ (1,1000] ����1000��ID�������ҵ����񽫱��ŶΣ�����1~1000������ID�����ص��ڴ档��ṹ���£�

```mysql
CREATE TABLE id_generator (
  id int(10) NOT NULL,
  max_id bigint(20) NOT NULL COMMENT '��ǰ���id',
  step int(20) NOT NULL COMMENT '�ŶεĲ���',
  biz_type	int(20) NOT NULL COMMENT 'ҵ������',
  version int(20) NOT NULL COMMENT '�汾��',
  PRIMARY KEY (`id`)
) 
```
id : Ψһ��ʾ
  
biz_type ������ͬҵ������

max_id ����ǰ���Ŀ���id

step ������Ŷεĳ���

version ����һ���ֹ�����ÿ�ζ�����version����֤����ʱ���ݵ���ȷ��

![img_2.png](img_2.png)
### ȱ��

1. ��Ϊ�̶������ �������а�ȫ����
2.  ��Ȼ����1000��ID�����ǻ���500��û�ã�����Ϊ��ID�Ǵ浽�ڴ��еģ����Բ��᷵�ظ����ݿ��
3. ����Ҫ�����·���IDʱ�����ݿ�崻��ˣ���ʱ��������⣬�������ǲ�һ����Ҫ�ȵ�Ҫ����֮��������룬��������һ����ֵ������һ�������ݿ���ȡ

## ʹ��redisʵ��

Redisʵ�ֲַ�ʽΨһID��Ҫ��ͨ���ṩ�� INCR �� INCRBY ����������ԭ���������Redis����ĵ��̵߳��ص������ܱ�֤���ɵ� ID �϶���Ψһ����ġ�

���ǵ�����������ƿ�����޷�����߲�����ҵ���������Կ��Բ��ü�Ⱥ�ķ�ʽ��ʵ�֡���Ⱥ�ķ�ʽ�ֻ��漰�������ݿ⼯Ⱥͬ�������⣬����Ҳ��Ҫ���÷ֶκͲ�����ʵ�֡�

Ϊ�˱��ⳤ�����������ֹ������ͨ���뵱ǰʱ����������ʹ�ã�����Ϊ�˱�֤������ҵ����̵߳�������Բ��� Redis + Lua�ķ�ʽ���б��룬��֤��ȫ��

Redis ʵ�ֲַ�ʽȫ��ΨһID���������ܱȽϸߣ����ɵ�����������ģ�������ҵ������������ͬ����������redis����Ҫϵͳ����redis�����������ϵͳ�����ø����ԡ�

��Ȼ����Redis��ʹ���Ժ��ձ飬�����������ҵ���Ѿ�������Redis��Ⱥ���������Դ���ÿ���ʹ��Redis��ʵ�֡�

��redisʵ����Ҫע��һ�㣬Ҫ���ǵ�redis�־û������⡣redis�����ֳ־û���ʽRDB��AOF

RDB�ᶨʱ��һ�����ս��г־û�����������������redisû��ʱ�־û��������Redis�ҵ��ˣ�����Redis������ID�ظ��������
AOF���ÿ��д������г־û�����ʹRedis�ҵ���Ҳ�������ID�ظ��������������incr����������ԣ��ᵼ��Redis�����ָ�������ʱ�������

��������˽��� Redis ��ʵ�֣����綩����=����+�����������ţ�������ͨ�� INCR ʵ�֡������������Ļ����޷������Ų��ɲ²�����

### �����ʵ��

#### ��һ����ʹ��RedisAtomicLong ԭ����ʹ��CAS����������ID��

```java
@Service
public class RedisSequenceFactory {
    @Autowired
    RedisTemplate<String, String> redisTemplate;

    public void setSeq(String key, int value, Date expireTime) {
        RedisAtomicLong counter = new RedisAtomicLong(key, redisTemplate.getConnectionFactory());
        counter.set(value);
        counter.expireAt(expireTime);
    }

    public void setSeq(String key, int value, long timeout, TimeUnit unit) {
        RedisAtomicLong counter = new RedisAtomicLong(key, redisTemplate.getConnectionFactory());
        counter.set(value);
        counter.expire(timeout, unit);
    }

    public long generate(String key) {
        RedisAtomicLong counter = new RedisAtomicLong(key, redisTemplate.getConnectionFactory());
        return counter.incrementAndGet();
    }

    public long incr(String key, Date expireTime) {
        RedisAtomicLong counter = new RedisAtomicLong(key, redisTemplate.getConnectionFactory());
        counter.expireAt(expireTime);
        return counter.incrementAndGet();
    }

    public long incr(String key, int increment) {
        RedisAtomicLong counter = new RedisAtomicLong(key, redisTemplate.getConnectionFactory());
        return counter.addAndGet(increment);
    }

    public long incr(String key, int increment, Date expireTime) {
        RedisAtomicLong counter = new RedisAtomicLong(key, redisTemplate.getConnectionFactory());
        counter.expireAt(expireTime);
        return counter.addAndGet(increment);
    }
}

```
### �ڶ�����ʹ��redisTemplate.opsForHash()�ͽ��UUID�ķ�ʽ����������ID��
```java
public Long getSeq(String key,String hashKey,Long delta) throws BusinessException{
        try {
            if (null == delta) {
                delta=1L;
            }
            return redisTemplate.opsForHash().increment(key, hashKey, delta);
        } catch (Exception e) {  // ����redis崻��Ͳ���uuid�ķ�ʽ
            int first = new Random(10).nextInt(8) + 1;
            int randNo=UUID.randomUUID().toString().hashCode();
            if (randNo < 0) {
                randNo=-randNo;
            }
            return Long.valueOf(first + String.format("%16d", randNo));
        }
    }

```

## ѩ���㷨-Snowflake

snowflake ���������ݿ⣬Ҳ�������ڴ�洢����ʱ������ ID����Ҳ��������ܻ�ӭ��ԭ�򡣵���Ϊ�������ʱͨ��ʱ�����������ڴ�����ݿ�������������������ڷ�������ʱ�䡣���������ᵽ�� Snowflake �� 4 �νṹ��ʵ����Ӱ�� ID ��С���ǽϸ�λ��ֵ���������λ�̶�Ϊ 0����Ӱ�� ID ��С������λ��ֵ��Ҳ����ʱ�����


ѩ���㷨�ṩ��һ���ܺõ����˼�룬ѩ���㷨���ɵ�ID�����Ƶ��������������ݿ�ȵ�����ϵͳ���Է���ķ�ʽ�����ȶ��Ը��ߣ�����ID������Ҳ�Ƿǳ��ߵģ����ҿ��Ը�������ҵ�����Է���bitλ���ǳ���

����ѩ���㷨ǿ��������ʱ�ӣ����������ʱ�ӻز����ᵼ�·����ظ����߷���ᴦ�ڲ�����״̬�����ǡ�ɻ���ǰ���ɹ�һЩID����ʱ����˺����ɵ�ID���п����ظ����ٷ����ڴ˲�û�и���������������Ǽ򵥵��״��������������ʱ�䱻׷��֮ǰ�����ʱ����񲻿��á�

![img_1.png](img_1.png)

1. ��1λռ��1bit����ֵʼ����0���ɿ����Ƿ���λ��ʹ�á�
2. ��2λ��ʼ��41λ��ʱ�����41-bitλ�ɱ�ʾ2^41������ÿ����������룬��ôѩ���㷨���õ�ʱ��������(1L<<41)/(1000L360024*365)=69 ���ʱ�䡣
3. �м��10-bitλ�ɱ�ʾ����������2^10 = 1024̨����������һ����������ǲ��Ჿ����ǫ̂������������Ƕ�IDC���������������ģ������󣬻����Խ� 10-bit �� 5-bit �� IDC����5-bit�����������������Ϳ��Ա�ʾ32��IDC��ÿ��IDC�¿�����32̨����������Ļ��ֿ��Ը������������塣
4. ���12-bitλ���������У��ɱ�ʾ2^12 = 4096������

�����Ļ���֮���൱����һ����һ���������ĵ�һ̨�����Ͽɲ���4096������Ĳ��ظ���ID���������� IDC �ͻ������϶���ֹһ�������Ժ����������ɵ�����ID���Ƿ����ġ�

ѩ���㷨ʹ����������ID�ͻ���ID��Ϊ��ʶ���������ID���ظ����������ڱ������ɣ������������磬Ч�ʸߣ���������ʾ��ÿ��������26���ID


ѩ���㷨��Snowflake���� Twitter ��Դ�ķֲ�ʽ ID �����㷨�������ɵ� ID ��һ�� 64 λ�ĳ��������֣���ʱ������������� ID �����к���ɡ�������ѩ���㷨��ʵ��ʹ���л����һЩ���⣬���潫��ϸ������Щ�����Լ���Ӧ�Ľ���취��

### 1. ʱ�ӻز�����



- **��������**��ѩ���㷨����ϵͳʱ�������� ID�����ϵͳʱ�ӷ����ز������ܻᵼ�������ظ��� ID�����磬������������ NTP ʱ��ͬ��ʱ��ʱ�ӿ��ܻᱻ������֮ǰ��ʱ�䣬��ʱ�����������ԭ�㷨���� ID���ͻ���֮ǰ�Ѿ����ɵ� ID ������ͻ��
- �������
    - **�ȴ�ʱ�ӻָ�**������⵽ʱ�ӻز�ʱ��������ͣһ��ʱ�䣬�ȴ�ʱ�ӻָ�������ʱ����ټ������� ID��



java











```java
// ��ʾ������⵽ʱ�ӻز���ȴ�
private long lastTimestamp = -1L;
private final long waitTime = 1000L; // �ȴ� 1 ��

public synchronized long nextId() {
    long currentTimestamp = System.currentTimeMillis();
    if (currentTimestamp < lastTimestamp) {
        // ����ʱ�ӻز�
        try {
            Thread.sleep(waitTime);
            currentTimestamp = System.currentTimeMillis();
            if (currentTimestamp < lastTimestamp) {
                throw new RuntimeException("Clock moved backwards. Refusing to generate id for " + waitTime + " milliseconds");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    lastTimestamp = currentTimestamp;
    // ���� ID �������߼�
    return id;
}
```



- **ʹ�ñ���ʱ��Դ**����������һ�����õ�ʱ��Դ���� GPS ʱ�ӣ���ϵͳʱ�ӷ����ز�ʱ���л�������ʱ��Դ������ ID��
- **��¼ʱ�ӻز�������**����¼ʱ�ӻز���ʱ���ƫ�������ں������� ID ʱ��ͨ������ʱ�����ƫ���������� ID �ظ���

### 2. ���� ID ��������



- **��������**��ѩ���㷨��ҪΪÿ��������������Ψһ�Ļ��� ID��������� ID ���䲻�������ܻᵼ�²�ͬ����������ͬ�� ID���ֶ�������� ID ���׳��������ڴ��ģ�ֲ�ʽϵͳ�У�������� ID ���÷ǳ����ӡ�
- �������
    - **ʹ�� Zookeeper ������� ID**������ Zookeeper ������ڵ����ԣ�Ϊÿ��������������Ψһ�Ļ��� ID������������ʱ���� Zookeeper ע��һ����ʱ����ڵ㣬�ڵ����ż�Ϊ���� ID��



java











```java
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class ZookeeperMachineIdAllocator {
    private static final String ZOOKEEPER_CONNECT_STRING = "localhost:2181";
    private static final int SESSION_TIMEOUT = 3000;
    private static final String MACHINE_ID_NODE_PATH = "/snowflake/machine_ids";
    private ZooKeeper zk;

    public ZookeeperMachineIdAllocator() throws IOException, InterruptedException, KeeperException {
        CountDownLatch connectedSignal = new CountDownLatch(1);
        zk = new ZooKeeper(ZOOKEEPER_CONNECT_STRING, SESSION_TIMEOUT, event -> {
            if (event.getState() == Watcher.Event.KeeperState.SyncConnected) {
                connectedSignal.countDown();
            }
        });
        connectedSignal.await();
        if (zk.exists(MACHINE_ID_NODE_PATH, false) == null) {
            zk.create(MACHINE_ID_NODE_PATH, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        }
    }

    public long getMachineId() throws KeeperException, InterruptedException {
        String path = zk.create(MACHINE_ID_NODE_PATH + "/machine_", new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        String[] parts = path.split("/");
        String nodeName = parts[parts.length - 1];
        return Long.parseLong(nodeName.substring("machine_".length()));
    }

    public void close() throws InterruptedException {
        zk.close();
    }
}
```



- **ʹ�����ݿ������� ID**�������ݿ��д���һ������������� ID��ÿ����������ʱ�����ݿ��л�ȡһ��Ψһ�Ļ��� ID�������� ID ���Ϊ��ʹ�á�

### 3. ������������



- **��������**��ѩ���㷨��ͨ��ͬ��������֤�̰߳�ȫ�ģ��ڸ߲��������£�Ƶ���ļ����ͽ����������Ϊ����ƿ����Ӱ�� ID ���ɵ��ٶȡ�
- �������
    - **�ֶλ��� ID**��ÿ����������Ԥ�ȴ� ID �������л�ȡһ�������� ID ��������������Ҫ���� ID ʱ��ֱ�Ӵӻ����л�ȡ������Ƶ���������������е� ID ������ʱ����������ȡ�µ� ID �Ρ�
    - **���߳����� ID**������ʹ�ö���߳�ͬʱ���� ID��ÿ���߳�ά���Լ��ļ�������ʱ�������� ID ���ɵĲ������ܡ�

### 4. �������� ID �ͻ��� ID λ����������



- **��������**��ѩ���㷨���������� ID �ͻ��� ID ��λ���ǹ̶��ģ����ϵͳ��ģ�������󣬿��ܻᵼ���������� ID ����� ID �����á�
- �������
    - **����λ������**������ʵ���������ʱ������������� ID������ ID �����кŵ�λ�����䡣���磬������������϶࣬�����ʵ������������� ID ��λ�������ӻ��� ID ��λ����
    - **���÷ֲ�ܹ�**����ϵͳ����Ϊ�����Σ�ÿ�����ʹ�ò�ͬ�� ID ���ɲ��ԣ���Ӧ�Դ��ģ�ֲ�ʽϵͳ������
## �ٶ�-UidGenerator


## ����Leaf

## Mist �����㷨

## �ε�TinyID