### �������;



- Exception���쳣��
    - `Exception` ��ʾ��������ʱ���ܳ��ֵĿɻָ����쳣�������Щ�쳣ͨ�����ɳ�����߼������ⲿ���������߻������ص�����ġ�������Ա����ͨ����д����������ʹ�����Щ�쳣��ʹ�����ܹ����쳣����лָ�������ִ�С�
    - ���磬�ڶ�ȡ�ļ�ʱ������ļ������ڣ����׳� `FileNotFoundException`������һ�� `Exception` ���͵��쳣��������Ա���Բ�����쳣��������Ӧ�Ĵ�������ʾ�û�����ļ�·����
- Error������
    - `Error` ��ʾϵͳ���Ĵ������Դ�ľ����������⣬ͨ���ǳ����޷����ƺʹ���ġ���Щ����һ������ Java �������JVM����ײ�ϵͳ�������⵼�µģ������ڴ������`OutOfMemoryError`����ջ�����`StackOverflowError`���ȡ�
    - ������ `Error` ʱ������ͨ���޷��ָ���������Ҫ��ֹ���в�������Ӧ��ϵͳ��������Դ�ͷš�

### �̳й�ϵ



- Exception
    - `Exception` ���� `Throwable` ���ֱ�����࣬���ֿ��Է�Ϊ�ܼ���쳣��Checked Exception���ͷ��ܼ���쳣��Unchecked Exception��Ҳ��Ϊ����ʱ�쳣 `RuntimeException`�����ܼ���쳣�ڱ���ʱ����Ҫ������������׳��������ܼ���쳣����Ҫ��
    - ���磬`IOException` ���ܼ���쳣��`NullPointerException` ������ʱ�쳣�����ڷ��ܼ���쳣��
- Error
    - `Error` ��ͬ���� `Throwable` ���ֱ�����࣬�������˸���ϵͳ���Ĵ������ͣ��� `VirtualMachineError`��JVM ���󣩡�`AWTError`�����󴰿ڹ��߰����󣩵ȡ�

### ����ʽ



- Exception
    - �����ܼ���쳣��Java ��������ǿ��Ҫ�󿪷���Ա���д�������ʽ�����֣�һ��ʹ�� `try-catch` ���鲶���쳣�����д��������ڷ���ǩ����ʹ�� `throws` �ؼ��������׳��쳣�����쳣�Ĵ������ν��������ߡ�
    - ʾ���������£�






```java
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class ExceptionExample {
    public static void main(String[] args) {
        try {
            File file = new File("nonexistent.txt");
            FileInputStream fis = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            System.out.println("�ļ�δ�ҵ�: " + e.getMessage());
        }
    }
}
```



- ���ڷ��ܼ���쳣����Ȼ��������ǿ��Ҫ������������ԱҲ���Ը�����Ҫ���в���ʹ�������ǿ����Ľ�׳�ԡ�
- Error
    - ���� `Error` ͨ����ʾϵͳ�����������⣬����һ���޷�������Щ������˲����鿪����Ա����ʹ��� `Error` ���͵��쳣�������� `Error` ʱ��ͨ����ζ�ų�����Ҫ��ֹ���в�������Ӧ��ϵͳ���͵�����
    - ʾ�������в�������������







```java
public class ErrorExample {
    public static void main(String[] args) {
        try {
            // ���ܻᵼ�� StackOverflowError �Ĵ���
            recursiveMethod();
        } catch (StackOverflowError e) {
            // �����鲶�� Error ���͵��쳣
            System.out.println("ջ�������: " + e.getMessage());
        }
    }

    public static void recursiveMethod() {
        recursiveMethod();
    }
}
```

### ��������ʾ��



- Exception
    - **�ܼ���쳣**��`IOException`����������쳣����`SQLException`�����ݿ�����쳣���ȡ�
    - **���ܼ���쳣**��`NullPointerException`����ָ���쳣����`ArrayIndexOutOfBoundsException`������Խ���쳣����`ArithmeticException`�������쳣���������󣩵ȡ�
- Error
    - `OutOfMemoryError`���ڴ�������󣩡�`StackOverflowError`��ջ������󣩡�`NoClassDefFoundError`���ඨ��δ�ҵ����󣩵ȡ�


## Checked Exception �� Unchecked Exception ��ʲô����

### ������̳й�ϵ



- �ܼ���쳣��Checked Exception��
    - �ܼ���쳣��ָ��Щ�̳��� `Exception` �࣬�����̳��� `RuntimeException` ���쳣�������쳣��ʾ�ڳ���ִ�й����п��ܳ��ֵĿ�Ԥ�����쳣�������������ǿ��Ҫ�󿪷��߶���Щ�쳣���д���
    - ���磬`IOException`��`SQLException` �ȶ������ܼ���쳣��
- ���ܼ���쳣��Unchecked Exception��
    - ���ܼ���쳣��ָ�̳��� `RuntimeException` ����쳣��Ҳ����Ϊ����ʱ�쳣������ͨ����ʾ�����е��߼�������̴��󣬱���������ǿ��Ҫ��������쳣���д���
    - �� `NullPointerException`��`ArrayIndexOutOfBoundsException`��`ArithmeticException` �ȶ����ڷ��ܼ���쳣��

### ���������



- �ܼ���쳣��Checked Exception��
    - ���������ϸ����ܼ���쳣����һ�����������׳��ܼ���쳣ʱ��Ҫô�ڷ����ڲ�ʹ�� `try-catch` ��䲶�񲢴�����쳣��Ҫô�ڷ���ǩ����ʹ�� `throws` �ؼ��������׳����쳣����������д����������ᱨ��
    - ʾ���������£�





```java
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class CheckedExceptionExample {
    // �����׳� FileNotFoundException
    public static void readFile() throws FileNotFoundException {
        File file = new File("nonexistent.txt");
        FileInputStream fis = new FileInputStream(file);
    }

    public static void main(String[] args) {
        try {
            readFile();
        } catch (FileNotFoundException e) {
            System.out.println("�ļ�δ�ҵ�: " + e.getMessage());
        }
    }
}
```



- ���ܼ���쳣��Unchecked Exception��
    - ����������Է��ܼ���쳣����ǿ�Ƽ�顣��ʹһ�����������׳����ܼ���쳣��Ҳ����Ҫ�ڷ���ǩ�������������ø÷���ʱҲ����Ҫʹ�� `try-catch` �����в���
    - ʾ���������£�








```java
public class UncheckedExceptionExample {
    public static void divide(int a, int b) {
        // ���ܻ��׳� ArithmeticException
        int result = a / b; 
        System.out.println("���: " + result);
    }

    public static void main(String[] args) {
        divide(10, 0); 
    }
}
```

### �����ͼ��ʹ�ó���



- �ܼ���쳣��Checked Exception��
    - �ܼ���쳣�������ͼ���ÿ������ڱ�д����ʱ�Ϳ��ǵ����ܳ��ֵ��쳣�������������Ӧ�Ĵ�������ͨ�����ڱ�ʾ�����ⲿ���ص��µ��쳣�����ļ��������������ӡ����ݿ�����ȡ�
    - ���磬�ڽ����ļ���д����ʱ�����ܻ�����ļ������ڡ��ļ�Ȩ�޲�������⣬��Щ������ͨ���ܼ���쳣��������ȷ������Ľ�׳�ԡ�
- ���ܼ���쳣��Unchecked Exception��
    - ���ܼ���쳣��Ҫ���ڱ�ʾ�����е��߼�������̴������ָ�����á�����Խ��ȡ���Щ�쳣ͨ�������ڴ����д��������ģ�Ӧ���ڿ����Ͳ��Խ׶α����ֺ��޸���������������ʱ���в���ʹ���
    - ���磬��ʹ�ö���֮ǰû�н��п�ֵ��飬�Ϳ��ܻ��׳� `NullPointerException`��������Ӧ��ͨ�����ƴ����߼������������쳣�ķ�����

### �쳣���������



- �ܼ���쳣��Checked Exception��
    - �ܼ���쳣�Ĵ�������ͨ���ɵ����߳е�����һ�������׳��ܼ���쳣ʱ����������Ҫ��ȷ֪�����ܻ���ֵ��쳣�������������Ӧ�Ĵ����Ա�֤������������С�
- ���ܼ���쳣��Unchecked Exception��
    - ���ܼ���쳣�Ĵ���������Ҫ���ڷ�����ʵ���ߡ�������ʵ����Ӧ��ȷ���������ȷ�ԣ���������߼������µķ��ܼ���쳣����������˷��ܼ���쳣��ͨ����ζ�Ŵ������ȱ�ݣ���Ҫ�����޸ġ�


����`RuntimeException`�����������⣬������`Exception`�༰�����඼�����ܼ���쳣 ���������ܼ���쳣�У�IO ��ص��쳣��`ClassNotFoundException`��`SQLException`...��

**Unchecked Exception** �� **���ܼ���쳣** ��Java �����ڱ�������� �����Ǽ�ʹ�������ܼ���쳣Ҳ��������ͨ�����롣

`RuntimeException` �������඼ͳ��Ϊ���ܼ���쳣���������У�������������ճ������лᾭ���õ�����

- `NullPointerException`(��ָ�����)
- `IllegalArgumentException`(����������緽��������ʹ���)
- `NumberFormatException`���ַ���ת��Ϊ���ָ�ʽ����`IllegalArgumentException`�����ࣩ
- `ArrayIndexOutOfBoundsException`������Խ�����
- `ClassCastException`������ת������
- `ArithmeticException`����������
- `SecurityException` ����ȫ�������Ȩ�޲�����
- `UnsupportedOperationException`(��֧�ֵĲ�����������ظ�����ͬһ�û�)

## try-catch-finally ���ʹ�ã� ����1

### �����﷨



java











```java
try {
    // ���ܻ��׳��쳣�Ĵ����
} catch (ExceptionType1 e1) {
    // ���� ExceptionType1 �����쳣�Ĵ����
} catch (ExceptionType2 e2) {
    // ���� ExceptionType2 �����쳣�Ĵ����
} finally {
    // �����Ƿ����쳣����ִ�еĴ����
}
```





### ������˵��



- **`try` ��**���������ܻ��׳��쳣�Ĵ��롣�� `try` ���еĴ����׳��쳣ʱ�������������ת����Ӧ�� `catch` ������쳣����
- **`catch` ��**�����ڲ���ʹ����ض����͵��쳣�������ж�� `catch` �飬ÿ�� `catch` �鲶��һ���ض����͵��쳣���쳣������Ҫ���մ����ൽ�����˳�����У���Ϊ��������쳣��ǰ�������쳣�� `catch` �齫��Զ���ᱻִ�С�
- **`finally` ��**������ `try` �����Ƿ����쳣��`finally` ���еĴ��붼�ᱻִ�С�ͨ�������ͷ���Դ����ر��ļ������ݿ����ӵȡ�

### ʹ��ʾ��

#### ʾ�� 1���򵥵� `try-catch-finally` ʹ��












```java
public class TryCatchFinallyExample {
    public static void main(String[] args) {
        try {
            int result = 10 / 0; 
            System.out.println("���: " + result);
        } catch (ArithmeticException e) {
            System.out.println("���������쳣: " + e.getMessage());
        } finally {
            System.out.println("finally ��ִ��");
        }
    }
}
```



�����������У�`try` ���е� `10 / 0` ���׳� `ArithmeticException` �쳣���������ת����Ӧ�� `catch` ����д������ִ�� `finally` ���еĴ��롣

#### ʾ�� 2����� `catch` ���ʹ��








```java
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class MultipleCatchExample {
    public static void main(String[] args) {
        try {
            File file = new File("nonexistent.txt");
            FileInputStream fis = new FileInputStream(file);
            int result = 10 / 0; 
        } catch (FileNotFoundException e) {
            System.out.println("�ļ�δ�ҵ�: " + e.getMessage());
        } catch (ArithmeticException e) {
            System.out.println("���������쳣: " + e.getMessage());
        } finally {
            System.out.println("finally ��ִ��");
        }
    }
}
```



�����ʾ���У�`try` ���п��ܻ��׳� `FileNotFoundException` �� `ArithmeticException` �����쳣�����������׳����쳣������ת����Ӧ�� `catch` ����д������ִ�� `finally` �顣

#### ʾ�� 3��`finally` ��������Դ�ͷ�











```java
import java.io.FileInputStream;
import java.io.IOException;

public class ResourceReleaseExample {
    public static void main(String[] args) {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream("example.txt");
            // ��ȡ�ļ��Ĳ���
            int data;
            while ((data = fis.read()) != -1) {
                // �����ȡ������
            }
        } catch (IOException e) {
            System.out.println("���� IO �쳣: " + e.getMessage());
        } finally {
            if (fis != null) {
                try {
                    fis.close(); 
                } catch (IOException e) {
                    System.out.println("�ر��ļ���ʱ�����쳣: " + e.getMessage());
                }
            }
        }
    }
}
```



�����ʾ���У�`finally` ������ȷ�� `FileInputStream` ��Դ���رգ����� `try` �����Ƿ����쳣��

### ע������



- ��� `try` ����û���׳��쳣��`catch` �齫����ִ�У��� `finally` ����Ȼ��ִ�С�
- ��� `try` �����׳����쳣û�б��κ� `catch` �鲶�񣬳������ִ���� `finally` ����쳣�׸������ߡ�
- �� `finally` ���о�������ʹ�� `return` ��䣬��Ϊ����ܻᵼ�� `try` �� `catch` ���е� `return` ���ʧЧ��

## try-catch-finally ���ʹ�ã� ����2

### **1. �����﷨**




```
try {
    // �����׳��쳣�Ĵ���
} catch (ExceptionType1 e1) {
    // ���� ExceptionType1 ���͵��쳣
} catch (ExceptionType2 e2) {
    // ���� ExceptionType2 ���͵��쳣
} finally {
    // �����Ƿ����쳣������ִ�еĴ���
}
```

------

### **2. �����ֵ�����**

1. **`try` ��**��
    - ���������׳��쳣�Ĵ��롣
    - ��������쳣�������������ת����Ӧ�� `catch` �顣
2. **`catch` ��**��
    - ���񲢴����ض����͵��쳣��
    - �����ж�� `catch` �飬���ڴ���ͬ���͵��쳣��
    - �쳣����Ӧ�Ӿ��嵽�������磬�Ȳ��������쳣���ٲ������쳣����
3. **`finally` ��**��
    - �����Ƿ����쳣��`finally` ���еĴ��붼��ִ�С�
    - ͨ�������ͷ���Դ����ر��ļ������ݿ����ӵȣ���

------

### **3. ʾ������**



```
public class TryCatchFinallyExample {
    public static void main(String[] args) {
        try {
            int result = 10 / 0; // ������׳� ArithmeticException
            System.out.println("Result: " + result);
        } catch (ArithmeticException e) {
            System.out.println("Caught an exception: " + e.getMessage());
        } finally {
            System.out.println("This will always be executed.");
        }
    }
}
```

**���**��



```
Caught an exception: / by zero
This will always be executed.
```

------

### **4. ע������**

1. **`finally` ���ִ��**��
    - `finally` ���еĴ��������Ƿ����쳣����ִ�С�
    - ��ʹ�� `try` �� `catch` ����ʹ���� `return`��`finally` ��Ҳ���ڷ�������֮ǰִ�С�
2. **`try` ������� `catch` �� `finally` ��һ��ʹ��**��
    - `try` �鲻�ܵ���ʹ�ã�����������һ�� `catch` ��� `finally` �顣
3. **��� `catch` ���˳��**��
    - ����ж�� `catch` �飬Ӧ���Ȳ��������쳣���ͣ��ٲ��������쳣���͡�
    - ���磬�Ȳ��� `ArithmeticException`���ٲ��� `Exception`��
4. **`finally` ���еķ���ֵ**��
    - ��� `finally` ������ `return` ��䣬���Ḳ�� `try` �� `catch` ���е� `return` ��䡣

------

### **5. `try-with-resources`��Java 7+��**

������Ҫ�رյ���Դ�����ļ������ݿ����ӵȣ�������ʹ�� `try-with-resources` �﷨�������� `try` ��������Զ��ر���Դ��������ʽ���� `finally` �顣

**ʾ��**��

```
import java.io.*;

public class TryWithResourcesExample {
    public static void main(String[] args) {
        try (BufferedReader br = new BufferedReader(new FileReader("test.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.out.println("Caught an exception: " + e.getMessage());
        }
    }
}
```

**�ص�**��

- ��Դ���� `BufferedReader`���� `try` ��������Զ��رա�
- ��Դ�����ʵ�� `AutoCloseable` �ӿڡ�

------

### **6. �ܽ�**

| **����**             | **����**                         |
| :------------------- | :------------------------------- |
| `try`                | ���������׳��쳣�Ĵ���           |
| `catch`              | ���񲢴����ض����͵��쳣         |
| `finally`            | �����Ƿ����쳣������ִ�еĴ��� |
| `try-with-resources` | �Զ�������Դ��������ʽ�ر�       |

ͨ������ʹ�� `try-catch-finally`��������Ч�ش����쳣��ȷ����Դ����ȷ�ͷš�


## finally �еĴ���һ����ִ����

### ͨ�������`finally` ������ִ��



�� `try` ���еĴ�������ʱ�������Ƿ��׳��쳣��Ҳ�����쳣�Ƿ� `catch` �鲶��`finally` ���еĴ��붼��ִ�С���һ����ʹ�� `finally` ��ǳ��ʺ������ͷ���Դ����ر��ļ������ݿ����ӵȡ�



**ʾ������**��








```java
public class NormalFinallyExecution {
    public static void main(String[] args) {
        try {
            System.out.println("���� try ��");
            int result = 10 / 2;
            System.out.println("try ���м�����: " + result);
        } catch (ArithmeticException e) {
            System.out.println("�����쳣: " + e.getMessage());
        } finally {
            System.out.println("finally ��ִ��");
        }
    }
}
```



�����������У�`try` ����Ĵ�������ִ�У�û���׳��쳣��`catch` �鲻��ִ�У��� `finally` ���ִ�С�

### ���������`finally` �������ܲ�ִ��

#### 1. ������ `try` ��� `catch` ���е��� `System.exit()`



`System.exit()` ����������ֹ��ǰ�������е� Java �������һ�����ø÷����������������ֹ��`finally` ���еĴ��뽫����ִ�С�



**ʾ������**��



```java
public class SystemExitExample {
    public static void main(String[] args) {
        try {
            System.out.println("���� try ��");
            System.exit(0); 
        } catch (Exception e) {
            System.out.println("�����쳣: " + e.getMessage());
        } finally {
            System.out.println("finally ��ִ��"); 
        }
    }
}
```



����������У�`try` ���е����� `System.exit(0)`�������ֱ����ֹ��`finally` ���еĴ��벻�ᱻִ�С�

#### 2. �������ڵ��̱߳�ǿ����ֹ



�����ִ�� `try-catch-finally` �ṹ���̱߳�ǿ����ֹ������ͨ�� `Thread.stop()` �������÷����ѱ����ã���Ϊ�����ܵ������ݲ�һ�µ����⣩��`finally` ���еĴ���Ҳ����ִ�С�

#### 3. Java ���������



�� Java �������ΪĳЩ���صĴ������ڴ������ջ��������ش��󣩶�����ʱ��`finally` ���еĴ��뽫�޷�ִ�С����磬���޵ݹ���ÿ��ܻᵼ�� `StackOverflowError`������ʹ Java �����������















```java
public class JvmCrashExample {
    public static void recursiveMethod() {
        recursiveMethod();
    }

    public static void main(String[] args) {
        try {
            recursiveMethod();
        } catch (Exception e) {
            System.out.println("�����쳣: " + e.getMessage());
        } finally {
            System.out.println("finally ��ִ��"); 
        }
    }
}
```







�����������У��������޵ݹ���ã����׳� `StackOverflowError`��Java ��������ܻ������`finally` ������޷�ִ�С�


## �쳣�Ĵ���?ʽ

- �����쳣����?���崦��?�Ǽ����׸���?�� ��throw��throws�����׳��쳣��������ʽ��?�� throw,?�� throws������?��ϵͳ?�����쳣��
  throws ?��?���ϣ���?�������쳣�࣬���Ը������? throw ?��?���ڣ���?�������쳣����
- try catch �����쳣