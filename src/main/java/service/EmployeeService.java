package service;

import java.sql.Connection;
import java.util.Date;
import java.util.List;

import model.Employee;

public interface EmployeeService {
    void addEmployee(Connection conn, Employee employee);
    void deleteEmployee(Connection conn, String code);
    void updateEmployee(Connection conn, Employee employee);
    List<Employee> getAllEmployees(Connection conn);
    Employee searchByCode(Connection conn, String code);
    boolean checkAge(Connection conn, Date dob);
}
