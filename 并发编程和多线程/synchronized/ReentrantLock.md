### **1. ��������**

#### **(1) `synchronized`**

- �� Java �Ĺؼ��֣��������η��������顣
- ���� JVM ���õ�������ʵ�֡�
- ʹ�ü򵥣�������ʽ�ͷ�����

#### **(2) `ReentrantLock`**

- �� `java.util.concurrent.locks` ���е�һ���ࡣ
- ���� API ʵ�֣��ṩ�˸����������ơ�
- ��Ҫ��ʽ�ػ�ȡ���ͷ�����

------

### **2. ��ͬ��Ա�**

| **����**           | **synchronized**                           | **ReentrantLock**                    |
| :----------------- | :----------------------------------------- | :----------------------------------- |
| **ʵ�ַ�ʽ**       | JVM ����ʵ��                               | ���� API ʵ��                        |
| **���Ļ�ȡ���ͷ�** | �Զ���ȡ���ͷ���                           | ��Ҫ��ʽ���� `lock()` �� `unlock()`  |
| **���ж���**       | ��֧���ж�                                 | ֧���жϣ�`lockInterruptibly()`��    |
| **��ƽ��**         | ��֧�ֹ�ƽ��                               | ֧�ֹ�ƽ���ͷǹ�ƽ����Ĭ�Ϸǹ�ƽ���� |
| **��������**       | ֻ��ͨ�� `wait()`��`notify()` ʵ�������ȴ� | ֧�ֶ������������`Condition`��      |
| **���ĳ��Ի�ȡ**   | ��֧��                                     | ֧�ֳ��Ի�ȡ����`tryLock()`��        |
| **���Ŀ�������**   | ֧��                                       | ֧��                                 |
| **����**           | �ڵ;�����������ܽϺ�                     | �ڸ߾�����������ܽϺ�               |
| **���븴�Ӷ�**     | ��                                       | �ϸ���                               |

------

### **3. ��ϸ˵��**

#### **(1) ���Ļ�ȡ���ͷ�**

- **`synchronized`**��
    - ���Ļ�ȡ���ͷ��� JVM �Զ�����
    - ����ͬ�������ʱ�Զ���ȡ�����˳�ʱ�Զ��ͷ�����
- **`ReentrantLock`**��
    - ��Ҫ��ʽ���� `lock()` ��ȡ�������� `unlock()` �ͷ�����
    - ������ `finally` �����ͷ������Ա���������

#### **(2) ���ж���**

- **`synchronized`**��
    - ��֧���жϣ��߳��ڵȴ���ʱ��һֱ������
- **`ReentrantLock`**��
    - ֧���жϣ�����ͨ�� `lockInterruptibly()` ������ȡ�����߳��ڵȴ���ʱ���Ա��жϡ�

#### **(3) ��ƽ��**

- **`synchronized`**��
    - ��֧�ֹ�ƽ�������Ļ�ȡ�Ƿǹ�ƽ�ġ�
- **`ReentrantLock`**��
    - ֧�ֹ�ƽ���ͷǹ�ƽ����Ĭ���Ƿǹ�ƽ������
    - ��ƽ�������߳���������˳��������������̼߳�����

#### **(4) ��������**

- **`synchronized`**��
    - ֻ��ͨ�� `wait()`��`notify()` ʵ�������ȴ����������ޡ�
- **`ReentrantLock`**��
    - ֧�ֶ������������`Condition`�������Ը����ؿ����̵߳ĵȴ��ͻ��ѡ�

#### **(5) ���ĳ��Ի�ȡ**

- **`synchronized`**��
    - ��֧�ֳ��Ի�ȡ����
- **`ReentrantLock`**��
    - ֧�ֳ��Ի�ȡ����`tryLock()`�����������ó�ʱʱ�䡣

#### **(6) ����**

- **`synchronized`**��
    - �ڵ;�����������ܽϺã���Ϊ JVM ����������Ż���
- **`ReentrantLock`**��
    - �ڸ߾�����������ܽϺã���Ϊ�ṩ�˸����������ơ�

------

### **4. ʾ��**

#### **(1) `synchronized`**

java

����

```
public class SynchronizedExample {
    private int count = 0;

    public synchronized void increment() {
        count++;
    }
}
```

#### **(2) `ReentrantLock`**

