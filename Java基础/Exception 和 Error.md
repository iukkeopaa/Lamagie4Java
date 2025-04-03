### 概念和用途



- Exception（异常）
    - `Exception` 表示程序运行时可能出现的可恢复的异常情况。这些异常通常是由程序的逻辑错误、外部输入错误或者环境因素等引起的。开发人员可以通过编写代码来捕获和处理这些异常，使程序能够从异常情况中恢复并继续执行。
    - 例如，在读取文件时，如果文件不存在，会抛出 `FileNotFoundException`，这是一个 `Exception` 类型的异常，开发人员可以捕获该异常并进行相应的处理，如提示用户检查文件路径。
- Error（错误）
    - `Error` 表示系统级的错误或资源耗尽等严重问题，通常是程序无法控制和处理的。这些错误一般是由 Java 虚拟机（JVM）或底层系统出现问题导致的，例如内存溢出（`OutOfMemoryError`）、栈溢出（`StackOverflowError`）等。
    - 当出现 `Error` 时，程序通常无法恢复，可能需要终止运行并进行相应的系统调整或资源释放。

### 继承关系



- Exception
    - `Exception` 类是 `Throwable` 类的直接子类，它又可以分为受检查异常（Checked Exception）和非受检查异常（Unchecked Exception，也称为运行时异常 `RuntimeException`）。受检查异常在编译时就需要被捕获或声明抛出，而非受检查异常则不需要。
    - 例如，`IOException` 是受检查异常，`NullPointerException` 是运行时异常，属于非受检查异常。
- Error
    - `Error` 类同样是 `Throwable` 类的直接子类，它包含了各种系统级的错误类型，如 `VirtualMachineError`（JVM 错误）、`AWTError`（抽象窗口工具包错误）等。

### 处理方式



- Exception
    - 对于受检查异常，Java 编译器会强制要求开发人员进行处理，处理方式有两种：一是使用 `try-catch` 语句块捕获异常并进行处理；二是在方法签名中使用 `throws` 关键字声明抛出异常，将异常的处理责任交给调用者。
    - 示例代码如下：






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
            System.out.println("文件未找到: " + e.getMessage());
        }
    }
}
```



- 对于非受检查异常，虽然编译器不强制要求处理，但开发人员也可以根据需要进行捕获和处理，以增强程序的健壮性。
- Error
    - 由于 `Error` 通常表示系统级的严重问题，程序一般无法处理这些错误，因此不建议开发人员捕获和处理 `Error` 类型的异常。当出现 `Error` 时，通常意味着程序需要终止运行并进行相应的系统检查和调整。
    - 示例代码中不建议这样做：







```java
public class ErrorExample {
    public static void main(String[] args) {
        try {
            // 可能会导致 StackOverflowError 的代码
            recursiveMethod();
        } catch (StackOverflowError e) {
            // 不建议捕获 Error 类型的异常
            System.out.println("栈溢出错误: " + e.getMessage());
        }
    }

    public static void recursiveMethod() {
        recursiveMethod();
    }
}
```

### 常见子类示例



- Exception
    - **受检查异常**：`IOException`（输入输出异常）、`SQLException`（数据库操作异常）等。
    - **非受检查异常**：`NullPointerException`（空指针异常）、`ArrayIndexOutOfBoundsException`（数组越界异常）、`ArithmeticException`（算术异常，如除零错误）等。
- Error
    - `OutOfMemoryError`（内存溢出错误）、`StackOverflowError`（栈溢出错误）、`NoClassDefFoundError`（类定义未找到错误）等。


## Checked Exception 和 Unchecked Exception 有什么区别

### 定义与继承关系



- 受检查异常（Checked Exception）
    - 受检查异常是指那些继承自 `Exception` 类，但不继承自 `RuntimeException` 的异常。这类异常表示在程序执行过程中可能出现的可预见的异常情况，编译器会强制要求开发者对这些异常进行处理。
    - 例如，`IOException`、`SQLException` 等都属于受检查异常。
- 非受检查异常（Unchecked Exception）
    - 非受检查异常是指继承自 `RuntimeException` 类的异常，也被称为运行时异常。它们通常表示程序中的逻辑错误或编程错误，编译器不会强制要求对这类异常进行处理。
    - 像 `NullPointerException`、`ArrayIndexOutOfBoundsException`、`ArithmeticException` 等都属于非受检查异常。

### 编译器检查



- 受检查异常（Checked Exception）
    - 编译器会严格检查受检查异常。当一个方法可能抛出受检查异常时，要么在方法内部使用 `try-catch` 语句捕获并处理该异常，要么在方法签名中使用 `throws` 关键字声明抛出该异常。如果不进行处理，编译器会报错。
    - 示例代码如下：





```java
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class CheckedExceptionExample {
    // 声明抛出 FileNotFoundException
    public static void readFile() throws FileNotFoundException {
        File file = new File("nonexistent.txt");
        FileInputStream fis = new FileInputStream(file);
    }

    public static void main(String[] args) {
        try {
            readFile();
        } catch (FileNotFoundException e) {
            System.out.println("文件未找到: " + e.getMessage());
        }
    }
}
```



- 非受检查异常（Unchecked Exception）
    - 编译器不会对非受检查异常进行强制检查。即使一个方法可能抛出非受检查异常，也不需要在方法签名中声明，调用该方法时也不需要使用 `try-catch` 语句进行捕获。
    - 示例代码如下：








```java
public class UncheckedExceptionExample {
    public static void divide(int a, int b) {
        // 可能会抛出 ArithmeticException
        int result = a / b; 
        System.out.println("结果: " + result);
    }

