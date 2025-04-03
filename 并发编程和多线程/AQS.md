## 谈谈你对AQS的理解，AQS如何实现可重入锁?
1. AQS是一个JAVA线程同步的框架。是JDK中很多锁工具的核心实现框架
2. 在AQS中，维护了一个信号量state和一个线程组成的双向链表队列。其中，这个线程队列，就是用来给线程
排队的，而state就像是一个红绿灯，用来控制线程排队或者放行的。在不同的场景下，有不用的意义。

3. 在可重入锁这个场景下，state就用来表示加锁的次数。0标识无锁，每加一次锁，state就加1，释放锁state就减1。
## 说下对AQS的理解

AQS(AbstractQueuedSynchronizer)是Java并发编程中的一个重要组件，它是一个抽象类，提供了线程同步
的底层实现机制。AQS的作用是实现线程的同步和互斥操作，它提供了两种主要的锁机制，分别是排他锁和共享
锁。

排他锁也称为独占锁，在多个线程竞争同一共享资源时，同一时刻只允许一个线程访问该共享资源，即多个线程中
只有一个线程获得锁资源。在AOS中，排他锁是通过内置的同步状态来实现的，当同步状态为0时，表示锁是未被
获取的;当同步状态大于0时，表示锁已经被获取目被占用;当同步状态小于0时，表示锁已经被获取但是处于等
待状态。

共享锁允许多个线程同时获得锁资源，但是在同一时刻只有一个线程可以获取到锁的拥有权，其他线程需要等待该
线程释放锁。在AQS中，共享锁的实现与排他锁类似，也是通过内置的同步状态来实现的，

AQS通过一个内置的FIFO(先进先出)等待队列来实现线程的排队和调度。当线程需要获取锁资源时，如果锁已
G
经被其他线程获取，则该线程会被加入到等待队列中等待。当锁被释放时，等待队列中的第一个线程会获得锁资源
并继续执行，

在实现AQS时，需要继承自AQS类并实现其抽象方法。其中比较重要的方法包括:tryAcquire(和tryRelease0)方
法，用于实现锁的获取和释放;acquire(0)和release()方法，用于实现阻塞和唤醒操作;isHeldExclusively()方法
用于判断是否是排他锁。

总之，AOS是Java并发编程中的重要组件之一，它提供了线程同步的底层实现机制，在使用AOS时，需要根据具
体的应用场最选择合适的锁机制来实现线程的同步和互斥操作。

## AQS支持的两种同步方式

1、独占式

2、共享式

这样方便使用者实现不同类型的同步组件，独占式如 ReentrantLock，共享式如Semaphore，CountDownLatch，组合式的如 ReentrantReadWriteLock。总之，AQS 为使用提供了底层支撑，如何组装实现，使用者可以自由发挥。

## AQS的设计模式

### 1. AQS 用的设计模式



AQS（AbstractQueuedSynchronizer）主要使用了模板方法模式。

#### 原理



模板方法模式定义了一个算法的骨架，将一些步骤的实现延迟到子类中。在 AQS 中，`AbstractQueuedSynchronizer` 类提供了一个框架来实现锁和同步器，它定义了一系列的方法和流程，其中一些方法是抽象的，需要子类去实现具体的逻辑。

#### 示例



比如，`ReentrantLock` 是基于 AQS 实现的，`ReentrantLock` 的内部类 `Sync` 继承自 `AbstractQueuedSynchronizer`，并实现了 `tryAcquire` 和 `tryRelease` 等方法，这些方法在 AQS 的获取和释放资源的模板流程中被调用。









```java
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Lock;

// 自定义锁
class MyLock implements Lock {
    private final Sync sync = new Sync();

    private static class Sync extends AbstractQueuedSynchronizer {
        @Override
        protected boolean tryAcquire(int arg) {
            if (compareAndSetState(0, 1)) {
                setExclusiveOwnerThread(Thread.currentThread());
                return true;
            }
            return false;
        }

        @Override
        protected boolean tryRelease(int arg) {
            if (getState() == 0) {
                throw new IllegalMonitorStateException();
            }
            setExclusiveOwnerThread(null);
            setState(0);
            return true;
        }
    }

    @Override
    public void lock() {
        sync.acquire(1);
    }

    @Override
    public void unlock() {
        sync.release(1);
    }

    // 其他方法省略
}
```

### 2. AQS 获取互斥量对应操作系统的 PV 原语



AQS 获取互斥量的过程可以类比操作系统中的 PV 原语：

#### P 原语（申请资源）



