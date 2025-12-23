# Quick Reference Guide

## 📊 Files Added & Modified

### ✨ New Files Created

| File | Lines | Purpose |
|------|-------|---------|
| `PrototypeService.java` | 28 | Demonstrates @Scope("prototype") |
| `VehicleFactory.java` | 20 | Factory pattern for bean creation |
| `ADVANCED_FEATURES.md` | 250+ | Comprehensive documentation |
| `IMPLEMENTATION_SUMMARY.md` | 200+ | Summary of all changes |
| `CODE_EXAMPLES.md` | 300+ | Side-by-side code examples |

### 🔄 Files Modified

| File | Changes | Lines Changed |
|------|---------|----------------|
| `Main.java` | Complete rewrite | 330+ lines |
| `ProjectConfig.java` | Added imports + 4 beans | 30+ lines |
| `Person.java` | Enhanced documentation | 15+ lines |

---

## 🎯 Nine Demonstration Methods

```java
1️⃣ demonstrateBasicBeanCreation()
   └─ Manual vs Spring creation

2️⃣ demonstrateErrorHandling()
   ├─ NoUniqueBeanDefinitionException
   ├─ NoSuchBeanDefinitionException
   ├─ Safe bean retrieval
   └─ Pre-checking with containsBean()

3️⃣ demonstrateSingletonScope()
   └─ Same instance proof (identity hashcode)

4️⃣ demonstratePrototypeScope()
   └─ New instances with UUID verification

5️⃣ demonstrateLazyInitialization()
   └─ Deferred bean creation

6️⃣ demonstrateFactoryPattern()
   └─ Factory method pattern usage

7️⃣ demonstrateCircularDependency()
   └─ @Lazy resolution technique

8️⃣ demonstrateBeanDiscovery()
   └─ Bulk retrieval + bean counting

9️⃣ demonstrateQualifierAndPrimary()
   └─ Annotation-based disambiguation
```

---

## 🔍 Quick Lookup Table

### Annotations Added

```java
@Scope("prototype")          // New scope for PrototypeService
@Lazy                        // New lazy init for lazyVehicle
// Already existed but now better documented:
@Primary                     // vehicle1 bean
@Qualifier("...")            // Employee bean selection
```

### Bean Configurations Added

```java
// 1. Prototype Scope
@Bean @Scope("prototype")
public PrototypeService prototypeService() { ... }

// 2. Lazy Initialization  
@Bean @Lazy
public Vehicle lazyVehicle() { ... }

// 3. Factory Pattern
@Bean
public Vehicle factoryVehicle() { ... }

// 4. Prototype Template
@Bean @Scope("prototype")
public Person prototypePersonTemplate() { ... }
```

---

## 🚀 How to Run

### Step 1: Compile
```bash
cd basics
mvn clean compile
```

### Step 2: Run
```bash
mvn exec:java -Dexec.mainClass="org.example.Main"
```

### Step 3: Observe Output
- 9 demonstration sections
- Visual indicators (✓/✗)
- Instance verification
- Error handling examples

---

## 📋 Circular Dependency Quick Fix

### Problem
```java
Person person1 → spouse (Person person2)
Person person2 → spouse (Person person1)
// DEADLOCK at startup
```

### Solution
```java
@Autowired
@Lazy  // ← Add this
private Person spouse;
// Resolved! No deadlock
```

### Why It Works
1. `@Lazy` delays spouse initialization
2. Person A created successfully
3. Person B created successfully  
4. Spouse accessed later (safe by then)

---

## 🎓 Learning Checklist

- [ ] Understand Singleton scope (default)
- [ ] Understand Prototype scope (new instance each time)
- [ ] Know when to use @Lazy (expensive operations)
- [ ] Implement Factory pattern (complex initialization)
- [ ] Handle NoUniqueBeanDefinitionException
- [ ] Handle NoSuchBeanDefinitionException  
- [ ] Always close ApplicationContext (resource leak)
- [ ] Use @Primary (single obvious default)
- [ ] Use @Qualifier (disambiguation)
- [ ] Resolve circular dependencies with @Lazy
- [ ] Discover beans at runtime (getBeansOfType)
- [ ] Verify bean instances (System.identityHashCode)

---

## 💡 Most Important Concepts

### 1. Singleton (Default)
```java
Person p1 = context.getBean("person", Person.class);
Person p2 = context.getBean("person", Person.class);
System.out.println(p1 == p2); // true - same instance!
```

