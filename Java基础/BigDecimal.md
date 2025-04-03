### 1. `BigDecimal` 的存储

`BigDecimal` 是 Java 中用于精确表示和计算任意精度十进制数的类。它内部通过两个主要部分来存储数值：

- **整数部分**：使用 `int[]` 数组存储大整数，这允许 `BigDecimal` 表示非常大的整数部分。
- **标度（scale）**：表示小数点的位置。标度是一个 `int` 类型的值，用于指示小数点相对于整数部分的位置。

例如，对于数值 `123.45`，整数部分存储为 `12345`，标度为 `2`，表示小数点在从右向左数第 2 位之后。

以下是创建 `BigDecimal` 对象的常见方式：

java











```java
import java.math.BigDecimal;

public class BigDecimalStorageExample {
    public static void main(String[] args) {
        // 使用字符串创建 BigDecimal 对象
        BigDecimal num1 = new BigDecimal("123.45");
        // 使用整数创建 BigDecimal 对象
        BigDecimal num2 = new BigDecimal(123);
        // 使用双精度浮点数创建 BigDecimal 对象（不推荐）
        BigDecimal num3 = new BigDecimal(123.45); 

        System.out.println("num1: " + num1);
        System.out.println("num2: " + num2);
        System.out.println("num3: " + num3);
    }
}
```

需要注意的是，使用 `double` 类型的参数创建 `BigDecimal` 对象时，可能会出现精度问题，因为 `double` 本身存在精度损失，所以推荐使用 `String` 类型的参数来创建 `BigDecimal` 对象。

### 2. `BigDecimal` 的计算

`BigDecimal` 提供了一系列的方法用于进行基本的数学运算，如加法、减法、乘法和除法等。

java











```java
import java.math.BigDecimal;

public class BigDecimalCalculationExample {
    public static void main(String[] args) {
        BigDecimal num1 = new BigDecimal("10.5");
        BigDecimal num2 = new BigDecimal("2.5");

        // 加法
        BigDecimal sum = num1.add(num2);
        System.out.println("加法结果: " + sum);

        // 减法
        BigDecimal difference = num1.subtract(num2);
        System.out.println("减法结果: " + difference);

        // 乘法
        BigDecimal product = num1.multiply(num2);
        System.out.println("乘法结果: " + product);

        // 除法
        // 需要指定舍入模式，否则可能会抛出 ArithmeticException
        BigDecimal quotient = num1.divide(num2, 2, BigDecimal.ROUND_HALF_UP); 
        System.out.println("除法结果: " + quotient);
    }
}
```

在进行除法运算时，需要指定舍入模式和保留的小数位数，因为有些除法运算可能会产生无限循环小数，不指定这些参数会抛出 `ArithmeticException` 异常。

### 3. 注意事项

- **构造方法选择**：如前面所述，优先使用 `String` 类型的构造方法来创建 `BigDecimal` 对象，避免使用 `double` 类型的构造方法，以防止精度丢失。
- **舍入模式**：在进行除法、取整等操作时，必须明确指定舍入模式，否则可能会抛出异常。Java 提供了多种舍入模式，如 `ROUND_HALF_UP`（四舍五入）、`ROUND_DOWN`（直接截断）等。
- **性能开销**：由于 `BigDecimal` 需要处理任意精度的数值，其计算性能相对基本数据类型（如 `int`、`double`）会低一些。在对性能要求极高且对精度要求不高的场景下，应谨慎使用。
- **对象不可变性**：`BigDecimal` 对象是不可变的，每次进行计算都会返回一个新的 `BigDecimal` 对象，而不会修改原对象。因此，在进行多次计算时，需要注意对象的引用更新。

### 4. 为什么 `BigDecimal` 能保证不丢失精度

- **基于十进制表示**：`BigDecimal` 采用十进制表示法，而不是像 `float` 和 `double` 那样基于二进制表示。十进制表示可以精确地表示所有的十进制分数，避免了二进制表示在表示某些十进制小数时的精度丢失问题。
- **任意精度支持**：`BigDecimal` 可以表示任意大小和精度的十进制数，它通过内部的数组来存储大整数部分，并使用标度来表示小数点的位置，因此可以处理非常大或非常小的数值，且不会丢失精度。

综上所述，`BigDecimal` 是 Java 中处理高精度十进制计算的重要工具，但在使用时需要注意构造方法的选择、舍入模式的指定等问题。



