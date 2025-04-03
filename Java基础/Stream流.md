## ʲô��Stream

### 1. ���� Stream



�ڽ��� Stream ����֮ǰ����Ҫ�ȴ���һ�� Stream ���󡣿���ͨ�����ַ�ʽ���� Stream�������Ǽ��ֳ����Ĵ�����ʽ��

#### �Ӽ��ϴ���






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

#### �����鴴��









```java
import java.util.stream.Stream;

public class StreamFromArray {
    public static void main(String[] args) {
        String[] array = {"apple", "banana", "cherry"};
        Stream<String> stream = Arrays.stream(array); 
    }
}
```

#### ʹ�� `Stream.of()` ��������








```java
import java.util.stream.Stream;

public class StreamOfExample {
    public static void main(String[] args) {
        Stream<String> stream = Stream.of("apple", "banana", "cherry"); 
    }
}
```

### 2. �м����



�м�����᷵��һ���µ� Stream ��������� Stream ������ʽ���á��������м�����У�

#### `filter()`������Ԫ��



`filter()` ��������һ�� `Predicate` ����ʽ�ӿ���Ϊ���������ڹ��� Stream �в�����������Ԫ�ء�













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

#### `map()`��ӳ��Ԫ��



`map()` ��������һ�� `Function` ����ʽ�ӿ���Ϊ���������ڽ� Stream �е�ÿ��Ԫ��ӳ��Ϊ��һ��Ԫ�ء�











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

#### `sorted()`������Ԫ��



`sorted()` �������ڶ� Stream �е�Ԫ�ؽ������򡣿���ʹ��Ĭ������Ҳ���Դ���һ�� `Comparator` ��ָ���������









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

#### `distinct()`��ȥ��Ԫ��



`distinct()` ��������ȥ�� Stream ���ظ���Ԫ�ء�






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

### 3. �ն˲���



�ն˲����ᴥ�� Stream ��ִ�У�������һ�����ս�����������ն˲����У�

#### `forEach()`������Ԫ��



`forEach()` ��������һ�� `Consumer` ����ʽ�ӿ���Ϊ���������ڶ� Stream �е�ÿ��Ԫ��ִ��ָ���Ĳ�����










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

#### `collect()`���ռ�Ԫ��



`collect()` �������ڽ� Stream �е�Ԫ���ռ���һ�����ϻ��������ݽṹ�С�












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

#### `count()`��ͳ��Ԫ������



`count()` ��������ͳ�� Stream ��Ԫ�ص�������










```java
import java.util.Arrays;
import java.util.List;

public class CountExample {
    public static void main(String[] args) {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
        long count = numbers.stream()
                            .filter(n -> n % 2 == 0)
                            .count(); 
        System.out.println("ż��������: " + count);
    }
}
```





#### `reduce()`���ۺ�Ԫ��



`reduce()` �������ڽ� Stream �е�Ԫ�ؽ��оۺϲ���������һ����ѡ�Ľ����








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

## Stream�����̰߳�ȫ����

Java �� Stream ����������Ȼ�߱��̰߳�ȫ���ԣ����̰߳�ȫ��ȡ���ھ����ʹ�ó����Ͳ�����ʽ������Ӳ�ͬ���������ϸ������

### ˳������Sequential Stream��

˳�����ǵ��̴߳���ģ�������Ԫ�ص�˳�����ζ�ÿ��Ԫ��ִ�в���������ڵ��̻߳�����ʹ��˳�����������̰߳�ȫ���⡣

#### ʾ������




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

����������У�`numbers.stream()` ��������˳�����������ڵ��߳������δ���ÿ��Ԫ�أ�������ֶ��߳̾����������

### ��������Parallel Stream��

�������Ὣ���е�Ԫ�طֳɶ��������Ȼ���ڶ���߳��в��д�����Щ��������������������������ɱ�״̬���Ϳ��������̰߳�ȫ���⡣

#### �����̰߳�ȫ�����ʾ��







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

�����������У�`sharedCounter` ��һ������Ŀɱ����������߳��ڲ��д���Ԫ��ʱ��ͬʱ��������޸ģ���Ϳ��ܵ������ݾ����Ͳ�һ�µĽ������Ϊ `+=` ��������ԭ�Ӳ���������߳�ͬʱִ�иò���ʱ���ܻḲ�Ǳ˴˵ĸ��¡�

#### �������

- **ʹ���̰߳�ȫ�����ݽṹ**������ʹ�� `java.util.concurrent` ���е��̰߳�ȫ���ݽṹ������ `AtomicInteger` �������ͨ�� `int` ������







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





������Ľ���Ĵ����У�`AtomicInteger` �ṩ��ԭ���ԵĲ����������� `addAndGet`������ȷ���ڶ��̻߳����¶Լ������ĸ������̰߳�ȫ�ġ�

- **ʹ��ͬ������**��Ҳ����ʹ�� `synchronized` �ؼ��ֻ� `Lock` �ӿ���ʵ��ͬ������֤�Թ�������ķ������̰߳�ȫ�ġ��������ַ�ʽ���ܻ�Ӱ�����ܣ���Ϊ�������̵߳������;�����

### �ܽ�

˳�����ڵ��̻߳����²������̰߳�ȫ���⣻���������ڴ�����ɱ�״̬ʱ��Ҫ�ر�ע���̰߳�ȫ������ͨ��ʹ���̰߳�ȫ�����ݽṹ��ͬ�����������������⡣



