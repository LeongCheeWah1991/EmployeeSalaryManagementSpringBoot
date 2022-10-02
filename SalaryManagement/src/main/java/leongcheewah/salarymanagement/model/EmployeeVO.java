package leongcheewah.salarymanagement.model;

import java.io.Serializable;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import leongcheewah.salarymanagement.util.ResponseMessageConstants;

public class EmployeeVO implements Serializable {

	private static final long serialVersionUID = 7044531438893359172L;

	@NotEmpty(message=ResponseMessageConstants.MISSING_ERROR + ResponseMessageConstants.EMPLOYEE_ID)
	@NotNull(message=ResponseMessageConstants.MISSING_ERROR + ResponseMessageConstants.EMPLOYEE_ID)
	private String id;
	
	@NotEmpty(message=ResponseMessageConstants.MISSING_ERROR + ResponseMessageConstants.EMPLOYEE_LOGIN)
	@NotNull(message=ResponseMessageConstants.MISSING_ERROR + ResponseMessageConstants.EMPLOYEE_LOGIN)
	private String login;
	
	@NotEmpty(message=ResponseMessageConstants.MISSING_ERROR + ResponseMessageConstants.EMPLOYEE_NAME)
	@NotNull(message=ResponseMessageConstants.MISSING_ERROR + ResponseMessageConstants.EMPLOYEE_NAME)
	private String name;
	
	@Min(value = 0, message = ResponseMessageConstants.BAD_INPUT_ERROR + ResponseMessageConstants.EMPLOYEE_SALARY)
	private Double salary;

	public EmployeeVO() {
		super();
	}

	public EmployeeVO(String id, String login, String name, double salary) {
		super();
		this.id = id;
		this.login = login;
		this.name = name;
		this.salary = salary;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getSalary() {
		return salary;
	}

	public void setSalary(Double salary) {
		this.salary = salary;
	}

	@Override
	public String toString() {
		return "EmployeeVO [id=" + id + ", login=" + login + ", name=" + name + ", salary=" + salary + "]";
	}

}
