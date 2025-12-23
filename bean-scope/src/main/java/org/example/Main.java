package org.example;

import org.example.beans.Person;
import org.example.beans.Vehicle;
import org.example.config.AppConfig;
import org.example.services.VehicleServices;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    public static void main(String[] args) {
        ApplicationContext context= new AnnotationConfigApplicationContext(AppConfig.class);

        /* Notice that there are three VehicleServices object is creating when using prototype scope in vehicle.
         * one of them because of the eager initialization of autowired constructor in VehicleServices. */
        VehicleServices vehicleServices1= context.getBean(VehicleServices.class);
        VehicleServices vehicleServices2= context.getBean(VehicleServices.class);

        // See when bean is creating on Eager/Lazy initialization
        System.out.println("Before retrieving the Person bean from the Spring Context");
        Vehicle eagerVehicle= context.getBean(Vehicle.class);
        Person lazyPerson = context.getBean(Person.class);
        System.out.println("After retrieving the Person bean from the Spring Context");

        System.out.println("------  ----------------------------------");

        // Checking the scope of the bean by comparing the hashcode
        System.out.println("Hashcode of vehicleServices1: "+vehicleServices1.hashCode());
        System.out.println("Hashcode of vehicleServices2: "+vehicleServices2.hashCode());

        if (vehicleServices1==vehicleServices2){
            System.out.println("VehicleServices bean is singleton scoped bean.");
        }
        else {
            System.out.println("VehicleServices bean is prototype scoped bean.");
        }
    }
}