在 AQS 中，当一个线程尝试获取锁（互斥量）时，会调用 `acquire` 方法，这个过程类似于 P 原语。P 原语会将信号量的值减 1，如果信号量的值小于 0，则线程进入阻塞状态。在 AQS 中，如果 `tryAcquire` 方法返回失败，线程会被加入到等待队列中阻塞，直到锁被释放。

#### V 原语（释放资源）



当一个线程释放锁时，会调用 `release` 方法，这类似于 V 原语。V 原语会将信号量的值加 1，如果信号量的值小于等于 0，则唤醒一个等待的线程。在 AQS 中，`tryRelease` 方法释放锁后，如果有等待的线程，会唤醒队列中的头节点线程。

### 3. AQS 互斥量的表示（volatile 的 state）



AQS 中使用 `volatile` 修饰的 `state` 变量来表示互斥量的状态：

#### 原理



`volatile` 关键字保证了 `state` 变量在多线程之间的可见性，即一个线程对 `state` 的修改会立即被其他线程看到。`state` 的值可以表示锁的不同状态，例如，对于独占锁，`state` 为 0 表示锁未被持有，`state` 为 1 表示锁已被持有。

#### 示例









```java
public abstract class AbstractQueuedSynchronizer
    extends AbstractOwnableSynchronizer
    implements java.io.Serializable {
    private volatile int state;

    protected final int getState() {
        return state;
    }

    protected final void setState(int newState) {
        state = newState;
    }

    protected final boolean compareAndSetState(int expect, int update) {
        return unsafe.compareAndSwapInt(this, stateOffset, expect, update);
    }
}
```

## AQS唤醒节点，为什么从后向前找

在 Java 的抽象队列同步器（Abstract Queued Synchronizer，简称 AQS）中，唤醒节点时从后往前遍历等待队列，主要是基于入队操作的特性以及避免并发问题等方面的考虑，下面为你详细分析：

### 入队操作特性



AQS 的等待队列是一个双向链表结构，新节点入队时采用的是尾插法。在多线程环境下，多个线程可能同时尝试将新节点插入到队列尾部。入队操作是一个先修改新节点的 `prev` 指针指向当前尾节点，然后使用 CAS（Compare-And-Swap）操作将新节点设置为新的尾节点的过程。这个过程不是原子的，可能会出现以下情况：












```plaintext
// 假设当前队列情况为： head <-> node1 <-> node2 <-> tail
// 线程 A 准备将 node3 入队
node3.prev = tail; // 此时 node3 的 prev 指向 tail

// 在线程 A 执行 CAS 操作将 node3 设置为新的尾节点之前
// 线程 B 也准备将 node4 入队
node4.prev = tail; // 此时 node4 的 prev 也指向 tail
```



如果采用从前往后遍历的方式唤醒节点，当一个新节点入队时，它的 `prev` 指针已经设置好了，但它还没有成功成为新的尾节点（即 `tail.next` 还没有指向该新节点），那么从前往后遍历可能会遗漏这个还未完全入队的节点。而从后往前遍历，由于节点的 `prev` 指针在入队时是先设置好的，只要通过 `prev` 指针就可以准确地找到队列中的所有节点。

### 避免并发问题



从后往前遍历可以避免并发修改带来的问题。在多线程环境下，节点的入队和出队操作是并发进行的。如果从前往后遍历，在遍历过程中可能会遇到某个节点的 `next` 指针被修改（例如该节点被移除队列），导致遍历中断或出现错误。而从后往前遍历，每个节点的 `prev` 指针一旦设置好就不会再被修改（除非节点被移除队列，但移除操作会保证 `prev` 指针的一致性），因此可以更安全地遍历整个队列。

### 代码示例与说明



以下是 AQS 中 `unparkSuccessor` 方法的简化代码，该方法用于唤醒后继节点：











```java
private void unparkSuccessor(Node node) {
    // 尝试将当前节点的状态设置为 0
    int ws = node.waitStatus;
    if (ws < 0)
        compareAndSetWaitStatus(node, ws, 0);

    // 从后往前找有效的后继节点
    Node s = node.next;
    if (s == null || s.waitStatus > 0) {
        s = null;
        // 从尾节点开始往前遍历
        for (Node t = tail; t != null && t != node; t = t.prev)
            if (t.waitStatus <= 0)
                s = t;
    }
    if (s != null)
        LockSupport.unpark(s.thread);
}
```







在上述代码中，当 `node` 的后继节点 `s` 为空或者状态无效时，会从尾节点 `tail` 开始往前遍历，找到一个状态有效的节点并唤醒它。



综上所述，AQS 唤醒节点时从后往前找是为了适应入队操作的特性，避免遗漏还未完全入队的节点，同时也可以避免并发修改带来的问题，保证遍历的安全性和准确性。




