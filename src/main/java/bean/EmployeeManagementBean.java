package bean;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.inject.Named;

import model.Employee;
import model.EmployeeComparators;
import util.DatabaseUtil;

@Named("employeeManagement")
@ConversationScoped
public class EmployeeManagementBean implements Serializable {
	private static final long serialVersionUID = 1L;

	@Inject
	private EmployeeBean employeeBean;

	@Inject
	private Conversation conversation;

	private List<Employee> employees;
	private Employee newEmployee;
	private Employee curEmployee;

	// getter and setter
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

	private Connection conn;

	@PostConstruct
	public void init() {
		try {
			conn = DatabaseUtil.getConnection();
			employees = employeeBean.getAllEmployees(conn);
			newEmployee = new Employee();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// for the default add, delete, update ( used conversation but no page
	// navigation)
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

	public void toggleAdd() {
		if (conversation.isTransient()) {
			conversation.begin("default add employee");
		}
		newEmployee = new Employee();
		showAdd = true;
		showList = false;
	}

	public void cancelAdd() {
		System.out.println("Current conversation: " + conversation.getId());
		conversation.end();
		showAdd = false;
		showList = true;
	}

	public void toggleUpdate(Employee employee) {
		if (conversation.isTransient()) {
			conversation.begin("update employee");
		}
		curEmployee = employee;
		showUpdate = true;
		showList = false;
	}

	public void cancelUpdate() {
		System.out.println("Current conversation: " + conversation.getId());
		conversation.end();
		showUpdate = false;
		showList = true;
	}

	public void addEmployee() {
		employeeBean.addEmployee(conn, newEmployee);
		refresh();
		System.out.println("Current conversation: " + conversation.getId());
		conversation.end();
		showAdd = false;
		newShowAdd = false;
		showList = true;
	}

	public void deleteEmployee(String code) {
		employeeBean.deleteEmployee(conn, code);
		employees = employeeBean.getAllEmployees(conn);
	}

	public void updateEmployee() {
		employeeBean.updateEmployee(conn, curEmployee);
		System.out.println("Current conversation: " + conversation.getId());
		conversation.end();
		refresh();
		curEmployee = new Employee();
		showUpdate = false;
		showList = true;
	}

	// for using add with custom component ( HTMLInputText- not finished with ajax)
	public void ntoggleAdd() {
		newEmployee = new Employee();
		newShowAdd = true;
		showList = false;
	}

	public void cancelNewAdd() {
		newShowAdd = false;
		showList = true;
	}

	// for using add with conversation ( page navigation)
	public String firstStep() {
		if (conversation.isTransient()) {
			conversation.begin("new add employee");
		}
		newEmployee = new Employee();
		return "firstStepPage";
	}

	public String newAddEmployee() {
		employeeBean.addEmployee(conn, newEmployee);
		System.out.println("Current conversation: " + conversation.getId());
		conversation.end();
		return "index?faces-redirect=true";
	}

	public String newCancel() {
		System.out.println("Current conversation: " + conversation.getId());
		conversation.end();
		return "index?faces-redirect=true";
	}

	// functional methods
	public void refresh() {
		employees = employeeBean.getAllEmployees(conn);
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
		if (employeeBean.searchByCode(conn, code) != null) {
			throw new ValidatorException(
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid Code", "Employee code already existed"));
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
		if (employeeBean.checkAge(conn, dob) == false) {
			throw new ValidatorException(
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Age must be > 0", "Age must be > 0"));
		}
	}

	@PreDestroy
	public void cleanup() {
		System.out.println("EmployeeManagementBean is being destroyed. Cleaning up...");
		employees = null;
		newEmployee = null;
		curEmployee = null;

		if (conn != null) {
			try {
				conn.close();
				System.out.println("Database connection closed successfully.");
			} catch (SQLException e) {
				System.err.println("Error while closing database connection: " + e.getMessage());
				e.printStackTrace();
			}
		}
	}
}