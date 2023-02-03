package leongcheewah.salarymanagement.exception;

public class EmployeeException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public String errorMsg;
    public EmployeeException(){

    }

    public EmployeeException(String errorMsg){
        this.errorMsg = errorMsg;
    }
}