### 2. Prototype
```java
PrototypeService s1 = context.getBean(PrototypeService.class);
PrototypeService s2 = context.getBean(PrototypeService.class);
System.out.println(s1 == s2); // false - different instances!
```

### 3. Lazy
```java
// Not created until accessed:
Vehicle lazy = context.getBean("lazyVehicle", Vehicle.class);
// Output: "Creating lazy vehicle bean..." appears HERE
```

### 4. Circular Dependency
```java
// WITHOUT @Lazy: ERROR at startup
// WITH @Lazy: Works perfectly
@Autowired @Lazy
private Person spouse;
```

### 5. Error Handling
```java
if (context.containsBean("myBean")) {
    MyBean bean = context.getBean("myBean", MyBean.class);
} else {
    System.out.println("Not available");
}
```

---

## 🔗 File Dependencies

```
ProjectConfig.java
├─ imports PrototypeService
├─ imports VehicleFactory  
├─ configures all beans
└─ used by Main.java

Main.java
├─ imports ProjectConfig
├─ imports all bean classes
├─ 9 demonstration methods
└─ proper resource management

Person.java
├─ @Lazy spouse (circular dependency fix)
├─ @Autowired vehicle
└─ proper documentation

PrototypeService.java
└─ simple demo bean

VehicleFactory.java
└─ factory methods
```

---

## ⏱️ Startup Flow

```
1. Main.main() starts
2. AnnotationConfigApplicationContext initialized
3. ProjectConfig processed
   ├─ @Bean methods executed (except @Lazy)
   ├─ @Component classes scanned
   ├─ @Autowired resolved
   └─ Singleton beans created
4. @Lazy beans NOT created yet
5. Demonstrations start
6. demonstrateLazyInitialization() triggers lazy bean creation
7. demonstrateCircularDependency() accesses @Lazy spouse
8. context.close() cleans up resources
```

---

## 🐛 Common Mistakes & Fixes

### ❌ Not closing context
```java
// WRONG
context = new AnnotationConfigApplicationContext(...);
// Forgot to close!
```

### ✅ Proper cleanup
```java
// RIGHT
try {
    context = new AnnotationConfigApplicationContext(...);
} finally {
    if (context != null) context.close();
}
```

---

### ❌ Circular dependency without @Lazy
```java
// WRONG
@Autowired
private Person spouse; // Circular with Person -> spouse
```

### ✅ Fixed with @Lazy
```java
// RIGHT
@Autowired
@Lazy
private Person spouse; // No circular dependency!
```

---

### ❌ Multiple beans without @Primary
```java
// WRONG
Vehicle v = context.getBean(Vehicle.class);
// ERROR: NoUniqueBeanDefinitionException
```

### ✅ Use @Primary or @Qualifier
```java
// RIGHT
Vehicle v = context.getBean("vehicle1", Vehicle.class);
// or use @Primary on one bean
```

---

## 📈 Complexity by Section

```
Easy:
  - Basic Bean Creation
  - Singleton Scope
  - @Primary / @Qualifier

Medium:
  - Error Handling
  - Bean Discovery
  - Lazy Initialization

Hard:
  - Prototype Scope
  - Factory Pattern
  - Circular Dependency Resolution
```

---

## 🎯 Project Structure After Changes

```
basics/
├── src/main/java/org/example/
│   ├── Main.java (REWRITTEN - 330 lines)
│   ├── beans/
│   │   ├── Person.java (ENHANCED - circular dependency)
│   │   ├── Vehicle.java
│   │   └── PrototypeService.java (NEW)
│   ├── config/
│   │   ├── ProjectConfig.java (ENHANCED - 4 new beans)
│   │   └── VehicleFactory.java (NEW)
│   ├── implementations/
│   ├── interfaces/
│   └── services/
├── pom.xml (unchanged)
├── ADVANCED_FEATURES.md (NEW)
├── IMPLEMENTATION_SUMMARY.md (NEW)
└── CODE_EXAMPLES.md (NEW)
```

---

## 🏆 Achievement Unlocked

By completing this project, you now understand:

✅ Bean lifecycle and scopes  
✅ Dependency injection techniques  
✅ Error handling in Spring  
✅ Circular dependency resolution  
✅ Factory patterns  
✅ Bean discovery and retrieval  
✅ Resource management best practices  
✅ Professional Spring application structure  

---

**Last Updated**: December 22, 2025  
**Status**: ✅ Complete - All features implemented and documented
