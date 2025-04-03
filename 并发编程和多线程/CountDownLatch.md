## CountDownLatch和Semaphore的区别和底层原理

CountDownLatch表示计数器，可以给CountDownLatch设置一个数字，一个线程调用CountDownLatch的
await(0)将会阻塞，其他线程可以调用CountDownLatch的countDown()方法来对CountDownLatch中的数字减
当数字被减成0后，所有await的线程都将被唤醒.
            
对应的底层原理就是，调用await(方法的线程会利用AQS排队，一旦数字被减为0，则会将AQS中排队的线程依次
唤醒。

Semaphore表示信号量，可以设置许可的个数，表示同时允许最多多少个线程使用该信号量，通过acquire()来获
取许可，如果没有许可可用则线程阻塞，并通过AQS来排队，可以通过release()方法来释放许可，当某个线程释
放了某个许可后，会从AOS中正在排队的第一个线程开始依次唤醒，直到没有空闲许可


1. CountDownLatch 简单的说就是一个线程等待，直到他所等待的其他线程都执

行完成并且调用 countDown()方法发出通知后，当前线程才可以继续执行。

2. cyclicBarrier 是所有线程都进行等待，直到所有线程都准备好进入 await()方

法之后，所有线程同时开始执行！

3. CountDownLatch 的计数器只能使用一次。而 CyclicBarrier 的计数器可以使

用 reset() 方法重置。所以 CyclicBarrier 能处理更为复杂的业务场景，比如如果

计算发生错误，可以重置计数器，并让线程们重新执行一次。

4. CyclicBarrier 还提供其他有用的方法，比如 getNumberWaiting 方法可以获

得 CyclicBarrier 阻塞的线程数量。isBroken 方法用来知道阻塞的线程是否被中断。

如果被中断返回 true，否则返回 false。