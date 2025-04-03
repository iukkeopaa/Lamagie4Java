`hashCode()` 和 `equals()` 是 Java 中 `Object` 类定义的两个重要方法，它们在 Java 的集合框架、哈希表等场景中起着关键作用，二者之间存在着紧密的联系。下面详细介绍它们之间的关系。

### 1. 方法定义



- **`equals()` 方法**：用于比较两个对象是否相等。在 `Object` 类中，`equals()` 方法默认比较的是两个对象的引用是否相等，即是否指向同一个内存地址。但很多类会重写这个方法，根据对象的内容来判断是否相等。



收起



java









```java
public boolean equals(Object obj) {
    return (this == obj);
}
```



- **`hashCode()` 方法**：返回对象的哈希码值，这是一个由对象的内部地址经过哈希算法转换得到的整数。在 `Object` 类中，`hashCode()` 方法通常是根据对象的内存地址计算得到的。



收起



java









```java
public native int hashCode();
```

### 2. 二者之间的关系规则



- **规则一：如果两个对象通过 `equals()` 方法比较结果为 `true`，那么它们的 `hashCode()` 值必须相同**
  当重写 `equals()` 方法时，必须同时重写 `hashCode()` 方法，以保证上述规则成立。这是因为在 Java 的哈希表（如 `HashMap`、`HashSet` 等）中，首先会使用 `hashCode()` 方法来确定对象在哈希表中的位置，然后再使用 `equals()` 方法来进一步判断两个对象是否相等。如果两个相等的对象 `hashCode()` 值不同，那么它们可能会被存储在哈希表的不同位置，导致哈希表无法正常工作。



收起



java









```java
import java.util.Objects;

class Person {
    private String name;
    private int age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    // 重写 equals 方法
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return age == person.age &&
                Objects.equals(name, person.name);
    }

    // 重写 hashCode 方法
    @Override
    public int hashCode() {
        return Objects.hash(name, age);
    }
}

public class Main {
    public static void main(String[] args) {
        Person p1 = new Person("Alice", 25);
        Person p2 = new Person("Alice", 25);
        if (p1.equals(p2)) {
            System.out.println("p1 和 p2 相等");
            System.out.println("p1 的 hashCode: " + p1.hashCode());
            System.out.println("p2 的 hashCode: " + p2.hashCode());
        }
    }
}
```



在上述代码中，`Person` 类重写了 `equals()` 和 `hashCode()` 方法。当 `p1` 和 `p2` 通过 `equals()` 方法比较结果为 `true` 时，它们的 `hashCode()` 值也相同。



- **规则二：如果两个对象的 `hashCode()` 值相同，它们不一定通过 `equals()` 方法比较结果为 `true`**
  这是因为哈希码可能会发生碰撞，即不同的对象可能会产生相同的哈希码。哈希算法是将任意长度的输入转换为固定长度的输出，因此不可避免地会出现不同的输入产生相同输出的情况。



收起



java









```java
class CollisionExample {
    private int value;

    public CollisionExample(int value) {
        this.value = value;
    }

    @Override
    public int hashCode() {
        // 故意让所有对象的 hashCode 都相同
        return 1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CollisionExample that = (CollisionExample) o;
        return value == that.value;
    }
}

public class HashCodeCollision {
    public static void main(String[] args) {
        CollisionExample ce1 = new CollisionExample(10);
        CollisionExample ce2 = new CollisionExample(20);
        System.out.println("ce1 的 hashCode: " + ce1.hashCode());
        System.out.println("ce2 的 hashCode: " + ce2.hashCode());
        System.out.println("ce1 和 ce2 是否相等: " + ce1.equals(ce2));
    }
}
```







在上述代码中，`CollisionExample` 类的所有对象的 `hashCode()` 值都被设置为 1，但通过 `equals()` 方法比较时，`ce1` 和 `ce2` 并不相等。

### 3. 总结



- `equals()` 方法用于判断两个对象的内容是否相等，而 `hashCode()` 方法用于生成对象的哈希码，主要用于哈希表等数据结构的快速查找。
- 为了保证 Java 集合框架等的正确性，当重写 `equals()` 方法时，必须重写 `hashCode()` 方法，确保相等的对象具有相同的哈希码。
- 哈希码相同的对象不一定相等，因为可能会发生哈希碰撞。