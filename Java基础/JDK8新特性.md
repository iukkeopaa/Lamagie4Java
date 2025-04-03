Java 8（JDK 8）是 Java 语言发展历程中的一个重要版本，引入了许多实用且强大的新特性，极大地提升了 Java 的表达能力和开发效率。以下是 JDK 8 的一些主要新特性：

### 1. Lambda 表达式



Lambda 表达式是 JDK 8 中最显著的特性之一，它允许你将函数作为参数传递给方法，或者将代码作为数据处理。Lambda 表达式使代码更加简洁，减少了匿名内部类的使用。



**示例代码**：



收起



java









```java
import java.util.Arrays;
import java.util.List;

public class LambdaExample {
    public static void main(String[] args) {
        List<String> names = Arrays.asList("Alice", "Bob", "Charlie");
        // 使用 Lambda 表达式遍历列表
        names.forEach(name -> System.out.println(name));
    }
}
```

### 2. 函数式接口



函数式接口是指只包含一个抽象方法的接口，通常使用 `@FunctionalInterface` 注解来标识。Lambda 表达式可以被赋值给函数式接口类型的变量。



**示例代码**：



收起



java









```java
@FunctionalInterface
interface MyFunction {
    int apply(int a, int b);
}

public class FunctionalInterfaceExample {
    public static void main(String[] args) {
        // 使用 Lambda 表达式实现函数式接口
        MyFunction add = (a, b) -> a + b;
        System.out.println(add.apply(3, 5)); 
    }
}
```

### 3. Stream API



Stream API 提供了一种高效且易于使用的方式来处理集合数据。它支持链式操作，如过滤、映射、排序等，可以进行并行处理，提高处理大数据集的性能。



**示例代码**：



收起



java









```java
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class StreamAPIExample {
    public static void main(String[] args) {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
        // 过滤出偶数并求和
        int sum = numbers.stream()
                         .filter(n -> n % 2 == 0)
                         .mapToInt(Integer::intValue)
                         .sum();
        System.out.println(sum); 
    }
}
```

### 4. 方法引用



方法引用是 Lambda 表达式的一种简化形式，它允许直接引用已有的方法。方法引用可以使代码更加简洁易读。



**示例代码**：



收起



java









```java
import java.util.Arrays;
import java.util.List;

public class MethodReferenceExample {
    public static void main(String[] args) {
        List<String> names = Arrays.asList("Alice", "Bob", "Charlie");
        // 使用方法引用遍历列表
        names.forEach(System.out::println);
    }
}
```

### 5. 默认方法



默认方法允许在接口中定义具体的方法实现，而不需要实现类去实现这些方法。这使得在不破坏现有实现类的情况下，可以向接口中添加新的方法。



**示例代码**：



收起



java









```java
interface MyInterface {
    void normalMethod();
    // 默认方法
    default void defaultMethod() {
        System.out.println("This is a default method.");
    }
}

class MyClass implements MyInterface {
    @Override
    public void normalMethod() {
        System.out.println("This is a normal method.");
    }
}

public class DefaultMethodExample {
    public static void main(String[] args) {
        MyClass myClass = new MyClass();
        myClass.normalMethod();
        myClass.defaultMethod();
    }
}
```

### 6. 静态方法



JDK 8 允许在接口中定义静态方法，这些方法可以直接通过接口名调用。



**示例代码**：



收起



java









```java
interface MyStaticInterface {
    // 静态方法
    static void staticMethod() {
        System.out.println("This is a static method in interface.");
    }
}

public class StaticMethodExample {
    public static void main(String[] args) {
        MyStaticInterface.staticMethod();
    }
}
```

### 7. 日期和时间 API



JDK 8 引入了全新的日期和时间 API（`java.time` 包），它提供了更加简洁、易用和线程安全的日期和时间处理方式，解决了旧的 `Date` 和 `Calendar` 类的一些问题。



**示例代码**：



收起



java









```java
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

public class DateTimeAPIExample {
    public static void main(String[] args) {
        // 获取当前日期
        LocalDate currentDate = LocalDate.now();
        System.out.println("Current Date: " + currentDate);

        // 获取当前时间
        LocalTime currentTime = LocalTime.now();
        System.out.println("Current Time: " + currentTime);

        // 获取当前日期和时间
        LocalDateTime currentDateTime = LocalDateTime.now();
        System.out.println("Current Date and Time: " + currentDateTime);
    }
}
```

### 8. Optional 类



`Optional` 类是一个容器类，用于表示一个值可能存在或不存在，避免了空指针异常（`NullPointerException`）的问题。



**示例代码**：



收起



java









```java
import java.util.Optional;

public class OptionalExample {
    public static void main(String[] args) {
        String name = null;
        Optional<String> optionalName = Optional.ofNullable(name);
        if (optionalName.isPresent()) {
            System.out.println("Name: " + optionalName.get());
        } else {
            System.out.println("Name is not present.");
        }
    }
}
```







这些新特性使得 Java 更加现代化和高效，为开发者提供了更多的编程选择和便利。



分享



方法引用有哪些使用场景？

并行流和顺序流有什么区别？