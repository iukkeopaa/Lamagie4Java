`@Contended` 是 Java 8 引入的一个注解，它主要用于解决伪共享（False Sharing）问题，下面为你详细介绍其作用、原理及使用方式。

### 作用



`@Contended` 注解的主要作用是减少伪共享带来的性能损耗，从而提升多线程程序的性能。伪共享是指多个线程同时访问不同变量，但这些变量却处于同一个缓存行（Cache Line）中，当其中一个线程修改了该缓存行中的某个变量时，会导致整个缓存行失效，其他线程需要重新从主内存中加载该缓存行，从而增加了内存访问的开销，降低了程序的性能。



`@Contended` 注解通过在被注解的字段或类的前后添加填充（Padding），使得不同的变量分布在不同的缓存行中，避免了伪共享问题。

### 原理



现代计算机的 CPU 缓存系统是以缓存行为单位进行数据存储和读写的，一个缓存行通常为 64 字节。当多个变量被存储在同一个缓存行中时，即使这些变量在逻辑上是独立的，但由于缓存行的更新机制，它们会相互影响。`@Contended` 注解会在字段或类的前后插入一定数量的字节，使得每个被注解的字段或类占据一个独立的缓存行，从而避免了多个线程同时访问同一个缓存行而导致的伪共享问题。

### 使用方式

#### 1. 注解字段



可以使用 `@Contended` 注解来注解类中的字段，示例代码如下：










```java
import sun.misc.Contended;

// 需要添加 JVM 参数 -XX:-RestrictContended 才能使用该注解
public class ContendedFieldExample {
    @Contended
    private long value1;

    @Contended
    private long value2;

    public long getValue1() {
        return value1;
    }

    public void setValue1(long value1) {
        this.value1 = value1;
    }

    public long getValue2() {
        return value2;
    }

    public void setValue2(long value2) {
        this.value2 = value2;
    }

    public static void main(String[] args) {
        ContendedFieldExample example = new ContendedFieldExample();
        example.setValue1(1L);
        example.setValue2(2L);
        System.out.println("Value 1: " + example.getValue1());
        System.out.println("Value 2: " + example.getValue2());
    }
}
```



在上述代码中，`value1` 和 `value2` 字段都被 `@Contended` 注解，这样它们会被分配到不同的缓存行中，避免了伪共享问题。

#### 2. 注解类



也可以使用 `@Contended` 注解来注解整个类，示例代码如下：











```java
import sun.misc.Contended;

// 需要添加 JVM 参数 -XX:-RestrictContended 才能使用该注解
@Contended
public class ContendedClassExample {
    private long value1;
    private long value2;

    public long getValue1() {
        return value1;
    }

    public void setValue1(long value1) {
        this.value1 = value1;
    }

    public long getValue2() {
        return value2;
    }

    public void setValue2(long value2) {
        this.value2 = value2;
    }

    public static void main(String[] args) {
        ContendedClassExample example = new ContendedClassExample();
        example.setValue1(1L);
        example.setValue2(2L);
        System.out.println("Value 1: " + example.getValue1());
        System.out.println("Value 2: " + example.getValue2());
    }
}
```







在上述代码中，`ContendedClassExample` 类被 `@Contended` 注解，这样该类的所有实例都会被分配到不同的缓存行中，避免了伪共享问题。

### 注意事项



- **JVM 参数**：在 Java 8 及以后的版本中，使用 `@Contended` 注解需要添加 `-XX:-RestrictContended` JVM 参数，否则会抛出 `IllegalAccessError` 异常。
- **性能影响**：虽然 `@Contended` 注解可以解决伪共享问题，但过度使用会增加内存开销，因为每个被注解的字段或类都会占用更多的内存空间。因此，在使用该注解时需要权衡性能和内存开销