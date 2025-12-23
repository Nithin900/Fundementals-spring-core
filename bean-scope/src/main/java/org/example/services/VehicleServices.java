package org.example.services;

import org.example.interfaces.Tyres;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
//@Scope(BeanDefinition.SCOPE_SINGLETON)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class VehicleServices {
    private Tyres tyres;
    public VehicleServices(){
        System.out.println("VehicleServices object is created");
    }

    private void moveVehicle(){
        String status= tyres.rotate();
    }
}
