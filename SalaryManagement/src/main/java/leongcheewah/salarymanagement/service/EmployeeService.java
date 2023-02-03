package leongcheewah.salarymanagement.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import leongcheewah.salarymanagement.model.EmployeeSearchParamsVO;
import leongcheewah.salarymanagement.model.EmployeeVO;
import leongcheewah.salarymanagement.model.ResponseVO;

public interface EmployeeService {
	
	void uploadEmployees(MultipartFile file);

	List<EmployeeVO> getEmployees();

	EmployeeVO getEmployeeByEmpId(String empId);
	
	void insertEmployee(EmployeeVO employeeData);
	
	void updateEmployee(String id, EmployeeVO employeeData);

	void deleteEmployee(String id);
	
	List<EmployeeVO> searchEmployees(EmployeeSearchParamsVO searchParams);
	
}
