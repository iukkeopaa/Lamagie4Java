### 1. `equals(Object obj)`



- **功能**：用于比较两个对象是否相等。默认情况下，该方法比较的是两个对象的引用是否相等（即是否指向同一个内存地址），但许多类会重写这个方法来实现基于对象内容的比较。
- **示例**：










```java
public class Person {
    private String name;
    private int age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Person other = (Person) obj;
        return age == other.age && (name == null ? other.name == null : name.equals(other.name));
    }

    public static void main(String[] args) {
        Person p1 = new Person("Alice", 25);
        Person p2 = new Person("Alice", 25);
        System.out.println(p1.equals(p2)); 
    }
}
```

### 2. `hashCode()`



- **功能**：返回对象的哈希码值。哈希码通常用于哈希表（如 `HashMap`、`HashSet` 等）中，以提高查找效率。在重写 `equals()` 方法时，通常也需要重写 `hashCode()` 方法，以保证相等的对象具有相同的哈希码。
- **示例**：












```java
import java.util.Objects;

public class Point {
    private int x;
    private int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Point other = (Point) obj;
        return x == other.x && y == other.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    public static void main(String[] args) {
        Point p1 = new Point(1, 2);
        Point p2 = new Point(1, 2);
        System.out.println(p1.hashCode() == p2.hashCode()); 
    }
}
```

### 3. `toString()`



- **功能**：返回对象的字符串表示形式。默认情况下，该方法返回的字符串包含类名和对象的哈希码的十六进制表示。为了提供更有意义的信息，通常会重写这个方法。
- **示例**：











```java
public class Book {
    private String title;
    private String author;

    public Book(String title, String author) {
        this.title = title;
        this.author = author;
    }

    @Override
    public String toString() {
        return "Book{title='" + title + "', author='" + author + "'}";
    }

    public static void main(String[] args) {
        Book book = new Book("Java Programming", "John Doe");
        System.out.println(book); 
    }
}
```

### 4. `clone()`



- **功能**：用于创建并返回对象的一个副本。要使用该方法，类必须实现 `Cloneable` 接口，否则会抛出 `CloneNotSupportedException` 异常。默认的 `clone()` 方法是浅拷贝，即只复制对象的基本数据类型属性，引用类型属性仍然指向同一个对象。
- **示例**：














```java
class Address implements Cloneable {
    String city;

    public Address(String city) {
        this.city = city;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}

class Employee implements Cloneable {
    String name;
    Address address;

    public Employee(String name, Address address) {
        this.name = name;
        this.address = address;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        Employee cloned = (Employee) super.clone();
        cloned.address = (Address) address.clone(); 
        return cloned;
    }

    public static void main(String[] args) throws CloneNotSupportedException {
        Address address = new Address("Beijing");
        Employee emp1 = new Employee("Alice", address);
        Employee emp2 = (Employee) emp1.clone();
        System.out.println(emp1.address == emp2.address); 
    }
}
```

### 5. `getClass()`



- **功能**：返回对象的运行时类的 `Class` 对象。可以通过 `Class` 对象获取类的各种信息，如类名、方法、字段等。
- **示例**：













```java
public class Animal {
    public static void main(String[] args) {
        Animal animal = new Animal();
        Class<?> clazz = animal.getClass();
        System.out.println(clazz.getName()); 
    }
}
```

### 6. `finalize()`



- **功能**：在对象被垃圾回收之前，Java 虚拟机（JVM）会调用该对象的 `finalize()` 方法。不过，该方法的使用已经不推荐，因为它的调用时机不确定，而且可能会导致资源泄漏等问题。
- **示例**：















```java
public class Resource {
    @Override
    protected void finalize() throws Throwable {
        System.out.println("Object is being garbage collected");
        super.finalize();
    }

    public static void main(String[] args) {
        Resource resource = new Resource();
        resource = null; 
        System.gc(); 
    }
}
```

### 7. `notify()`、`notifyAll()` 和 `wait()`



- **功能**：这三个方法用于线程间的通信。`notify()` 用于唤醒在此对象监视器上等待的单个线程；`notifyAll()` 用于唤醒在此对象监视器上等待的所有线程；`wait()` 用于使当前线程等待，直到其他线程调用该对象的 `notify()` 或 `notifyAll()` 方法。
- **示例**：





```java
public class ThreadCommunication {
    public static void main(String[] args) {
        final Object lock = new Object();

        Thread t1 = new Thread(() -> {
            synchronized (lock) {
                try {
                    System.out.println("Thread 1 is waiting");
                    lock.wait();
                    System.out.println("Thread 1 is awakened");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        Thread t2 = new Thread(() -> {
            synchronized (lock) {
                System.out.println("Thread 2 is notifying");
                lock.notify();
            }
        });

        t1.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        t2.start();
    }
}
```

## == 和 equals() 解释1

### 基本数据类型的比较



- **`==`**：对于基本数据类型（如 `int`、`double`、`char` 等），`==` 比较的是它们的值是否相等。



java











```java
int a = 5;
int b = 5;
System.out.println(a == b); 
```



在上述代码中，`a` 和 `b` 都是 `int` 类型，`==` 比较它们存储的值，由于值都是 5，所以输出为 `true`。

### 引用数据类型的比较

#### `==`



- 对于引用数据类型（如类、接口、数组等），`==` 比较的是两个引用是否指向同一个对象，即它们存储的内存地址是否相同。













```java
String str1 = new String("Hello");
String str2 = new String("Hello");
System.out.println(str1 == str2); 
```



在上述代码中，`str1` 和 `str2` 是两个不同的 `String` 对象，虽然它们的内容相同，但存储的内存地址不同，所以 `str1 == str2` 的结果为 `false`。

#### `equals()`



- `equals()` 是 `Object` 类中的一个方法，所有类都继承自 `Object` 类，因此都可以使用 `equals()` 方法。`Object` 类中 `equals()` 方法的默认实现和 `==` 一样，也是比较引用是否相等。但很多类（如 `String`、`Integer` 等）会重写 `equals()` 方法，使其比较对象的内容是否相等。














```java
String str1 = new String("Hello");
String str2 = new String("Hello");
System.out.println(str1.equals(str2)); 
```



在上述代码中，`String` 类重写了 `equals()` 方法，用于比较字符串的内容，由于 `str1` 和 `str2` 的内容都是 `"Hello"`，所以 `str1.equals(str2)` 的结果为 `true`。

### 自定义类的比较



- 对于自定义类，如果不重写 `equals()` 方法，那么使用 `equals()` 方法和 `==` 效果相同，都是比较引用是否相等。如果需要比较对象的内容是否相等，就需要重写 `equals()` 方法。












```java
class Person {
    private String name;
    private int age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    // 重写 equals 方法
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Person other = (Person) obj;
        return age == other.age && (name == null ? other.name == null : name.equals(other.name));
    }
}

public class Main {
    public static void main(String[] args) {
        Person p1 = new Person("Alice", 25);
        Person p2 = new Person("Alice", 25);
        System.out.println(p1 == p2); 
        System.out.println(p1.equals(p2)); 
    }
}
```







在上述代码中，`Person` 类重写了 `equals()` 方法，使得 `p1.equals(p2)` 比较的是两个 `Person` 对象的 `name` 和 `age` 属性是否相等，所以输出为 `false` 和 `true`。

### 总结



- `==`：对于基本数据类型，比较的是值是否相等；对于引用数据类型，比较的是引用是否指向同一个对象（即内存地址是否相同）。
- `equals()`：`Object` 类的默认实现和 `==` 一样，但很多类会重写该方法以比较对象的内容是否相等。自定义类可以通过重写 `equals()` 方法来实现基于内容的比较。

##  == 和 equals() 解释2 

### **1. `==` 运算符**

- **作用**：

    - 用于比较两个变量的值。
    - 对于基本数据类型（如 `int`, `char`, `boolean` 等），`==` 比较的是它们的**实际值**。
    - 对于引用类型（如对象、数组等），`==` 比较的是它们的**内存地址**（即是否指向同一个对象）。

- **示例**：



  ```
  int a = 5;
  int b = 5;
  System.out.println(a == b); // true，比较值
  
  String str1 = new String("hello");
  String str2 = new String("hello");
  System.out.println(str1 == str2); // false，比较内存地址
  ```

------

### **2. `equals()` 方法**

- **作用**：

    - 用于比较两个对象的**内容是否相等**。
    - 是 `Object` 类的方法，默认实现与 `==` 相同（比较内存地址）。
    - 通常需要重写 `equals()` 方法以实现自定义的内容比较逻辑。

- **示例**：



  ```
  String str1 = new String("hello");
  String str2 = new String("hello");
  System.out.println(str1.equals(str2)); // true，比较内容
  ```

- **重写 `equals()` 的规范**：

    - 自反性：`x.equals(x)` 必须为 `true`。
    - 对称性：如果 `x.equals(y)` 为 `true`，则 `y.equals(x)` 也必须为 `true`。
    - 传递性：如果 `x.equals(y)` 和 `y.equals(z)` 都为 `true`，则 `x.equals(z)` 也必须为 `true`。
    - 一致性：多次调用 `x.equals(y)` 的结果必须一致。
    - 非空性：`x.equals(null)` 必须为 `false`。

------

### **3. 区别总结**

| **特性**       | **`==`**                             | **`equals()`**                     |
| :------------- | :----------------------------------- | :--------------------------------- |
| **比较对象**   | 基本数据类型：值；引用类型：内存地址 | 引用类型：内容（默认比较内存地址） |
| **是否可重写** | 不可重写                             | 可重写                             |
| **用途**       | 判断两个变量是否指向同一对象         | 判断两个对象的内容是否相等         |

------

### **4. 示例代码**



```
public class Main {
    public static void main(String[] args) {
        // 基本数据类型
        int a = 10;
        int b = 10;
        System.out.println(a == b); // true

        // 引用类型
        String str1 = new String("hello");
        String str2 = new String("hello");
        System.out.println(str1 == str2); // false，比较内存地址
        System.out.println(str1.equals(str2)); // true，比较内容
    }
}
```

------

### **5. 注意事项**

- 对于字符串，推荐使用 `equals()` 而不是 `==`，因为 `==` 可能因为字符串常量池的优化导致意外结果。
- 重写 `equals()` 时，通常也需要重写 `hashCode()`，以确保对象在哈希表（如 `HashMap`）中正常工作。

## hashCode()

### 1. 哈希表的基础



哈希表是一种根据键（key）直接访问内存存储位置的数据结构，它通过哈希函数将键映射到一个特定的索引位置，从而实现快速的数据访问。`hashCode()` 方法的返回值就是这个哈希函数的重要输入，它返回一个对象的哈希码（通常是一个整数）。

### 2. 提高哈希表操作效率



在使用哈希表（如 `HashMap`、`HashSet`）时，`hashCode()` 方法起着关键作用，具体体现在以下操作中：



- **插入操作**：当向哈希表中插入一个元素时，首先会调用该元素的 `hashCode()` 方法，得到一个哈希码。然后，哈希表会根据这个哈希码计算出元素应该存储的位置。如果多个元素的哈希码相同，它们会被存储在同一个位置（这种情况称为哈希冲突），通常会使用链表或红黑树等数据结构来处理冲突。











```java
import java.util.HashMap;

public class HashCodeInsertExample {
    public static void main(String[] args) {
        HashMap<String, Integer> map = new HashMap<>();
        String key = "example";
        int value = 10;
        // 插入操作会使用 key 的 hashCode() 方法
        map.put(key, value); 
    }
}
```



- **查找操作**：当从哈希表中查找一个元素时，同样会先调用该元素的 `hashCode()` 方法，得到哈希码，然后根据哈希码定位到可能存储该元素的位置。接着，会比较该位置上的元素是否与要查找的元素相等（通常使用 `equals()` 方法）。由于哈希码可以快速定位到大致的存储位置，大大减少了需要比较的元素数量，从而提高了查找效率。












```java
import java.util.HashMap;

public class HashCodeLookupExample {
    public static void main(String[] args) {
        HashMap<String, Integer> map = new HashMap<>();
        String key = "example";
        int value = 10;
        map.put(key, value);
        // 查找操作会使用 key 的 hashCode() 方法
        Integer result = map.get(key); 
        System.out.println(result);
    }
}
```



- **删除操作**：删除操作与查找操作类似，先通过 `hashCode()` 方法定位到元素的大致位置，然后再进行精确比较和删除操作。

### 3. 与 `equals()` 方法的关系



在 Java 中，有一个重要的规定：如果两个对象通过 `equals()` 方法比较相等，那么它们的 `hashCode()` 方法返回值必须相同；反之，如果两个对象的 `hashCode()` 方法返回值不同，那么它们通过 `equals()` 方法比较一定不相等。但是，如果两个对象的 `hashCode()` 方法返回值相同，它们通过 `equals()` 方法比较不一定相等（即可能存在哈希冲突）。因此，在重写 `equals()` 方法时，通常也需要重写 `hashCode()` 方法，以保证这一规定的正确性。








