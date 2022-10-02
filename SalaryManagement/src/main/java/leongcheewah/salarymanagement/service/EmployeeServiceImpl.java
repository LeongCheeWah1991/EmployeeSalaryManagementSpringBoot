package leongcheewah.salarymanagement.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import leongcheewah.salarymanagement.bean.Employee;
import leongcheewah.salarymanagement.model.EmployeeVO;
import leongcheewah.salarymanagement.repository.EmployeeRepository;
import leongcheewah.salarymanagement.util.UtilHelper;

@Service
public class EmployeeServiceImpl implements EmployeeService {

	@Autowired
	private EmployeeRepository employeeRepository;
	
	@Override
	public List<EmployeeVO> getEmployees() {

		List<Employee> employeeBeanList = (List<Employee>) employeeRepository.findAll();
		List<EmployeeVO> returnEmployeeList = UtilHelper.mapEmployeeBeanListToVOList(employeeBeanList);
		return returnEmployeeList;
	}
}
