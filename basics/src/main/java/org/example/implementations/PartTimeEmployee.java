package org.example.implementations;

import org.example.interfaces.Employee;
import org.springframework.stereotype.Component;

@Component
public class PartTimeEmployee implements Employee {
    @Override
    public void work() {
        System.out.println("Part-time employee is working.");
    }
}
