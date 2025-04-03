`hashCode()` �� `equals()` �� Java �� `Object` �ඨ���������Ҫ������������ Java �ļ��Ͽ�ܡ���ϣ��ȳ��������Źؼ����ã�����֮������Ž��ܵ���ϵ��������ϸ��������֮��Ĺ�ϵ��

### 1. ��������



- **`equals()` ����**�����ڱȽ����������Ƿ���ȡ��� `Object` ���У�`equals()` ����Ĭ�ϱȽϵ�����������������Ƿ���ȣ����Ƿ�ָ��ͬһ���ڴ��ַ�����ܶ������д������������ݶ�����������ж��Ƿ���ȡ�



����



java









```java
public boolean equals(Object obj) {
    return (this == obj);
}
```



- **`hashCode()` ����**�����ض���Ĺ�ϣ��ֵ������һ���ɶ�����ڲ���ַ������ϣ�㷨ת���õ����������� `Object` ���У�`hashCode()` ����ͨ���Ǹ��ݶ�����ڴ��ַ����õ��ġ�



����



java









```java
public native int hashCode();
```

### 2. ����֮��Ĺ�ϵ����



- **����һ�������������ͨ�� `equals()` �����ȽϽ��Ϊ `true`����ô���ǵ� `hashCode()` ֵ������ͬ**
  ����д `equals()` ����ʱ������ͬʱ��д `hashCode()` �������Ա�֤�������������������Ϊ�� Java �Ĺ�ϣ���� `HashMap`��`HashSet` �ȣ��У����Ȼ�ʹ�� `hashCode()` ������ȷ�������ڹ�ϣ���е�λ�ã�Ȼ����ʹ�� `equals()` ��������һ���ж����������Ƿ���ȡ����������ȵĶ��� `hashCode()` ֵ��ͬ����ô���ǿ��ܻᱻ�洢�ڹ�ϣ��Ĳ�ͬλ�ã����¹�ϣ���޷�����������



����



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

    // ��д equals ����
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return age == person.age &&
                Objects.equals(name, person.name);
    }

    // ��д hashCode ����
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
            System.out.println("p1 �� p2 ���");
            System.out.println("p1 �� hashCode: " + p1.hashCode());
            System.out.println("p2 �� hashCode: " + p2.hashCode());
        }
    }
}
```



�����������У�`Person` ����д�� `equals()` �� `hashCode()` �������� `p1` �� `p2` ͨ�� `equals()` �����ȽϽ��Ϊ `true` ʱ�����ǵ� `hashCode()` ֵҲ��ͬ��



- **������������������� `hashCode()` ֵ��ͬ�����ǲ�һ��ͨ�� `equals()` �����ȽϽ��Ϊ `true`**
  ������Ϊ��ϣ����ܻᷢ����ײ������ͬ�Ķ�����ܻ������ͬ�Ĺ�ϣ�롣��ϣ�㷨�ǽ����ⳤ�ȵ�����ת��Ϊ�̶����ȵ��������˲��ɱ���ػ���ֲ�ͬ�����������ͬ����������



����



java









```java
class CollisionExample {
    private int value;

    public CollisionExample(int value) {
        this.value = value;
    }

    @Override
    public int hashCode() {
        // ���������ж���� hashCode ����ͬ
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
        System.out.println("ce1 �� hashCode: " + ce1.hashCode());
        System.out.println("ce2 �� hashCode: " + ce2.hashCode());
        System.out.println("ce1 �� ce2 �Ƿ����: " + ce1.equals(ce2));
    }
}
```







�����������У�`CollisionExample` ������ж���� `hashCode()` ֵ��������Ϊ 1����ͨ�� `equals()` �����Ƚ�ʱ��`ce1` �� `ce2` ������ȡ�

### 3. �ܽ�



- `equals()` ���������ж���������������Ƿ���ȣ��� `hashCode()` �����������ɶ���Ĺ�ϣ�룬��Ҫ���ڹ�ϣ������ݽṹ�Ŀ��ٲ��ҡ�
- Ϊ�˱�֤ Java ���Ͽ�ܵȵ���ȷ�ԣ�����д `equals()` ����ʱ��������д `hashCode()` ������ȷ����ȵĶ��������ͬ�Ĺ�ϣ�롣
- ��ϣ����ͬ�Ķ���һ����ȣ���Ϊ���ܻᷢ����ϣ��ײ��