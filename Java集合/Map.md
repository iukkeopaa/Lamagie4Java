## .HashMap的put流程知道吗？

### 1. 整体流程概述



当调用 `HashMap` 的 `put(K key, V value)` 方法时，它会根据键的哈希值计算存储位置，然后将键值对存储到相应的位置。如果该位置已经有元素，会根据不同情况进行处理，可能会发生链表节点的插入、红黑树节点的插入或者键值对的替换。

### 2. 详细流程步骤

#### 2.1 检查 `HashMap` 是否为空



如果 `HashMap` 的数组（`table`）为空或者长度为 0，会调用 `resize()` 方法进行扩容操作，将数组初始化。

#### 2.2 计算键的哈希值和存储位置



- 首先，通过 `hash(key)` 方法计算键的哈希值。`hash(key)` 方法会对键的 `hashCode()` 进行二次哈希，以减少哈希冲突的概率。









```java
static final int hash(Object key) {
    int h;
    return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
}
```



- 然后，根据哈希值和数组长度计算存储位置 `i`，使用 `(n - 1) & hash` 公式，其中 `n` 是数组的长度。

#### 2.3 检查存储位置是否为空



- 如果计算得到的存储位置 `table[i]` 为空，说明该位置没有元素，直接创建一个新的节点并放入该位置。

#### 2.4 处理哈希冲突



如果存储位置已经有元素，说明发生了哈希冲突，需要进一步处理：



- **判断键是否相同**：比较当前节点的键和要插入的键是否相等（使用 `==` 或者 `equals()` 方法），如果相等，则直接替换该节点的值。
- **判断是否为红黑树节点**：如果当前节点是红黑树节点（`TreeNode` 类型），则调用红黑树的插入方法 `putTreeVal()` 将键值对插入到红黑树中。
- **处理链表节点**：如果当前节点是链表节点，会遍历链表，比较每个节点的键和要插入的键是否相等。如果找到相等的键，就替换该节点的值；如果遍历到链表末尾都没有找到相等的键，就创建一个新的节点插入到链表尾部。插入后，如果链表长度达到 8 且数组长度大于 64，会将链表转换为红黑树，调用 `treeifyBin()` 方法。

#### 2.5 检查是否需要扩容



插入键值对后，会检查 `HashMap` 的元素数量是否超过了阈值（`threshold`），如果超过了，会调用 `resize()` 方法进行扩容操作。

### 3. 代码示例



以下是简化的 `put` 方法代码示例，帮助理解上述流程：







```java
public V put(K key, V value) {
    return putVal(hash(key), key, value, false, true);
}

final V putVal(int hash, K key, V value, boolean onlyIfAbsent,
               boolean evict) {
    Node<K,V>[] tab; Node<K,V> p; int n, i;
    // 检查 table 是否为空或长度为 0，若为空则进行扩容
    if ((tab = table) == null || (n = tab.length) == 0)
        n = (tab = resize()).length;
    // 计算存储位置，若该位置为空，直接创建新节点放入
    if ((p = tab[i = (n - 1) & hash]) == null)
        tab[i] = newNode(hash, key, value, null);
    else {
        Node<K,V> e; K k;
        // 判断键是否相同
        if (p.hash == hash &&
            ((k = p.key) == key || (key != null && key.equals(k))))
            e = p;
        // 判断是否为红黑树节点
        else if (p instanceof TreeNode)
            e = ((TreeNode<K,V>)p).putTreeVal(this, tab, hash, key, value);
        else {
            // 处理链表节点
            for (int binCount = 0; ; ++binCount) {
                if ((e = p.next) == null) {
                    p.next = newNode(hash, key, value, null);
                    // 链表长度达到 8 且数组长度大于 64，将链表转换为红黑树
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
        // 若键已存在，替换值
        if (e != null) { 
            V oldValue = e.value;
            if (!onlyIfAbsent || oldValue == null)
                e.value = value;
            afterNodeAccess(e);
            return oldValue;
        }
    }
    ++modCount;
    // 检查是否需要扩容
    if (++size > threshold)
        resize();
    afterNodeInsertion(evict);
    return null;
}
```

### 4. 总结



`HashMap` 的 `put` 方法主要包括计算哈希值、处理哈希冲突、插入键值对和检查扩容等步骤。通过这些步骤，`HashMap` 能够高效地存储和管理键值对，同时在处理哈希冲突时，使用链表和红黑树的结合，提高了查找和插入的性能。

| 步骤         | 描述                                                         |
| ------------ | ------------------------------------------------------------ |
| 开始         | 调用 `HashMap` 的 `put(K key, V value)` 方法                 |
| 计算哈希值   | 通过 `hash(key)` 方法计算键的哈希值                          |
| 检查数组状态 | 检查 `HashMap` 的数组（`table`）是否为空或者长度为 0 - 是，调用 `resize()` 方法进行扩容，进入下一步 - 否，进入下一步 |
| 计算存储位置 | 根据哈希值和数组长度，使用 `(n - 1) & hash` 公式计算存储位置 `i`（`n` 为数组长度） |
| 检查存储位置 | 检查 `table[i]` 是否为空 - 是，创建一个新的节点并放入 `table[i]`，结束 - 否，进入下一步 |
| 处理哈希冲突 | 当前位置已有元素，进行以下判断： 1. 判断当前节点的键和要插入的键是否相等（使用 `==` 或者 `equals()` 方法） - 是，直接替换该节点的值，结束 - 否，进入下一步 2. 判断当前节点是否为红黑树节点（`TreeNode` 类型） - 是，调用红黑树的插入方法 `putTreeVal()` 将键值对插入到红黑树中，结束 - 否，进入下一步 3. 处理链表节点：遍历链表，比较每个节点的键和要插入的键是否相等 - 是，替换该节点的值，结束 - 否，若遍历到链表末尾都没有找到相等的键，创建一个新的节点插入到链表尾部。插入后，如果链表长度达到 8 且数组长度大于 64，调用 `treeifyBin()` 方法将链表转换为红黑树，结束 |
| 检查是否扩容 | 检查 `HashMap` 的元素数量 `size` 是否超过了阈值（`threshold`） - 是，调用 `resize()` 方法进行扩容 - 否，结束 |
| 结束         | 返回 `null` 或者被替换的旧值（如果键已存在被替换）           |


## HashMap怎么查找元素的呢？

### 1. 整体流程概述



当调用 `HashMap` 的 `get(Object key)` 方法查找元素时，它会先计算键的哈希值，然后根据哈希值找到对应的存储位置，接着在该位置的链表或红黑树中查找目标键对应的节点，最后返回节点的值。

### 2. 详细流程步骤

#### 2.1 计算键的哈希值



调用 `hash(key)` 方法对传入的键进行哈希计算，这个方法会对键的 `hashCode()` 进行二次哈希，以减少哈希冲突的概率。










```java
static final int hash(Object key) {
    int h;
    return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
}
```

#### 2.2 检查 `HashMap` 数组



检查 `HashMap` 的数组（`table`）是否为空或者长度为 0，以及根据哈希值计算得到的存储位置是否为空。如果数组为空、长度为 0 或者该存储位置为空，说明没有找到目标元素，直接返回 `null`。

#### 2.3 比较首节点



如果存储位置不为空，比较该位置的首节点的键和要查找的键是否相等（使用 `==` 或者 `equals()` 方法）。如果相等，说明找到了目标元素，返回该节点的值。

#### 2.4 判断节点类型



如果首节点的键和要查找的键不相等，需要判断该节点是链表节点还是红黑树节点：



- **红黑树节点**：如果是红黑树节点（`TreeNode` 类型），调用红黑树的查找方法 `getTreeNode()` 在红黑树中查找目标键对应的节点。如果找到，返回该节点的值；如果未找到，返回 `null`。
- **链表节点**：如果是链表节点，遍历链表，依次比较每个节点的键和要查找的键是否相等。如果找到相等的键，返回该节点的值；如果遍历到链表末尾都没有找到相等的键，返回 `null`。

### 3. 代码示例



以下是 `get` 方法的简化代码示例，帮助理解上述流程：









```java
public V get(Object key) {
    Node<K,V> e;
    return (e = getNode(hash(key), key)) == null ? null : e.value;
}

final Node<K,V> getNode(int hash, Object key) {
    Node<K,V>[] tab; Node<K,V> first, e; int n; K k;
    // 检查数组是否为空、长度是否为 0 以及存储位置是否为空
    if ((tab = table) != null && (n = tab.length) > 0 &&
        (first = tab[(n - 1) & hash]) != null) {
        // 比较首节点
        if (first.hash == hash && 
            ((k = first.key) == key || (key != null && key.equals(k))))
            return first;
        if ((e = first.next) != null) {
            // 判断是否为红黑树节点
            if (first instanceof TreeNode)
                return ((TreeNode<K,V>)first).getTreeNode(hash, key);
            // 遍历链表
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









### 4. 总结



`HashMap` 查找元素的过程主要依赖于键的哈希值来定位存储位置，然后根据该位置的节点类型（链表或红黑树）进行进一步的查找。通过这种方式，`HashMap` 可以在平均情况下以 \(O(1)\) 的时间复杂度完成查找操作，在哈希冲突较多时，最坏情况下的时间复杂度为 \(O(n)\)（链表情况）或 \(O(log n)\)（红黑树情况）。


## HashMap的哈希/扰动函数是怎么设计的?


HashMap的哈希函数是先拿到 key 的hashcode，是?个32位的int类型的数值，然后让hashcode的?16位和低16位进?异或操作。

## HashMap的扩容机制是什么

### 1. 核心属性



在了解扩容机制前，先认识几个与扩容相关的核心属性：










```java
// 默认初始容量，必须是 2 的幂次方，这里是 16
static final int DEFAULT_INITIAL_CAPACITY = 1 << 4; 
// 最大容量，为 2 的 30 次方
static final int MAXIMUM_CAPACITY = 1 << 30; 
// 默认加载因子，用于衡量何时进行扩容
static final float DEFAULT_LOAD_FACTOR = 0.75f; 
// 存储键值对的数组，每个位置称为一个桶
transient Node<K,V>[] table; 
// 键值对的数量
transient int size; 
// 扩容阈值，当 size 达到该值时进行扩容
int threshold; 
// 加载因子
final float loadFactor; 
```

### 2. 扩容触发条件



当 `HashMap` 中的键值对数量 `size` 超过扩容阈值 `threshold` 时，就会触发扩容操作。扩容阈值 `threshold` 的计算公式为：`threshold = capacity * loadFactor`，其中 `capacity` 是当前数组的容量，`loadFactor` 是加载因子。默认情况下，初始容量 `capacity` 为 16，加载因子 `loadFactor` 为 0.75f，所以初始的扩容阈值 `threshold` 为 12（16 * 0.75）。

### 3. 扩容过程



扩容操作主要通过 `resize()` 方法实现，下面是该方法的详细步骤和代码分析。

#### 3.1 记录旧数组信息






```java
Node<K,V>[] oldTab = table;
int oldCap = (oldTab == null) ? 0 : oldTab.length;
int oldThr = threshold;
```



首先记录旧数组 `oldTab`、旧容量 `oldCap` 和旧扩容阈值 `oldThr`。

#### 3.2 计算新容量和新扩容阈值









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



- 如果旧容量



  ```
  oldCap
  ```



大于 0：

- 若旧容量已经达到最大容量 `MAXIMUM_CAPACITY`，则将扩容阈值 `threshold` 设置为 `Integer.MAX_VALUE`，不再进行扩容。
- 否则，将新容量 `newCap` 扩大为旧容量的 2 倍（`oldCap << 1`），如果新容量小于最大容量且旧容量大于等于默认初始容量，将新扩容阈值 `newThr` 也扩大为旧扩容阈值的 2 倍。

- 如果旧容量为 0，但旧扩容阈值 `oldThr` 大于 0，将新容量 `newCap` 设置为旧扩容阈值。

- 如果旧容量和旧扩容阈值都为 0，使用默认的初始容量和加载因子计算新容量和新扩容阈值。

#### 3.3 创建新数组













```java
@SuppressWarnings({"rawtypes","unchecked"})
Node<K,V>[] newTab = (Node<K,V>[])new Node[newCap];
table = newTab;
```



根据新容量 `newCap` 创建一个新的数组 `newTab`，并将 `table` 引用指向新数组。

#### 3.4 迁移元素













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







- 遍历旧数组的每个桶：
    - 如果桶中只有一个元素，直接根据新容量重新计算其在新数组中的位置并放入。
    - 如果桶中的元素是红黑树节点，调用 `split()` 方法将红黑树进行拆分并迁移到新数组中。
    - 如果桶中的元素是链表节点，将链表拆分为两个链表，一个链表的元素在新数组中的位置与旧位置相同，另一个链表的元素在新数组中的位置为旧位置加上旧容量。然后将这两个链表分别放入新数组的相应位置。

### 4. 总结



`HashMap` 的扩容机制通过将数组容量扩大为原来的 2 倍，并重新计算元素在新数组中的位置，保证了 `HashMap` 在元素数量增加时仍能保持较好的性能。但扩容操作会带来一定的性能开销，因为需要重新分配内存并迁移元素，所以在创建 `HashMap` 时，如果能预估元素数量，建议指定合适的初始容量，以减少扩容次数。


## Hash冲突怎么解决

HashMap 是一种常用的数据结构，用于存储键值对，其核心在于解决哈希冲突。除了常见的拉链法（链地址法），还有以下几种处理哈希冲突的方式：

### 开放寻址法



开放寻址法的核心思想是当发生哈希冲突时，通过一定的规则在哈希表中寻找下一个可用的位置来存放冲突的元素。常见的开放寻址法有以下几种具体实现：

#### 线性探测法



- **原理**：当发生哈希冲突时，从冲突位置开始，依次向后探测下一个位置，直到找到一个空位置为止。如果探测到哈希表的末尾还没有找到空位置，则从哈希表的开头继续探测。
- **示例代码（Java 简单示意）**：










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

#### 二次探测法



- **原理**：当发生哈希冲突时，探测的步长不再是固定的 1，而是按照二次函数的方式进行递增，如 \(h_i=(h(key)+i^2)\bmod m\) 或 \(h_i=(h(key)-i^2)\bmod m\)（\(i = 1, 2, 3, \cdots\)），其中 \(h(key)\) 是初始哈希值，m 是哈希表的大小。
- **特点**：二次探测法可以在一定程度上缓解线性探测法容易产生的 “聚集” 问题，但可能会导致某些位置无法被探测到。

#### 双重哈希法



- **原理**：使用两个不同的哈希函数 \(h_1(key)\) 和 \(h_2(key)\)。当发生哈希冲突时，按照 \(h_i=(h_1(key)+i\times h_2(key))\bmod m\) 的方式进行探测，其中 \(i = 1, 2, 3, \cdots\)。\(h_2(key)\) 的值要保证与哈希表的大小 m 互质，这样可以保证能够遍历到哈希表中的所有位置。
- **特点**：双重哈希法能够更均匀地分布冲突元素，减少聚集现象，但需要额外的哈希函数计算。

### 再哈希法



- **原理**：准备多个哈希函数，当使用第一个哈希函数发生冲突时，就换用第二个哈希函数，直到找到一个不冲突的位置。即 \(h_i = H_i(key)\)（\(i = 1, 2, \cdots\)），其中 \(H_i\) 是不同的哈希函数。
- **特点**：这种方法的优点是可以更灵活地处理冲突，但需要额外的哈希函数计算，增加了计算开销。

### 公共溢出区法



- **原理**：将哈希表分为基本表和溢出表两部分。当发生哈希冲突时，将冲突的元素放入溢出表中。查找时，先在基本表中查找，如果找不到，再到溢出表中查找。
- **特点**：这种方法的优点是基本表中的元素相对较少，查找速度较快；缺点是溢出表可能会变得很大，导致查找溢出表的效率降低。

## TreeMap怎么排序的

### 1. `TreeMap` 的排序方式



`TreeMap` 是 Java 集合框架中的一个类，它实现了 `SortedMap` 接口，基于红黑树（Red-Black Tree）这种自平衡的二叉搜索树数据结构来存储键值对，并根据键的自然顺序或者自定义的比较器（`Comparator`）来对键进行排序。

#### 自然排序



当使用默认构造函数创建 `TreeMap` 时，它会使用键的自然顺序进行排序。键必须实现 `Comparable` 接口，`TreeMap` 会调用键的 `compareTo` 方法来确定元素的顺序。















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



在上述代码中，键是 `Integer` 类型，`Integer` 实现了 `Comparable` 接口，`TreeMap` 会按照键的升序（1, 2, 3）对元素进行排序。

#### 自定义排序



可以通过传入一个 `Comparator` 对象来指定自定义的排序规则。`Comparator` 接口定义了一个 `compare` 方法，用于比较两个对象的顺序。













```java
import java.util.Comparator;
import java.util.TreeMap;

public class TreeMapCustomOrderExample {
    public static void main(String[] args) {
        // 自定义比较器，按键的降序排序
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



在这个例子中，我们创建了一个自定义的 `Comparator`，它会使 `TreeMap` 按照键的降序（3, 2, 1）对元素进行排序。

### 2. 注意事项



- **键的不可变性**：由于 `TreeMap` 是根据键来排序的，键必须是不可变的。如果键在插入到 `TreeMap` 后发生了改变，可能会破坏 `TreeMap` 的排序规则，导致不可预期的结果。
- **`null` 键的限制**：`TreeMap` 不允许使用 `null` 作为键。因为 `TreeMap` 需要对键进行比较排序，而 `null` 无法进行比较，插入 `null` 键会抛出 `NullPointerException`。
- **性能考虑**：`TreeMap` 的插入、删除和查找操作的时间复杂度都是 \(O(log n)\)，其中 n 是 `TreeMap` 中元素的数量。这是因为红黑树是一种平衡的二叉搜索树，它可以保证树的高度始终保持在 \(O(log n)\) 级别。但是，如果需要频繁进行随机访问，`HashMap` 可能是更好的选择，因为它的平均时间复杂度是 \(O(1)\)。

### 3. 自定义排序



可以通过实现 `Comparator` 接口来实现自定义排序。`Comparator` 接口包含一个 `compare` 方法，该方法接收两个参数，返回一个整数值，表示两个参数的比较结果。






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
        // 自定义比较器，按年龄排序
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



在这个例子中，我们定义了一个 `Person` 类，并创建了一个自定义的 `Comparator` 来按照 `Person` 对象的年龄进行排序。

### 4. 排序的数据结构



`TreeMap` 使用红黑树作为其底层数据结构。红黑树是一种自平衡的二叉搜索树，它在每个节点上增加了一个存储位来表示节点的颜色（红色或黑色）。通过对任何一条从根到叶子的路径上各个节点着色方式的限制，红黑树确保没有一条路径会比其他路径长出两倍，因而是接近平衡的。



红黑树具有以下特点：



- 每个节点要么是红色，要么是黑色。
- 根节点是黑色。
- 每个叶子节点（NIL 节点，空节点）是黑色的。
- 如果一个节点是红色的，则它的两个子节点都是黑色的。
- 对每个节点，从该节点到其所有后代叶节点的简单路径上，均包含相同数目的黑色节点。



这些性质保证了红黑树的高度始终保持在 \(O(log n)\) 级别，从而使得 `TreeMap` 的插入、删除和查找操作都具有较好的性能。

## 有没有有顺序的Map实现类

在 Java 中，有几种 `Map` 实现类可以保证元素的顺序，下面为你详细介绍这些类及其保证有序的方式。

### 1. `LinkedHashMap`

#### 概述



`LinkedHashMap` 是 `HashMap` 的子类，它继承了 `HashMap` 的基本特性，同时维护了一个双向链表，用于记录元素的插入顺序或者访问顺序，从而保证元素的有序性。

#### 保证有序的方式



- **插入顺序**：当使用默认构造函数创建 `LinkedHashMap` 时，它会按照元素的插入顺序来维护元素。每次插入一个新元素时，该元素会被添加到双向链表的尾部。因此，遍历 `LinkedHashMap` 时，元素会按照插入的先后顺序依次出现。





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



- **访问顺序**：可以通过构造函数 `LinkedHashMap(int initialCapacity, float loadFactor, boolean accessOrder)` 并将 `accessOrder` 参数设置为 `true` 来启用访问顺序。在访问顺序模式下，每次访问（如 `get` 方法）一个元素时，该元素会被移动到双向链表的尾部。这样，最久未被访问的元素会位于链表头部，常用于实现缓存淘汰策略（如 LRU 缓存）。








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
        // 访问元素 "one"
        linkedHashMap.get("one");

        for (Map.Entry<String, Integer> entry : linkedHashMap.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }
}
```

### 2. `TreeMap`

#### 概述



`TreeMap` 是基于红黑树（一种自平衡的二叉搜索树）实现的 `Map`，它会根据键的自然顺序或者指定的比较器来对元素进行排序，从而保证元素的有序性。

#### 保证有序的方式



- **自然顺序**：如果键实现了 `Comparable` 接口，`TreeMap` 会根据键的自然顺序对元素进行排序。例如，对于 `Integer`、`String` 等实现了 `Comparable` 接口的类，`TreeMap` 会按照它们的自然顺序（如整数的大小顺序、字符串的字典序）来排列元素。












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



- **指定比较器**：可以通过构造函数 `TreeMap(Comparator comparator)` 传入一个自定义的比较器，`TreeMap` 会根据该比较器的规则对元素进行排序。









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







综上所述，`LinkedHashMap` 通过双向链表来维护元素的插入顺序或访问顺序，而 `TreeMap` 则通过红黑树和键的比较规则来保证元素的有序性。

## HashMap的哈希/扰动函数是怎么设计的


### 设计背景



在 `HashMap` 里，元素的存储位置是通过键的哈希值与数组长度进行位运算得到的。若直接使用键的 `hashCode()` 方法返回的哈希值，可能会出现大量哈希冲突，这是因为 `hashCode()` 返回的哈希值高位部分在计算存储位置时可能未被充分利用。为了降低这种冲突的概率，`HashMap` 引入了哈希 / 扰动函数，对键的原始哈希值进行处理。

### 具体实现



在 JDK 8 及之后的版本中，`HashMap` 的哈希 / 扰动函数由 `hash()` 方法实现，代码如下：













```java
static final int hash(Object key) {
    int h;
    return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
}
```



以下是对这段代码的详细解释：



1. **空键处理**：若键 `key` 为 `null`，直接返回哈希值 0。这表明 `HashMap` 允许键为 `null`，且 `null` 键的哈希值固定为 0，会被存储在数组的第一个位置。
2. **获取键的原始哈希值**：调用键的 `hashCode()` 方法获取原始哈希值，并将其赋值给变量 `h`。
3. **扰动处理**：将 `h` 右移 16 位（`h >>> 16`），然后与 `h` 进行异或运算（`^`）。异或运算的规则是相同为 0，不同为 1。通过右移 16 位，把 `h` 的高 16 位移到低 16 位，再与原哈希值进行异或运算，让高 16 位和低 16 位的信息都参与到最终哈希值的计算中。

### 设计目的和优势



- **充分利用哈希值的高位信息**：在计算 `HashMap` 存储位置时，通常使用 `(n - 1) & hash` 公式（其中 `n` 是数组长度），这实际上是取哈希值的低位部分。若直接使用原始哈希值，高位部分可能无法对存储位置的计算产生影响。通过扰动函数，将高位信息混入低位，使得哈希值的高位和低位都能参与到存储位置的计算中，降低了哈希冲突的可能性。
- **减少哈希冲突**：通过对哈希值进行扰动处理，让键值对在哈希表中分布得更加均匀，减少了链表或红黑树过长的情况，从而提升了 `HashMap` 的查找、插入和删除操作的性能。

### 示例



下面是一个简单的示例，展示了 `hash()` 函数的效果：





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







在这个示例中，我们可以看到键 `"example"` 的原始哈希值和经过扰动函数处理后的哈希值的二进制表示，从而直观地感受到扰动函数对哈希值的影响。



综上所述，`HashMap` 的哈希 / 扰动函数通过巧妙的设计，充分利用了哈希值的高位信息，有效减少了哈希冲突，提升了 `HashMap` 的性能。



## 你还知道哪些哈希函数的构造?法呢

1. 除留取余法：H（key)=key%p（p<=N）,关键字除以?个不?于哈希表长度的正整数p，所得余
数为地址，当然HashMap?进?了优化改造，效率更?，散列也更均衡。
除此之外，还有这?种常见的哈希函数构造?法：
2. 直接定址法
直接根据 key 来映射到对应的数组位置，例如1232放到下标1232的位置。
3. 数字分析法
取 key 的某些数字（例如?位和百位）作为映射的位置
4. 平?取中法
取 key 平?的中间?位作为映射的位置
5. 折叠法
将 key 分割成位数相同的?段，然后把它们的叠加和作为映射的位置
## HashMap为什么线程不安全？如何实现线程安全

1. 并发修改：当?个线程进?写操作（插?、删除等）时，另?个线程进?读操作，可能会导致读取到不?致的
   数据，甚?抛出 ConcurrentModificationException 异常。
2. ?原?性操作： HashMap 的?些操作不是原?的，例如，检查是否存在某个键、获取某个键对应的值等，这
   样在多线程环境中可能发?竞态条件

为了实现线程安全的 HashMap ，有以下?种?式：

- 使? Collections.synchronizedMap() ?法：可以通过 Collections.synchronizedMap() ?法创建?
个线程安全的 HashMap ，该?法返回?个同步的 Map 包装器，使得所有对 Map 的操作都是同步的。

```java
Map<String, String> synchronizedMap = Collections.synchronizedMap(new HashMap<>());
```
- 使? ConcurrentHashMap ： ConcurrentHashMap 是专?设计?于多线程环境的哈希表实现。它使?分段
锁机制，允许多个线程同时进?读操作，提?并发性能。

```java
Map<String, String> concurrentHashMap = new ConcurrentHashMap<>();
```
- 使?锁机制：可以在?定义的 HashMap 操作中使?显式的锁（例如 ReentrantLock ）来保证线程安全。


```java
Map<String, String> customMap = new HashMap<>();
ReentrantLock lock = new ReentrantLock();
// 在需要线程安全的操作中使?锁
lock.lock();
try {
 // 执?操作
} finally {
 lock.unlock();
}
```
## Hashmap循环链表的产生


### 1. `HashMap` 扩容机制概述



`HashMap` 是基于数组和链表实现的，当元素数量超过阈值（数组长度乘以负载因子）时，会触发扩容操作。扩容时，会创建一个新的数组，然后将原数组中的元素重新散列到新数组中。

### 2. JDK 1.7 中 `HashMap` 扩容代码分析



以下是 JDK 1.7 中 `HashMap` 扩容时将元素转移到新数组的核心代码：






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



这段代码的逻辑是遍历原数组的每个链表，将链表中的元素依次取出，重新计算哈希值并插入到新数组的对应位置。插入采用的是头插法，即新元素会插入到链表的头部。

### 3. 多线程环境下循环链表的产生过程



假设有两个线程 `T1` 和 `T2` 同时对 `HashMap` 进行扩容操作，原链表为 `A -> B`，以下是可能导致循环链表的步骤：

#### 步骤 1：线程 `T1` 和 `T2` 同时进入 `transfer` 方法



此时 `T1` 和 `T2` 都获取到了原链表的引用，并且 `e` 指向 `A`，`next` 指向 `B`。

#### 步骤 2：线程 `T1` 被挂起，线程 `T2` 完成扩容



线程 `T2` 完成扩容后，新链表的顺序变为 `B -> A`（因为采用头插法）。

#### 步骤 3：线程 `T1` 继续执行



- `T1` 中 `e` 仍然指向 `A`，`next` 指向 `B`。
- `T1` 将 `A` 插入到新数组的某个位置，此时 `A` 的 `next` 指向 `null`。
- 然后 `e` 指向 `B`，`next` 指向 `null`（因为 `T2` 已经改变了链表顺序）。
- `T1` 将 `B` 插入到新数组的某个位置，并且让 `B` 的 `next` 指向 `A`（头插法）。
- 接着 `T1` 要处理 `A`，由于 `B` 的 `next` 已经指向 `A`，`T1` 又将 `A` 插入到 `B` 的前面，此时就形成了循环链表 `A <-> B`。

### 4. 示例代码演示



以下是一个简单的示例代码，模拟多线程环境下 `HashMap` 扩容可能产生循环链表的情况：






```java
import java.util.HashMap;

public class HashMapCycleDemo {
    private static HashMap<Integer, Integer> map = new HashMap<>(2, 1.5f);

