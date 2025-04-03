
### 1. 代理机制



- **JDK 动态代理**：基于接口实现。它要求被代理的类必须实现至少一个接口，代理类会实现与目标对象相同的接口，在运行时通过反射机制动态生成代理类的字节码，并加载到 JVM 中。
- **CGLIB 动态代理**：基于继承实现。它通过继承被代理类来创建代理对象，在运行时生成被代理类的子类，并重写其中的方法来实现代理逻辑。因此，被代理的类不能是 `final` 类，因为 `final` 类不能被继承。

### 2. 依赖库



- **JDK 动态代理**：是 JDK 自带的功能，无需引入额外的依赖库，只需要使用 `java.lang.reflect.Proxy` 和 `java.lang.reflect.InvocationHandler` 这两个类即可实现。
- **CGLIB 动态代理**：需要引入 CGLIB 库。在 Maven 项目中，可以通过以下依赖引入：










```xml
<dependency>
    <groupId>cglib</groupId>
    <artifactId>cglib</artifactId>
    <version>3.4.0</version>
</dependency>
```

### 3. 性能



- **JDK 动态代理**：在创建代理对象时，由于使用了反射机制，会有一定的性能开销。但是在调用代理方法时，性能相对较好，尤其是在频繁调用的场景下。
- **CGLIB 动态代理**：创建代理对象的性能相对较高，因为它是通过字节码生成的方式创建代理类。但是在调用代理方法时，由于需要通过继承和方法重写，会有一定的性能损耗，尤其是在方法调用链较长的情况下。

### 4. 使用场景



- **JDK 动态代理**：适用于被代理类实现了接口的场景。例如，在 Spring 框架中，如果使用 `@Transactional` 注解对实现了接口的服务类进行事务管理，默认会使用 JDK 动态代理。
- **CGLIB 动态代理**：适用于被代理类没有实现接口的场景。例如，在 Spring 框架中，如果服务类没有实现接口，Spring 会使用 CGLIB 动态代理来实现 AOP 功能。

### 5. 代码示例

#### JDK 动态代理示例








```java
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

// 定义接口
interface Subject {
    void request();
}

// 实现接口的目标类
class RealSubject implements Subject {
    @Override
    public void request() {
        System.out.println("RealSubject: Handling request.");
    }
}

// 实现 InvocationHandler 接口
class JdkProxyHandler implements InvocationHandler {
    private final Object target;

    public JdkProxyHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("Before method call");
        Object result = method.invoke(target, args);
        System.out.println("After method call");
        return result;
    }
}

// 测试代码
public class JdkProxyExample {
    public static void main(String[] args) {
        RealSubject realSubject = new RealSubject();
        JdkProxyHandler handler = new JdkProxyHandler(realSubject);
        Subject proxySubject = (Subject) Proxy.newProxyInstance(
                Subject.class.getClassLoader(),
                new Class<?>[]{Subject.class},
                handler
        );
        proxySubject.request();
    }
}
```

#### CGLIB 动态代理示例










```java
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

// 目标类
class TargetClass {
    public void method() {
        System.out.println("TargetClass: Executing method.");
    }
}

// 实现 MethodInterceptor 接口
class CglibProxyInterceptor implements MethodInterceptor {
    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        System.out.println("Before method call");
        Object result = proxy.invokeSuper(obj, args);
        System.out.println("After method call");
        return result;
    }
}

// 测试代码
public class CglibProxyExample {
    public static void main(String[] args) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(TargetClass.class);
        enhancer.setCallback(new CglibProxyInterceptor());
        TargetClass proxy = (TargetClass) enhancer.create();
        proxy.method();
    }
}
```


## JDK代理失效的场景

在 Java 中，JDK 代理模式主要指的是 JDK 动态代理，它是基于接口实现的一种代理机制。下面为你介绍 JDK 动态代理的原理、失效场景及对应解决方案。

### JDK 动态代理原理

JDK 动态代理利用 `java.lang.reflect.Proxy` 类和 `java.lang.reflect.InvocationHandler` 接口来实现。`Proxy` 类用于创建代理对象，`InvocationHandler` 接口用于定义代理对象方法调用时的处理逻辑。当调用代理对象的方法时，实际上会调用 `InvocationHandler` 中的 `invoke` 方法。

以下是一个简单的 JDK 动态代理示例：






```java
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

// 定义接口
interface Subject {
    void request();
}

// 实现接口的真实对象
class RealSubject implements Subject {
    @Override
    public void request() {
        System.out.println("RealSubject: Handling request.");
    }
}

// 实现 InvocationHandler 接口
class ProxyHandler implements InvocationHandler {
    private final Object target;

    public ProxyHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("Before method call");
        Object result = method.invoke(target, args);
        System.out.println("After method call");
        return result;
    }
}

public class JdkProxyExample {
    public static void main(String[] args) {
        RealSubject realSubject = new RealSubject();
        ProxyHandler handler = new ProxyHandler(realSubject);
        Subject proxySubject = (Subject) Proxy.newProxyInstance(
                Subject.class.getClassLoader(),
                new Class<?>[]{Subject.class},
                handler
        );
        proxySubject.request();
    }
}
```

### 失效场景及解决方案

#### 1. 目标对象未实现接口

- **失效原因**：JDK 动态代理是基于接口实现的，若目标对象没有实现任何接口，`Proxy.newProxyInstance` 方法将无法为其创建代理对象。
- **解决方案**：使用 CGLIB 代理，CGLIB 是基于继承实现的代理机制，它可以为没有实现接口的类创建代理对象。以下是使用 CGLIB 代理的示例：








```java
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

// 没有实现接口的类
class TargetClass {
    public void doSomething() {
        System.out.println("TargetClass: Doing something.");
    }
}

// 实现 MethodInterceptor 接口
class CglibProxyHandler implements MethodInterceptor {
    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        System.out.println("Before method call");
        Object result = proxy.invokeSuper(obj, args);
        System.out.println("After method call");
        return result;
    }
}

public class CglibProxyExample {
    public static void main(String[] args) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(TargetClass.class);
        enhancer.setCallback(new CglibProxyHandler());
        TargetClass proxy = (TargetClass) enhancer.create();
        proxy.doSomething();
    }
}
```

#### 2. 方法为 `final` 或 `static`

- **失效原因**：对于 `final` 方法，由于其不能被重写，JDK 动态代理无法对其进行增强；对于 `static` 方法，它属于类而非对象，JDK 动态代理是基于对象方法调用的，所以无法对 `static` 方法进行代理。
- **解决方案**：对于 `final` 方法，若需要增强逻辑，可考虑重构代码，将 `final` 方法的逻辑提取到非 `final` 方法中；对于 `static` 方法，可通过静态代码块或其他方式在类加载时进行增强，或者使用 AOP 框架（如 AspectJ）来处理。

#### 3. 类加载器问题

- **失效原因**：在使用 `Proxy.newProxyInstance` 方法时，需要传入合适的类加载器。若传入的类加载器无法加载目标接口或相关类，会导致代理对象创建失败。
- **解决方案**：确保传入的类加载器能够正确加载目标接口和相关类。通常可以使用目标对象的类加载器，如 `target.getClass().getClassLoader()`。

#### 4. 权限问题

- **失效原因**：若在创建代理对象时，没有足够的权限访问目标类或接口，会导致代理对象创建失败。
- **解决方案**：检查并确保程序具有访问目标类和接口的权限，特别是在使用安全管理器的环境中，需要正确配置安全策略。


