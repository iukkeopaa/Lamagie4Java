## .HashMap��put����֪����

### 1. �������̸���



������ `HashMap` �� `put(K key, V value)` ����ʱ��������ݼ��Ĺ�ϣֵ����洢λ�ã�Ȼ�󽫼�ֵ�Դ洢����Ӧ��λ�á������λ���Ѿ���Ԫ�أ�����ݲ�ͬ������д������ܻᷢ������ڵ�Ĳ��롢������ڵ�Ĳ�����߼�ֵ�Ե��滻��

### 2. ��ϸ���̲���

#### 2.1 ��� `HashMap` �Ƿ�Ϊ��



��� `HashMap` �����飨`table`��Ϊ�ջ��߳���Ϊ 0������� `resize()` �����������ݲ������������ʼ����

#### 2.2 ������Ĺ�ϣֵ�ʹ洢λ��



- ���ȣ�ͨ�� `hash(key)` ����������Ĺ�ϣֵ��`hash(key)` ������Լ��� `hashCode()` ���ж��ι�ϣ���Լ��ٹ�ϣ��ͻ�ĸ��ʡ�









```java
static final int hash(Object key) {
    int h;
    return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
}
```



- Ȼ�󣬸��ݹ�ϣֵ�����鳤�ȼ���洢λ�� `i`��ʹ�� `(n - 1) & hash` ��ʽ������ `n` ������ĳ��ȡ�

#### 2.3 ���洢λ���Ƿ�Ϊ��



- �������õ��Ĵ洢λ�� `table[i]` Ϊ�գ�˵����λ��û��Ԫ�أ�ֱ�Ӵ���һ���µĽڵ㲢�����λ�á�

#### 2.4 �����ϣ��ͻ



����洢λ���Ѿ���Ԫ�أ�˵�������˹�ϣ��ͻ����Ҫ��һ������



- **�жϼ��Ƿ���ͬ**���Ƚϵ�ǰ�ڵ�ļ���Ҫ����ļ��Ƿ���ȣ�ʹ�� `==` ���� `equals()` �������������ȣ���ֱ���滻�ýڵ��ֵ��
- **�ж��Ƿ�Ϊ������ڵ�**�������ǰ�ڵ��Ǻ�����ڵ㣨`TreeNode` ���ͣ�������ú�����Ĳ��뷽�� `putTreeVal()` ����ֵ�Բ��뵽������С�
- **��������ڵ�**�������ǰ�ڵ�������ڵ㣬����������Ƚ�ÿ���ڵ�ļ���Ҫ����ļ��Ƿ���ȡ�����ҵ���ȵļ������滻�ýڵ��ֵ���������������ĩβ��û���ҵ���ȵļ����ʹ���һ���µĽڵ���뵽����β�����������������ȴﵽ 8 �����鳤�ȴ��� 64���Ὣ����ת��Ϊ����������� `treeifyBin()` ������

#### 2.5 ����Ƿ���Ҫ����



�����ֵ�Ժ󣬻��� `HashMap` ��Ԫ�������Ƿ񳬹�����ֵ��`threshold`������������ˣ������ `resize()` �����������ݲ�����

### 3. ����ʾ��



�����Ǽ򻯵� `put` ��������ʾ������������������̣�







```java
public V put(K key, V value) {
    return putVal(hash(key), key, value, false, true);
}

final V putVal(int hash, K key, V value, boolean onlyIfAbsent,
               boolean evict) {
    Node<K,V>[] tab; Node<K,V> p; int n, i;
    // ��� table �Ƿ�Ϊ�ջ򳤶�Ϊ 0����Ϊ�����������
    if ((tab = table) == null || (n = tab.length) == 0)
        n = (tab = resize()).length;
    // ����洢λ�ã�����λ��Ϊ�գ�ֱ�Ӵ����½ڵ����
    if ((p = tab[i = (n - 1) & hash]) == null)
        tab[i] = newNode(hash, key, value, null);
    else {
        Node<K,V> e; K k;
        // �жϼ��Ƿ���ͬ
        if (p.hash == hash &&
            ((k = p.key) == key || (key != null && key.equals(k))))
            e = p;
        // �ж��Ƿ�Ϊ������ڵ�
        else if (p instanceof TreeNode)
            e = ((TreeNode<K,V>)p).putTreeVal(this, tab, hash, key, value);
        else {
            // ��������ڵ�
            for (int binCount = 0; ; ++binCount) {
                if ((e = p.next) == null) {
                    p.next = newNode(hash, key, value, null);
                    // �����ȴﵽ 8 �����鳤�ȴ��� 64��������ת��Ϊ�����
                    if (binCount >= TREEIFY_THRESHOLD - 1) // -1 for 1st
                        treeifyBin(tab, hash);
                    break;
                }
                if (e.hash == hash &&
                    ((k = e.key) == key || (key != null && key.equals(k))))
                    break;
                p = e;
            }
        }
        // �����Ѵ��ڣ��滻ֵ
        if (e != null) { 
            V oldValue = e.value;
            if (!onlyIfAbsent || oldValue == null)
                e.value = value;
            afterNodeAccess(e);
            return oldValue;
        }
    }
    ++modCount;
    // ����Ƿ���Ҫ����
    if (++size > threshold)
        resize();
    afterNodeInsertion(evict);
    return null;
}
```

### 4. �ܽ�



`HashMap` �� `put` ������Ҫ���������ϣֵ�������ϣ��ͻ�������ֵ�Ժͼ�����ݵȲ��衣ͨ����Щ���裬`HashMap` �ܹ���Ч�ش洢�͹����ֵ�ԣ�ͬʱ�ڴ����ϣ��ͻʱ��ʹ������ͺ�����Ľ�ϣ�����˲��ҺͲ�������ܡ�

| ����         | ����                                                         |
| ------------ | ------------------------------------------------------------ |
| ��ʼ         | ���� `HashMap` �� `put(K key, V value)` ����                 |
| �����ϣֵ   | ͨ�� `hash(key)` ����������Ĺ�ϣֵ                          |
| �������״̬ | ��� `HashMap` �����飨`table`���Ƿ�Ϊ�ջ��߳���Ϊ 0 - �ǣ����� `resize()` �����������ݣ�������һ�� - �񣬽�����һ�� |
| ����洢λ�� | ���ݹ�ϣֵ�����鳤�ȣ�ʹ�� `(n - 1) & hash` ��ʽ����洢λ�� `i`��`n` Ϊ���鳤�ȣ� |
| ���洢λ�� | ��� `table[i]` �Ƿ�Ϊ�� - �ǣ�����һ���µĽڵ㲢���� `table[i]`������ - �񣬽�����һ�� |
| �����ϣ��ͻ | ��ǰλ������Ԫ�أ����������жϣ� 1. �жϵ�ǰ�ڵ�ļ���Ҫ����ļ��Ƿ���ȣ�ʹ�� `==` ���� `equals()` ������ - �ǣ�ֱ���滻�ýڵ��ֵ������ - �񣬽�����һ�� 2. �жϵ�ǰ�ڵ��Ƿ�Ϊ������ڵ㣨`TreeNode` ���ͣ� - �ǣ����ú�����Ĳ��뷽�� `putTreeVal()` ����ֵ�Բ��뵽������У����� - �񣬽�����һ�� 3. ��������ڵ㣺���������Ƚ�ÿ���ڵ�ļ���Ҫ����ļ��Ƿ���� - �ǣ��滻�ýڵ��ֵ������ - ��������������ĩβ��û���ҵ���ȵļ�������һ���µĽڵ���뵽����β�����������������ȴﵽ 8 �����鳤�ȴ��� 64������ `treeifyBin()` ����������ת��Ϊ����������� |
| ����Ƿ����� | ��� `HashMap` ��Ԫ������ `size` �Ƿ񳬹�����ֵ��`threshold`�� - �ǣ����� `resize()` ������������ - �񣬽��� |
| ����         | ���� `null` ���߱��滻�ľ�ֵ��������Ѵ��ڱ��滻��           |


## HashMap��ô����Ԫ�ص��أ�

### 1. �������̸���



������ `HashMap` �� `get(Object key)` ��������Ԫ��ʱ�������ȼ�����Ĺ�ϣֵ��Ȼ����ݹ�ϣֵ�ҵ���Ӧ�Ĵ洢λ�ã������ڸ�λ�õ�����������в���Ŀ�����Ӧ�Ľڵ㣬��󷵻ؽڵ��ֵ��

### 2. ��ϸ���̲���

#### 2.1 ������Ĺ�ϣֵ



���� `hash(key)` �����Դ���ļ����й�ϣ���㣬���������Լ��� `hashCode()` ���ж��ι�ϣ���Լ��ٹ�ϣ��ͻ�ĸ��ʡ�










```java
static final int hash(Object key) {
    int h;
    return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
}
```

#### 2.2 ��� `HashMap` ����



��� `HashMap` �����飨`table`���Ƿ�Ϊ�ջ��߳���Ϊ 0���Լ����ݹ�ϣֵ����õ��Ĵ洢λ���Ƿ�Ϊ�ա��������Ϊ�ա�����Ϊ 0 ���߸ô洢λ��Ϊ�գ�˵��û���ҵ�Ŀ��Ԫ�أ�ֱ�ӷ��� `null`��

#### 2.3 �Ƚ��׽ڵ�



����洢λ�ò�Ϊ�գ��Ƚϸ�λ�õ��׽ڵ�ļ���Ҫ���ҵļ��Ƿ���ȣ�ʹ�� `==` ���� `equals()` �������������ȣ�˵���ҵ���Ŀ��Ԫ�أ����ظýڵ��ֵ��

#### 2.4 �жϽڵ�����



����׽ڵ�ļ���Ҫ���ҵļ�����ȣ���Ҫ�жϸýڵ�������ڵ㻹�Ǻ�����ڵ㣺



- **������ڵ�**������Ǻ�����ڵ㣨`TreeNode` ���ͣ������ú�����Ĳ��ҷ��� `getTreeNode()` �ں�����в���Ŀ�����Ӧ�Ľڵ㡣����ҵ������ظýڵ��ֵ�����δ�ҵ������� `null`��
- **����ڵ�**�����������ڵ㣬�����������αȽ�ÿ���ڵ�ļ���Ҫ���ҵļ��Ƿ���ȡ�����ҵ���ȵļ������ظýڵ��ֵ���������������ĩβ��û���ҵ���ȵļ������� `null`��

### 3. ����ʾ��



������ `get` �����ļ򻯴���ʾ������������������̣�









```java
public V get(Object key) {
    Node<K,V> e;
    return (e = getNode(hash(key), key)) == null ? null : e.value;
}

final Node<K,V> getNode(int hash, Object key) {
    Node<K,V>[] tab; Node<K,V> first, e; int n; K k;
    // ��������Ƿ�Ϊ�ա������Ƿ�Ϊ 0 �Լ��洢λ���Ƿ�Ϊ��
    if ((tab = table) != null && (n = tab.length) > 0 &&
        (first = tab[(n - 1) & hash]) != null) {
        // �Ƚ��׽ڵ�
        if (first.hash == hash && 
            ((k = first.key) == key || (key != null && key.equals(k))))
            return first;
        if ((e = first.next) != null) {
            // �ж��Ƿ�Ϊ������ڵ�
            if (first instanceof TreeNode)
                return ((TreeNode<K,V>)first).getTreeNode(hash, key);
            // ��������
            do {
                if (e.hash == hash &&
                    ((k = e.key) == key || (key != null && key.equals(k))))
                    return e;
            } while ((e = e.next) != null);
        }
    }
    return null;
}
```









### 4. �ܽ�



`HashMap` ����Ԫ�صĹ�����Ҫ�����ڼ��Ĺ�ϣֵ����λ�洢λ�ã�Ȼ����ݸ�λ�õĽڵ����ͣ���������������н�һ���Ĳ��ҡ�ͨ�����ַ�ʽ��`HashMap` ������ƽ��������� \(O(1)\) ��ʱ�临�Ӷ���ɲ��Ҳ������ڹ�ϣ��ͻ�϶�ʱ�������µ�ʱ�临�Ӷ�Ϊ \(O(n)\)������������� \(O(log n)\)��������������


## HashMap�Ĺ�ϣ/�Ŷ���������ô��Ƶ�?


HashMap�Ĺ�ϣ���������õ� key ��hashcode����?��32λ��int���͵���ֵ��Ȼ����hashcode��?16λ�͵�16λ��?��������

## HashMap�����ݻ�����ʲô

### 1. ��������



���˽����ݻ���ǰ������ʶ������������صĺ������ԣ�










```java
// Ĭ�ϳ�ʼ������������ 2 ���ݴη��������� 16
static final int DEFAULT_INITIAL_CAPACITY = 1 << 4; 
// ���������Ϊ 2 �� 30 �η�
static final int MAXIMUM_CAPACITY = 1 << 30; 
// Ĭ�ϼ������ӣ����ں�����ʱ��������
static final float DEFAULT_LOAD_FACTOR = 0.75f; 
// �洢��ֵ�Ե����飬ÿ��λ�ó�Ϊһ��Ͱ
transient Node<K,V>[] table; 
// ��ֵ�Ե�����
transient int size; 
// ������ֵ���� size �ﵽ��ֵʱ��������
int threshold; 
// ��������
final float loadFactor; 
```

### 2. ���ݴ�������



�� `HashMap` �еļ�ֵ������ `size` ����������ֵ `threshold` ʱ���ͻᴥ�����ݲ�����������ֵ `threshold` �ļ��㹫ʽΪ��`threshold = capacity * loadFactor`������ `capacity` �ǵ�ǰ�����������`loadFactor` �Ǽ������ӡ�Ĭ������£���ʼ���� `capacity` Ϊ 16���������� `loadFactor` Ϊ 0.75f�����Գ�ʼ��������ֵ `threshold` Ϊ 12��16 * 0.75����

### 3. ���ݹ���



���ݲ�����Ҫͨ�� `resize()` ����ʵ�֣������Ǹ÷�������ϸ����ʹ��������

#### 3.1 ��¼��������Ϣ






```java
Node<K,V>[] oldTab = table;
int oldCap = (oldTab == null) ? 0 : oldTab.length;
int oldThr = threshold;
```



���ȼ�¼������ `oldTab`�������� `oldCap` �;�������ֵ `oldThr`��

#### 3.2 ��������������������ֵ









```java
int newCap, newThr = 0;
if (oldCap > 0) {
    if (oldCap >= MAXIMUM_CAPACITY) {
        threshold = Integer.MAX_VALUE;
        return oldTab;
    }
    else if ((newCap = oldCap << 1) < MAXIMUM_CAPACITY &&
             oldCap >= DEFAULT_INITIAL_CAPACITY)
        newThr = oldThr << 1; // double threshold
}
else if (oldThr > 0) // initial capacity was placed in threshold
    newCap = oldThr;
else {               // zero initial threshold signifies using defaults
    newCap = DEFAULT_INITIAL_CAPACITY;
    newThr = (int)(DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY);
}
if (newThr == 0) {
    float ft = (float)newCap * loadFactor;
    newThr = (newCap < MAXIMUM_CAPACITY && ft < (float)MAXIMUM_CAPACITY ?
              (int)ft : Integer.MAX_VALUE);
}
threshold = newThr;
```



- ���������



  ```
  oldCap
  ```



���� 0��

- ���������Ѿ��ﵽ������� `MAXIMUM_CAPACITY`����������ֵ `threshold` ����Ϊ `Integer.MAX_VALUE`�����ٽ������ݡ�
- ���򣬽������� `newCap` ����Ϊ�������� 2 ����`oldCap << 1`�������������С����������Ҿ��������ڵ���Ĭ�ϳ�ʼ����������������ֵ `newThr` Ҳ����Ϊ��������ֵ�� 2 ����

- ���������Ϊ 0������������ֵ `oldThr` ���� 0���������� `newCap` ����Ϊ��������ֵ��

- ����������;�������ֵ��Ϊ 0��ʹ��Ĭ�ϵĳ�ʼ�����ͼ������Ӽ�������������������ֵ��

#### 3.3 ����������













```java
@SuppressWarnings({"rawtypes","unchecked"})
Node<K,V>[] newTab = (Node<K,V>[])new Node[newCap];
table = newTab;
```



���������� `newCap` ����һ���µ����� `newTab`������ `table` ����ָ�������顣

#### 3.4 Ǩ��Ԫ��













```java
if (oldTab != null) {
    for (int j = 0; j < oldCap; ++j) {
        Node<K,V> e;
        if ((e = oldTab[j]) != null) {
            oldTab[j] = null;
            if (e.next == null)
                newTab[e.hash & (newCap - 1)] = e;
            else if (e instanceof TreeNode)
                ((TreeNode<K,V>)e).split(this, newTab, j, oldCap);
            else { // preserve order
                Node<K,V> loHead = null, loTail = null;
                Node<K,V> hiHead = null, hiTail = null;
                Node<K,V> next;
                do {
                    next = e.next;
                    if ((e.hash & oldCap) == 0) {
                        if (loTail == null)
                            loHead = e;
                        else
                            loTail.next = e;
                        loTail = e;
                    }
                    else {
                        if (hiTail == null)
                            hiHead = e;
                        else
                            hiTail.next = e;
                        hiTail = e;
                    }
                } while ((e = next) != null);
                if (loTail != null) {
                    loTail.next = null;
                    newTab[j] = loHead;
                }
                if (hiTail != null) {
                    hiTail.next = null;
                    newTab[j + oldCap] = hiHead;
                }
            }
        }
    }
}
```







- �����������ÿ��Ͱ��
    - ���Ͱ��ֻ��һ��Ԫ�أ�ֱ�Ӹ������������¼��������������е�λ�ò����롣
    - ���Ͱ�е�Ԫ���Ǻ�����ڵ㣬���� `split()` ��������������в�ֲ�Ǩ�Ƶ��������С�
    - ���Ͱ�е�Ԫ��������ڵ㣬��������Ϊ��������һ�������Ԫ�����������е�λ�����λ����ͬ����һ�������Ԫ�����������е�λ��Ϊ��λ�ü��Ͼ�������Ȼ������������ֱ�������������Ӧλ�á�

### 4. �ܽ�



`HashMap` �����ݻ���ͨ����������������Ϊԭ���� 2 ���������¼���Ԫ�����������е�λ�ã���֤�� `HashMap` ��Ԫ����������ʱ���ܱ��ֽϺõ����ܡ������ݲ��������һ�������ܿ�������Ϊ��Ҫ���·����ڴ沢Ǩ��Ԫ�أ������ڴ��� `HashMap` ʱ�������Ԥ��Ԫ������������ָ�����ʵĳ�ʼ�������Լ������ݴ�����


## Hash��ͻ��ô���

HashMap ��һ�ֳ��õ����ݽṹ�����ڴ洢��ֵ�ԣ���������ڽ����ϣ��ͻ�����˳�����������������ַ�������������¼��ִ����ϣ��ͻ�ķ�ʽ��

### ����Ѱַ��



����Ѱַ���ĺ���˼���ǵ�������ϣ��ͻʱ��ͨ��һ���Ĺ����ڹ�ϣ����Ѱ����һ�����õ�λ������ų�ͻ��Ԫ�ء������Ŀ���Ѱַ�������¼��־���ʵ�֣�

#### ����̽�ⷨ



- **ԭ��**����������ϣ��ͻʱ���ӳ�ͻλ�ÿ�ʼ���������̽����һ��λ�ã�ֱ���ҵ�һ����λ��Ϊֹ�����̽�⵽��ϣ���ĩβ��û���ҵ���λ�ã���ӹ�ϣ��Ŀ�ͷ����̽�⡣
- **ʾ�����루Java ��ʾ�⣩**��










```java
class LinearProbingHashMap {
    private static final int SIZE = 10;
    private int[] keys;
    private int[] values;

    public LinearProbingHashMap() {
        keys = new int[SIZE];
        values = new int[SIZE];
        for (int i = 0; i < SIZE; i++) {
            keys[i] = -1;
        }
    }

    public void put(int key, int value) {
        int index = key % SIZE;
        while (keys[index] != -1 && keys[index] != key) {
            index = (index + 1) % SIZE;
        }
        keys[index] = key;
        values[index] = value;
    }

    public int get(int key) {
        int index = key % SIZE;
        while (keys[index] != -1) {
            if (keys[index] == key) {
                return values[index];
            }
            index = (index + 1) % SIZE;
        }
        return -1;
    }
}
```

#### ����̽�ⷨ



- **ԭ��**����������ϣ��ͻʱ��̽��Ĳ��������ǹ̶��� 1�����ǰ��ն��κ����ķ�ʽ���е������� \(h_i=(h(key)+i^2)\bmod m\) �� \(h_i=(h(key)-i^2)\bmod m\)��\(i = 1, 2, 3, \cdots\)�������� \(h(key)\) �ǳ�ʼ��ϣֵ��m �ǹ�ϣ��Ĵ�С��
- **�ص�**������̽�ⷨ������һ���̶��ϻ�������̽�ⷨ���ײ����� ���ۼ��� ���⣬�����ܻᵼ��ĳЩλ���޷���̽�⵽��

#### ˫�ع�ϣ��



- **ԭ��**��ʹ��������ͬ�Ĺ�ϣ���� \(h_1(key)\) �� \(h_2(key)\)����������ϣ��ͻʱ������ \(h_i=(h_1(key)+i\times h_2(key))\bmod m\) �ķ�ʽ����̽�⣬���� \(i = 1, 2, 3, \cdots\)��\(h_2(key)\) ��ֵҪ��֤���ϣ��Ĵ�С m ���ʣ��������Ա�֤�ܹ���������ϣ���е�����λ�á�
- **�ص�**��˫�ع�ϣ���ܹ������ȵطֲ���ͻԪ�أ����پۼ����󣬵���Ҫ����Ĺ�ϣ�������㡣

### �ٹ�ϣ��



- **ԭ��**��׼�������ϣ��������ʹ�õ�һ����ϣ����������ͻʱ���ͻ��õڶ�����ϣ������ֱ���ҵ�һ������ͻ��λ�á��� \(h_i = H_i(key)\)��\(i = 1, 2, \cdots\)�������� \(H_i\) �ǲ�ͬ�Ĺ�ϣ������
- **�ص�**�����ַ������ŵ��ǿ��Ը����ش����ͻ������Ҫ����Ĺ�ϣ�������㣬�����˼��㿪����

### �����������



- **ԭ��**������ϣ���Ϊ�����������������֡���������ϣ��ͻʱ������ͻ��Ԫ�ط���������С�����ʱ�����ڻ������в��ң�����Ҳ������ٵ�������в��ҡ�
- **�ص�**�����ַ������ŵ��ǻ������е�Ԫ����Խ��٣������ٶȽϿ죻ȱ�����������ܻ��úܴ󣬵��²���������Ч�ʽ��͡�

## TreeMap��ô�����

### 1. `TreeMap` ������ʽ



`TreeMap` �� Java ���Ͽ���е�һ���࣬��ʵ���� `SortedMap` �ӿڣ����ں������Red-Black Tree��������ƽ��Ķ������������ݽṹ���洢��ֵ�ԣ������ݼ�����Ȼ˳������Զ���ıȽ�����`Comparator`�����Լ���������

#### ��Ȼ����



��ʹ��Ĭ�Ϲ��캯������ `TreeMap` ʱ������ʹ�ü�����Ȼ˳��������򡣼�����ʵ�� `Comparable` �ӿڣ�`TreeMap` ����ü��� `compareTo` ������ȷ��Ԫ�ص�˳��















```java
import java.util.TreeMap;

public class TreeMapNaturalOrderExample {
    public static void main(String[] args) {
        TreeMap<Integer, String> treeMap = new TreeMap<>();
        treeMap.put(3, "Three");
        treeMap.put(1, "One");
        treeMap.put(2, "Two");

        for (Integer key : treeMap.keySet()) {
            System.out.println(key + ": " + treeMap.get(key));
        }
    }
}
```



�����������У����� `Integer` ���ͣ�`Integer` ʵ���� `Comparable` �ӿڣ�`TreeMap` �ᰴ�ռ�������1, 2, 3����Ԫ�ؽ�������

#### �Զ�������



����ͨ������һ�� `Comparator` ������ָ���Զ�����������`Comparator` �ӿڶ�����һ�� `compare` ���������ڱȽ����������˳��













```java
import java.util.Comparator;
import java.util.TreeMap;

public class TreeMapCustomOrderExample {
    public static void main(String[] args) {
        // �Զ���Ƚ����������Ľ�������
        Comparator<Integer> descendingComparator = (a, b) -> b - a;
        TreeMap<Integer, String> treeMap = new TreeMap<>(descendingComparator);
        treeMap.put(3, "Three");
        treeMap.put(1, "One");
        treeMap.put(2, "Two");

        for (Integer key : treeMap.keySet()) {
            System.out.println(key + ": " + treeMap.get(key));
        }
    }
}
```



����������У����Ǵ�����һ���Զ���� `Comparator`������ʹ `TreeMap` ���ռ��Ľ���3, 2, 1����Ԫ�ؽ�������

### 2. ע������



- **���Ĳ��ɱ���**������ `TreeMap` �Ǹ��ݼ�������ģ��������ǲ��ɱ�ġ�������ڲ��뵽 `TreeMap` �����˸ı䣬���ܻ��ƻ� `TreeMap` ��������򣬵��²���Ԥ�ڵĽ����
- **`null` ��������**��`TreeMap` ������ʹ�� `null` ��Ϊ������Ϊ `TreeMap` ��Ҫ�Լ����бȽ����򣬶� `null` �޷����бȽϣ����� `null` �����׳� `NullPointerException`��
- **���ܿ���**��`TreeMap` �Ĳ��롢ɾ���Ͳ��Ҳ�����ʱ�临�Ӷȶ��� \(O(log n)\)������ n �� `TreeMap` ��Ԫ�ص�������������Ϊ�������һ��ƽ��Ķ����������������Ա�֤���ĸ߶�ʼ�ձ����� \(O(log n)\) ���𡣵��ǣ������ҪƵ������������ʣ�`HashMap` �����Ǹ��õ�ѡ����Ϊ����ƽ��ʱ�临�Ӷ��� \(O(1)\)��

### 3. �Զ�������



����ͨ��ʵ�� `Comparator` �ӿ���ʵ���Զ�������`Comparator` �ӿڰ���һ�� `compare` �������÷���������������������һ������ֵ����ʾ���������ıȽϽ����






```java
import java.util.Comparator;
import java.util.TreeMap;

class Person {
    private String name;
    private int age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    @Override
    public String toString() {
        return "Person{name='" + name + "', age=" + age + "}";
    }
}

public class TreeMapCustomSorting {
    public static void main(String[] args) {
        // �Զ���Ƚ���������������
        Comparator<Person> ageComparator = Comparator.comparingInt(Person::getAge);
        TreeMap<Person, String> treeMap = new TreeMap<>(ageComparator);
        treeMap.put(new Person("Alice", 25), "Data1");
        treeMap.put(new Person("Bob", 20), "Data2");
        treeMap.put(new Person("Charlie", 30), "Data3");

        for (Person person : treeMap.keySet()) {
            System.out.println(person + ": " + treeMap.get(person));
        }
    }
}
```



����������У����Ƕ�����һ�� `Person` �࣬��������һ���Զ���� `Comparator` ������ `Person` ����������������

### 4. ��������ݽṹ



`TreeMap` ʹ�ú������Ϊ��ײ����ݽṹ���������һ����ƽ��Ķ���������������ÿ���ڵ���������һ���洢λ����ʾ�ڵ����ɫ����ɫ���ɫ����ͨ�����κ�һ���Ӹ���Ҷ�ӵ�·���ϸ����ڵ���ɫ��ʽ�����ƣ������ȷ��û��һ��·���������·����������������ǽӽ�ƽ��ġ�



��������������ص㣺



- ÿ���ڵ�Ҫô�Ǻ�ɫ��Ҫô�Ǻ�ɫ��
- ���ڵ��Ǻ�ɫ��
- ÿ��Ҷ�ӽڵ㣨NIL �ڵ㣬�սڵ㣩�Ǻ�ɫ�ġ�
- ���һ���ڵ��Ǻ�ɫ�ģ������������ӽڵ㶼�Ǻ�ɫ�ġ�
- ��ÿ���ڵ㣬�Ӹýڵ㵽�����к��Ҷ�ڵ�ļ�·���ϣ���������ͬ��Ŀ�ĺ�ɫ�ڵ㡣



��Щ���ʱ�֤�˺�����ĸ߶�ʼ�ձ����� \(O(log n)\) ���𣬴Ӷ�ʹ�� `TreeMap` �Ĳ��롢ɾ���Ͳ��Ҳ��������нϺõ����ܡ�

## ��û����˳���Mapʵ����

�� Java �У��м��� `Map` ʵ������Ա�֤Ԫ�ص�˳������Ϊ����ϸ������Щ�༰�䱣֤����ķ�ʽ��

### 1. `LinkedHashMap`

#### ����



`LinkedHashMap` �� `HashMap` �����࣬���̳��� `HashMap` �Ļ������ԣ�ͬʱά����һ��˫���������ڼ�¼Ԫ�صĲ���˳����߷���˳�򣬴Ӷ���֤Ԫ�ص������ԡ�

#### ��֤����ķ�ʽ



- **����˳��**����ʹ��Ĭ�Ϲ��캯������ `LinkedHashMap` ʱ�����ᰴ��Ԫ�صĲ���˳����ά��Ԫ�ء�ÿ�β���һ����Ԫ��ʱ����Ԫ�ػᱻ��ӵ�˫�������β������ˣ����� `LinkedHashMap` ʱ��Ԫ�ػᰴ�ղ�����Ⱥ�˳�����γ��֡�





```java
import java.util.LinkedHashMap;
import java.util.Map;

public class LinkedHashMapInsertionOrderExample {
    public static void main(String[] args) {
        LinkedHashMap<String, Integer> linkedHashMap = new LinkedHashMap<>();
        linkedHashMap.put("one", 1);
        linkedHashMap.put("two", 2);
        linkedHashMap.put("three", 3);

        for (Map.Entry<String, Integer> entry : linkedHashMap.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }
}
```



- **����˳��**������ͨ�����캯�� `LinkedHashMap(int initialCapacity, float loadFactor, boolean accessOrder)` ���� `accessOrder` ��������Ϊ `true` �����÷���˳���ڷ���˳��ģʽ�£�ÿ�η��ʣ��� `get` ������һ��Ԫ��ʱ����Ԫ�ػᱻ�ƶ���˫�������β�������������δ�����ʵ�Ԫ�ػ�λ������ͷ����������ʵ�ֻ�����̭���ԣ��� LRU ���棩��








```java
import java.util.LinkedHashMap;
import java.util.Map;

public class LinkedHashMapAccessOrderExample {
    public static void main(String[] args) {
        LinkedHashMap<String, Integer> linkedHashMap = new LinkedHashMap<>(16, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<String, Integer> eldest) {
                return size() > 3;
            }
        };
        linkedHashMap.put("one", 1);
        linkedHashMap.put("two", 2);
        linkedHashMap.put("three", 3);
        // ����Ԫ�� "one"
        linkedHashMap.get("one");

        for (Map.Entry<String, Integer> entry : linkedHashMap.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }
}
```

### 2. `TreeMap`

#### ����



`TreeMap` �ǻ��ں������һ����ƽ��Ķ�����������ʵ�ֵ� `Map`��������ݼ�����Ȼ˳�����ָ���ıȽ�������Ԫ�ؽ������򣬴Ӷ���֤Ԫ�ص������ԡ�

#### ��֤����ķ�ʽ



- **��Ȼ˳��**�������ʵ���� `Comparable` �ӿڣ�`TreeMap` ����ݼ�����Ȼ˳���Ԫ�ؽ����������磬���� `Integer`��`String` ��ʵ���� `Comparable` �ӿڵ��࣬`TreeMap` �ᰴ�����ǵ���Ȼ˳���������Ĵ�С˳���ַ������ֵ���������Ԫ�ء�












```java
import java.util.TreeMap;
import java.util.Map;

public class TreeMapNaturalOrderExample {
    public static void main(String[] args) {
        TreeMap<String, Integer> treeMap = new TreeMap<>();
        treeMap.put("banana", 2);
        treeMap.put("apple", 1);
        treeMap.put("cherry", 3);

        for (Map.Entry<String, Integer> entry : treeMap.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }
}
```



- **ָ���Ƚ���**������ͨ�����캯�� `TreeMap(Comparator comparator)` ����һ���Զ���ıȽ�����`TreeMap` ����ݸñȽ����Ĺ����Ԫ�ؽ�������









```java
import java.util.Comparator;
import java.util.TreeMap;
import java.util.Map;

public class TreeMapCustomComparatorExample {
    public static void main(String[] args) {
        Comparator<String> lengthComparator = (s1, s2) -> s1.length() - s2.length();
        TreeMap<String, Integer> treeMap = new TreeMap<>(lengthComparator);
        treeMap.put("banana", 2);
        treeMap.put("apple", 1);
        treeMap.put("cherry", 3);

        for (Map.Entry<String, Integer> entry : treeMap.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }
}
```







����������`LinkedHashMap` ͨ��˫��������ά��Ԫ�صĲ���˳������˳�򣬶� `TreeMap` ��ͨ��������ͼ��ıȽϹ�������֤Ԫ�ص������ԡ�

## HashMap�Ĺ�ϣ/�Ŷ���������ô��Ƶ�


### ��Ʊ���



�� `HashMap` �Ԫ�صĴ洢λ����ͨ�����Ĺ�ϣֵ�����鳤�Ƚ���λ����õ��ġ���ֱ��ʹ�ü��� `hashCode()` �������صĹ�ϣֵ�����ܻ���ִ�����ϣ��ͻ��������Ϊ `hashCode()` ���صĹ�ϣֵ��λ�����ڼ���洢λ��ʱ����δ��������á�Ϊ�˽������ֳ�ͻ�ĸ��ʣ�`HashMap` �����˹�ϣ / �Ŷ��������Լ���ԭʼ��ϣֵ���д���

### ����ʵ��



�� JDK 8 ��֮��İ汾�У�`HashMap` �Ĺ�ϣ / �Ŷ������� `hash()` ����ʵ�֣��������£�













```java
static final int hash(Object key) {
    int h;
    return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
}
```



�����Ƕ���δ������ϸ���ͣ�



1. **�ռ�����**������ `key` Ϊ `null`��ֱ�ӷ��ع�ϣֵ 0������� `HashMap` �����Ϊ `null`���� `null` ���Ĺ�ϣֵ�̶�Ϊ 0���ᱻ�洢������ĵ�һ��λ�á�
2. **��ȡ����ԭʼ��ϣֵ**�����ü��� `hashCode()` ������ȡԭʼ��ϣֵ�������丳ֵ������ `h`��
3. **�Ŷ�����**���� `h` ���� 16 λ��`h >>> 16`����Ȼ���� `h` ����������㣨`^`�����������Ĺ�������ͬΪ 0����ͬΪ 1��ͨ������ 16 λ���� `h` �ĸ� 16 λ�Ƶ��� 16 λ������ԭ��ϣֵ����������㣬�ø� 16 λ�͵� 16 λ����Ϣ�����뵽���չ�ϣֵ�ļ����С�

### ���Ŀ�ĺ�����



- **������ù�ϣֵ�ĸ�λ��Ϣ**���ڼ��� `HashMap` �洢λ��ʱ��ͨ��ʹ�� `(n - 1) & hash` ��ʽ������ `n` �����鳤�ȣ�����ʵ������ȡ��ϣֵ�ĵ�λ���֡���ֱ��ʹ��ԭʼ��ϣֵ����λ���ֿ����޷��Դ洢λ�õļ������Ӱ�졣ͨ���Ŷ�����������λ��Ϣ�����λ��ʹ�ù�ϣֵ�ĸ�λ�͵�λ���ܲ��뵽�洢λ�õļ����У������˹�ϣ��ͻ�Ŀ����ԡ�
- **���ٹ�ϣ��ͻ**��ͨ���Թ�ϣֵ�����Ŷ������ü�ֵ���ڹ�ϣ���зֲ��ø��Ӿ��ȣ����������������������������Ӷ������� `HashMap` �Ĳ��ҡ������ɾ�����������ܡ�

### ʾ��



������һ���򵥵�ʾ����չʾ�� `hash()` ������Ч����





```java
import java.util.HashMap;

public class HashMapHashFunctionExample {
    public static void main(String[] args) {
        String key = "example";
        int hashCode = key.hashCode();
        int hashValue = HashMap.hash(key);

        System.out.println("Original hash code: " + Integer.toBinaryString(hashCode));
        System.out.println("Hash value after perturbation: " + Integer.toBinaryString(hashValue));
    }
}
```







�����ʾ���У����ǿ��Կ����� `"example"` ��ԭʼ��ϣֵ�;����Ŷ����������Ĺ�ϣֵ�Ķ����Ʊ�ʾ���Ӷ�ֱ�۵ظ��ܵ��Ŷ������Թ�ϣֵ��Ӱ�졣



����������`HashMap` �Ĺ�ϣ / �Ŷ�����ͨ���������ƣ���������˹�ϣֵ�ĸ�λ��Ϣ����Ч�����˹�ϣ��ͻ�������� `HashMap` �����ܡ�



## �㻹֪����Щ��ϣ�����Ĺ���?����

1. ����ȡ�෨��H��key)=key%p��p<=N��,�ؼ��ֳ���?����?�ڹ�ϣ���ȵ�������p��������
��Ϊ��ַ����ȻHashMap?��?���Ż����죬Ч�ʸ�?��ɢ��Ҳ�����⡣
����֮�⣬������?�ֳ����Ĺ�ϣ��������?����
2. ֱ�Ӷ�ַ��
ֱ�Ӹ��� key ��ӳ�䵽��Ӧ������λ�ã�����1232�ŵ��±�1232��λ�á�
3. ���ַ�����
ȡ key ��ĳЩ���֣�����?λ�Ͱ�λ����Ϊӳ���λ��
4. ƽ?ȡ�з�
ȡ key ƽ?���м�?λ��Ϊӳ���λ��
5. �۵���
�� key �ָ��λ����ͬ��?�Σ�Ȼ������ǵĵ��Ӻ���Ϊӳ���λ��
## HashMapΪʲô�̲߳���ȫ�����ʵ���̰߳�ȫ

1. �����޸ģ���?���߳̽�?д��������?��ɾ���ȣ�ʱ����?���߳̽�?�����������ܻᵼ�¶�ȡ����?�µ�
   ���ݣ���?�׳� ConcurrentModificationException �쳣��
2. ?ԭ?�Բ����� HashMap ��?Щ��������ԭ?�ģ����磬����Ƿ����ĳ��������ȡĳ������Ӧ��ֵ�ȣ���
   ���ڶ��̻߳����п��ܷ�?��̬����

Ϊ��ʵ���̰߳�ȫ�� HashMap ��������?��?ʽ��

- ʹ? Collections.synchronizedMap() ?��������ͨ�� Collections.synchronizedMap() ?������?
���̰߳�ȫ�� HashMap ����?������?��ͬ���� Map ��װ����ʹ�����ж� Map �Ĳ�������ͬ���ġ�

```java
Map<String, String> synchronizedMap = Collections.synchronizedMap(new HashMap<>());
```
- ʹ? ConcurrentHashMap �� ConcurrentHashMap ��ר?���?�ڶ��̻߳����Ĺ�ϣ��ʵ�֡���ʹ?�ֶ�
�����ƣ��������߳�ͬʱ��?����������?�������ܡ�

```java
Map<String, String> concurrentHashMap = new ConcurrentHashMap<>();
```
- ʹ?�����ƣ�������?����� HashMap ������ʹ?��ʽ���������� ReentrantLock ������֤�̰߳�ȫ��


```java
Map<String, String> customMap = new HashMap<>();
ReentrantLock lock = new ReentrantLock();
// ����Ҫ�̰߳�ȫ�Ĳ�����ʹ?��
lock.lock();
try {
 // ִ?����
} finally {
 lock.unlock();
}
```
## Hashmapѭ������Ĳ���


### 1. `HashMap` ���ݻ��Ƹ���



`HashMap` �ǻ������������ʵ�ֵģ���Ԫ������������ֵ�����鳤�ȳ��Ը������ӣ�ʱ���ᴥ�����ݲ���������ʱ���ᴴ��һ���µ����飬Ȼ��ԭ�����е�Ԫ������ɢ�е��������С�

### 2. JDK 1.7 �� `HashMap` ���ݴ������



������ JDK 1.7 �� `HashMap` ����ʱ��Ԫ��ת�Ƶ�������ĺ��Ĵ��룺






```java
void transfer(Entry[] newTable, boolean rehash) {
    int newCapacity = newTable.length;
    for (Entry<K,V> e : table) {
        while(null != e) {
            Entry<K,V> next = e.next;
            if (rehash) {
                e.hash = null == e.key ? 0 : hash(e.key);
            }
            int i = indexFor(e.hash, newCapacity);
            e.next = newTable[i];
            newTable[i] = e;
            e = next;
        }
    }
}
```



��δ�����߼��Ǳ���ԭ�����ÿ�������������е�Ԫ������ȡ�������¼����ϣֵ�����뵽������Ķ�Ӧλ�á�������õ���ͷ�巨������Ԫ�ػ���뵽�����ͷ����

### 3. ���̻߳�����ѭ������Ĳ�������



�����������߳� `T1` �� `T2` ͬʱ�� `HashMap` �������ݲ�����ԭ����Ϊ `A -> B`�������ǿ��ܵ���ѭ������Ĳ��裺

#### ���� 1���߳� `T1` �� `T2` ͬʱ���� `transfer` ����



��ʱ `T1` �� `T2` ����ȡ����ԭ��������ã����� `e` ָ�� `A`��`next` ָ�� `B`��

#### ���� 2���߳� `T1` �������߳� `T2` �������



�߳� `T2` ������ݺ��������˳���Ϊ `B -> A`����Ϊ����ͷ�巨����

#### ���� 3���߳� `T1` ����ִ��



- `T1` �� `e` ��Ȼָ�� `A`��`next` ָ�� `B`��
- `T1` �� `A` ���뵽�������ĳ��λ�ã���ʱ `A` �� `next` ָ�� `null`��
- Ȼ�� `e` ָ�� `B`��`next` ָ�� `null`����Ϊ `T2` �Ѿ��ı�������˳�򣩡�
- `T1` �� `B` ���뵽�������ĳ��λ�ã������� `B` �� `next` ָ�� `A`��ͷ�巨����
- ���� `T1` Ҫ���� `A`������ `B` �� `next` �Ѿ�ָ�� `A`��`T1` �ֽ� `A` ���뵽 `B` ��ǰ�棬��ʱ���γ���ѭ������ `A <-> B`��

### 4. ʾ��������ʾ



������һ���򵥵�ʾ�����룬ģ����̻߳����� `HashMap` ���ݿ��ܲ���ѭ������������






```java
import java.util.HashMap;

public class HashMapCycleDemo {
    private static HashMap<Integer, Integer> map = new HashMap<>(2, 1.5f);

    public static void main(String[] args) {
        map.put(5, 55);

        // �߳� 1
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                new Thread(() -> {
                    map.put(i, i);
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }, "ftf" + i).start();
            }
        });

        // �߳� 2
        Thread t2 = new Thread(() -> {
            while (true) {
                if (map.get(5) == null) {
                    break;
                }
            }
        });

        t1.start();
        t2.start();
    }
}
```







�����ʾ���У�����߳�ͬʱ�� `HashMap` ���в�����������ܻᴥ�����ݣ��Ӷ���һ�����ʲ���ѭ������


## HashMap����ʱ����remove����ʲôʱ������

### 1. `remove` ������������



���� `HashMap` ���Ƴ��ڵ�ʱ������Ƴ��ڵ�������Ľڵ�����С�ڵ��� 6 ������������˻�Ϊ����������Ϊ�������ά���ɱ���Խϸߣ����ڵ���������ʱ������Ĳ���Ч�ʸ��ߣ���˻�������˻�Ϊ����Ĳ�������������ܡ�



������ `HashMap` �� `remove` �������ܴ������˻�����ش����߼����� `TreeNode` ��� `removeTreeNode` �����У���














```java
if (root == null || root.right == null ||
    (rl = root.left) == null || rl.left == null) {
    tab[index] = first.untreeify(map);  // too small
    return;
}
```



�� `untreeify` �����лὫ�����ת��Ϊ����

### 2. ���ݲ�����������



�� `HashMap` �������ݲ���ʱ����Խڵ�������¹�ϣ�ͷ��䡣����������У������ֺ�ĺ�����ڵ�����С�ڵ��� 6 ���������Ҳ���˻�Ϊ����



`HashMap` �����ݲ�����Ҫ�� `resize` ������ʵ�֣�������������ֺͿ��ܵ����˻���صĴ���Ƭ�Σ�










```java
if (loHead != null) {
    if (lc <= UNTREEIFY_THRESHOLD)
        tab[j] = loHead.untreeify(map);
    else {
        tab[j] = loHead;
        if (hiHead != null) // (else is already treeified)
            loHead.treeify(tab);
    }
}
if (hiHead != null) {
    if (hc <= UNTREEIFY_THRESHOLD)
        tab[j + oldCap] = hiHead.untreeify(map);
    else {
        tab[j + oldCap] = hiHead;
        if (loHead != null)
            hiHead.treeify(tab);
    }
}
```







����� `UNTREEIFY_THRESHOLD` ����ֵΪ 6������ֺ�ĳһ���ֵĽڵ����� `lc` �� `hc` С�ڵ��� 6 ʱ������� `untreeify` �����������ת��Ϊ����



������������ `HashMap` �У����� `remove` ���������ݲ���Ҳ���ܻ���Ϊ��ֺ����ڵ��������ٵ���ֵ���¶�����������˻�Ϊ����Ĳ�����


## Ϊʲôhashmap�ĸ���������0��75


�� Java �� `HashMap` �У��������ӣ�`load factor`��Ĭ��ֵΪ 0.75�����ǻ���ʱ��Ϳռ�ɱ����ۺϿ�����������Ȩ��ѡ������Ӽ���������ϸ����ԭ��

### 1. �ռ���ʱ���ƽ��

- �������ӹ�С
    - ��������������õý�С������ 0.5����ô�ڹ�ϣ����Ԫ�������ﵽ���鳤�ȵ�һ��ʱ���ͻᴥ�����ݲ�����Ƶ�������ݲ����ᵼ�¸�����ڴ汻���䣬�����˿ռ俪����ͬʱ�����ݲ�����Ҫ���¼���Ԫ�صĹ�ϣֵ����Ԫ�����²��뵽�µ������У������������ʱ��ɱ���
- �������ӹ���
    - �������������õýϴ��� 1.0����ζ��ֻ�е�������ȫ����ʱ�Ż�������ݡ�������Ȼ���������ݵĴ�������ʡ�˿ռ䣬���ᵼ�¹�ϣ��ͻ�ĸ��ʴ�����ӡ���Ϊ��ϣ���е�Ԫ��Խ��Խ�࣬��ͬ�ļ����ܻ�ӳ�䵽��ͬ������λ�ã��γɽϳ��������� JDK 8 ���Ժ������ȹ��������ܻ�ת��Ϊ����������Ӷ�ʹ���ҡ������ɾ��������ʱ�临�Ӷ����ߣ�Ӱ�����ܡ�
- ��������Ϊ 0.75 ������
    - ����������ʵ���ʵ����֤��0.75 ���ֵ�ڿռ��ʱ��ɱ�֮��ȡ���˽Ϻõ�ƽ�⡣��������������£���ϣ��ͻ�ĸ�����Խϵͣ�ͬʱҲ�������Ƶ���ؽ������ݲ������ܹ��ڱ�֤һ�����ܵ�ǰ���£����������ڴ�ռ䡣

### 2. ���ϲ��ɷֲ�

- ����������£���ϣ���е�Ԫ��Ӧ�þ��ȷֲ�������ĸ���λ�á���ʵ���ϣ����ڹ�ϣ�����ľ����ԣ�Ԫ�صķֲ������һ����ƫ�����ֹ�ϣ��ͻ��
- ����������Ϊ 0.75 ʱ����ϣ���������ȵķֲ����ϲ��ɷֲ������ɷֲ���һ�ֳ����ĸ��ʷֲ�������������һ��ʱ���ռ�������¼������Ĵ������� `HashMap` �У������ȵķֲ���ѭ���ɷֲ���ζ�Ŵ󲿷�����ĳ��Ƚ϶̣��������Ա�֤���ҡ������ɾ��������ʱ�临�ӶȽӽ� *O*(1)��
- ���磬������Ϊ 8 �ĸ��ʷǳ��ͣ�ԼΪ 6��10?8������ JDK 8 �У��������ȴﵽ 8 ʱ�������ת��Ϊ�����������߲���Ч�ʡ�����������Ϊ 0.75 ʱ�������ȴﵽ 8 ��������ٳ��֣������˲���Ҫ�ĺ����ת����������һ�����������ܡ�

### 3. �����ʵ����ѡ��

- Java �Ŀ����Ŷ������ `HashMap` ʱ�������˴��������ܲ��Ժ�ʵ��Ӧ�õ���֤������ѡ���� 0.75 ��ΪĬ�ϵĸ������ӡ����ֵ�ڶ��ֳ����¶����ֳ��˽Ϻõ����ܺ��ȶ��ԣ����㷺��Ϊ��һ���ȽϺ��ʵľ���ֵ��

����������`HashMap` ��������������Ϊ 0.75 ��Ϊ���ڿռ�ʹ��Ч�ʺͲ�������֮���ҵ�һ����ѵ�ƽ��㣬ͬʱҲ���Ϲ�ϣ��ͻ�ĸ��ʷֲ����ɣ��Ǿ���ʵ����֤�Ľ�Ϊ�����ѡ��


## ʹ��HashMap��ʱ����α����ڴ�й©


��ʹ�� `HashMap` ʱ��Ϊ�����ڴ�й©����Ҫ�Ӷ��������п��������潫��ϸ�������ܵ����ڴ�й©��ԭ����Ӧ�Ľ���취��

### 1. ������ʹ�������û�������

#### ԭ��



�� `HashMap` �еļ����������ط�ǿ���ã��� `HashMap` ����һֱ����ʱ����ʹ��Щ�������������ط�����ʹ�ã����� `HashMap` �������ǵ�ǿ���ã������������޷�������Щ���������Ӧ��ֵ���󣬴Ӷ�����ڴ�й©��

#### ����취



����ʹ�� `WeakHashMap` ���Զ���ʹ�������ã�`WeakReference`���������ã�`SoftReference`������װ������`WeakHashMap` �еļ��������ã�����������������ʱ����Ӧ�ļ�ֵ�Ի��Զ��� `WeakHashMap` ���Ƴ���



**ʾ������**��











```java
import java.util.WeakHashMap;

public class WeakHashMapExample {
    public static void main(String[] args) {
        WeakHashMap<Object, String> weakHashMap = new WeakHashMap<>();
        Object key = new Object();
        weakHashMap.put(key, "value");

        // �Ƴ��Լ������ǿ����
        key = null;

        // ������������
        System.gc();

        try {
            // �ȴ������������
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // ��ʱ weakHashMap Ӧ��Ϊ��
        System.out.println(weakHashMap.size()); 
    }
}
```

### 2. ��ʱ������ʹ�õļ�ֵ��

#### ԭ��



��� `HashMap` ������Ӽ�ֵ�ԣ�����������ʹ�õļ�ֵ�ԣ��ᵼ�� `HashMap` ����ռ���ڴ棬���տ��������ڴ�й©��

#### ����취



��ҵ���߼��У���ȷ��ĳЩ��ֵ�Բ���ʹ��ʱ����ʱ���� `remove` ��������� `HashMap` ���Ƴ���



**ʾ������**��








```java
import java.util.HashMap;

public class HashMapCleanupExample {
    public static void main(String[] args) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("key1", "value1");
        hashMap.put("key2", "value2");

        // ���� key1 ��Ӧ�ļ�ֵ�Բ���ʹ��
        hashMap.remove("key1");

        System.out.println(hashMap.size()); 
    }
}
```

### 3. �����ڼ�������ʹ�ÿɱ����

#### ԭ��



���ʹ�ÿɱ������Ϊ `HashMap` �ļ�������������Է����ı�ʱ���� `hashCode` �� `equals` �����ķ���ֵ���ܻ�ı䣬��ᵼ���� `HashMap` ���޷���ȷ��λ�ü�ֵ�ԣ�ʹ�øü�ֵ���޷����������ʺ��Ƴ����Ӷ�����ڴ�й©��

#### ����취



����ʹ�ò��ɱ������ `String`��`Integer` �ȣ���Ϊ `HashMap` �ļ����������ʹ�ÿɱ����Ҫȷ���ڶ������Ըı�ʱ������Ӱ���� `hashCode` �� `equals` �����ķ���ֵ��

### 4. ���⾲̬ `HashMap` ���д����

#### ԭ��



��̬����������������Ӧ�ó��������������ͬ�������̬�� `HashMap` ���д��������Щ������Ӧ�ó������й����в���ʹ�ã������ھ�̬ `HashMap` �����ã������޷����������գ�������ڴ�й©��

#### ����취



��������ʹ�þ�̬�� `HashMap` ���洢��������ȷʵ��Ҫʹ�þ�̬ `HashMap`���ڶ�����ʹ��ʱ����ʱ����� `HashMap` ���Ƴ���

### 5. ע�������ʹ��

#### ԭ��



��ʹ�õ��������� `HashMap` ʱ������ڵ���������ʹ�� `HashMap` �� `remove` ���������ǵ������� `remove` �������Ƴ�Ԫ�أ����׳� `ConcurrentModificationException` �쳣�����ҿ��ܵ����ڴ�й©��

#### ����취



�ڵ��������У������Ҫ�Ƴ�Ԫ�أ�ʹ�õ������� `remove` ������



**ʾ������**��









```java
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class HashMapIteratorExample {
    public static void main(String[] args) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("key1", "value1");
        hashMap.put("key2", "value2");

        Iterator<Map.Entry<String, String>> iterator = hashMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();
            if ("key1".equals(entry.getKey())) {
                // ʹ�õ������� remove �����Ƴ�Ԫ��
                iterator.remove(); 
            }
        }

        System.out.println(hashMap.size()); 
    }
}
```









