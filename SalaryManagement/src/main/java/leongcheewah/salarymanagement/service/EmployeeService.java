package leongcheewah.salarymanagement.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import leongcheewah.salarymanagement.model.EmployeeSearchParamsVO;
import leongcheewah.salarymanagement.model.EmployeeVO;
import leongcheewah.salarymanagement.model.ResponseVO;

public interface EmployeeService {
	
	ResponseVO uploadEmployees(MultipartFile file);

	List<EmployeeVO> getEmployees();

	ResponseVO getEmployeeByEmpId(String empId);
	
	ResponseVO insertEmployee(EmployeeVO employeeData);
	
	ResponseVO updateEmployee(String id, EmployeeVO employeeData);

	ResponseVO deleteEmployee(String id);
	
	ResponseVO searchEmployees(EmployeeSearchParamsVO searchParams);
	
}
