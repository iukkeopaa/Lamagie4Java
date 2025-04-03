### 1. �ɱ���



- **String**��`String` ���ǲ��ɱ�ģ�һ��������һ�� `String` �����������ݾͲ��ܱ��ı䡣����� `String` �������ƴ�ӡ��滻�Ȳ�����ʵ�����Ǵ�����һ���µ� `String` ����



java











```java
String str = "Hello";
str = str + " World"; 
// ����� str ʵ����ָ����һ���µ� String ����
```



- **StringBuffer**��`StringBuffer` �ǿɱ�ģ����ṩ��һϵ�з������� `append()`��`insert()`��`delete()` �ȣ����޸��ַ��������ݣ������ᴴ���µĶ���



java











```java
StringBuffer sb = new StringBuffer("Hello");
sb.append(" World"); 
// sb ָ��Ķ������ݱ��޸ģ�û�д����¶���
```



- **StringBuilder**��`StringBuilder` ͬ���ǿɱ�ģ��� `StringBuffer` ���ƣ�Ҳ�ṩ���޸��ַ������ݵķ���������ʱ���ᴴ���¶���













```java
StringBuilder sbuilder = new StringBuilder("Hello");
sbuilder.append(" World"); 
// sbuilder ָ��Ķ������ݱ��޸ģ�û�д����¶���
```

### 2. �̰߳�ȫ��



- **String**������ `String` �ǲ��ɱ�ģ����������̰߳�ȫ�ġ�����߳̿���ͬʱ����ͬһ�� `String` ���󣬲���������ݲ�һ�µ����⡣
- **StringBuffer**��`StringBuffer` ���̰߳�ȫ�ģ��������й����������� `synchronized` �ؼ������Σ�����ζ���ڶ��̻߳����£�ͬһʱ��ֻ����һ���̷߳��� `StringBuffer` �ķ������Ӷ���֤���̰߳�ȫ��











```java
public synchronized StringBuffer append(String str) {
    // ����ʵ��
    return this;
}
```



- **StringBuilder**��`StringBuilder` �Ƿ��̰߳�ȫ�ģ���û��ʹ�� `synchronized` �ؼ������η�������ˣ��ڶ��̻߳����£��������߳�ͬʱ���ʺ��޸� `StringBuilder` ���󣬿��ܻ�������ݲ�һ�µ����⡣

### 3. ����



- **String**������ÿ�ζ� `String` ��������޸Ķ��ᴴ���µĶ���Ƶ�����޸Ĳ����ᵼ�´������ڴ濪�����������գ����ܽϵ͡��ر�������Ҫ���д����ַ���ƴ�ӵĳ����£�������ʹ�� `String`��
- **StringBuffer**������ `StringBuffer` �ķ�����ͬ���ģ��ڶ��̻߳����£��߳�ͬ�������һ�������ܿ��������ڵ��̻߳����£�������Ҫ����ͬ����飬������Ҳ���� `StringBuilder`��
- **StringBuilder**��`StringBuilder` û���߳�ͬ���Ŀ������ڵ��̻߳����£��������ܱ� `StringBuffer` Ҫ�ߣ��ǽ��д����ַ���ƴ�ӵȲ�������ѡ��

### 4. ʹ�ó���



- **String**���������ַ������ݲ����������仯�ĳ���������洢�����ַ�������Ϊ�����Ĳ����ȡ�













```java
public class StringExample {
    public static final String GREETING = "Hello";

    public static void printMessage(String message) {
        System.out.println(message);
    }

    public static void main(String[] args) {
        printMessage(GREETING);
    }
}
```







- **StringBuffer**�������ڶ��̻߳�������Ҫ���ַ�������Ƶ���޸ĵĳ����������ڶ��̵߳���־��¼������ƴ�ӵȳ����п���ʹ�� `StringBuffer` ����֤�̰߳�ȫ��
- **StringBuilder**�������ڵ��̻߳�������Ҫ���ַ�������Ƶ���޸ĵĳ�����������ѭ���н����ַ���ƴ�ӵȲ�����ʹ�� `StringBuilder` ����������ܡ�














```java
public class StringBuilderExample {
    public static void main(String[] args) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            sb.append(i);
        }
        System.out.println(sb.toString());
    }
}
```







## String Ϊʲô�ǲ��ɱ��?

### 1. ��ȫ��



- **���̰߳�ȫ**���ڶ��̻߳����£����ɱ�������̰߳�ȫ�ġ���Ϊ����߳̿���ͬʱ����ͬһ�� `String` ���󣬶����õ������ݱ������޸ġ����磬��һ�����̵߳� Web Ӧ���У�����߳̿��ܻ�ͬʱ��ȡһ���洢�û���Ϣ�� `String` ������� `String` �ǿɱ�ģ��Ϳ��ܻ�������ݲ�һ�µ����⡣











```java
public class StringThreadSafetyExample {
    public static final String MESSAGE = "Welcome to the application";

    public static void main(String[] args) {
        // ����߳̿��԰�ȫ�ط��� MESSAGE
        Thread t1 = new Thread(() -> System.out.println(MESSAGE));
        Thread t2 = new Thread(() -> System.out.println(MESSAGE));
        t1.start();
        t2.start();
    }
}
```



- **��ȫ������Ϣ**��`String` �����ڴ洢��ȫ������Ϣ�������롢���ݿ������ַ����ȡ���� `String` �ǿɱ�ģ���ô��Щ��Ϣ���ܻ��ڲ�����䱻�޸ģ��Ӷ����°�ȫ©�������磬��һ����֤�û���¼�ķ����У�������� `String` ���������޸ģ����ܻᵼ����֤ʧ�ܻ�ȫ��Ϣй¶��

### 2. �����Ż�



- **�ַ���������**��Java �е��ַ�����������һ����Ҫ�������Ż����ơ����� `String` �ǲ��ɱ�ģ���ͬ���ݵ��ַ������Թ���ͬһ�����󣬴洢���ַ����������С��������Լ����ڴ��ʹ�ã�������ܡ����磺









```java
String str1 = "Hello";
String str2 = "Hello";
// str1 �� str2 ָ���ַ����������е�ͬһ������
System.out.println(str1 == str2); 
```



- **��ϣ�뻺��**��`String` ��Ỻ�����Ĺ�ϣ�룬��Ϊ `String` �ǲ��ɱ�ģ��������Ĺ�ϣ���ڶ��󴴽�ʱ�Ϳ��Ա����㲢����������������ʹ�� `String` ������Ϊ��ϣ���� `HashMap`��`HashSet`���ļ�ʱ��������߹�ϣ������ܣ������ظ������ϣ�롣








```java
public final class String
    implements java.io.Serializable, Comparable<String>, CharSequence {
    private int hash; // ����Ĺ�ϣ��

    @Override
    public int hashCode() {
        int h = hash;
        if (h == 0 && value.length > 0) {
            hash = h = isLatin1() ? StringLatin1.hashCode(value)
                                  : StringUTF16.hashCode(value);
        }
        return h;
    }
}
```

### 3. ��Ƽ����



- **��������һ����**�����ɱ�� `String` ʹ�� `String` ��ķ������ø��Ӽ򵥺�һ�¡���Ϊ `String` ��������ݲ���ı䣬���Է������õĽ���ǿ�Ԥ��ģ�������������á����磬`substring()`��`toUpperCase()` �ȷ������᷵��һ���µ� `String` ���󣬶������޸�ԭ����











```java
String str = "Hello";
String newStr = str.toUpperCase();
// str ��Ȼ�� "Hello"��newStr �� "HELLO"
System.out.println(str); 
System.out.println(newStr); 
```

### 4. ���ʵ�ֻ���



�� `String` ���Դ�����������ĺ�����һ�� `private final char[] value` �������洢�ַ������ַ����С�`private` ���η���֤���ⲿ�޷�ֱ�ӷ��ʸ����飬`final` ���η���֤�˸���������ò��ܱ��ı䡣ͬʱ��`String` ����û���ṩ�޸ĸ��������ݵķ������Ӷ���֤�� `String` ����Ĳ��ɱ��ԡ�










```java
public final class String
    implements java.io.Serializable, Comparable<String>, CharSequence {
    /** The value is used for character storage. */
    private final char value[];

    // ���췽��
    public String(String original) {
        this.value = original.value;
        this.hash = original.hash;
    }

    // ��������...
}
```
## String �������ɱ������漸��ԭ�� 

### **1. `String` �౻����Ϊ `final`**

- `String` �౻����Ϊ `final`������ζ�������ܱ��̳С�
- ��ֹ����ͨ���̳��޸� `String` ����Ϊ���ƻ��䲻�ɱ��ԡ�

------

### **2. �ַ����鱻����Ϊ `final`**

- `String` �ڲ�ʹ��һ�� `final` ���ε��ַ����飨`private final char value[]`�����洢�ַ������ݡ�
- ���� `final` ���Σ��ַ���������ò��ܱ��޸ģ�������ָ����һ�����顣
- �ַ����鱾����˽�еģ��ⲿ�޷�ֱ�ӷ��ʻ��޸ġ�

------

### **3. û���޸��ַ�����ķ���**

- `String` ��û���ṩ�κο����޸��ڲ��ַ�����ķ�����
- ���п����޸��ַ����ķ������� `substring()`��`replace()` �ȣ�ʵ���϶��Ƿ���һ���µ� `String` ���󣬶������޸�ԭ����

------

### **4. �ַ��������ص��Ż�**

- Java ʹ���ַ������������洢�ַ������������� `"hello"`����
- �������ַ�������������ͬ�������������ǻ�ָ�������е�ͬһ������
- ��� `String` �ǿɱ�ģ��޸�һ���ַ������ܻ�Ӱ���������ø��ַ����ı��������²���Ԥ�����Ϊ��

------

### **5. ��ȫ��**

- ���ɱ���ʹ�� `String` �����ڶ��̻߳��������̰߳�ȫ�ģ���������ͬ�����ơ�
- `String` ���������в��������ļ�����URL������ȣ������ɱ��Կ��Է�ֹ�����޸ġ�

------

### **6. ��ϣ�뻺��**

- `String` ���ڲ������˹�ϣ�루`private int hash`������Ϊ�ַ������ɱ䣬��ϣ��ֻ�����һ�Ρ�
- ����ַ����ɱ䣬��ϣ����ܻ�仯�������ڹ�ϣ���� `HashMap`���г������⡣

------

### **7. ʾ������**


```
String str = "hello";
str = str + " world"; // ������һ���µ� String ����
System.out.println(str); // ��� "hello world"
```

������Ĵ����У�

1. ��ʼʱ��`str` ָ���ַ����������е� `"hello"`��
2. ִ�� `str + " world"` ʱ���ᴴ��һ���µ� `String` ���� `"hello world"`����ԭ���� `"hello"` ���ᱻ�޸ġ�
3. `str` ����ָ���µĶ��� `"hello world"`��

------

### **8. �ܽ�**

`String` �Ĳ��ɱ�����ͨ�����»���ʵ�ֵģ�

1. `final` �࣬��ֹ�̳С�
2. `final` �ַ����飬��ֹ���ñ��޸ġ�
3. ���ṩ�޸��ַ�����ķ�����
4. �ַ��������ص��Ż���
5. ��ȫ�Կ��ǡ�
6. ��ϣ�뻺�档

��Щ���ƹ�ͬȷ���� `String` ����Ĳ��ɱ��ԣ��Ӷ�����˰�ȫ�ԡ��̰߳�ȫ�Ժ����ܡ�






## �ַ���ƴ���á�+�� ���� StringBuilder?

### ʹ�� ��+�� �����ַ���ƴ��

#### �ŵ�



- **�﷨���**��ʹ�� ��+�� �����ַ���ƴ������ֱ�ۡ���򵥵ķ�ʽ������ɶ��Ըߣ����������ַ�����ƴ�ӷǳ����㡣

**�ײ�ԭ�� �� ͨ�� StringBuilder ���� append() ����ʵ�ֵģ�ƴ�����֮����� toString() �õ�һ�� String ����**

java











```java
String str1 = "Hello";
String str2 = " World";
String result = str1 + str2;
System.out.println(result); 
```

#### ȱ��



- **���ܽϲ�**����ʹ�� ��+�� ���д����ַ���ƴ��ʱ��������϶����ʱ������Ϊ `String` ���ǲ��ɱ�ģ�ÿ��ʹ�� ��+�� ƴ���ַ���ʱ��ʵ���϶��ᴴ��һ���µ� `String` ������ᵼ��Ƶ�����ڴ������������գ����ܿ����ϴ�



java











```java
String result = "";
for (int i = 0; i < 1000; i++) {
    result = result + i; 
}
```



�����������У�ÿ��ѭ�����ᴴ��һ���µ� `String` ����ѭ�� 1000 �ξͻᴴ����������ʱ��������Ӱ�����ܡ�

### ʹ�� `StringBuilder` �����ַ���ƴ��

#### �ŵ�



- **���ܸ�**��`StringBuilder` �ǿɱ�ģ����ڲ�ά����һ���ַ����飬�ڽ����ַ���ƴ��ʱ����ֱ������������Ͻ��в��������ᴴ����������ʱ���󡣵���Ҫ���д����ַ���ƴ��ʱ��`StringBuilder` ��������������ʹ�� ��+�� ƴ�ӡ�



java











```java
StringBuilder sb = new StringBuilder();
for (int i = 0; i < 1000; i++) {
    sb.append(i); 
}
String result = sb.toString();
```



�����������У�ʹ�� `StringBuilder` �� `append` ��������ƴ�ӣ�ֻ�ᴴ��һ�� `StringBuilder` ��������յ� `String` ���󣬱����˴�����ʱ����Ĵ�������������ܡ�



- **����Ը�**��`StringBuilder` �ṩ�˷ḻ�ķ������� `append`��`insert`��`delete` �ȣ����Է���ض��ַ������и��ֲ�����

#### ȱ��



- **�﷨��Ը���**�������ʹ�� ��+�� ����ƴ�ӣ�ʹ�� `StringBuilder` ��Ҫ�������󡢵��÷�����������Ը���һЩ��

### ʹ�ý���



- **�����ַ���ƴ��**�����ֻ�ǽ����������ַ���ƴ�ӣ�����ƴ���������ַ�����ʹ�� ��+�� �ǱȽϺ��ʵģ���Ϊ�����﷨��࣬��������ܲ�������Ӱ�졣














```java
String name = "John";
String message = "Hello, " + name + "!";
System.out.println(message);
```



- **�����ַ���ƴ��**������Ҫ���д������ַ���ƴ�ӣ���������ѭ���н���ƴ��ʱ������ʹ�� `StringBuilder`��������������������ܣ������ڴ濪����













```java
StringBuilder sb = new StringBuilder();
for (int i = 0; i < 10000; i++) {
    sb.append("Item " + i + ", ");
}
String result = sb.toString();
```







- **���̻߳���**������ڶ��̻߳����½����ַ���ƴ�ӣ���Ҫ�����̰߳�ȫ���⡣`StringBuilder` �Ƿ��̰߳�ȫ�ģ���ʱӦ��ʹ�� `StringBuffer`�������̰߳�ȫ�ģ���Ȼ���ܱ� `StringBuilder` �Եͣ������Ա�֤�ڶ��̻߳����µ�����һ���ԡ�











```java
StringBuffer sb = new StringBuffer();
for (int i = 0; i < 1000; i++) {
    sb.append(i);
}
String result = sb.toString();
```

## String#equals() �� Object#equals() �к�����

### ��������



- **`Object#equals()`**��`Object` ���� Java ��������Ļ��࣬`Object#equals()` �� `Object` ���ж����һ���������䶨�����£�










```java
public boolean equals(Object obj) {
    return (this == obj);
}
```



- **`String#equals()`**��`String` ��̳��� `Object` �࣬����д�� `equals()` ������`String` ���� `equals()` �������ڱȽ������ַ����������Ƿ���ȡ�

### �Ƚ��߼�



- **`Object#equals()`**��`Object` ���� `equals()` ����Ĭ�ϵıȽ��߼���ʹ�� `==` ��������Ƚ���������������Ƿ���ȣ�Ҳ�����ж����������Ƿ�ָ��ͬһ���ڴ��ַ��ֻ�е���������ָ��ͬһ������ʱ���÷����Ż᷵�� `true`��ʾ�����£�












```java
Object obj1 = new Object();
Object obj2 = new Object();
Object obj3 = obj1;

System.out.println(obj1.equals(obj2)); 
System.out.println(obj1.equals(obj3)); 
```



���������У�`obj1` �� `obj2` �ǲ�ͬ�Ķ���ʵ�������ǵ��ڴ��ַ��ͬ������ `obj1.equals(obj2)` ���� `false`���� `obj3` ָ�� `obj1` ��ͬ���ڴ��ַ����� `obj1.equals(obj3)` ���� `true`��



- **`String#equals()`**��`String` ����д�� `equals()` �������ȼ�鴫��Ķ����Ƿ�Ϊ `String` ���ͣ�����ǣ����һ���Ƚ������ַ������ַ������Ƿ���ͬ�������� `String#equals()` �����ļ��߼���









```java
public boolean equals(Object anObject) {
    if (this == anObject) {
        return true;
    }
    if (anObject instanceof String) {
        String anotherString = (String)anObject;
        int n = value.length;
        if (n == anotherString.value.length) {
            char v1[] = value;
            char v2[] = anotherString.value;
            int i = 0;
            while (n-- != 0) {
                if (v1[i] != v2[i])
                    return false;
                i++;
            }
            return true;
        }
    }
    return false;
}
```



ʾ���������£�















```java
String str1 = new String("Hello");
String str2 = new String("Hello");
String str3 = str1;

System.out.println(str1.equals(str2)); 
System.out.println(str1.equals(str3)); 
```







����������У���Ȼ `str1` �� `str2` �ǲ�ͬ�� `String` ����ʵ�����ڴ��ַ��ͬ���������ǵ��ַ����ж��� `"Hello"`������ `str1.equals(str2)` ���� `true`��`str3` ָ�� `str1` ��ͬ�Ķ������ `str1.equals(str3)` Ҳ���� `true`��

### ��д���



- **`Object#equals()`**��`Object` ���е� `equals()` �������������ʵ�֣���û�п��Ƕ���ľ������ݣ�ֻ�Ǽ򵥱Ƚ����á��ںܶ�����£����Ĭ��ʵ�ֲ�������ҵ������������Ҫ�ھ������������д�÷�����
- **`String#equals()`**��`String` �������������ԣ���д�� `Object` ��� `equals()` ������ʹ�ø÷����������ڱȽ��ַ��������ݡ��������˶�̬�ԣ���ͬ������Ը������������� `equals()` ��������Ϊ��


## String#intern ������ʲô����?

### ���ø���



`String#intern` ��������Ҫ�����ǽ��ַ���������ӵ��ַ����������У������س������еĸ��ַ��������á�������������Ѿ���������ַ���������ͬ���ַ�������ֱ�ӷ��س��������ַ��������ã���������ڣ��򽫸��ַ�����ӵ��������У�Ȼ�󷵻س������и��ַ��������á�

### ԭ�����



�� Java �У��ַ�����������һ��������ڴ��������ڴ洢�ַ�����������ʹ��˫����ֱ�Ӵ����ַ���ʱ������ `String str = "hello";`������ַ����ᱻ�Զ������ַ����������С���ͨ�� `new` �ؼ��ִ������ַ������󣬻��ڶ��ڴ��з���ռ䣬Ĭ������²�������ַ��������ء�`intern` ���������ṩ��һ�ֽ����е��ַ���������뵽�ַ��������ص�;����

### ʾ������







```java
public class StringInternExample {
    public static void main(String[] args) {
        // ʹ��˫���Ŵ����ַ�����ֱ�ӽ��볣����
        String str1 = "hello";

        // ʹ�� new �ؼ��ִ����ַ������ڶ��з����ڴ�
        String str2 = new String("hello");

        // ���� intern �������� str2 ��Ӧ���ַ������볣����
        String str3 = str2.intern();

        // �Ƚ�����
        System.out.println(str1 == str2); 
        System.out.println(str1 == str3); 
    }
}
```



�����������У�`str1` ��ͨ��˫���Ŵ������ַ�������ֱ�Ӵ洢���ַ����������С�`str2` ��ͨ�� `new` �ؼ��ִ����ģ��洢�ڶ��ڴ��С����� `str2.intern()` �����ڳ��������Ѿ����� `"hello"` �ַ��������� `str3` �õ����ǳ������� `"hello"` �ַ��������ã���� `str1 == str3` ���Ϊ `true`��

### ʹ�ó���



- **��ʡ�ڴ�**������������Ҫ����ʹ����ͬ���ݵ��ַ���ʱ��ʹ�� `intern` ������������Щ�ַ������������е�ͬһ��ʵ�����Ӷ������ڴ濪�������磬�ڴ���������ı�����ʱ������кܶ��ظ����ַ��������� `intern` ����������Ч��ʡ�ڴ档














```java
import java.util.ArrayList;
import java.util.List;

public class MemorySavingExample {
    public static void main(String[] args) {
        List<String> stringList = new ArrayList<>();
        for (int i = 0; i < 100000; i++) {
            String str = new String("example").intern();
            stringList.add(str);
        }
    }
}
```







- **�ַ����Ƚ��Ż�**���ڽ����ַ����Ƚ�ʱ�����ʹ�� `==` �Ƚϳ������е��ַ������ã���ʹ�� `equals` �����Ƚ��ַ������ݵ�Ч��Ҫ�ߡ���Ϊ `==` �Ƚϵ��������Ƿ���ȣ��� `equals` ������Ҫ����ַ��Ƚ��ַ������ݡ�

### ע������



- **���ܿ���**��`intern` �����ĵ��ñ�����һ�������ܿ�������Ϊ����Ҫ�ڳ������в����Ƿ��Ѿ�������ͬ���ݵ��ַ���������������е��ַ��������ǳ��󣬲��Ҳ������ܻ��ñȽ�����
- **����������**���ַ��������ص����������޵ģ������������ `intern` ��������ͬ���ַ�����ӵ��������У����ܻᵼ�³�����������Ӷ������ڴ����⡣�� Java 7 ��ʼ���ַ��������شӷ������Ƶ��˶��У�һ���̶��ϻ�����������⣬����Ȼ��Ҫ����ʹ�á�

## Java�������ַ�����ӵĵײ�ԭ��

�� Java �У������ַ������ͨ��ʹ�� `+` ��������� `concat` ����������ֱ�������ǵĵײ�ʵ��ԭ��

### ʹ�� `+` ����������ַ������

#### �������Ż�

�� Java �У���ʹ�� `+` ����������ַ������ʱ�����������������Ż��������ӵ��ַ������ǳ������ڱ���ʱ����ȷ��ֵ���ַ����������������ڱ���׶ν����Ǻϲ�Ϊһ���ַ�����

java











```java
public class StringAdditionExample {
    public static void main(String[] args) {
        String str1 = "Hello";
        String str2 = "World";
        String result = str1 + str2;
        System.out.println(result);
    }
}
```

�����������룬�ڱ���ʱ��`javac` �������Ὣ `str1 + str2` ת��Ϊ `new StringBuilder().append(str1).append(str2).toString()`��Ҳ����˵�����������Զ�ʹ�� `StringBuilder` �������ַ�����ƴ�Ӳ�����

#### �����ڴ���

`StringBuilder` ��һ���ɱ���ַ����У����ڲ�ά����һ���ַ����顣������ `append` ����ʱ���Ὣ�µ��ַ���׷�ӵ��ַ������С�����ַ����������������`StringBuilder` ���Զ��������ݡ������� `toString` ����ʱ���ᴴ��һ���µ� `String` ���󣬽� `StringBuilder` �е��ַ����и��Ƶ��� `String` �����С�

### ʹ�� `concat` ���������ַ������

#### ��������

`concat` ������ `String` ���һ��ʵ�����������ڽ�ָ�����ַ������ӵ����ַ�����ĩβ���䷽��ǩ�����£�

java











```java
public String concat(String str)
```

#### �ײ�ʵ��ԭ��

`concat` �����ĵײ�ʵ����ͨ������һ���µ��ַ����飬��ԭ�ַ�����Ҫƴ�ӵ��ַ������ַ����θ��Ƶ��������У�Ȼ��ʹ����������鴴��һ���µ� `String` ���������� `concat` �����ļ�ʵ�֣�

java











```java
public String concat(String str) {
    int otherLen = str.length();
    if (otherLen == 0) {
        return this;
    }
    int len = value.length;
    char buf[] = Arrays.copyOf(value, len + otherLen);
    str.getChars(buf, len);
    return new String(buf, true);
}
```

���У�`value` �� `String` �����ڲ��洢�ַ����ַ����顣`Arrays.copyOf` �������ڴ���һ���µ��ַ����飬�䳤��Ϊԭ�ַ�����Ҫƴ���ַ����ĳ���֮�͡�Ȼ��ԭ�ַ������ַ����Ƶ��������У����Ž�Ҫƴ�ӵ��ַ������ַ����Ƶ�������ĺ��沿�֡����ʹ������µ��ַ����鴴��һ���µ� `String` ���󲢷��ء�

### ���ܱȽ�

- **`+` �����**���ڱ����ڻᱻ�Ż�Ϊ `StringBuilder` �Ĳ�������ѭ�������Ƶ��ʹ�� `+` �����ַ���ƴ�ӣ�����ÿ��ѭ�����ᴴ���µ� `StringBuilder` ���󣬿��ܻᵼ���������⡣
- **`concat` ����**��ÿ�ε��ö��ᴴ��һ���µ��ַ������ `String` ���󣬶����������ַ���ƴ�ӣ������пɣ����ڴ���ƴ�ӵĳ����£����ܲ��� `StringBuilder`��

��������������ҪƵ�������ַ���ƴ�ӵĳ����£�����ֱ��ʹ�� `StringBuilder` �� `StringBuffer`��`StringBuffer` ���̰߳�ȫ�ģ������Ե��� `StringBuilder`����������ܡ�



## String ��ôת�� Integer �ģ�ԭ��

### 1. ʹ�� `Integer.parseInt()` ����

#### ʾ������













```java
public class StringToIntegerExample {
    public static void main(String[] args) {
        String str = "123";
        int num = Integer.parseInt(str);
        System.out.println("ת���������: " + num);
    }
}
```

#### ԭ��



`Integer.parseInt()` �� `Integer` ���һ����̬���������������ǽ��ַ�����������Ϊ�з��ŵ�ʮ�����������÷�����ʵ��ԭ��������£�



- **����ַ����Ƿ�Ϊ�ջ� `null`**�����������ַ���Ϊ `null` ���߳���Ϊ 0�����׳� `NullPointerException` �� `NumberFormatException`��
- **�������**�����ȼ���ַ����ĵ�һ���ַ��Ƿ�Ϊ `+` �� `-`������ǣ����¼������Ϣ�����ӵڶ����ַ���ʼ������
- **�ַ���֤**�������ַ����е�ÿ���ַ�������Ƿ�Ϊ��Ч�������ַ������ַ��� ASCII ���� `'0'` �� `'9'` ֮�䣩����������������ַ������׳� `NumberFormatException`��
- **����ת��**����ÿ�������ַ�ת��Ϊ��Ӧ����ֵ�������������ַ����е�λ�ý��м�Ȩ��͡����磬�����ַ��� `"123"`���Ƚ��ַ� `'1'` ת��Ϊ��ֵ 1������ 100���ַ� `'2'` ת��Ϊ��ֵ 2������ 10���ַ� `'3'` ת��Ϊ��ֵ 3������ 1�������Щ�����ӵõ� 123��
- **��Χ���**����ת�������У��������Ƿ񳬳��� `int` ���͵�ȡֵ��Χ��-2147483648 �� 2147483647�������������Χ�����׳� `NumberFormatException`��

### 2. ʹ�� `Integer.valueOf()` ����

#### ʾ������












```java
public class StringToIntegerExample {
    public static void main(String[] args) {
        String str = "123";
        Integer integerObj = Integer.valueOf(str);
        System.out.println("ת����� Integer ����: " + integerObj);
    }
}
```

#### ԭ��



`Integer.valueOf()` Ҳ�� `Integer` ���һ����̬��������ʵ���������ڲ������� `Integer.parseInt()` ���������ַ�����������ת����Ȼ��ת����� `int` ֵ��װ��һ�� `Integer` ���󷵻ء��÷�����ʹ���˻�����ƣ�����ֵ�� -128 �� 127 ֮��� `Integer` ���󣬻�ӻ����л�ȡ����������ܺͽ�ʡ�ڴ档���磺













```java
Integer a = Integer.valueOf(100);
Integer b = Integer.valueOf(100);
System.out.println(a == b); // ��� true����Ϊ 100 �ڻ��淶Χ�ڣ�a �� b ����ͬһ������

Integer c = Integer.valueOf(200);
Integer d = Integer.valueOf(200);
System.out.println(c == d); // ��� false����Ϊ 200 ���ڻ��淶Χ�ڣ�c �� d �ǲ�ͬ�Ķ���
```





### 3. ʹ�� `new Integer(String s)` ���췽�������Ƽ��������ã�

#### ʾ������











```java
public class StringToIntegerExample {
    public static void main(String[] args) {
        String str = "123";
        Integer integerObj = new Integer(str);
        System.out.println("ת����� Integer ����: " + integerObj);
    }
}
```





#### ԭ��



`new Integer(String s)` ���췽��ͬ���ǵ��� `Integer.parseInt()` �������ַ���ת��Ϊ `int` ֵ��Ȼ�����װ�� `Integer` ���󡣲������� Java 9 ��ʼ��������췽���Ѿ������ã�����ʹ�� `Integer.valueOf()` �������棬��Ϊ `Integer.valueOf()` �����������û�����ƣ����ܸ��á�



�����������ڽ� `String` ת��Ϊ `Integer` ʱ��`Integer.parseInt()` �� `Integer.valueOf()` �ǳ��õķ��������ǵĺ��Ķ��ǻ��ڶ��ַ������ַ��Ľ�����ת�������һ������Ч�Լ��ͷ�Χ�жϡ�

## ������û�� length()�������? String ��û�� length()���������

����û�� length()����������� length �����ԡ�


String ���� length()���������

