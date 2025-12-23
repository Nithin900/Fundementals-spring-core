package org.example.implementations;

import org.example.interfaces.Tyres;
import org.springframework.stereotype.Component;

@Component
public class MichelinTyres implements Tyres {
    @Override
    public String rotate() {
        return "Vehicle moving with Michelin tyres";
    }
}