    public static void main(String[] args) {
        divide(10, 0); 
    }
}
```

### 设计意图与使用场景



- 受检查异常（Checked Exception）
    - 受检查异常的设计意图是让开发者在编写代码时就考虑到可能出现的异常情况，并进行相应的处理。它们通常用于表示程序外部因素导致的异常，如文件操作、网络连接、数据库操作等。
    - 例如，在进行文件读写操作时，可能会出现文件不存在、文件权限不足等问题，这些都可以通过受检查异常来处理，以确保程序的健壮性。
- 非受检查异常（Unchecked Exception）
    - 非受检查异常主要用于表示程序中的逻辑错误或编程错误，如空指针引用、数组越界等。这些异常通常是由于代码编写不当引起的，应该在开发和测试阶段被发现和修复，而不是在运行时进行捕获和处理。
    - 例如，在使用对象之前没有进行空值检查，就可能会抛出 `NullPointerException`，开发者应该通过完善代码逻辑来避免这类异常的发生。

### 异常处理的责任



- 受检查异常（Checked Exception）
    - 受检查异常的处理责任通常由调用者承担。当一个方法抛出受检查异常时，调用者需要明确知道可能会出现的异常情况，并进行相应的处理，以保证程序的正常运行。
- 非受检查异常（Unchecked Exception）
    - 非受检查异常的处理责任主要在于方法的实现者。方法的实现者应该确保代码的正确性，避免出现逻辑错误导致的非受检查异常。如果出现了非受检查异常，通常意味着代码存在缺陷，需要进行修改。


除了`RuntimeException`及其子类以外，其他的`Exception`类及其子类都属于受检查异常 。常见的受检查异常有：IO 相关的异常、`ClassNotFoundException`、`SQLException`...。

**Unchecked Exception** 即 **不受检查异常** ，Java 代码在编译过程中 ，我们即使不处理不受检查异常也可以正常通过编译。

`RuntimeException` 及其子类都统称为非受检查异常，常见的有（建议记下来，日常开发中会经常用到）：

- `NullPointerException`(空指针错误)
- `IllegalArgumentException`(参数错误比如方法入参类型错误)
- `NumberFormatException`（字符串转换为数字格式错误，`IllegalArgumentException`的子类）
- `ArrayIndexOutOfBoundsException`（数组越界错误）
- `ClassCastException`（类型转换错误）
- `ArithmeticException`（算术错误）
- `SecurityException` （安全错误比如权限不够）
- `UnsupportedOperationException`(不支持的操作错误比如重复创建同一用户)

## try-catch-finally 如何使用？ 解释1

### 基本语法



java











```java
try {
    // 可能会抛出异常的代码块
} catch (ExceptionType1 e1) {
    // 处理 ExceptionType1 类型异常的代码块
} catch (ExceptionType2 e2) {
    // 处理 ExceptionType2 类型异常的代码块
} finally {
    // 无论是否发生异常都会执行的代码块
}
```





### 各部分说明



- **`try` 块**：包含可能会抛出异常的代码。当 `try` 块中的代码抛出异常时，程序会立即跳转到相应的 `catch` 块进行异常处理。
- **`catch` 块**：用于捕获和处理特定类型的异常。可以有多个 `catch` 块，每个 `catch` 块捕获一种特定类型的异常。异常类型需要按照从子类到父类的顺序排列，因为如果父类异常在前，子类异常的 `catch` 块将永远不会被执行。
- **`finally` 块**：无论 `try` 块中是否发生异常，`finally` 块中的代码都会被执行。通常用于释放资源，如关闭文件、数据库连接等。

### 使用示例

#### 示例 1：简单的 `try-catch-finally` 使用












```java
public class TryCatchFinallyExample {
    public static void main(String[] args) {
        try {
            int result = 10 / 0; 
            System.out.println("结果: " + result);
        } catch (ArithmeticException e) {
            System.out.println("发生算术异常: " + e.getMessage());
        } finally {
            System.out.println("finally 块执行");
        }
    }
}
```



在上述代码中，`try` 块中的 `10 / 0` 会抛出 `ArithmeticException` 异常，程序会跳转到相应的 `catch` 块进行处理，最后执行 `finally` 块中的代码。

#### 示例 2：多个 `catch` 块的使用








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
            System.out.println("文件未找到: " + e.getMessage());
        } catch (ArithmeticException e) {
            System.out.println("发生算术异常: " + e.getMessage());
        } finally {
            System.out.println("finally 块执行");
        }
    }
}
```



在这个示例中，`try` 块中可能会抛出 `FileNotFoundException` 和 `ArithmeticException` 两种异常。程序会根据抛出的异常类型跳转到相应的 `catch` 块进行处理，最后执行 `finally` 块。

#### 示例 3：`finally` 块用于资源释放











```java
import java.io.FileInputStream;
import java.io.IOException;

public class ResourceReleaseExample {
    public static void main(String[] args) {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream("example.txt");
            // 读取文件的操作
            int data;
            while ((data = fis.read()) != -1) {
                // 处理读取的数据
            }
        } catch (IOException e) {
            System.out.println("发生 IO 异常: " + e.getMessage());
        } finally {
            if (fis != null) {
                try {
                    fis.close(); 
                } catch (IOException e) {
                    System.out.println("关闭文件流时发生异常: " + e.getMessage());
                }
            }
        }
    }
}
```



在这个示例中，`finally` 块用于确保 `FileInputStream` 资源被关闭，无论 `try` 块中是否发生异常。

### 注意事项



- 如果 `try` 块中没有抛出异常，`catch` 块将不会执行，但 `finally` 块仍然会执行。
- 如果 `try` 块中抛出的异常没有被任何 `catch` 块捕获，程序会在执行完 `finally` 块后将异常抛给调用者。
- 在 `finally` 块中尽量避免使用 `return` 语句，因为这可能会导致 `try` 或 `catch` 块中的 `return` 语句失效。

## try-catch-finally 如何使用？ 解释2

### **1. 基本语法**




```
try {
    // 可能抛出异常的代码
} catch (ExceptionType1 e1) {
    // 处理 ExceptionType1 类型的异常
} catch (ExceptionType2 e2) {
    // 处理 ExceptionType2 类型的异常
} finally {
    // 无论是否发生异常，都会执行的代码
}
```

------

### **2. 各部分的作用**

1. **`try` 块**：
    - 包含可能抛出异常的代码。
    - 如果发生异常，程序会立即跳转到对应的 `catch` 块。
2. **`catch` 块**：
    - 捕获并处理特定类型的异常。
    - 可以有多个 `catch` 块，用于处理不同类型的异常。
    - 异常类型应从具体到抽象（例如，先捕获子类异常，再捕获父类异常）。
3. **`finally` 块**：
    - 无论是否发生异常，`finally` 块中的代码都会执行。
    - 通常用于释放资源（如关闭文件、数据库连接等）。

------

### **3. 示例代码**



```
public class TryCatchFinallyExample {
    public static void main(String[] args) {
        try {
            int result = 10 / 0; // 这里会抛出 ArithmeticException
            System.out.println("Result: " + result);
        } catch (ArithmeticException e) {
            System.out.println("Caught an exception: " + e.getMessage());
        } finally {
            System.out.println("This will always be executed.");
        }
    }
}
```

**输出**：



```
Caught an exception: / by zero
This will always be executed.
```

------

### **4. 注意事项**

1. **`finally` 块的执行**：
    - `finally` 块中的代码无论是否发生异常都会执行。
    - 即使在 `try` 或 `catch` 块中使用了 `return`，`finally` 块也会在方法返回之前执行。
2. **`try` 块必须与 `catch` 或 `finally` 块一起使用**：
    - `try` 块不能单独使用，必须至少有一个 `catch` 块或 `finally` 块。
3. **多个 `catch` 块的顺序**：
    - 如果有多个 `catch` 块，应该先捕获具体的异常类型，再捕获抽象的异常类型。
    - 例如，先捕获 `ArithmeticException`，再捕获 `Exception`。
4. **`finally` 块中的返回值**：
    - 如果 `finally` 块中有 `return` 语句，它会覆盖 `try` 或 `catch` 块中的 `return` 语句。

------

### **5. `try-with-resources`（Java 7+）**

对于需要关闭的资源（如文件、数据库连接等），可以使用 `try-with-resources` 语法，它会在 `try` 块结束后自动关闭资源，无需显式调用 `finally` 块。

**示例**：

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

**特点**：

- 资源（如 `BufferedReader`）在 `try` 块结束后自动关闭。
- 资源类必须实现 `AutoCloseable` 接口。

------

### **6. 总结**

| **部分**             | **作用**                         |
| :------------------- | :------------------------------- |
| `try`                | 包含可能抛出异常的代码           |
| `catch`              | 捕获并处理特定类型的异常         |
| `finally`            | 无论是否发生异常，都会执行的代码 |
| `try-with-resources` | 自动管理资源，无需显式关闭       |

通过合理使用 `try-catch-finally`，可以有效地处理异常并确保资源的正确释放。


## finally 中的代码一定会执行吗？

### 通常情况：`finally` 块代码会执行



当 `try` 块中的代码运行时，无论是否抛出异常，也无论异常是否被 `catch` 块捕获，`finally` 块中的代码都会执行。这一特性使得 `finally` 块非常适合用于释放资源，如关闭文件、数据库连接等。



**示例代码**：








```java
public class NormalFinallyExecution {
    public static void main(String[] args) {
        try {
            System.out.println("进入 try 块");
            int result = 10 / 2;
            System.out.println("try 块中计算结果: " + result);
        } catch (ArithmeticException e) {
            System.out.println("捕获到异常: " + e.getMessage());
        } finally {
            System.out.println("finally 块执行");
        }
    }
}
```



在上述代码中，`try` 块里的代码正常执行，没有抛出异常，`catch` 块不会执行，但 `finally` 块会执行。

### 特殊情况：`finally` 块代码可能不执行

#### 1. 程序在 `try` 块或 `catch` 块中调用 `System.exit()`



`System.exit()` 方法用于终止当前正在运行的 Java 虚拟机，一旦调用该方法，程序会立即终止，`finally` 块中的代码将不会执行。



**示例代码**：



```java
public class SystemExitExample {
    public static void main(String[] args) {
        try {
            System.out.println("进入 try 块");
            System.exit(0); 
        } catch (Exception e) {
            System.out.println("捕获到异常: " + e.getMessage());
        } finally {
            System.out.println("finally 块执行"); 
        }
    }
}
```



在这个例子中，`try` 块中调用了 `System.exit(0)`，程序会直接终止，`finally` 块中的代码不会被执行。

#### 2. 程序所在的线程被强制终止



如果在执行 `try-catch-finally` 结构的线程被强制终止，例如通过 `Thread.stop()` 方法（该方法已被弃用，因为它可能导致数据不一致等问题），`finally` 块中的代码也不会执行。

#### 3. Java 虚拟机崩溃



当 Java 虚拟机因为某些严重的错误（如内存溢出、栈溢出等严重错误）而崩溃时，`finally` 块中的代码将无法执行。例如，无限递归调用可能会导致 `StackOverflowError`，进而使 Java 虚拟机崩溃。















```java
public class JvmCrashExample {
    public static void recursiveMethod() {
        recursiveMethod();
    }

    public static void main(String[] args) {
        try {
            recursiveMethod();
        } catch (Exception e) {
            System.out.println("捕获到异常: " + e.getMessage());
        } finally {
            System.out.println("finally 块执行"); 
        }
    }
}
```







在上述代码中，由于无限递归调用，会抛出 `StackOverflowError`，Java 虚拟机可能会崩溃，`finally` 块可能无法执行。


## 异常的处理?式

- 遇到异常不进?具体处理，?是继续抛给调?者 （throw，throws），抛出异常有三种形式，?是 throw,?个 throws，还有?种系统?动抛异常。
  throws ?在?法上，后?跟的是异常类，可以跟多个；? throw ?在?法内，后?跟的是异常对象。
- try catch 捕获异常