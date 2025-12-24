package org.example;

import org.example.infra.LifecycleLoggingBeanPostProcessor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.*;

@Configuration
//@ComponentScan(basePackages = "org.example")
public class SpringConfig {
    @Bean
    public LifecycleLoggingBeanPostProcessor lifecycleLoggingBeanPostProcessor() {
        return new LifecycleLoggingBeanPostProcessor();
    }

    @Bean
    public AddressService addressService() {
        AddressServiceImpl addressService = new AddressServiceImpl();
        addressService.setCity("New York");
        addressService.setStreet("Broadway");
        return addressService;
    }

    @Bean
    public Map<String, Grade> grades() {
        Map<String, Grade> grades = new LinkedHashMap<>();
        grades.put("math", mathGrade());
        grades.put("science", scienceGrade());
        return grades;
    }

    @Bean
    @Qualifier("mathGrade")
    public Grade mathGrade() {
        Grade grade = new Grade();
        grade.setSubject("math");
        grade.setGrade("A");
        return grade;
    }

    @Bean
    @Qualifier("scienceGrade")
    public Grade scienceGrade() {
        Grade grade = new Grade();
        grade.setSubject("science");
        grade.setGrade("B+");
        return grade;
    }

    @Bean
    public List<Course> courses() {
        List<Course> courses = new ArrayList<>();
        courses.add(javaCourse());
        courses.add(springCourse());
        return courses;
    }

    @Bean
    public Course javaCourse() {
        Course course = new Course();
        course.setCourseId(01);
        course.setCourseName("Java");
        return course;
    }

    @Bean
    public Course springCourse() {
        Course course = new Course();
        course.setCourseId(02);
        course.setCourseName("Spring Framework");
        return course;
    }

    @Bean
    public Course reactCourse() {
        Course course = new Course();
        course.setCourseId(03);
        course.setCourseName("React js");
        return course;
    }

    @Bean
    public Set<String> hobbies() {
        Set<String> hobbies = new HashSet<>();
        hobbies.add("Reading");
        hobbies.add("Coding");
        hobbies.add("Singing");
        return hobbies;
    }

    @Bean
    public Friend friend() {
        Friend friend = new Friend();
        friend.setFid(001);
        friend.setName("Alice");
        return friend;
    }

    /** This constructor is required for autowiring field/setter using @Autowired to set properties */
    @Bean
    @Qualifier("student")
    public Student student() {
        return new Student(001, "John Doe");
    }

    /** Constructor Injection & autowiring using method parameter.
     * It will require even for autowiring using @Autowired on constructor to set values */
    @Bean
    @Primary
    @Qualifier("studentConstructorInjection")
    public Student studentConstructorInjection(
            @Qualifier("addressService") AddressService addressService,
            List<Course> courses,
            Set<String> hobbies,
            Map<String, Grade> grades,
            Friend friend
    ) {
        return new Student(1, "Alice", addressService, courses, hobbies, grades, friend);
    }

   /** Setter Injection & autowiring using method call.
    *  */
    @Bean
//    @Primary
    @Qualifier("studentAman")
    public Student studentAman(
            // Autowiring using method parameter
            @Qualifier("addressService") AddressService addressService,
            List<Course> courses,
            Set<String> hobbies,
            Map<String, Grade> grades,
            Friend friend
    ) {
        Student student = new Student();
        student.setSid(2);
        student.setStudentName("Aman");
        student.setAddressService(addressService);
        student.setCourses(courses);
        student.setHobbies(hobbies);
        student.setGrades(grades);
        student.setFriend(friend);
        return student;
    }

    @Bean
//    @Primary
    @Qualifier("studentBob")    // help in disambiguating beans of the same type
    public Student studentBob() {
        Student student = new Student();
        student.setSid(2);
        student.setStudentName("Bob");
        // Autowiring using method call, Setter injection
        // courses, hobbies, grades are obtained from other methods or sources
        student.setAddressService(addressService());
        student.setCourses(Arrays.asList(reactCourse()));
        student.setHobbies(new HashSet<>(Arrays.asList("Skiing", "Painting")));
        Map<String, Grade> grades = new HashMap<>();
        grades.put("Math", mathGrade());
        grades.put("Science", scienceGrade());
        student.setGrades(grades);
        student.setFriend(friend());
        return student;
    }
}
