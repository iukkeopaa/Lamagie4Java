
### 1. �������



- **JDK ��̬����**�����ڽӿ�ʵ�֡���Ҫ�󱻴���������ʵ������һ���ӿڣ��������ʵ����Ŀ�������ͬ�Ľӿڣ�������ʱͨ��������ƶ�̬���ɴ�������ֽ��룬�����ص� JVM �С�
- **CGLIB ��̬����**�����ڼ̳�ʵ�֡���ͨ���̳б��������������������������ʱ���ɱ�����������࣬����д���еķ�����ʵ�ִ����߼�����ˣ���������಻���� `final` �࣬��Ϊ `final` �಻�ܱ��̳С�

### 2. ������



- **JDK ��̬����**���� JDK �Դ��Ĺ��ܣ������������������⣬ֻ��Ҫʹ�� `java.lang.reflect.Proxy` �� `java.lang.reflect.InvocationHandler` �������༴��ʵ�֡�
- **CGLIB ��̬����**����Ҫ���� CGLIB �⡣�� Maven ��Ŀ�У�����ͨ�������������룺










```xml
<dependency>
    <groupId>cglib</groupId>
    <artifactId>cglib</artifactId>
    <version>3.4.0</version>
</dependency>
```

### 3. ����



- **JDK ��̬����**���ڴ����������ʱ������ʹ���˷�����ƣ�����һ�������ܿ����������ڵ��ô�����ʱ��������ԽϺã���������Ƶ�����õĳ����¡�
- **CGLIB ��̬����**��������������������Խϸߣ���Ϊ����ͨ���ֽ������ɵķ�ʽ���������ࡣ�����ڵ��ô�����ʱ��������Ҫͨ���̳кͷ�����д������һ����������ģ��������ڷ����������ϳ�������¡�

### 4. ʹ�ó���



- **JDK ��̬����**�������ڱ�������ʵ���˽ӿڵĳ��������磬�� Spring ����У����ʹ�� `@Transactional` ע���ʵ���˽ӿڵķ���������������Ĭ�ϻ�ʹ�� JDK ��̬����
- **CGLIB ��̬����**�������ڱ�������û��ʵ�ֽӿڵĳ��������磬�� Spring ����У����������û��ʵ�ֽӿڣ�Spring ��ʹ�� CGLIB ��̬������ʵ�� AOP ���ܡ�

### 5. ����ʾ��

#### JDK ��̬����ʾ��








```java
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

// ����ӿ�
interface Subject {
    void request();
}

// ʵ�ֽӿڵ�Ŀ����
class RealSubject implements Subject {
    @Override
    public void request() {
        System.out.println("RealSubject: Handling request.");
    }
}

// ʵ�� InvocationHandler �ӿ�
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

// ���Դ���
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

#### CGLIB ��̬����ʾ��










```java
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

// Ŀ����
class TargetClass {
    public void method() {
        System.out.println("TargetClass: Executing method.");
    }
}

// ʵ�� MethodInterceptor �ӿ�
class CglibProxyInterceptor implements MethodInterceptor {
    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        System.out.println("Before method call");
        Object result = proxy.invokeSuper(obj, args);
        System.out.println("After method call");
        return result;
    }
}

// ���Դ���
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


## JDK����ʧЧ�ĳ���

�� Java �У�JDK ����ģʽ��Ҫָ���� JDK ��̬�������ǻ��ڽӿ�ʵ�ֵ�һ�ִ�����ơ�����Ϊ����� JDK ��̬�����ԭ��ʧЧ��������Ӧ���������

### JDK ��̬����ԭ��

JDK ��̬�������� `java.lang.reflect.Proxy` ��� `java.lang.reflect.InvocationHandler` �ӿ���ʵ�֡�`Proxy` �����ڴ����������`InvocationHandler` �ӿ����ڶ��������󷽷�����ʱ�Ĵ����߼��������ô������ķ���ʱ��ʵ���ϻ���� `InvocationHandler` �е� `invoke` ������

������һ���򵥵� JDK ��̬����ʾ����






```java
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

// ����ӿ�
interface Subject {
    void request();
}

// ʵ�ֽӿڵ���ʵ����
class RealSubject implements Subject {
    @Override
    public void request() {
        System.out.println("RealSubject: Handling request.");
    }
}

// ʵ�� InvocationHandler �ӿ�
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

### ʧЧ�������������

#### 1. Ŀ�����δʵ�ֽӿ�

- **ʧЧԭ��**��JDK ��̬�����ǻ��ڽӿ�ʵ�ֵģ���Ŀ�����û��ʵ���κνӿڣ�`Proxy.newProxyInstance` �������޷�Ϊ�䴴���������
- **�������**��ʹ�� CGLIB ����CGLIB �ǻ��ڼ̳�ʵ�ֵĴ�����ƣ�������Ϊû��ʵ�ֽӿڵ��ഴ���������������ʹ�� CGLIB �����ʾ����








```java
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

// û��ʵ�ֽӿڵ���
class TargetClass {
    public void doSomething() {
        System.out.println("TargetClass: Doing something.");
    }
}

// ʵ�� MethodInterceptor �ӿ�
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

#### 2. ����Ϊ `final` �� `static`

- **ʧЧԭ��**������ `final` �����������䲻�ܱ���д��JDK ��̬�����޷����������ǿ������ `static` ����������������Ƕ���JDK ��̬�����ǻ��ڶ��󷽷����õģ������޷��� `static` �������д���
- **�������**������ `final` ����������Ҫ��ǿ�߼����ɿ����ع����룬�� `final` �������߼���ȡ���� `final` �����У����� `static` ��������ͨ����̬������������ʽ�������ʱ������ǿ������ʹ�� AOP ��ܣ��� AspectJ��������

#### 3. �����������

- **ʧЧԭ��**����ʹ�� `Proxy.newProxyInstance` ����ʱ����Ҫ������ʵ�������������������������޷�����Ŀ��ӿڻ�����࣬�ᵼ�´�����󴴽�ʧ�ܡ�
- **�������**��ȷ���������������ܹ���ȷ����Ŀ��ӿں�����ࡣͨ������ʹ��Ŀ����������������� `target.getClass().getClassLoader()`��

#### 4. Ȩ������

- **ʧЧԭ��**�����ڴ����������ʱ��û���㹻��Ȩ�޷���Ŀ�����ӿڣ��ᵼ�´�����󴴽�ʧ�ܡ�
- **�������**����鲢ȷ��������з���Ŀ����ͽӿڵ�Ȩ�ޣ��ر�����ʹ�ð�ȫ�������Ļ����У���Ҫ��ȷ���ð�ȫ���ԡ�


