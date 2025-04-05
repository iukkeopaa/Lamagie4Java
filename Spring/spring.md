## Spring有哪些模块

### 核心容器（Core Container）

- **Spring Core**：Spring 框架的基础，提供了 IoC（控制反转）和 DI（依赖注入）功能。它负责对象的创建、管理和依赖关系的注入，是整个 Spring 框架的核心。
- **Spring Beans**：基于 Spring Core 实现，定义了 Bean 的创建、配置和管理机制。它允许开发者通过 XML、Java 注解或 Java 代码来配置 Bean。
- **Spring Context**：建立在 Core 和 Beans 模块之上，提供了一个应用程序上下文环境。它允许开发者以一种更高级的方式管理对象，例如支持国际化、事件传播、资源加载等。
- **Spring Expression Language (SpEL)**：一种强大的表达式语言，允许在运行时查询和操作对象图。它可以用于配置 Bean 的属性、在 XML 或注解中进行表达式求值等。

### 数据访问 / 集成（Data Access/Integration）

- **Spring JDBC**：简化了 JDBC（Java Database Connectivity）的使用，提供了一致的数据库访问接口，减少了样板代码。
- **Spring ORM**：提供了与各种 ORM（对象关系映射）框架的集成，如 Hibernate、JPA（Java Persistence API）等，使得开发者可以方便地使用这些框架进行数据库操作。
- **Spring OXM**：支持对象 / XML 映射，允许开发者将 Java 对象与 XML 数据进行相互转换。
- **Spring Transactions**：提供了声明式和编程式的事务管理功能，确保数据库操作的一致性和完整性。

### Web 模块（Web）

- **Spring Web**：提供了基本的 Web 开发支持，包括处理 HTTP 请求、文件上传、Servlet 集成等。
- **Spring Web MVC**：基于 Servlet API 实现的 MVC（Model-View-Controller）框架，用于构建 Web 应用程序。它提供了强大的请求映射、视图解析、数据绑定等功能。
- **Spring WebFlux**：一个响应式 Web 框架，用于构建非阻塞、异步的 Web 应用程序。它基于 Reactor 库，支持高并发和低延迟的应用场景。

### AOP 模块（Aspect Oriented Programming）

- **Spring AOP**：支持面向切面编程，允许开发者将横切关注点（如日志记录、事务管理等）从业务逻辑中分离出来，提高代码的可维护性和可复用性。
- **Spring Aspects**：提供了与 AspectJ 框架的集成，使得开发者可以使用 AspectJ 的强大功能进行 AOP 编程。

### 测试模块（Testing）

- **Spring Test**：提供了测试 Spring 应用程序的工具和支持，包括集成测试、单元测试等。它允许开发者在测试环境中模拟 Spring 容器和 Bean，方便进行测试。

### 其他模块

- **Spring Messaging**：支持消息传递和消息驱动的应用程序开发，包括 JMS（Java Message Service）、AMQP（Advanced Message Queuing Protocol）等。
- **Spring Batch**：用于处理大量数据的批处理任务，提供了任务调度、数据读取、处理和写入等功能。