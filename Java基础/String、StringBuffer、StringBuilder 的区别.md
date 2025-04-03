### 1. 可变性



- **String**：`String` 类是不可变的，一旦创建了一个 `String` 对象，它的内容就不能被改变。如果对 `String` 对象进行拼接、替换等操作，实际上是创建了一个新的 `String` 对象。



java











```java
String str = "Hello";
str = str + " World"; 
// 这里的 str 实际上指向了一个新的 String 对象
```



- **StringBuffer**：`StringBuffer` 是可变的，它提供了一系列方法（如 `append()`、`insert()`、`delete()` 等）来修改字符串的内容，而不会创建新的对象。



java











```java
StringBuffer sb = new StringBuffer("Hello");
sb.append(" World"); 
// sb 指向的对象内容被修改，没有创建新对象
```



- **StringBuilder**：`StringBuilder` 同样是可变的，和 `StringBuffer` 类似，也提供了修改字符串内容的方法，操作时不会创建新对象。













```java
StringBuilder sbuilder = new StringBuilder("Hello");
sbuilder.append(" World"); 
// sbuilder 指向的对象内容被修改，没有创建新对象
```

### 2. 线程安全性



- **String**：由于 `String` 是不可变的，所以它是线程安全的。多个线程可以同时访问同一个 `String` 对象，不会出现数据不一致的问题。
- **StringBuffer**：`StringBuffer` 是线程安全的，它的所有公共方法都被 `synchronized` 关键字修饰，这意味着在多线程环境下，同一时间只能有一个线程访问 `StringBuffer` 的方法，从而保证了线程安全。











```java
public synchronized StringBuffer append(String str) {
    // 方法实现
    return this;
}
```



- **StringBuilder**：`StringBuilder` 是非线程安全的，它没有使用 `synchronized` 关键字修饰方法。因此，在多线程环境下，如果多个线程同时访问和修改 `StringBuilder` 对象，可能会出现数据不一致的问题。

### 3. 性能



- **String**：由于每次对 `String` 对象进行修改都会创建新的对象，频繁的修改操作会导致大量的内存开销和垃圾回收，性能较低。特别是在需要进行大量字符串拼接的场景下，不建议使用 `String`。
- **StringBuffer**：由于 `StringBuffer` 的方法是同步的，在多线程环境下，线程同步会带来一定的性能开销。但在单线程环境下，由于需要进行同步检查，其性能也不如 `StringBuilder`。
- **StringBuilder**：`StringBuilder` 没有线程同步的开销，在单线程环境下，它的性能比 `StringBuffer` 要高，是进行大量字符串拼接等操作的首选。

### 4. 使用场景



- **String**：适用于字符串内容不经常发生变化的场景，例如存储常量字符串、作为方法的参数等。













```java
public class StringExample {
    public static final String GREETING = "Hello";

    public static void printMessage(String message) {
        System.out.println(message);
    }

    public static void main(String[] args) {
        printMessage(GREETING);
    }
}
```







- **StringBuffer**：适用于多线程环境下需要对字符串进行频繁修改的场景，例如在多线程的日志记录、数据拼接等场景中可以使用 `StringBuffer` 来保证线程安全。
- **StringBuilder**：适用于单线程环境下需要对字符串进行频繁修改的场景，例如在循环中进行字符串拼接等操作，使用 `StringBuilder` 可以提高性能。














```java
public class StringBuilderExample {
    public static void main(String[] args) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            sb.append(i);
        }
        System.out.println(sb.toString());
    }
}
```







## String 为什么是不可变的?

### 1. 安全性



- **多线程安全**：在多线程环境下，不可变对象是线程安全的。因为多个线程可以同时访问同一个 `String` 对象，而不用担心数据被意外修改。例如，在一个多线程的 Web 应用中，多个线程可能会同时读取一个存储用户信息的 `String` 对象，如果 `String` 是可变的，就可能会出现数据不一致的问题。











```java
public class StringThreadSafetyExample {
    public static final String MESSAGE = "Welcome to the application";

    public static void main(String[] args) {
        // 多个线程可以安全地访问 MESSAGE
        Thread t1 = new Thread(() -> System.out.println(MESSAGE));
        Thread t2 = new Thread(() -> System.out.println(MESSAGE));
        t1.start();
        t2.start();
    }
}
```



- **安全敏感信息**：`String` 常用于存储安全敏感信息，如密码、数据库连接字符串等。如果 `String` 是可变的，那么这些信息可能会在不经意间被修改，从而导致安全漏洞。例如，在一个验证用户登录的方法中，如果密码 `String` 对象被意外修改，可能会导致验证失败或安全信息泄露。

### 2. 性能优化



- **字符串常量池**：Java 中的字符串常量池是一种重要的性能优化机制。由于 `String` 是不可变的，相同内容的字符串可以共享同一个对象，存储在字符串常量池中。这样可以减少内存的使用，提高性能。例如：









```java
String str1 = "Hello";
String str2 = "Hello";
// str1 和 str2 指向字符串常量池中的同一个对象
System.out.println(str1 == str2); 
```



- **哈希码缓存**：`String` 类会缓存对象的哈希码，因为 `String` 是不可变的，所以它的哈希码在对象创建时就可以被计算并缓存起来。这样在使用 `String` 对象作为哈希表（如 `HashMap`、`HashSet`）的键时，可以提高哈希表的性能，避免重复计算哈希码。








```java
public final class String
    implements java.io.Serializable, Comparable<String>, CharSequence {
    private int hash; // 缓存的哈希码

    @Override
    public int hashCode() {
        int h = hash;
        if (h == 0 && value.length > 0) {
            hash = h = isLatin1() ? StringLatin1.hashCode(value)
                                  : StringUTF16.hashCode(value);
        }
        return h;
    }
}
```

### 3. 设计简洁性



- **方法调用一致性**：不可变的 `String` 使得 `String` 类的方法调用更加简单和一致。因为 `String` 对象的内容不会改变，所以方法调用的结果是可预测的，不会产生副作用。例如，`substring()`、`toUpperCase()` 等方法都会返回一个新的 `String` 对象，而不会修改原对象。











```java
String str = "Hello";
String newStr = str.toUpperCase();
// str 仍然是 "Hello"，newStr 是 "HELLO"
System.out.println(str); 
System.out.println(newStr); 
```

### 4. 类的实现机制



从 `String` 类的源码来看，它的核心是一个 `private final char[] value` 数组来存储字符串的字符序列。`private` 修饰符保证了外部无法直接访问该数组，`final` 修饰符保证了该数组的引用不能被改变。同时，`String` 类中没有提供修改该数组内容的方法，从而保证了 `String` 对象的不可变性。










```java
public final class String
    implements java.io.Serializable, Comparable<String>, CharSequence {
    /** The value is used for character storage. */
    private final char value[];

    // 构造方法
    public String(String original) {
        this.value = original.value;
        this.hash = original.hash;
    }

    // 其他方法...
}
```
## String 真正不可变有下面几点原因 

### **1. `String` 类被声明为 `final`**

- `String` 类被声明为 `final`，这意味着它不能被继承。
- 防止子类通过继承修改 `String` 的行为或破坏其不可变性。

------

### **2. 字符数组被声明为 `final`**

- `String` 内部使用一个 `final` 修饰的字符数组（`private final char value[]`）来存储字符串内容。
- 由于 `final` 修饰，字符数组的引用不能被修改，即不能指向另一个数组。
- 字符数组本身是私有的，外部无法直接访问或修改。

------

### **3. 没有修改字符数组的方法**

- `String` 类没有提供任何可以修改内部字符数组的方法。
- 所有看似修改字符串的方法（如 `substring()`、`replace()` 等）实际上都是返回一个新的 `String` 对象，而不是修改原对象。

------

### **4. 字符串常量池的优化**

- Java 使用字符串常量池来存储字符串字面量（如 `"hello"`）。
- 如果多个字符串变量引用相同的字面量，它们会指向常量池中的同一个对象。
- 如果 `String` 是可变的，修改一个字符串可能会影响其他引用该字符串的变量，导致不可预测的行为。

------

### **5. 安全性**

- 不可变性使得 `String` 对象在多线程环境下是线程安全的，无需额外的同步机制。
- `String` 常用于敏感操作（如文件名、URL、密码等），不可变性可以防止恶意修改。

------

### **6. 哈希码缓存**

- `String` 类内部缓存了哈希码（`private int hash`），因为字符串不可变，哈希码只需计算一次。
- 如果字符串可变，哈希码可能会变化，导致在哈希表（如 `HashMap`）中出现问题。

------

### **7. 示例代码**


```
String str = "hello";
str = str + " world"; // 创建了一个新的 String 对象
System.out.println(str); // 输出 "hello world"
```

在上面的代码中：

1. 初始时，`str` 指向字符串常量池中的 `"hello"`。
2. 执行 `str + " world"` 时，会创建一个新的 `String` 对象 `"hello world"`，而原对象 `"hello"` 不会被修改。
3. `str` 现在指向新的对象 `"hello world"`。

------

### **8. 总结**

`String` 的不可变性是通过以下机制实现的：

1. `final` 类，防止继承。
2. `final` 字符数组，防止引用被修改。
3. 不提供修改字符数组的方法。
4. 字符串常量池的优化。
5. 安全性考虑。
6. 哈希码缓存。

这些机制共同确保了 `String` 对象的不可变性，从而提高了安全性、线程安全性和性能。






## 字符串拼接用“+” 还是 StringBuilder?

### 使用 “+” 进行字符串拼接

#### 优点



- **语法简洁**：使用 “+” 进行字符串拼接是最直观、最简单的方式，代码可读性高，对于少量字符串的拼接非常方便。

**底层原理 ： 通过 StringBuilder 调用 append() 方法实现的，拼接完成之后调用 toString() 得到一个 String 对象**

java











```java
String str1 = "Hello";
String str2 = " World";
String result = str1 + str2;
System.out.println(result); 
```

#### 缺点



- **性能较差**：当使用 “+” 进行大量字符串拼接时，会产生较多的临时对象。因为 `String` 类是不可变的，每次使用 “+” 拼接字符串时，实际上都会创建一个新的 `String` 对象，这会导致频繁的内存分配和垃圾回收，性能开销较大。



java











```java
String result = "";
for (int i = 0; i < 1000; i++) {
    result = result + i; 
}
```



在上述代码中，每次循环都会创建一个新的 `String` 对象，循环 1000 次就会创建大量的临时对象，严重影响性能。

### 使用 `StringBuilder` 进行字符串拼接

#### 优点



- **性能高**：`StringBuilder` 是可变的，它内部维护了一个字符数组，在进行字符串拼接时，会直接在这个数组上进行操作，不会创建大量的临时对象。当需要进行大量字符串拼接时，`StringBuilder` 的性能明显优于使用 “+” 拼接。



java











```java
StringBuilder sb = new StringBuilder();
for (int i = 0; i < 1000; i++) {
    sb.append(i); 
}
String result = sb.toString();
```



在上述代码中，使用 `StringBuilder` 的 `append` 方法进行拼接，只会创建一个 `StringBuilder` 对象和最终的 `String` 对象，避免了大量临时对象的创建，提高了性能。



- **灵活性高**：`StringBuilder` 提供了丰富的方法，如 `append`、`insert`、`delete` 等，可以方便地对字符串进行各种操作。

#### 缺点



- **语法相对复杂**：相比于使用 “+” 进行拼接，使用 `StringBuilder` 需要创建对象、调用方法，代码相对复杂一些。

### 使用建议



- **少量字符串拼接**：如果只是进行少量的字符串拼接，例如拼接两三个字符串，使用 “+” 是比较合适的，因为它的语法简洁，不会对性能产生明显影响。














```java
String name = "John";
String message = "Hello, " + name + "!";
System.out.println(message);
```



- **大量字符串拼接**：当需要进行大量的字符串拼接，尤其是在循环中进行拼接时，建议使用 `StringBuilder`。这样可以显著提高性能，减少内存开销。













```java
StringBuilder sb = new StringBuilder();
for (int i = 0; i < 10000; i++) {
    sb.append("Item " + i + ", ");
}
String result = sb.toString();
```







- **多线程环境**：如果在多线程环境下进行字符串拼接，需要考虑线程安全问题。`StringBuilder` 是非线程安全的，此时应该使用 `StringBuffer`，它是线程安全的，虽然性能比 `StringBuilder` 稍低，但可以保证在多线程环境下的数据一致性。











```java
StringBuffer sb = new StringBuffer();
for (int i = 0; i < 1000; i++) {
    sb.append(i);
}
String result = sb.toString();
```

## String#equals() 和 Object#equals() 有何区别？

### 方法定义



- **`Object#equals()`**：`Object` 类是 Java 中所有类的基类，`Object#equals()` 是 `Object` 类中定义的一个方法。其定义如下：










```java
public boolean equals(Object obj) {
    return (this == obj);
}
```



- **`String#equals()`**：`String` 类继承自 `Object` 类，并重写了 `equals()` 方法。`String` 类中 `equals()` 方法用于比较两个字符串的内容是否相等。

### 比较逻辑



- **`Object#equals()`**：`Object` 类中 `equals()` 方法默认的比较逻辑是使用 `==` 运算符来比较两个对象的引用是否相等，也就是判断两个对象是否指向同一个内存地址。只有当两个引用指向同一个对象时，该方法才会返回 `true`。示例如下：












```java
Object obj1 = new Object();
Object obj2 = new Object();
Object obj3 = obj1;

System.out.println(obj1.equals(obj2)); 
System.out.println(obj1.equals(obj3)); 
```



上述代码中，`obj1` 和 `obj2` 是不同的对象实例，它们的内存地址不同，所以 `obj1.equals(obj2)` 返回 `false`；而 `obj3` 指向 `obj1` 相同的内存地址，因此 `obj1.equals(obj3)` 返回 `true`。



- **`String#equals()`**：`String` 类重写的 `equals()` 方法会先检查传入的对象是否为 `String` 类型，如果是，则进一步比较两个字符串的字符序列是否相同。以下是 `String#equals()` 方法的简化逻辑：









```java
public boolean equals(Object anObject) {
    if (this == anObject) {
        return true;
    }
    if (anObject instanceof String) {
        String anotherString = (String)anObject;
        int n = value.length;
        if (n == anotherString.value.length) {
            char v1[] = value;
            char v2[] = anotherString.value;
            int i = 0;
            while (n-- != 0) {
                if (v1[i] != v2[i])
                    return false;
                i++;
            }
            return true;
        }
    }
    return false;
}
```



示例代码如下：















```java
String str1 = new String("Hello");
String str2 = new String("Hello");
String str3 = str1;

System.out.println(str1.equals(str2)); 
System.out.println(str1.equals(str3)); 
```







在这个例子中，虽然 `str1` 和 `str2` 是不同的 `String` 对象实例（内存地址不同），但它们的字符序列都是 `"Hello"`，所以 `str1.equals(str2)` 返回 `true`；`str3` 指向 `str1` 相同的对象，因此 `str1.equals(str3)` 也返回 `true`。

### 重写情况



- **`Object#equals()`**：`Object` 类中的 `equals()` 方法是最基础的实现，它没有考虑对象的具体内容，只是简单比较引用。在很多情况下，这个默认实现不能满足业务需求，所以需要在具体的子类中重写该方法。
- **`String#equals()`**：`String` 类根据自身的特性，重写了 `Object` 类的 `equals()` 方法，使得该方法可以用于比较字符串的内容。这体现了多态性，不同的类可以根据自身需求定制 `equals()` 方法的行为。


## String#intern 方法有什么作用?

### 作用概述



`String#intern` 方法的主要作用是将字符串对象添加到字符串常量池中，并返回常量池中的该字符串的引用。如果常量池中已经存在与该字符串内容相同的字符串，则直接返回常量池中字符串的引用；如果不存在，则将该字符串添加到常量池中，然后返回常量池中该字符串的引用。

### 原理分析



在 Java 中，字符串常量池是一个特殊的内存区域，用于存储字符串常量。当使用双引号直接创建字符串时，例如 `String str = "hello";`，这个字符串会被自动放入字符串常量池中。而通过 `new` 关键字创建的字符串对象，会在堆内存中分配空间，默认情况下不会进入字符串常量池。`intern` 方法就是提供了一种将堆中的字符串对象加入到字符串常量池的途径。

### 示例代码







```java
public class StringInternExample {
    public static void main(String[] args) {
        // 使用双引号创建字符串，直接进入常量池
        String str1 = "hello";

        // 使用 new 关键字创建字符串，在堆中分配内存
        String str2 = new String("hello");

        // 调用 intern 方法，将 str2 对应的字符串加入常量池
        String str3 = str2.intern();

        // 比较引用
        System.out.println(str1 == str2); 
        System.out.println(str1 == str3); 
    }
}
```



在上述代码中，`str1` 是通过双引号创建的字符串，它直接存储在字符串常量池中。`str2` 是通过 `new` 关键字创建的，存储在堆内存中。调用 `str2.intern()` 后，由于常量池中已经存在 `"hello"` 字符串，所以 `str3` 得到的是常量池中 `"hello"` 字符串的引用，因此 `str1 == str3` 结果为 `true`。

### 使用场景



- **节省内存**：当程序中需要大量使用相同内容的字符串时，使用 `intern` 方法可以让这些字符串共享常量池中的同一个实例，从而减少内存开销。例如，在处理大量的文本数据时，如果有很多重复的字符串，调用 `intern` 方法可以有效节省内存。














```java
import java.util.ArrayList;
import java.util.List;

public class MemorySavingExample {
    public static void main(String[] args) {
        List<String> stringList = new ArrayList<>();
        for (int i = 0; i < 100000; i++) {
            String str = new String("example").intern();
            stringList.add(str);
        }
    }
}
```







- **字符串比较优化**：在进行字符串比较时，如果使用 `==` 比较常量池中的字符串引用，比使用 `equals` 方法比较字符串内容的效率要高。因为 `==` 比较的是引用是否相等，而 `equals` 方法需要逐个字符比较字符串内容。

### 注意事项



- **性能开销**：`intern` 方法的调用本身有一定的性能开销，因为它需要在常量池中查找是否已经存在相同内容的字符串。如果常量池中的字符串数量非常大，查找操作可能会变得比较慢。
- **常量池容量**：字符串常量池的容量是有限的，如果大量调用 `intern` 方法将不同的字符串添加到常量池中，可能会导致常量池溢出，从而引发内存问题。从 Java 7 开始，字符串常量池从方法区移到了堆中，一定程度上缓解了这个问题，但仍然需要谨慎使用。

## Java中两个字符串相加的底层原理

在 Java 中，两个字符串相加通常使用 `+` 运算符或者 `concat` 方法，下面分别介绍它们的底层实现原理。

### 使用 `+` 运算符进行字符串相加

#### 编译期优化

在 Java 中，当使用 `+` 运算符进行字符串相加时，编译器会对其进行优化。如果相加的字符串都是常量（在编译时就能确定值的字符串），编译器会在编译阶段将它们合并为一个字符串。

java











```java
public class StringAdditionExample {
    public static void main(String[] args) {
        String str1 = "Hello";
        String str2 = "World";
        String result = str1 + str2;
        System.out.println(result);
    }
}
```

对于上述代码，在编译时，`javac` 编译器会将 `str1 + str2` 转换为 `new StringBuilder().append(str1).append(str2).toString()`。也就是说，编译器会自动使用 `StringBuilder` 来进行字符串的拼接操作。

#### 运行期处理

`StringBuilder` 是一个可变的字符序列，它内部维护了一个字符数组。当调用 `append` 方法时，会将新的字符串追加到字符数组中。如果字符数组的容量不够，`StringBuilder` 会自动进行扩容。最后调用 `toString` 方法时，会创建一个新的 `String` 对象，将 `StringBuilder` 中的字符序列复制到该 `String` 对象中。

### 使用 `concat` 方法进行字符串相加

#### 方法定义

`concat` 方法是 `String` 类的一个实例方法，用于将指定的字符串连接到该字符串的末尾。其方法签名如下：

java











```java
public String concat(String str)
```

#### 底层实现原理

`concat` 方法的底层实现是通过创建一个新的字符数组，将原字符串和要拼接的字符串的字符依次复制到新数组中，然后使用这个新数组创建一个新的 `String` 对象。以下是 `concat` 方法的简化实现：

java











```java
public String concat(String str) {
    int otherLen = str.length();
    if (otherLen == 0) {
        return this;
    }
    int len = value.length;
    char buf[] = Arrays.copyOf(value, len + otherLen);
    str.getChars(buf, len);
    return new String(buf, true);
}
```

其中，`value` 是 `String` 对象内部存储字符的字符数组。`Arrays.copyOf` 方法用于创建一个新的字符数组，其长度为原字符串和要拼接字符串的长度之和。然后将原字符串的字符复制到新数组中，接着将要拼接的字符串的字符复制到新数组的后面部分。最后使用这个新的字符数组创建一个新的 `String` 对象并返回。

### 性能比较

- **`+` 运算符**：在编译期会被优化为 `StringBuilder` 的操作，在循环中如果频繁使用 `+` 进行字符串拼接，由于每次循环都会创建新的 `StringBuilder` 对象，可能会导致性能问题。
- **`concat` 方法**：每次调用都会创建一个新的字符数组和 `String` 对象，对于少量的字符串拼接，性能尚可，但在大量拼接的场景下，性能不如 `StringBuilder`。

综上所述，在需要频繁进行字符串拼接的场景下，建议直接使用 `StringBuilder` 或 `StringBuffer`（`StringBuffer` 是线程安全的，性能稍低于 `StringBuilder`）来提高性能。



## String 怎么转成 Integer 的？原理？

### 1. 使用 `Integer.parseInt()` 方法

#### 示例代码













```java
public class StringToIntegerExample {
    public static void main(String[] args) {
        String str = "123";
        int num = Integer.parseInt(str);
        System.out.println("转换后的整数: " + num);
    }
}
```

#### 原理



`Integer.parseInt()` 是 `Integer` 类的一个静态方法，它的作用是将字符串参数解析为有符号的十进制整数。该方法的实现原理大致如下：



- **检查字符串是否为空或 `null`**：如果传入的字符串为 `null` 或者长度为 0，会抛出 `NullPointerException` 或 `NumberFormatException`。
- **处理符号**：首先检查字符串的第一个字符是否为 `+` 或 `-`，如果是，则记录符号信息，并从第二个字符开始解析。
- **字符验证**：遍历字符串中的每个字符，检查是否为有效的数字字符（即字符的 ASCII 码在 `'0'` 到 `'9'` 之间）。如果遇到非数字字符，会抛出 `NumberFormatException`。
- **数字转换**：将每个数字字符转换为对应的数值，并根据其在字符串中的位置进行加权求和。例如，对于字符串 `"123"`，先将字符 `'1'` 转换为数值 1，乘以 100；字符 `'2'` 转换为数值 2，乘以 10；字符 `'3'` 转换为数值 3，乘以 1。最后将这些结果相加得到 123。
- **范围检查**：在转换过程中，会检查结果是否超出了 `int` 类型的取值范围（-2147483648 到 2147483647）。如果超出范围，会抛出 `NumberFormatException`。

### 2. 使用 `Integer.valueOf()` 方法

#### 示例代码












```java
public class StringToIntegerExample {
    public static void main(String[] args) {
        String str = "123";
        Integer integerObj = Integer.valueOf(str);
        System.out.println("转换后的 Integer 对象: " + integerObj);
    }
}
```

#### 原理



`Integer.valueOf()` 也是 `Integer` 类的一个静态方法，它实际上是在内部调用了 `Integer.parseInt()` 方法进行字符串到整数的转换，然后将转换后的 `int` 值封装成一个 `Integer` 对象返回。该方法还使用了缓存机制，对于值在 -128 到 127 之间的 `Integer` 对象，会从缓存中获取，以提高性能和节省内存。例如：













```java
Integer a = Integer.valueOf(100);
Integer b = Integer.valueOf(100);
System.out.println(a == b); // 输出 true，因为 100 在缓存范围内，a 和 b 引用同一个对象

Integer c = Integer.valueOf(200);
Integer d = Integer.valueOf(200);
System.out.println(c == d); // 输出 false，因为 200 不在缓存范围内，c 和 d 是不同的对象
```





### 3. 使用 `new Integer(String s)` 构造方法（不推荐，已弃用）

#### 示例代码











```java
public class StringToIntegerExample {
    public static void main(String[] args) {
        String str = "123";
        Integer integerObj = new Integer(str);
        System.out.println("转换后的 Integer 对象: " + integerObj);
    }
}
```





#### 原理



`new Integer(String s)` 构造方法同样是调用 `Integer.parseInt()` 方法将字符串转换为 `int` 值，然后将其封装成 `Integer` 对象。不过，从 Java 9 开始，这个构造方法已经被弃用，建议使用 `Integer.valueOf()` 方法代替，因为 `Integer.valueOf()` 方法可以利用缓存机制，性能更好。



综上所述，在将 `String` 转换为 `Integer` 时，`Integer.parseInt()` 和 `Integer.valueOf()` 是常用的方法，它们的核心都是基于对字符串中字符的解析和转换，并且会进行有效性检查和范围判断。

## 数组有没有 length()这个方法? String 有没有 length()这个方法？

数组没有 length()这个方法，有 length 的属性。


String 有有 length()这个方法。

