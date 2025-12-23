# Spring-Core practices

## Tools and Technologies
**Spring Framework:** Version 6	
**Build tool:** Maven	
**Java:** Version 17	
**IDE:** IntelliJ Idea	

## Project Descriptions
**Project Name:** basics

This project contains practice of various things including:
- Using Spring Context
- Creating beans using annotations such as @Bean, @Component, @Configuration
- Providing custom name to a bean by @Bean annotation
- Used @Qualifier to disambiguated beans
- Handling NoUniqueBeanDefinitionException with @Primary
- Utilizing ComponentScan and @Autowired for bean discovery and dependency injection
- Fetching a bean from context
- Practiced circular dependency

---

**Project Name:** lifecyle-methods	

- This is a Java application that utilizes Spring Framework to manage a database connection using JDBC. 
- This project demonstrates the usage of Spring's lifecycle annotations (`@PostConstruct` and `@PreDestroy`) to handle the initialization and cleanup of resources. 

---

**Project Name:** spring-di

This project serves as a comprehensive guide to Dependency Injection (DI) within a Spring application, showcasing different implementation approaches through XML and Java configurations. 

**Ways of injecting dependencies:**
- **XML configurations:** Dependencies are defined and wired in XML files, allowing for a clear separation of concerns between configuration and code.
- **Java configurations:** Dependencies are configured programmatically using Java classes, offering flexibility and type safety.
- **Annotation-based:** Dependencies are declared using annotations like `@Autowired`, simplifying configuration and reducing boilerplate code.

**Types of DI:**
- **Constructor Injection:** Dependencies are provided through a class constructor, ensuring that all required dependencies are available when an object is created.
- **Setter Injection:** Dependencies are injected via setter methods, enabling more flexibility in changing dependencies at runtime without altering the constructor signature.
<!-- - **Interface Injection:** Dependencies are injected through an interface implemented by the class, though this approach is less common in Spring due to its reliance on runtime reflection. -->

**Ways of autowiring:**
Dependencies can be automatically wired by Spring based on predefined rules or annotations, reducing manual configuration efforts.
- **Using method parameter:** Dependencies are resolved based on method parameters, with Spring injecting appropriate beans.
- **Using method call:** Beans are instantiated and wired with method calls, often used in conjunction with method parameter autowiring.
- **@Autowired annotation:**
    - **Field Injection:** Dependencies are directly injected into fields of a class, offering simplicity but potentially sacrificing encapsulation.
    - **Constructor Injection:** Dependencies are injected via the constructor, promoting immutable objects and enhancing testability.
    - **Setter Injection:** Dependencies are set using setter methods, allowing for optional dependencies and easier modification post-construction.

**@ComponentScan:** Spring scans the specified packages for components and automatically registers them as beans, reducing manual bean registration overhead.

>Annotations used in Student class and others is for DI using annotation & java configurations only not for DI using xml configurations. <u>This is a practice project and I wanted to practice all types of injection in the same Student class to get a better idea of how things work.</u>

**Note:** Autowired annotation on variable/setters is equivalent to autowire="byType" and Autowired annotation on Constructor is equivalent to autowire="constructor".

**Note:** It's important to note that field injection is generally considered less preferable than constructor injection or setter injection due to potential issues with testability, encapsulation, and readability. It's often recommended to favor constructor injection or setter injection over field injection whenever possible.

```java
spring-di
.
├── pom.xml
├── src
│ ├── main
│ │ ├── java
│ │ │ └── org
│ │ │     └── example
│ │ │         ├── AddressService.java
│ │ │         ├── AddressServiceImpl.java
│ │ │         ├── Course.java
│ │ │         ├── Friend.java
│ │ │         ├── Grade.java
│ │ │         ├── Main.java
│ │ │         ├── SpringConfig.java
│ │ │         └── Student.java
│ │ └── resources
│ │     └── spring.core.config.xml
│ └── test
│     └── java
└── target
    ├── classes
    └── generated-sources
        └── annotations
  
```

---

**Project Name:** beans-scope

"beans-scope" is a project focusing on the fundamental aspects of Spring beans within the Spring framework. It covers essential concepts such as bean scopes (singleton and prototype), lazy vs. eager initialization, thread safety, etc.

- Practiced Singleton & Prototype bean scope
- Practiced Eager Initialization & Lazy Initialization

---

**Project Name:** aop

This project demonstrates comprehensive Aspect-Oriented Programming (AOP) in Spring, covering multiple cross-cutting concerns beyond logging.

**Core AOP Concepts:**
- Brief explanation of key AOP annotations (`@Before`, `@After`, `@Around`, `@AfterReturning`, `@AfterThrowing`) and their roles in intercepting method execution.
- Custom annotations and aspect-based method interception.
- Integration of logging framework (Java Util Logging).

**Implemented Aspects & Features:**

1. **Logging Aspect** (`LoggingAspect`)
   - Logs method entry/exit, return values, and exceptions.
   - Uses `@Before`, `@After`, `@AfterReturning`, `@AfterThrowing`, and `@Around` annotations.

2. **Custom Annotation Timing** (`CustomAnnotationAspect`)
   - Custom `@LogExecutionTime` annotation marks methods for execution time tracking.
   - Measures and logs method execution duration.

3. **Transactional Aspect** (`TransactionalAspect`)
   - Custom `@Transactional` annotation simulates transaction lifecycle.
   - Logs transaction BEGIN/COMMIT/ROLLBACK operations.

4. **Security Aspect** (`SecurityAspect`)
   - Custom `@RequireRole` annotation enforces role-based access control.
   - Uses `SecurityContext` to check current user role.
   - Throws `SecurityException` if user lacks required role.

5. **Metrics Aspect** (`MetricsAspect`)
   - Custom `@Metrics` annotation tracks method call counts and execution duration.
   - Maintains in-memory counters for method invocations.

6. **Audit Aspect** (`AuditAspect`)
   - Custom `@Audit` annotation logs method arguments and return values.
   - Tracks all service method calls for compliance/debugging.

7. **Retry Aspect** (`RetryAspect`)
   - Custom `@Retryable` annotation with configurable retry attempts and delay.
   - Automatically retries failed method executions with exponential backoff support.

8. **Invocation ID Tracing** (`InvocationIdAspect` & `InvocationContext`)
   - Generates unique per-invocation IDs (8-character UUID).
   - Stores ID in ThreadLocal for access across all aspects.
   - All logs/prints prefixed with `[invocationId]` for easy tracing of a single method call through the entire aspect chain.

**Annotations:**
- `@LogExecutionTime` — Track execution time
- `@Transactional` — Simulate transaction management
- `@RequireRole` — Enforce role-based access control
- `@Metrics` — Track invocation counts and performance
- `@Audit` — Log before/after method execution
- `@Retryable` — Auto-retry on failure

**Example Usage in BookServiceImpl:**
```java
@LogExecutionTime
@Transactional
@RequireRole("ADMIN")
@Metrics(name = "book.remove")
@Audit
@Retryable(attempts = 3, delayMs = 300)
public void removeBook(String title) { ... }
```

**Benefits for Beginners:**
- Unified logging with invocation IDs makes output tracing easy — each method call has a unique ID prefix.
- Learn multiple aspects in one project without verbose output confusion.
- Real-world cross-cutting concerns (security, transactions, metrics, audit, retry).

---
