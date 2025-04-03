### 1. 唤醒因 `Object.wait()` 阻塞的线程



当线程调用对象的 `wait()` 方法时，它会释放对象的锁并进入等待状态，直到其他线程调用该对象的 `notify()` 或 `notifyAll()` 方法来唤醒它。

#### 示例代码



java











```java
class WaitNotifyExample {
    public static void main(String[] args) {
        final Object lock = new Object();

        // 等待线程
        Thread waitingThread = new Thread(() -> {
            synchronized (lock) {
                try {
                    System.out.println("线程进入等待状态");
                    lock.wait();
                    System.out.println("线程被唤醒");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        // 唤醒线程
        Thread notifyingThread = new Thread(() -> {
            try {
                // 让等待线程先执行
                Thread.sleep(1000); 
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (lock) {
                System.out.println("准备唤醒等待线程");
                lock.notify();
            }
        });

        waitingThread.start();
        notifyingThread.start();
    }
}
```

#### 代码解释



- `waitingThread` 线程在获取 `lock` 对象的锁后，调用 `lock.wait()` 方法进入等待状态，并释放锁。
- `notifyingThread` 线程在休眠 1 秒后，获取 `lock` 对象的锁，调用 `lock.notify()` 方法唤醒一个在 `lock` 对象上等待的线程。
- `notify()` 方法只会唤醒一个等待的线程，而 `notifyAll()` 方法会唤醒所有在该对象上等待的线程。

### 2. 唤醒因 `Thread.sleep()` 阻塞的线程



`Thread.sleep()` 方法会使当前线程暂停执行指定的时间。如果需要提前唤醒因 `Thread.sleep()` 阻塞的线程，可以使用 `Thread.interrupt()` 方法。

#### 示例代码



java











```java
class SleepInterruptExample {
    public static void main(String[] args) {
        Thread sleepingThread = new Thread(() -> {
            try {
                System.out.println("线程开始睡眠");
                Thread.sleep(5000);
                System.out.println("线程睡眠结束");
            } catch (InterruptedException e) {
                System.out.println("线程被中断，提前唤醒");
            }
        });

        sleepingThread.start();

        try {
            // 让线程先睡一会儿
            Thread.sleep(1000); 
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 中断线程
        sleepingThread.interrupt(); 
    }
}
```

#### 代码解释



- `sleepingThread` 线程调用 `Thread.sleep(5000)` 方法进入睡眠状态。
- `main` 线程在休眠 1 秒后，调用 `sleepingThread.interrupt()` 方法中断 `sleepingThread` 线程。
- `sleepingThread` 线程在被中断时会抛出 `InterruptedException` 异常，从而提前结束睡眠。

### 3. 唤醒因 `Thread.join()` 阻塞的线程



当一个线程调用另一个线程的 `join()` 方法时，它会阻塞直到被调用的线程执行完毕。可以使用 `Thread.interrupt()` 方法来中断等待的线程。

#### 示例代码



java











```java
class JoinInterruptExample {
    public static void main(String[] args) {
        Thread targetThread = new Thread(() -> {
            try {
                System.out.println("目标线程开始执行");
                Thread.sleep(3000);
                System.out.println("目标线程执行结束");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread waitingThread = new Thread(() -> {
            try {
                System.out.println("等待线程开始等待目标线程执行完毕");
                targetThread.join();
                System.out.println("等待线程继续执行");
            } catch (InterruptedException e) {
                System.out.println("等待线程被中断，提前唤醒");
            }
        });

        targetThread.start();
        waitingThread.start();

        try {
            // 让线程先执行一会儿
            Thread.sleep(1000); 
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 中断等待线程
        waitingThread.interrupt(); 
    }
}
```

#### 代码解释



- `waitingThread` 线程调用 `targetThread.join()` 方法，阻塞等待 `targetThread` 线程执行完毕。
- `main` 线程在休眠 1 秒后，调用 `waitingThread.interrupt()` 方法中断 `waitingThread` 线程。
- `waitingThread` 线程在被中断时会抛出 `InterruptedException` 异常，从而提前结束等待。

### 4. 唤醒因 I/O 操作阻塞的线程



对于因 I/O 操作阻塞的线程，可以通过关闭相应的 I/O 流来唤醒线程。例如，对于 `Socket` 连接的输入流读取操作，可以关闭 `Socket` 来中断阻塞。

#### 示例代码



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
                    System.out.println("开始读取数据");
                    int data;
                    while ((data = inputStream.read()) != -1) {
                        // 处理数据
                    }
                    System.out.println("读取数据结束");
                } catch (IOException e) {
                    System.out.println("读取数据被中断");
                }
            });

            readingThread.start();

            // 模拟一段时间后关闭连接
            Thread.sleep(2000);
            socket.close();

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
```





#### 代码解释



- `readingThread` 线程在调用 `inputStream.read()` 方法时会阻塞，等待数据的到来。
- `main` 线程在休眠 2 秒后，调用 `socket.close()` 方法关闭 `Socket` 连接，从而中断 `readingThread` 线程的 I/O 操作，使其抛出 `IOException` 异常。