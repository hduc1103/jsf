package service;

import java.util.Date;
import java.util.List;

import model.Employee;

public interface EmployeeService {
    void addEmployee(Employee employee);
    void deleteEmployee(String code);
    void updateEmployee(Employee employee);
    List<Employee> getAllEmployees();
    Employee searchByCode(String code);
    boolean checkAge(Date dob);
}
