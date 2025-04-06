package JdkProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

// 定义一个接口
interface Subject {
    void request();
}

// 实现接口的真实类
class RealSubject implements Subject {
    @Override
    public void request() {
        System.out.println("RealSubject: Handling request.");
    }
}

// 实现InvocationHandler接口
class ProxyHandler implements InvocationHandler {
    private final Object target;

    public ProxyHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("Before method call.");
        Object result = method.invoke(target, args);
        System.out.println("After method call.");
        return result;
    }
}

public class Main {
    public static void main(String[] args) {
        // 创建真实对象
        RealSubject realSubject = new RealSubject();

        // 创建InvocationHandler实例
        ProxyHandler handler = new ProxyHandler(realSubject);

        // 创建代理对象
        Subject proxySubject = (Subject) Proxy.newProxyInstance(
                Subject.class.getClassLoader(),
                new Class<?>[]{Subject.class},
                handler
        );

        // 调用代理对象的方法
        proxySubject.request();
    }
}    