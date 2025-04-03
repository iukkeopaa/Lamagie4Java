### 1. ������������ʽ



- **����**�������� Java ���õ����ݽṹ��������ʱ��Ҫָ����������ͺͳ��ȡ�������ʽ�ж��֣����磺





```java
// ��������ʼ��һ������Ϊ 5 ����������
int[] array = new int[5];
// ������ֱ�ӳ�ʼ������Ԫ��
String[] stringArray = {"apple", "banana", "cherry"};
```



- **`ArrayList`**��`ArrayList` �� Java ���Ͽ���е�һ���࣬���� `List` �ӿڵ�ʵ���ࡣ����ʱ����ָ����ʼ���ȣ����ҿ��Դ洢�������͵Ķ���ʹ�÷��Ϳ���ָ���洢�ľ������ͣ���ʾ�����£�





```java
import java.util.ArrayList;
import java.util.List;

// ����һ���洢�ַ����� ArrayList
List<String> arrayList = new ArrayList<>();
```

### 2. ��������



- **����**������ĳ����ڴ���ʱ�ͱ�ȷ����֮���޷��ı䡣�����Ҫ��������ĳ��ȣ�ֻ�ܴ���һ���µ����飬����ԭ�����Ԫ�ظ��Ƶ��������С�ʾ���������£�








```java
int[] oldArray = {1, 2, 3};
// ����һ���µ����飬����Ϊԭ����� 2 ��
int[] newArray = new int[oldArray.length * 2];
// ��ԭ����Ԫ�ظ��Ƶ�������
System.arraycopy(oldArray, 0, newArray, 0, oldArray.length);
```



- **`ArrayList`**��`ArrayList` �Ƕ�̬���飬���ĳ��ȿ��Ը�����Ҫ�Զ������������Ԫ��ʱ�������ǰ�������㣬`ArrayList` ���Զ��������ݲ�����һ��Ὣ��������Ϊԭ���� 1.5 ����ʾ���������£�












```java
import java.util.ArrayList;
import java.util.List;

List<Integer> arrayList = new ArrayList<>();
// �������Ԫ�أ�ArrayList ���Զ�����
for (int i = 0; i < 10; i++) {
    arrayList.add(i);
}
```

### 3. �洢����



- **����**��������Դ洢�����������ͣ��� `int`��`double`��`char` �ȣ��������������ͣ��� `String`���Զ��������ȣ������磺











```java
// �洢�����������͵�����
int[] intArray = {1, 2, 3};
// �洢�����������͵�����
String[] stringArray = {"hello", "world"};
```



- **`ArrayList`**��`ArrayList` ֻ�ܴ洢�����������ͣ�����ֱ�Ӵ洢�����������͡������Ҫ�洢�����������ͣ���Ҫʹ�ö�Ӧ�İ�װ�ࡣ���磺










```java
import java.util.ArrayList;
import java.util.List;

// �洢 Integer ��װ��� ArrayList
List<Integer> arrayList = new ArrayList<>();
arrayList.add(1); 
```

### 4. ���������



- **����**������Ĳ�����Լ򵥣���Ҫͨ���±������ʺ��޸�Ԫ�ء������ڲ����ɾ���������������������м������Щ����ʱ����Ҫ�ֶ��ƶ�����Ԫ�أ�Ч�ʽϵ͡�ʾ���������£�













```java
int[] array = {1, 2, 3, 4, 5};
// �������м����Ԫ��
int insertIndex = 2;
int insertValue = 10;
// �ƶ�Ԫ��
for (int i = array.length - 1; i > insertIndex; i--) {
    array[i] = array[i - 1];
}
array[insertIndex] = insertValue;
```



- **`ArrayList`**��`ArrayList` �ṩ�˷ḻ�ķ���������Ԫ�ص���ӡ�ɾ�������ҵȲ�����ʹ���������ӷ��㡣���磺




```java
import java.util.ArrayList;
import java.util.List;

List<Integer> arrayList = new ArrayList<>();
arrayList.add(1);
arrayList.add(2);
// ��ָ��λ�ò���Ԫ��
arrayList.add(1, 10); 
// ɾ��ָ��λ�õ�Ԫ��
arrayList.remove(2); 
```









