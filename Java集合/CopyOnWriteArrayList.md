## 源码分析
### 核心原理



写时复制（Copy-On-Write）的核心思想是：当对列表进行写操作（如添加、删除元素）时，会先复制一份当前列表的副本，然后在副本上进行修改，最后将原列表的引用指向修改后的副本。读操作则直接在原列表上进行，不需要加锁，因此读操作的性能较高。这样可以在保证线程安全的同时，提高读操作的并发性能。

### 主要属性



java











```java
/** 可重入锁，用于保证写操作的线程安全 */
final transient ReentrantLock lock = new ReentrantLock();

/** 存储列表元素的数组 */
private transient volatile Object[] array;

/**
 * 获取当前存储元素的数组
 */
final Object[] getArray() {
    return array;
}

/**
 * 设置存储元素的数组
 */
final void setArray(Object[] a) {
    array = a;
}
```



- `lock`：`ReentrantLock` 类型的锁，用于在写操作时保证线程安全。
- `array`：`volatile` 修饰的数组，用于存储列表的元素。`volatile` 关键字保证了数组引用的可见性，使得一个线程对数组引用的修改能立即被其他线程看到。

### 构造方法



java











```java
// 无参构造方法，创建一个空的 CopyOnWriteArrayList
public CopyOnWriteArrayList() {
    setArray(new Object[0]);
}

// 构造一个包含指定集合元素的 CopyOnWriteArrayList
public CopyOnWriteArrayList(Collection<? extends E> c) {
    Object[] elements;
    if (c.getClass() == CopyOnWriteArrayList.class)
        elements = ((CopyOnWriteArrayList<?>)c).getArray();
    else {
        elements = c.toArray();
        // c.toArray might (incorrectly) not return Object[] (see 6260652)
        if (elements.getClass() != Object[].class)
            elements = Arrays.copyOf(elements, elements.length, Object[].class);
    }
    setArray(elements);
}

// 构造一个包含指定元素的 CopyOnWriteArrayList
public CopyOnWriteArrayList(E[] toCopyIn) {
    setArray(Arrays.copyOf(toCopyIn, toCopyIn.length, Object[].class));
}
```



- 无参构造方法：创建一个空的数组作为初始存储。
- 包含指定集合元素的构造方法：根据传入的集合类型，将集合元素复制到新数组中。
- 包含指定元素数组的构造方法：将传入的元素数组复制到新数组中。

### 添加元素



java











```java
// 在列表末尾添加元素
public boolean add(E e) {
    final ReentrantLock lock = this.lock;
    lock.lock();
    try {
        Object[] elements = getArray();
        int len = elements.length;
        // 复制原数组到新数组，新数组长度加 1
        Object[] newElements = Arrays.copyOf(elements, len + 1);
        newElements[len] = e;
        // 更新数组引用
        setArray(newElements);
        return true;
    } finally {
        lock.unlock();
    }
}

// 在指定位置插入元素
public void add(int index, E element) {
    final ReentrantLock lock = this.lock;
    lock.lock();
    try {
        Object[] elements = getArray();
        int len = elements.length;
        if (index > len || index < 0)
            throw new IndexOutOfBoundsException("Index: "+index+
                                                ", Size: "+len);
        Object[] newElements;
        int numMoved = len - index;
        if (numMoved == 0)
            newElements = Arrays.copyOf(elements, len + 1);
        else {
            newElements = new Object[len + 1];
            System.arraycopy(elements, 0, newElements, 0, index);
            System.arraycopy(elements, index, newElements, index + 1,
                             numMoved);
        }
        newElements[index] = element;
        setArray(newElements);
    } finally {
        lock.unlock();
    }
}
```



- 添加元素时，首先获取锁，保证写操作的线程安全。
- 复制原数组到一个新数组，新数组长度根据添加操作的类型进行调整（末尾添加长度加 1，指定位置插入长度也加 1）。
- 将新元素添加到新数组的相应位置。
- 更新数组引用，使原数组引用指向新数组。
- 最后释放锁。

### 删除元素



java











```java
// 删除指定位置的元素
public E remove(int index) {
    final ReentrantLock lock = this.lock;
    lock.lock();
    try {
        Object[] elements = getArray();
        int len = elements.length;
        E oldValue = get(elements, index);
        int numMoved = len - index - 1;
        if (numMoved == 0)
            setArray(Arrays.copyOf(elements, len - 1));
        else {
            Object[] newElements = new Object[len - 1];
            System.arraycopy(elements, 0, newElements, 0, index);
            System.arraycopy(elements, index + 1, newElements, index,
                             numMoved);
            setArray(newElements);
        }
        return oldValue;
    } finally {
        lock.unlock();
    }
}

// 删除指定元素
public boolean remove(Object o) {
    Object[] snapshot = getArray();
    int index = indexOf(o, snapshot, 0, snapshot.length);
    return (index < 0) ? false : remove(o, snapshot, index);
}

private boolean remove(Object o, Object[] snapshot, int index) {
    final ReentrantLock lock = this.lock;
    lock.lock();
    try {
        Object[] current = getArray();
        int len = current.length;
        if (snapshot != current) findIndex: {
            int prefix = Math.min(index, len);
            for (int i = 0; i < prefix; i++) {
                if (current[i] != snapshot[i] && eq(o, current[i])) {
                    index = i;
                    break findIndex;
                }
            }
            if (index >= len)
                return false;
            if (current[index] == o)
                break findIndex;
            index = indexOf(o, current, index, len);
            if (index < 0)
                return false;
        }
        Object[] newElements = new Object[len - 1];
        System.arraycopy(current, 0, newElements, 0, index);
        System.arraycopy(current, index + 1, newElements, index,
                         len - index - 1);
        setArray(newElements);
        return true;
    } finally {
        lock.unlock();
    }
}
```



- 删除元素时，同样先获取锁，保证线程安全。
- 复制原数组到一个新数组，新数组长度减 1。
- 将原数组中除要删除元素外的其他元素复制到新数组中。
- 更新数组引用，使原数组引用指向新数组。
- 最后释放锁。

### 迭代器



java











```java
// 返回一个迭代器
public Iterator<E> iterator() {
    return new COWIterator<E>(getArray(), 0);
}

static final class COWIterator<E> implements ListIterator<E> {
    /** 迭代器创建时数组的快照 */
    private final Object[] snapshot;
    /** 当前迭代的索引 */
    private int cursor;

    private COWIterator(Object[] elements, int initialCursor) {
        cursor = initialCursor;
        snapshot = elements;
    }

    public boolean hasNext() {
        return cursor < snapshot.length;
    }

    public E next() {
        if (! hasNext())
            throw new NoSuchElementException();
        return (E) snapshot[cursor++];
    }

    // 不支持 remove 操作
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
```







- `CopyOnWriteArrayList` 的迭代器是弱一致性的，它在创建时会获取当前数组的一个快照。
- 迭代器的 `next()` 方法直接从快照数组中获取元素，因此在迭代过程中，对列表的写操作不会影响迭代器的遍历。
- 迭代器不支持 `remove()` 操作，因为它使用的是数组的快照，无法对原列表进行修改。

### 总结



`CopyOnWriteArrayList` 通过写时复制的策略，在保证线程安全的同时，提高了读操作的并发性能。但由于每次写操作都需要复制数组，会带来一定的内存开销和性能损耗，因此适用于读多写少的场景。


## 怎么保证线程安全，优点是什么，缺点是什么

`CopyOnWriteArrayList` 是 Java 并发包（`java.util.concurrent`）中提供的一个线程安全的列表实现类，下面详细介绍它是如何保证线程安全的，以及它的优缺点。

### 如何保证线程安全

`CopyOnWriteArrayList` 采用写时复制（Copy-On-Write）的策略来保证线程安全，其核心原理如下：

- **读操作**：读操作（如 `get`、`iterator` 等）是直接在原数组上进行的，不需要加锁。因为在读取时不会对数组进行修改，所以多个线程可以同时进行读操作，不会产生并发问题。
- **写操作**：当进行写操作（如 `add`、`remove`、`set` 等）时，`CopyOnWriteArrayList` 会先将原数组进行复制，创建一个新的数组副本。然后在新数组上进行相应的修改操作，修改完成后，再将新数组的引用赋值给原数组。整个写操作过程是加锁的，使用 `ReentrantLock` 来保证同一时间只有一个线程可以进行写操作，避免多个线程同时修改数组导致数据不一致的问题。

以下是 `CopyOnWriteArrayList` 写操作的简化代码示例：

java











```java
import java.util.concurrent.locks.ReentrantLock;

public class SimpleCopyOnWriteArrayList<E> {
    private volatile Object[] array;
    private final ReentrantLock lock = new ReentrantLock();

    public SimpleCopyOnWriteArrayList() {
        array = new Object[0];
    }

    public void add(E element) {
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            Object[] oldArray = array;
            int oldLength = oldArray.length;
            Object[] newArray = new Object[oldLength + 1];
            System.arraycopy(oldArray, 0, newArray, 0, oldLength);
            newArray[oldLength] = element;
            array = newArray;
        } finally {
            lock.unlock();
        }
    }

    @SuppressWarnings("unchecked")
    public E get(int index) {
        return (E) array[index];
    }
}
```

### 优点

- **读操作无锁，性能高**：由于读操作不需要加锁，多个线程可以同时进行读操作，因此在读多写少的场景下，`CopyOnWriteArrayList` 的读性能非常高，不会因为锁的竞争而导致性能下降。
- **线程安全**：通过写时复制的策略，`CopyOnWriteArrayList` 保证了在多线程环境下的线程安全，避免了数据不一致的问题。
- **迭代器安全**：`CopyOnWriteArrayList` 的迭代器是基于创建迭代器时的数组快照实现的，在迭代过程中，即使其他线程对列表进行了修改，也不会影响当前迭代器的遍历，不会抛出 `ConcurrentModificationException` 异常。

### 缺点

- **写操作开销大**：每次写操作都需要复制原数组，这会带来较大的内存开销和时间开销。特别是当数组规模较大时，复制操作会消耗大量的内存和 CPU 资源，导致写操作的性能下降。
- **内存占用高**：由于在写操作时需要创建新的数组副本，会导致内存占用增加一倍。如果频繁进行写操作，会造成内存资源的浪费。
- **数据实时性问题**：由于读操作是基于原数组进行的，而写操作是在新数组上进行的，所以在写操作完成之前，读操作可能无法立即看到最新的数据，存在一定的数据延迟。

综上所述，`CopyOnWriteArrayList` 适用于读多写少、对数据实时性要求不高的场景。在使用时，需要根据具体的业务需求和性能要求来权衡其优缺点。


