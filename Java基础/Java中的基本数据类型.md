## 基本数据类型
### **1. 整数类型**

用于存储整数值，包括正数、负数和零。

| 数据类型 | 大小（字节） | 取值范围                           | 默认值 |
| :------- | :----------- | :--------------------------------- | :----- |
| `byte`   | 1            | -128 到 127                        | 0      |
| `short`  | 2            | -32,768 到 32,767                  | 0      |
| `int`    | 4            | -2?? 到 2??-1（约 -21 亿到 21 亿） | 0      |
| `long`   | 8            | -2?? 到 2??-1                      | 0L     |

**示例**：


```
byte b = 100;
short s = 10000;
int i = 100000;
long l = 10000000000L; // 注意：long 类型需要在数字后加 'L'
```

------

### **2. 浮点类型**

用于存储带小数部分的数值。

| 数据类型 | 大小（字节） | 取值范围                 | 默认值 |
| :------- | :----------- | :----------------------- | :----- |
| `float`  | 4            | 约 ±3.4e-38 到 ±3.4e38   | 0.0f   |
| `double` | 8            | 约 ±1.7e-308 到 ±1.7e308 | 0.0    |

**示例**：



```
float f = 3.14f; // 注意：float 类型需要在数字后加 'f'
double d = 3.141592653589793;
```

------

### **3. 字符类型**

用于存储单个字符。

| 数据类型 | 大小（字节） | 取值范围                    | 默认值             |
| :------- | :----------- | :-------------------------- | :----------------- |
| `char`   | 2            | 0 到 65,535（Unicode 字符） | '\u0000'（空字符） |

**示例**：



```
char c = 'A';
char unicodeChar = '\u0041'; // Unicode 表示，等价于 'A'
```

------

### **4. 布尔类型**

用于存储逻辑值，只有两个可能的值：`true` 或 `false`。

| 数据类型  | 大小（字节）                 | 取值范围          | 默认值  |
| :-------- | :--------------------------- | :---------------- | :------ |
| `boolean` | 1（实际大小取决于 JVM 实现） | `true` 或 `false` | `false` |

**示例**：



```
boolean flag = true;
```

------

### **总结表**

| 数据类型  | 分类     | 大小（字节）    | 取值范围                           | 默认值   |
| :-------- | :------- | :-------------- | :--------------------------------- | :------- |
| `byte`    | 整数类型 | 1               | -128 到 127                        | 0        |
| `short`   | 整数类型 | 2               | -32,768 到 32,767                  | 0        |
| `int`     | 整数类型 | 4               | -2?? 到 2??-1（约 -21 亿到 21 亿） | 0        |
| `long`    | 整数类型 | 8               | -2?? 到 2??-1                      | 0L       |
| `float`   | 浮点类型 | 4               | 约 ±3.4e-38 到 ±3.4e38             | 0.0f     |
| `double`  | 浮点类型 | 8               | 约 ±1.7e-308 到 ±1.7e308           | 0.0      |
| `char`    | 字符类型 | 2               | 0 到 65,535（Unicode 字符）        | '\u0000' |
| `boolean` | 布尔类型 | 1（取决于 JVM） | `true` 或 `false`                  | `false`  |

------

### **注意事项**

1. **默认值**：
    - 在类中定义的成员变量（实例变量）会被赋予默认值。
    - 在方法中定义的局部变量必须显式初始化，否则会编译错误。
2. **类型转换**：
    - 小范围类型可以自动转换为大范围类型（如 `int` 转 `long`）。
    - 大范围类型转换为小范围类型需要强制类型转换（如 `long` 转 `int`）。
3. **字面量表示**：
    - `long` 类型需要在数字后加 `L` 或 `l`。
    - `float` 类型需要在数字后加 `F` 或 `f`。
    - `char` 类型可以用单引号表示字符，也可以用 Unicode 编码表示。

------

### **示例代码**



