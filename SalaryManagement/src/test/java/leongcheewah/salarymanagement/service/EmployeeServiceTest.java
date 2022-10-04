package leongcheewah.salarymanagement.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import javax.validation.Validator;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import leongcheewah.salarymanagement.bean.Employee;
import leongcheewah.salarymanagement.model.EmployeeVO;
import leongcheewah.salarymanagement.model.ResponseVO;
import leongcheewah.salarymanagement.repository.EmployeeRepository;
import leongcheewah.salarymanagement.util.ResponseMessageConstants;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

	@Mock
	private EmployeeRepository employeeRepository;

	@InjectMocks
	private EmployeeServiceImpl employeeSvc;

	@Mock
	private Validator validator;

	private EmployeeVO testEmployeeVO1 = new EmployeeVO("suser01", "suser01", "Sample User 1", 1000.0);

	private Employee testEmployee1 = new Employee("suser01", "suser01", "Sample User 1", 1000.0);

	@Test
	void getEmployeeByIdServiceTest() {
		String testId = testEmployee1.getId();
		when(employeeRepository.findById(testId)).thenReturn(Optional.of(testEmployee1));

		ResponseVO result = employeeSvc.getEmployeeByEmpId(testId);
		assertThat(result.getResultObj()).usingRecursiveComparison().isEqualTo(testEmployee1);

		verify(employeeRepository).findById(testId);
	}
	
	@Test
	void getEmployeeByIdWithNonExistEmployeeIdTest() {
		ResponseVO result = employeeSvc.getEmployeeByEmpId("001");
		assertThat(result.getMsg()).isEqualTo(ResponseMessageConstants.DATA_ERROR_EMPLOYEE_NO_SUCH_EMPLOYEE);
	}

	@Test
	void updateEmployeesServiceTest() {
		when(employeeRepository.findById(testEmployee1.getId())).thenReturn(Optional.of(testEmployee1));
		ResponseVO result = employeeSvc.updateEmployee(testEmployee1.getId(), testEmployeeVO1);
		assertThat(result.getMsg()).isEqualTo(ResponseMessageConstants.SUCCESS_UPDATE);

		verify(employeeRepository).save(testEmployee1);
	}

	@Test
	void updateEmployeesServiceWithNonExistEmployeeIdTest() {
		ResponseVO result = employeeSvc.updateEmployee(null, testEmployeeVO1);
		assertThat(result.getMsg()).isEqualTo(ResponseMessageConstants.DATA_ERROR_EMPLOYEE_NO_SUCH_EMPLOYEE);
	}

	@Test
	void deleteEmployeesServiceTest() {
		when(employeeRepository.findById(testEmployee1.getId())).thenReturn(Optional.of(testEmployee1));
		ResponseVO result = employeeSvc.deleteEmployee(testEmployee1.getId());
		assertThat(result.getMsg()).isEqualTo(ResponseMessageConstants.SUCCESS_DELETE);

		verify(employeeRepository).delete(testEmployee1);
	}

	@Test
	void deleteEmployeesServiceTestWithNonExistEmployeeIdTest() {
		ResponseVO result = employeeSvc.deleteEmployee("n0001");
		assertThat(result.getMsg()).isEqualTo(ResponseMessageConstants.DATA_ERROR_EMPLOYEE_NO_SUCH_EMPLOYEE);

	}
}
