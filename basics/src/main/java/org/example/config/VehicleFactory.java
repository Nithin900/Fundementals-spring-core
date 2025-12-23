package org.example.config;

import org.example.beans.Vehicle;

/**
 * Factory class demonstrating the Factory Pattern
 * Used with Spring's factory method for creating beans
 */
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
