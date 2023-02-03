package leongcheewah.salarymanagement.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;

import leongcheewah.salarymanagement.model.EmployeeVO;
import leongcheewah.salarymanagement.model.ResponseVO;
import leongcheewah.salarymanagement.service.EmployeeService;
import leongcheewah.salarymanagement.service.UploadService;

@WebMvcTest(value = EmployeeController.class)
public class EmployeeControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private EmployeeService employeeSvc;

	@MockBean
	private UploadService uploadSvc;

	@Test
	void mockMvcCreated() {
		assertThat(mockMvc).isNotNull();
	}

	private EmployeeVO testEmployee1 = new EmployeeVO("suser01", "suser01", "Sample User 1", 1000.0);

	@Test
	public void getEmployeeByIdControllerTest() throws Exception {

		String testId = testEmployee1.getId();

		when(employeeSvc.getEmployeeByEmpId(testId)).thenReturn(testEmployee1);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/users/{id}", testId)
				.accept(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType("application/json")).andReturn();

		String responseResult = result.getResponse().getContentAsString();
		EmployeeVO resultEmployee = objectMapper.readValue(responseResult, EmployeeVO.class);
		assertThat(resultEmployee).usingRecursiveComparison().isEqualTo(testEmployee1);

		verify(employeeSvc).getEmployeeByEmpId(testId);
	}

}
