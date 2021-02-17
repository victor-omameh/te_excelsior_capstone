package com.techelevator.projects.model;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.projects.model.jdbc.JDBCDepartmentDAO;
import com.techelevator.projects.model.jdbc.JDBCProjectDAO;

public class JdbcProjectDaoIntegrationTest {
	
	private static SingleConnectionDataSource dataSource;  // must be static
	private ProjectDAO projectDao;
	private DepartmentDAO departmentDao;
	private JdbcTemplate jdbcTemplate;
	private EmployeeDAO employeeDao;
	
	
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
		projectDao = new JDBCProjectDAO(dataSource);
		jdbcTemplate = new JdbcTemplate(dataSource);
	}

	
	@Test
	public void get_all_Active_projects_test() {
		
		List<Project> originalList = projectDao.getAllActiveProjects();
		int originalCount = originalList.size();
		
		Project testProject = createProject();
	
		
		String sqlProject = "INSERT INTO project (project_id, name, from_date, to_date) VALUES (DEFAULT, ?, ?, ?) RETURNING project_id";
		SqlRowSet projectRow = jdbcTemplate.queryForRowSet(sqlProject, testProject.getName(), testProject.getStartDate(), testProject.getEndDate());
		projectRow.next();
		testProject.setId(projectRow.getLong("project_id"));
		

		
		List<Project> result = projectDao.getAllActiveProjects();
		
		Assert.assertEquals(originalCount + 1, result.size());
		
		
	}
	
	@Test
	public void add_employee_to_project() {
		Employee testEmployee = createEmployee();
		Project testProject = createProject();
		
		List<Employee> testEmployeeList = new ArrayList<Employee>();
	
		
		String sql = "INSERT INTO employee (employee_id, department_id, first_name, last_name, birth_date, gender, hire_date) VALUES (DEFAULT, ?, ?, ?, ?, ?, ?) RETURNING employee_id";
		SqlRowSet row = jdbcTemplate.queryForRowSet(sql, testEmployee.getDepartmentId(), testEmployee.getFirstName(), testEmployee.getLastName(), testEmployee.getBirthDay(), testEmployee.getGender(), testEmployee.getHireDate()); 
		row.next();
		testEmployee.setId(row.getLong("employee_id"));
		testEmployeeList.add(testEmployee);
		Long employeeID = testEmployee.getId();
		
		String sqlProject = "INSERT INTO project (project_id, name, from_date, to_date) VALUES (DEFAULT, ?, ?, ?) RETURNING project_id";
		SqlRowSet projectRow = jdbcTemplate.queryForRowSet(sqlProject, testProject.getName(), testProject.getStartDate(), testProject.getEndDate());
		projectRow.next();
		testProject.setId(projectRow.getLong("project_id"));
		Long projectID = testProject.getId();
		
		projectDao.addEmployeeToProject(projectID, employeeID);
		
		String sqlProjectEmployee = "SELECT e.employee_id, e.department_id, e.first_name, e.last_name, e.birth_date, e.gender, e.hire_date FROM employee e " + 
				"JOIN project_employee ON e.employee_id = project_employee.employee_id " + 
				"WHERE project_id = ?";
		SqlRowSet projectEmployeeRow = jdbcTemplate.queryForRowSet(sqlProjectEmployee, projectID);
		
		projectEmployeeRow.next();
		Long result = projectEmployeeRow.getLong("employee_id");
		
		
		Assert.assertEquals(employeeID, result);
		
	}
	
	@Test
	public void remove_employee_from_project() {
		Employee testEmployee = createEmployee();
		Project testProject = createProject();
		
		List<Employee> testEmployeeList = new ArrayList<Employee>();
	
		
		String sql = "INSERT INTO employee (employee_id, department_id, first_name, last_name, birth_date, gender, hire_date) VALUES (DEFAULT, ?, ?, ?, ?, ?, ?) RETURNING employee_id";
		SqlRowSet row = jdbcTemplate.queryForRowSet(sql, testEmployee.getDepartmentId(), testEmployee.getFirstName(), testEmployee.getLastName(), testEmployee.getBirthDay(), testEmployee.getGender(), testEmployee.getHireDate()); 
		row.next();
		testEmployee.setId(row.getLong("employee_id"));
		testEmployeeList.add(testEmployee);
		Long employeeID = testEmployee.getId();
		
		String sqlProject = "INSERT INTO project (project_id, name, from_date, to_date) VALUES (DEFAULT, ?, ?, ?) RETURNING project_id";
		SqlRowSet projectRow = jdbcTemplate.queryForRowSet(sqlProject, testProject.getName(), testProject.getStartDate(), testProject.getEndDate());
		projectRow.next();
		testProject.setId(projectRow.getLong("project_id"));
		Long projectID = testProject.getId();
		
		projectDao.addEmployeeToProject(projectID, employeeID);
		
		String sqlProjectEmployee = "SELECT e.employee_id, e.department_id, e.first_name, e.last_name, e.birth_date, e.gender, e.hire_date FROM employee e " + 
				"JOIN project_employee ON e.employee_id = project_employee.employee_id " + 
				"WHERE project_id = ?";
		jdbcTemplate.queryForRowSet(sqlProjectEmployee, projectID);
		
		projectDao.removeEmployeeFromProject(projectID, employeeID);
		SqlRowSet projectEmployeeRowRemoved = jdbcTemplate.queryForRowSet(sqlProjectEmployee, projectID);
		
		Assert.assertFalse(projectEmployeeRowRemoved.next());

	}
	
	
	private Employee createEmployee() {
		Employee testEmployee = new Employee();
		
		testEmployee.setFirstName("TestFirst");
		testEmployee.setLastName("TestLast");
		testEmployee.setBirthDay(LocalDate.of(1953, 07, 15));
		testEmployee.setHireDate(LocalDate.of(2001, 04, 01));
		testEmployee.setGender('M');
		Long departmentId = (long) 3;
		testEmployee.setDepartmentId(departmentId);
		
		return testEmployee;
	}
	
	private Project createProject() {
		
		Project testProject = new Project();
		
		testProject.setName("TestProject");
		testProject.setStartDate(LocalDate.of(1953, 07, 15));
		testProject.setEndDate(LocalDate.of(2001, 04, 01));
		
		return testProject;
		
	}
	
//	private void createProjectEmployee() {
//		
//		Project newProject = createProject();
//		Employee newEmployee = createEmployee();
//		
//		String sql = "INSERT INTO project_employee (project_id, employee_id) VALUES (?, ?) RETURNING employee_id";
//		//SqlRowSet row = jdbcTemplate.queryForRowSet(sql, newProject.get );
//		
//		
//	}
	
}
