### 1. ������ `Object.wait()` �������߳�



���̵߳��ö���� `wait()` ����ʱ�������ͷŶ������������ȴ�״̬��ֱ�������̵߳��øö���� `notify()` �� `notifyAll()` ��������������

#### ʾ������



java











```java
class WaitNotifyExample {
    public static void main(String[] args) {
        final Object lock = new Object();

        // �ȴ��߳�
        Thread waitingThread = new Thread(() -> {
            synchronized (lock) {
                try {
                    System.out.println("�߳̽���ȴ�״̬");
                    lock.wait();
                    System.out.println("�̱߳�����");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        // �����߳�
        Thread notifyingThread = new Thread(() -> {
            try {
                // �õȴ��߳���ִ��
                Thread.sleep(1000); 
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (lock) {
                System.out.println("׼�����ѵȴ��߳�");
                lock.notify();
            }
        });

        waitingThread.start();
        notifyingThread.start();
    }
}
```

#### �������



- `waitingThread` �߳��ڻ�ȡ `lock` ��������󣬵��� `lock.wait()` ��������ȴ�״̬�����ͷ�����
- `notifyingThread` �߳������� 1 ��󣬻�ȡ `lock` ������������� `lock.notify()` ��������һ���� `lock` �����ϵȴ����̡߳�
- `notify()` ����ֻ�ỽ��һ���ȴ����̣߳��� `notifyAll()` �����ỽ�������ڸö����ϵȴ����̡߳�

### 2. ������ `Thread.sleep()` �������߳�



`Thread.sleep()` ������ʹ��ǰ�߳���ִͣ��ָ����ʱ�䡣�����Ҫ��ǰ������ `Thread.sleep()` �������̣߳�����ʹ�� `Thread.interrupt()` ������

#### ʾ������



java











```java
class SleepInterruptExample {
    public static void main(String[] args) {
        Thread sleepingThread = new Thread(() -> {
            try {
                System.out.println("�߳̿�ʼ˯��");
                Thread.sleep(5000);
                System.out.println("�߳�˯�߽���");
            } catch (InterruptedException e) {
                System.out.println("�̱߳��жϣ���ǰ����");
            }
        });

        sleepingThread.start();

        try {
            // ���߳���˯һ���
            Thread.sleep(1000); 
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // �ж��߳�
        sleepingThread.interrupt(); 
    }
}
```

#### �������



- `sleepingThread` �̵߳��� `Thread.sleep(5000)` ��������˯��״̬��
- `main` �߳������� 1 ��󣬵��� `sleepingThread.interrupt()` �����ж� `sleepingThread` �̡߳�
- `sleepingThread` �߳��ڱ��ж�ʱ���׳� `InterruptedException` �쳣���Ӷ���ǰ����˯�ߡ�

### 3. ������ `Thread.join()` �������߳�



��һ���̵߳�����һ���̵߳� `join()` ����ʱ����������ֱ�������õ��߳�ִ����ϡ�����ʹ�� `Thread.interrupt()` �������жϵȴ����̡߳�

#### ʾ������



java











```java
class JoinInterruptExample {
    public static void main(String[] args) {
        Thread targetThread = new Thread(() -> {
            try {
                System.out.println("Ŀ���߳̿�ʼִ��");
                Thread.sleep(3000);
                System.out.println("Ŀ���߳�ִ�н���");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread waitingThread = new Thread(() -> {
            try {
                System.out.println("�ȴ��߳̿�ʼ�ȴ�Ŀ���߳�ִ�����");
                targetThread.join();
                System.out.println("�ȴ��̼߳���ִ��");
            } catch (InterruptedException e) {
                System.out.println("�ȴ��̱߳��жϣ���ǰ����");
            }
        });

        targetThread.start();
        waitingThread.start();

        try {
            // ���߳���ִ��һ���
            Thread.sleep(1000); 
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // �жϵȴ��߳�
        waitingThread.interrupt(); 
    }
}
```

#### �������



- `waitingThread` �̵߳��� `targetThread.join()` �����������ȴ� `targetThread` �߳�ִ����ϡ�
- `main` �߳������� 1 ��󣬵��� `waitingThread.interrupt()` �����ж� `waitingThread` �̡߳�
- `waitingThread` �߳��ڱ��ж�ʱ���׳� `InterruptedException` �쳣���Ӷ���ǰ�����ȴ���

### 4. ������ I/O �����������߳�



������ I/O �����������̣߳�����ͨ���ر���Ӧ�� I/O ���������̡߳����磬���� `Socket` ���ӵ���������ȡ���������Թر� `Socket` ���ж�������

#### ʾ������



java











```java
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

class IOInterruptExample {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(8888);
            Socket socket = serverSocket.accept();
            InputStream inputStream = socket.getInputStream();

            Thread readingThread = new Thread(() -> {
                try {
                    System.out.println("��ʼ��ȡ����");
                    int data;
                    while ((data = inputStream.read()) != -1) {
                        // ��������
                    }
                    System.out.println("��ȡ���ݽ���");
                } catch (IOException e) {
                    System.out.println("��ȡ���ݱ��ж�");
                }
            });

            readingThread.start();

            // ģ��һ��ʱ���ر�����
            Thread.sleep(2000);
            socket.close();

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
```





#### �������



- `readingThread` �߳��ڵ��� `inputStream.read()` ����ʱ���������ȴ����ݵĵ�����
- `main` �߳������� 2 ��󣬵��� `socket.close()` �����ر� `Socket` ���ӣ��Ӷ��ж� `readingThread` �̵߳� I/O ������ʹ���׳� `IOException` �쳣��