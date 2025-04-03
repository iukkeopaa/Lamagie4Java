### 1. 类的加载与静态成员初始化



当 Java 虚拟机（JVM）首次使用某个类时，会对该类进行加载、链接和初始化。在这个过程中，静态成员（静态变量和静态代码块）会按照它们在类中出现的顺序依次执行。



java











```java
public class StaticInitializationOrder {
    // 静态变量
    public static int staticVariable = initializeStaticVariable();

    // 静态代码块
    static {
        System.out.println("静态代码块执行");
    }

    public static int initializeStaticVariable() {
        System.out.println("静态变量初始化");
        return 10;
    }

    public static void main(String[] args) {
        System.out.println("main 方法执行");
    }
}
```



执行上述代码，输出结果如下：



plaintext











```plaintext
静态变量初始化
静态代码块执行
main 方法执行
```



可以看到，静态变量和静态代码块在 `main` 方法执行之前就已经按照顺序执行完毕。

### 2. 实例成员初始化与构造方法调用



在创建类的实例时，会先进行实例成员（实例变量和实例代码块）的初始化，然后再调用构造方法。实例成员的初始化顺序也是按照它们在类中出现的顺序。



java











```java
public class InstanceInitializationOrder {
    // 实例变量
    public int instanceVariable = initializeInstanceVariable();

    // 实例代码块
    {
        System.out.println("实例代码块执行");
    }

    public int initializeInstanceVariable() {
        System.out.println("实例变量初始化");
        return 20;
    }

    // 构造方法
    public InstanceInitializationOrder() {
        System.out.println("构造方法执行");
    }

    public static void main(String[] args) {
        InstanceInitializationOrder obj = new InstanceInitializationOrder();
    }
}
```



执行上述代码，输出结果如下：



plaintext











```plaintext
实例变量初始化
实例代码块执行
构造方法执行
```



这表明在创建对象时，先初始化实例变量和执行实例代码块，最后调用构造方法。

### 3. 继承关系中的代码执行次序



在有继承关系的类中，代码的执行次序遵循以下规则：



- 父类的静态成员（静态变量和静态代码块）先执行，且只执行一次。
- 子类的静态成员（静态变量和静态代码块）执行。
- 父类的实例成员（实例变量和实例代码块）初始化，然后调用父类的构造方法。
- 子类的实例成员（实例变量和实例代码块）初始化，最后调用子类的构造方法。



java











```java
class Parent {
    // 父类静态变量
    public static int parentStaticVariable = initializeParentStaticVariable();

    // 父类静态代码块
    static {
        System.out.println("父类静态代码块执行");
    }

    // 父类实例变量
    public int parentInstanceVariable = initializeParentInstanceVariable();

    // 父类实例代码块
    {
        System.out.println("父类实例代码块执行");
    }

    // 父类构造方法
    public Parent() {
        System.out.println("父类构造方法执行");
    }

    public static int initializeParentStaticVariable() {
        System.out.println("父类静态变量初始化");
        return 30;
    }

    public int initializeParentInstanceVariable() {
        System.out.println("父类实例变量初始化");
        return 40;
    }
}

class Child extends Parent {
    // 子类静态变量
    public static int childStaticVariable = initializeChildStaticVariable();

    // 子类静态代码块
    static {
        System.out.println("子类静态代码块执行");
    }

    // 子类实例变量
    public int childInstanceVariable = initializeChildInstanceVariable();

    // 子类实例代码块
    {
        System.out.println("子类实例代码块执行");
    }

    // 子类构造方法
    public Child() {
        System.out.println("子类构造方法执行");
    }

    public static int initializeChildStaticVariable() {
        System.out.println("子类静态变量初始化");
        return 50;
    }

    public int initializeChildInstanceVariable() {
        System.out.println("子类实例变量初始化");
        return 60;
    }
}

public class InheritanceInitializationOrder {
    public static void main(String[] args) {
        Child child = new Child();
    }
}
```



执行上述代码，输出结果如下：



plaintext











```plaintext
父类静态变量初始化
父类静态代码块执行
子类静态变量初始化
子类静态代码块执行
父类实例变量初始化
父类实例代码块执行
父类构造方法执行
子类实例变量初始化
子类实例代码块执行
子类构造方法执行
```

### 4. 方法调用次序



方法的调用次序取决于代码的逻辑和调用顺序。当调用一个方法时，会先执行方法内部的代码，直到方法返回或抛出异常。如果方法内部调用了其他方法，会按照调用顺序依次执行被调用的方法。



java











```java
public class MethodInvocationOrder {
    public static void method1() {
        System.out.println("方法 1 执行");
        method2();
    }

    public static void method2() {
        System.out.println("方法 2 执行");
    }

    public static void main(String[] args) {
        method1();
    }
}
```







执行上述代码，输出结果如下：



plaintext











```plaintext
方法 1 执行
方法 2 执行
```


## 总结 1

1. 父类静态代码块（只执行一次）
2. 子类静态代码块（只执行一次）
3. 父类构造代码块
4. 父类构造函数
5. 子类构造代码块
6. 子类构造函数
7. 普通代码块
## 总结2

先后顺序：静态成员变量、成员变量、构造方法。

详细的先后顺序：父类静态变量、父类静态代码块、子类静态变量、子类静态代码块、父类非静态变 量、父类非静态代码块、父类构造函数、子类非静态变量、子类非静态代码块、子类构造函数。
