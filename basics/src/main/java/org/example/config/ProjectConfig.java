package org.example.config;

import org.example.beans.Person;
import org.example.beans.PrototypeService;
import org.example.beans.Vehicle;
import org.example.implementations.FullTimeEmployee;
import org.example.implementations.PartTimeEmployee;
import org.example.interfaces.Employee;
import org.example.services.EmployeeService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;

/**
Spring @Configuration annotation is part of the spring core framework.
Spring Configuration annotation indicates that the class has @Bean definition
methods. So Spring container can process the class and generate Spring Beans
to be used in the application.
**/
@Configuration
@ComponentScan(basePackages = { "org.example.implementations", "org.example.services" })
@ComponentScan(basePackageClasses =  { Person.class })
public class ProjectConfig {

    /**
    @Bean annotation, which lets Spring know that it needs to call
    this method when it initializes its context and adds the returned value to the context.
    */
    @Primary
    @Bean
//    @Qualifier("volkswagen")




    Vehicle vehicle1(){
        var veh= new Vehicle();
        veh.setName("Volkswagen Golf");
        return veh;
    }
    @Bean
//    @Qualifier("audi")
    Vehicle vehicle2(){
        var veh= new Vehicle();
        veh.setName("Audi 8");
        return veh;
    }
    @Bean
//    @Qualifier("bmw")
    Vehicle vehicle3(){
        var veh= new Vehicle();
        veh.setName("BMW 3");
        return veh;
    }

     @Primary
    @Bean(value = "hello")
    String hello() {
        return "Hello World";
    }

    @Bean(name="bye")
    String bye(){
        return "bye!";
    }

    @Bean("number")
    Integer number() {
        return 16;
    }

    @Bean
    public Person person(){
        Person person = new Person("Lucky", 25, vehicle3());
        person.setSpouse(person2());
        return person;
    }

    @Bean
    public Person person2(){
        Person person= new Person("Lucy", 21, vehicle2());
        return person;
    }

    @Bean
    @Qualifier("fullTimeEmployee")
    public Employee fullTimeEmployee(){
        return new FullTimeEmployee();
    }

    @Bean
    @Qualifier("partTimeEmployee")
    public Employee partTimeEmployee(){
        return new PartTimeEmployee();
    }

    // EmployeeService bean will be injected with a FullTimeEmployee
    @Bean
    public EmployeeService employeeService(@Qualifier("fullTimeEmployee") Employee employee){
        return new  EmployeeService(employee);
    }

    /**
     * PROTOTYPE SCOPE: Creates a new instance every time the bean is requested
     * Unlike Singleton (default), each call to getBean() returns a different instance
     */
    @Bean
    @Scope("prototype")
    public PrototypeService prototypeService(){
        return new PrototypeService();
    }

    /**
     * LAZY INITIALIZATION: Bean is created only when first requested, not at startup
     * Useful for expensive beans that might not always be needed
     */
    @Bean
    @Lazy
    public Vehicle lazyVehicle(){
        System.out.println("Creating lazy vehicle bean...");
        var veh = new Vehicle();
        veh.setName("Lazy Loaded Tesla Model 3");
        return veh;
    }

    /**
     * CUSTOM FACTORY METHOD: Demonstrates factory pattern with Spring beans
     * This creates a Vehicle through a factory instead of direct instantiation
     */
    @Bean
    public Vehicle factoryVehicle(){
        return VehicleFactory.createPremiumVehicle();
    }

    /**
     * PROTOTYPE SCOPE WITH INITIALIZATION: Shows initialization logic in prototype beans
     */
    @Bean
    @Scope("prototype")
    public Person prototypePersonTemplate(){
        return new Person("Template Person", 0, vehicle1());
    }
}
