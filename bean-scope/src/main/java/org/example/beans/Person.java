package org.example.beans;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component("personBean")
@Lazy
public class Person {
    @Value("Lucky")
    private String name;
    private final Vehicle vehicle;

    @Autowired
    public Person(Vehicle vehicle) {
        this.vehicle = vehicle;
        System.out.println("Person bean is created by spring.");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }
}
