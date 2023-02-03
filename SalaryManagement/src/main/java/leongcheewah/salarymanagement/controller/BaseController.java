package leongcheewah.salarymanagement.controller;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import leongcheewah.salarymanagement.exception.EmployeeException;
import leongcheewah.salarymanagement.exception.ExceptionResponseVO;
import leongcheewah.salarymanagement.util.ResponseMessageConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static leongcheewah.salarymanagement.util.ResponseMessageConstants.RESPONSE_ERROR_CODE;

public class BaseController {
    private static final Logger logger = LoggerFactory.getLogger(BaseController.class);

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ EmployeeException.class })
    public Object handleEmployeeException(EmployeeException empEx) {
        empEx.printStackTrace();
        return new ExceptionResponseVO(RESPONSE_ERROR_CODE, empEx.errorMsg);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Object parameterExceptionHandler(MethodArgumentNotValidException e) {

        List<String> errList = new ArrayList<String>();
        BindingResult exceptions = e.getBindingResult();
        if (exceptions.hasErrors()) {
            List<ObjectError> errors = exceptions.getAllErrors();
            if (!errors.isEmpty()) {
                for (ObjectError error : errors) {
                    FieldError fieldError = (FieldError) error;
                    String errMsg = fieldError.getDefaultMessage();
                    if (!errList.contains(errMsg)) {
                        errList.add(errMsg);
                    }
                }
            }
        }
        Map<String, Object> returnMsgObj = new HashMap<String, Object>();
        returnMsgObj.put("messages", errList);

        return returnMsgObj;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidFormatException.class)
    public Object parameterExceptionHandler(InvalidFormatException e) {

        String fieldName = e.getPath().get(0).getFieldName();
        Map<String, Object> returnMsgObj = new HashMap<String, Object>();
        returnMsgObj.put("message", ResponseMessageConstants.BAD_INPUT_ERROR + fieldName);

        return returnMsgObj;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public Object parameterExceptionHandler(MethodArgumentTypeMismatchException e) {

        String fieldName = e.getName();
        Map<String, Object> returnMsgObj = new HashMap<String, Object>();
        returnMsgObj.put("message", ResponseMessageConstants.BAD_INPUT_ERROR + fieldName);

        return returnMsgObj;
    }
}
