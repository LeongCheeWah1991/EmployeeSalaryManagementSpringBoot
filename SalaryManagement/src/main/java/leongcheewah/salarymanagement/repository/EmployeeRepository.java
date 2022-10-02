package leongcheewah.salarymanagement.repository;

import org.springframework.data.repository.CrudRepository;

import leongcheewah.salarymanagement.bean.Employee;

public interface EmployeeRepository extends CrudRepository<Employee, String>{

}