```
public class PrimitiveTypesExample {
    public static void main(String[] args) {
        // 整数类型
        byte b = 100;
        short s = 10000;
        int i = 100000;
        long l = 10000000000L;

        // 浮点类型
        float f = 3.14f;
        double d = 3.141592653589793;

        // 字符类型
        char c = 'A';
        char unicodeChar = '\u0041';

        // 布尔类型
        boolean flag = true;

        // 输出
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

## 引用数据类型

1. 类 （class）
2. 接口 (interface)
3. 数组 ([])

## 自动类型转换、强制类型转换

### 自动类型转换（隐式类型转换）

#### 概念



自动类型转换是指在某些特定情况下，Java 编译器会自动将一种数据类型转换为另一种数据类型，而无需程序员显式地进行类型转换操作。自动类型转换通常发生在从一个小范围的数据类型转换为大范围的数据类型时，因为大范围的数据类型可以容纳小范围数据类型的值，不会造成数据丢失。

#### 转换规则



在 Java 中，基本数据类型的自动类型转换规则如下：
`byte` -> `short` -> `int` -> `long` -> `float` -> `double`
`char` -> `int`

#### 代码示例










```java
public class AutomaticTypeConversion {
    public static void main(String[] args) {
        int intValue = 10;
        // 自动将 int 类型转换为 double 类型
        double doubleValue = intValue; 
        System.out.println("intValue: " + intValue);
        System.out.println("doubleValue: " + doubleValue);
    }
}
```



在上述代码中，`int` 类型的变量 `intValue` 被自动转换为 `double` 类型并赋值给 `doubleValue`，这是因为 `double` 类型的范围比 `int` 类型大，不会造成数据丢失。

### 强制类型转换（显式类型转换）

#### 概念



强制类型转换是指当需要将一个大范围的数据类型转换为小范围的数据类型时，由于可能会造成数据丢失，Java 编译器不会自动进行转换，需要程序员显式地使用类型转换运算符 `(目标类型)` 来进行转换。

#### 代码示例













```java
public class ForcedTypeConversion {
    public static void main(String[] args) {
        double doubleValue = 10.5;
        // 强制将 double 类型转换为 int 类型
        int intValue = (int) doubleValue; 
        System.out.println("doubleValue: " + doubleValue);
        System.out.println("intValue: " + intValue);
    }
}
```



在上述代码中，`double` 类型的变量 `doubleValue` 被强制转换为 `int` 类型并赋值给 `intValue`。需要注意的是，强制类型转换可能会丢失小数部分，这里 `doubleValue` 的小数部分 `.5` 被丢弃，`intValue` 的值为 10。

### 综合示例分析











```java
public class TypeConversionExample {
    public static void main(String[] args) {
        byte byteValue = 10;
        // 自动类型转换：byte -> int
        int intValue = byteValue; 
        System.out.println("byteValue: " + byteValue);
        System.out.println("intValue: " + intValue);

        int anotherIntValue = 128;
        // 强制类型转换：int -> byte
        byte anotherByteValue = (byte) anotherIntValue; 
        System.out.println("anotherIntValue: " + anotherIntValue);
        System.out.println("anotherByteValue: " + anotherByteValue);
    }
}
```







在这个综合示例中，首先进行了自动类型转换，将 `byte` 类型的 `byteValue` 转换为 `int` 类型的 `intValue`。然后进行了强制类型转换，将 `int` 类型的 `anotherIntValue` 转换为 `byte` 类型的 `anotherByteValue`。由于 `byte` 类型的取值范围是 -128 到 127，而 `anotherIntValue` 的值为 128，超出了 `byte` 类型的范围，强制转换后会发生数据溢出，`anotherByteValue` 的值为 -128。

**注意:?增/?减运算符、复合赋值运算符底层做了优化，内部?动强制类型转换; 如:++, --, +=, -=, ......**

### float f=3.4 ，对吗？

### Java 中浮点数常量的默认类型



在 Java 里，小数形式的浮点数常量默认是 `double` 类型。`double` 是双精度浮点数，占用 8 个字节（64 位）；而 `float` 是单精度浮点数，占用 4 个字节（32 位）。`double` 类型能表示的数值范围和精度都比 `float` 类型更大。

### 错误原因



`float f = 3.4;` 这行代码之所以错误，是因为 `3.4` 是一个 `double` 类型的常量，而变量 `f` 是 `float` 类型。从 `double` 类型到 `float` 类型的转换属于从大范围数据类型到小范围数据类型的转换，可能会造成精度损失，Java 编译器不会自动进行这种可能丢失数据的转换。如果直接这样赋值，编译器会报错，提示不兼容的类型：从 `double` 转换到 `float` 可能会有损失。

### 正确的写法



有两种常见的正确写法可以将浮点数赋值给 `float` 类型的变量。

#### 方式一：在浮点数常量后面添加 `f` 或 `F`



在浮点数常量后面添加 `f` 或 `F` 可以明确指定该常量为 `float` 类型。示例代码如下：







```java
public class FloatAssignment {
    public static void main(String[] args) {
        float f = 3.4f; 
        System.out.println("f 的值: " + f);
    }
}
```







在上述代码中，`3.4f` 表示这是一个 `float` 类型的常量，因此可以直接赋值给 `float` 类型的变量 `f`。

#### 方式二：使用强制类型转换



通过强制类型转换运算符 `(float)` 可以将 `double` 类型的常量转换为 `float` 类型。示例代码如下：







```java
public class FloatAssignmentWithCasting {
    public static void main(String[] args) {
        float f = (float) 3.4; 
        System.out.println("f 的值: " + f);
    }
}
```







不过需要注意的是，使用强制类型转换可能会导致精度损失，因为 `double` 类型的精度比 `float` 类型高。



综上所述，`float f = 3.4;` 这种写法不正确，需要使用上述两种正确方式之一来给 `float` 类型的变量赋值。
## short s1 = 1; s1 = s1 + 1； 对吗？ short s1 = 1; s1 += 1; 对吗？


### 1. `short s1 = 1; s1 = s1 + 1;` 分析

#### 代码示例







```java
public class ShortAdditionTest {
    public static void main(String[] args) {
        short s1 = 1;
        // 下面这行代码会报错
        // s1 = s1 + 1; 
    }
}
```





#### 错误原因



- 在 Java 中，当进行算术运算时，`short`、`byte`、`char` 类型的数据会自动提升为 `int` 类型。在 `s1 + 1` 这个表达式中，`s1` 是 `short` 类型，`1` 是 `int` 类型，运算时 `s1` 会被自动提升为 `int` 类型，整个表达式的结果也是 `int` 类型。
- 而 `s1` 是 `short` 类型，将一个 `int` 类型的值赋给 `short` 类型的变量，属于从大范围数据类型到小范围数据类型的赋值，可能会导致数据丢失，Java 编译器不允许这种隐式转换，所以会报错，提示 “不兼容的类型：从 `int` 转换到 `short` 可能会有损失”。

### 2. `short s1 = 1; s1 += 1;` 分析

#### 代码示例










```java
public class ShortCompoundAdditionTest {
    public static void main(String[] args) {
        short s1 = 1;
        s1 += 1; 
        System.out.println(s1);
    }
}
```

#### 正确原因



- `+=` 是复合赋值运算符。对于复合赋值运算符，Java 语言规范规定它会自动进行强制类型转换。`s1 += 1` 实际上等价于 `s1 = (short)(s1 + 1)`。
- 也就是说，在使用 `+=` 进行运算时，会先进行加法运算，然后将结果强制转换为 `short` 类型再赋值给 `s1`，所以不会出现编译错误。



综上所述，`short s1 = 1; s1 = s1 + 1;` 是错误的，而 `short s1 = 1; s1 += 1;` 是正确的。
## 使用 final 关键字修饰一个变量时，是引用不能变，还是引用的对象不能变


### 修饰基本数据类型变量



当 `final` 修饰基本数据类型变量时，意味着该变量一旦被赋值，其值就不能再被改变。

#### 示例代码



java











```java
public class FinalPrimitiveVariable {
    public static void main(String[] args) {
        final int num = 10;
        // 以下代码会编译错误，因为 num 是 final 变量，值不能被修改
        // num = 20; 
        System.out.println(num);
    }
}
```

#### 解释



在上述代码中，`num` 被 `final` 修饰，赋值为 `10` 后，再尝试对其重新赋值就会导致编译错误，这表明 `final` 修饰基本数据类型变量时，变量的值不可变。

### 修饰引用数据类型变量



当 `final` 修饰引用数据类型变量时，意味着该变量的引用不能再指向其他对象，但引用所指向的对象的内容是可以改变的。

#### 示例代码



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

        // 可以修改引用对象的内容
        list.remove(0);
        System.out.println(list);

        // 以下代码会编译错误，因为 list 是 final 变量，引用不能再指向其他对象
        // list = new ArrayList<>(); 
    }
}
```