### 5. ���ܷ���



- **����**�����������������洢�ģ��������Ԫ�ص��ٶȷǳ��죬ʱ�临�Ӷ�Ϊ \(O(1)\)�����ڽ��в����ɾ������ʱ���������������м������Щ��������Ҫ�ƶ�����Ԫ�أ�ʱ�临�Ӷ�Ϊ \(O(n)\)��
- **`ArrayList`**��`ArrayList` �������Ԫ�ص��ٶ�Ҳ�Ͽ죬ʱ�临�Ӷ�Ϊ \(O(1)\)��������ĩβ���в����ɾ��������ʱ�临�Ӷ�Ϊ \(O(1)\)�������м��ͷ���в����ɾ������ʱ����Ҫ�ƶ�Ԫ�أ�ʱ�临�Ӷ�Ϊ \(O(n)\)��������`ArrayList` �����ݲ��������һ�������ܿ�����


## ArrayList �� LinkedList ����?

`ArrayList` �� `LinkedList` ��ʵ���� `java.util.List` �ӿڣ����ڴ洢�����ҿ��ظ���Ԫ�أ��������ڵײ����ݽṹ�������ص㡢ʹ�ó����ȷ���������Բ��죬��������ϸ�Աȣ�

### 1. �ײ����ݽṹ



- **`ArrayList`**�����ڶ�̬����ʵ�֡���ʹ��һ���������洢Ԫ�أ���Ԫ��������������ĳ�ʼ����ʱ���ᴴ��һ������������飬����ԭ�����е�Ԫ�ظ��Ƶ��������У�������̱���Ϊ���ݡ�
- **`LinkedList`**������˫������ʵ�֡�������һϵ�нڵ���ɣ�ÿ���ڵ�������ݡ�ָ��ǰһ���ڵ�����ú�ָ���һ���ڵ�����á�ͨ����Щ���ã��ڵ�����໥�����γ�һ��˫������

### 2. �����ص�

#### �����������



- **`ArrayList`**���������Ԫ�ص��ٶȷǳ��죬ʱ�临�Ӷ�Ϊ \(O(1)\)����Ϊ�����������洢�ģ�ͨ�������±����ֱ�Ӽ����Ԫ�����ڴ��еĵ�ַ���Ӷ����ٷ��ʵ���ӦԪ�ء����磺










```java
import java.util.ArrayList;
import java.util.List;

public class ArrayListRandomAccess {
    public static void main(String[] args) {
        List<Integer> arrayList = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            arrayList.add(i);
        }
        // ������ʵ� 500 ��Ԫ��
        int element = arrayList.get(500); 
        System.out.println(element);
    }
}
```



- **`LinkedList`**���������Ԫ�ص��ٶȽ�����ʱ�临�Ӷ�Ϊ \(O(n)\)����Ϊ�����������洢�ģ�Ҫ����ĳ��λ�õ�Ԫ�أ���Ҫ�������ͷ�ڵ��β�ڵ㿪ʼ�����α�������ֱ���ҵ�Ŀ��Ԫ�ء����磺







```java
import java.util.LinkedList;
import java.util.List;

public class LinkedListRandomAccess {
    public static void main(String[] args) {
        List<Integer> linkedList = new LinkedList<>();
        for (int i = 0; i < 1000; i++) {
            linkedList.add(i);
        }
        // ������ʵ� 500 ��Ԫ�أ���Ҫ��������
        int element = linkedList.get(500); 
        System.out.println(element);
    }
}
```

#### �����ɾ������



- **`ArrayList`**��������ĩβ�����ɾ��Ԫ�ص�Ч�ʽϸߣ�ʱ�临�Ӷ�Ϊ \(O(1)\)�����������м��ͷ���в����ɾ������ʱ����Ҫ�ƶ�����Ԫ�أ�ʱ�临�Ӷ�Ϊ \(O(n)\)�����磺












```java
import java.util.ArrayList;
import java.util.List;

public class ArrayListInsertDelete {
    public static void main(String[] args) {
        List<Integer> arrayList = new ArrayList<>();
        arrayList.add(1);
        arrayList.add(2);
        // �������м����Ԫ�أ���Ҫ�ƶ�����Ԫ��
        arrayList.add(1, 3); 
        // ɾ�������м��Ԫ�أ���Ҫ�ƶ�����Ԫ��
        arrayList.remove(1); 
    }
}
```



- **`LinkedList`**�������ɾ��Ԫ�ص�Ч�ʽϸߣ�ʱ�临�Ӷ�Ϊ \(O(1)\)����Ϊֻ��Ҫ�޸Ľڵ�����ã�����Ҫ�ƶ�����Ԫ�ء����磺





```java
import java.util.LinkedList;
import java.util.List;

public class LinkedListInsertDelete {
    public static void main(String[] args) {
        List<Integer> linkedList = new LinkedList<>();
        linkedList.add(1);
        linkedList.add(2);
        // �������м����Ԫ�أ�ֻ���޸Ľڵ�����
        linkedList.add(1, 3); 
        // ɾ�������м��Ԫ�أ�ֻ���޸Ľڵ�����
        linkedList.remove(1); 
    }
}
```









### 3. �ڴ�ռ��



- **`ArrayList`**�����������������洢�ģ���Ԥ�ȷ���һ�����ڴ�ռ䡣��������ʵ��Ԫ������ԶС������������������һ�����ڴ��˷ѡ�����������ʱ����Ҫ�����µ����鲢����Ԫ�أ�Ҳ��ռ�ö�����ڴ档
- **`LinkedList`**��ÿ���ڵ���˴洢�����⣬����Ҫ����Ŀռ����洢ָ��ǰһ���ڵ�ͺ�һ���ڵ�����ã���˻�ռ�ø�����ڴ�ռ䡣

### 4. ʹ�ó���



- **`ArrayList`**����������ҪƵ���������Ԫ�أ��������ɾ��������Ҫ������ĩβ���еĳ��������磬��Ҫ�����������ٲ���Ԫ�صĳ�������ʵ�ַ�ҳ��ѯ���ܡ�
- **`LinkedList`**����������ҪƵ�����в����ɾ����������������ʲ������ٵĳ��������磬ʵ�ֶ��л�ջ�����ݽṹʱ��`LinkedList` ��һ�������ѡ��



����������`ArrayList` �� `LinkedList` ������ȱ�㣬��ʵ�ʿ����У���Ҫ���ݾ����ʹ�ó�����ѡ����ʵļ����ࡣ


## ˵˵�����е� fail-fast �� fail-safe ��ʲô

### fail-fast������ʧ�ܣ�

#### ����



`fail-fast` �� Java ������һ�ִ�������ơ���һ���߳����ڵ���һ������ʱ�������һ���̶߳Ըü��ϵĽṹ�������޸ģ�������ӡ�ɾ��Ԫ�صȣ����������������׳� `ConcurrentModificationException` �쳣���Ӷ����ٵط����������޸ĵ����⣬������ֲ���Ԥ�ڵĽ����

#### ԭ��



`fail-fast` ���������ڼ����ڲ���һ�������� `modCount`��ÿ�����ϵĽṹ�����仯ʱ��`modCount` ��ֵ�ͻ����ӡ��������ڴ���ʱ���¼��ǰ�� `modCount` ֵ����ÿ�ε���ʱ���鵱ǰ�� `modCount` ֵ�Ƿ��봴��ʱ��¼��ֵ��ͬ�������ͬ��˵�����ϵĽṹ�ڵ��������б��޸��ˣ��������ͻ��׳� `ConcurrentModificationException` �쳣��

#### ʾ������












```java
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FailFastExample {
    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        list.add("apple");
        list.add("banana");
        list.add("cherry");

        Iterator<String> iterator = list.iterator();
        while (iterator.hasNext()) {
            String element = iterator.next();
            if ("banana".equals(element)) {
                // ���׳� ConcurrentModificationException �쳣
                list.remove(element); 
            }
        }
    }
}
```



�����������У�ʹ�õ��������� `ArrayList`���ڱ���������ֱ�ӵ��� `list.remove()` �����޸ļ��Ͻṹ����ᵼ�� `modCount` ֵ�ı䣬�Ӷ����� `ConcurrentModificationException` �쳣��

#### ����ʹ�� `fail-fast` �ļ���



`ArrayList`��`HashMap`��`HashSet` �ȼ��ϵĵ��������� `fail-fast` �ġ�

### fail-safe����ȫʧ�ܣ�

#### ����



`fail-safe` ��һ�ָ����ɵĵ������ơ���һ���߳����ڵ���һ������ʱ�������һ���̶߳Ըü��ϵĽṹ�������޸ģ������������׳��쳣�����Ǽ����������ϡ�������Ϊ `fail-safe` �����ڵ���ʱʹ�õ��Ǽ��ϵ�һ��������������ԭʼ���ϣ����Զ�ԭʼ���ϵ��޸Ĳ���Ӱ�쵽�������̡�

#### ԭ��



`fail-safe` �����ڴ���������ʱ�Ḵ��һ��ԭʼ���ϵ����ݣ��������ڸ����Ͻ��е�����������������ʹԭʼ���ϵĽṹ�ڵ��������з����˱仯��Ҳ����Ӱ�쵽��������ʹ�õĸ������Ӷ������� `ConcurrentModificationException` �쳣���׳���

#### ʾ������









```java
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.Iterator;

public class FailSafeExample {
    public static void main(String[] args) {
        CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<>();
        list.add("apple");
        list.add("banana");
        list.add("cherry");

        Iterator<String> iterator = list.iterator();
        while (iterator.hasNext()) {
            String element = iterator.next();
            if ("banana".equals(element)) {
                // �����׳��쳣
                list.remove(element); 
            }
        }
    }
}
```



�����������У�ʹ�� `CopyOnWriteArrayList` ���ϣ����� `fail-safe` �ġ��ڵ��������жԼ��Ͻ����޸ģ������׳��쳣����Ϊ������ʹ�õ��Ǽ��ϵĸ�����

#### ����ʹ�� `fail-safe` �ļ���



`CopyOnWriteArrayList`��`ConcurrentHashMap` �ȼ��ϵĵ������� `fail-safe` �ġ�

### �Ա��ܽ�



| ����     | fail-fast                                                    | fail-safe                                                    |
| -------- | ------------------------------------------------------------ | ------------------------------------------------------------ |
| �쳣���� | ���������м��Ͻṹ���޸Ļ��׳� `ConcurrentModificationException` �쳣 | ���������м��Ͻṹ���޸Ĳ����׳��쳣                         |
| ������Դ | ֱ�ӵ���ԭʼ����                                             | �������ϵĸ���                                               |
| ����     | ���ܽϸߣ���Ϊ����Ҫ���Ƽ���                                 | ���ܽϵͣ���Ϊ��Ҫ���Ƽ��ϣ����޸Ĳ���������                 |
| ���ó��� | �����ڵ��̻߳�����������һ����Ҫ��ϸߵĳ���                 | �����ڶ��̻߳�����������һ����Ҫ�󲻸ߣ���ע�س�����ȶ��Եĳ��� |



## .ArrayList�����ݻ����˽���


`ArrayList` �� Java ���Ͽ���г��õĶ�̬����ʵ���࣬�������ݻ���������Ԫ������������ǰ����ʱ�Զ����������С�������ɸ����Ԫ�ء�������ϸ���� `ArrayList` �����ݻ��ơ�

### 1. ��������



���˽����ݻ���֮ǰ������Ϥ `ArrayList` ����������صļ����������ԣ�



java











```java
// Ĭ�ϳ�ʼ����
private static final int DEFAULT_CAPACITY = 10;
// ���ڿ�ʵ���Ĺ��������ʵ��
private static final Object[] EMPTY_ELEMENTDATA = {};
// ����Ĭ�ϴ�С�Ŀ�ʵ���Ĺ��������ʵ��
private static final Object[] DEFAULTCAPACITY_EMPTY_ELEMENTDATA = {};
// �洢 ArrayList Ԫ�ص����黺����
transient Object[] elementData; 
// ArrayList �Ĵ�С��������Ԫ�ظ�����
private int size;
```





### 2. ���췽��



`ArrayList` �ж�����췽������ͬ�Ĺ��췽����Ӱ���ʼ���������ã�



java











```java
// ����һ������ָ����ʼ�����Ŀ��б�
public ArrayList(int initialCapacity) {
    if (initialCapacity > 0) {
        this.elementData = new Object[initialCapacity];
    } else if (initialCapacity == 0) {
        this.elementData = EMPTY_ELEMENTDATA;
    } else {
        throw new IllegalArgumentException("Illegal Capacity: "+
                                           initialCapacity);
    }
}

// ����һ����ʼ����Ϊ 10 �Ŀ��б�
public ArrayList() {
    this.elementData = DEFAULTCAPACITY_EMPTY_ELEMENTDATA;
}

// ����һ������ָ������Ԫ�ص��б���˳���ɼ��ϵĵ���������
public ArrayList(Collection<? extends E> c) {
    elementData = c.toArray();
    if ((size = elementData.length) != 0) {
        // c.toArray might (incorrectly) not return Object[] (see 6260652)
        if (elementData.getClass() != Object[].class)
            elementData = Arrays.copyOf(elementData, size, Object[].class);
    } else {
        // replace with empty array.
        this.elementData = EMPTY_ELEMENTDATA;
    }
}
```







- ��ʹ�� `new ArrayList(int initialCapacity)` ���췽��ʱ�����ָ���ĳ�ʼ�������� 0���򴴽�һ��ָ�����������飻�����ʼ����Ϊ 0����ʹ�� `EMPTY_ELEMENTDATA` �����顣
- ��ʹ�� `new ArrayList()` �޲ι��췽��ʱ����ʹ�� `DEFAULTCAPACITY_EMPTY_ELEMENTDATA` �����飬��ʼ����ʵ����Ϊ 0���ڵ�һ�����Ԫ��ʱ�Ὣ������ʼ��Ϊ 10��
- ��ʹ�� `new ArrayList(Collection c)` ���췽��ʱ���Ὣ���뼯�ϵ�Ԫ�ظ��Ƶ�һ���������С�

### 3. ���ݹ���



���� `ArrayList` �����Ԫ��ʱ�������ǰ���������������������Ԫ�أ��ͻᴥ�����ݲ��������ݵ���Ҫ�������£�

#### 3.1 ���Ԫ��ʱ�������



�ڵ��� `add(E e)` �������Ԫ��ʱ�����ȵ��� `ensureCapacityInternal(int minCapacity)` ������ȷ���������㹻��������



java











```java
public boolean add(E e) {
    ensureCapacityInternal(size + 1);  // Increments modCount!!
    elementData[size++] = e;
    return true;
}
```

#### 3.2 ������С����



`ensureCapacityInternal(int minCapacity)` ��������ݵ�ǰ�����Ƿ�ΪĬ�Ϲ���Ŀ�������ȷ����С������



java











```java
private void ensureCapacityInternal(int minCapacity) {
    ensureExplicitCapacity(calculateCapacity(elementData, minCapacity));
}

private static int calculateCapacity(Object[] elementData, int minCapacity) {
    if (elementData == DEFAULTCAPACITY_EMPTY_ELEMENTDATA) {
        return Math.max(DEFAULT_CAPACITY, minCapacity);
    }
    return minCapacity;
}
```



�����Ĭ�Ϲ���Ŀ����飬��С����Ϊ 10 �� `minCapacity` �еĽϴ�ֵ��������С�������� `minCapacity`��

#### 3.3 ����Ƿ���Ҫ����



`ensureExplicitCapacity(int minCapacity)` �������鵱ǰ����������Ƿ�С����С����������ǣ������ `grow(int minCapacity)` �����������ݣ�



java











```java
private void ensureExplicitCapacity(int minCapacity) {
    modCount++;

    // overflow-conscious code
    if (minCapacity - elementData.length > 0)
        grow(minCapacity);
}
```

#### 3.4 ִ�����ݲ���



`grow(int minCapacity)` ���������ݵĺ��ķ��������Ὣ�������������Ϊԭ���� 1.5 ����ʹ��λ���� `oldCapacity + (oldCapacity >> 1)` ʵ�֣���������ݺ��������ȻС����С����������������Ϊ��С������������ݺ������������������������ `MAX_ARRAY_SIZE`������� `hugeCapacity(int minCapacity)` �������д���



java











```java
private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;

private void grow(int minCapacity) {
    // overflow-conscious code
    int oldCapacity = elementData.length;
    int newCapacity = oldCapacity + (oldCapacity >> 1);
    if (newCapacity - minCapacity < 0)
        newCapacity = minCapacity;
    if (newCapacity - MAX_ARRAY_SIZE > 0)
        newCapacity = hugeCapacity(minCapacity);
    // minCapacity is usually close to size, so this is a win:
    elementData = Arrays.copyOf(elementData, newCapacity);
}

private static int hugeCapacity(int minCapacity) {
    if (minCapacity < 0) // overflow
        throw new OutOfMemoryError();
    return (minCapacity > MAX_ARRAY_SIZE) ?
        Integer.MAX_VALUE :
        MAX_ARRAY_SIZE;
}
```

### 4. �ܽ�



- `ArrayList` �����ݻ����Ƕ�̬�ģ���Ԫ������������ǰ����ʱ���Զ��������ݡ�
- ʹ���޲ι��췽�������� `ArrayList` ��ʼ����Ϊ 0����һ�����Ԫ��ʱ�Ὣ������ʼ��Ϊ 10��
- ����ʱ��������һ��Ϊ�������� 1.5 �����������ʵ��������е�����ȷ����������������С����������
- ���ݲ����ᴴ��һ���µ����飬����ԭ�����Ԫ�ظ��Ƶ��������У�������һ�������ܿ�������ˣ��ڴ��� `ArrayList` ʱ�������Ԥ��Ԫ������������ָ�����ʵĳ�ʼ�������Լ������ݴ�����������ܡ�

## ArrayList��ô���л���֪���� Ϊʲô?transient�������飿


### 1. `ArrayList` �����л�����



�� Java �У�`ArrayList` ʵ���� `java.io.Serializable` �ӿڣ�����ζ����֧�����л��ͷ����л����������л��ǽ�����ת��Ϊ�ֽ����Ĺ��̣��������л����ǽ��ֽ����ָ�Ϊ����Ĺ��̡�



`ArrayList` �д洢Ԫ�صĺ����� `elementData` ���飬���������� `ArrayList` ����ν������л��ġ�

#### 1.1 �Զ������л�����



`ArrayList` ��д�� `writeObject` �� `readObject` �����������������Զ������л��ͷ����л����߼���������Ϊ `ArrayList` �� `elementData` ������ܴ�����������ʵ��Ԫ����������������ֱ�����л��������飬���˷Ѵ����Ŀռ䡣



������ `writeObject` ������ʾ�����룺



java











```java
private void writeObject(java.io.ObjectOutputStream s)
    throws java.io.IOException{
    // Write out element count, and any hidden stuff
    int expectedModCount = modCount;
    s.defaultWriteObject();

    // Write out size as capacity for behavioural compatibility with clone()
    s.writeInt(size);

    // Write out all elements in the proper order.
    for (int i=0; i<size; i++) {
        s.writeObject(elementData[i]);
    }

    if (modCount != expectedModCount) {
        throw new ConcurrentModificationException();
    }
}
```



- ���ȵ��� `s.defaultWriteObject()` д��Ĭ�ϵ����л����ݡ�
- Ȼ��д�� `ArrayList` ��ʵ��Ԫ������ `size`��
- ���ţ�ֻ���л�ʵ�ʴ洢��Ԫ�أ����� `0` �� `size - 1` ��Ԫ�أ������˶�������δʹ�ò��ֵ����л���

#### 1.2 �Զ��巴���л�����



`readObject` �������ڽ��ֽ����ָ�Ϊ `ArrayList` ����ʾ���������£�



java











```java
private void readObject(java.io.ObjectInputStream s)
    throws java.io.IOException, ClassNotFoundException {
    elementData = EMPTY_ELEMENTDATA;

    // Read in size, and any hidden stuff
    s.defaultReadObject();

    // Read in capacity
    s.readInt(); // ignored

    if (size > 0) {
        // be like clone(), allocate array based upon size not capacity
        ensureCapacityInternal(size);

        Object[] a = elementData;
        // Read in all elements in the proper order.
        for (int i=0; i<size; i++) {
            a[i] = s.readObject();
        }
    }
}
```



- ���Ƚ� `elementData` ��ʼ��Ϊ�����顣
- ���� `s.defaultReadObject()` ��ȡĬ�ϵķ����л����ݡ�
- ��ȡ֮ǰд��� `size` ֵ��
- ���� `size` ȷ�� `ArrayList` ���㹻��������
- �����ȡԪ�ز��洢�� `elementData` �����С�

### 2. Ϊʲô�� `transient` ��������



`ArrayList` �е� `elementData` ���鱻 `transient` �ؼ������Σ�`transient` �ؼ��ֵ������Ǳ�Ǹ��ֶβ�����Ĭ�ϵ����л����̡���Ҫԭ�����£�

#### 2.1 ����ռ��˷�



`ArrayList` ������ `elementData` ���ܴ�����������ʵ��Ԫ����������������磬�� `ArrayList` ���ж����Ӻ�ɾ��������������������ܻ�ԶԶ����ʵ�ʴ洢��Ԫ�����������ֱ�Ӷ���������������л����Ὣ������δʹ�õĲ���Ҳ���л�����ɴ����Ŀռ��˷ѡ�ͨ��ʹ�� `transient` ���� `elementData` ���飬���� `writeObject` ������ֻ���л�ʵ�ʴ洢��Ԫ�أ����Ա������ֿռ��˷ѡ�

#### 2.2 ��֤���л��������



ʹ�� `transient` ���� `elementData` ����󣬿����� `writeObject` �� `readObject` �������Զ������л��ͷ����л����߼����������Ը���ʵ�����ѡ�����л���Щ���ݣ��Լ�������л�����������л�������ԡ����磬���������л�ʱ��Ԫ�ؽ���һЩ����Ĵ��������ڷ����л�ʱ����һЩ������֤��ת��������



����������`ArrayList` ͨ���Զ������л��ͷ����л���������� `transient` �ؼ��ֵ�ʹ�ã�ʵ���˸�Ч���������л����̣������˲���Ҫ�Ŀռ��˷ѡ�

## Vector�洢����

### Java �е� `java.util.Vector`



�� Java �`java.util.Vector` ���̰߳�ȫ�Ķ�̬���飬���Ķ����Ԫ�ش洢����� Java ���ڴ���������ء�

#### ���ϴ洢



Java ��һ���������ı�����ԣ�����ͨ���洢�ڶ��ϡ������� `Vector` ����ʱ���������������Ԫ�ض��洢�ڶ��ϡ�



����



java









```java
import java.util.Vector;

public class VectorStorageExample {
    public static void main(String[] args) {
        // ���� Vector ����
        Vector<Integer> vector = new Vector<>();
        vector.add(1);
        vector.add(2);
        vector.add(3);
        // vector �������Ԫ�ض��洢�ڶ���
        for (int num : vector) {
            System.out.print(num + " ");
        }
        System.out.println();
    }
}
```







����� Java �����У�`vector` �������ڶ��Ϸ���ģ���������������Ԫ��Ҳ�洢�ڶ��ϡ�Java ���������ջ��ƻ��Զ�������ϵ��ڴ棬�������ٱ�����ʱ�����Զ�������ռ�õ��ڴ档