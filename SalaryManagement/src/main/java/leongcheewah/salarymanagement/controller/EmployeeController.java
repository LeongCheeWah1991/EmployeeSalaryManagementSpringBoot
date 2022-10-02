package leongcheewah.salarymanagement.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import leongcheewah.salarymanagement.model.EmployeeSearchParamsVO;
import leongcheewah.salarymanagement.model.EmployeeVO;
import leongcheewah.salarymanagement.model.ResponseVO;
import leongcheewah.salarymanagement.service.EmployeeService;

@RestController
@RequestMapping(path = "/users")
public class EmployeeController {

	@Autowired
	private EmployeeService employeeSvc;
	
	@GetMapping(path = "/", produces = "application/json")
	public ResponseEntity<Object> searchEmployees(
			@RequestParam (required=false) Double minSalary, 
			@RequestParam (required=false) Double maxSalary,
			@RequestParam (required=false) Integer offset, 
			@RequestParam (required=false) Integer limit, 
			@RequestParam (required=false) String sort) {

		EmployeeSearchParamsVO searchParams = new EmployeeSearchParamsVO();
		searchParams.setMinSalary(minSalary);
		searchParams.setMaxSalary(maxSalary);
		searchParams.setOffset(offset);
		searchParams.setLimit(limit);
		searchParams.setSort(sort);
		
		ResponseVO returnObj = employeeSvc.searchEmployees(searchParams);

		Map<String, Object> returnMsgObj = new HashMap<String, Object>();

		if (returnObj.isSuccess()) {
			returnMsgObj.put("results", returnObj.getResultObj());
			return ResponseEntity.status(HttpStatus.OK).body(returnMsgObj);
		} else {
			returnMsgObj.put("message", returnObj.getMsg());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(returnMsgObj);
		}
	}
	
	@GetMapping(path = "/getall", produces = "application/json")
	public ResponseEntity<Object> tempGetAllEmployees() {

		List<EmployeeVO> employees = employeeSvc.getEmployees();

		return ResponseEntity.status(HttpStatus.OK).body(employees);
	}
}
