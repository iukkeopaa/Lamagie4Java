## дһ�����OOM������

```java
public class OOMExample {
    public static void main(String[] args) {
        List<byte[]> list = new ArrayList<>();

        try {
            while (true) {
                byte[] arr = new byte[1024 * 1024]; // ����1MB���ֽ�����
                list.add(arr);
            }
        } catch (OutOfMemoryError e) {
            System.out.println("OOM error occurred!");
            // OOM error occurred!
        }
    }
}
```

## ��Ԫ�ռ䷢��OOM

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

��������ʹ�� `Javassist` ����`��̬`�����ಢ���ص�Ԫ�ռ䡣��һ������ѭ���У����ǲ��ϴ����µ��࣬ÿ���඼����Ψһ�����ơ����Ų��ϴ����ಢ���ص�`Ԫ�ռ�`��`Ԫ�ռ�`�Ŀ��ÿռ佫�𽥺ľ�����Ԫ�ռ��޷������������Ԫ����ʱ��`JVM `���׳� `OutOfMemoryError`������ӡ"Metaspace OOM error occurred!"��

��ע�⣬�����д�ʾ������ʱ����Ҫ����ʵ��������� `JVM `��Ԫ�ռ��С���ƣ�ͨ������`-XX:MaxMetaspaceSize`���������Լ��㹻�������ڴ���Դ��