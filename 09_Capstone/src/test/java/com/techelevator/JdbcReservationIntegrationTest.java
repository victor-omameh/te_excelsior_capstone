package com.techelevator;
import java.sql.SQLException;

import org.junit.*;
import org.springframework.jdbc.core.JdbcTemplate;

import com.techelevator.jdbc.JdbcReservationDao;
import com.techelevator.operations.ReservationDao;
public class JdbcReservationIntegrationTest extends DAOIntegrationTest {

	
	private ReservationDao reservationDao;
	private JdbcTemplate jdbcTemplate;
	
	
	@BeforeClass 
	public static void setupData() {
		setupDataSource();
	}
	
	@AfterClass
	public static void closeData() throws SQLException {
		closeDataSource();
	}
	
	@After
	public void rollbackTransaction() throws SQLException {
		rollback();
	}
	
	
	@Before
	public void setup() {
		reservationDao = new JdbcReservationDao(getDataSource());
		jdbcTemplate = new JdbcTemplate(getDataSource());
		
	}
	
	@Test
	public void add_days_to_date() {
		String testStartDate = "10/10/2000";
		int testNumberOfDays = 5;
		
		String result = reservationDao.getEndDate(testStartDate, testNumberOfDays);
		
		Assert.assertEquals("2000-10-15", result);
		
	}
	

	
}
