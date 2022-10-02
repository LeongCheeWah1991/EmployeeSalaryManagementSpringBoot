package leongcheewah.salarymanagement.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import leongcheewah.salarymanagement.bean.Employee;
import leongcheewah.salarymanagement.model.EmployeeSearchParamsVO;
import leongcheewah.salarymanagement.model.EmployeeVO;
import leongcheewah.salarymanagement.model.ResponseVO;
import leongcheewah.salarymanagement.repository.EmployeeRepository;
import leongcheewah.salarymanagement.util.UtilHelper;

@Service
public class EmployeeServiceImpl implements EmployeeService {

	@Autowired
	private Validator validator;
	
	@Autowired
	private EmployeeRepository employeeRepository;
	
	@Override
	public List<EmployeeVO> getEmployees() {

		List<Employee> EmployeeList = (List<Employee>) employeeRepository.findAll();
		List<EmployeeVO> returnEmployeeList = UtilHelper.mapEmployeeListToVOList(EmployeeList);
		return returnEmployeeList;
	}
	
	@Override
	public ResponseVO searchEmployees(EmployeeSearchParamsVO searchParams) {

		String violationMsg = null;

		Set<ConstraintViolation<EmployeeSearchParamsVO>> violations = validator.validate(searchParams);

		if (!violations.isEmpty()) {
			for (ConstraintViolation<EmployeeSearchParamsVO> violation : violations) {
				violationMsg = violation.getMessage();
			}

			return new ResponseVO(false, violationMsg);
		}

		double minSalary = searchParams.getMinSalary();
		double maxSalary = searchParams.getMaxSalary();

		int limit = searchParams.getLimit();
		int offset = searchParams.getOffset();
		String sort = searchParams.getSort();

		// no search criteria specified, get everything
		boolean hasSearch = false;

		if (maxSalary != 0) {
			hasSearch = true;
		}
	
		// if no limit specified, retrieve by default value of 30
		if (limit == 0) {
			limit = 30;
		}

		// no sort specified, default to ascending, id
		if (null == sort || sort.isEmpty()) {
			sort = "+id";
		}
		System.out.println("sort " + sort);

		Direction sortDirection = null;
		char sortSymbol = sort.charAt(0);
		System.out.println("sortSymbol " + sortSymbol);
		if ('+' == sortSymbol) {
			sortDirection = Direction.ASC;
			System.out.println("asc");

		} else {
			System.out.println("desc");

			sortDirection = Direction.DESC;
		}

		String sortParameter = null;

		String sortParam = sort.substring(1);
		if (null == sortParam) {
			sortParameter = "id";
		} else {
			switch (sortParam) {
			case "id":
				sortParameter = "id";
				break;
			case "login":
				sortParameter = "login";
				break;
			case "name":
				sortParameter = "name";
				break;
			case "salary":
				sortParameter = "salary";
				break;
			default:
				sortParameter = "id";
			}
		}
		System.out.println("sortParameter " + sortParameter);

		Page<Employee> pageEmployees = null;

		if (hasSearch) {
			pageEmployees = employeeRepository.findEmployeesBySalaryBetween(minSalary, maxSalary,
					PageRequest.of(0, Integer.MAX_VALUE, sortDirection, sortParameter));
		} else {
			pageEmployees = employeeRepository
					.findAll(PageRequest.of(0, Integer.MAX_VALUE, sortDirection, sortParameter));
		}

		List<EmployeeVO> returnEmployeeList = new ArrayList<EmployeeVO>();
		if (null != pageEmployees) {
			List<Employee> employeeList = null;
			if (pageEmployees.getNumberOfElements() > offset) {
				if (pageEmployees.getNumberOfElements() < limit) {
					employeeList = pageEmployees.getContent().subList(offset, pageEmployees.getNumberOfElements());
				} else {
					employeeList = pageEmployees.getContent().subList(offset, limit + offset);
				}
				returnEmployeeList.addAll(UtilHelper.mapEmployeeListToVOList(employeeList));
			}
		}
		ResponseVO returnResponse = new ResponseVO(true, returnEmployeeList);

		return returnResponse;
	}
}
