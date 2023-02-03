package leongcheewah.salarymanagement.model;

public class ResponseVO {

	private boolean success;
	private String msg;
	private Object resultObj;

	public ResponseVO() {

	}

	public ResponseVO(boolean success, String msg) {
		super();
		this.success = success;
		this.msg = msg;
	}

	public ResponseVO(boolean success, Object resultObj) {
		super();
		this.success = success;
		this.resultObj = resultObj;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getResultObj() {
		return resultObj;
	}

	public void setResultObj(Object resultObj) {
		this.resultObj = resultObj;
	}

	@Override
	public String toString() {
		return "ResponseVO [success=" + success + ", msg=" + msg + "]";
	}

}
