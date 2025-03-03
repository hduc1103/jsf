package bean;

import javax.inject.Inject;

import java.io.Serializable;
import java.sql.Connection;
import java.util.Date;
import java.util.List;

import model.Employee;
import service.EmployeeService;

public class EmployeeBean implements Serializable {
	private static final long serialVersionUID = 1L;

	@Inject
	private EmployeeService employeeService;

	public List<Employee> getAllEmployees(Connection conn){
		return employeeService.getAllEmployees(conn);
	}
	public void addEmployee(Connection conn, Employee employee) {
			employeeService.addEmployee(conn, employee);
	}

	public void deleteEmployee(Connection conn, String code) {
		employeeService.deleteEmployee(conn, code);
	}

	public void updateEmployee(Connection conn, Employee employee) {
		employeeService.updateEmployee(conn, employee);
	}

	public Employee searchByCode(Connection conn, String code) {
		return employeeService.searchByCode(conn,code);
	}
	public boolean checkAge(Connection conn, Date dob) {
		return employeeService.checkAge(conn, dob);
	}

}
