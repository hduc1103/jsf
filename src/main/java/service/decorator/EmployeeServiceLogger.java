package service.decorator;

import java.sql.Connection;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import javax.decorator.Decorator;
import javax.decorator.Delegate;
import javax.inject.Inject;
import model.Employee;
import service.EmployeeService;

@Decorator
public abstract class EmployeeServiceLogger implements EmployeeService {
    
    private static final Logger LOGGER = Logger.getLogger(EmployeeServiceLogger.class.getName());
    
    @Inject
    @Delegate
    private EmployeeService employeeService;
    
    @Override
    public List<Employee> getAllEmployees(Connection conn) {
        LOGGER.info("Getting all employees");
        return employeeService.getAllEmployees(conn);
    }
    
    @Override
    public void addEmployee(Connection conn, Employee employee) {
        LOGGER.info("Adding employee: " + employee.getCode() + " - " + employee.getName());
        employeeService.addEmployee(conn, employee);
    }
    
    @Override
    public void deleteEmployee(Connection conn, String code) {
        LOGGER.info("Deleting employee with code: " + code);
        employeeService.deleteEmployee(conn, code);
    }
    
    @Override
    public void updateEmployee(Connection conn, Employee employee) {
        LOGGER.info("Updating employee: " + employee.getCode() + " - " + employee.getName());
        employeeService.updateEmployee(conn, employee);
    }
    
    @Override
    public Employee searchByCode(Connection conn, String code) {
        LOGGER.info("Searching employee by code: " + code);
        return employeeService.searchByCode(conn,code);
    }
    
    @Override
    public boolean checkAge(Connection conn, Date dob) {
        LOGGER.info("Checking age for DOB: " + dob);
        return employeeService.checkAge(conn, dob);
    }
}