    public static void main(String[] args) {
        map.put(5, 55);

        // 线程 1
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

        // 线程 2
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







在这个示例中，多个线程同时对 `HashMap` 进行插入操作，可能会触发扩容，从而有一定概率产生循环链表。


## HashMap树化时除了remove还有什么时候链表化

### 1. `remove` 操作触发链表化



当在 `HashMap` 中移除节点时，如果移除节点后红黑树的节点数量小于等于 6 个，红黑树会退化为链表。这是因为红黑树的维护成本相对较高，当节点数量较少时，链表的操作效率更高，因此会进行树退化为链表的操作，以提高性能。



以下是 `HashMap` 中 `remove` 操作可能触发树退化的相关代码逻辑（在 `TreeNode` 类的 `removeTreeNode` 方法中）：














```java
if (root == null || root.right == null ||
    (rl = root.left) == null || rl.left == null) {
    tab[index] = first.untreeify(map);  // too small
    return;
}
```



在 `untreeify` 方法中会将红黑树转换为链表。

### 2. 扩容操作触发链表化



当 `HashMap` 进行扩容操作时，会对节点进行重新哈希和分配。在这个过程中，如果拆分后的红黑树节点数量小于等于 6 个，红黑树也会退化为链表。



`HashMap` 的扩容操作主要在 `resize` 方法中实现，以下是与树拆分和可能的树退化相关的代码片段：










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







这里的 `UNTREEIFY_THRESHOLD` 常量值为 6，当拆分后某一部分的节点数量 `lc` 或 `hc` 小于等于 6 时，会调用 `untreeify` 方法将红黑树转换为链表。



综上所述，在 `HashMap` 中，除了 `remove` 操作，扩容操作也可能会因为拆分后树节点数量减少到阈值以下而触发红黑树退化为链表的操作。


## 为什么hashmap的负载因子是0点75


在 Java 的 `HashMap` 中，负载因子（`load factor`）默认值为 0.75，这是基于时间和空间成本的综合考虑所做出的权衡选择，下面从几个方面详细分析原因：

### 1. 空间与时间的平衡

- 负载因子过小
    - 如果负载因子设置得较小，例如 0.5，那么在哈希表中元素数量达到数组长度的一半时，就会触发扩容操作。频繁的扩容操作会导致更多的内存被分配，增加了空间开销。同时，扩容操作需要重新计算元素的哈希值并将元素重新插入到新的数组中，这会带来额外的时间成本。
- 负载因子过大
    - 当负载因子设置得较大，如 1.0，意味着只有当数组完全填满时才会进行扩容。这样虽然减少了扩容的次数，节省了空间，但会导致哈希冲突的概率大大增加。因为哈希表中的元素越来越多，不同的键可能会映射到相同的数组位置，形成较长的链表（在 JDK 8 及以后，链表长度过长还可能会转化为红黑树），从而使查找、插入和删除操作的时间复杂度升高，影响性能。
- 负载因子为 0.75 的优势
    - 经过大量的实验和实践验证，0.75 这个值在空间和时间成本之间取得了较好的平衡。在这个负载因子下，哈希冲突的概率相对较低，同时也不会过于频繁地进行扩容操作，能够在保证一定性能的前提下，合理利用内存空间。

### 2. 符合泊松分布

- 在理想情况下，哈希表中的元素应该均匀分布在数组的各个位置。但实际上，由于哈希函数的局限性，元素的分布会存在一定的偏差，会出现哈希冲突。
- 当负载因子为 0.75 时，哈希表中链表长度的分布符合泊松分布。泊松分布是一种常见的概率分布，用于描述在一定时间或空间内随机事件发生的次数。在 `HashMap` 中，链表长度的分布遵循泊松分布意味着大部分链表的长度较短，这样可以保证查找、插入和删除操作的时间复杂度接近 *O*(1)。
- 例如，链表长度为 8 的概率非常低（约为 6×10?8），在 JDK 8 中，当链表长度达到 8 时，链表会转化为红黑树，以提高查找效率。而负载因子为 0.75 时，链表长度达到 8 的情况很少出现，减少了不必要的红黑树转换操作，进一步提升了性能。

### 3. 经验和实践的选择

- Java 的开发团队在设计 `HashMap` 时，经过了大量的性能测试和实际应用的验证，最终选择了 0.75 作为默认的负载因子。这个值在多种场景下都表现出了较好的性能和稳定性，被广泛认为是一个比较合适的经验值。

综上所述，`HashMap` 将负载因子设置为 0.75 是为了在空间使用效率和操作性能之间找到一个最佳的平衡点，同时也符合哈希冲突的概率分布规律，是经过实践验证的较为合理的选择。


## 使用HashMap的时候如何避免内存泄漏


在使用 `HashMap` 时，为避免内存泄漏，需要从多个方面进行考量，下面将详细阐述可能导致内存泄漏的原因及相应的解决办法。

### 1. 键对象使用弱引用或软引用

#### 原因



当 `HashMap` 中的键对象被其他地方强引用，且 `HashMap` 本身一直存在时，即使这些键对象在其他地方不再使用，由于 `HashMap` 持有它们的强引用，垃圾回收器无法回收这些键对象及其对应的值对象，从而造成内存泄漏。

#### 解决办法



可以使用 `WeakHashMap` 或自定义使用弱引用（`WeakReference`）、软引用（`SoftReference`）来包装键对象。`WeakHashMap` 中的键是弱引用，当键对象被垃圾回收时，对应的键值对会自动从 `WeakHashMap` 中移除。



**示例代码**：











```java
import java.util.WeakHashMap;

public class WeakHashMapExample {
    public static void main(String[] args) {
        WeakHashMap<Object, String> weakHashMap = new WeakHashMap<>();
        Object key = new Object();
        weakHashMap.put(key, "value");

        // 移除对键对象的强引用
        key = null;

        // 触发垃圾回收
        System.gc();

        try {
            // 等待垃圾回收完成
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 此时 weakHashMap 应该为空
        System.out.println(weakHashMap.size()); 
    }
}
```

### 2. 及时清理不再使用的键值对

#### 原因



如果 `HashMap` 持续添加键值对，而不清理不再使用的键值对，会导致 `HashMap` 不断占用内存，最终可能引发内存泄漏。

#### 解决办法



在业务逻辑中，当确定某些键值对不再使用时，及时调用 `remove` 方法将其从 `HashMap` 中移除。



**示例代码**：








```java
import java.util.HashMap;

public class HashMapCleanupExample {
    public static void main(String[] args) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("key1", "value1");
        hashMap.put("key2", "value2");

        // 假设 key1 对应的键值对不再使用
        hashMap.remove("key1");

        System.out.println(hashMap.size()); 
    }
}
```

### 3. 避免在键对象中使用可变对象

#### 原因



如果使用可变对象作为 `HashMap` 的键，当对象的属性发生改变时，其 `hashCode` 和 `equals` 方法的返回值可能会改变，这会导致在 `HashMap` 中无法正确定位该键值对，使得该键值对无法被正常访问和移除，从而造成内存泄漏。

#### 解决办法



尽量使用不可变对象（如 `String`、`Integer` 等）作为 `HashMap` 的键。如果必须使用可变对象，要确保在对象属性改变时，不会影响其 `hashCode` 和 `equals` 方法的返回值。

### 4. 避免静态 `HashMap` 持有大对象

#### 原因



静态变量的生命周期与应用程序的生命周期相同，如果静态的 `HashMap` 持有大对象，且这些对象在应用程序运行过程中不再使用，但由于静态 `HashMap` 的引用，它们无法被垃圾回收，会造成内存泄漏。

#### 解决办法



尽量避免使用静态的 `HashMap` 来存储大对象。如果确实需要使用静态 `HashMap`，在对象不再使用时，及时将其从 `HashMap` 中移除。

### 5. 注意迭代器使用

#### 原因



在使用迭代器遍历 `HashMap` 时，如果在迭代过程中使用 `HashMap` 的 `remove` 方法而不是迭代器的 `remove` 方法来移除元素，会抛出 `ConcurrentModificationException` 异常，并且可能导致内存泄漏。

#### 解决办法



在迭代过程中，如果需要移除元素，使用迭代器的 `remove` 方法。



**示例代码**：









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
                // 使用迭代器的 remove 方法移除元素
                iterator.remove(); 
            }
        }

        System.out.println(hashMap.size()); 
    }
}
```









