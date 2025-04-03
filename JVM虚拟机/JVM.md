## 写一个造成OOM的例子

```java
public class OOMExample {
    public static void main(String[] args) {
        List<byte[]> list = new ArrayList<>();

        try {
            while (true) {
                byte[] arr = new byte[1024 * 1024]; // 分配1MB的字节数组
                list.add(arr);
            }
        } catch (OutOfMemoryError e) {
            System.out.println("OOM error occurred!");
            // OOM error occurred!
        }
    }
}
```

## 让元空间发生OOM

```java
import javassist.ClassPool;

public class MetaspaceOOMExample {
    public static void main(String[] args) {
        try {
            ClassPool classPool = ClassPool.getDefault();
            int count = 0;

            while (true) {
                String className = "Class" + count++;
                classPool.makeClass(className).toClass();
            }
        } catch (OutOfMemoryError e) {
            System.out.println("Metaspace OOM error occurred!");
        }
    }
}
```

上述代码使用 `Javassist` 库来`动态`创建类并加载到元空间。在一个无限循环中，我们不断创建新的类，每个类都具有唯一的名称。随着不断创建类并加载到`元空间`，`元空间`的可用空间将逐渐耗尽。当元空间无法分配更多的类的元数据时，`JVM `将抛出 `OutOfMemoryError`，并打印"Metaspace OOM error occurred!"。

请注意，在运行此示例代码时，需要根据实际情况调整 `JVM `的元空间大小限制（通过设置`-XX:MaxMetaspaceSize`参数），以及足够的物理内存资源。