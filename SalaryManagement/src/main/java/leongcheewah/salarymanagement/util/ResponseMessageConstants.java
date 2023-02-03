package leongcheewah.salarymanagement.util;

public class ResponseMessageConstants {

	public static final String EMPLOYEE_ID = "Employee Id";
	public static final String EMPLOYEE_LOGIN = "Employee Login";
	public static final String EMPLOYEE_NAME = "Employee Name";
	public static final String EMPLOYEE_SALARY = "salary";
	public static final String EMPLOYEE_STARTDATE = "date";
	
	public static final String MIN_SALARY = "minSalary";
	public static final String MAX_SALARY = "maxSalary";
	public static final String OFFSET = "offset";
	public static final String LIMIT = "limit";
	public static final String SORT = "sort";

	public static final String BAD_INPUT_ERROR = "Invalid ";
	public static final String MISSING_ERROR = "Missing ";
	public static final String DUPLICATE = "Duplicate ";
	public static final String ONGOING_UPLOAD = "Ongoing Upload";

	public static final String UPLOAD_ERROR = "Upload Failed - ";
	public static final String CSV_FILE_FORMAT_NOT_MATCH = "Invalid CSV file";
	public static final String CSV_FILE_IS_EMPTY = "Empty CSV file / No Records";
	public static final String CSV_HEADERS_DONT_MATCH_EXPECTED = "CSV headers does not match. Expected: [id, login, name, salary]";

	public static final String SUCCESS_UPLOAD = "Successfully uploaded";

	public static final String SUCCESS_CREATE = "Successfully created";
	public static final String SUCCESS_UPDATE = "Successfully updated";
	public static final String SUCCESS_DELETE = "Successfully deleted";
	
	public static final String ERROR_CREATE = "Error trying to create";
	public static final String ERROR_UPDATE = "Error trying to update";
	public static final String ERROR_DELETE = "Error trying to delete";

	public static final String DATA_ERROR_EMPLOYEE_NO_SUCH_EMPLOYEE = "No such employee";
	public static final String DATA_ERROR_EMPLOYEE_ID_EXISTS = "Employee ID already exists";
	public static final String DATA_ERROR_EMPLOYEE_LOGIN_NOT_UNIQUE = "Employee login not unique";
	public static final String DATA_ERROR_FAILED_TO_RETRIEVE = "Failed to retrieve employee";

	public static final String RESPONSE_ERROR_CODE = "500";

}
