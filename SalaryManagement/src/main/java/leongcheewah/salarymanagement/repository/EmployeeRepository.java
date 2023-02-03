package leongcheewah.salarymanagement.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import leongcheewah.salarymanagement.bean.Employee;

public interface EmployeeRepository extends CrudRepository<Employee, String>, PagingAndSortingRepository<Employee, String>{

	Optional<Employee> findByLogin(String login);
	
	boolean existsByLogin(String login);

	Page<Employee> findEmployeesBySalaryBetween(double minSalary, double maxSalary, Pageable pageable);

}
