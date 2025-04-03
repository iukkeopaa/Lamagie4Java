## ̸̸���AQS����⣬AQS���ʵ�ֿ�������?
1. AQS��һ��JAVA�߳�ͬ���Ŀ�ܡ���JDK�кܶ������ߵĺ���ʵ�ֿ��
2. ��AQS�У�ά����һ���ź���state��һ���߳���ɵ�˫��������С����У�����̶߳��У������������߳�
�Ŷӵģ���state������һ�����̵ƣ����������߳��Ŷӻ��߷��еġ��ڲ�ͬ�ĳ����£��в��õ����塣

3. �ڿ���������������£�state��������ʾ�����Ĵ�����0��ʶ������ÿ��һ������state�ͼ�1���ͷ���state�ͼ�1��
## ˵�¶�AQS�����

AQS(AbstractQueuedSynchronizer)��Java��������е�һ����Ҫ���������һ�������࣬�ṩ���߳�ͬ��
�ĵײ�ʵ�ֻ��ơ�AQS��������ʵ���̵߳�ͬ���ͻ�����������ṩ��������Ҫ�������ƣ��ֱ����������͹���
����

������Ҳ��Ϊ��ռ�����ڶ���߳̾���ͬһ������Դʱ��ͬһʱ��ֻ����һ���̷߳��ʸù�����Դ��������߳���
ֻ��һ���̻߳������Դ����AOS�У���������ͨ�����õ�ͬ��״̬��ʵ�ֵģ���ͬ��״̬Ϊ0ʱ����ʾ����δ��
��ȡ��;��ͬ��״̬����0ʱ����ʾ���Ѿ�����ȡĿ��ռ��;��ͬ��״̬С��0ʱ����ʾ���Ѿ�����ȡ���Ǵ��ڵ�
��״̬��

�������������߳�ͬʱ�������Դ��������ͬһʱ��ֻ��һ���߳̿��Ի�ȡ������ӵ��Ȩ�������߳���Ҫ�ȴ���
�߳��ͷ�������AQS�У���������ʵ�������������ƣ�Ҳ��ͨ�����õ�ͬ��״̬��ʵ�ֵģ�

AQSͨ��һ�����õ�FIFO(�Ƚ��ȳ�)�ȴ�������ʵ���̵߳��ŶӺ͵��ȡ����߳���Ҫ��ȡ����Դʱ���������
G
���������̻߳�ȡ������̻߳ᱻ���뵽�ȴ������еȴ����������ͷ�ʱ���ȴ������еĵ�һ���̻߳�������Դ
������ִ�У�

��ʵ��AQSʱ����Ҫ�̳���AQS�ಢʵ������󷽷������бȽ���Ҫ�ķ�������:tryAcquire(��tryRelease0)��
��������ʵ�����Ļ�ȡ���ͷ�;acquire(0)��release()����������ʵ�������ͻ��Ѳ���;isHeldExclusively()����
�����ж��Ƿ�����������

��֮��AOS��Java��������е���Ҫ���֮һ�����ṩ���߳�ͬ���ĵײ�ʵ�ֻ��ƣ���ʹ��AOSʱ����Ҫ���ݾ�
���Ӧ�ó���ѡ����ʵ���������ʵ���̵߳�ͬ���ͻ��������

## AQS֧�ֵ�����ͬ����ʽ

1����ռʽ

2������ʽ

��������ʹ����ʵ�ֲ�ͬ���͵�ͬ���������ռʽ�� ReentrantLock������ʽ��Semaphore��CountDownLatch�����ʽ���� ReentrantReadWriteLock����֮��AQS Ϊʹ���ṩ�˵ײ�֧�ţ������װʵ�֣�ʹ���߿������ɷ��ӡ�

## AQS�����ģʽ

### 1. AQS �õ����ģʽ



AQS��AbstractQueuedSynchronizer����Ҫʹ����ģ�巽��ģʽ��

#### ԭ��



ģ�巽��ģʽ������һ���㷨�ĹǼܣ���һЩ�����ʵ���ӳٵ������С��� AQS �У�`AbstractQueuedSynchronizer` ���ṩ��һ�������ʵ������ͬ��������������һϵ�еķ��������̣�����һЩ�����ǳ���ģ���Ҫ����ȥʵ�־�����߼���

#### ʾ��



���磬`ReentrantLock` �ǻ��� AQS ʵ�ֵģ�`ReentrantLock` ���ڲ��� `Sync` �̳��� `AbstractQueuedSynchronizer`����ʵ���� `tryAcquire` �� `tryRelease` �ȷ�������Щ������ AQS �Ļ�ȡ���ͷ���Դ��ģ�������б����á�









```java
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Lock;

// �Զ�����
class MyLock implements Lock {
    private final Sync sync = new Sync();

    private static class Sync extends AbstractQueuedSynchronizer {
        @Override
        protected boolean tryAcquire(int arg) {
            if (compareAndSetState(0, 1)) {
                setExclusiveOwnerThread(Thread.currentThread());
                return true;
            }
            return false;
        }

        @Override
        protected boolean tryRelease(int arg) {
            if (getState() == 0) {
                throw new IllegalMonitorStateException();
            }
            setExclusiveOwnerThread(null);
            setState(0);
            return true;
        }
    }

    @Override
    public void lock() {
        sync.acquire(1);
    }

    @Override
    public void unlock() {
        sync.release(1);
    }

    // ��������ʡ��
}
```

### 2. AQS ��ȡ��������Ӧ����ϵͳ�� PV ԭ��



AQS ��ȡ�������Ĺ��̿�����Ȳ���ϵͳ�е� PV ԭ�

#### P ԭ�������Դ��



�� AQS �У���һ���̳߳��Ի�ȡ������������ʱ������� `acquire` ������������������� P ԭ�P ԭ��Ὣ�ź�����ֵ�� 1������ź�����ֵС�� 0�����߳̽�������״̬���� AQS �У���� `tryAcquire` ��������ʧ�ܣ��̻߳ᱻ���뵽�ȴ�������������ֱ�������ͷš�

#### V ԭ��ͷ���Դ��



��һ���߳��ͷ���ʱ������� `release` �������������� V ԭ�V ԭ��Ὣ�ź�����ֵ�� 1������ź�����ֵС�ڵ��� 0������һ���ȴ����̡߳��� AQS �У�`tryRelease` �����ͷ���������еȴ����̣߳��ỽ�Ѷ����е�ͷ�ڵ��̡߳�

### 3. AQS �������ı�ʾ��volatile �� state��



AQS ��ʹ�� `volatile` ���ε� `state` ��������ʾ��������״̬��

#### ԭ��



`volatile` �ؼ��ֱ�֤�� `state` �����ڶ��߳�֮��Ŀɼ��ԣ���һ���̶߳� `state` ���޸Ļ������������߳̿�����`state` ��ֵ���Ա�ʾ���Ĳ�ͬ״̬�����磬���ڶ�ռ����`state` Ϊ 0 ��ʾ��δ�����У�`state` Ϊ 1 ��ʾ���ѱ����С�

#### ʾ��









```java
public abstract class AbstractQueuedSynchronizer
    extends AbstractOwnableSynchronizer
    implements java.io.Serializable {
    private volatile int state;

    protected final int getState() {
        return state;
    }

    protected final void setState(int newState) {
        state = newState;
    }

    protected final boolean compareAndSetState(int expect, int update) {
        return unsafe.compareAndSwapInt(this, stateOffset, expect, update);
    }
}
```

## AQS���ѽڵ㣬Ϊʲô�Ӻ���ǰ��

�� Java �ĳ������ͬ������Abstract Queued Synchronizer����� AQS���У����ѽڵ�ʱ�Ӻ���ǰ�����ȴ����У���Ҫ�ǻ�����Ӳ����������Լ����Ⲣ������ȷ���Ŀ��ǣ�����Ϊ����ϸ������

### ��Ӳ�������



AQS �ĵȴ�������һ��˫������ṹ���½ڵ����ʱ���õ���β�巨���ڶ��̻߳����£�����߳̿���ͬʱ���Խ��½ڵ���뵽����β������Ӳ�����һ�����޸��½ڵ�� `prev` ָ��ָ��ǰβ�ڵ㣬Ȼ��ʹ�� CAS��Compare-And-Swap���������½ڵ�����Ϊ�µ�β�ڵ�Ĺ��̡�������̲���ԭ�ӵģ����ܻ�������������












```plaintext
// ���赱ǰ�������Ϊ�� head <-> node1 <-> node2 <-> tail
// �߳� A ׼���� node3 ���
node3.prev = tail; // ��ʱ node3 �� prev ָ�� tail

// ���߳� A ִ�� CAS ������ node3 ����Ϊ�µ�β�ڵ�֮ǰ
// �߳� B Ҳ׼���� node4 ���
node4.prev = tail; // ��ʱ node4 �� prev Ҳָ�� tail
```



������ô�ǰ��������ķ�ʽ���ѽڵ㣬��һ���½ڵ����ʱ������ `prev` ָ���Ѿ����ú��ˣ�������û�гɹ���Ϊ�µ�β�ڵ㣨�� `tail.next` ��û��ָ����½ڵ㣩����ô��ǰ����������ܻ���©�����δ��ȫ��ӵĽڵ㡣���Ӻ���ǰ���������ڽڵ�� `prev` ָ�������ʱ�������úõģ�ֻҪͨ�� `prev` ָ��Ϳ���׼ȷ���ҵ������е����нڵ㡣

### ���Ⲣ������



�Ӻ���ǰ�������Ա��Ⲣ���޸Ĵ��������⡣�ڶ��̻߳����£��ڵ����Ӻͳ��Ӳ����ǲ������еġ������ǰ����������ڱ��������п��ܻ�����ĳ���ڵ�� `next` ָ�뱻�޸ģ�����ýڵ㱻�Ƴ����У������±����жϻ���ִ��󡣶��Ӻ���ǰ������ÿ���ڵ�� `prev` ָ��һ�����úþͲ����ٱ��޸ģ����ǽڵ㱻�Ƴ����У����Ƴ������ᱣ֤ `prev` ָ���һ���ԣ�����˿��Ը���ȫ�ر����������С�

### ����ʾ����˵��



������ AQS �� `unparkSuccessor` �����ļ򻯴��룬�÷������ڻ��Ѻ�̽ڵ㣺











```java
private void unparkSuccessor(Node node) {
    // ���Խ���ǰ�ڵ��״̬����Ϊ 0
    int ws = node.waitStatus;
    if (ws < 0)
        compareAndSetWaitStatus(node, ws, 0);

    // �Ӻ���ǰ����Ч�ĺ�̽ڵ�
    Node s = node.next;
    if (s == null || s.waitStatus > 0) {
        s = null;
        // ��β�ڵ㿪ʼ��ǰ����
        for (Node t = tail; t != null && t != node; t = t.prev)
            if (t.waitStatus <= 0)
                s = t;
    }
    if (s != null)
        LockSupport.unpark(s.thread);
}
```







�����������У��� `node` �ĺ�̽ڵ� `s` Ϊ�ջ���״̬��Чʱ�����β�ڵ� `tail` ��ʼ��ǰ�������ҵ�һ��״̬��Ч�Ľڵ㲢��������



����������AQS ���ѽڵ�ʱ�Ӻ���ǰ����Ϊ����Ӧ��Ӳ��������ԣ�������©��δ��ȫ��ӵĽڵ㣬ͬʱҲ���Ա��Ⲣ���޸Ĵ��������⣬��֤�����İ�ȫ�Ժ�׼ȷ�ԡ�




