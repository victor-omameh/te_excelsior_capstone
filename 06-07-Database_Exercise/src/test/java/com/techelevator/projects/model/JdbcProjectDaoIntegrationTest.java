package com.techelevator.projects.model;

import java.sql.SQLException;
import java.time.LocalDate;
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
