### **1. `continue`**

- **作用**：跳过当前循环的剩余代码，直接进入下一次循环。

- **适用场景**：用于循环结构（`for`、`while`、`do-while`）。

- **特点**：

    - 不会终止循环，只是跳过当前迭代。
    - 通常与条件语句结合使用，用于跳过某些特定情况。

- **示例**：


  ```
  for (int i = 1; i <= 5; i++) {
      if (i == 3) {
          continue; // 跳过 i == 3 的情况
      }
      System.out.println(i);
  }
  ```

  **输出**：

  复制

  ```
  1
  2
  4
  5
  ```

------

### **2. `break`**

- **作用**：立即终止当前循环或 `switch` 语句。

- **适用场景**：用于循环结构（`for`、`while`、`do-while`）和 `switch` 语句。

- **特点**：

    - 完全退出当前循环或 `switch` 语句。
    - 可以用于提前结束循环。

- **示例**：

 

  ```
  for (int i = 1; i <= 5; i++) {
      if (i == 3) {
          break; // 当 i == 3 时终止循环
      }
      System.out.println(i);
  }
  ```

  **输出**：



  ```
  1
  2
  ```

------

### **3. `return`**

- **作用**：立即结束当前方法的执行，并返回一个值（如果方法有返回值）。

- **适用场景**：用于方法中。

- **特点**：

    - 终止当前方法的执行。
    - 如果方法有返回值，必须返回一个与声明类型匹配的值。
    - 如果方法返回类型为 `void`，可以省略返回值。
      `return` 用于跳出所在方法，结束该方法的运行。return 一般有两种用法：


- **示例**：



  ```
  public int add(int a, int b) {
      return a + b; // 返回 a + b 的结果
  }
  
  public void print(int value) {
      if (value < 0) {
          return; // 如果 value < 0，直接结束方法
      }
      System.out.println(value);
  }
  ```

------

### **三者的对比**

| 关键字     | 作用范围           | 功能描述                               | 是否终止循环 | 是否终止方法 |
| :--------- | :----------------- | :------------------------------------- | :----------- | :----------- |
| `continue` | 循环结构           | 跳过当前迭代，进入下一次循环           | 否           | 否           |
| `break`    | 循环结构、`switch` | 终止当前循环或 `switch` 语句           | 是           | 否           |
| `return`   | 方法               | 结束当前方法的执行，并返回值（如果有） | 是           | 是           |

------

### **使用场景总结**

1. **`continue`**：
    - 当需要跳过某些特定条件时使用。
    - 例如：遍历数组时跳过某些元素。
2. **`break`**：
    - 当满足某些条件时需要提前退出循环或 `switch` 语句时使用。
    - 例如：在查找元素时，找到目标后立即退出循环。
3. **`return`**：
    - 当方法执行到某个条件时需要立即返回结果或结束方法时使用。
    - 例如：递归方法的终止条件。

------

### **示例代码**



```
public class ControlFlowExample {
    public static void main(String[] args) {
        // continue 示例
        System.out.println("continue 示例：");
        for (int i = 1; i <= 5; i++) {
            if (i == 3) {
                continue; // 跳过 i == 3
            }
            System.out.println(i);
        }

        // break 示例
        System.out.println("break 示例：");
        for (int i = 1; i <= 5; i++) {
            if (i == 3) {
                break; // 终止循环
            }
            System.out.println(i);
        }

        // return 示例
        System.out.println("return 示例：");
        System.out.println(add(2, 3)); // 输出：5
        print(-1); // 无输出
    }

    public static int add(int a, int b) {
        return a + b; // 返回 a + b 的结果
    }

    public static void print(int value) {
        if (value < 0) {
            return; // 直接结束方法
        }
        System.out.println(value);
    }
}
```

------

### **总结**

- `continue` 用于跳过当前循环的剩余代码，进入下一次循环。
- `break` 用于立即终止当前循环或 `switch` 语句。
- `return` 用于立即结束当前方法的执行，并返回值（如果有）。


## finally 中的代码比 return 和 break 语句后执行