#### 解释



在上述代码中，`list` 被 `final` 修饰，它指向一个 `ArrayList` 对象。可以向该列表中添加或移除元素，说明引用所指向的对象的内容是可以改变的。但如果尝试让 `list` 引用另一个新的 `ArrayList` 对象，就会导致编译错误，这表明 `final` 修饰引用数据类型变量时，变量的引用不能变。

### 总结



- 当 `final` 修饰基本数据类型变量时，变量的值不能改变。
- 当 `final` 修饰引用数据类型变量时，变量的引用不能再指向其他对象，但引用所指向的对象的内容可以改变。


## 下面的代码有什么不妥之处

```java
if(username.equals(“zxx”){}
``` 
username 可能为 NULL,会报空指针错误；改为"zxx".equals(username)

## 写 clone()方法时，通常都有一行代码，是什么

clone 有缺省行为，

```java
super.clone();
```

因为首先要把父类中的成员复制到位，然后才是复制自己的成员。


## Anonymous Inner Class (匿名内部类)是否可以 extends(继承)其它类，是否可以 implements(实现)interface(接口)?

可以继承其他类或实现其他接口。不仅是可以，而是必须!

## super.getClass()方法调用

下面程序的输出结果是多少？
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

在 test 方法中，直接调用 getClass().getName()方法，返回的是 Test 类名

由于 getClass()在 Object 类中定义成了 final，子类不能覆盖该方法，所以，在test 方法中调用 getClass().getName()方法，其实就是在调用从父类继承的 getClass()方法，等效于调用 super.getClass().getName()方法，所以，super.getClass().getName()方法返回的也应该是 Test。

如果想得到父类的名称，应该用如下代码：
```java
getClass().getSuperClass().getName();


```


## Java中double使用有什么要注意的

### 1. 精度问题



- 浮点数表示的不精确性

  ：

   - `double` 遵循 IEEE 754 标准，用于表示双精度浮点数，在内存中占 64 位。但它无法精确表示所有十进制小数，像 0.1 这样的简单小数在 `double` 中只能近似表示。














```java
public class DoublePrecision {
    public static void main(String[] args) {
        double num = 0.1;
        System.out.println(num); 
        // 输出结果可能是 0.1000000000000000055511151231257827021181583404541015625
    }
}
```



- 比较问题

  ：

   - 由于精度问题，直接使用 `==` 比较两个 `double` 值是否相等可能会得到错误的结果。应通过计算差值的绝对值，并与一个极小的阈值（如 `1e-9`）比较来判断是否近似相等。











```java
public class DoubleComparison {
    public static void main(String[] args) {
        double a = 0.1 + 0.2;
        double b = 0.3;
        if (Math.abs(a - b) < 1e-9) {
            System.out.println("a 和 b 近似相等");
        } else {
            System.out.println("a 和 b 不相等");
        }
    }
}
```

### 2. 范围问题



- 取值范围

  ：

   - `double` 类型有其表示的最大值和最小值，可通过 `Double.MAX_VALUE` 和 `Double.MIN_VALUE` 获取。超出这个范围会导致溢出或下溢。












```java
public class DoubleRange {
    public static void main(String[] args) {
        double max = Double.MAX_VALUE;
        double result = max * 2;
        if (Double.isInfinite(result)) {
            System.out.println("发生正无穷溢出");
        }

        double min = Double.MIN_VALUE;
        double smallResult = min / 2;
        if (smallResult == 0) {
            System.out.println("发生下溢");
        }
    }
}
```

### 3. 性能问题



- 运算速度

  ：

   - `double` 类型的运算通常比整数类型（如 `int`）慢，因为浮点数运算涉及更复杂的硬件操作。在对性能要求较高的场景下，尽量避免过多使用 `double` 类型的运算。






```java
public class PerformanceComparison {
    public static void main(String[] args) {
        long startTime = System.nanoTime();
        double sumDouble = 0;
        for (int i = 0; i < 1000000; i++) {
            sumDouble += 1.0;
        }
        long endTime = System.nanoTime();
        System.out.println("double 类型求和耗时: " + (endTime - startTime) + " 纳秒");

        startTime = System.nanoTime();
        int sumInt = 0;
        for (int i = 0; i < 1000000; i++) {
            sumInt += 1;
        }
        endTime = System.nanoTime();
        System.out.println("整数类型求和耗时: " + (endTime - startTime) + " 纳秒");
    }
}
```

### 4. 输入输出问题



- 格式化输出

  ：

   - 使用 `System.out.printf` 或 `String.format` 进行格式化输出时，要注意控制精度。













```java
public class DoubleOutputFormat {
    public static void main(String[] args) {
        double num = 3.1415926;
        System.out.printf("%.2f\n", num); 
        // 输出结果为 3.14
    }
}
```



- 输入问题

  ：

   - 使用 `Scanner` 等类从用户输入读取 `double` 值时，要确保输入的格式正确，否则会抛出 `InputMismatchException` 异常。











