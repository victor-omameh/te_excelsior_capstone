package com.techelevator.projects.model;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.projects.model.jdbc.JDBCDepartmentDAO;



public class JdbcDepartmentDaoIntegrationTest {

	private static SingleConnectionDataSource dataSource;  // must be static
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
		departmentDao = new JDBCDepartmentDAO(dataSource);
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Test
	public void get_department_by_id() {
		
		Department testDepartment = getTestDepartment();
		String sql = "INSERT INTO department (department_id, name) VALUES (DEFAULT, ?) RETURNING department_id";
		
		SqlRowSet result = jdbcTemplate.queryForRowSet(sql, testDepartment.getName());
		result.next();
		testDepartment.setId(result.getLong("department_id")); 
		
		Department departmentfromDatabase = departmentDao.getDepartmentById(testDepartment.getId());
		
		Assert.assertNotNull(departmentfromDatabase);
		Assert.assertEquals(testDepartment, departmentfromDatabase);
		
	}
	
	@Test
	public void get_departments() {
		List<Department> dummyList = departmentDao.getAllDepartments();
		int originalCount = dummyList.size();
		
		departmentDao.createDepartment(getTestDepartment());
		Department testDepartment = new Department();
		testDepartment.setName("TestName2");
		departmentDao.createDepartment(testDepartment);
		
		List<Department> listAfterInserts = departmentDao.getAllDepartments();
		
		Assert.assertEquals(originalCount + 2, listAfterInserts.size());
		
		

		
	}
	
	@Test
	public void save_departments_test() {
		Department originalDepartment = departmentDao.createDepartment(getTestDepartment());
		
		Department updateDepartment = new Department();
		updateDepartment.setName("UpdateTest");
		updateDepartment.setId(originalDepartment.getId());
		
		
		departmentDao.saveDepartment(updateDepartment);
	
		
		Assert.assertEquals(updateDepartment, departmentDao.getDepartmentById(originalDepartment.getId()));
		
	}
	
	@Test
	public void search_departments_by_name_test() {
		
		Department testDepartmentOne = departmentDao.createDepartment(getTestDepartment());
		Department testDepartment = new Department();
		testDepartment.setName("TestName2");
		departmentDao.createDepartment(testDepartment);
		
		List<Department> listAfterInserts = new ArrayList<Department>();
		listAfterInserts.add(testDepartmentOne);
		listAfterInserts.add(testDepartment);
		
		List<Department> result = departmentDao.searchDepartmentsByName("TestName");
		
		Assert.assertEquals(listAfterInserts, result);
	}
	
	private Department getTestDepartment() {
		Department testDepartment = new Department();
		testDepartment.setName("TestName");
	
		return testDepartment;
	}
	
}
