## 在spring中，定义bean的初始化和销毁方法有三种方法

1. 在xml文件中制定init-method和destroy-method
2. 继承自InitializingBean和DisposableBean
3. 在方法上加注解PostConstruct和PreDestroy