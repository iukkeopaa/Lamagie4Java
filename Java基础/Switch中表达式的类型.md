在Java中，`switch`语句的表达式类型可以是以下几种：

1. **整数类型**：
    - `byte`
    - `short`
    - `int`
    - `char`
2. **枚举类型**（从Java 5开始）：
    - 枚举常量
3. **字符串类型**（从Java 7开始）：
    - `String`
4. **包装类类型**（从Java 5开始）：
    - `Byte`
    - `Short`
    - `Integer`
    - `Character`

**但是?整型(long)在?前所有的版本中都是不可以的**
### 示例代码

#### 1. 整数类型



```
int day = 3;
switch (day) {
    case 1:
        System.out.println("Monday");
        break;
    case 2:
        System.out.println("Tuesday");
        break;
    case 3:
        System.out.println("Wednesday");
        break;
    default:
        System.out.println("Invalid day");
}
```

#### 2. 枚举类型



```
enum Day { MONDAY, TUESDAY, WEDNESDAY }

Day day = Day.WEDNESDAY;
switch (day) {
    case MONDAY:
        System.out.println("Monday");
        break;
    case TUESDAY:
        System.out.println("Tuesday");
        break;
    case WEDNESDAY:
        System.out.println("Wednesday");
        break;
    default:
        System.out.println("Invalid day");
}
```

#### 3. 字符串类型



```
String day = "Wednesday";
switch (day) {
    case "Monday":
        System.out.println("Monday");
        break;
    case "Tuesday":
        System.out.println("Tuesday");
        break;
    case "Wednesday":
        System.out.println("Wednesday");
        break;
    default:
        System.out.println("Invalid day");
}
```

#### 4. 包装类类型




```
Integer day = 3;
switch (day) {
    case 1:
        System.out.println("Monday");
        break;
    case 2:
        System.out.println("Tuesday");
        break;
    case 3:
        System.out.println("Wednesday");
        break;
    default:
        System.out.println("Invalid day");
}
```

### 注意事项

- `switch`表达式的类型必须与`case`标签的类型兼容。
- 从Java 12开始，`switch`表达式还可以作为表达式使用，并且支持`->`语法和`yield`关键字。

### Java 12+ 示例



```
int day = 3;
String dayName = switch (day) {
    case 1 -> "Monday";
    case 2 -> "Tuesday";
    case 3 -> "Wednesday";
    default -> "Invalid day";
};
System.out.println(dayName);
```

