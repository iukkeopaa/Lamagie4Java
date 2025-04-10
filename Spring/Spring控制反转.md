### 什么是Spring IOC 容器

控制反转即IoC (Inversion of Control)，它把传统上由程序代码直接操控的对象的调用权交给容器，通
过容器来实现对象组件的装配和管理。所谓的“控制反转”概念就是对组件对象控制权的转移，从程序代
码本身转移到了外部容器。

Spring IOC 负责创建对象，管理对象（通过依赖注入（DI），装配对象，配置对象，并且管理这些对象
的整个生命周期。

### 控制反转(IoC)有什么作用

1. 管理对象的创建和依赖关系的维护。对象的创建并不是一件简单的事，在对象关系比较复杂时，如
果依赖关系需要程序猿来维护的话，那是相当头疼的
2. 解耦，由容器去维护具体的对象
3. 托管了类的产生过程，比如我们需要在类的产生过程中做一些处理，最直接的例子就是代理，如果
有容器程序可以把这部分处理交给容器，应用程序则无需去关心类是如何完成代理的
### IOC的优点是什么

1. IOC 或 依赖注入把应用的代码量降到最低。
2. 它使应用容易测试，单元测试不再需要单例和JNDI查找机制。
3. 最小的代价和最小的侵入性使松散耦合得以实现。
4. IOC容器支持加载服务时的饿汉式初始化和懒加载
### Spring IoC 的实现机制

Spring 中的 IoC 的实现原理就是工厂模式加反射机制。

````java
interface Fruit {
    public abstract void eat();
}
class Apple implements Fruit {
    public void eat(){
        System.out.println("Apple");
    }
}
class Orange implements Fruit {
    public void eat(){
        System.out.println("Orange");
    }
}
class Factory {
    public static Fruit getInstance(String ClassName) {
        Fruit f=null;
        try {
            f=(Fruit)Class.forName(ClassName).newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return f;
    }
}
class Client {
    public static void main(String[] a) {
        Fruit f=Factory.getInstance("io.github.dunwu.spring.Apple");
        if(f!=null){
            f.eat();
        }
    }
}
````

### Spring 的 IoC 设计支持以下功能：

* 依赖注入
* 依赖检查
* 自动装配
* 支持集合
* 指定初始化方法和销毁方法
* 支持回调某些方法（但是需要实现 Spring 接口，略有侵入）

其中，最重要的就是依赖注入，从 XML 的配置上说，即 ref 标签。对应 Spring
RuntimeBeanReference 对象。

对于 IoC 来说，最重要的就是容器。容器管理着 Bean 的生命周期，控制着 Bean 的依赖注入

### BeanFactory 和 ApplicationContext有什么区别？

BeanFactory和ApplicationContext是Spring的两大核心接口，都可以当做Spring的容器。

其中ApplicationContext是BeanFactory的子接口。

#### 依赖关系
***BeanFactory***：是Spring里面最底层的接口，包含了各种Bean的定义，读取bean配置文档，管理bean
的加载、实例化，控制bean的生命周期，维护bean之间的依赖关系。

***ApplicationContext***接口作为BeanFactory的派生，除了提供BeanFactory所具有的功能外，还提供了
更完整的框架功能：
1. 继承MessageSource，因此支持国际化。
2. 统一的资源文件访问方式。
3. 提供在监听器中注册bean的事件。
4. 同时加载多个配置文件。
5. 载入多个（有继承关系）上下文 ，使得每一个上下文都专注于一个特定的层次，比如应用的web
层。
#### 加载方式

BeanFactroy采用的是延迟加载形式来注入Bean的，即只有在使用到某个Bean时(调用getBean())，才
对该Bean进行加载实例化。这样，我们就不能发现一些存在的Spring的配置问题。如果Bean的某一个
属性没有注入，BeanFacotry加载后，直至第一次使用调用getBean方法才会抛出异常。

ApplicationContext，它是在容器启动时，一次性创建了所有的Bean。这样，在容器启动时，我们就可
以发现Spring中存在的配置错误，这样有利于检查所依赖属性是否注入。 ApplicationContext启动后
预载入所有的单实例Bean，通过预载入单实例bean ,确保当你需要的时候，你就不用等待，因为它们
已经创建好了。

相对于基本的BeanFactory，ApplicationContext 唯一的不足是占用内存空间。当应用程序配置Bean较
多时，程序启动较慢

#### 创建方式

BeanFactory通常以编程的方式被创建，ApplicationContext还能以声明的方式创建，如使用
ContextLoader。

#### 注册方式

BeanFactory和ApplicationContext都支持BeanPostProcessor、BeanFactoryPostProcessor的使用，
但两者之间的区别是：BeanFactory需要手动注册，而ApplicationContext则是自动注册。

#### ApplicationContext通常的实现是什么？

***FileSystemXmlApplicationContext***：此容器从一个XML文件中加载beans的定义，XML Bean 配置
文件的全路径名必须提供给它的构造函数。

***ClassPathXmlApplicationContext***：此容器也从一个XML文件中加载beans的定义，这里，你需要正
确设置classpath因为这个容器将在classpath里找bean配置。

***WebXmlApplicationContext***：此容器加载一个XML文件，此文件定义了一个WEB应用的所有bean。

### 什么是Spring的依赖注入

控制反转IoC是一个很大的概念，可以用不同的方式来实现。其主要实现方式有两种：依赖注入和依赖
查找

依赖注入：相对于IoC而言，依赖注入(DI)更加准确地描述了IoC的设计理念。所谓依赖注入
（Dependency Injection），即组件之间的依赖关系由容器在应用系统运行期来决定，也就是由容器
动态地将某种依赖关系的目标对象实例注入到应用系统中的各个关联的组件之中。组件不做定位查询，
只提供普通的Java方法让容器去决定依赖关系。

### 依赖注入的基本原则

依赖注入的基本原则是：应用组件不应该负责查找资源或者其他依赖的协作对象。配置对象的工作应该
由IoC容器负责，“查找资源”的逻辑应该从应用组件的代码中抽取出来，交给IoC容器负责。容器全权负
责组件的装配，它会把符合依赖关系的对象通过属性（JavaBean中的setter）或者是构造器传递给需要
的对象。

### 有哪些不同类型的依赖注入实现方式

依赖注入是时下最流行的IoC实现方式，依赖注入分为接口注入（Interface Injection），Setter方法注
入（Setter Injection）和构造器注入（Constructor Injection）三种方式。其中接口注入由于在灵活
性和易用性比较差，现在从Spring4开始已被废弃。

构造器依赖注入：构造器依赖注入通过容器触发一个类的构造器来实现的，该类有一系列参数，每个参
数代表一个对其他类的依赖。

Setter方法注入：Setter方法注入是容器通过调用无参构造器或无参static工厂 方法实例化bean之后，
调用该bean的setter方法，即实现了基于setter的依赖注入。

### 构造器依赖注入和 Setter方法注入的区别

### Spring框架中的单例bean是线程安全的吗

不是，Spring框架中的单例bean不是线程安全的。

spring 中的 bean 默认是单例模式，spring 框架并没有对单例 bean 进行多线程的封装处理。

实际上大部分时候 spring bean 无状态的（比如 dao 类），所有某种程度上来说 bean 也是安全的，但
如果 bean 有状态的话（比如 view model 对象），那就要开发者自己去保证线程安全了，最简单的就
是改变 bean 的作用域，把“singleton”变更为“prototype”，这样请求 bean 相当于 new Bean()了，所
以就可以保证线程安全了。

有状态就是有数据存储功能。

无状态就是不会保存数据。

### Spring如何处理线程并发问题

在一般情况下，只有无状态的Bean才可以在多线程环境下共享，在Spring中，绝大部分Bean都可以声
明为singleton作用域，因为Spring对一些Bean中非线程安全状态采用ThreadLocal进行处理，解决线
程安全问题。

ThreadLocal和线程同步机制都是为了解决多线程中相同变量的访问冲突问题。同步机制采用了“时间换
空间”的方式，仅提供一份变量，不同的线程在访问前需要获取锁，没获得锁的线程则需要排队。而
ThreadLocal采用了“空间换时间”的方式。

ThreadLocal会为每一个线程提供一个独立的变量副本，从而隔离了多个线程对数据的访问冲突。因为
每一个线程都拥有自己的变量副本，从而也就没有必要对该变量进行同步了。ThreadLocal提供了线程
安全的共享对象，在编写多线程代码时，可以把不安全的变量封装进ThreadLocal。

### 什么是Spring的内部bean

在Spring框架中，当一个bean仅被用作另一个bean的属性时，它能被声明为一个内部bean。内部
bean可以用setter注入“属性”和构造方法注入“构造参数”的方式来实现，内部bean通常是匿名的，它们
的Scope一般是prototype。

```java
<bean id="outerBean" class="com.example.OuterBean">
    <!-- 内部Bean -->
    <property name="innerBean">
        <bean class="com.example.InnerBean">
            <property name="message" value="Hello, Spring!" />
        </bean>
    </property>
</bean>
```

* 内部 Bean 不能被其他 Bean 引用，只能作为外部 Bean 的属性使用。
* 内部 Bean 的生命周期由外部 Bean 管理，外部 Bean 销毁时，内部 Bean 也会被销毁。
* 内部 Bean 可以是任何类型的 Bean，包括单例、原型等。
### spring 自动装配 bean 有哪些方式
在Spring框架xml配置中共有5种自动装配：

1. no：默认的方式是不进行自动装配的，通过手工设置ref属性来进行装配bean。
2. byName：通过bean的名称进行自动装配，如果一个bean的 property 与另一bean 的name 相
同，就进行自动装配。
3. byType：通过参数的数据类型进行自动装配。
4. constructor：利用构造函数进行装配，并且构造函数的参数通过byType进行装配。
5. ***autodetect***：自动探测，如果有构造方法，通过 construct的方式自动装配，否则使用 byType的
方式自动装配。
### 使用@Autowired注解自动装配的过程是怎样的

使用@Autowired注解来自动装配指定的bean。在使用@Autowired注解之前需要在Spring配置文件进
行配置，<context:annotation-config />。

在启动spring IoC时，容器自动装载了一个AutowiredAnnotationBeanPostProcessor后置处理器，当
容器扫描到@Autowied、@Resource或@Inject时，就会在IoC容器自动查找需要的bean，并装配给该
对象的属性。在使用@Autowired时，首先在容器中查询对应类型的bean：

* 如果查询结果刚好为一个，就将该bean装配给@Autowired指定的数据；
* 如果查询的结果不止一个，那么@Autowired会根据名称来查找；
* 如果上述查找的结果为空，那么会抛出异常。解决方法时，使用required=false。
### 自动装配有哪些局限性

自动装配的局限性是：

***重写***：你仍需用 和 配置来定义依赖，意味着总要重写自动装配。
***基本数据类型***：你不能自动装配简单的属性，如基本数据类型，String字符串，和类。
***模糊特性***：自动装配不如显式装配精确，如果有可能，建议使用显式装配。

### 你可以在Spring中注入一个null 和一个空字符串吗

#### XML 配置

```java
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
                           http://www.springframework.org/schema/beans/spring-beans.xsd">

    <bean id="exampleBean" class="com.example.ExampleBean">
        <!-- 注入 null -->
        <property name="nullProperty">
            <null/>
        </property>
        <!-- 注入空字符串 -->
        <property name="emptyStringProperty" value=""/>
    </bean>
</beans>
```
#### Java 注解配置


```java
package com.example;

public class ExampleBean {
    private Object nullProperty;
    private String emptyStringProperty;

    public void setNullProperty(Object nullProperty) {
        this.nullProperty = nullProperty;
    }

    public void setEmptyStringProperty(String emptyStringProperty) {
        this.emptyStringProperty = emptyStringProperty;
    }
    // 可以添加getter方法用于测试
    public Object getNullProperty() {
        return nullProperty;
    }

    public String getEmptyStringProperty() {
        return emptyStringProperty;
    }
}
```
```java
package com.example;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public ExampleBean exampleBean() {
        ExampleBean exampleBean = new ExampleBean();
        // 注入 null
        exampleBean.setNullProperty(null);
        // 注入空字符串
        exampleBean.setEmptyStringProperty("");
        return exampleBean;
    }
}
```
```java
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        ExampleBean exampleBean = context.getBean(ExampleBean.class);
        System.out.println("nullProperty: " + exampleBean.getNullProperty());
        System.out.println("emptyStringProperty: " + exampleBean.getEmptyStringProperty());
        context.close();
    }
}
```


