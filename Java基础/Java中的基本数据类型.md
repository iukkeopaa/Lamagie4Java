## ������������
### **1. ��������**

���ڴ洢����ֵ�������������������㡣

| �������� | ��С���ֽڣ� | ȡֵ��Χ                           | Ĭ��ֵ |
| :------- | :----------- | :--------------------------------- | :----- |
| `byte`   | 1            | -128 �� 127                        | 0      |
| `short`  | 2            | -32,768 �� 32,767                  | 0      |
| `int`    | 4            | -2?? �� 2??-1��Լ -21 �ڵ� 21 �ڣ� | 0      |
| `long`   | 8            | -2?? �� 2??-1                      | 0L     |

**ʾ��**��


```
byte b = 100;
short s = 10000;
int i = 100000;
long l = 10000000000L; // ע�⣺long ������Ҫ�����ֺ�� 'L'
```

------

### **2. ��������**

���ڴ洢��С�����ֵ���ֵ��

| �������� | ��С���ֽڣ� | ȡֵ��Χ                 | Ĭ��ֵ |
| :------- | :----------- | :----------------------- | :----- |
| `float`  | 4            | Լ ��3.4e-38 �� ��3.4e38   | 0.0f   |
| `double` | 8            | Լ ��1.7e-308 �� ��1.7e308 | 0.0    |

**ʾ��**��



```
float f = 3.14f; // ע�⣺float ������Ҫ�����ֺ�� 'f'
double d = 3.141592653589793;
```

------

### **3. �ַ�����**

���ڴ洢�����ַ���

| �������� | ��С���ֽڣ� | ȡֵ��Χ                    | Ĭ��ֵ             |
| :------- | :----------- | :-------------------------- | :----------------- |
| `char`   | 2            | 0 �� 65,535��Unicode �ַ��� | '\u0000'�����ַ��� |

**ʾ��**��



```
char c = 'A';
char unicodeChar = '\u0041'; // Unicode ��ʾ���ȼ��� 'A'
```

------

### **4. ��������**

���ڴ洢�߼�ֵ��ֻ���������ܵ�ֵ��`true` �� `false`��

| ��������  | ��С���ֽڣ�                 | ȡֵ��Χ          | Ĭ��ֵ  |
| :-------- | :--------------------------- | :---------------- | :------ |
| `boolean` | 1��ʵ�ʴ�Сȡ���� JVM ʵ�֣� | `true` �� `false` | `false` |

**ʾ��**��



```
boolean flag = true;
```

------

### **�ܽ��**

| ��������  | ����     | ��С���ֽڣ�    | ȡֵ��Χ                           | Ĭ��ֵ   |
| :-------- | :------- | :-------------- | :--------------------------------- | :------- |
| `byte`    | �������� | 1               | -128 �� 127                        | 0        |
| `short`   | �������� | 2               | -32,768 �� 32,767                  | 0        |
| `int`     | �������� | 4               | -2?? �� 2??-1��Լ -21 �ڵ� 21 �ڣ� | 0        |
| `long`    | �������� | 8               | -2?? �� 2??-1                      | 0L       |
| `float`   | �������� | 4               | Լ ��3.4e-38 �� ��3.4e38             | 0.0f     |
| `double`  | �������� | 8               | Լ ��1.7e-308 �� ��1.7e308           | 0.0      |
| `char`    | �ַ����� | 2               | 0 �� 65,535��Unicode �ַ���        | '\u0000' |
| `boolean` | �������� | 1��ȡ���� JVM�� | `true` �� `false`                  | `false`  |

------

### **ע������**

1. **Ĭ��ֵ**��
    - �����ж���ĳ�Ա������ʵ���������ᱻ����Ĭ��ֵ��
    - �ڷ����ж���ľֲ�����������ʽ��ʼ���������������
2. **����ת��**��
    - С��Χ���Ϳ����Զ�ת��Ϊ��Χ���ͣ��� `int` ת `long`����
    - ��Χ����ת��ΪС��Χ������Ҫǿ������ת������ `long` ת `int`����
3. **��������ʾ**��
    - `long` ������Ҫ�����ֺ�� `L` �� `l`��
    - `float` ������Ҫ�����ֺ�� `F` �� `f`��
    - `char` ���Ϳ����õ����ű�ʾ�ַ���Ҳ������ Unicode �����ʾ��

------

### **ʾ������**



```
public class PrimitiveTypesExample {
    public static void main(String[] args) {
        // ��������
        byte b = 100;
        short s = 10000;
        int i = 100000;
        long l = 10000000000L;

        // ��������
        float f = 3.14f;
        double d = 3.141592653589793;

        // �ַ�����
        char c = 'A';
        char unicodeChar = '\u0041';

        // ��������
        boolean flag = true;

        // ���
        System.out.println("byte: " + b);
        System.out.println("short: " + s);
        System.out.println("int: " + i);
        System.out.println("long: " + l);
        System.out.println("float: " + f);
        System.out.println("double: " + d);
        System.out.println("char: " + c);
        System.out.println("Unicode char: " + unicodeChar);
        System.out.println("boolean: " + flag);
    }
}
```

## ������������

1. �� ��class��
2. �ӿ� (interface)
3. ���� ([])

## �Զ�����ת����ǿ������ת��

### �Զ�����ת������ʽ����ת����

#### ����



�Զ�����ת����ָ��ĳЩ�ض�����£�Java ���������Զ���һ����������ת��Ϊ��һ���������ͣ����������Ա��ʽ�ؽ�������ת���������Զ�����ת��ͨ�������ڴ�һ��С��Χ����������ת��Ϊ��Χ����������ʱ����Ϊ��Χ���������Ϳ�������С��Χ�������͵�ֵ������������ݶ�ʧ��

#### ת������



�� Java �У������������͵��Զ�����ת���������£�
`byte` -> `short` -> `int` -> `long` -> `float` -> `double`
`char` -> `int`

#### ����ʾ��










```java
public class AutomaticTypeConversion {
    public static void main(String[] args) {
        int intValue = 10;
        // �Զ��� int ����ת��Ϊ double ����
        double doubleValue = intValue; 
        System.out.println("intValue: " + intValue);
        System.out.println("doubleValue: " + doubleValue);
    }
}
```



�����������У�`int` ���͵ı��� `intValue` ���Զ�ת��Ϊ `double` ���Ͳ���ֵ�� `doubleValue`��������Ϊ `double` ���͵ķ�Χ�� `int` ���ʹ󣬲���������ݶ�ʧ��

### ǿ������ת������ʽ����ת����

#### ����



ǿ������ת����ָ����Ҫ��һ����Χ����������ת��ΪС��Χ����������ʱ�����ڿ��ܻ�������ݶ�ʧ��Java �����������Զ�����ת������Ҫ����Ա��ʽ��ʹ������ת������� `(Ŀ������)` ������ת����

#### ����ʾ��













```java
public class ForcedTypeConversion {
    public static void main(String[] args) {
        double doubleValue = 10.5;
        // ǿ�ƽ� double ����ת��Ϊ int ����
        int intValue = (int) doubleValue; 
        System.out.println("doubleValue: " + doubleValue);
        System.out.println("intValue: " + intValue);
    }
}
```



�����������У�`double` ���͵ı��� `doubleValue` ��ǿ��ת��Ϊ `int` ���Ͳ���ֵ�� `intValue`����Ҫע����ǣ�ǿ������ת�����ܻᶪʧС�����֣����� `doubleValue` ��С������ `.5` ��������`intValue` ��ֵΪ 10��

### �ۺ�ʾ������











```java
public class TypeConversionExample {
    public static void main(String[] args) {
        byte byteValue = 10;
        // �Զ�����ת����byte -> int
        int intValue = byteValue; 
        System.out.println("byteValue: " + byteValue);
        System.out.println("intValue: " + intValue);

        int anotherIntValue = 128;
        // ǿ������ת����int -> byte
        byte anotherByteValue = (byte) anotherIntValue; 
        System.out.println("anotherIntValue: " + anotherIntValue);
        System.out.println("anotherByteValue: " + anotherByteValue);
    }
}
```







������ۺ�ʾ���У����Ƚ������Զ�����ת������ `byte` ���͵� `byteValue` ת��Ϊ `int` ���͵� `intValue`��Ȼ�������ǿ������ת������ `int` ���͵� `anotherIntValue` ת��Ϊ `byte` ���͵� `anotherByteValue`������ `byte` ���͵�ȡֵ��Χ�� -128 �� 127���� `anotherIntValue` ��ֵΪ 128�������� `byte` ���͵ķ�Χ��ǿ��ת����ᷢ�����������`anotherByteValue` ��ֵΪ -128��

**ע��:?��/?������������ϸ�ֵ������ײ������Ż����ڲ�?��ǿ������ת��; ��:++, --, +=, -=, ......**

### float f=3.4 ������

### Java �и�����������Ĭ������



�� Java �С����ʽ�ĸ���������Ĭ���� `double` ���͡�`double` ��˫���ȸ�������ռ�� 8 ���ֽڣ�64 λ������ `float` �ǵ����ȸ�������ռ�� 4 ���ֽڣ�32 λ����`double` �����ܱ�ʾ����ֵ��Χ�;��ȶ��� `float` ���͸���

### ����ԭ��



`float f = 3.4;` ���д���֮���Դ�������Ϊ `3.4` ��һ�� `double` ���͵ĳ����������� `f` �� `float` ���͡��� `double` ���͵� `float` ���͵�ת�����ڴӴ�Χ�������͵�С��Χ�������͵�ת�������ܻ���ɾ�����ʧ��Java �����������Զ��������ֿ��ܶ�ʧ���ݵ�ת�������ֱ��������ֵ���������ᱨ����ʾ�����ݵ����ͣ��� `double` ת���� `float` ���ܻ�����ʧ��

### ��ȷ��д��



�����ֳ�������ȷд�����Խ���������ֵ�� `float` ���͵ı�����

#### ��ʽһ���ڸ���������������� `f` �� `F`



�ڸ���������������� `f` �� `F` ������ȷָ���ó���Ϊ `float` ���͡�ʾ���������£�







```java
public class FloatAssignment {
    public static void main(String[] args) {
        float f = 3.4f; 
        System.out.println("f ��ֵ: " + f);
    }
}
```







�����������У�`3.4f` ��ʾ����һ�� `float` ���͵ĳ�������˿���ֱ�Ӹ�ֵ�� `float` ���͵ı��� `f`��

#### ��ʽ����ʹ��ǿ������ת��



ͨ��ǿ������ת������� `(float)` ���Խ� `double` ���͵ĳ���ת��Ϊ `float` ���͡�ʾ���������£�







```java
public class FloatAssignmentWithCasting {
    public static void main(String[] args) {
        float f = (float) 3.4; 
        System.out.println("f ��ֵ: " + f);
    }
}
```







������Ҫע����ǣ�ʹ��ǿ������ת�����ܻᵼ�¾�����ʧ����Ϊ `double` ���͵ľ��ȱ� `float` ���͸ߡ�



����������`float f = 3.4;` ����д������ȷ����Ҫʹ������������ȷ��ʽ֮һ���� `float` ���͵ı�����ֵ��
## short s1 = 1; s1 = s1 + 1�� ���� short s1 = 1; s1 += 1; ����


### 1. `short s1 = 1; s1 = s1 + 1;` ����

#### ����ʾ��







```java
public class ShortAdditionTest {
    public static void main(String[] args) {
        short s1 = 1;
        // �������д���ᱨ��
        // s1 = s1 + 1; 
    }
}
```





#### ����ԭ��



- �� Java �У���������������ʱ��`short`��`byte`��`char` ���͵����ݻ��Զ�����Ϊ `int` ���͡��� `s1 + 1` ������ʽ�У�`s1` �� `short` ���ͣ�`1` �� `int` ���ͣ�����ʱ `s1` �ᱻ�Զ�����Ϊ `int` ���ͣ��������ʽ�Ľ��Ҳ�� `int` ���͡�
- �� `s1` �� `short` ���ͣ���һ�� `int` ���͵�ֵ���� `short` ���͵ı��������ڴӴ�Χ�������͵�С��Χ�������͵ĸ�ֵ�����ܻᵼ�����ݶ�ʧ��Java ������������������ʽת�������Իᱨ����ʾ �������ݵ����ͣ��� `int` ת���� `short` ���ܻ�����ʧ����

### 2. `short s1 = 1; s1 += 1;` ����

#### ����ʾ��










```java
public class ShortCompoundAdditionTest {
    public static void main(String[] args) {
        short s1 = 1;
        s1 += 1; 
        System.out.println(s1);
    }
}
```

#### ��ȷԭ��



- `+=` �Ǹ��ϸ�ֵ����������ڸ��ϸ�ֵ�������Java ���Թ淶�涨�����Զ�����ǿ������ת����`s1 += 1` ʵ���ϵȼ��� `s1 = (short)(s1 + 1)`��
- Ҳ����˵����ʹ�� `+=` ��������ʱ�����Ƚ��мӷ����㣬Ȼ�󽫽��ǿ��ת��Ϊ `short` �����ٸ�ֵ�� `s1`�����Բ�����ֱ������



����������`short s1 = 1; s1 = s1 + 1;` �Ǵ���ģ��� `short s1 = 1; s1 += 1;` ����ȷ�ġ�
## ʹ�� final �ؼ�������һ������ʱ�������ò��ܱ䣬�������õĶ����ܱ�


### ���λ����������ͱ���



�� `final` ���λ����������ͱ���ʱ����ζ�Ÿñ���һ������ֵ����ֵ�Ͳ����ٱ��ı䡣

#### ʾ������



java











```java
public class FinalPrimitiveVariable {
    public static void main(String[] args) {
        final int num = 10;
        // ���´������������Ϊ num �� final ������ֵ���ܱ��޸�
        // num = 20; 
        System.out.println(num);
    }
}
```

#### ����



�����������У�`num` �� `final` ���Σ���ֵΪ `10` ���ٳ��Զ������¸�ֵ�ͻᵼ�±����������� `final` ���λ����������ͱ���ʱ��������ֵ���ɱ䡣

### ���������������ͱ���



�� `final` ���������������ͱ���ʱ����ζ�Ÿñ��������ò�����ָ���������󣬵�������ָ��Ķ���������ǿ��Ըı�ġ�

#### ʾ������



java











```java
import java.util.ArrayList;
import java.util.List;

public class FinalReferenceVariable {
    public static void main(String[] args) {
        final List<String> list = new ArrayList<>();
        list.add("apple");
        list.add("banana");
        System.out.println(list);

        // �����޸����ö��������
        list.remove(0);
        System.out.println(list);

        // ���´������������Ϊ list �� final ���������ò�����ָ����������
        // list = new ArrayList<>(); 
    }
}
```





#### ����



�����������У�`list` �� `final` ���Σ���ָ��һ�� `ArrayList` ���󡣿�������б�����ӻ��Ƴ�Ԫ�أ�˵��������ָ��Ķ���������ǿ��Ըı�ġ������������ `list` ������һ���µ� `ArrayList` ���󣬾ͻᵼ�±����������� `final` ���������������ͱ���ʱ�����������ò��ܱ䡣

### �ܽ�



- �� `final` ���λ����������ͱ���ʱ��������ֵ���ܸı䡣
- �� `final` ���������������ͱ���ʱ�����������ò�����ָ���������󣬵�������ָ��Ķ�������ݿ��Ըı䡣


## ����Ĵ�����ʲô����֮��

```java
if(username.equals(��zxx��){}
``` 
username ����Ϊ NULL,�ᱨ��ָ����󣻸�Ϊ"zxx".equals(username)

## д clone()����ʱ��ͨ������һ�д��룬��ʲô

clone ��ȱʡ��Ϊ��

```java
super.clone();
```

��Ϊ����Ҫ�Ѹ����еĳ�Ա���Ƶ�λ��Ȼ����Ǹ����Լ��ĳ�Ա��


## Anonymous Inner Class (�����ڲ���)�Ƿ���� extends(�̳�)�����࣬�Ƿ���� implements(ʵ��)interface(�ӿ�)?

���Լ̳��������ʵ�������ӿڡ������ǿ��ԣ����Ǳ���!

## super.getClass()��������

���������������Ƕ��٣�
```java
importjava.util.Date;
public class Test extends Date{
public static void main(String[] args) {
new Test().test();
}

public void test(){
System.out.println(super.getClass().getName());
}
}
```

//Test

�� test �����У�ֱ�ӵ��� getClass().getName()���������ص��� Test ����

���� getClass()�� Object ���ж������ final�����಻�ܸ��Ǹ÷��������ԣ���test �����е��� getClass().getName()��������ʵ�����ڵ��ôӸ���̳е� getClass()��������Ч�ڵ��� super.getClass().getName()���������ԣ�super.getClass().getName()�������ص�ҲӦ���� Test��

�����õ���������ƣ�Ӧ�������´��룺
```java
getClass().getSuperClass().getName();


```


## Java��doubleʹ����ʲôҪע���

### 1. ��������



- ��������ʾ�Ĳ���ȷ��

  ��

   - `double` ��ѭ IEEE 754 ��׼�����ڱ�ʾ˫���ȸ����������ڴ���ռ 64 λ�������޷���ȷ��ʾ����ʮ����С������ 0.1 �����ļ�С���� `double` ��ֻ�ܽ��Ʊ�ʾ��














```java
public class DoublePrecision {
    public static void main(String[] args) {
        double num = 0.1;
        System.out.println(num); 
        // ������������ 0.1000000000000000055511151231257827021181583404541015625
    }
}
```



- �Ƚ�����

  ��

   - ���ھ������⣬ֱ��ʹ�� `==` �Ƚ����� `double` ֵ�Ƿ���ȿ��ܻ�õ�����Ľ����Ӧͨ�������ֵ�ľ���ֵ������һ����С����ֵ���� `1e-9`���Ƚ����ж��Ƿ������ȡ�











```java
public class DoubleComparison {
    public static void main(String[] args) {
        double a = 0.1 + 0.2;
        double b = 0.3;
        if (Math.abs(a - b) < 1e-9) {
            System.out.println("a �� b �������");
        } else {
            System.out.println("a �� b �����");
        }
    }
}
```

### 2. ��Χ����



- ȡֵ��Χ

  ��

   - `double` ���������ʾ�����ֵ����Сֵ����ͨ�� `Double.MAX_VALUE` �� `Double.MIN_VALUE` ��ȡ�����������Χ�ᵼ����������硣












```java
public class DoubleRange {
    public static void main(String[] args) {
        double max = Double.MAX_VALUE;
        double result = max * 2;
        if (Double.isInfinite(result)) {
            System.out.println("�������������");
        }

        double min = Double.MIN_VALUE;
        double smallResult = min / 2;
        if (smallResult == 0) {
            System.out.println("��������");
        }
    }
}
```

### 3. ��������



- �����ٶ�

  ��

   - `double` ���͵�����ͨ�����������ͣ��� `int`��������Ϊ�����������漰�����ӵ�Ӳ���������ڶ�����Ҫ��ϸߵĳ����£������������ʹ�� `double` ���͵����㡣






```java
public class PerformanceComparison {
    public static void main(String[] args) {
        long startTime = System.nanoTime();
        double sumDouble = 0;
        for (int i = 0; i < 1000000; i++) {
            sumDouble += 1.0;
        }
        long endTime = System.nanoTime();
        System.out.println("double ������ͺ�ʱ: " + (endTime - startTime) + " ����");

        startTime = System.nanoTime();
        int sumInt = 0;
        for (int i = 0; i < 1000000; i++) {
            sumInt += 1;
        }
        endTime = System.nanoTime();
        System.out.println("����������ͺ�ʱ: " + (endTime - startTime) + " ����");
    }
}
```

### 4. �����������



- ��ʽ�����

  ��

   - ʹ�� `System.out.printf` �� `String.format` ���и�ʽ�����ʱ��Ҫע����ƾ��ȡ�













```java
public class DoubleOutputFormat {
    public static void main(String[] args) {
        double num = 3.1415926;
        System.out.printf("%.2f\n", num); 
        // ������Ϊ 3.14
    }
}
```



- ��������

  ��

   - ʹ�� `Scanner` ������û������ȡ `double` ֵʱ��Ҫȷ������ĸ�ʽ��ȷ��������׳� `InputMismatchException` �쳣��











```java
import java.util.Scanner;

public class DoubleInput {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try {
            System.out.print("������һ�� double ���͵���: ");
            double num = scanner.nextDouble();
            System.out.println("�����������: " + num);
        } catch (Exception e) {
            System.out.println("�����ʽ������������Ч�� double ������ֵ��");
        }
        scanner.close();
    }
}
```

### 5. ��ʼ������



- δ��ʼ���ı���

  ��

   - �� Java �У��ֲ�������ʹ��ǰ�����ʼ��������������󡣶���ĳ�Ա��������Ĭ�ϳ�ʼֵ��`double` ���͵�Ĭ�ϳ�ʼֵ�� 0.0��






```java
public class DoubleInitialization {
    public static void main(String[] args) {
        // ������󣺿�����δ��ʼ������ num
        // double num;
        // System.out.println(num);

        double num = 1.23;
        System.out.println(num);
    }
}
```





### 6. �Զ�װ��Ͳ�������



- ���ܿ���

  ��

   - `double` �ǻ����������ͣ�`Double` �����Ӧ�İ�װ�ࡣ�� Java �У����Զ�����װ�䣨��������ת��װ�ࣩ�Ͳ��䣨��װ��ת�������ͣ���������������һ�������ܿ��������������еĳ����£�Ӧ����ʹ�û����������͡�







```java
public class BoxingUnboxing {
    public static void main(String[] args) {
        double primitive = 1.23;
        Double wrapper = primitive; // �Զ�װ��
        double backToPrimitive = wrapper; // �Զ�����
    }
}
```
## Java�д����ĵײ�ԭ��

�� Java �У��������ͨ����ʹ�� `java.math` ���µ� `BigInteger` �� `BigDecimal` �࣬����ֱ�������ǵĵײ�ԭ��

### 1. `BigInteger` �ײ�ԭ��

#### ���ݴ洢



`BigInteger` ���ڱ�ʾ�����С�����������ڲ�ʹ��һ�� `int` �������洢�������Ķ����Ʋ�����ʽ��������˵�������ÿ��Ԫ����һ�� 32 λ�� `int` ���ͣ�ͨ����� `int` Ԫ������������Ա�ʾ�ǳ����������



���磬һ���ǳ��������������Ҫ��� 32 λ�Ŀ����洢������ĵ�һ��Ԫ�ش洢���������������Ч 32 λ���������ƣ���������һ��Ԫ�ش洢���������������Чλ��











```java
import java.math.BigInteger;

public class BigIntegerExample {
    public static void main(String[] args) {
        BigInteger bigNum = new BigInteger("12345678901234567890");
        // �ײ�ͨ������洢�ô�����
    }
}
```

#### ����ԭ��



- **�ӷ��ͼ���**��`BigInteger` �ļӷ��ͼ������㱾�����ǶԴ洢������������λ���в������������ֹ�����ӷ��ͼ�������Ҫ���ǽ�λ�ͽ�λ�������
- **�˷�**��ͨ������ Karatsuba �㷨����߼��� Toom - Cook �㷨�ȡ�Karatsuba �㷨��һ�ַ����㷨��������������ֳɽ�С�Ĳ��֣�ͨ���ݹ�ؼ�����Щ���ֵĳ˻���Ȼ��ϲ�������Ӷ����ٳ˷�����Ĵ�������߼���Ч�ʡ�
- **����**��`BigInteger` �ĳ���ʵ�ֽ�Ϊ���ӣ�ͨ����ʹ��ţ�� - ����ѷ���������㷨���ƽ��̵�ֵ��ͬʱ����Ҫ���������ļ��㡣

### 2. `BigDecimal` �ײ�ԭ��

#### ���ݴ洢



`BigDecimal` ���ڱ�ʾ���⾫�ȵ�ʮ������������Ҫ����������ɣ�һ�� `BigInteger` ���͵� `intVal` ���ڴ洢��ֵ���������֣��Լ�һ�� `int` ���͵� `scale` ���ڱ�ʾС�����λ�á�



���磬����ʮ������ `123.45`��`intVal` �洢���� `12345`��`scale` �洢���� `2`����ʾС���������ƶ� 2 λ��









```java
import java.math.BigDecimal;

public class BigDecimalExample {
    public static void main(String[] args) {
        BigDecimal bigDec = new BigDecimal("123.45");
        // �ײ�ͨ�� BigInteger �洢�������֣�int �洢С��λ��
    }
}
```





#### ����ԭ��



- **�ӷ��ͼ���**���ڽ��мӷ��ͼ�������ʱ��������Ҫ������ `BigDecimal` �����С����λ�ö��룬Ȼ����������ֽ�����Ӧ�ļӷ���������㣬�����ݽ������ `scale` ��ֵ��
- **�˷�**���˷�������ȼ������� `BigDecimal` ������������ֵĳ˻���Ȼ������ `scale` ֵ��ӵõ��µ� `scale` ֵ���������µ� `scale` �Խ�����е�����
- **����**������������Ը��ӣ���Ҫ���Ǿ��Ⱥ�����ģʽ���ڽ��г���ʱ�������ָ���ľ��Ⱥ�����ģʽ���̽��м���͵�������ȷ�������׼ȷ�ԡ�

### �ܽ�



`BigInteger` �� `BigDecimal` ͨ��������ض������ݽṹ���洢���������Ҳ�����һϵ�и�Ч���㷨��ʵ�ָ������㣬�Ӷ��ܹ����������С�;��ȵ���ֵ����������Щ�����漰�����ӵ��㷨�����ݴ���������Ի����������ͻ�ϵ͡�



