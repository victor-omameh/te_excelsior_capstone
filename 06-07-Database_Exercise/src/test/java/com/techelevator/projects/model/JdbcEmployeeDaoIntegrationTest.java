package com.techelevator.projects.model;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.projects.model.jdbc.JDBCEmployeeDAO;

public class JdbcEmployeeDaoIntegrationTest {
	
	private static SingleConnectionDataSource dataSource;  // must be static
	private EmployeeDAO employeeDao;
	private DepartmentDAO departmentDao;
	private JdbcTemplate jdbcTemplate;
	
	private Long departmentId;
	
	/*
	 * STEP 2: Define a @BeforeClass that setups the datasource
	 */
	@BeforeClass
	public static void createDataSource() {
		dataSource = new SingleConnectionDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/projects");
		dataSource.setUsername("postgres");
		dataSource.setPassword("postgres1");
		dataSource.setAutoCommit(false);  // set autoCommit = false to create Transaction scope
	}
	
	
	@AfterClass
	public static void destroyDataSource() {
		dataSource.destroy();
	}
	
	
	@After
	public void rollbackTransation() throws SQLException {
		dataSource.getConnection().rollback();
	}
	
	@Before
	public void setup() {
		employeeDao = new JDBCEmployeeDAO(dataSource);
		jdbcTemplate = new JdbcTemplate(dataSource);
		
		Department dummyDepartment = new Department();
		dummyDepartment.setName("TestDepartment");
//		Department dummyDepartmentUpdate = departmentDao.createDepartment(dummyDepartment);
//		
		String sql = "INSERT INTO department (department_id, name) VALUES (DEFAULT, ?) RETURNING department_id";
		SqlRowSet rows = jdbcTemplate.queryForRowSet(sql, dummyDepartment.getName());
		
		rows.next();
		
		dummyDepartment.setId(rows.getLong("department_id"));
		
		
		
		departmentId = dummyDepartment.getId();
	}
	
	@Test
	public void get_employees_by_department_id() {
		
		Employee testEmployee = createEmployee();
		List<Employee> testEmployeeList = new ArrayList<Employee>();
		
		
		String sql = "INSERT INTO employee (employee_id, department_id, first_name, last_name, birth_date, gender, hire_date) VALUES (DEFAULT, ?, ?, ?, ?, ?, ?) RETURNING employee_id";
		SqlRowSet row = jdbcTemplate.queryForRowSet(sql, testEmployee.getDepartmentId(), testEmployee.getFirstName(), testEmployee.getLastName(), testEmployee.getBirthDay(), testEmployee.getGender(), testEmployee.getHireDate()); 
		row.next();
		testEmployee.setId(row.getLong("employee_id"));
		testEmployeeList.add(testEmployee);
		
		List<Employee> result = employeeDao.getEmployeesByDepartmentId(departmentId);
		
		Assert.assertEquals(testEmployeeList, result);
		
		
	}
	
	@Test
	public void get_all_employees_test() {
		Employee testEmployee = createEmployee();
		List<Employee> testEmployeeList = employeeDao.getAllEmployees();
		
		String sql = "INSERT INTO employee (employee_id, department_id, first_name, last_name, birth_date, gender, hire_date) VALUES (DEFAULT, ?, ?, ?, ?, ?, ?) RETURNING employee_id";
		SqlRowSet row = jdbcTemplate.queryForRowSet(sql, testEmployee.getDepartmentId(), testEmployee.getFirstName(), testEmployee.getLastName(), testEmployee.getBirthDay(), testEmployee.getGender(), testEmployee.getHireDate()); 
		row.next();
		testEmployee.setId(row.getLong("employee_id"));
		testEmployeeList.add(testEmployee);
		
		List<Employee> result = employeeDao.getAllEmployees();
		
		Assert.assertEquals(testEmployeeList, result);
		
	}
	
	@Test
	public void search_employees_by_name() {
		Employee testEmployee = createEmployee();
		List<Employee> testEmployeeList = new ArrayList<Employee>();
		
		String sql = "INSERT INTO employee (employee_id, department_id, first_name, last_name, birth_date, gender, hire_date) VALUES (DEFAULT, ?, ?, ?, ?, ?, ?) RETURNING employee_id";
		SqlRowSet row = jdbcTemplate.queryForRowSet(sql, testEmployee.getDepartmentId(), testEmployee.getFirstName(), testEmployee.getLastName(), testEmployee.getBirthDay(), testEmployee.getGender(), testEmployee.getHireDate()); 
		row.next();
		testEmployee.setId(row.getLong("employee_id"));
		testEmployeeList.add(testEmployee);
		
		List<Employee> result = employeeDao.searchEmployeesByName(testEmployee.getFirstName(), testEmployee.getLastName());
		
		Assert.assertEquals(testEmployeeList, result);
		
	}
	
	@Test
	public void get_employees_without_a_project() {
		
		Employee testEmployee = createEmployee();
		List<Employee> testEmployeeList = new ArrayList<Employee>();
		
		String sql = "INSERT INTO employee (employee_id, department_id, first_name, last_name, birth_date, gender, hire_date) VALUES (DEFAULT, ?, ?, ?, ?, ?, ?) RETURNING employee_id";
		SqlRowSet row = jdbcTemplate.queryForRowSet(sql, testEmployee.getDepartmentId(), testEmployee.getFirstName(), testEmployee.getLastName(), testEmployee.getBirthDay(), testEmployee.getGender(), testEmployee.getHireDate()); 
		row.next();
		testEmployee.setId(row.getLong("employee_id"));
		testEmployeeList.add(testEmployee);
	
		
		List<Employee> result = employeeDao.getEmployeesWithoutProjects();
		
		List<Employee> matchingResultList = new ArrayList<Employee>();
		
		for( Employee employee: result) {
			
			if(employee.getId().equals(testEmployee.getId())) {
				matchingResultList.add(employee);
			}
		}
		
		Assert.assertEquals(testEmployeeList, matchingResultList);
		
	}
	
	@Test
	public void get_employee_by_project_id() {
		
		Employee testEmployee = createEmployee();
		
		Project testProject = new Project();
		testProject.setName("TestProject");
		testProject.setStartDate(LocalDate.of(1953, 07, 15));
		testProject.setEndDate(LocalDate.of(2001, 04, 01));
		
		List<Employee> testEmployeeList = new ArrayList<Employee>();
		
		String sql = "INSERT INTO employee (employee_id, department_id, first_name, last_name, birth_date, gender, hire_date) VALUES (DEFAULT, ?, ?, ?, ?, ?, ?) RETURNING employee_id";
		SqlRowSet row = jdbcTemplate.queryForRowSet(sql, testEmployee.getDepartmentId(), testEmployee.getFirstName(), testEmployee.getLastName(), testEmployee.getBirthDay(), testEmployee.getGender(), testEmployee.getHireDate()); 
		row.next();
		testEmployee.setId(row.getLong("employee_id"));
		testEmployeeList.add(testEmployee);
		
		String sqlProject = "INSERT INTO project (project_id, name, from_date, to_date) VALUES (DEFAULT, ?, ?, ?) RETURNING project_id";
		SqlRowSet projectRow = jdbcTemplate.queryForRowSet(sqlProject, testProject.getName(), testProject.getStartDate(), testProject.getEndDate());
		projectRow.next();
		testProject.setId(projectRow.getLong("project_id"));
		
		
		String sqlProjectEmployee = "INSERT INTO project_employee (project_id, employee_id) VALUES (?, ?) RETURNING employee_id";
		jdbcTemplate.queryForRowSet(sqlProjectEmployee, testProject.getId(), testEmployee.getId());
		
		List<Employee> result = employeeDao.getEmployeesByProjectId(testProject.getId());
		
		Assert.assertEquals(testEmployeeList, result);
		
		
	}
	
	@Test
	public void change_employee_department_test() {
		
		Employee testEmployee = createEmployee();
		
		String sql = "INSERT INTO employee (employee_id, department_id, first_name, last_name, birth_date, gender, hire_date) VALUES (DEFAULT, ?, ?, ?, ?, ?, ?) RETURNING employee_id";
		SqlRowSet row = jdbcTemplate.queryForRowSet(sql, testEmployee.getDepartmentId(), testEmployee.getFirstName(), testEmployee.getLastName(), testEmployee.getBirthDay(), testEmployee.getGender(), testEmployee.getHireDate()); 
		row.next();
		testEmployee.setId(row.getLong("employee_id"));
		
		Department updatedDummyDepartment = new Department();
		updatedDummyDepartment.setName("NEWTestDepartment");
	
		String sqlDepartment = "INSERT INTO department (department_id, name) VALUES (DEFAULT, ?) RETURNING department_id";
		SqlRowSet rows = jdbcTemplate.queryForRowSet(sqlDepartment, updatedDummyDepartment.getName());
		rows.next();
		updatedDummyDepartment.setId(rows.getLong("department_id"));
		
		testEmployee.setDepartmentId(updatedDummyDepartment.getId());
		
		employeeDao.changeEmployeeDepartment(testEmployee.getId(), updatedDummyDepartment.getId());
		
		Long newDepartmentId = testEmployee.getDepartmentId();
		
		Assert.assertEquals(updatedDummyDepartment.getId(), newDepartmentId);
		
	}
	
	
	private Employee createEmployee() {
		Employee testEmployee = new Employee();
		
		testEmployee.setFirstName("TestFirst");
		testEmployee.setLastName("TestLast");
		testEmployee.setBirthDay(LocalDate.of(1953, 07, 15));
		testEmployee.setHireDate(LocalDate.of(2001, 04, 01));
		testEmployee.setGender('M');
		testEmployee.setDepartmentId(departmentId);
		
		
		
		return testEmployee;
	}
	
}