java

����

```
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockExample {
    private int count = 0;
    private Lock lock = new ReentrantLock();

    public void increment() {
        lock.lock();
        try {
            count++;
        } finally {
            lock.unlock();
        }
    }
}
```

------

### **5. �ܽ�**

| **����**           | **synchronized**                           | **ReentrantLock**                    |
| :----------------- | :----------------------------------------- | :----------------------------------- |
| **ʵ�ַ�ʽ**       | JVM ����ʵ��                               | ���� API ʵ��                        |
| **���Ļ�ȡ���ͷ�** | �Զ���ȡ���ͷ���                           | ��Ҫ��ʽ���� `lock()` �� `unlock()`  |
| **���ж���**       | ��֧���ж�                                 | ֧���жϣ�`lockInterruptibly()`��    |
| **��ƽ��**         | ��֧�ֹ�ƽ��                               | ֧�ֹ�ƽ���ͷǹ�ƽ����Ĭ�Ϸǹ�ƽ���� |
| **��������**       | ֻ��ͨ�� `wait()`��`notify()` ʵ�������ȴ� | ֧�ֶ������������`Condition`��      |
| **���ĳ��Ի�ȡ**   | ��֧��                                     | ֧�ֳ��Ի�ȡ����`tryLock()`��        |
| **���Ŀ�������**   | ֧��                                       | ֧��                                 |
| **����**           | �ڵ;�����������ܽϺ�                     | �ڸ߾�����������ܽϺ�               |
| **���븴�Ӷ�**     | ��                                       | �ϸ���                               |

- **`synchronized`**���ʺϼ򵥵�ͬ��������ʹ�÷��㣬���ܽϺá�
- **`ReentrantLock`**���ʺϸ��ӵ�ͬ���������ṩ�����������ƣ������ڸ߾�������½Ϻá�

![img.png](img.png)

## synchronized�������ַ�����  

### ����ʹ�� `synchronized` ���ַ���



���﷨��ʹ�÷�ʽ����˵��`synchronized` �ؼ��ֿ��Զ��ַ���������м�����������Ϊ `synchronized` �ؼ��ֿ��������κζ��󣬶��ַ����� Java ���� `String` ���ʵ�������ڶ���ķ��롣

#### ʾ������



java











```java
public class SynchronizedStringExample {
    private static final String LOCK_STRING = "lock";

    public static void main(String[] args) {
        Thread thread1 = new Thread(() -> {
            synchronized (LOCK_STRING) {
                System.out.println("�߳� 1 ��ȡ����");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("�߳� 1 �ͷ���");
            }
        });

        Thread thread2 = new Thread(() -> {
            synchronized (LOCK_STRING) {
                System.out.println("�߳� 2 ��ȡ����");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("�߳� 2 �ͷ���");
            }
        });

        thread1.start();
        thread2.start();
    }
}
```





#### �������



�����������У�������һ����̬���ַ������� `LOCK_STRING`�������߳� `thread1` �� `thread2` ��ʹ�� `synchronized` �ؼ��ֶ�����ַ���������м������� `thread1` ����ͬ����ʱ�����ȡ�� `LOCK_STRING` ���������`thread2` ����Ҫ�ȴ� `thread1` �ͷ�������ܽ���ͬ���顣

### ʹ�� `synchronized` ���ַ������ڵ�����

#### 1. �ַ��������ص�Ӱ��



Java �е��ַ��������ػỺ���ַ���������ͬ���������ַ�����ָ��ͬһ����������ܻᵼ�²�ͬ�������ʹ����ͬ���������ַ�������ʱ���໥Ӱ�죬���������ͬ�����⡣

#### ʾ������



java











```java
public class StringPoolSyncProblem {
    public static void main(String[] args) {
        String lock1 = "hello";
        String lock2 = "hello";

        Thread thread1 = new Thread(() -> {
            synchronized (lock1) {
                System.out.println("�߳� 1 ��ȡ����");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("�߳� 1 �ͷ���");
            }
        });

        Thread thread2 = new Thread(() -> {
            synchronized (lock2) {
                System.out.println("�߳� 2 ��ȡ����");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("�߳� 2 �ͷ���");
            }
        });

        thread1.start();
        thread2.start();
    }
}
```

#### �������



����������У�`lock1` �� `lock2` ��Ȼ��������ͬ�ı��������������ǵ���������ͬ������ʵ����ָ���ַ����������е�ͬһ��������ˣ�`thread1` �� `thread2` �Ὰ��ͬһ��������ʹ���ǿ������ڲ�ͬ��ҵ���߼���ʹ�á�

#### 2. ���ɿص�������



�����ַ�����ʹ�÷ǳ��㷺�����ѱ�֤������Ӧ�ó����в����������ط�ʹ����ͬ���ַ������м���������ܻᵼ�²��ɿص������������Ӵ���ĸ����Ժͳ���ķ��ա�

### ����



ͨ������£�������ʹ���ַ�����Ϊ�����������Ҫ����ͬ������������ʹ��ר�Ŵ����Ķ�����Ϊ�������磺



java











```java
private final Object lock = new Object();

public void someMethod() {
    synchronized (lock) {
        // ͬ�������
    }
}
```



�������Ա������ַ��������ش��������⣬ʹ����ʹ�ø��ӿɿغͰ�ȫ��

## Ϊʲô_wait_��_notify_����Ҫ��ͬ�����е���

�� Java �У�`wait()`��`notify()` �� `notifyAll()` ������ `Object` ��ķ����������̼߳��Э������Щ����Ҫ�������ͬ���飨`synchronized` �飩�е��ã���Ҫ�ǳ����̰߳�ȫ�����Ĺ����״̬һ�����⼸����Ŀ��ǣ���������ϸ���ͣ�

### 1. �̰߳�ȫ�����Ĺ���



- **���Ļ�ȡ���ͷ�**��`wait()` �������������õ�ǰ�߳��ͷŶ������������ȴ�״̬��ֱ�������̵߳�����ͬ����� `notify()` �� `notifyAll()` ��������������`notify()` �� `notifyAll()` �����������������ڸö����ϵȴ����̡߳�Ϊ�˱�֤��Щ�������̰߳�ȫ�ԣ�Java �涨��Щ����������ͬ�����е��ã���Ϊֻ����ͬ�����У��̲߳��ܻ�ȡ�����������
- **ʾ������**



java











```java
class WaitNotifyExample {
    private final Object lock = new Object();

    public void threadA() {
        synchronized (lock) {
            try {
                System.out.println("�߳� A ����ͬ���飬׼���ȴ�");
                // ���� wait() �������ͷ���������ȴ�״̬
                lock.wait(); 
                System.out.println("�߳� A �����ѣ�����ִ��");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void threadB() {
        synchronized (lock) {
            System.out.println("�߳� B ����ͬ���飬׼�������߳�");
            // ���� notify() ������������ lock �����ϵȴ���һ���߳�
            lock.notify(); 
            System.out.println("�߳� B �����̺߳󣬼���ִ��");
        }
    }

    public static void main(String[] args) {
        WaitNotifyExample example = new WaitNotifyExample();
        Thread a = new Thread(example::threadA);
        Thread b = new Thread(example::threadB);
        a.start();
        try {
            // ���߳� A ��ִ��
            Thread.sleep(1000); 
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        b.start();
    }
}
```



- **�������**�������������У��߳� A ����ͬ�������� `wait()` ��������ʱ�����ͷ� `lock` �������������ȴ�״̬���߳� B ������ͬ���飬���� `notify()` ���������߳� A����� `wait()`��`notify()` ��������ͬ�����е��ã��߳̾��޷���ȷ�ػ�ȡ���ͷŶ���������ᵼ���̰߳�ȫ���⡣

### 2. ״̬һ����



- **��֤������ԭ����**��`wait()`��`notify()` �� `notifyAll()` ����ͨ���빲������״̬�仯��������ڵ�����Щ����ʱ����Ҫȷ���Թ������״̬�ļ����޸���ԭ���Եģ�������־�̬������ͬ������Ա�֤��ͬһʱ��ֻ��һ���߳��ܹ����ʺ��޸Ĺ�������״̬���Ӷ���֤״̬��һ���ԡ�
- **ʾ������**



java











