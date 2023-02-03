package leongcheewah.salarymanagement.exception;

public class ExceptionResponseVO {

    public String errorCode;
    public String errorMsg;

    public ExceptionResponseVO() {
    }

    public ExceptionResponseVO(String errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
