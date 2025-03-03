package dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import exception.EmployeeException;
import model.Employee;
import dao.EmployeeDAO;

public class EmployeeDAOImpl implements EmployeeDAO {
	
	@Override
	public void addEmployee(Connection conn, Employee employee) {
	    String query = "INSERT INTO Mt_employee (employee_code, employee_name, employee_age, date_of_birth) VALUES (?, ?, ?, ?)";

	    try (PreparedStatement stmt = conn.prepareStatement(query)) {
	        stmt.setString(1, employee.getCode());
	        stmt.setString(2, employee.getName());
	        stmt.setInt(3, employee.getAge());
	        stmt.setDate(4, new java.sql.Date(employee.getDob().getTime()));

	        int rowsAffected = stmt.executeUpdate();
	        if (rowsAffected > 0) {
	            System.out.println("Employee with code " + employee.getCode() + " and name " + employee.getName() + " was added successfully!");
	        } else {
	            throw new EmployeeException("Failed to add employee " + employee.getName());
	        }
	    } catch (SQLException e) {
	        e.printStackTrace(); 
	        throw new EmployeeException("Database error while adding employee: " + e.getMessage());
	    }
	}


	@Override
	public void updateEmployee(Connection conn, Employee newEmployeeData) {
		String query = "UPDATE Mt_employee SET employee_name = ?, employee_age = ?, date_of_birth = ? WHERE employee_code = ?";

		try (PreparedStatement stmt = conn.prepareStatement(query)) {

			stmt.setString(1, newEmployeeData.getName());
			stmt.setInt(2, newEmployeeData.getAge());
			stmt.setDate(3, new java.sql.Date(newEmployeeData.getDob().getTime()));
			stmt.setString(4, newEmployeeData.getCode());

			int rowsAffected = stmt.executeUpdate();
			if (rowsAffected > 0) {
				System.out.println("Employee with code, name " + newEmployeeData.getCode() + " , "
						+ newEmployeeData.getName() + " was updated successfully!");
			} else {
				throw new EmployeeException("Failed to update employee with code " + newEmployeeData.getCode());
			}

		} catch (SQLException e) {
			throw new EmployeeException("Database error while updating employee: " + e.getMessage(), e);
		}
	}

	@Override
	public void deleteEmployee(Connection conn, String code) {
		String query = "DELETE FROM Mt_employee WHERE employee_code = ?";
		try (PreparedStatement stmt = conn.prepareStatement(query)) {
			stmt.setString(1, code);
			int rowsAffected = stmt.executeUpdate();
			if (rowsAffected > 0) {
				System.out.println("Employee with code " + code + " was successfully deleted");
			} else {
				throw new EmployeeException("Failed to delete employee " + code);
			}
		} catch (Exception e) {
			throw new EmployeeException("Database error while deleting employee: " + e.getMessage(), e);
		}
	}

	@Override
	public List<Employee> getAllEmployees(Connection conn) {
		List<Employee> employeeList = new ArrayList<>();
		String query = "SELECT * FROM Mt_employee";

		try (PreparedStatement stmt = conn.prepareStatement(query);
				ResultSet rs = stmt.executeQuery()) {
			while (rs.next()) {
				employeeList.add(new Employee(rs.getString("employee_code"), rs.getString("employee_name"),
						rs.getInt("employee_age"), rs.getDate("date_of_birth")));
			}

		} catch (Exception e) {
			throw new EmployeeException("Database error while fetching employee: " + e.getMessage(), e);
		}
		return employeeList;
	}

	@Override
	public Employee getEmployeeByCode(Connection conn, String code) {
		String query = "SELECT * FROM Mt_employee WHERE employee_code = ?";
		Employee employee = null;

		try (PreparedStatement stmt = conn.prepareStatement(query)) {

			stmt.setString(1, code);
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					employee = new Employee();
					employee.setCode(code);
					employee.setAge(rs.getInt("employee_age"));
					employee.setName(rs.getString("employee_name"));
					employee.setDob(rs.getDate("date_of_birth"));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return employee;
	}

	@Override
	public boolean checkAge(Connection conn, Date edob) {
		Calendar now = Calendar.getInstance();
		Calendar dob = Calendar.getInstance();
		dob.setTime(edob);

		int age = now.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

		if (now.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
			age--;
		}

		if (age < 1) {
			return false;
		}
		return true;
	}

}
