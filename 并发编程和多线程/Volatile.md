## volatile����ЩӦ�ó���_
### 1. ״̬��־



�ڶ��̻߳����У�������Ҫʹ��һ����־λ�������̵߳�ִ���߼���ʹ�� `volatile` ���������־λ����ȷ�������߳��ܹ���ʱ������־λ�ı仯��

#### ʾ������



java











```java
class VolatileFlagExample {
    // ʹ�� volatile ���α�־λ
    private volatile boolean isRunning = true;

    public void start() {
        Thread worker = new Thread(() -> {
            while (isRunning) {
                // ִ��һЩ����
                System.out.println("�߳���������...");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("�߳���ֹͣ");
        });
        worker.start();
    }

    public void stop() {
        isRunning = false;
    }

    public static void main(String[] args) {
        VolatileFlagExample example = new VolatileFlagExample();
        example.start();

        try {
            // ���߳�����һ��ʱ��
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // ֹͣ�߳�
        example.stop();
    }
}
```

#### �������



�����������У�`isRunning` �� `volatile` ���Σ��� `stop()` ������ `isRunning` ����Ϊ `false` ʱ��ִ�� `start()` �����д������߳��ܹ�������֪������仯���Ӷ��˳�ѭ����ִֹͣ�С�

### 2. ����ģʽ�е�˫�ؼ��������DCL��



��ʵ�ֵ���ģʽʱ��˫�ؼ��������DCL����һ�ֳ������Ż���ʽ��ʹ�� `volatile` ���Ա����ڶ��̻߳����³��ֵ����⡣

#### ʾ������



java











```java
class Singleton {
    // ʹ�� volatile ���ε�������
    private static volatile Singleton instance;

    private Singleton() {}

    public static Singleton getInstance() {
        if (instance == null) {
            synchronized (Singleton.class) {
                if (instance == null) {
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }
}
```

#### �������



�� `getInstance()` �����У���һ�μ�� `instance` �Ƿ�Ϊ `null` ��Ϊ�˱��ⲻ��Ҫ��ͬ���������ڶ��μ����Ϊ��ȷ���ڶ��̻߳�����ֻ��һ��ʵ����������`volatile` �ؼ��ֵ������ǽ�ָֹ�������򣬱�֤ `instance` �����ڱ���ֵʱ����ȫ��ʼ���ģ����������߳̿���һ�����ֳ�ʼ���Ķ���

### 3. ����д�ٵĳ���



��һЩ������ԶԶ����д�����ĳ����У�ʹ�� `volatile` ����������ܣ���Ϊ���� `synchronized` ������������

#### ʾ������



java











```java
class VolatileReadWriteExample {
    // ʹ�� volatile ���ι������
    private volatile int counter = 0;

    public void increment() {
        // д����
        counter++;
    }

    public int getCounter() {
        // ������
        return counter;
    }
}
```

#### �������



����������У�`counter` ������ `volatile` ���Σ�����߳̿���ͬʱ��ȡ `counter` ��ֵ��������д��������ʱ�������߳��ܹ������������º��ֵ������ `volatile` ����֤ԭ���ԣ�����д�������ܻ���ڲ������⣬����һЩ��д��������Ҫ�󲻸ߵĳ����У����ַ�ʽ����������ܡ�

### 4. ��ԭ�������ʹ��



`volatile` ������ Java ��ԭ���ࣨ�� `AtomicInteger`��`AtomicLong` �ȣ����ʹ�ã���һЩ���ӵĲ���������ʵ�ָ���Ч��ͬ����

#### ʾ������



java











```java
import java.util.concurrent.atomic.AtomicInteger;

class VolatileAtomicExample {
    // ʹ�� volatile ����ԭ�������
    private volatile AtomicInteger atomicCounter = new AtomicInteger(0);

    public void increment() {
        // ԭ���Ե���������
        atomicCounter.incrementAndGet();
    }

    public int getCounter() {
        // ��ȡԭ�����ֵ
        return atomicCounter.get();
    }
}
```





#### �������



����������У�`atomicCounter` �� `volatile` ���Σ�ȷ���˸����߳��ܹ���ʱ���� `AtomicInteger` ��������ñ仯��ͬʱ��`AtomicInteger` ���ṩ��ԭ���ԵĲ�����������֤�˲������̰߳�ȫ�ԡ�



## Volatile��Atomic������


Volatile ��������ȷ�����й�ϵ����д�����ᷢ���ں����Ķ�����֮ǰ, ��������
�ܱ�֤ԭ���ԡ������� volatile ���� count ������ô count++ �����Ͳ���ԭ��
�Եġ�


�� AtomicInteger ���ṩ�� atomic �������������ֲ�������ԭ������
getAndIncrement()������ԭ���ԵĽ������������ѵ�ǰֵ��һ��������������
�����ñ���Ҳ���Խ������Ʋ�����