```java
class SharedResource {
    private boolean isReady = false;
    private final Object lock = new Object();

    public void waitForReady() {
        synchronized (lock) {
            while (!isReady) {
                try {
                    // ��״̬������ʱ���̵߳ȴ�
                    lock.wait(); 
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("��Դ��׼���ã�����ʹ��");
        }
    }

    public void setReady() {
        synchronized (lock) {
            isReady = true;
            // ״̬�ı�󣬻��ѵȴ����߳�
            lock.notifyAll(); 
        }
    }
}
```







- **�������**������������У�`waitForReady()` �������� `isReady` ״̬�����״̬����������� `wait()` �����ȴ���`setReady()` �������޸� `isReady` ״̬������ `notifyAll()` �������ѵȴ����̡߳�ͬ����ȷ����״̬����״̬�޸Ĳ�����ԭ���ԣ�������һ���߳��ڼ��״̬���޸�״̬ǰ����һ���̶߳�״̬�������޸ĵ������

### 3. ����Ƿ�״̬�쳣



�������ͬ�����е��� `wait()`��`notify()` �� `notifyAll()` ������Java �������JVM�����׳� `IllegalMonitorStateException` �쳣��������Ϊ��Щ���������ڶ���ļ�������������ֻ���ڻ�ȡ������ļ���������ܵ�����Щ������



����������Ϊ�˱�֤�̰߳�ȫ��������ȷ�����Լ��������״̬��һ���ԣ�`wait()`��`notify()` �� `notifyAll()` ����������ͬ�����е��á�



## ʲô����̻߳����_WAITING_״̬

�߳̽���WAITING״̬�����¼������:
1. ����Object.wait()�������÷�����ʹ�õ�ǰ�߳̽���ȴ�״̬���ȴ������̵߳���ͬһ�������notify()��
notifyAI0)�������Ѹ��̡߳�
2. ����Thread.join()�������÷�����ʹ�õ�ǰ�̵߳ȴ�ָ���̵߳Ľ�������ָ���߳̽���ʱ����ǰ�߳̽�����
�ѡ�
3. ����LockSupport.park()�������÷�����ʹ�õ�ǰ�̵߳ȴ���ֱ����ȡLockSupportָ������ɻ����̱߳�
�жϡ����ȡ�
4. �ڻ�ȡ��ʱ�������ǰ�߳�û�л�ȡ�������ͻ����ȴ�״̬���ȴ������߳��ͷ�����
5. �ȴ�I/O����:���߳���ִ���ڼ���������Ҫ�ȴ�I/0������ɵ�������������WAITING״̬��ֱ��/O0��
����ɡ�


��Ҫע����ǣ����߳̽���WAITING״̬ʱ�����ͷ����е�������Ŀ����ռ��CPU��Դ������̳߳�ʱ�䴦�ڵ�
��״̬�����ܻ����CPU�Ŀ��У������Ҫע�������ֲ���Ҫ�ĵȴ���


## ʲô�ǲ��ɱ���󣬶�д������ʲô����
���ɱ����(lmmutable object)��һ��һ����������״̬�Ͳ��ܱ��޸ĵĶ�����ava�У����ɱ�������
String���������͵İ�װ��(��Integer��Double��)��
���ɱ�����д���������°���:
1. �̰߳�ȫ:���ɱ�������̰߳�ȫ�ģ���Ϊ���ǲ��ᱻ�����߳��޸ġ���ˣ�����߳̿���ͬʱʹ�ò��ɱ��
����������ͬ����ʩ��
2. ����������:���ڲ��ɱ�����״̬���ܱ��޸ģ���˲���Ҫʹ���������������ķ��ʡ���������������Ŀ� ��
���ԣ��Ӷ�����˳�������ܡ�
3. �����Ż�:���ڲ��ɱ����һ����������״̬�Ͳ��ܱ��޸ģ���˿��Խ��������������������Ϊ�������
ֵ�����ڻ����ʹ��֮�䷢���ı䣬�Ӷ��������򻺴���״̬���޸Ķ����µĻ���ʧЧ���⡣


��Ҫע����ǣ���Ȼ���ɱ�����������ŵ㣬������Ҳ��һЩȱ�㡣���磬�����µĲ��ɱ����ȴ����ɱ������
Ҫ������ڴ棬��Ϊÿ��״̬�ı䶼��Ҫ�����µĶ�����ˣ�����Ʋ���Ӧ��ʱ��Ӧ���ݾ������������Ҫ����
�����Ƿ�ʹ�ò��ɱ����

## ʲô��α���������Լ���ν��

### α��������Ķ���



�ڼ����ϵͳ�У�CPU �����Ի����У�Cache Line��Ϊ��λ�������ݴ洢�ʹ��䣬ͨ��һ�������д�СΪ 64 �ֽڡ�α����False Sharing����ָ����߳�ͬʱ���ʲ�ͬ�ı���������Щ����ǡ��λ��ͬһ���������У�������һ���߳��޸��˸û������е�ĳ������ʱ���ᵼ������������ʧЧ�������߳��ٷ��ʸû������е���������ʱ������Ҫ���´����ڴ��м������ݣ��Ӷ�Ӱ���˳�������ܡ�

### ʾ������˵��



�������������� `a` �� `b` λ��ͬһ���������У��߳� 1 Ƶ���޸ı��� `a`���߳� 2 Ƶ����ȡ���� `b`�����߳� 1 �޸� `a` ʱ���û����е�״̬���Ϊ��Ч���߳� 2 �ٶ�ȡ `b` ʱ�����ڻ�����ʧЧ������Ҫ�����ڴ����¼��ذ��� `b` �Ļ����У��������˲���Ҫ�����ܿ�����

### ����ʾ������α��������



java











```java
public class FalseSharingExample {
    private static class Data {
        long value1;
        long value2;
    }

    private static final int THREAD_COUNT = 2;
    private static final int ITERATIONS = 10000000;

    public static void main(String[] args) throws InterruptedException {
        Data data = new Data();
        Thread[] threads = new Thread[THREAD_COUNT];

        threads[0] = new Thread(() -> {
            for (int i = 0; i < ITERATIONS; i++) {
                data.value1++;
            }
        });

        threads[1] = new Thread(() -> {
            for (int i = 0; i < ITERATIONS; i++) {
                data.value2++;
            }
        });

        long startTime = System.currentTimeMillis();
        for (Thread thread : threads) {
            thread.start();
        }
        for (Thread thread : threads) {
            thread.join();
        }
        long endTime = System.currentTimeMillis();
        System.out.println("ִ��ʱ��: " + (endTime - startTime) + " ����");
    }
}
```



�����������У�`value1` �� `value2` �ܿ���λ��ͬһ���������У������̷ֱ߳�����ǽ��в����������α�������⣬Ӱ�����ܡ�

### ���α��������ķ���

#### 1. ��������䣨Padding��



ͨ���ڱ���֮������㹻���������ݣ�ȷ��ÿ����Ҫ�������ʵı���λ�ڲ�ͬ�Ļ������С�



java











```java
public class PaddingSolution {
    private static class Data {
        // ������ݣ�ȷ�� value1 ����ռ��һ��������
        long p1, p2, p3, p4, p5, p6, p7;
        long value1;
        // ������ݣ�ȷ�� value2 ����ռ��һ��������
        long q1, q2, q3, q4, q5, q6, q7;
        long value2;
    }

    private static final int THREAD_COUNT = 2;
    private static final int ITERATIONS = 10000000;

    public static void main(String[] args) throws InterruptedException {
        Data data = new Data();
        Thread[] threads = new Thread[THREAD_COUNT];

        threads[0] = new Thread(() -> {
            for (int i = 0; i < ITERATIONS; i++) {
                data.value1++;
            }
        });

        threads[1] = new Thread(() -> {
            for (int i = 0; i < ITERATIONS; i++) {
                data.value2++;
            }
        });

        long startTime = System.currentTimeMillis();
        for (Thread thread : threads) {
            thread.start();
        }
        for (Thread thread : threads) {
            thread.join();
        }
        long endTime = System.currentTimeMillis();
        System.out.println("ִ��ʱ��: " + (endTime - startTime) + " ����");
    }
}
```



������Ľ���Ĵ����У�ͨ�������� `long` ���͵���������ʹ�� `value1` �� `value2` �ֱ�λ�ڲ�ͬ�Ļ������У�������α�������⡣

#### 2. ʹ�� `@Contended` ע�⣨Java 8 �����ϣ�



Java 8 ������ `@Contended` ע�⣬�����Զ����л�������䡣ʹ�ø�ע����Ҫ���� JVM ���� `-XX:-RestrictContended`��



java











```java
import sun.misc.Contended;

public class ContendedSolution {
    @Contended
    private static class Data {
        long value1;
        long value2;
    }

    private static final int THREAD_COUNT = 2;
    private static final int ITERATIONS = 10000000;

    public static void main(String[] args) throws InterruptedException {
        Data data = new Data();
        Thread[] threads = new Thread[THREAD_COUNT];

        threads[0] = new Thread(() -> {
            for (int i = 0; i < ITERATIONS; i++) {
                data.value1++;
            }
        });

        threads[1] = new Thread(() -> {
            for (int i = 0; i < ITERATIONS; i++) {
                data.value2++;
            }
        });

        long startTime = System.currentTimeMillis();
        for (Thread thread : threads) {
            thread.start();
        }
        for (Thread thread : threads) {
            thread.join();
        }
        long endTime = System.currentTimeMillis();
        System.out.println("ִ��ʱ��: " + (endTime - startTime) + " ����");
    }
}
```



`@Contended` ע����Զ��ڱ�����Χ���������ݣ�ȷ������λ�ڲ�ͬ�Ļ������У��Ӷ�����α����

#### 3. ���ݽṹ����Ż�



��������ݽṹʱ�����Խ���������ͬ�̷߳��ʵı����ֿ��洢����������λ��ͬһ���������С����磬��ԭ���洢��һ�������еĲ�ͬ�̷߳��ʵ����ݲ�ֳɶ�����飬ÿ�����鵥���洢һ���̷߳��ʵ����ݡ�



java











```java
public class DataStructureOptimization {
    private static final int THREAD_COUNT = 2;
    private static final int ITERATIONS = 10000000;

    public static void main(String[] args) throws InterruptedException {
        long[] array1 = new long[1];
        long[] array2 = new long[1];

        Thread[] threads = new Thread[THREAD_COUNT];

        threads[0] = new Thread(() -> {
            for (int i = 0; i < ITERATIONS; i++) {
                array1[0]++;
            }
        });

        threads[1] = new Thread(() -> {
            for (int i = 0; i < ITERATIONS; i++) {
                array2[0]++;
            }
        });

        long startTime = System.currentTimeMillis();
        for (Thread thread : threads) {
            thread.start();
        }
        for (Thread thread : threads) {
            thread.join();
        }
        long endTime = System.currentTimeMillis();
        System.out.println("ִ��ʱ��: " + (endTime - startTime) + " ����");
    }
}
```







�����ʾ���У���ԭ������λ��ͬһ�������е����������ֱ�洢�ڲ�ͬ�������У�������α����Ŀ����ԡ�


## �����߳�Ϊ�ε���start������run����

����start���������jvm����һ���µ��̣߳�����������߳���ִ���� run( ����������Ĵ���
�顣��������������߳�ͬʱ���У�ÿ���̶߳�ӵ�ж�����ִ�������ģ�����ζ�����ǿ����ڲ�������ŵ����
��ִ������

��˲�ͬ�����ֱ�ӵ��� run(��������������һ����ͨ�ķ������ã����ᴴ���µ��̡߳��෴�������ڵ�ǰ��
�̵���������ִ�� run()�����еĴ��롣�⽫���´����˳��ִ�У�û�в����Կ��ԣ���Ϊ���Ƕ���ͬһ���߳���
ִ�С�

��������ǳ���Ҫ����Ϊ���̱߳�̵�һ����ҪĿ����ʵ�ֲ����ԣ��Ӷ���߳�������ܺ���Ӧ������ͨ������
start() �������������ö�˴����������ƣ�ͬʱִ�ж���̣߳��Ը���Ч��������񡣶�ֱ�ӵ��� run() ����ֻ
�ǰ���һ��ķ���˳��ִ�д��룬�޷����Ӷ��̵߳�Ǳ������ˣ�ʹ�� start0) �����������߳���ʵ�ֲ����ԵĹ�
����

## ReentrantLock��tryLock()��lock()����������
1. tryLock()��ʾ���Լ��������ܼӵ���Ҳ���ܼӲ������÷������������̣߳�����ӵ����򷵻�true��û�м�
���򷵻�false
2. lock0)��ʾ�����������̻߳�����ֱ���ӵ���������Ҳû�з���ֵ

