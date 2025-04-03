### 1. `equals(Object obj)`



- **����**�����ڱȽ����������Ƿ���ȡ�Ĭ������£��÷����Ƚϵ�����������������Ƿ���ȣ����Ƿ�ָ��ͬһ���ڴ��ַ��������������д���������ʵ�ֻ��ڶ������ݵıȽϡ�
- **ʾ��**��










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



- **����**�����ض���Ĺ�ϣ��ֵ����ϣ��ͨ�����ڹ�ϣ���� `HashMap`��`HashSet` �ȣ��У�����߲���Ч�ʡ�����д `equals()` ����ʱ��ͨ��Ҳ��Ҫ��д `hashCode()` �������Ա�֤��ȵĶ��������ͬ�Ĺ�ϣ�롣
- **ʾ��**��












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



- **����**�����ض�����ַ�����ʾ��ʽ��Ĭ������£��÷������ص��ַ������������Ͷ���Ĺ�ϣ���ʮ�����Ʊ�ʾ��Ϊ���ṩ�����������Ϣ��ͨ������д���������
- **ʾ��**��











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



- **����**�����ڴ��������ض����һ��������Ҫʹ�ø÷����������ʵ�� `Cloneable` �ӿڣ�������׳� `CloneNotSupportedException` �쳣��Ĭ�ϵ� `clone()` ������ǳ��������ֻ���ƶ���Ļ��������������ԣ���������������Ȼָ��ͬһ������
- **ʾ��**��














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



- **����**�����ض��������ʱ��� `Class` ���󡣿���ͨ�� `Class` �����ȡ��ĸ�����Ϣ�����������������ֶεȡ�
- **ʾ��**��













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



- **����**���ڶ�����������֮ǰ��Java �������JVM������øö���� `finalize()` �������������÷�����ʹ���Ѿ����Ƽ�����Ϊ���ĵ���ʱ����ȷ�������ҿ��ܻᵼ����Դй©�����⡣
- **ʾ��**��















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

### 7. `notify()`��`notifyAll()` �� `wait()`



- **����**�����������������̼߳��ͨ�š�`notify()` ���ڻ����ڴ˶���������ϵȴ��ĵ����̣߳�`notifyAll()` ���ڻ����ڴ˶���������ϵȴ��������̣߳�`wait()` ����ʹ��ǰ�̵߳ȴ���ֱ�������̵߳��øö���� `notify()` �� `notifyAll()` ������
- **ʾ��**��





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

## == �� equals() ����1

### �����������͵ıȽ�



- **`==`**�����ڻ����������ͣ��� `int`��`double`��`char` �ȣ���`==` �Ƚϵ������ǵ�ֵ�Ƿ���ȡ�



java











```java
int a = 5;
int b = 5;
System.out.println(a == b); 
```



�����������У�`a` �� `b` ���� `int` ���ͣ�`==` �Ƚ����Ǵ洢��ֵ������ֵ���� 5���������Ϊ `true`��

### �����������͵ıȽ�

#### `==`



- ���������������ͣ����ࡢ�ӿڡ�����ȣ���`==` �Ƚϵ������������Ƿ�ָ��ͬһ�����󣬼����Ǵ洢���ڴ��ַ�Ƿ���ͬ��













```java
String str1 = new String("Hello");
String str2 = new String("Hello");
System.out.println(str1 == str2); 
```



�����������У�`str1` �� `str2` ��������ͬ�� `String` ������Ȼ���ǵ�������ͬ�����洢���ڴ��ַ��ͬ������ `str1 == str2` �Ľ��Ϊ `false`��

#### `equals()`



- `equals()` �� `Object` ���е�һ�������������඼�̳��� `Object` �࣬��˶�����ʹ�� `equals()` ������`Object` ���� `equals()` ������Ĭ��ʵ�ֺ� `==` һ����Ҳ�ǱȽ������Ƿ���ȡ����ܶ��ࣨ�� `String`��`Integer` �ȣ�����д `equals()` ������ʹ��Ƚ϶���������Ƿ���ȡ�














```java
String str1 = new String("Hello");
String str2 = new String("Hello");
System.out.println(str1.equals(str2)); 
```