```java
import java.util.Scanner;

public class DoubleInput {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try {
            System.out.print("请输入一个 double 类型的数: ");
            double num = scanner.nextDouble();
            System.out.println("你输入的数是: " + num);
        } catch (Exception e) {
            System.out.println("输入格式错误，请输入有效的 double 类型数值。");
        }
        scanner.close();
    }
}
```

### 5. 初始化问题



- 未初始化的变量

  ：

   - 在 Java 中，局部变量在使用前必须初始化，否则会编译错误。而类的成员变量会有默认初始值，`double` 类型的默认初始值是 0.0。






```java
public class DoubleInitialization {
    public static void main(String[] args) {
        // 编译错误：可能尚未初始化变量 num
        // double num;
        // System.out.println(num);

        double num = 1.23;
        System.out.println(num);
    }
}
```





### 6. 自动装箱和拆箱问题



- 性能开销

  ：

   - `double` 是基本数据类型，`Double` 是其对应的包装类。在 Java 中，会自动进行装箱（基本类型转包装类）和拆箱（包装类转基本类型）操作，但这会带来一定的性能开销。在性能敏感的场景下，应尽量使用基本数据类型。







```java
public class BoxingUnboxing {
    public static void main(String[] args) {
        double primitive = 1.23;
        Double wrapper = primitive; // 自动装箱
        double backToPrimitive = wrapper; // 自动拆箱
    }
}
```
## Java中大数的底层原理

在 Java 中，处理大数通常会使用 `java.math` 包下的 `BigInteger` 和 `BigDecimal` 类，下面分别介绍它们的底层原理。

### 1. `BigInteger` 底层原理

#### 数据存储



`BigInteger` 用于表示任意大小的整数，它内部使用一个 `int` 数组来存储大整数的二进制补码形式。具体来说，数组的每个元素是一个 32 位的 `int` 类型，通过多个 `int` 元素组合起来可以表示非常大的整数。



例如，一个非常大的整数可能需要多个 32 位的块来存储，数组的第一个元素存储的是整数的最低有效 32 位，依次类推，数组的最后一个元素存储的是整数的最高有效位。











```java
import java.math.BigInteger;

public class BigIntegerExample {
    public static void main(String[] args) {
        BigInteger bigNum = new BigInteger("12345678901234567890");
        // 底层通过数组存储该大整数
    }
}
```

#### 运算原理



- **加法和减法**：`BigInteger` 的加法和减法运算本质上是对存储整数的数组逐位进行操作，类似于手工计算加法和减法，需要考虑进位和借位的情况。
- **乘法**：通常采用 Karatsuba 算法或更高级的 Toom - Cook 算法等。Karatsuba 算法是一种分治算法，它将大整数拆分成较小的部分，通过递归地计算这些部分的乘积，然后合并结果，从而减少乘法运算的次数，提高计算效率。
- **除法**：`BigInteger` 的除法实现较为复杂，通常会使用牛顿 - 拉夫逊迭代法等算法来逼近商的值，同时还需要处理余数的计算。

### 2. `BigDecimal` 底层原理

#### 数据存储



`BigDecimal` 用于表示任意精度的十进制数，它主要由两部分组成：一个 `BigInteger` 类型的 `intVal` 用于存储数值的整数部分，以及一个 `int` 类型的 `scale` 用于表示小数点的位置。



例如，对于十进制数 `123.45`，`intVal` 存储的是 `12345`，`scale` 存储的是 `2`，表示小数点向左移动 2 位。









```java
import java.math.BigDecimal;

public class BigDecimalExample {
    public static void main(String[] args) {
        BigDecimal bigDec = new BigDecimal("123.45");
        // 底层通过 BigInteger 存储整数部分，int 存储小数位数
    }
}
```





#### 运算原理



- **加法和减法**：在进行加法和减法运算时，首先需要将两个 `BigDecimal` 对象的小数点位置对齐，然后对整数部分进行相应的加法或减法运算，最后根据结果调整 `scale` 的值。
- **乘法**：乘法运算会先计算两个 `BigDecimal` 对象的整数部分的乘积，然后将两个 `scale` 值相加得到新的 `scale` 值，最后根据新的 `scale` 对结果进行调整。
- **除法**：除法运算相对复杂，需要考虑精度和舍入模式。在进行除法时，会根据指定的精度和舍入模式对商进行计算和调整，以确保结果的准确性。

### 总结



`BigInteger` 和 `BigDecimal` 通过数组和特定的数据结构来存储大数，并且采用了一系列高效的算法来实现各种运算，从而能够处理任意大小和精度的数值。但由于这些运算涉及到复杂的算法和数据处理，性能相对基本数据类型会较低。



