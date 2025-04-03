## Դ�����
### ����ԭ��



дʱ���ƣ�Copy-On-Write���ĺ���˼���ǣ������б����д����������ӡ�ɾ��Ԫ�أ�ʱ�����ȸ���һ�ݵ�ǰ�б�ĸ�����Ȼ���ڸ����Ͻ����޸ģ����ԭ�б������ָ���޸ĺ�ĸ�������������ֱ����ԭ�б��Ͻ��У�����Ҫ��������˶����������ܽϸߡ����������ڱ�֤�̰߳�ȫ��ͬʱ����߶������Ĳ������ܡ�

### ��Ҫ����



java











```java
/** �������������ڱ�֤д�������̰߳�ȫ */
final transient ReentrantLock lock = new ReentrantLock();

/** �洢�б�Ԫ�ص����� */
private transient volatile Object[] array;

/**
 * ��ȡ��ǰ�洢Ԫ�ص�����
 */
final Object[] getArray() {
    return array;
}

/**
 * ���ô洢Ԫ�ص�����
 */
final void setArray(Object[] a) {
    array = a;
}
```



- `lock`��`ReentrantLock` ���͵�����������д����ʱ��֤�̰߳�ȫ��
- `array`��`volatile` ���ε����飬���ڴ洢�б��Ԫ�ء�`volatile` �ؼ��ֱ�֤���������õĿɼ��ԣ�ʹ��һ���̶߳��������õ��޸��������������߳̿�����

### ���췽��



java











```java
// �޲ι��췽��������һ���յ� CopyOnWriteArrayList
public CopyOnWriteArrayList() {
    setArray(new Object[0]);
}

// ����һ������ָ������Ԫ�ص� CopyOnWriteArrayList
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

// ����һ������ָ��Ԫ�ص� CopyOnWriteArrayList
public CopyOnWriteArrayList(E[] toCopyIn) {
    setArray(Arrays.copyOf(toCopyIn, toCopyIn.length, Object[].class));
}
```



- �޲ι��췽��������һ���յ�������Ϊ��ʼ�洢��
- ����ָ������Ԫ�صĹ��췽�������ݴ���ļ������ͣ�������Ԫ�ظ��Ƶ��������С�
- ����ָ��Ԫ������Ĺ��췽�����������Ԫ�����鸴�Ƶ��������С�

### ���Ԫ��



java











```java
// ���б�ĩβ���Ԫ��
public boolean add(E e) {
    final ReentrantLock lock = this.lock;
    lock.lock();
    try {
        Object[] elements = getArray();
        int len = elements.length;
        // ����ԭ���鵽�����飬�����鳤�ȼ� 1
        Object[] newElements = Arrays.copyOf(elements, len + 1);
        newElements[len] = e;
        // ������������
        setArray(newElements);
        return true;
    } finally {
        lock.unlock();
    }
}

// ��ָ��λ�ò���Ԫ��
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



- ���Ԫ��ʱ�����Ȼ�ȡ������֤д�������̰߳�ȫ��
- ����ԭ���鵽һ�������飬�����鳤�ȸ�����Ӳ��������ͽ��е�����ĩβ��ӳ��ȼ� 1��ָ��λ�ò��볤��Ҳ�� 1����
- ����Ԫ����ӵ����������Ӧλ�á�
- �����������ã�ʹԭ��������ָ�������顣
- ����ͷ�����

### ɾ��Ԫ��



java











```java
// ɾ��ָ��λ�õ�Ԫ��
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

// ɾ��ָ��Ԫ��
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



- ɾ��Ԫ��ʱ��ͬ���Ȼ�ȡ������֤�̰߳�ȫ��
- ����ԭ���鵽һ�������飬�����鳤�ȼ� 1��
- ��ԭ�����г�Ҫɾ��Ԫ���������Ԫ�ظ��Ƶ��������С�
- �����������ã�ʹԭ��������ָ�������顣
- ����ͷ�����

### ������



java











```java
// ����һ��������
public Iterator<E> iterator() {
    return new COWIterator<E>(getArray(), 0);
}

static final class COWIterator<E> implements ListIterator<E> {
    /** ����������ʱ����Ŀ��� */
    private final Object[] snapshot;
    /** ��ǰ���������� */
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

    // ��֧�� remove ����
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
```







- `CopyOnWriteArrayList` �ĵ���������һ���Եģ����ڴ���ʱ���ȡ��ǰ�����һ�����ա�
- �������� `next()` ����ֱ�Ӵӿ��������л�ȡԪ�أ�����ڵ��������У����б��д��������Ӱ��������ı�����
- ��������֧�� `remove()` ��������Ϊ��ʹ�õ�������Ŀ��գ��޷���ԭ�б�����޸ġ�

### �ܽ�



`CopyOnWriteArrayList` ͨ��дʱ���ƵĲ��ԣ��ڱ�֤�̰߳�ȫ��ͬʱ������˶������Ĳ������ܡ�������ÿ��д��������Ҫ�������飬�����һ�����ڴ濪����������ģ���������ڶ���д�ٵĳ�����


## ��ô��֤�̰߳�ȫ���ŵ���ʲô��ȱ����ʲô

`CopyOnWriteArrayList` �� Java ��������`java.util.concurrent`�����ṩ��һ���̰߳�ȫ���б�ʵ���࣬������ϸ����������α�֤�̰߳�ȫ�ģ��Լ�������ȱ�㡣

### ��α�֤�̰߳�ȫ

`CopyOnWriteArrayList` ����дʱ���ƣ�Copy-On-Write���Ĳ�������֤�̰߳�ȫ�������ԭ�����£�

- **������**������������ `get`��`iterator` �ȣ���ֱ����ԭ�����Ͻ��еģ�����Ҫ��������Ϊ�ڶ�ȡʱ�������������޸ģ����Զ���߳̿���ͬʱ���ж���������������������⡣
- **д����**��������д�������� `add`��`remove`��`set` �ȣ�ʱ��`CopyOnWriteArrayList` ���Ƚ�ԭ������и��ƣ�����һ���µ����鸱����Ȼ�����������Ͻ�����Ӧ���޸Ĳ������޸���ɺ��ٽ�����������ø�ֵ��ԭ���顣����д���������Ǽ����ģ�ʹ�� `ReentrantLock` ����֤ͬһʱ��ֻ��һ���߳̿��Խ���д�������������߳�ͬʱ�޸����鵼�����ݲ�һ�µ����⡣

������ `CopyOnWriteArrayList` д�����ļ򻯴���ʾ����

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

### �ŵ�

- **���������������ܸ�**�����ڶ���������Ҫ����������߳̿���ͬʱ���ж�����������ڶ���д�ٵĳ����£�`CopyOnWriteArrayList` �Ķ����ܷǳ��ߣ�������Ϊ���ľ��������������½���
- **�̰߳�ȫ**��ͨ��дʱ���ƵĲ��ԣ�`CopyOnWriteArrayList` ��֤���ڶ��̻߳����µ��̰߳�ȫ�����������ݲ�һ�µ����⡣
- **��������ȫ**��`CopyOnWriteArrayList` �ĵ������ǻ��ڴ���������ʱ���������ʵ�ֵģ��ڵ��������У���ʹ�����̶߳��б�������޸ģ�Ҳ����Ӱ�쵱ǰ�������ı����������׳� `ConcurrentModificationException` �쳣��

### ȱ��

- **д����������**��ÿ��д��������Ҫ����ԭ���飬�������ϴ���ڴ濪����ʱ�俪�����ر��ǵ������ģ�ϴ�ʱ�����Ʋ��������Ĵ������ڴ�� CPU ��Դ������д�����������½���
- **�ڴ�ռ�ø�**��������д����ʱ��Ҫ�����µ����鸱�����ᵼ���ڴ�ռ������һ�������Ƶ������д������������ڴ���Դ���˷ѡ�
- **����ʵʱ������**�����ڶ������ǻ���ԭ������еģ���д���������������Ͻ��еģ�������д�������֮ǰ�������������޷������������µ����ݣ�����һ���������ӳ١�

����������`CopyOnWriteArrayList` �����ڶ���д�١�������ʵʱ��Ҫ�󲻸ߵĳ�������ʹ��ʱ����Ҫ���ݾ����ҵ�����������Ҫ����Ȩ������ȱ�㡣


