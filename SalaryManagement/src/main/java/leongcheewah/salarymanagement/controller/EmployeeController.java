package leongcheewah.salarymanagement.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import leongcheewah.salarymanagement.model.EmployeeSearchParamsVO;
import leongcheewah.salarymanagement.model.EmployeeVO;
import leongcheewah.salarymanagement.model.ResponseVO;
import leongcheewah.salarymanagement.service.EmployeeService;

@RestController
@RequestMapping(path = "/users")
public class EmployeeController {

	@Autowired
	private EmployeeService employeeSvc;
	
	@PostMapping(path = "/upload", consumes = "multipart/form-data", produces = "application/json")
	public ResponseEntity<Object> uploadEmployees(@RequestParam("file") MultipartFile file) {
		ResponseVO returnObj = employeeSvc.uploadEmployees(file);

		Map<String, Object> returnMsgObj = new HashMap<String, Object>();
		returnMsgObj.put("message", returnObj.getMsg());

		if (returnObj.isSuccess()) {
			return ResponseEntity.status(HttpStatus.OK).body(returnMsgObj);
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(returnMsgObj);
		}
	}
	
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
	public ResponseEntity<Object> getAllEmployees() {

		List<EmployeeVO> employees = employeeSvc.getEmployees();

		return ResponseEntity.status(HttpStatus.OK).body(employees);
	}
	
	@GetMapping(path = "/{id}", produces = "application/json")
	public ResponseEntity<Object> getEmployeeById(@PathVariable String id) {

		ResponseVO returnObj = employeeSvc.getEmployeeByEmpId(id);
		
		if (returnObj.isSuccess()) {
			return ResponseEntity.status(HttpStatus.OK).body(returnObj.getResultObj());
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(returnObj.getMsg());
		}
	}
	
	@PostMapping(path = "/", produces = "application/json")
	public ResponseEntity<Object> insertEmployee(@RequestBody EmployeeVO employeeData) {

		ResponseVO returnObj = employeeSvc.insertEmployee(employeeData);

		Map<String, Object> returnMsgObj = new HashMap<String, Object>();
		returnMsgObj.put("message", returnObj.getMsg());

		if (returnObj.isSuccess()) {
			return ResponseEntity.status(HttpStatus.OK).body(returnMsgObj);
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(returnMsgObj);
		}
	}
	
	@PatchMapping(path = "/{id}", produces = "application/json")
	public ResponseEntity<Object> updateEmployee(@PathVariable String id, @RequestBody EmployeeVO employeeData) {

		ResponseVO returnObj = employeeSvc.updateEmployee(id, employeeData);

		Map<String, Object> returnMsgObj = new HashMap<String, Object>();
		returnMsgObj.put("message", returnObj.getMsg());

		if (returnObj.isSuccess()) {
			return ResponseEntity.status(HttpStatus.OK).body(returnMsgObj);
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(returnMsgObj);
		}
	}

	@DeleteMapping(path = "/{id}", produces = "application/json")
	public ResponseEntity<Object> deleteEmployee(@PathVariable String id) {

		ResponseVO returnObj = employeeSvc.deleteEmployee(id);

		Map<String, Object> returnMsgObj = new HashMap<String, Object>();
		returnMsgObj.put("message", returnObj.getMsg());

		if (returnObj.isSuccess()) {
			return ResponseEntity.status(HttpStatus.OK).body(returnMsgObj);
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(returnMsgObj);
		}
	}
}
