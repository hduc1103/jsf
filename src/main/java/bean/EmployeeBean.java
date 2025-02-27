package bean;

import javax.inject.Inject;
import javax.inject.Named;

import exception.EmployeeException;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.faces.view.ViewScoped;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import model.Employee;
import model.EmployeeComparators;
import service.EmployeeService;

@Named("employeeBean")
@ViewScoped
public class EmployeeBean implements Serializable {
	private static final long serialVersionUID = 1L;

	@Inject
	private EmployeeService employeeService;

	private List<Employee> employees;
	private Employee newEmployee;
	private Employee curEmployee;

	public List<Employee> getEmployees() {
		return employees;
	}

	public Employee getNewEmployee() {
		return newEmployee;
	}

	public void setNewEmployee(Employee newEmployee) {
		this.newEmployee = newEmployee;
	}

	public Employee getCurEmployee() {
		return curEmployee;
	}

	public void setCurEmployee(Employee curEmployee) {
		this.curEmployee = curEmployee;
	}

	private boolean showAdd = false;
	private boolean showUpdate = false;
	private boolean showList = true;
	private boolean newShowAdd = false;
	
	public boolean isShowAdd() {
		return showAdd;
	}

	public void setShowAdd(boolean showAdd) {
		this.showAdd = showAdd;
	}

	public boolean isShowUpdate() {
		return showUpdate;
	}

	public void setShowUpdate(boolean showUpdate) {
		this.showUpdate = showUpdate;
	}

	public boolean isShowList() {
		return showList;
	}
	
	public void setShowList(boolean showList) {
		this.showList = showList;
	}
	
	public boolean isNewShowAdd() {
		return newShowAdd;
	}
	
	public void setNewShowAdd(boolean newShowAdd) {
		this.newShowAdd = newShowAdd;
	}
	
	@PostConstruct
	public void init() {
		employees = employeeService.getAllEmployees();
		newEmployee = new Employee();
	}

	public void refresh() {
		employees = employeeService.getAllEmployees();
	}

	public void toggleAdd() {
		newEmployee = new Employee();
		showAdd = true;
		showList = false;
	}
	
	public void ntoggleAdd() {
		newEmployee = new Employee();
		newShowAdd = true;
		showList = false;
	}
	
	public void cancelAdd() {
		showAdd = false;
		showList = true;
	}

	public void cancelNewAdd() {
		newShowAdd = false;
		showList = true;
	}
	public void toggleUpdate(Employee employee) {
		curEmployee = employee;
		showUpdate = true;
		showList = false;
	}

	public void cancelUpdate() {
		showUpdate = false;
		showList = true;
	}

	public void addEmployee() {
			employeeService.addEmployee(newEmployee);
			employees = employeeService.getAllEmployees();
			refresh();
			showAdd = false;
			newShowAdd = false;
			showList = true;
	}

	public void deleteEmployee(String code) {
		employeeService.deleteEmployee(code);
		employees = employeeService.getAllEmployees();
	}

	public void updateEmployee() {
		employeeService.updateEmployee(curEmployee);
		refresh();
		curEmployee = new Employee();
		showUpdate = false;
		showList = true;
	}

	public void sortByName() {
		employees.sort(EmployeeComparators.byName);
		employees = new ArrayList<>(employees);
	}

	public void sortByAge() {
		employees.sort(EmployeeComparators.byAge);
		employees = new ArrayList<>(employees);
	}

	public void sortByDob() {
		employees.sort(new EmployeeComparators.byDoB());
		employees = new ArrayList<>(employees);
	}

	public void codeValidator(FacesContext context, UIComponent component, Object value) throws ValidatorException {
		if (value == null || value.toString().trim().isEmpty()) {
	        return;
	    }
		String code = value.toString().trim();
		if (!code.matches("^\\S+$")) {
			throw new ValidatorException(
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid Code", "Code cannot contain white spaces"));
		}
		if (!(code.charAt(0) == 'E' && code.substring(1).matches("\\d+"))) {
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid Code",
					"Code should begin with E and continue with number"));
		}
		if(employeeService.searchByCode(code)!=null) {
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid Code",
					"Employee code already existed"));
		}
	}

	public void nameValidator(FacesContext context, UIComponent component, Object value) throws ValidatorException {
		if (value == null || value.toString().trim().isEmpty()) {
	        return;
	    }
		String name = value.toString().trim();
		if (!name.matches("^[a-zA-Z\\s]+$")) {
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Validation Error",
					"Name can only contain letters and spaces."));
		}

	}

	public void dateValidator(FacesContext context, UIComponent component, Object value) throws ValidatorException {
		if (value == null || value.toString().trim().isEmpty()) {
	        return;
	    }
		Date dob = (Date) value;
		Date curDate = new Date();
		if (dob.after(curDate)) {
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Date of Birth cannot be in the future", "Date of Birth cannot be in the future"));
		}
		if(employeeService.checkAge(dob)== false) {
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Age must be > 0", "Age must be > 0"));
		}
		}
}
