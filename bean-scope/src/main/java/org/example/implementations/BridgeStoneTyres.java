package org.example.implementations;

import org.example.interfaces.Tyres;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
//@Primary
public class BridgeStoneTyres implements Tyres {
    @Override
    public String rotate(){
        return "Vehicle moving with BridgeStone tyres";
    }
}
