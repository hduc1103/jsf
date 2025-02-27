package dao;

import java.util.Date;
import java.util.List;

import model.Employee;

public interface EmployeeDAO {
	void addEmployee(Employee employee);

	void updateEmployee(Employee employee);

	void deleteEmployee(String code);

	List<Employee> getAllEmployees();

	Employee getEmployeeByCode(String code);
	
	boolean checkAge(Date dob);
}
