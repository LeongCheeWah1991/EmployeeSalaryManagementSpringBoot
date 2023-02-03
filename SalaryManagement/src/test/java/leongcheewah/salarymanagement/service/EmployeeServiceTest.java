package leongcheewah.salarymanagement.service;

import leongcheewah.salarymanagement.bean.Employee;
import leongcheewah.salarymanagement.model.EmployeeVO;
import leongcheewah.salarymanagement.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.Validator;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

		EmployeeVO result = employeeSvc.getEmployeeByEmpId(testId);
		assertThat(result).usingRecursiveComparison().isEqualTo(testEmployee1);

		verify(employeeRepository).findById(testId);
	}

	@Test
	void updateEmployeesServiceTest() {
		when(employeeRepository.findById(testEmployee1.getId())).thenReturn(Optional.of(testEmployee1));
		employeeSvc.updateEmployee(testEmployee1.getId(), testEmployeeVO1);

		verify(employeeRepository).save(testEmployee1);
	}

	@Test
	void deleteEmployeesServiceTest() {
		when(employeeRepository.findById(testEmployee1.getId())).thenReturn(Optional.of(testEmployee1));
		employeeSvc.deleteEmployee(testEmployee1.getId());

		verify(employeeRepository).delete(testEmployee1);
	}

}
