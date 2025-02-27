package service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import model.Employee;
import dao.EmployeeDAO;
import service.EmployeeService;

@Named("employeeService")
@ApplicationScoped
public class EmployeeServiceImpl implements EmployeeService {

	@Inject
	private EmployeeDAO employeeDAO;

	@Override
	public void addEmployee(Employee employee) {
		Calendar now = Calendar.getInstance();
		Calendar dob = Calendar.getInstance();
		dob.setTime(employee.getDob());

		int age = now.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

		if (now.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
			age--;
		}

		employee.setAge(age);
		employeeDAO.addEmployee(employee);
	}

	@Override
	public void deleteEmployee(String code) {
		employeeDAO.deleteEmployee(code);
	}

	@Override
	public List<Employee> getAllEmployees() {
		return employeeDAO.getAllEmployees();
	}

	@Override
	public void updateEmployee(Employee newEmployeeData) {
		Calendar now = Calendar.getInstance();
		Calendar dob = Calendar.getInstance();
		dob.setTime(newEmployeeData.getDob());

		int age = now.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

		if (now.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
			age--;
		}

		newEmployeeData.setAge(age);
		employeeDAO.updateEmployee(newEmployeeData);
	}

	@Override
	public Employee searchByCode(String code) {
		Employee employee = employeeDAO.getEmployeeByCode(code);
		return employee;
	}

	@Override
	public boolean checkAge(Date dob) {
		return employeeDAO.checkAge(dob);
	}

}
