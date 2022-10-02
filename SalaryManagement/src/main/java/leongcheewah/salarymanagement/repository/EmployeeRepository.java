package leongcheewah.salarymanagement.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import leongcheewah.salarymanagement.bean.Employee;

public interface EmployeeRepository extends CrudRepository<Employee, String>, PagingAndSortingRepository<Employee, String>{

	Page<Employee> findEmployeesBySalaryBetween(double minSalary, double maxSalary, Pageable pageable);

}
