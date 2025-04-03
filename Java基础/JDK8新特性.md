Java 8��JDK 8���� Java ���Է�չ�����е�һ����Ҫ�汾�����������ʵ����ǿ��������ԣ������������ Java �ı�������Ϳ���Ч�ʡ������� JDK 8 ��һЩ��Ҫ�����ԣ�

### 1. Lambda ���ʽ



Lambda ���ʽ�� JDK 8 ��������������֮һ���������㽫������Ϊ�������ݸ����������߽�������Ϊ���ݴ���Lambda ���ʽʹ������Ӽ�࣬�����������ڲ����ʹ�á�



**ʾ������**��



����



java









```java
import java.util.Arrays;
import java.util.List;

public class LambdaExample {
    public static void main(String[] args) {
        List<String> names = Arrays.asList("Alice", "Bob", "Charlie");
        // ʹ�� Lambda ���ʽ�����б�
        names.forEach(name -> System.out.println(name));
    }
}
```

### 2. ����ʽ�ӿ�



����ʽ�ӿ���ָֻ����һ�����󷽷��Ľӿڣ�ͨ��ʹ�� `@FunctionalInterface` ע������ʶ��Lambda ���ʽ���Ա���ֵ������ʽ�ӿ����͵ı�����



**ʾ������**��



����



java









```java
@FunctionalInterface
interface MyFunction {
    int apply(int a, int b);
}

public class FunctionalInterfaceExample {
    public static void main(String[] args) {
        // ʹ�� Lambda ���ʽʵ�ֺ���ʽ�ӿ�
        MyFunction add = (a, b) -> a + b;
        System.out.println(add.apply(3, 5)); 
    }
}
```

### 3. Stream API



Stream API �ṩ��һ�ָ�Ч������ʹ�õķ�ʽ�����������ݡ���֧����ʽ����������ˡ�ӳ�䡢����ȣ����Խ��в��д�����ߴ�������ݼ������ܡ�



**ʾ������**��



����



java









```java
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class StreamAPIExample {
    public static void main(String[] args) {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
        // ���˳�ż�������
        int sum = numbers.stream()
                         .filter(n -> n % 2 == 0)
                         .mapToInt(Integer::intValue)
                         .sum();
        System.out.println(sum); 
    }
}
```

### 4. ��������



���������� Lambda ���ʽ��һ�ּ���ʽ��������ֱ���������еķ������������ÿ���ʹ������Ӽ���׶���



**ʾ������**��



����



java









```java
import java.util.Arrays;
import java.util.List;

public class MethodReferenceExample {
    public static void main(String[] args) {
        List<String> names = Arrays.asList("Alice", "Bob", "Charlie");
        // ʹ�÷������ñ����б�
        names.forEach(System.out::println);
    }
}
```

### 5. Ĭ�Ϸ���



Ĭ�Ϸ��������ڽӿ��ж������ķ���ʵ�֣�������Ҫʵ����ȥʵ����Щ��������ʹ���ڲ��ƻ�����ʵ���������£�������ӿ�������µķ�����



**ʾ������**��



����



java









```java
interface MyInterface {
    void normalMethod();
    // Ĭ�Ϸ���
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

### 6. ��̬����



JDK 8 �����ڽӿ��ж��徲̬��������Щ��������ֱ��ͨ���ӿ������á�



**ʾ������**��



����



java









```java
interface MyStaticInterface {
    // ��̬����
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

### 7. ���ں�ʱ�� API



JDK 8 ������ȫ�µ����ں�ʱ�� API��`java.time` ���������ṩ�˸��Ӽ�ࡢ���ú��̰߳�ȫ�����ں�ʱ�䴦��ʽ������˾ɵ� `Date` �� `Calendar` ���һЩ���⡣



**ʾ������**��



����



java









```java
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

public class DateTimeAPIExample {
    public static void main(String[] args) {
        // ��ȡ��ǰ����
        LocalDate currentDate = LocalDate.now();
        System.out.println("Current Date: " + currentDate);

        // ��ȡ��ǰʱ��
        LocalTime currentTime = LocalTime.now();
        System.out.println("Current Time: " + currentTime);

        // ��ȡ��ǰ���ں�ʱ��
        LocalDateTime currentDateTime = LocalDateTime.now();
        System.out.println("Current Date and Time: " + currentDateTime);
    }
}
```

### 8. Optional ��



`Optional` ����һ�������࣬���ڱ�ʾһ��ֵ���ܴ��ڻ򲻴��ڣ������˿�ָ���쳣��`NullPointerException`�������⡣



**ʾ������**��



����



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







��Щ������ʹ�� Java �����ִ����͸�Ч��Ϊ�������ṩ�˸���ı��ѡ��ͱ�����



����



������������Щʹ�ó�����

��������˳������ʲô����