package leongcheewah.salarymanagement.service;

import java.util.List;

import leongcheewah.salarymanagement.model.EmployeeSearchParamsVO;
import leongcheewah.salarymanagement.model.EmployeeVO;
import leongcheewah.salarymanagement.model.ResponseVO;

public interface EmployeeService {
	
	List<EmployeeVO> getEmployees();
	
	ResponseVO searchEmployees(EmployeeSearchParamsVO searchParams);

}
