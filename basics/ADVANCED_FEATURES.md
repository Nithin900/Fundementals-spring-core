# Basics Project - Advanced Features Implementation

## Summary of Additions

This document outlines all the advanced Spring Framework features and error handling that have been added to the `basics` project.

---

## 1. **Advanced Bean Features**

### 1.1 Prototype Scope Beans
**File**: [PrototypeService.java](src/main/java/org/example/beans/PrototypeService.java)

- **What**: A bean that creates a new instance every time it's requested
- **Why**: Useful for stateful beans that shouldn't be shared
- **Configuration**: `@Scope("prototype")`
- **Example**:
  ```java
  @Bean
  @Scope("prototype")
  public PrototypeService prototypeService(){
      return new PrototypeService();
  }
  ```
- **Behavior**: Each call to `context.getBean(PrototypeService.class)` returns a different instance with a unique ID

### 1.2 Lazy Initialization
**File**: [ProjectConfig.java](src/main/java/org/example/config/ProjectConfig.java)

- **What**: Beans that are created only when first requested, not at application startup
- **Why**: Improves startup performance for expensive operations
- **Configuration**: `@Lazy`
- **Example**:
  ```java
  @Bean
  @Lazy
  public Vehicle lazyVehicle(){
      System.out.println("Creating lazy vehicle bean...");
      var veh = new Vehicle();
      veh.setName("Lazy Loaded Tesla Model 3");
      return veh;
  }
  ```
- **Benefit**: Heavy initialization is deferred until the bean is actually needed

### 1.3 Factory Pattern
**Files**: 
- [VehicleFactory.java](src/main/java/org/example/config/VehicleFactory.java)
- [ProjectConfig.java](src/main/java/org/example/config/ProjectConfig.java)

- **What**: Creating beans through factory methods instead of direct instantiation
- **Why**: Allows complex initialization logic, encapsulation, and flexibility
- **Example**:
  ```java
  @Bean
  public Vehicle factoryVehicle(){
      return VehicleFactory.createPremiumVehicle();
  }
  ```
- **Use Case**: When bean creation requires conditional logic or multiple steps

---

## 2. **Comprehensive Error Handling**

**File**: [Main.java](src/main/java/org/example/Main.java)

### 2.1 NoUniqueBeanDefinitionException
```java
// Problem: Multiple beans of same type exist
Vehicle vehicle = context.getBean(Vehicle.class);
// Solution: Use @Primary or specify bean name
Vehicle vehicle1 = context.getBean("vehicle1", Vehicle.class);
```

### 2.2 NoSuchBeanDefinitionException
```java
try {
    context.getBean("nonExistentBean", String.class);
} catch (NoSuchBeanDefinitionException e) {
    System.out.println("Bean not found: " + e.getMessage());
}
```

### 2.3 Pre-checking Bean Existence
```java
if (context.containsBean("bye")) {
    String bye = context.getBean("bye", String.class);
    System.out.println("Bean exists: '" + bye + "'");
} else {
    System.out.println("Bean doesn't exist");
}
```

### 2.4 Resource Cleanup
```java
AnnotationConfigApplicationContext context = null;
try {
    context = new AnnotationConfigApplicationContext(ProjectConfig.class);
    // use context
} catch (Exception e) {
    // handle error
} finally {
    if (context != null) {
        context.close(); // Always release resources
    }
}
```

---

## 3. **Circular Dependency Resolution**

**Files**:
- [Person.java](src/main/java/org/example/beans/Person.java)
- [Main.java](src/main/java/org/example/Main.java)

### Problem
```java
// Person -> Spouse (Person) -> creates circular dependency
@Autowired
private Person spouse;

@Autowired
private Person spouse; // Circular!
```

### Solution: @Lazy Annotation
```java
@Autowired
@Lazy  // ← This breaks the circular reference at initialization
private Person spouse;
```

### How It Works
1. When Spring creates Person A, it doesn't immediately resolve Person B (spouse)
2. Person B is created only when first accessed via `person1.getSpouse()`
3. By this time, Person A is already fully constructed
4. No deadlock occurs

### Demonstration in Main.java
```java
Person person1 = context.getBean("person", Person.class);
Person person2 = context.getBean("person2", Person.class);

// Safely access spouse without circular dependency issues
person1.getSpouse(); // Returns person2
person2.getSpouse(); // Can safely reference back
```

---

## 4. **Complete Main.java Features**

The new [Main.java](src/main/java/org/example/Main.java) provides:

1. **Organized Demonstrations**: 9 different methods showcasing different concepts
2. **Professional Structure**: Try-catch-finally with proper resource management
3. **Instance Identity Verification**: Using `System.identityHashCode()` to prove singleton vs prototype
4. **Comprehensive Error Handling**: Safe bean lookups with error messages
5. **Visual Output**: ✓ and ✗ symbols for clarity

### Run Method Structure
```
1. Basic Bean Creation
2. Error Handling & Bean Lookup
3. Singleton Scope (Default)
4. Prototype Scope
5. Lazy Initialization
6. Factory Pattern
7. Circular Dependency Resolution
8. Bean Discovery & Retrieval
9. Qualifier & Primary Annotations
```

---

## 5. **Key Files Added/Modified**

| File | Type | Purpose |
|------|------|---------|
| `PrototypeService.java` | New | Demonstrates prototype scope |
| `VehicleFactory.java` | New | Factory pattern example |
| `ProjectConfig.java` | Modified | Added advanced bean configurations |
| `Person.java` | Modified | Improved circular dependency handling |
| `Main.java` | Completely Rewritten | Comprehensive demonstration application |

---

## 6. **Running the Application**

### Prerequisites
- Java 17+
- Maven 3.6+
- Spring Framework 6.1.6

### Compile
```bash
cd basics
mvn clean compile
```

### Run
```bash
mvn exec:java -Dexec.mainClass="org.example.Main"
```

### Expected Output
```
========== SPRING CORE PRACTICE APPLICATION ==========

---------- 1. BASIC BEAN CREATION ----------
Creating Vehicle without Spring:
  Manual creation: Honda City

Fetching Vehicle from Spring context:
  Spring bean: Volkswagen Golf

✓ Spring context initialized successfully

---------- 2. ERROR HANDLING & BEAN LOOKUP ----------
1. Handling NoUniqueBeanDefinitionException:
   ✓ Got primary vehicle: Volkswagen Golf
...
```

---

## 7. **Learning Outcomes**

After studying this enhanced project, you will understand:

✓ How Spring manages bean lifecycles (Singleton vs Prototype)  
✓ How to optimize startup time with `@Lazy`  
✓ How to implement factory patterns with Spring  
✓ How to handle Spring exceptions properly  
✓ How to resolve circular dependencies  
✓ How to use `@Primary` and `@Qualifier` for bean disambiguation  
✓ How to write robust, production-ready Spring applications  
✓ How to discover and retrieve beans at runtime  

---

## 8. **Best Practices Demonstrated**

1. **Always close ApplicationContext**: Use try-finally blocks
2. **Pre-check bean existence**: Use `containsBean()` before retrieval
3. **Use @Lazy for circular dependencies**: Better than ObjectProvider
4. **Use @Primary for single obvious default**: When only one bean makes sense
5. **Use @Qualifier for specific selection**: When multiple implementations exist
6. **Factory methods for complex initialization**: When simple instantiation isn't enough
7. **Prototype scope for stateful beans**: Avoid shared state issues
8. **Meaningful bean names**: Makes debugging easier

---

## Advanced Topics for Further Learning

- [ ] ObjectProvider for optional dependencies
- [ ] BeanFactory vs ApplicationContext
- [ ] Bean PostProcessors
- [ ] Event-driven Architecture with ApplicationEvents
- [ ] Bean Validation with JSR-380
- [ ] Integration with other frameworks

