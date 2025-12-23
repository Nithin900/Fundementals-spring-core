package org.example;

import java.util.Map;

import org.example.beans.Person;
import org.example.beans.Vehicle;
import org.example.beans.PrototypeService;
import org.example.config.ProjectConfig;
import org.example.services.EmployeeService;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Main application demonstrating Spring Framework fundamentals:
 * - Bean creation and configuration
 * - Dependency injection
 * - Singleton vs Prototype scopes
 * - Lazy initialization
 * - Circular dependency resolution
 * - Error handling
 * - Bean factory patterns
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("========== SPRING CORE PRACTICE APPLICATION ==========\n");

        AnnotationConfigApplicationContext context = null;
        
        try {
            // Initialize Spring context
            context = new AnnotationConfigApplicationContext(ProjectConfig.class);
            System.out.println("✓ Spring context initialized successfully\n");

            // ==================== 1. BASIC BEAN CREATION ====================
            System.out.println("---------- 1. BASIC BEAN CREATION ----------");
            demonstrateBasicBeanCreation(context);

            // ==================== 2. ERROR HANDLING ====================
            System.out.println("\n---------- 2. ERROR HANDLING & BEAN LOOKUP ----------");
            demonstrateErrorHandling(context);

            // ==================== 3. SINGLETON SCOPE ====================
            System.out.println("\n---------- 3. SINGLETON SCOPE (Default) ----------");
            demonstrateSingletonScope(context);

            // ==================== 4. PROTOTYPE SCOPE ====================
            System.out.println("\n---------- 4. PROTOTYPE SCOPE ----------");
            demonstratePrototypeScope(context);

            // ==================== 5. LAZY INITIALIZATION ====================
            System.out.println("\n---------- 5. LAZY INITIALIZATION ----------");
            demonstrateLazyInitialization(context);

            // ==================== 6. FACTORY PATTERN ====================
            System.out.println("\n---------- 6. FACTORY PATTERN ----------");
            demonstrateFactoryPattern(context);

            // ==================== 7. CIRCULAR DEPENDENCY RESOLUTION ====================
            System.out.println("\n---------- 7. CIRCULAR DEPENDENCY RESOLUTION ----------");
            demonstrateCircularDependency(context);

            // ==================== 8. BEAN DISCOVERY & RETRIEVAL ====================
            System.out.println("\n---------- 8. BEAN DISCOVERY & RETRIEVAL ----------");
            demonstrateBeanDiscovery(context);

            // ==================== 9. QUALIFIER & PRIMARY ====================
            System.out.println("\n---------- 9. QUALIFIER & PRIMARY ANNOTATIONS ----------");
            demonstrateQualifierAndPrimary(context);

        } catch (Exception e) {
            System.err.println("Fatal error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Always close the context to release resources
            if (context != null) {
                context.close();
                System.out.println("\n✓ Spring context closed successfully");
            }
        }
    }

    /**
     * Demonstrates basic bean creation and manual instantiation comparison
     */
    private static void demonstrateBasicBeanCreation(AnnotationConfigApplicationContext context) {
        System.out.println("Creating Vehicle without Spring:");
        Vehicle manualVehicle = new Vehicle();
        manualVehicle.setName("Honda City");
        System.out.println("  Manual creation: " + manualVehicle.getName());

        System.out.println("\nFetching Vehicle from Spring context:");
        Vehicle springVehicle = context.getBean("vehicle1", Vehicle.class);
        System.out.println("  Spring bean: " + springVehicle.getName());
    }

    /**
     * Demonstrates various error handling scenarios
     */
    private static void demonstrateErrorHandling(AnnotationConfigApplicationContext context) {
        // 1. Fetching bean without specifying name (when multiple exist)
        System.out.println("1. Handling NoUniqueBeanDefinitionException:");
        try {
            Vehicle vehicle = context.getBean(Vehicle.class);
            System.out.println("   ✓ Got primary vehicle: " + vehicle.getName());
        } catch (Exception e) {
            System.out.println("   ✗ Error: " + e.getClass().getSimpleName() + " - " + e.getMessage());
        }

        // 2. Fetching non-existent bean
        System.out.println("\n2. Handling NoSuchBeanDefinitionException:");
        try {
            context.getBean("nonExistentBean", String.class);
            System.out.println("   ✓ Bean found");
        } catch (NoSuchBeanDefinitionException e) {
            System.out.println("   ✗ Bean not found: " + e.getMessage());
        }

        // 3. Safe bean lookup with try-catch
        System.out.println("\n3. Safe bean retrieval with error handling:");
        try {
            String hello = context.getBean("hello", String.class);
            System.out.println("   ✓ String bean found: '" + hello + "'");
        } catch (NoSuchBeanDefinitionException e) {
            System.out.println("   ✗ String bean not found");
        }

        // 4. Using containsBean for pre-check
        System.out.println("\n4. Pre-checking bean existence:");
        if (context.containsBean("bye")) {
            String bye = context.getBean("bye", String.class);
            System.out.println("   ✓ 'bye' bean exists: '" + bye + "'");
        } else {
            System.out.println("   ✗ 'bye' bean doesn't exist");
        }
    }

    /**
     * Demonstrates Singleton scope (default) - same instance every time
     */
    private static void demonstrateSingletonScope(AnnotationConfigApplicationContext context) {
        System.out.println("Fetching same Singleton bean multiple times:");
        
        Person person1 = context.getBean("person", Person.class);
        Person person2 = context.getBean("person", Person.class);
        Person person3 = context.getBean("person", Person.class);

        System.out.println("  person1 instance: " + System.identityHashCode(person1));
        System.out.println("  person2 instance: " + System.identityHashCode(person2));
        System.out.println("  person3 instance: " + System.identityHashCode(person3));
        System.out.println("  All references equal? " + (person1 == person2 && person2 == person3) + " ✓");
        System.out.println("  This proves Singleton scope - only one instance exists!");
    }

    /**
     * Demonstrates Prototype scope - new instance every time
     */
    private static void demonstratePrototypeScope(AnnotationConfigApplicationContext context) {
        System.out.println("Fetching Prototype beans multiple times:");
        
        PrototypeService service1 = context.getBean(PrototypeService.class);
        PrototypeService service2 = context.getBean(PrototypeService.class);
        PrototypeService service3 = context.getBean(PrototypeService.class);

        System.out.println("\n  Service1: " + service1.getInstanceId());
        System.out.println("  Service2: " + service2.getInstanceId());
        System.out.println("  Service3: " + service3.getInstanceId());
        System.out.println("\n  All instances different? " + 
            (!service1.getInstanceId().equals(service2.getInstanceId()) && 
             !service2.getInstanceId().equals(service3.getInstanceId())) + " ✓");
        System.out.println("  This proves Prototype scope - new instance created each time!");
    }

    /**
     * Demonstrates Lazy initialization - bean created only when first accessed
     */
    private static void demonstrateLazyInitialization(AnnotationConfigApplicationContext context) {
        System.out.println("Lazy bean not created until first access:");
        System.out.println("  (Watch for 'Creating lazy vehicle bean...' message when we fetch it)");
        
        try {
            Vehicle lazyVehicle = context.getBean("lazyVehicle", Vehicle.class);
            System.out.println("  ✓ Lazy vehicle retrieved: " + lazyVehicle.getName());
        } catch (Exception e) {
            System.out.println("  ✗ Error: " + e.getMessage());
        }
    }

    /**
     * Demonstrates Factory pattern for bean creation
     */
    private static void demonstrateFactoryPattern(AnnotationConfigApplicationContext context) {
        System.out.println("Creating bean using Factory method:");
        
        try {
            Vehicle factoryVehicle = context.getBean("factoryVehicle", Vehicle.class);
            System.out.println("  ✓ Factory-created vehicle: " + factoryVehicle.getName());
        } catch (Exception e) {
            System.out.println("  ✗ Error: " + e.getMessage());
        }
    }

    /**
     * Demonstrates circular dependency resolution using @Lazy
     */
    private static void demonstrateCircularDependency(AnnotationConfigApplicationContext context) {
        System.out.println("Testing Circular Dependency Resolution:");
        System.out.println("  (Person -> Spouse -> Person would be circular)");
        
        Person person1 = context.getBean("person", Person.class);
        Person person2 = context.getBean("person2", Person.class);

        System.out.println("\n  Person 1: " + person1.getName() + ", Spouse: " + 
            (person1.getSpouse() != null ? person1.getSpouse().getName() : "None"));
        System.out.println("  Person 2: " + person2.getName() + ", Spouse: " + 
            (person2.getSpouse() != null ? person2.getSpouse().getName() : "None"));
        System.out.println("\n  ✓ Circular dependency resolved using @Lazy annotation!");
    }

    /**
     * Demonstrates bean discovery and bulk retrieval
     */
    private static void demonstrateBeanDiscovery(AnnotationConfigApplicationContext context) {
        System.out.println("1. Getting all Vehicle beans:");
        Map<String, Vehicle> vehicles = context.getBeansOfType(Vehicle.class);
        vehicles.forEach((name, bean) -> 
            System.out.println("   - " + name + " -> " + bean.getName())
        );

        System.out.println("\n2. Getting all Person beans:");
        Map<String, Person> persons = context.getBeansOfType(Person.class);
        persons.forEach((name, bean) -> 
            System.out.println("   - " + name + " -> " + bean.getName())
        );

        System.out.println("\n3. Total beans in context: " + context.getBeanDefinitionCount());
    }

    /**
     * Demonstrates @Qualifier and @Primary annotations
     */
    private static void demonstrateQualifierAndPrimary(AnnotationConfigApplicationContext context) {
        System.out.println("1. Using @Primary annotation:");
        Vehicle primaryVehicle = context.getBean(Vehicle.class);
        System.out.println("   ✓ Primary vehicle: " + primaryVehicle.getName());

        System.out.println("\n2. Using @Qualifier annotation:");
        String helloString = context.getBean(String.class);
        System.out.println("   ✓ Primary String bean: '" + helloString + "'");

        String byeString = context.getBean("bye", String.class);
        System.out.println("   ✓ Qualified String bean: '" + byeString + "'");

        System.out.println("\n3. Dependency injection with @Qualifier:");
        EmployeeService employeeService = context.getBean(EmployeeService.class);
        System.out.println("   ✓ EmployeeService created with qualified Employee dependency");
        employeeService.manageEmployee();
        }       }