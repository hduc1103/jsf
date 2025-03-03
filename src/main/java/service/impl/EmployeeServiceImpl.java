package service.impl;

import java.sql.Connection;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import model.Employee;
import dao.EmployeeDAO;
import interceptor.Loggable;
import service.EmployeeService;

@Named("employeeService")
@ApplicationScoped
public class EmployeeServiceImpl implements EmployeeService {

	@Inject
	private EmployeeDAO employeeDAO;
	
	@Override
	@Loggable
	public void addEmployee(Connection conn, Employee employee) {
		Calendar now = Calendar.getInstance();
		Calendar dob = Calendar.getInstance();
		dob.setTime(employee.getDob());

		int age = now.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

		if (now.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
			age--;
		}

		employee.setAge(age);
		employeeDAO.addEmployee(conn, employee);
	}

	@Override
	@Loggable
	public void deleteEmployee(Connection conn, String code) {
		employeeDAO.deleteEmployee(conn, code);
	}

	@Override
	@Loggable
	public List<Employee> getAllEmployees(Connection conn) {
		return employeeDAO.getAllEmployees(conn);
	}

	@Override
	@Loggable
	public void updateEmployee(Connection conn, Employee newEmployeeData) {
		Calendar now = Calendar.getInstance();
		Calendar dob = Calendar.getInstance();
		dob.setTime(newEmployeeData.getDob());

		int age = now.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

		if (now.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
			age--;
		}

		newEmployeeData.setAge(age);
		employeeDAO.updateEmployee(conn, newEmployeeData);
	}

	@Override
	@Loggable
	public Employee searchByCode(Connection conn, String code) {
		Employee employee = employeeDAO.getEmployeeByCode(conn, code);
		return employee;
	}

	@Override
	@Loggable
	public boolean checkAge(Connection conn, Date dob) {
		return employeeDAO.checkAge(conn, dob);
	}

}
