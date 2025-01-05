## 并发编程Bug的源头


### 源头之一：缓存导致的可见性问题

一个线程对共享变量的修改，另外一个线程能够立刻看到，我们称为可见性


### 源头之二：线程切换带来的原子性问题

我们把一个或者多个操作在 CPU 执行的过程中不被中断的特性称为原子性

### 源头之三：编译优化带来的有序性问题

在 Java 领域一个经典的案例就是利用双重检查创建单例对象，例如下面的代码：在获取实
例 getInstance() 的方法中，我们首先判断 instance 是否为空，如果为空，则锁定
Singleton.class 并再次检查 instance 是否为空，如果还为空则创建 Singleton 的一个实
例

    public class Singleton {
    // 使用volatile关键字修饰实例变量，保证多线程环境下的可见性以及禁止指令重排序
    private static volatile Singleton instance;

    // 私有构造函数，防止外部通过new关键字创建实例
    private Singleton() {}

    // 提供公共的静态方法来获取单例实例
    public static Singleton getInstance() {
        // 第一次检查，如果实例已经存在，直接返回，避免不必要的同步开销
        if (instance == null) {
            // 同步代码块，保证同一时刻只有一个线程能进入创建实例的逻辑
            synchronized (Singleton.class) {
                // 第二次检查，进入同步块后再次检查实例是否已经存在，
                // 因为可能有其他线程在等待进入同步块时已经创建好了实例
                if (instance == null) {
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }
}


假设有两个线程 A、B 同时调用 getInstance() 方法，他们会同时发现 instance ==
null ，于是同时对 Singleton.class 加锁，此时 JVM 保证只有一个线程能够加锁成功（假
设是线程 A），另外一个线程则会处于等待状态（假设是线程 B）；线程 A 会创建一个
Singleton 实例，之后释放锁，锁释放后，线程 B 被唤醒，线程 B 再次尝试加锁，此时是
可以加锁成功的，加锁成功后，线程 B 检查 instance == null 时会发现，已经创建过
Singleton 实例了，所以线程 B 不会再创建一个 Singleton 实例。


这看上去一切都很完美，无懈可击，但实际上这个 getInstance() 方法并不完美。问题出在
哪里呢？出在 new 操作上，我们以为的 new 操作应该是：
1. 分配一块内存 M；
2. 在内存 M 上初始化 Singleton 对象；
3. 然后 M 的地址赋值给 instance 变量。

   但是实际上优化后的执行路径却是这样的：

1. 分配一块内存 M；
2. 将 M 的地址赋值给 instance 变量；
3. 最后在内存 M 上初始化 Singleton 对象。
   优化后会导致什么问题呢？我们假设线程 A 先执行 getInstance() 方法，当执行完指令 2
   时恰好发生了线程切换，切换到了线程 B 上；如果此时线程 B 也执行 getInstance() 方
   法，那么线程 B 在执行第一个判断时会发现 instance != null ，所以直接返回
   instance，而此时的 instance 是没有初始化过的，如果我们这个时候访问 instance 的成
   员变量就可能触发空指针异常。