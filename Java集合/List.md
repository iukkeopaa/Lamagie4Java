### 1. 定义与声明方式



- **数组**：数组是 Java 内置的数据结构，在声明时需要指定数组的类型和长度。声明方式有多种，例如：





```java
// 声明并初始化一个长度为 5 的整型数组
int[] array = new int[5];
// 声明并直接初始化数组元素
String[] stringArray = {"apple", "banana", "cherry"};
```



- **`ArrayList`**：`ArrayList` 是 Java 集合框架中的一个类，属于 `List` 接口的实现类。声明时无需指定初始长度，并且可以存储任意类型的对象（使用泛型可以指定存储的具体类型）。示例如下：





```java
import java.util.ArrayList;
import java.util.List;

// 声明一个存储字符串的 ArrayList
List<String> arrayList = new ArrayList<>();
```

### 2. 长度特性



- **数组**：数组的长度在创建时就被确定，之后无法改变。如果需要调整数组的长度，只能创建一个新的数组，并将原数组的元素复制到新数组中。示例代码如下：








```java
int[] oldArray = {1, 2, 3};
// 创建一个新的数组，长度为原数组的 2 倍
int[] newArray = new int[oldArray.length * 2];
// 将原数组元素复制到新数组
System.arraycopy(oldArray, 0, newArray, 0, oldArray.length);
```



- **`ArrayList`**：`ArrayList` 是动态数组，它的长度可以根据需要自动增长。当添加元素时，如果当前容量不足，`ArrayList` 会自动进行扩容操作，一般会将容量扩大为原来的 1.5 倍。示例代码如下：












```java
import java.util.ArrayList;
import java.util.List;

List<Integer> arrayList = new ArrayList<>();
// 不断添加元素，ArrayList 会自动扩容
for (int i = 0; i < 10; i++) {
    arrayList.add(i);
}
```

### 3. 存储类型



- **数组**：数组可以存储基本数据类型（如 `int`、`double`、`char` 等）和引用数据类型（如 `String`、自定义类对象等）。例如：











```java
// 存储基本数据类型的数组
int[] intArray = {1, 2, 3};
// 存储引用数据类型的数组
String[] stringArray = {"hello", "world"};
```



- **`ArrayList`**：`ArrayList` 只能存储引用数据类型，不能直接存储基本数据类型。如果需要存储基本数据类型，需要使用对应的包装类。例如：










```java
import java.util.ArrayList;
import java.util.List;

// 存储 Integer 包装类的 ArrayList
List<Integer> arrayList = new ArrayList<>();
arrayList.add(1); 
```

### 4. 操作灵活性



- **数组**：数组的操作相对简单，主要通过下标来访问和修改元素。但对于插入和删除操作，尤其是在数组中间进行这些操作时，需要手动移动大量元素，效率较低。示例代码如下：













```java
int[] array = {1, 2, 3, 4, 5};
// 在数组中间插入元素
int insertIndex = 2;
int insertValue = 10;
// 移动元素
for (int i = array.length - 1; i > insertIndex; i--) {
    array[i] = array[i - 1];
}
array[insertIndex] = insertValue;
```



- **`ArrayList`**：`ArrayList` 提供了丰富的方法来进行元素的添加、删除、查找等操作，使用起来更加方便。例如：




```java
import java.util.ArrayList;
import java.util.List;

List<Integer> arrayList = new ArrayList<>();
arrayList.add(1);
arrayList.add(2);
// 在指定位置插入元素
arrayList.add(1, 10); 
// 删除指定位置的元素
arrayList.remove(2); 
```









### 5. 性能方面



- **数组**：由于数组是连续存储的，随机访问元素的速度非常快，时间复杂度为 \(O(1)\)。但在进行插入和删除操作时，尤其是在数组中间进行这些操作，需要移动大量元素，时间复杂度为 \(O(n)\)。
- **`ArrayList`**：`ArrayList` 随机访问元素的速度也较快，时间复杂度为 \(O(1)\)。在数组末尾进行插入和删除操作的时间复杂度为 \(O(1)\)，但在中间或开头进行插入和删除操作时，需要移动元素，时间复杂度为 \(O(n)\)。不过，`ArrayList` 的扩容操作会带来一定的性能开销。


## ArrayList 与 LinkedList 区别?

`ArrayList` 和 `LinkedList` 都实现了 `java.util.List` 接口，用于存储有序且可重复的元素，但它们在底层数据结构、性能特点、使用场景等方面存在明显差异，以下是详细对比：

### 1. 底层数据结构



- **`ArrayList`**：基于动态数组实现。它使用一个数组来存储元素，当元素数量超过数组的初始容量时，会创建一个更大的新数组，并将原数组中的元素复制到新数组中，这个过程被称为扩容。
- **`LinkedList`**：基于双向链表实现。链表由一系列节点组成，每个节点包含数据、指向前一个节点的引用和指向后一个节点的引用。通过这些引用，节点可以相互连接形成一个双向链表。

### 2. 性能特点

#### 随机访问性能



- **`ArrayList`**：随机访问元素的速度非常快，时间复杂度为 \(O(1)\)。因为数组是连续存储的，通过数组下标可以直接计算出元素在内存中的地址，从而快速访问到对应元素。例如：










```java
import java.util.ArrayList;
import java.util.List;

public class ArrayListRandomAccess {
    public static void main(String[] args) {
        List<Integer> arrayList = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            arrayList.add(i);
        }
        // 随机访问第 500 个元素
        int element = arrayList.get(500); 
        System.out.println(element);
    }
}
```



- **`LinkedList`**：随机访问元素的速度较慢，时间复杂度为 \(O(n)\)。因为链表不是连续存储的，要访问某个位置的元素，需要从链表的头节点或尾节点开始，依次遍历链表，直到找到目标元素。例如：







```java
import java.util.LinkedList;
import java.util.List;

public class LinkedListRandomAccess {
    public static void main(String[] args) {
        List<Integer> linkedList = new LinkedList<>();
        for (int i = 0; i < 1000; i++) {
            linkedList.add(i);
        }
        // 随机访问第 500 个元素，需要遍历链表
        int element = linkedList.get(500); 
        System.out.println(element);
    }
}
```

#### 插入和删除性能



- **`ArrayList`**：在数组末尾插入和删除元素的效率较高，时间复杂度为 \(O(1)\)。但在数组中间或开头进行插入和删除操作时，需要移动大量元素，时间复杂度为 \(O(n)\)。例如：












```java
import java.util.ArrayList;
import java.util.List;

public class ArrayListInsertDelete {
    public static void main(String[] args) {
        List<Integer> arrayList = new ArrayList<>();
        arrayList.add(1);
        arrayList.add(2);
        // 在数组中间插入元素，需要移动后续元素
        arrayList.add(1, 3); 
        // 删除数组中间的元素，需要移动后续元素
        arrayList.remove(1); 
    }
}
```



- **`LinkedList`**：插入和删除元素的效率较高，时间复杂度为 \(O(1)\)。因为只需要修改节点的引用，不需要移动大量元素。例如：





```java
import java.util.LinkedList;
import java.util.List;

public class LinkedListInsertDelete {
    public static void main(String[] args) {
        List<Integer> linkedList = new LinkedList<>();
        linkedList.add(1);
        linkedList.add(2);
        // 在链表中间插入元素，只需修改节点引用
        linkedList.add(1, 3); 
        // 删除链表中间的元素，只需修改节点引用
        linkedList.remove(1); 
    }
}
```









### 3. 内存占用



- **`ArrayList`**：由于数组是连续存储的，会预先分配一定的内存空间。如果数组的实际元素数量远小于数组的容量，会造成一定的内存浪费。而且在扩容时，需要创建新的数组并复制元素，也会占用额外的内存。
- **`LinkedList`**：每个节点除了存储数据外，还需要额外的空间来存储指向前一个节点和后一个节点的引用，因此会占用更多的内存空间。

### 4. 使用场景



- **`ArrayList`**：适用于需要频繁随机访问元素，而插入和删除操作主要在数组末尾进行的场景。例如，需要根据索引快速查找元素的场景，如实现分页查询功能。
- **`LinkedList`**：适用于需要频繁进行插入和删除操作，而随机访问操作较少的场景。例如，实现队列或栈等数据结构时，`LinkedList` 是一个不错的选择。



综上所述，`ArrayList` 和 `LinkedList` 各有优缺点，在实际开发中，需要根据具体的使用场景来选择合适的集合类。


## 说说集合中的 fail-fast 和 fail-safe 是什么

### fail-fast（快速失败）

#### 概念



`fail-fast` 是 Java 集合中一种错误检测机制。当一个线程正在迭代一个集合时，如果另一个线程对该集合的结构进行了修改（例如添加、删除元素等），迭代器会立即抛出 `ConcurrentModificationException` 异常，从而快速地反馈出并发修改的问题，避免出现不可预期的结果。

#### 原理



`fail-fast` 机制依赖于集合内部的一个计数器 `modCount`。每当集合的结构发生变化时，`modCount` 的值就会增加。迭代器在创建时会记录当前的 `modCount` 值，在每次迭代时会检查当前的 `modCount` 值是否与创建时记录的值相同。如果不同，说明集合的结构在迭代过程中被修改了，迭代器就会抛出 `ConcurrentModificationException` 异常。

#### 示例代码












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
                // 会抛出 ConcurrentModificationException 异常
                list.remove(element); 
            }
        }
    }
}
```



在上述代码中，使用迭代器遍历 `ArrayList`，在遍历过程中直接调用 `list.remove()` 方法修改集合结构，这会导致 `modCount` 值改变，从而触发 `ConcurrentModificationException` 异常。

#### 常见使用 `fail-fast` 的集合



`ArrayList`、`HashMap`、`HashSet` 等集合的迭代器都是 `fail-fast` 的。

### fail-safe（安全失败）

#### 概念



`fail-safe` 是一种更宽松的迭代机制。当一个线程正在迭代一个集合时，如果另一个线程对该集合的结构进行了修改，迭代器不会抛出异常，而是继续迭代集合。这是因为 `fail-safe` 机制在迭代时使用的是集合的一个副本，而不是原始集合，所以对原始集合的修改不会影响到迭代过程。

#### 原理



`fail-safe` 机制在创建迭代器时会复制一份原始集合的数据，迭代器在副本上进行迭代操作。这样，即使原始集合的结构在迭代过程中发生了变化，也不会影响到迭代器所使用的副本，从而避免了 `ConcurrentModificationException` 异常的抛出。

#### 示例代码









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
                // 不会抛出异常
                list.remove(element); 
            }
        }
    }
}
```



在上述代码中，使用 `CopyOnWriteArrayList` 集合，它是 `fail-safe` 的。在迭代过程中对集合进行修改，不会抛出异常，因为迭代器使用的是集合的副本。

#### 常见使用 `fail-safe` 的集合



`CopyOnWriteArrayList`、`ConcurrentHashMap` 等集合的迭代器是 `fail-safe` 的。

### 对比总结



| 特性     | fail-fast                                                    | fail-safe                                                    |
| -------- | ------------------------------------------------------------ | ------------------------------------------------------------ |
| 异常处理 | 迭代过程中集合结构被修改会抛出 `ConcurrentModificationException` 异常 | 迭代过程中集合结构被修改不会抛出异常                         |
| 数据来源 | 直接迭代原始集合                                             | 迭代集合的副本                                               |
| 性能     | 性能较高，因为不需要复制集合                                 | 性能较低，因为需要复制集合，且修改操作开销大                 |
| 适用场景 | 适用于单线程环境，对数据一致性要求较高的场景                 | 适用于多线程环境，对数据一致性要求不高，更注重程序的稳定性的场景 |



## .ArrayList的扩容机制了解吗？


`ArrayList` 是 Java 集合框架中常用的动态数组实现类，它的扩容机制允许在元素数量超过当前容量时自动调整数组大小，以容纳更多的元素。下面详细介绍 `ArrayList` 的扩容机制。

### 1. 核心属性



在了解扩容机制之前，先熟悉 `ArrayList` 中与扩容相关的几个核心属性：



java











```java
// 默认初始容量
private static final int DEFAULT_CAPACITY = 10;
// 用于空实例的共享空数组实例
private static final Object[] EMPTY_ELEMENTDATA = {};
// 用于默认大小的空实例的共享空数组实例
private static final Object[] DEFAULTCAPACITY_EMPTY_ELEMENTDATA = {};
// 存储 ArrayList 元素的数组缓冲区
transient Object[] elementData; 
// ArrayList 的大小（包含的元素个数）
private int size;
```





### 2. 构造方法



`ArrayList` 有多个构造方法，不同的构造方法会影响初始容量的设置：



java











```java
// 构造一个具有指定初始容量的空列表
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

// 构造一个初始容量为 10 的空列表
public ArrayList() {
    this.elementData = DEFAULTCAPACITY_EMPTY_ELEMENTDATA;
}

// 构造一个包含指定集合元素的列表，其顺序由集合的迭代器返回
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







- 当使用 `new ArrayList(int initialCapacity)` 构造方法时，如果指定的初始容量大于 0，则创建一个指定容量的数组；如果初始容量为 0，则使用 `EMPTY_ELEMENTDATA` 空数组。
- 当使用 `new ArrayList()` 无参构造方法时，会使用 `DEFAULTCAPACITY_EMPTY_ELEMENTDATA` 空数组，初始容量实际上为 0，在第一次添加元素时会将容量初始化为 10。
- 当使用 `new ArrayList(Collection c)` 构造方法时，会将传入集合的元素复制到一个新数组中。

### 3. 扩容过程



当向 `ArrayList` 中添加元素时，如果当前数组的容量不足以容纳新元素，就会触发扩容操作。扩容的主要步骤如下：

#### 3.1 添加元素时检查容量



在调用 `add(E e)` 方法添加元素时，会先调用 `ensureCapacityInternal(int minCapacity)` 方法来确保数组有足够的容量：



java











```java
public boolean add(E e) {
    ensureCapacityInternal(size + 1);  // Increments modCount!!
    elementData[size++] = e;
    return true;
}
```

#### 3.2 计算最小容量



`ensureCapacityInternal(int minCapacity)` 方法会根据当前数组是否为默认构造的空数组来确定最小容量：



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



如果是默认构造的空数组，最小容量为 10 和 `minCapacity` 中的较大值；否则，最小容量就是 `minCapacity`。

#### 3.3 检查是否需要扩容



`ensureExplicitCapacity(int minCapacity)` 方法会检查当前数组的容量是否小于最小容量，如果是，则调用 `grow(int minCapacity)` 方法进行扩容：



java











```java
private void ensureExplicitCapacity(int minCapacity) {
    modCount++;

    // overflow-conscious code
    if (minCapacity - elementData.length > 0)
        grow(minCapacity);
}
```

#### 3.4 执行扩容操作



`grow(int minCapacity)` 方法是扩容的核心方法，它会将数组的容量扩大为原来的 1.5 倍（使用位运算 `oldCapacity + (oldCapacity >> 1)` 实现），如果扩容后的容量仍然小于最小容量，则将容量设置为最小容量；如果扩容后的容量超过了数组的最大容量 `MAX_ARRAY_SIZE`，则调用 `hugeCapacity(int minCapacity)` 方法进行处理：



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

### 4. 总结



- `ArrayList` 的扩容机制是动态的，当元素数量超过当前容量时会自动进行扩容。
- 使用无参构造方法创建的 `ArrayList` 初始容量为 0，第一次添加元素时会将容量初始化为 10。
- 扩容时，新容量一般为旧容量的 1.5 倍，但会根据实际情况进行调整，确保新容量能满足最小容量的需求。
- 扩容操作会创建一个新的数组，并将原数组的元素复制到新数组中，这会带来一定的性能开销。因此，在创建 `ArrayList` 时，如果能预估元素数量，建议指定合适的初始容量，以减少扩容次数，提高性能。

## ArrayList怎么序列化的知道吗？ 为什么?transient修饰数组？


### 1. `ArrayList` 的序列化过程



在 Java 中，`ArrayList` 实现了 `java.io.Serializable` 接口，这意味着它支持序列化和反序列化操作。序列化是将对象转换为字节流的过程，而反序列化则是将字节流恢复为对象的过程。



`ArrayList` 中存储元素的核心是 `elementData` 数组，下面来看看 `ArrayList` 是如何进行序列化的。

#### 1.1 自定义序列化方法



`ArrayList` 重写了 `writeObject` 和 `readObject` 这两个方法，用于自定义序列化和反序列化的逻辑。这是因为 `ArrayList` 的 `elementData` 数组可能存在容量大于实际元素数量的情况，如果直接序列化整个数组，会浪费大量的空间。



以下是 `writeObject` 方法的示例代码：



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



- 首先调用 `s.defaultWriteObject()` 写入默认的序列化数据。
- 然后写入 `ArrayList` 的实际元素数量 `size`。
- 接着，只序列化实际存储的元素，即从 `0` 到 `size - 1` 的元素，避免了对数组中未使用部分的序列化。

#### 1.2 自定义反序列化方法



`readObject` 方法用于将字节流恢复为 `ArrayList` 对象，示例代码如下：



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



- 首先将 `elementData` 初始化为空数组。
- 调用 `s.defaultReadObject()` 读取默认的反序列化数据。
- 读取之前写入的 `size` 值。
- 根据 `size` 确保 `ArrayList` 有足够的容量。
- 逐个读取元素并存储到 `elementData` 数组中。

### 2. 为什么用 `transient` 修饰数组



`ArrayList` 中的 `elementData` 数组被 `transient` 关键字修饰，`transient` 关键字的作用是标记该字段不参与默认的序列化过程。主要原因如下：

#### 2.1 避免空间浪费



`ArrayList` 的数组 `elementData` 可能存在容量大于实际元素数量的情况。例如，当 `ArrayList` 进行多次添加和删除操作后，数组的容量可能会远远大于实际存储的元素数量。如果直接对整个数组进行序列化，会将数组中未使用的部分也序列化，造成大量的空间浪费。通过使用 `transient` 修饰 `elementData` 数组，并在 `writeObject` 方法中只序列化实际存储的元素，可以避免这种空间浪费。

#### 2.2 保证序列化的灵活性



使用 `transient` 修饰 `elementData` 数组后，可以在 `writeObject` 和 `readObject` 方法中自定义序列化和反序列化的逻辑。这样可以根据实际情况选择序列化哪些数据，以及如何序列化，提高了序列化的灵活性。例如，可以在序列化时对元素进行一些额外的处理，或者在反序列化时进行一些数据验证和转换操作。



综上所述，`ArrayList` 通过自定义序列化和反序列化方法，结合 `transient` 关键字的使用，实现了高效、灵活的序列化过程，避免了不必要的空间浪费。

## Vector存储在哪

### Java 中的 `java.util.Vector`



在 Java 里，`java.util.Vector` 是线程安全的动态数组，它的对象和元素存储情况与 Java 的内存管理机制相关。

#### 堆上存储



Java 是一种面向对象的编程语言，对象通常存储在堆上。当创建 `Vector` 对象时，对象本身和其管理的元素都存储在堆上。



收起



java









```java
import java.util.Vector;

public class VectorStorageExample {
    public static void main(String[] args) {
        // 创建 Vector 对象
        Vector<Integer> vector = new Vector<>();
        vector.add(1);
        vector.add(2);
        vector.add(3);
        // vector 对象和其元素都存储在堆上
        for (int num : vector) {
            System.out.print(num + " ");
        }
        System.out.println();
    }
}
```







在这段 Java 代码中，`vector` 对象是在堆上分配的，并且它所包含的元素也存储在堆上。Java 的垃圾回收机制会自动管理堆上的内存，当对象不再被引用时，会自动回收其占用的内存。