## 什么是Stream

### 1. 创建 Stream



在进行 Stream 操作之前，需要先创建一个 Stream 对象。可以通过多种方式创建 Stream，以下是几种常见的创建方式：

#### 从集合创建






```java
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class StreamCreation {
    public static void main(String[] args) {
        List<String> list = Arrays.asList("apple", "banana", "cherry");
        Stream<String> stream = list.stream(); 
    }
}
```

#### 从数组创建









```java
import java.util.stream.Stream;

public class StreamFromArray {
    public static void main(String[] args) {
        String[] array = {"apple", "banana", "cherry"};
        Stream<String> stream = Arrays.stream(array); 
    }
}
```

#### 使用 `Stream.of()` 方法创建








```java
import java.util.stream.Stream;

public class StreamOfExample {
    public static void main(String[] args) {
        Stream<String> stream = Stream.of("apple", "banana", "cherry"); 
    }
}
```

### 2. 中间操作



中间操作会返回一个新的 Stream 对象，允许对 Stream 进行链式调用。常见的中间操作有：

#### `filter()`：过滤元素



`filter()` 方法接受一个 `Predicate` 函数式接口作为参数，用于过滤 Stream 中不符合条件的元素。













```java
import java.util.Arrays;
import java.util.List;

public class FilterExample {
    public static void main(String[] args) {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6);
        numbers.stream()
               .filter(n -> n % 2 == 0) 
               .forEach(System.out::println); 
    }
}
```

#### `map()`：映射元素



`map()` 方法接受一个 `Function` 函数式接口作为参数，用于将 Stream 中的每个元素映射为另一个元素。











```java
import java.util.Arrays;
import java.util.List;

public class MapExample {
    public static void main(String[] args) {
        List<String> words = Arrays.asList("hello", "world");
        words.stream()
             .map(String::toUpperCase) 
             .forEach(System.out::println); 
    }
}
```

#### `sorted()`：排序元素



`sorted()` 方法用于对 Stream 中的元素进行排序。可以使用默认排序，也可以传入一个 `Comparator` 来指定排序规则。









```java
import java.util.Arrays;
import java.util.List;

public class SortedExample {
    public static void main(String[] args) {
        List<Integer> numbers = Arrays.asList(3, 1, 4, 2, 5);
        numbers.stream()
               .sorted() 
               .forEach(System.out::println); 
    }
}
```

#### `distinct()`：去重元素



`distinct()` 方法用于去除 Stream 中重复的元素。






```java
import java.util.Arrays;
import java.util.List;

public class DistinctExample {
    public static void main(String[] args) {
        List<Integer> numbers = Arrays.asList(1, 2, 2, 3, 3, 3);
        numbers.stream()
               .distinct() 
               .forEach(System.out::println); 
    }
}
```

### 3. 终端操作



终端操作会触发 Stream 的执行，并产生一个最终结果。常见的终端操作有：

#### `forEach()`：遍历元素



`forEach()` 方法接受一个 `Consumer` 函数式接口作为参数，用于对 Stream 中的每个元素执行指定的操作。










```java
import java.util.Arrays;
import java.util.List;

public class ForEachExample {
    public static void main(String[] args) {
        List<String> names = Arrays.asList("Alice", "Bob", "Charlie");
        names.stream()
             .forEach(name -> System.out.println(name)); 
    }
}
```

#### `collect()`：收集元素



`collect()` 方法用于将 Stream 中的元素收集到一个集合或其他数据结构中。












```java
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CollectExample {
    public static void main(String[] args) {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
        List<Integer> evenNumbers = numbers.stream()
                                           .filter(n -> n % 2 == 0)
                                           .collect(Collectors.toList()); 
        System.out.println(evenNumbers);
    }
}
```

#### `count()`：统计元素数量



`count()` 方法用于统计 Stream 中元素的数量。










```java
import java.util.Arrays;
import java.util.List;

public class CountExample {
    public static void main(String[] args) {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
        long count = numbers.stream()
                            .filter(n -> n % 2 == 0)
                            .count(); 
        System.out.println("偶数的数量: " + count);
    }
}
```





#### `reduce()`：聚合元素



`reduce()` 方法用于将 Stream 中的元素进行聚合操作，返回一个可选的结果。








```java
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class ReduceExample {
    public static void main(String[] args) {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
        Optional<Integer> sum = numbers.stream()
                                       .reduce((a, b) -> a + b); 
        sum.ifPresent(System.out::println); 
    }
}
```

## Stream流是线程安全的吗

Java 的 Stream 流本身并非天然具备线程安全特性，其线程安全性取决于具体的使用场景和操作方式，下面从不同方面进行详细分析。

### 顺序流（Sequential Stream）

顺序流是单线程处理的，即按照元素的顺序依次对每个元素执行操作，因此在单线程环境下使用顺序流不存在线程安全问题。

#### 示例代码




```java
import java.util.Arrays;
import java.util.List;

public class SequentialStreamExample {
    public static void main(String[] args) {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
        int sum = numbers.stream()
                .mapToInt(Integer::intValue)
                .sum();
        System.out.println("Sum: " + sum);
    }
}
```

在这个例子中，`numbers.stream()` 创建的是顺序流，它会在单线程中依次处理每个元素，不会出现多线程竞争的情况。

### 并行流（Parallel Stream）

并行流会将流中的元素分成多个子流，然后在多个线程中并行处理这些子流，这种情况下如果操作共享可变状态，就可能引发线程安全问题。

#### 存在线程安全问题的示例







```java
import java.util.Arrays;
import java.util.List;

public class ParallelStreamUnsafeExample {
    static int sharedCounter = 0;

    public static void main(String[] args) {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
        numbers.parallelStream()
               .forEach(num -> sharedCounter += num);
        System.out.println("Shared Counter: " + sharedCounter);
    }
}
```

在上述代码中，`sharedCounter` 是一个共享的可变变量，多个线程在并行处理元素时会同时对其进行修改，这就可能导致数据竞争和不一致的结果。因为 `+=` 操作不是原子操作，多个线程同时执行该操作时可能会覆盖彼此的更新。

#### 解决方案

- **使用线程安全的数据结构**：可以使用 `java.util.concurrent` 包中的线程安全数据结构，例如 `AtomicInteger` 来替代普通的 `int` 变量。







```java
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ParallelStreamSafeExample {
    static AtomicInteger sharedCounter = new AtomicInteger(0);

    public static void main(String[] args) {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
        numbers.parallelStream()
               .forEach(num -> sharedCounter.addAndGet(num));
        System.out.println("Shared Counter: " + sharedCounter.get());
    }
}
```





在这个改进后的代码中，`AtomicInteger` 提供了原子性的操作方法，如 `addAndGet`，可以确保在多线程环境下对计数器的更新是线程安全的。

- **使用同步机制**：也可以使用 `synchronized` 关键字或 `Lock` 接口来实现同步，保证对共享变量的访问是线程安全的。不过这种方式可能会影响性能，因为会引入线程的阻塞和竞争。

### 总结

顺序流在单线程环境下不存在线程安全问题；而并行流在处理共享可变状态时需要特别注意线程安全，可以通过使用线程安全的数据结构或同步机制来解决相关问题。