�����������У�`String` ����д�� `equals()` ���������ڱȽ��ַ��������ݣ����� `str1` �� `str2` �����ݶ��� `"Hello"`������ `str1.equals(str2)` �Ľ��Ϊ `true`��

### �Զ�����ıȽ�



- �����Զ����࣬�������д `equals()` ��������ôʹ�� `equals()` ������ `==` Ч����ͬ�����ǱȽ������Ƿ���ȡ������Ҫ�Ƚ϶���������Ƿ���ȣ�����Ҫ��д `equals()` ������












```java
class Person {
    private String name;
    private int age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    // ��д equals ����
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







�����������У�`Person` ����д�� `equals()` ������ʹ�� `p1.equals(p2)` �Ƚϵ������� `Person` ����� `name` �� `age` �����Ƿ���ȣ��������Ϊ `false` �� `true`��

### �ܽ�



- `==`�����ڻ����������ͣ��Ƚϵ���ֵ�Ƿ���ȣ����������������ͣ��Ƚϵ��������Ƿ�ָ��ͬһ�����󣨼��ڴ��ַ�Ƿ���ͬ����
- `equals()`��`Object` ���Ĭ��ʵ�ֺ� `==` һ�������ܶ������д�÷����ԱȽ϶���������Ƿ���ȡ��Զ��������ͨ����д `equals()` ������ʵ�ֻ������ݵıȽϡ�

##  == �� equals() ����2 

### **1. `==` �����**

- **����**��

    - ���ڱȽ�����������ֵ��
    - ���ڻ����������ͣ��� `int`, `char`, `boolean` �ȣ���`==` �Ƚϵ������ǵ�**ʵ��ֵ**��
    - �����������ͣ����������ȣ���`==` �Ƚϵ������ǵ�**�ڴ��ַ**�����Ƿ�ָ��ͬһ�����󣩡�

- **ʾ��**��



  ```
  int a = 5;
  int b = 5;
  System.out.println(a == b); // true���Ƚ�ֵ
  
  String str1 = new String("hello");
  String str2 = new String("hello");
  System.out.println(str1 == str2); // false���Ƚ��ڴ��ַ
  ```

------

### **2. `equals()` ����**

- **����**��

    - ���ڱȽ����������**�����Ƿ����**��
    - �� `Object` ��ķ�����Ĭ��ʵ���� `==` ��ͬ���Ƚ��ڴ��ַ����
    - ͨ����Ҫ��д `equals()` ������ʵ���Զ�������ݱȽ��߼���

- **ʾ��**��



  ```
  String str1 = new String("hello");
  String str2 = new String("hello");
  System.out.println(str1.equals(str2)); // true���Ƚ�����
  ```

- **��д `equals()` �Ĺ淶**��

    - �Է��ԣ�`x.equals(x)` ����Ϊ `true`��
    - �Գ��ԣ���� `x.equals(y)` Ϊ `true`���� `y.equals(x)` Ҳ����Ϊ `true`��
    - �����ԣ���� `x.equals(y)` �� `y.equals(z)` ��Ϊ `true`���� `x.equals(z)` Ҳ����Ϊ `true`��
    - һ���ԣ���ε��� `x.equals(y)` �Ľ������һ�¡�
    - �ǿ��ԣ�`x.equals(null)` ����Ϊ `false`��

------

### **3. �����ܽ�**

| **����**       | **`==`**                             | **`equals()`**                     |
| :------------- | :----------------------------------- | :--------------------------------- |
| **�Ƚ϶���**   | �����������ͣ�ֵ���������ͣ��ڴ��ַ | �������ͣ����ݣ�Ĭ�ϱȽ��ڴ��ַ�� |
| **�Ƿ����д** | ������д                             | ����д                             |
| **��;**       | �ж����������Ƿ�ָ��ͬһ����         | �ж���������������Ƿ����         |

------

### **4. ʾ������**



```
public class Main {
    public static void main(String[] args) {
        // ������������
        int a = 10;
        int b = 10;
        System.out.println(a == b); // true

        // ��������
        String str1 = new String("hello");
        String str2 = new String("hello");
        System.out.println(str1 == str2); // false���Ƚ��ڴ��ַ
        System.out.println(str1.equals(str2)); // true���Ƚ�����
    }
}
```

------

### **5. ע������**

- �����ַ������Ƽ�ʹ�� `equals()` ������ `==`����Ϊ `==` ������Ϊ�ַ��������ص��Ż�������������
- ��д `equals()` ʱ��ͨ��Ҳ��Ҫ��д `hashCode()`����ȷ�������ڹ�ϣ���� `HashMap`��������������

## hashCode()

### 1. ��ϣ��Ļ���



��ϣ����һ�ָ��ݼ���key��ֱ�ӷ����ڴ�洢λ�õ����ݽṹ����ͨ����ϣ��������ӳ�䵽һ���ض�������λ�ã��Ӷ�ʵ�ֿ��ٵ����ݷ��ʡ�`hashCode()` �����ķ���ֵ���������ϣ��������Ҫ���룬������һ������Ĺ�ϣ�루ͨ����һ����������

### 2. ��߹�ϣ�����Ч��



��ʹ�ù�ϣ���� `HashMap`��`HashSet`��ʱ��`hashCode()` �������Źؼ����ã��������������²����У�



- **�������**�������ϣ���в���һ��Ԫ��ʱ�����Ȼ���ø�Ԫ�ص� `hashCode()` �������õ�һ����ϣ�롣Ȼ�󣬹�ϣ�����������ϣ������Ԫ��Ӧ�ô洢��λ�á�������Ԫ�صĹ�ϣ����ͬ�����ǻᱻ�洢��ͬһ��λ�ã����������Ϊ��ϣ��ͻ����ͨ����ʹ����������������ݽṹ�������ͻ��











```java
import java.util.HashMap;

public class HashCodeInsertExample {
    public static void main(String[] args) {
        HashMap<String, Integer> map = new HashMap<>();
        String key = "example";
        int value = 10;
        // ���������ʹ�� key �� hashCode() ����
        map.put(key, value); 
    }
}
```



- **���Ҳ���**�����ӹ�ϣ���в���һ��Ԫ��ʱ��ͬ�����ȵ��ø�Ԫ�ص� `hashCode()` �������õ���ϣ�룬Ȼ����ݹ�ϣ�붨λ�����ܴ洢��Ԫ�ص�λ�á����ţ���Ƚϸ�λ���ϵ�Ԫ���Ƿ���Ҫ���ҵ�Ԫ����ȣ�ͨ��ʹ�� `equals()` �����������ڹ�ϣ����Կ��ٶ�λ�����µĴ洢λ�ã�����������Ҫ�Ƚϵ�Ԫ���������Ӷ�����˲���Ч�ʡ�












```java
import java.util.HashMap;

public class HashCodeLookupExample {
    public static void main(String[] args) {
        HashMap<String, Integer> map = new HashMap<>();
        String key = "example";
        int value = 10;
        map.put(key, value);
        // ���Ҳ�����ʹ�� key �� hashCode() ����
        Integer result = map.get(key); 
        System.out.println(result);
    }
}
```



- **ɾ������**��ɾ����������Ҳ������ƣ���ͨ�� `hashCode()` ������λ��Ԫ�صĴ���λ�ã�Ȼ���ٽ��о�ȷ�ȽϺ�ɾ��������

### 3. �� `equals()` �����Ĺ�ϵ



�� Java �У���һ����Ҫ�Ĺ涨�������������ͨ�� `equals()` �����Ƚ���ȣ���ô���ǵ� `hashCode()` ��������ֵ������ͬ����֮�������������� `hashCode()` ��������ֵ��ͬ����ô����ͨ�� `equals()` �����Ƚ�һ������ȡ����ǣ������������� `hashCode()` ��������ֵ��ͬ������ͨ�� `equals()` �����Ƚϲ�һ����ȣ������ܴ��ڹ�ϣ��ͻ������ˣ�����д `equals()` ����ʱ��ͨ��Ҳ��Ҫ��д `hashCode()` �������Ա�֤��һ�涨����ȷ�ԡ�








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

### 4. ���ڼ��Ͽ���е�Ԫ��Ψһ���ж�



�� `HashSet` �ȼ����У�`hashCode()` ���������ж�Ԫ���Ƿ��ظ������� `HashSet` �����Ԫ��ʱ�����ȱȽ�Ԫ�صĹ�ϣ�룬�����ϣ�벻ͬ������Ϊ�ǲ�ͬ��Ԫ�أ������ϣ����ͬ����ʹ�� `equals()` �������о�ȷ�Ƚϡ����������ڴ��������¿����ж�Ԫ���Ƿ��ظ�����߼��ϲ�����Ч�ʡ�












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

## Ϊʲô��ֻ�ṩ hashCode() ����

### 1. ��ϣ��ͻ����



- **ԭ��**��`hashCode()` ��������һ��������ϣ�룬�����ڹ�ϣ���п��ٶ�λԪ�صĴ洢λ�á�Ȼ�������ڹ�ϣ���ȡֵ��Χ�����޵ģ������ܵĶ������������޵ģ����Բ�ͬ�Ķ�����ܻ������ͬ�Ĺ�ϣ�룬���������Ϊ��ϣ��ͻ��
- **ʾ��**��������һ���򵥵Ĺ�ϣ��ʹ�ö���� `hashCode()` �����Ľ���Թ�ϣ��Ĵ�Сȡģ��ȷ���洢λ�á���������ͬ�Ķ��� `obj1` �� `obj2` ��������ͬ�Ĺ�ϣ�룬���ǻᱻӳ�䵽��ϣ���ͬһ��λ�á���ʱ�������� `hashCode()` �޷�����������������Ҫʹ�� `equals()` ��������һ���Ƚ����ǵ������Ƿ�������ȡ�












```java
import java.util.HashMap;

