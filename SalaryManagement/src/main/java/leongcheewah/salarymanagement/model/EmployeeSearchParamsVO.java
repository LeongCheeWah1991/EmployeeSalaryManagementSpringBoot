package leongcheewah.salarymanagement.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import leongcheewah.salarymanagement.util.ResponseMessageConstants;

public class EmployeeSearchParamsVO {

	@NotNull(message = ResponseMessageConstants.MISSING_ERROR + ResponseMessageConstants.MIN_SALARY)
	@Min(value = 0, message = ResponseMessageConstants.BAD_INPUT_ERROR + ResponseMessageConstants.EMPLOYEE_SALARY)
	private Double minSalary;

	@NotNull(message = ResponseMessageConstants.MISSING_ERROR + ResponseMessageConstants.MAX_SALARY)
	@Min(value = 0, message = ResponseMessageConstants.BAD_INPUT_ERROR + ResponseMessageConstants.EMPLOYEE_SALARY)
	private Double maxSalary;

	@NotNull(message = ResponseMessageConstants.MISSING_ERROR + ResponseMessageConstants.OFFSET)
	@Min(value = 0, message = ResponseMessageConstants.BAD_INPUT_ERROR + ResponseMessageConstants.OFFSET)
	private Integer offset;

	@NotNull(message = ResponseMessageConstants.MISSING_ERROR + ResponseMessageConstants.LIMIT)
	@Min(value = 0, message = ResponseMessageConstants.BAD_INPUT_ERROR + ResponseMessageConstants.LIMIT)
	private Integer limit;

	@NotEmpty(message = ResponseMessageConstants.MISSING_ERROR + ResponseMessageConstants.SORT)
	@NotNull(message = ResponseMessageConstants.MISSING_ERROR + ResponseMessageConstants.SORT)
	private String sort;

	public EmployeeSearchParamsVO() {
	}

	public EmployeeSearchParamsVO(
			@NotNull(message = "Missing minSalary") @Min(value = 0, message = "Invalid salary") Double minSalary,
			@NotNull(message = "Missing maxSalary") @Min(value = 0, message = "Invalid salary") Double maxSalary,
			@NotNull(message = "Missing offset") @Min(value = 0, message = "Invalid offset") Integer offset,
			@NotNull(message = "Missing limit") @Min(value = 0, message = "Invalid limit") Integer limit,
			@NotEmpty(message = "Missing sort") @NotNull(message = "Missing sort") String sort) {
		super();
		this.minSalary = minSalary;
		this.maxSalary = maxSalary;
		this.offset = offset;
		this.limit = limit;
		this.sort = sort;
	}

	public Double getMinSalary() {
		return minSalary;
	}

	public void setMinSalary(Double minSalary) {
		this.minSalary = minSalary;
	}

	public Double getMaxSalary() {
		return maxSalary;
	}

	public void setMaxSalary(Double maxSalary) {
		this.maxSalary = maxSalary;
	}

	public Integer getOffset() {
		return offset;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	@Override
	public String toString() {
		return "EmployeeSearchParamsVO [minSalary=" + minSalary + ", maxSalary=" + maxSalary + ", offset=" + offset
				+ ", limit=" + limit + ", sort=" + sort + "]";
	}

}
