package leongcheewah.salarymanagement.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import leongcheewah.salarymanagement.model.EmployeeVO;
import leongcheewah.salarymanagement.service.EmployeeService;

@RestController
@RequestMapping(path = "/users")
public class EmployeeController {

	@Autowired
	private EmployeeService employeeSvc;
	
	@GetMapping(path = "/getall", produces = "application/json")
	public ResponseEntity<Object> tempGetAllEmployees() {

		List<EmployeeVO> employees = employeeSvc.getEmployees();

		return ResponseEntity.status(HttpStatus.OK).body(employees);
	}
}
