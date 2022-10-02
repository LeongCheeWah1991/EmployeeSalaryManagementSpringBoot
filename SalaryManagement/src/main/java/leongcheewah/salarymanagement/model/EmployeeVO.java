package leongcheewah.salarymanagement.model;

import java.io.Serializable;

public class EmployeeVO implements Serializable {

	private static final long serialVersionUID = 7044531438893359172L;

	private String id;
	
	private String login;
	
	private String name;
	
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
		return "Employee [id=" + id + ", login=" + login + ", name=" + name + ", salary=" + salary + "]";
	}

}
