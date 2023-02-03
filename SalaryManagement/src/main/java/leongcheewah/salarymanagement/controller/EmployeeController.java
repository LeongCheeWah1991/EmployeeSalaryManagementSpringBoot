package leongcheewah.salarymanagement.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import leongcheewah.salarymanagement.bean.Employee;
import leongcheewah.salarymanagement.exception.EmployeeException;
import leongcheewah.salarymanagement.exception.ExceptionResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;

import leongcheewah.salarymanagement.model.EmployeeSearchParamsVO;
import leongcheewah.salarymanagement.model.EmployeeVO;
import leongcheewah.salarymanagement.model.ResponseVO;
import leongcheewah.salarymanagement.service.EmployeeService;
import leongcheewah.salarymanagement.service.UploadService;
import leongcheewah.salarymanagement.util.ResponseMessageConstants;

import static leongcheewah.salarymanagement.util.ResponseMessageConstants.*;

@RestController
@Validated
@RequestMapping(path = "/users")
public class EmployeeController extends BaseController {

    @Autowired
    private EmployeeService employeeSvc;

    @Autowired
    private UploadService uploadSvc;

    @PostMapping(path = "/upload", consumes = "multipart/form-data", produces = "application/json")
    public ResponseEntity<Object> uploadEmployees(@RequestParam("file") MultipartFile file) {

        ResponseVO returnObj = null;

        Map<String, Object> returnMsgObj = new HashMap<String, Object>();

        if (uploadSvc.getUploadStatus()) {
            // ongoing upload, return msg and uploadStatus
            returnMsgObj.put("errMsg",
                    ResponseMessageConstants.UPLOAD_ERROR + ResponseMessageConstants.ONGOING_UPLOAD);
            returnMsgObj.put("uploadStatus", false);

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(returnMsgObj);
        } else {
            // means no ongoing upload, continue

            try {
                // set to ongoing
                uploadSvc.setUploadStatus(true);
                employeeSvc.uploadEmployees(file);
                returnMsgObj.put("message", SUCCESS_UPLOAD);
                try {
                    Thread.sleep(5000);
                    uploadSvc.setUploadStatus(false);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                uploadSvc.setUploadStatus(false);
            }

            return ResponseEntity.status(HttpStatus.OK).body(returnMsgObj);
        }
    }

    @GetMapping(path = "/", produces = "application/json")
    public ResponseEntity<Object> searchEmployees(@RequestParam(required = false) Double minSalary,
                                                  @RequestParam(required = false) Double maxSalary, @RequestParam(required = false) Integer offset,
                                                  @RequestParam(required = false) Integer limit, @RequestParam(required = false) String sort) {

        Map<String, Object> returnMsgObj = new HashMap<String, Object>();

        EmployeeSearchParamsVO searchParams = new EmployeeSearchParamsVO();
        searchParams.setMinSalary(minSalary);
        searchParams.setMaxSalary(maxSalary);
        searchParams.setOffset(offset);
        searchParams.setLimit(limit);
        searchParams.setSort(sort);

        List<EmployeeVO> returnObjList = employeeSvc.searchEmployees(searchParams);
        returnMsgObj.put("results", returnObjList);

        return ResponseEntity.status(HttpStatus.OK).body(returnMsgObj);
    }

    @GetMapping(path = "/getall", produces = "application/json")
    public ResponseEntity<Object> getAllEmployees() {

        List<EmployeeVO> employees = employeeSvc.getEmployees();

        return ResponseEntity.status(HttpStatus.OK).body(employees);
    }

    @GetMapping(path = "/{id}", produces = "application/json")
    public ResponseEntity<Object> getEmployeeById(@PathVariable String id) {

        EmployeeVO returnObj = employeeSvc.getEmployeeByEmpId(id);

        return ResponseEntity.status(HttpStatus.OK).body(returnObj);
    }

    @PostMapping(path = "/", produces = "application/json")
    public ResponseEntity<Object> insertEmployee(@Valid @RequestBody EmployeeVO employeeData) {
        employeeSvc.insertEmployee(employeeData);

        Map<String, Object> returnMsgObj = new HashMap<String, Object>();
        returnMsgObj.put("message", SUCCESS_CREATE);

        return ResponseEntity.status(HttpStatus.OK).body(returnMsgObj);
    }

    @PatchMapping(path = "/{id}", produces = "application/json")
    public ResponseEntity<Object> updateEmployee(@PathVariable String id, @Valid @RequestBody EmployeeVO
            employeeData) {

        employeeSvc.updateEmployee(id, employeeData);

        Map<String, Object> returnMsgObj = new HashMap<String, Object>();
        returnMsgObj.put("message", SUCCESS_UPDATE);

        return ResponseEntity.status(HttpStatus.OK).body(returnMsgObj);
    }

    @DeleteMapping(path = "/{id}", produces = "application/json")
    public ResponseEntity<Object> deleteEmployee(@PathVariable String id) {

        employeeSvc.deleteEmployee(id);

        Map<String, Object> returnMsgObj = new HashMap<String, Object>();
        returnMsgObj.put("message", SUCCESS_DELETE);

        return ResponseEntity.status(HttpStatus.OK).body(returnMsgObj);

    }

//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(EmployeeException.class)
//    public ResponseEntity<Object> handleException() {
//        System.out.println("HEREEEEEEE");
//        ExceptionResponseVO temp = new ExceptionResponseVO("errorCode", "errorMsg");
//
//        Map<String, Object> returnMsgObj = new HashMap<String, Object>();
//        returnMsgObj.put("temp", temp);
//
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(returnMsgObj);
//    }

//	@ResponseStatus(HttpStatus.BAD_REQUEST)
//	@ExceptionHandler(MethodArgumentNotValidException.class)
//	public Object parameterExceptionHandler(MethodArgumentNotValidException e) {
//
//		List<String> errList = new ArrayList<String>();
//		BindingResult exceptions = e.getBindingResult();
//		if (exceptions.hasErrors()) {
//			List<ObjectError> errors = exceptions.getAllErrors();
//			if (!errors.isEmpty()) {
//				for (ObjectError error : errors) {
//					FieldError fieldError = (FieldError) error;
//					String errMsg = fieldError.getDefaultMessage();
//					if (!errList.contains(errMsg)) {
//						errList.add(errMsg);
//					}
//				}
//			}
//		}
//		Map<String, Object> returnMsgObj = new HashMap<String, Object>();
//		returnMsgObj.put("messages", errList);
//
//		return returnMsgObj;
//	}
//
//	@ResponseStatus(HttpStatus.BAD_REQUEST)
//	@ExceptionHandler(InvalidFormatException.class)
//	public Object parameterExceptionHandler(InvalidFormatException e) {
//
//		String fieldName = e.getPath().get(0).getFieldName();
//		Map<String, Object> returnMsgObj = new HashMap<String, Object>();
//		returnMsgObj.put("message", ResponseMessageConstants.BAD_INPUT_ERROR + fieldName);
//
//		return returnMsgObj;
//	}
//
//	@ResponseStatus(HttpStatus.BAD_REQUEST)
//	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
//	public Object parameterExceptionHandler(MethodArgumentTypeMismatchException e) {
//
//		String fieldName = e.getName();
//		Map<String, Object> returnMsgObj = new HashMap<String, Object>();
//		returnMsgObj.put("message", ResponseMessageConstants.BAD_INPUT_ERROR + fieldName);
//
//		return returnMsgObj;
//	}
}
