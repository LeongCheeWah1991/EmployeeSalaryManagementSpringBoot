package leongcheewah.salarymanagement.util;

import java.util.ArrayList;
import java.util.List;

import leongcheewah.salarymanagement.bean.Employee;
import leongcheewah.salarymanagement.model.EmployeeVO;

public class UtilHelper {

	public static EmployeeVO mapEmployeeToVO(Employee employeeBean) {
		return new EmployeeVO(employeeBean.getId(), employeeBean.getLogin(), employeeBean.getName(),
				employeeBean.getSalary());
	}

	public static List<EmployeeVO> mapEmployeeListToVOList(List<Employee> employeeBeanList) {
		List<EmployeeVO> returnEmployeeList = new ArrayList<EmployeeVO>();
		for (Employee bean : employeeBeanList) {
			returnEmployeeList.add(mapEmployeeToVO(bean));

		}
		return returnEmployeeList;
	}

	public static Employee mapEmployeeVOToEmployee(EmployeeVO employee) {
		return new Employee(employee.getId(), employee.getLogin(), employee.getName(), employee.getSalary());
	}

}
