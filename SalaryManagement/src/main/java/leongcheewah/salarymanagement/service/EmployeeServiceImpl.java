package leongcheewah.salarymanagement.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import leongcheewah.salarymanagement.bean.Employee;
import leongcheewah.salarymanagement.model.EmployeeSearchParamsVO;
import leongcheewah.salarymanagement.model.EmployeeVO;
import leongcheewah.salarymanagement.model.ResponseVO;
import leongcheewah.salarymanagement.repository.EmployeeRepository;
import leongcheewah.salarymanagement.util.CSVUtil;
import leongcheewah.salarymanagement.util.ResponseMessageConstants;
import leongcheewah.salarymanagement.util.UtilHelper;

@Service
public class EmployeeServiceImpl implements EmployeeService {

	@Autowired
	private Validator validator;

	@Autowired
	private EmployeeRepository employeeRepository;

	public ResponseVO uploadEmployees(MultipartFile file) {

		try {
			if (!CSVUtil.validateCSVFileFormat(file)) {
				return new ResponseVO(false,
						ResponseMessageConstants.UPLOAD_ERROR + ResponseMessageConstants.CSV_FILE_FORMAT_NOT_MATCH);
			}

			if (file.isEmpty()) {
				return new ResponseVO(false,
						ResponseMessageConstants.UPLOAD_ERROR + ResponseMessageConstants.CSV_FILE_IS_EMPTY);
			}

			List<String> csvIdList = new ArrayList<String>();
			List<String> csvLoginList = new ArrayList<String>();

			List<Employee> employeeList = new ArrayList<Employee>();
			List<CSVRecord> csvRecords = CSVUtil.parseCsv(file);
			for (CSVRecord csvRecord : csvRecords) {
				String employeeId = csvRecord.get("id");

				if (employeeId.startsWith("#")) {
					continue;
				}

				if (csvIdList.contains(employeeId)) {
					return new ResponseVO(false, ResponseMessageConstants.UPLOAD_ERROR
							+ ResponseMessageConstants.DUPLICATE + " " + ResponseMessageConstants.EMPLOYEE_ID);
				}

				String employeeLogin = csvRecord.get("login");

				if (csvLoginList.contains(employeeLogin)) {
					return new ResponseVO(false, ResponseMessageConstants.UPLOAD_ERROR
							+ ResponseMessageConstants.DUPLICATE + " " + ResponseMessageConstants.EMPLOYEE_LOGIN);
				}

				String employeeName = csvRecord.get("name");
				String salaryStr = csvRecord.get("salary");

				double salary = 0;
				try {
					salary = Double.parseDouble(salaryStr);
				} catch (Exception ex) {
					return new ResponseVO(false,
							ResponseMessageConstants.BAD_INPUT_ERROR + ResponseMessageConstants.EMPLOYEE_SALARY);
				}

				EmployeeVO employeeData = new EmployeeVO(employeeId, employeeLogin, employeeName, salary);
				String violationMsg = validateEmployeeVO(employeeData);

				if (null != violationMsg) {
					return new ResponseVO(false, violationMsg);
				}

				Employee employee = new Employee(employeeId, employeeLogin, employeeName, salary);

				employeeList.add(employee);
				csvIdList.add(employeeData.getId());
				csvLoginList.add(employeeData.getLogin());
			}

			List<Employee> employeeListForCreate = new ArrayList<Employee>();
			List<Employee> employeeListForUpdate = new ArrayList<Employee>();

			for (Employee employee : employeeList) {
				String employeeId = employee.getId();
				Employee existingEmployee = getEmployeeById(employeeId);

				if (null == existingEmployee) {
					String validateResult = validateEmployeeIdAndLoginForInsert(employee.getId(), employee.getLogin());
					if (null != validateResult) {
						return new ResponseVO(false, validateResult);
					}
					employeeListForCreate.add(employee);
				} else {

					if (!existingEmployee.equals(employee)) {
						String login = employee.getLogin();
						if (!existingEmployee.getLogin().equals(login)) {
							if (!isEmployeeLoginUnique(login)) {
								return new ResponseVO(false,
										ResponseMessageConstants.DATA_ERROR_EMPLOYEE_LOGIN_NOT_UNIQUE);
							}
						}

						employeeListForUpdate.add(employee);
					}
				}
			}

			if (null != employeeListForCreate && !employeeListForCreate.isEmpty()) {
				employeeRepository.saveAll(employeeListForCreate);
			}

			if (null != employeeListForUpdate && !employeeListForUpdate.isEmpty()) {
				employeeRepository.saveAll(employeeListForUpdate);
			}
		} catch (Exception ex) {
			return new ResponseVO(false, ResponseMessageConstants.UPLOAD_ERROR);
		}

		return new ResponseVO(true, ResponseMessageConstants.SUCCESS_UPLOAD);

	}

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
		Direction sortDirection = null;
		char sortSymbol = sort.charAt(0);
		if ('+' == sortSymbol) {
			sortDirection = Direction.ASC;
		} else {
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

	@Override
	public ResponseVO getEmployeeByEmpId(String empId) {

		Optional<Employee> optionalEmployee = employeeRepository.findById(empId);

		EmployeeVO returnEmployee = null;

		try {
			if (!optionalEmployee.isEmpty()) {
				returnEmployee = UtilHelper.mapEmployeeToVO(optionalEmployee.get());
			} else {
				return new ResponseVO(false, ResponseMessageConstants.DATA_ERROR_EMPLOYEE_NO_SUCH_EMPLOYEE);
			}
		} catch (Exception ex) {
			return new ResponseVO(false, ResponseMessageConstants.DATA_ERROR_FAILED_TO_RETRIEVE);

		}
		return new ResponseVO(true, returnEmployee);
	}

	public Employee getEmployeeById(String id) {

		Optional<Employee> optionalEmployee = employeeRepository.findById(id);

		if (!optionalEmployee.isEmpty()) {
			return optionalEmployee.get();
		} else {
			return null;
		}
	}

	@Override
	public ResponseVO insertEmployee(EmployeeVO employeeData) {

		String violationMsg = validateEmployeeVO(employeeData);
		if (null != violationMsg) {
			return new ResponseVO(false, violationMsg);
		}

		String id = employeeData.getId();
		String login = employeeData.getLogin();

		String validateResult = validateEmployeeIdAndLoginForInsert(id, login);

		if (null != validateResult) {
			return new ResponseVO(false, validateResult);
		}

		try {
			Employee createEmployee = new Employee(id, login, employeeData.getName(), employeeData.getSalary());
			employeeRepository.save(createEmployee);

		} catch (Exception ex) {
			return new ResponseVO(false, ResponseMessageConstants.ERROR_CREATE);
		}

		return new ResponseVO(true, ResponseMessageConstants.SUCCESS_CREATE);
	}

	@Override
	public ResponseVO updateEmployee(String id, EmployeeVO employeeData) {

		Employee existingEmployee = getEmployeeById(id);

		if (null == existingEmployee) {
			return new ResponseVO(false, ResponseMessageConstants.DATA_ERROR_EMPLOYEE_NO_SUCH_EMPLOYEE);
		}

		String violationMsg = validateEmployeeVO(employeeData);
		if (null != violationMsg) {
			return new ResponseVO(false, violationMsg);
		}

		try {

			String login = employeeData.getLogin();
			if (!existingEmployee.getLogin().equals(login)) {
				if (!isEmployeeLoginUnique(login)) {
					return new ResponseVO(false, ResponseMessageConstants.DATA_ERROR_EMPLOYEE_LOGIN_NOT_UNIQUE);
				}
				existingEmployee.setLogin(login);
			}

			existingEmployee.setName(employeeData.getName());
			existingEmployee.setSalary(employeeData.getSalary());

			employeeRepository.save(existingEmployee);

		} catch (Exception ex) {
			return new ResponseVO(false, ResponseMessageConstants.ERROR_UPDATE);
		}

		return new ResponseVO(true, ResponseMessageConstants.SUCCESS_UPDATE);
	}

	@Override
	public ResponseVO deleteEmployee(String id) {

		Employee existingEmployee = getEmployeeById(id);

		if (null == existingEmployee) {
			return new ResponseVO(false, ResponseMessageConstants.DATA_ERROR_EMPLOYEE_NO_SUCH_EMPLOYEE);
		}

		try {
			employeeRepository.delete(existingEmployee);

		} catch (Exception ex) {
			return new ResponseVO(false, ResponseMessageConstants.ERROR_DELETE);
		}

		return new ResponseVO(true, ResponseMessageConstants.SUCCESS_DELETE);
	}

	public String validateEmployeeVO(EmployeeVO employeeData) {
		String violationMsg = null;

		Set<ConstraintViolation<EmployeeVO>> violations = validator.validate(employeeData);

		if (!violations.isEmpty()) {
			for (ConstraintViolation<EmployeeVO> violation : violations) {
				violationMsg = violation.getMessage();
			}
		}
		return violationMsg;
	}

	public boolean isEmployeeIdUnique(String id) {
		Optional<Employee> checkEmployeeExists = employeeRepository.findById(id);

		if (!checkEmployeeExists.isEmpty()) {
			return false;
		}
		return true;
	}

	public boolean isEmployeeLoginUnique(String login) {
		Optional<Employee> checkEmployeeExists = employeeRepository.findByLogin(login);

		if (!checkEmployeeExists.isEmpty()) {
			return false;
		}
		return true;
	}

	public String validateEmployeeIdAndLoginForInsert(String id, String login) {

		if (!isEmployeeIdUnique(id)) {
			return ResponseMessageConstants.DATA_ERROR_EMPLOYEE_ID_EXISTS;
		}

		if (!isEmployeeLoginUnique(login)) {
			return ResponseMessageConstants.DATA_ERROR_EMPLOYEE_LOGIN_NOT_UNIQUE;
		}

		return null;
	}
}
