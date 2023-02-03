package leongcheewah.salarymanagement.service;

import leongcheewah.salarymanagement.bean.Employee;
import leongcheewah.salarymanagement.exception.EmployeeException;
import leongcheewah.salarymanagement.model.EmployeeSearchParamsVO;
import leongcheewah.salarymanagement.model.EmployeeVO;
import leongcheewah.salarymanagement.repository.EmployeeRepository;
import leongcheewah.salarymanagement.util.CSVUtil;
import leongcheewah.salarymanagement.util.ResponseMessageConstants;
import leongcheewah.salarymanagement.util.UtilHelper;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.*;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private Validator validator;

    @Autowired
    private EmployeeRepository employeeRepository;

    public void uploadEmployees(MultipartFile file) {

        try {

            if (!CSVUtil.validateCSVFileFormat(file)) {
                throw new EmployeeException(ResponseMessageConstants.CSV_FILE_FORMAT_NOT_MATCH);
            }

            if (file.isEmpty()) {
                throw new EmployeeException(ResponseMessageConstants.CSV_FILE_IS_EMPTY);
            }

            if (!CSVUtil.validateCsvHeaders(file)) {
                throw new EmployeeException(ResponseMessageConstants.CSV_HEADERS_DONT_MATCH_EXPECTED);
            }

            List<CSVRecord> csvRecords = CSVUtil.parseCsv(file);
            if (csvRecords == null || csvRecords.isEmpty()) {
                throw new EmployeeException(ResponseMessageConstants.UPLOAD_ERROR + ResponseMessageConstants.CSV_FILE_IS_EMPTY);
            }

            Set<String> csvIdListSet = new HashSet<>();
            List<String> csvLoginList = new ArrayList<>();
            List<Employee> employeeList = new ArrayList<>();

            int recordNo = 1;
            for (CSVRecord csvRecord : csvRecords) {
                recordNo++;
                String employeeId = csvRecord.get("id");

                if (employeeId.startsWith("#")) {
                    continue;
                }

                if (csvIdListSet.contains(employeeId)) {
                    throw new EmployeeException(ResponseMessageConstants.DUPLICATE + " "
                                                + ResponseMessageConstants.EMPLOYEE_ID + " at Row: " + recordNo);
                }

                String employeeLogin = csvRecord.get("login");

                if (csvLoginList.contains(employeeLogin)) {
                    throw new EmployeeException(ResponseMessageConstants.DUPLICATE + " "
                                                + ResponseMessageConstants.EMPLOYEE_LOGIN + " at Row: " + recordNo);
                }

                String employeeName = csvRecord.get("name");
                String salaryStr = csvRecord.get("salary");

                double salary = 0;
                try {
                    salary = Double.parseDouble(salaryStr);
                } catch (Exception ex) {
                    throw new EmployeeException(ResponseMessageConstants.BAD_INPUT_ERROR
                                                + ResponseMessageConstants.EMPLOYEE_SALARY + " at Row: " + recordNo);
                }

                EmployeeVO employeeData = new EmployeeVO(employeeId, employeeLogin, employeeName, salary);
                String violationMsg = validateEmployeeVO(employeeData);

                if (null != violationMsg) {
                    throw new EmployeeException(violationMsg);
                }

                Employee employee = new Employee(employeeId, employeeLogin, employeeName, salary);

                employeeList.add(employee);
                csvIdListSet.add(employeeData.getId());
                csvLoginList.add(employeeData.getLogin());
            }

            List<Employee> employeeListForCreate = new ArrayList<Employee>();
            List<Employee> employeeListForUpdate = new ArrayList<Employee>();

            for (Employee employee : employeeList) {
                String employeeId = employee.getId();
                Employee existingEmployee = getEmployeeById(employeeId);

                if (null == existingEmployee) {
                    String validateResult = null;
                    if (isEmployeeExistByLogin(employee.getLogin())) {
                        validateResult = ResponseMessageConstants.DATA_ERROR_EMPLOYEE_LOGIN_NOT_UNIQUE;
                    }

                    if (null != validateResult) {
                        throw new EmployeeException(validateResult);
                    }
                    employeeListForCreate.add(employee);
                } else {

                    if (!existingEmployee.getId().equals(employee.getId())) {
                        String login = employee.getLogin();
                        if (!existingEmployee.getLogin().equals(login)) {
                            if (isEmployeeExistByLogin(login)) {
                                throw new EmployeeException(ResponseMessageConstants.DATA_ERROR_EMPLOYEE_LOGIN_NOT_UNIQUE);
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
        } catch (EmployeeException empEx) {
            empEx.printStackTrace();
            throw new EmployeeException(empEx.errorMsg);
        } catch (Exception ex) {
            throw new EmployeeException(ResponseMessageConstants.UPLOAD_ERROR);
        }
    }

    @Override
    public List<EmployeeVO> getEmployees() {
        List<EmployeeVO> returnEmployeeList = null;
        try {
            List<Employee> employeeList = (List<Employee>) employeeRepository.findAll();
            returnEmployeeList = UtilHelper.mapEmployeeListToVOList(employeeList);

        } catch (Exception ex) {
            throw new EmployeeException(ResponseMessageConstants.DATA_ERROR_FAILED_TO_RETRIEVE);
        }
        return returnEmployeeList;
    }

    @Override
    public List<EmployeeVO> searchEmployees(EmployeeSearchParamsVO searchParams) {

        String violationMsg = null;
        List<EmployeeVO> returnEmployeeList = new ArrayList<EmployeeVO>();

        try {
            Set<ConstraintViolation<EmployeeSearchParamsVO>> violations = validator.validate(searchParams);

            if (!violations.isEmpty()) {
                for (ConstraintViolation<EmployeeSearchParamsVO> violation : violations) {
                    violationMsg = violation.getMessage();
                }

                throw new EmployeeException(violationMsg);
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

            if (null != pageEmployees) {
                List<Employee> employeeList = null;
                if (pageEmployees.getNumberOfElements() > offset) {
                    if (pageEmployees.getNumberOfElements() < limit + offset) {
                        employeeList = pageEmployees.getContent().subList(offset, pageEmployees.getNumberOfElements());
                    } else {
                        employeeList = pageEmployees.getContent().subList(offset, limit + offset);
                    }
                    returnEmployeeList.addAll(UtilHelper.mapEmployeeListToVOList(employeeList));
                }
            }
        } catch (EmployeeException empEx) {
            throw new EmployeeException(empEx.errorMsg);
        } catch (Exception ex) {
            throw new EmployeeException(ResponseMessageConstants.DATA_ERROR_FAILED_TO_RETRIEVE);
        }

        return returnEmployeeList;
    }

    @Override
    public EmployeeVO getEmployeeByEmpId(String empId) {

        Optional<Employee> optionalEmployee = employeeRepository.findById(empId);

        EmployeeVO returnEmployee = null;

        try {
            if (!optionalEmployee.isEmpty()) {
                returnEmployee = UtilHelper.mapEmployeeToVO(optionalEmployee.get());
            } else {
                throw new EmployeeException(ResponseMessageConstants.DATA_ERROR_EMPLOYEE_NO_SUCH_EMPLOYEE);
            }
        } catch (EmployeeException empEx) {
            throw new EmployeeException(empEx.errorMsg);
        } catch (Exception ex) {
            throw new EmployeeException(ResponseMessageConstants.DATA_ERROR_FAILED_TO_RETRIEVE);

        }
        return returnEmployee;
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
    public void insertEmployee(EmployeeVO employeeData) {
        try {
            String violationMsg = validateEmployeeVO(employeeData);
            if (null != violationMsg) {
                throw new EmployeeException(violationMsg);
            }

            String id = employeeData.getId();
            String login = employeeData.getLogin();

            String validateResult = validateEmployeeIdAndLoginForInsert(id, login);

            if (null != validateResult) {
                throw new EmployeeException(validateResult);
            }

            Employee createEmployee = new Employee(id, login, employeeData.getName(), employeeData.getSalary());
            employeeRepository.save(createEmployee);
        } catch (EmployeeException empEx) {
            throw new EmployeeException(empEx.errorMsg);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new EmployeeException(ResponseMessageConstants.ERROR_CREATE);
        }
    }

    @Override
    public void updateEmployee(String id, EmployeeVO employeeData) {
        try {
            Employee existingEmployee = getEmployeeById(id);

            if (null == existingEmployee) {
                throw new EmployeeException(ResponseMessageConstants.DATA_ERROR_EMPLOYEE_NO_SUCH_EMPLOYEE);
            }

            String violationMsg = validateEmployeeVO(employeeData);
            if (null != violationMsg) {
                throw new EmployeeException(violationMsg);
            }

            String login = employeeData.getLogin();
            if (!existingEmployee.getLogin().equals(login)) {
                if (isEmployeeExistByLogin(login)) {
                    throw new EmployeeException(ResponseMessageConstants.DATA_ERROR_EMPLOYEE_LOGIN_NOT_UNIQUE);
                }
                existingEmployee.setLogin(login);
            }

            existingEmployee.setName(employeeData.getName());
            existingEmployee.setSalary(employeeData.getSalary());
            employeeRepository.save(existingEmployee);

        } catch (EmployeeException empEx) {
            throw new EmployeeException(empEx.errorMsg);
        } catch (Exception ex) {
            throw new EmployeeException(ResponseMessageConstants.ERROR_UPDATE);
        }
    }

    @Override
    public void deleteEmployee(String id) {
        try {
            Employee existingEmployee = getEmployeeById(id);

            if (null == existingEmployee) {
                throw new EmployeeException(ResponseMessageConstants.DATA_ERROR_EMPLOYEE_NO_SUCH_EMPLOYEE);
            }
            employeeRepository.delete(existingEmployee);

        } catch (EmployeeException empEx) {
            throw new EmployeeException(empEx.errorMsg);
        } catch (Exception ex) {
            throw new EmployeeException(ResponseMessageConstants.ERROR_DELETE);
        }
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

    public boolean isEmployeeExistByLogin(String login) {
        return employeeRepository.existsByLogin(login);
    }

    public String validateEmployeeIdAndLoginForInsert(String id, String login) {

        if (!isEmployeeIdUnique(id)) {
            return ResponseMessageConstants.DATA_ERROR_EMPLOYEE_ID_EXISTS;
        }

        if (isEmployeeExistByLogin(login)) {
            return ResponseMessageConstants.DATA_ERROR_EMPLOYEE_LOGIN_NOT_UNIQUE;
        }

        return null;
    }
}
