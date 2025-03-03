package dao;

import java.sql.Connection;
import java.util.Date;
import java.util.List;

import model.Employee;

public interface EmployeeDAO {
	void addEmployee(Connection conn, Employee employee);

	void updateEmployee(Connection conn, Employee employee);

	void deleteEmployee(Connection conn, String code);

	List<Employee> getAllEmployees(Connection conn);

	Employee getEmployeeByCode(Connection conn, String code);
	
	boolean checkAge(Connection conn, Date dob);
}
