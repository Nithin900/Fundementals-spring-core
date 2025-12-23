package org.example.services;

import org.example.interfaces.Employee;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class EmployeeService {
    private final Employee employee;

    public EmployeeService(@Qualifier("fullTimeEmployee") Employee employee) {
        this.employee = employee;
    }

    public void manageEmployee() {
        employee.work();
    }
}
