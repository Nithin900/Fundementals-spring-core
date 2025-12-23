package org.example.implementations;

import org.example.interfaces.Employee;
import org.springframework.stereotype.Component;

@Component
public class FullTimeEmployee implements Employee {
    @Override
    public void work() {
        System.out.println("Full-time employee is working.");
    }
}
