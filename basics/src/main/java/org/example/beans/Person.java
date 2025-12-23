package org.example.beans;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class Person {
    private String name;
    private int age;

    /**
     * CIRCULAR DEPENDENCY RESOLUTION:
     * When Person A references Person B and Person B references Person A,
     * a circular dependency occurs. Spring resolves this using @Lazy annotation.
     * 
     * @Lazy: The spouse bean is created only when first accessed, breaking the circular reference at initialization.
     * This is one of the solutions; others include using ObjectProvider or setter injection.
     */
    @Autowired
    @Lazy
    private Person spouse;

    @Autowired
    private Vehicle vehicle;

    public Person(String name, int age, Vehicle vehicle) {
        this.name = name;
        this.age = age;
        this.vehicle = vehicle;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Person getSpouse() {
        return spouse;
    }

    /**
     * Setter method for spouse to allow manual setting of circular references
     * This is also injected with @Lazy annotation to break circular dependency
     */
    public void setSpouse(Person spouse) {
        this.spouse = spouse;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", spouse=" + (spouse != null ? spouse.getName() : "None") +
                ", vehicle=" + vehicle +
                '}';
    }
}

