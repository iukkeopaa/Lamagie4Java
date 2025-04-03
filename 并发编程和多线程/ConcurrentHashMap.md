### 1. `ConcurrentHashMap` ʵ��ԭ��

`ConcurrentHashMap` �� Java �������ڶ��̻߳����¸�Ч�ؽ��в��������Ĺ�ϣ��ʵ�֣����ڲ�ͬ�� JDK �汾���в�ͬ��ʵ�ַ�ʽ������ֱ���� JDK 7 �� JDK 8 ��ʵ��ԭ��

#### JDK 7 ʵ��ԭ��

�� JDK 7 �У�`ConcurrentHashMap` ���÷ֶ�����Segment�����ơ�����������ϣ��ֳɶ���Σ�Segment����ÿ�����൱��һ��С�� `HashMap`������ÿ���ζ����Լ�����������������̷߳��ʲ�ͬ�ε�����ʱ�����ǿ���ͬʱ���в������Ӷ���߲������ܡ�

���岽�����£�

1. **��ʼ��**������ `ConcurrentHashMap` ʱ�����ʼ��һ�������� `Segment`��ÿ�� `Segment` ����һ�� `HashEntry` ���飬���ڴ洢��ֵ�ԡ�
2. **�������**�����ݼ��Ĺ�ϣֵ�������Ӧ�� `Segment` ������Ȼ��Ը� `Segment` �������ڸ� `Segment` �� `HashEntry` �����н��в��������
3. **���Ҳ���**��ͬ�����ݼ��Ĺ�ϣֵ�������Ӧ�� `Segment` ������Ȼ���ڸ� `Segment` �� `HashEntry` �����в��Ҽ�ֵ�ԣ����ҹ��̲���Ҫ������

#### JDK 8 ʵ��ԭ��

JDK 8 �� `ConcurrentHashMap` �������ش�Ľ��������˷ֶ������ƣ������� CAS��Compare-And-Swap���� `synchronized` ����֤������ȫ����ʹ������ + ���� + ����������ݽṹ��

���岽�����£�

1. **��ʼ��**������ `ConcurrentHashMap` ʱ��ֻ��ʼ��һ���յ����飬�ڵ�һ�β�������ʱ�Ż�������ʼ�����顣

2. �������

   ��

    - ���ȸ��ݼ��Ĺ�ϣֵ��������������λ�á�
    - �����λ��Ϊ�գ�ʹ�� CAS �������½ڵ�����λ�á�
    - �����λ���Ѿ��нڵ㣬ʹ�� `synchronized` �Ը�λ�õ�ͷ�ڵ������Ȼ���½ڵ��������������С�

3. **���Ҳ���**�����ݼ��Ĺ�ϣֵ��������������λ�ã�Ȼ���ڸ�λ�õ�����������в��Ҽ�ֵ�ԣ����ҹ��̲���Ҫ������

### 2. ԭ��ʹ�õ���

�� JDK 7 �У�`ConcurrentHashMap` ʹ�õ��Ƿֶ�����`ReentrantLock`����ÿ�� `Segment` �̳��� `ReentrantLock`������Ҫ��ĳ�� `Segment` ����д����ʱ�����Ȼ�ȡ�� `Segment` �������Ӷ���֤ͬһʱ��ֻ��һ���߳̿��ԶԸ� `Segment` ����д������

�� JDK 8 �У�ʹ�õ��� CAS �� `synchronized` ����CAS ����������������������λ��Ϊ��ʱ��ʹ�� CAS ���������½ڵ㣻������λ���Ѿ��нڵ�ʱ��ʹ�� `synchronized` �Ը�λ�õ�ͷ�ڵ��������֤������ȫ��

### 3. CAS ����ʵ��

CAS ��һ�������㷨���������������������ڴ�λ�ã�V����Ԥ��ԭֵ��A������ֵ��B��������ڴ�λ�õ�ֵ��Ԥ��ԭֵ��ƥ�䣬��ô���������Զ�����λ��ֵ����Ϊ��ֵ�����򣬴����������κβ�����

�� Java �У�`java.util.concurrent.atomic` ���ṩ��һϵ�л��� CAS ʵ�ֵ�ԭ���࣬`ConcurrentHashMap` ��Ҳʹ������Щԭ������ʵ�� CAS ������

������һ���򵥵�ʾ����չʾ�����ʹ�� `AtomicInteger` ʵ�� CAS ������








```java
import java.util.concurrent.atomic.AtomicInteger;

public class CASExample {
    public static void main(String[] args) {
        AtomicInteger atomicInteger = new AtomicInteger(10);

        // Ԥ��ԭֵ
        int expectedValue = 10;
        // ��ֵ
        int newValue = 20;

        // ִ�� CAS ����
        boolean success = atomicInteger.compareAndSet(expectedValue, newValue);

        if (success) {
            System.out.println("CAS �����ɹ�����ֵΪ: " + atomicInteger.get());
        } else {
            System.out.println("CAS ����ʧ�ܣ���ǰֵΪ: " + atomicInteger.get());
        }
    }
}
```

�� `ConcurrentHashMap` �У�CAS ������Ҫ���ڳ�ʼ�����顢�����½ڵ�Ȳ��������磬�ڲ����½ڵ�ʱ���������λ��Ϊ�գ���ʹ�� `U.compareAndSwapObject(tab, i, null, new Node<K,V>(hash, key, value, null))` �����Խ��½ڵ�����λ�ã����� `U` �� `Unsafe` ���ʵ����`compareAndSwapObject` �� `Unsafe` ���ṩ�� CAS ������









```java
// JDK 8 ConcurrentHashMap ����ڵ�ʱ�� CAS ����ʾ��
if ((f = tabAt(tab, i = (n - 1) & hash)) == null) {
    if (casTabAt(tab, i, null,
                 new Node<K,V>(hash, key, value, null)))
        break;                   // no lock when adding to empty bin
}
```





���� `casTabAt` �����ڲ������� `Unsafe` ��� `compareAndSwapObject` ������







```java
static final <K,V> boolean casTabAt(Node<K,V>[] tab, int i,
                                    Node<K,V> c, Node<K,V> v) {
    return U.compareAndSwapObject(tab, ((long)i << ASHIFT) + ABASE, c, v);
}
```





ͨ�����ַ�ʽ��`ConcurrentHashMap` ���� CAS ����ʵ�����������Ĳ������룬����˲������ܡ�


## ConcurrentHashMap�������Ƽ����ķ�Χ

### 1. `ConcurrentHashMap` �������Ƽ����ķ�Χ

#### JDK 1.7



- **��������**��JDK 1.7 �е� `ConcurrentHashMap` ���÷ֶ�����Segment�����ơ�����������ϣ��ֳɶ�� `Segment`��ÿ�� `Segment` ������һ��С�� `HashMap`������ÿ�� `Segment` �����Լ������������ڽ���д�������� `put`��`remove` �ȣ�ʱ��ֻ��Ҫ�����������ڵ� `Segment`�������� `Segment` ���Լ������ж�д������
- **���ķ�Χ**�����ķ�Χ��һ�� `Segment`����ͬ `Segment` ֮��Ĳ������Բ������У�ֻ����ͬһ�� `Segment` �ڵĲ����Ż������������
- **���Ⲣ�������ԭ��**�����ڲ�ͬ�� `Segment` �ж�������������߳̿���ͬʱ�Բ�ͬ�� `Segment` ���в������Ӷ�����˲������ܡ�ͬʱ����ͬһ�� `Segment` �ڣ�ͨ��������֤���̰߳�ȫ�������˶���߳�ͬʱ�޸�ͬһ�� `Segment` �ڵ����ݵ��µ����ݲ�һ�����⡣



������ JDK 1.7 �� `ConcurrentHashMap` �ļ򵥽ṹʾ����





```java
import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentHashMap17Example {
    public static void main(String[] args) {
        ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();
        map.put("key1", 1);
        map.put("key2", 2);
    }
}
```

#### JDK 1.8



- **��������**��JDK 1.8 �е� `ConcurrentHashMap` �����˷ֶ������ƣ������� `CAS`��Compare-And-Swap���� `synchronized` �ؼ�������֤������ȫ���ڽ���д����ʱ�����Ȼ᳢��ʹ�� `CAS` ���������½ڵ㣬��� `CAS` ����ʧ�ܣ���ʹ�� `synchronized` �ؼ�����������ͷ�ڵ�������ĸ��ڵ㡣
- **���ķ�Χ**�����ķ�Χ������ͷ�ڵ�������ĸ��ڵ㡣�ڽ���д����ʱ��ֻ��Ҫ������ǰ�����Ľڵ����ڵ�����������ĸ��ڵ㣬������Ӱ����������������Ĳ�����
- **���Ⲣ�������ԭ��**��`CAS` ������һ��������ԭ�Ӳ����������ڲ�ʹ����������±�֤���ݵ�ԭ���ԡ��� `CAS` ����ʧ��ʱ��ʹ�� `synchronized` �ؼ��������ڵ㣬��֤��ͬһʱ��ֻ��һ���߳̿��ԶԸýڵ����д�������Ӷ������˲������⡣



������ JDK 1.8 �� `ConcurrentHashMap` �ļ򵥽ṹʾ����











```java
import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentHashMap18Example {
    public static void main(String[] args) {
        ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();
        map.put("key1", 1);
        map.put("key2", 2);
    }
}
```





### 2. JDK 1.7 �� JDK 1.8 �ĸĽ��������ֶλ���Ч�ʸ��ߵ�ԭ��

#### �Ľ���



- **���ݽṹ**��JDK 1.7 �������� + ����Ľṹ��ÿ�� `Segment` ��һ��С�� `HashMap`��JDK 1.8 �������� + ���� + ������Ľṹ���������ȳ���һ����ֵ��Ĭ��Ϊ 8��ʱ��������ת��Ϊ�����������˲���Ч�ʡ�
- **������**��JDK 1.7 ���÷ֶ������ƣ����������� `Segment`��JDK 1.8 ���� `CAS` �� `synchronized` �ؼ��֣���������������ͷ�ڵ�������ĸ��ڵ㣬�������ȸ�С��

#### �����ֶλ���Ч�ʸ��ߵ�ԭ��



- **�����ȸ�С**���ֶ���������Ȼ��������ϣ��ֳɶ�� `Segment`������˲������ܣ��� `Segment` �������ǹ̶��ģ���ĳЩ����£����ܻᵼ����������Ȼ�Ƚ����ء��� JDK 1.8 �е������ȸ�С��ֻ��Ҫ������ǰ�����Ľڵ����ڵ�����������ĸ��ڵ㣬�������������Ŀ����ԣ�����˲������ܡ�
- **�������ʹ��**��JDK 1.8 �����˺�������������ȳ���һ����ֵʱ��������ת��Ϊ�����������˲���Ч�ʡ��ڸ߲��������£����Ҳ���������������������������� `ConcurrentHashMap` �����ܡ�
- **�����ڴ濪��**���ֶ���������Ҫά����� `Segment` �����������ڴ濪������ JDK 1.8 �����˷ֶ������ƣ��������ڴ濪����



����������JDK 1.8 �е� `ConcurrentHashMap` ͨ���Ľ����ݽṹ�������ƣ�����˲������ܺͲ���Ч�ʣ�ͬʱ�������ڴ濪����