```java
import java.util.Objects;

class Person {
    private String name;
    private int age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Person other = (Person) obj;
        return age == other.age && (name == null ? other.name == null : name.equals(other.name));
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, age);
    }
}
```

### 4. 用于集合框架中的元素唯一性判断



在 `HashSet` 等集合中，`hashCode()` 方法用于判断元素是否重复。当向 `HashSet` 中添加元素时，会先比较元素的哈希码，如果哈希码不同，则认为是不同的元素；如果哈希码相同，再使用 `equals()` 方法进行精确比较。这样可以在大多数情况下快速判断元素是否重复，提高集合操作的效率。












```java
import java.util.HashSet;

public class HashSetExample {
    public static void main(String[] args) {
        HashSet<Person> set = new HashSet<>();
        Person p1 = new Person("Alice", 25);
        Person p2 = new Person("Alice", 25);
        set.add(p1);
        set.add(p2);
        System.out.println(set.size()); 
    }
}
```

## 为什么不只提供 hashCode() 方法

### 1. 哈希冲突问题



- **原理**：`hashCode()` 方法返回一个整数哈希码，用于在哈希表中快速定位元素的存储位置。然而，由于哈希码的取值范围是有限的，而可能的对象数量是无限的，所以不同的对象可能会产生相同的哈希码，这种情况称为哈希冲突。
- **示例**：假设有一个简单的哈希表，使用对象的 `hashCode()` 方法的结果对哈希表的大小取模来确定存储位置。当两个不同的对象 `obj1` 和 `obj2` 产生了相同的哈希码，它们会被映射到哈希表的同一个位置。此时，仅依靠 `hashCode()` 无法区分这两个对象，需要使用 `equals()` 方法来进一步比较它们的内容是否真正相等。












```java
import java.util.HashMap;

class MyClass {
    private int value;

    public MyClass(int value) {
        this.value = value;
    }

    @Override
    public int hashCode() {
        // 故意让所有对象的哈希码都相同
        return 1; 
    }
}

public class HashCollisionExample {
    public static void main(String[] args) {
        HashMap<MyClass, String> map = new HashMap<>();
        MyClass obj1 = new MyClass(1);
        MyClass obj2 = new MyClass(2);
        map.put(obj1, "Value 1");
        map.put(obj2, "Value 2");
        // 这里需要 equals() 方法来区分 obj1 和 obj2
        System.out.println(map.get(obj1)); 
        System.out.println(map.get(obj2)); 
    }
}
```

### 2. 语义完整性



- **原理**：`hashCode()` 方法主要用于哈希表的快速查找和存储，它关注的是对象的哈希码，而不关心对象的具体内容。而 `equals()` 方法的设计目的是从语义上判断两个对象是否相等，即它们是否代表相同的业务逻辑实体。在很多场景下，开发者需要明确知道两个对象在业务上是否等价，而不仅仅是它们的哈希码是否相同。
- **示例**：在一个学生管理系统中，两个 `Student` 对象可能具有不同的内存地址，但它们的学号、姓名、年龄等信息都相同，从业务角度来看，这两个对象应该被认为是相等的。此时，就需要重写 `equals()` 方法来实现这种语义上的比较。








```java
class Student {
    private String id;
    private String name;
    private int age;

    public Student(String id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Student other = (Student) obj;
        return age == other.age && 
               (id == null ? other.id == null : id.equals(other.id)) && 
               (name == null ? other.name == null : name.equals(other.name));
    }

    @Override
    public int hashCode() {
        // 根据对象的属性计算哈希码
        return (id != null ? id.hashCode() : 0) + 
               (name != null ? name.hashCode() : 0) + age;
    }
}
```

### 3. 兼容性和通用性



- **原理**：`equals()` 方法是 Java 中所有对象都具备的基本比较方法，它的使用场景非常广泛，不仅仅局限于哈希表。许多 Java 标准库和第三方库中的方法都依赖于 `equals()` 方法来进行对象的比较和判断。如果只提供 `hashCode()` 方法，会导致这些方法无法正常工作，破坏了 Java 语言的兼容性和通用性。
- **示例**：`List` 接口中的 `contains()` 方法用于判断列表中是否包含某个元素，它就是使用 `equals()` 方法来进行比较的。如果没有 `equals()` 方法，就无法准确判断列表中是否包含指定的元素。







```java
import java.util.ArrayList;
import java.util.List;

class MyObject {
    private int value;

    public MyObject(int value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        MyObject other = (MyObject) obj;
        return value == other.value;
    }

    @Override
    public int hashCode() {
        return value;
    }
}

public class ListContainsExample {
    public static void main(String[] args) {
        List<MyObject> list = new ArrayList<>();
        MyObject obj1 = new MyObject(1);
        MyObject obj2 = new MyObject(1);
        list.add(obj1);
        // 使用 equals() 方法判断列表中是否包含 obj2
        System.out.println(list.contains(obj2)); 
    }
}
```

### 总结

1. 如果两个对象的hashCode 值相等，那这两个对象不一定相等（哈希碰撞）。
2. 如果两个对象的hashCode 值相等并且equals()方法也返回 true，我们才认为这两个对象相等。
3. 如果两个对象的hashCode 值不相等，我们就可以直接认为这两个对象不相等

## 为什么重写 equals() 时必须重写 hashCode() 方法？

### 1. 遵循 Java 语言的通用约定



Java 语言规范要求，如果两个对象通过 `equals()` 方法比较结果为 `true`，那么它们的 `hashCode()` 方法返回值必须相同。即：














```java
if (obj1.equals(obj2)) {
    // 那么 obj1.hashCode() 必须等于 obj2.hashCode()
    assert obj1.hashCode() == obj2.hashCode();
}
```



如果只重写 `equals()` 方法而不重写 `hashCode()` 方法，就会违反这个约定。例如：







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

    // 未重写 hashCode 方法
    // 默认使用 Object 类的 hashCode 方法，与对象内存地址相关
}

public class Main {
    public static void main(String[] args) {
        Person p1 = new Person("Alice", 25);
        Person p2 = new Person("Alice", 25);
        System.out.println(p1.equals(p2)); // 输出 true
        System.out.println(p1.hashCode() == p2.hashCode()); // 可能输出 false
    }
}
```



在上述例子中，`p1` 和 `p2` 通过 `equals()` 比较为 `true`，但由于没有重写 `hashCode()` 方法，它们的 `hashCode()` 返回值可能不同，违反了 Java 的约定。

### 2. 确保哈希表等数据结构正常工作



许多 Java 集合类，如 `HashMap`、`HashSet` 等，都依赖于 `hashCode()` 和 `equals()` 方法来实现其功能。这些集合类在存储和查找元素时，首先会使用 `hashCode()` 方法来快速定位元素可能存储的位置，然后再使用 `equals()` 方法来精确比较元素是否相等。

#### 存储元素



当向 `HashSet` 中添加元素时，`HashSet` 会先根据元素的 `hashCode()` 值找到对应的存储桶（bucket），如果该存储桶中已经有其他元素，再使用 `equals()` 方法判断是否存在重复元素。如果重写了 `equals()` 方法但没有重写 `hashCode()` 方法，可能会导致两个 `equals()` 比较为 `true` 的对象被存储在不同的存储桶中，从而破坏了 `HashSet` 不允许存储重复元素的特性。













```java
import java.util.HashSet;

class Student {
    private String id;

    public Student(String id) {
        this.id = id;
    }

    // 重写 equals 方法
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return id.equals(student.id);
    }

    // 未重写 hashCode 方法
}

public class HashSetExample {
    public static void main(String[] args) {
        HashSet<Student> set = new HashSet<>();
        Student s1 = new Student("001");
        Student s2 = new Student("001");
        set.add(s1);
        set.add(s2);
        System.out.println(set.size()); // 可能输出 2，违反了 HashSet 不允许重复元素的特性
    }
}
```

#### 查找元素



在 `HashMap` 中查找元素时，也是先根据键的 `hashCode()` 值找到对应的存储位置，再通过 `equals()` 方法进行精确匹配。如果 `hashCode()` 方法没有正确重写，可能会导致无法正确找到对应的元素。



因此，为了保证 `equals()` 和 `hashCode()` 方法的一致性，以及哈希表等数据结构的正常工作，在重写 `equals()` 方法时必须重写 `hashCode()` 方法。通常可以使用 `Objects.hash()` 方法来方便地重写 `hashCode()` 方法，例如：




```java
import java.util.Objects;

class Book {
    private String title;
    private String author;

    public Book(String title, String author) {
        this.title = title;
        this.author = author;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(title, book.title) &&
                Objects.equals(author, book.author);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, author);
    }
}
```




## Object和泛型的区别

- 泛型不需要做强制类型转换

- 泛型编译时更安全。如果使用Object的话，无法保证返回的类型一定是想要的类型。





