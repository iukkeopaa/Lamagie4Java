### 1. ��ļ����뾲̬��Ա��ʼ��



�� Java �������JVM���״�ʹ��ĳ����ʱ����Ը�����м��ء����Ӻͳ�ʼ��������������У���̬��Ա����̬�����;�̬����飩�ᰴ�����������г��ֵ�˳������ִ�С�



java











```java
public class StaticInitializationOrder {
    // ��̬����
    public static int staticVariable = initializeStaticVariable();

    // ��̬�����
    static {
        System.out.println("��̬�����ִ��");
    }

    public static int initializeStaticVariable() {
        System.out.println("��̬������ʼ��");
        return 10;
    }

    public static void main(String[] args) {
        System.out.println("main ����ִ��");
    }
}
```



ִ���������룬���������£�



plaintext











```plaintext
��̬������ʼ��
��̬�����ִ��
main ����ִ��
```



���Կ�������̬�����;�̬������� `main` ����ִ��֮ǰ���Ѿ�����˳��ִ����ϡ�

### 2. ʵ����Ա��ʼ���빹�췽������



�ڴ������ʵ��ʱ�����Ƚ���ʵ����Ա��ʵ��������ʵ������飩�ĳ�ʼ����Ȼ���ٵ��ù��췽����ʵ����Ա�ĳ�ʼ��˳��Ҳ�ǰ������������г��ֵ�˳��



java











```java
public class InstanceInitializationOrder {
    // ʵ������
    public int instanceVariable = initializeInstanceVariable();

    // ʵ�������
    {
        System.out.println("ʵ�������ִ��");
    }

    public int initializeInstanceVariable() {
        System.out.println("ʵ��������ʼ��");
        return 20;
    }

    // ���췽��
    public InstanceInitializationOrder() {
        System.out.println("���췽��ִ��");
    }

    public static void main(String[] args) {
        InstanceInitializationOrder obj = new InstanceInitializationOrder();
    }
}
```



ִ���������룬���������£�



plaintext











```plaintext
ʵ��������ʼ��
ʵ�������ִ��
���췽��ִ��
```



������ڴ�������ʱ���ȳ�ʼ��ʵ��������ִ��ʵ������飬�����ù��췽����

### 3. �̳й�ϵ�еĴ���ִ�д���



���м̳й�ϵ�����У������ִ�д�����ѭ���¹���



- ����ľ�̬��Ա����̬�����;�̬����飩��ִ�У���ִֻ��һ�Ρ�
- ����ľ�̬��Ա����̬�����;�̬����飩ִ�С�
- �����ʵ����Ա��ʵ��������ʵ������飩��ʼ����Ȼ����ø���Ĺ��췽����
- �����ʵ����Ա��ʵ��������ʵ������飩��ʼ��������������Ĺ��췽����



java











```java
class Parent {
    // ���ྲ̬����
    public static int parentStaticVariable = initializeParentStaticVariable();

    // ���ྲ̬�����
    static {
        System.out.println("���ྲ̬�����ִ��");
    }

    // ����ʵ������
    public int parentInstanceVariable = initializeParentInstanceVariable();

    // ����ʵ�������
    {
        System.out.println("����ʵ�������ִ��");
    }

    // ���๹�췽��
    public Parent() {
        System.out.println("���๹�췽��ִ��");
    }

    public static int initializeParentStaticVariable() {
        System.out.println("���ྲ̬������ʼ��");
        return 30;
    }

    public int initializeParentInstanceVariable() {
        System.out.println("����ʵ��������ʼ��");
        return 40;
    }
}

class Child extends Parent {
    // ���ྲ̬����
    public static int childStaticVariable = initializeChildStaticVariable();

    // ���ྲ̬�����
    static {
        System.out.println("���ྲ̬�����ִ��");
    }

    // ����ʵ������
    public int childInstanceVariable = initializeChildInstanceVariable();

    // ����ʵ�������
    {
        System.out.println("����ʵ�������ִ��");
    }

    // ���๹�췽��
    public Child() {
        System.out.println("���๹�췽��ִ��");
    }

    public static int initializeChildStaticVariable() {
        System.out.println("���ྲ̬������ʼ��");
        return 50;
    }

    public int initializeChildInstanceVariable() {
        System.out.println("����ʵ��������ʼ��");
        return 60;
    }
}

public class InheritanceInitializationOrder {
    public static void main(String[] args) {
        Child child = new Child();
    }
}
```



ִ���������룬���������£�



plaintext











```plaintext
���ྲ̬������ʼ��
���ྲ̬�����ִ��
���ྲ̬������ʼ��
���ྲ̬�����ִ��
����ʵ��������ʼ��
����ʵ�������ִ��
���๹�췽��ִ��
����ʵ��������ʼ��
����ʵ�������ִ��
���๹�췽��ִ��
```

### 4. �������ô���



�����ĵ��ô���ȡ���ڴ�����߼��͵���˳�򡣵�����һ������ʱ������ִ�з����ڲ��Ĵ��룬ֱ���������ػ��׳��쳣����������ڲ������������������ᰴ�յ���˳������ִ�б����õķ�����



java











```java
public class MethodInvocationOrder {
    public static void method1() {
        System.out.println("���� 1 ִ��");
        method2();
    }

    public static void method2() {
        System.out.println("���� 2 ִ��");
    }

    public static void main(String[] args) {
        method1();
    }
}
```







ִ���������룬���������£�



plaintext











```plaintext
���� 1 ִ��
���� 2 ִ��
```


## �ܽ� 1

1. ���ྲ̬����飨ִֻ��һ�Σ�
2. ���ྲ̬����飨ִֻ��һ�Σ�
3. ���๹������
4. ���๹�캯��
5. ���๹������
6. ���๹�캯��
7. ��ͨ�����
## �ܽ�2

�Ⱥ�˳�򣺾�̬��Ա��������Ա���������췽����

��ϸ���Ⱥ�˳�򣺸��ྲ̬���������ྲ̬����顢���ྲ̬���������ྲ̬����顢����Ǿ�̬�� ��������Ǿ�̬����顢���๹�캯��������Ǿ�̬����������Ǿ�̬����顢���๹�캯����
