package org.example;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.example.config.DemoConfig;
import org.example.demo.SelfInvokeService;
import org.springframework.aop.support.AopUtils;

import java.util.*;

public class Main {
    public static void main(String[] args) {

        //---------------- Dependency Injection using java configurations ----------------

        AnnotationConfigApplicationContext javaConfigCtx = new AnnotationConfigApplicationContext();

        // Scan components/aspects and add drill config to see lifecycle/AOP output
        javaConfigCtx.scan("org.example");
        javaConfigCtx.register(SpringConfig.class, DemoConfig.class);

        System.out.println("=== REFRESH CONTEXT (JAVA CONFIG + DRILLS) ===");
        javaConfigCtx.refresh();

        // Show Student bean populated from Java config
        Student javaConfigStudent = javaConfigCtx.getBean(Student.class);
        System.out.println("\n----- Dependency injection using java config: -----");
        System.out.println(javaConfigStudent);

        System.out.println("\n=== SELF INVOCATION DRILL ===");
        SelfInvokeService svc = javaConfigCtx.getBean(SelfInvokeService.class);
        System.out.println("SelfInvokeService runtime class = " + svc.getClass());
        System.out.println("Is AOP proxy? " + AopUtils.isAopProxy(svc));

        System.out.println("\n-- Calling inner() from outside (should be advised) --");
        svc.inner();

        System.out.println("\n-- Calling outer() (inner() called internally, should bypass advice) --");
        svc.outer();

        System.out.println("\n=== PROTOTYPE DRILL ===");
        Object p1 = javaConfigCtx.getBean("prototypeThing");
        Object p2 = javaConfigCtx.getBean("prototypeThing");
        System.out.println("prototypeThing same instance? " + (p1 == p2));

        System.out.println("\n=== LAZY INIT DRILL ===");
        System.out.println("Requesting lazyThing now...");
        javaConfigCtx.getBean("lazyThing"); // constructor prints here, not at refresh

        System.out.println("\n=== CLOSE CONTEXT (DESTROY DRILL) ===");
        javaConfigCtx.close();

//        -------------------- For ComponentScan only(Setiing values) --------------------
//        AnnotationConfigApplicationContext context= new AnnotationConfigApplicationContext(SpringConfig.class);
//
//        // Retrieve beans from the context
//        AddressService addressService = context.getBean(AddressService.class);
//        Friend friend = context.getBean(Friend.class);
//        Grade grade = context.getBean(Grade.class);
//        Course course = context.getBean(Course.class);
//
//        // Set values for AddressService
//        if (addressService instanceof AddressServiceImpl) {
//            AddressServiceImpl addressServiceImpl = (AddressServiceImpl) addressService;
//            addressServiceImpl.setCity("YourCity");
//            addressServiceImpl.setStreet("YourStreet");
//        }
//
//        // Set values for Friend
//        friend.setFid(1);
//        friend.setName("John");
//
//        // Set values for Grade
//        grade.setSubject("Math");
//        grade.setGrade("A");
//
//        // Set values for Course
//        course.setCourseId(101);
//        course.setCourseName("Computer Science");
//
//        // Create instance of Student and set values
//        Student student = context.getBean(Student.class);
//        student.setSid(1);
//        student.setStudentName("Alice");
//        student.setAddressService(addressService);
//        student.setFriend(friend);
//        student.setGrades(Collections.singletonMap(grade.getSubject(), grade));
//        student.setCourses(Collections.singletonList(course));
//        student.setHobbies(Collections.singleton("Reading"));
//
//        // Use the instance of Student
//        System.out.println(student);
//
//        // Close the context
//        context.close();

        //----------------- Dependency Injection using xml configurations -----------------

        // Load the Spring configuration file
        ApplicationContext xmlContext = new ClassPathXmlApplicationContext("spring.core.config.xml");

        // Retrieve the Student bean from the context
//        Student student2= (Student) xmlContext.getBean("studentConstructorInjection");
//        Student student2= (Student) xmlContext.getBean("studentSetterInjection");
        Student student2 = xmlContext.getBean("autowiringStudent", Student.class);

//        AddressService student2= (AddressService) xmlContext.getBean("addressInterfaceInjection");
//        System.out.println("Address: "+student.getAddress());

        // Display information about the student
        System.out.println("\n----- Dependency injection using xml config: -----");

        System.out.println("Student Details: "+student2.getClass().getName());
        System.out.println(student2);


//         ---------- this code is only for setting values while using @ComponentScan annotation ----------
        // Note: This might not be the proper way of doing it. I will see it later.
        //  Functional Bean registeration
//        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SpringConfig.class)) {
//            // Create instances of required beans with desired values
//            AddressServiceImpl addressService = context.getBean(AddressServiceImpl.class);
//            addressService.setCity("New York");
//            addressService.setStreet("Broadway");
//
//            // Create sample data for courses
//            List<Course> courses = new ArrayList<>();
//            courses.add(new Course(1, "Mathematics"));
//            courses.add(new Course(2, "Physics"));
//
//            // Create sample data for hobbies
//            Set<String> hobbies = new HashSet<>();
//            hobbies.add("Reading");
//            hobbies.add("Swimming");
//
//            // Create sample data for grades
//            Map<String, Grade> grades = new HashMap<>();
//            grades.put("Math", new Grade("Math", "A"));
//            grades.put("Physics", new Grade("Physics", "B"));
//
//            // Create sample data for friend
//            Friend friend = new Friend(1, "Alice");
//
//            // Register beans with the application context
//            context.registerBean("addressService", AddressService.class, () -> addressService);
//            context.registerBean("grades", Map.class, () -> grades);
//            context.registerBean("courses", List.class, () -> courses);
//            context.registerBean("hobbies", Set.class, () -> hobbies);
//            context.registerBean("friend", Friend.class, () -> friend);
//
//            context.registerBean(Student.class, () -> {
//                Student student = new Student();
//
//                student.setAddressService(addressService);
//                student.setCourses(courses);
//                student.setHobbies(hobbies);
//                student.setGrades(grades);
//                student.setFriend(friend);
//                return student;
//            });
//
//            // Retrieve the Student bean from the context
//            Student student1 = context.getBean(Student.class);
//            student1.setSid(1);
//            student1.setStudentName("John Doe");
//
//            // Display information about the student
//            System.out.println("----- Dependency injection using java config: -----");
//            System.out.println("Student Details: " + student1.getClass().getName());
//            System.out.println(student1);
//        }
    }
}
