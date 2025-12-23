# Code Examples: Advanced Bean Features

## 1. Prototype Scope Example

### Before (No Prototype Support)
```java
// Every retrieval returns the same instance
Person p1 = context.getBean("person", Person.class);
Person p2 = context.getBean("person", Person.class);
System.out.println(p1 == p2); // true (singleton)
```

### After (With Prototype Support)
```java
// New class for prototype demonstration
@Bean
@Scope("prototype")
public PrototypeService prototypeService(){
    return new PrototypeService();
}

// Usage
PrototypeService s1 = context.getBean(PrototypeService.class);
PrototypeService s2 = context.getBean(PrototypeService.class);
System.out.println(s1.getInstanceId().equals(s2.getInstanceId())); // false (different instances)
```

---

## 2. Lazy Initialization Example

### Before (Eager Loading)
```java
// Bean created at startup, even if never used
@Bean
public Vehicle vehicle() {
    System.out.println("Creating vehicle...");
    return new Vehicle();
}
```

### After (Lazy Loading)
```java
// Bean created only when first requested
@Bean
@Lazy
public Vehicle lazyVehicle(){
    System.out.println("Creating lazy vehicle bean...");
    var veh = new Vehicle();
    veh.setName("Lazy Loaded Tesla Model 3");
    return veh;
}

// Usage
// No output until first retrieval:
Vehicle lazyVehicle = context.getBean("lazyVehicle", Vehicle.class);
// Now output: "Creating lazy vehicle bean..."
```

---

## 3. Factory Pattern Example

### Before (Direct Instantiation)
```java
@Bean
public Vehicle premiumVehicle() {
    var v = new Vehicle();
    v.setName("Premium Vehicle");
    return v;
}

@Bean
public Vehicle economyVehicle() {
    var v = new Vehicle();
    v.setName("Economy Vehicle");
    return v;
}
// Repetitive code!
```

### After (Factory Pattern)
```java
// Factory class
public class VehicleFactory {
    public static Vehicle createPremiumVehicle() {
        System.out.println("Factory: Creating premium vehicle...");
        var vehicle = new Vehicle();
        vehicle.setName("Premium Vehicle - Created by Factory");
        return vehicle;
    }

    public static Vehicle createEconomyVehicle() {
        System.out.println("Factory: Creating economy vehicle...");
        var vehicle = new Vehicle();
        vehicle.setName("Economy Vehicle - Created by Factory");
        return vehicle;
    }
}

// Bean configuration (cleaner)
@Bean
public Vehicle factoryVehicle(){
    return VehicleFactory.createPremiumVehicle();
}
```

---

## 4. Circular Dependency Resolution

### Before (Broken)
```java
// Person.java
@Component
public class Person {
    @Autowired
    private Person spouse; // Circular dependency!
}

// At startup: ERROR - Circular dependency!
// Spring cannot initialize this bean.
```

### After (Fixed with @Lazy)
```java
// Person.java
@Component
public class Person {
    @Autowired
    @Lazy  // ← Magic fix!
    private Person spouse; // No longer circular
}

// At startup: SUCCESS - spouse is lazily initialized
// When accessed: Person spouse = person.getSpouse();
```

### How It Works - Timeline

**Without @Lazy:**
```
Spring Start
  ↓
Create Person A
  ↓
Inject spouse (Person B)
  ↓
Create Person B
  ↓
Inject spouse (Person A)  ← DEADLOCK! Person A not finished
  ↓
ERROR
```

**With @Lazy:**
```
Spring Start
  ↓
Create Person A
  ↓
Skip spouse injection (Lazy) ← Key difference
  ↓
Person A fully created ✓
  ↓
Create Person B
  ↓
Skip spouse injection (Lazy)
  ↓
Person B fully created ✓
  ↓
Later: person.getSpouse() creates proxy when needed
```

---

## 5. Error Handling Examples

### NoUniqueBeanDefinitionException

```java
// Multiple beans exist
@Bean public Vehicle vehicle1() { ... }
@Bean public Vehicle vehicle2() { ... }
@Bean public Vehicle vehicle3() { ... }

// WRONG: Which one to return?
Vehicle vehicle = context.getBean(Vehicle.class);  // ERROR!

// CORRECT: Use @Primary
@Primary
@Bean
public Vehicle vehicle1() { ... }  // This one is default

// OR use @Qualifier
Vehicle vehicle2 = context.getBean("vehicle2", Vehicle.class);
```

### NoSuchBeanDefinitionException

```java
// Bean doesn't exist
try {
    String notFound = context.getBean("nonExistent", String.class);
} catch (NoSuchBeanDefinitionException e) {
    System.out.println("Bean not found: " + e.getMessage());
}

// BETTER: Pre-check
if (context.containsBean("myBean")) {
    MyBean bean = context.getBean("myBean", MyBean.class);
} else {
    System.out.println("Bean not available");
}
```

### Resource Cleanup

```java
// WRONG: Resource leak
AnnotationConfigApplicationContext context = 
    new AnnotationConfigApplicationContext(ProjectConfig.class);
// Use context...
// context never closed!

// CORRECT: Always close
AnnotationConfigApplicationContext context = null;
try {
    context = new AnnotationConfigApplicationContext(ProjectConfig.class);
    // Use context...
} catch (Exception e) {
    // Handle error
} finally {
    if (context != null) {
        context.close();  // Release resources
    }
}

// EVEN BETTER: Try-with-resources
try (AnnotationConfigApplicationContext context = 
     new AnnotationConfigApplicationContext(ProjectConfig.class)) {
    // Use context...
} // Auto-closed
```

---

## 6. Complete Demonstration Flow

### Main.java Organization

```java
public class Main {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = null;
        
        try {
            // Initialize
            context = new AnnotationConfigApplicationContext(ProjectConfig.class);
            
            // 9 demonstrations
            demonstrateBasicBeanCreation(context);           // 1
            demonstrateErrorHandling(context);               // 2
            demonstrateSingletonScope(context);              // 3
            demonstratePrototypeScope(context);              // 4
            demonstrateLazyInitialization(context);          // 5
            demonstrateFactoryPattern(context);              // 6
            demonstrateCircularDependency(context);          // 7
            demonstrateBeanDiscovery(context);               // 8
            demonstrateQualifierAndPrimary(context);         // 9
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (context != null) {
                context.close();  // Always cleanup
            }
        }
    }
}
```

---

## 7. ProjectConfig.java Changes

### Original Configuration
```java
@Configuration
@ComponentScan(basePackages = { "org.example.implementations", ... })
public class ProjectConfig {
    
    @Primary
    @Bean
    Vehicle vehicle1(){ ... }
    
    @Bean
    Vehicle vehicle2(){ ... }
    
    @Bean
    Vehicle vehicle3(){ ... }
    
    // ... more beans
}
```

### Enhanced Configuration
```java
@Configuration
@ComponentScan(basePackages = { "org.example.implementations", ... })
public class ProjectConfig {
    
    // Original beans...
    
    // NEW: Prototype scope bean
    @Bean
    @Scope("prototype")
    public PrototypeService prototypeService(){
        return new PrototypeService();
    }
    
    // NEW: Lazy initialization
    @Bean
    @Lazy
    public Vehicle lazyVehicle(){
        System.out.println("Creating lazy vehicle bean...");
        var veh = new Vehicle();
        veh.setName("Lazy Loaded Tesla Model 3");
        return veh;
    }
    
    // NEW: Factory pattern
    @Bean
    public Vehicle factoryVehicle(){
        return VehicleFactory.createPremiumVehicle();
    }
    
    // NEW: Prototype template
    @Bean
    @Scope("prototype")
    public Person prototypePersonTemplate(){
        return new Person("Template Person", 0, vehicle1());
    }
}
```

---

## 8. Key Takeaways

| Concept | Use Case | Example |
|---------|----------|---------|
| **Singleton** | Stateless services | PersonService, DatabaseConnection |
| **Prototype** | Stateful objects | RequestContext, PrototypeService |
| **Lazy** | Expensive beans | DataCache, ThirdPartyClient |
| **Factory** | Complex init | VehicleFactory, ConfigurationFactory |
| **@Primary** | Single obvious choice | PrimaryDatabaseConnection |
| **@Qualifier** | Multiple implementations | @Qualifier("mysql") vs @Qualifier("postgres") |
| **@Lazy + Circular** | Circular deps | Person ↔ Spouse relationships |

---

## 9. Testing the Implementation

```java
// Test Singleton
Person p1 = context.getBean("person", Person.class);
Person p2 = context.getBean("person", Person.class);
assert p1 == p2; // Same instance

// Test Prototype
PrototypeService s1 = context.getBean(PrototypeService.class);
PrototypeService s2 = context.getBean(PrototypeService.class);
assert s1 != s2; // Different instances
assert !s1.getInstanceId().equals(s2.getInstanceId()); // Different IDs

// Test Lazy (no output until accessed)
Vehicle lazyVehicle = context.getBean("lazyVehicle", Vehicle.class);
// Output appears here

// Test Circular Dependency (no error at startup)
Person person1 = context.getBean("person", Person.class);
Person spouse = person1.getSpouse(); // Works!
```

---

This document provides complete code examples for all advanced features implemented in the basics project.
