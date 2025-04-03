- static ?于修饰成员时，该成员成为类级别的，?不是实例级别的。静态成员属于类，?不是属于类的实
例。

```java
public class MyClass {
static int staticVariable; // 静态变量
static void staticMethod() { // 静态?法
// ...
}
}
int value = MyClass.staticVariable;
MyClass.staticMethod();
```
- 当 final ?于修饰变量、?法或类时，表示它是不可变的。对于变量，?旦赋值后就不能再修改；对于?
法，表示?法不能被?类重写；对于类，表示类不能被继承。

```java
public class Example {
 final int constantValue = 42; // 不可变的变量
 final void finalMethod() { // 不可变的?法
 // ...
 }
}
final class FinalClass { // 不可变的类
 // ...
}
```

