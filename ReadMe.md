---------------------------------------------------------------------------
Leong Chee Wah Employee Salary Management Assignment
---------------------------------------------------------------------------

This is the Backend component for the assignment. For the Frontend component, please refer to the Frontend Repo as follows:
https://github.com/LeongCheeWah1991/EmployeeSalaryManagementAngular

This project (Employee Salary Management) is a Spring boot Application that consists of the following features:
- US1. Upload CSV records of Employees
- US2. Fetch list of Employees by muliple criteria
- US3. CUD operations related to Employees
- US4. Uploading CSV records single thread (No Concurrent Uploads)

Spring Boot Version used: 2.7.4
---------------------------------------------------------------------------
Dependencies used:
---------------------------------------------------------------------------
1. spring-boot-starter-data-jpa
2. spring-boot-starter-web-services
3. spring-boot-starter-validation
4. spring-boot-starter-test
5. h2
6. commons-csv

---------------------------------------------------------------------------
Assumptions:
---------------------------------------------------------------------------
- For User Story 1: Upload Employees
1. Salary must be of correct format: Positive numbers with up to 2 Decimal places
Examples of salary with incorrect formats: -100, -100.555

2. During concurrent upload of files, if there is an ongoing upload, subsequent upload requests will receive error stating there is an ongoing upload.

---------------------------------------------------------------------------
Design:
---------------------------------------------------------------------------
Project structure:
Controller (endpoints) > Service (business logics) > Repository (data access)

- EmployeeController: Class for service endpoints and return respective responses
- EmployeeService: Interface for EmployeeService with methods definition
- EmployeeServiceImpl: Implements EmployeeService, handling business logics
- EmployeeRepository: extends CrudRepository for common crud methods and contains custom retrieval methods
- ResponseMessageConstants: contains constant variables used for formulating response messages
- CSVUtil: Util class for handling reading of CSV records
- Employee: Bean class for Employee
- EmployeeSearchParamsVO: VO class for EmployeeSearchParams
- EmployeeVO: VO class for Employee (Receive/Return data from FrontEnd)
- ResponseVO: VO class for returning Backend Responses to Frontend

Database: 
H2 Database Engine

Database Schema:
- id 	- Primary Key, String, stores Employee Id
- login 	- String, stores Employee Login
- name 	- String, stores Employee Name
- salary 	- double, stores Employee Salary

--------------------------------------------------------------------------
Cloning down the repository and setting up project
---------------------------------------------------------------------------
1. Git clone the repository: 
e.g. git clone https://github.com/LeongCheeWah1991/EmployeeSalaryManagementAngular.git
2. Import project to IDE
e.g. Eclipse -> Import projects > Maven > Existing Maven Projects
3. Browse to cloned folder, select /SalaryManagement/pom.xml
4. Click Finish

---------------------------------------------------------------------------
Running the application:
---------------------------------------------------------------------------
Using IDE (e.g. Eclipse)
1. At Eclipse, locate the following class: SalaryManagementApplication.java, 
2. Right-Click SalaryManagementApplication.java -> Run-as -> Java Application/ Spring boot App

---------------------------------------------------------------------------
Testing the application: (Using Postman)
---------------------------------------------------------------------------
1. At Postman, import the collection (SalaryManagement.postman_collection.json)
2. Test the respective service endpoints.
*For testing upload employees, create a csv file and select as file in postman (form-data)

---------------------------------------------------------------------------
JUnit Test classes
---------------------------------------------------------------------------
Run the tests in the respective Test classs with junit: Right-Click -> Run-as -> JUnit Test
- EmployeeControllerTest: Contains tests methods to test EmployeeController endpoints
- EmployeeServiceTest: Contains test methods to test EmployeeService methods and logics

---------------------------------------------------------------------------
Accessing H2 in-memory
---------------------------------------------------------------------------
In application.properties, "spring.h2.console.enabled" is set to true, this enables toe H2 console.

To access the H2 console:
1. After starting up the Spring Boot application, access: http://localhost:8080/h2-console/login.jsp?
2. Enter the following details as specified in application.properties:
    - JDBC URL: jdbc:h2:mem:salarymanagement
    - User Name: sa
    - Password: password
3. Click "Connect"
4. Click "EMPLOYEE" on the left, Click "RUN" to query the database