class MyClass {
    private int value;

    public MyClass(int value) {
        this.value = value;
    }

    @Override
    public int hashCode() {
        // ���������ж���Ĺ�ϣ�붼��ͬ
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
        // ������Ҫ equals() ���������� obj1 �� obj2
        System.out.println(map.get(obj1)); 
        System.out.println(map.get(obj2)); 
    }
}
```

### 2. ����������



- **ԭ��**��`hashCode()` ������Ҫ���ڹ�ϣ��Ŀ��ٲ��Һʹ洢������ע���Ƕ���Ĺ�ϣ�룬�������Ķ���ľ������ݡ��� `equals()` ���������Ŀ���Ǵ��������ж����������Ƿ���ȣ��������Ƿ������ͬ��ҵ���߼�ʵ�塣�ںܶೡ���£���������Ҫ��ȷ֪������������ҵ�����Ƿ�ȼۣ��������������ǵĹ�ϣ���Ƿ���ͬ��
- **ʾ��**����һ��ѧ������ϵͳ�У����� `Student` ������ܾ��в�ͬ���ڴ��ַ�������ǵ�ѧ�š��������������Ϣ����ͬ����ҵ��Ƕ�����������������Ӧ�ñ���Ϊ����ȵġ���ʱ������Ҫ��д `equals()` ������ʵ�����������ϵıȽϡ�








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
        // ���ݶ�������Լ����ϣ��
        return (id != null ? id.hashCode() : 0) + 
               (name != null ? name.hashCode() : 0) + age;
    }
}
```

### 3. �����Ժ�ͨ����



- **ԭ��**��`equals()` ������ Java �����ж��󶼾߱��Ļ����ȽϷ���������ʹ�ó����ǳ��㷺�������������ڹ�ϣ����� Java ��׼��͵��������еķ����������� `equals()` ���������ж���ıȽϺ��жϡ����ֻ�ṩ `hashCode()` �������ᵼ����Щ�����޷������������ƻ��� Java ���Եļ����Ժ�ͨ���ԡ�
- **ʾ��**��`List` �ӿ��е� `contains()` ���������ж��б����Ƿ����ĳ��Ԫ�أ�������ʹ�� `equals()` ���������бȽϵġ����û�� `equals()` ���������޷�׼ȷ�ж��б����Ƿ����ָ����Ԫ�ء�







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
        // ʹ�� equals() �����ж��б����Ƿ���� obj2
        System.out.println(list.contains(obj2)); 
    }
}
```

### �ܽ�

1. ������������hashCode ֵ��ȣ�������������һ����ȣ���ϣ��ײ����
2. ������������hashCode ֵ��Ȳ���equals()����Ҳ���� true�����ǲ���Ϊ������������ȡ�
3. ������������hashCode ֵ����ȣ����ǾͿ���ֱ����Ϊ�������������

## Ϊʲô��д equals() ʱ������д hashCode() ������

### 1. ��ѭ Java ���Ե�ͨ��Լ��



Java ���Թ淶Ҫ�������������ͨ�� `equals()` �����ȽϽ��Ϊ `true`����ô���ǵ� `hashCode()` ��������ֵ������ͬ������














```java
if (obj1.equals(obj2)) {
    // ��ô obj1.hashCode() ������� obj2.hashCode()
    assert obj1.hashCode() == obj2.hashCode();
}
```



���ֻ��д `equals()` ����������д `hashCode()` �������ͻ�Υ�����Լ�������磺







```java
import java.util.Objects;

class Person {
    private String name;
    private int age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    // ��д equals ����
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return age == person.age &&
                Objects.equals(name, person.name);
    }

    // δ��д hashCode ����
    // Ĭ��ʹ�� Object ��� hashCode ������������ڴ��ַ���
}

public class Main {
    public static void main(String[] args) {
        Person p1 = new Person("Alice", 25);
        Person p2 = new Person("Alice", 25);
        System.out.println(p1.equals(p2)); // ��� true
        System.out.println(p1.hashCode() == p2.hashCode()); // ������� false
    }
}
```



�����������У�`p1` �� `p2` ͨ�� `equals()` �Ƚ�Ϊ `true`��������û����д `hashCode()` ���������ǵ� `hashCode()` ����ֵ���ܲ�ͬ��Υ���� Java ��Լ����

### 2. ȷ����ϣ������ݽṹ��������



��� Java �����࣬�� `HashMap`��`HashSet` �ȣ��������� `hashCode()` �� `equals()` ������ʵ���书�ܡ���Щ�������ڴ洢�Ͳ���Ԫ��ʱ�����Ȼ�ʹ�� `hashCode()` ���������ٶ�λԪ�ؿ��ܴ洢��λ�ã�Ȼ����ʹ�� `equals()` ��������ȷ�Ƚ�Ԫ���Ƿ���ȡ�

#### �洢Ԫ��



���� `HashSet` �����Ԫ��ʱ��`HashSet` ���ȸ���Ԫ�ص� `hashCode()` ֵ�ҵ���Ӧ�Ĵ洢Ͱ��bucket��������ô洢Ͱ���Ѿ�������Ԫ�أ���ʹ�� `equals()` �����ж��Ƿ�����ظ�Ԫ�ء������д�� `equals()` ������û����д `hashCode()` ���������ܻᵼ������ `equals()` �Ƚ�Ϊ `true` �Ķ��󱻴洢�ڲ�ͬ�Ĵ洢Ͱ�У��Ӷ��ƻ��� `HashSet` ������洢�ظ�Ԫ�ص����ԡ�













```java
import java.util.HashSet;

class Student {
    private String id;

    public Student(String id) {
        this.id = id;
    }

    // ��д equals ����
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return id.equals(student.id);
    }

    // δ��д hashCode ����
}

public class HashSetExample {
    public static void main(String[] args) {
        HashSet<Student> set = new HashSet<>();
        Student s1 = new Student("001");
        Student s2 = new Student("001");
        set.add(s1);
        set.add(s2);
        System.out.println(set.size()); // ������� 2��Υ���� HashSet �������ظ�Ԫ�ص�����
    }
}
```

#### ����Ԫ��



�� `HashMap` �в���Ԫ��ʱ��Ҳ���ȸ��ݼ��� `hashCode()` ֵ�ҵ���Ӧ�Ĵ洢λ�ã���ͨ�� `equals()` �������о�ȷƥ�䡣��� `hashCode()` ����û����ȷ��д�����ܻᵼ���޷���ȷ�ҵ���Ӧ��Ԫ�ء�



��ˣ�Ϊ�˱�֤ `equals()` �� `hashCode()` ������һ���ԣ��Լ���ϣ������ݽṹ����������������д `equals()` ����ʱ������д `hashCode()` ������ͨ������ʹ�� `Objects.hash()` �������������д `hashCode()` ���������磺




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




## Object�ͷ��͵�����

- ���Ͳ���Ҫ��ǿ������ת��

- ���ͱ���ʱ����ȫ�����ʹ��Object�Ļ����޷���֤���ص�����һ������Ҫ�����͡�





