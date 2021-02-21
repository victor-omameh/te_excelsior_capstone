package com.techelevator;
import java.sql.SQLException;
import java.util.List;

import org.junit.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.jdbc.JdbcReservationDao;
import com.techelevator.jdbc.JdbcSpaceDao;
import com.techelevator.operations.ReservationDao;
import com.techelevator.operations.Space;
import com.techelevator.operations.SpaceDao;
import com.techelevator.operations.Venue;
public class JdbcReservationIntegrationTest extends DAOIntegrationTest {

	
	private ReservationDao reservationDao;
	private SpaceDao spaceDao;
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
		spaceDao = new JdbcSpaceDao(getDataSource());
		reservationDao = new JdbcReservationDao(getDataSource());
		jdbcTemplate = new JdbcTemplate(getDataSource());
		
	}
	
	@Test
	public void check_if_space_is_available() {
		
		Venue testVenue = insertVenue();
		insertSpace(testVenue.getVenueId());
		List<Space> testList = spaceDao.getAllSpacesOfVenue(testVenue.getVenueId());
		
		Space testSpace = new Space();
		for (Space space : testList ) {
			if (space.getSpaceName().equalsIgnoreCase("TestName")) {
				testSpace = space;
			}
		}
		
		boolean result = reservationDao.isAvailable(testVenue.getVenueId(), "2023-3-1", "2023-3-10");
		
		Assert.assertTrue(result);
		
		
	}
	
	@Test
	public void get_list_of_available_spaces() {
		
		Venue testVenue = insertVenue();
		insertSpace(testVenue.getVenueId());
		List<Space> testList = spaceDao.getAllSpacesOfVenue(testVenue.getVenueId());
		
		List<Space> resultList = reservationDao.getAvailableSpaces(testList, "2023-4-1", "2023-4-5");
		
		Assert.assertEquals(testList, resultList);
	
	}
	
	@Test
	public void add_days_to_date() {
		String testStartDate = "10/10/2000";
		int testNumberOfDays = 5;
		
		String result = reservationDao.getEndDate(testStartDate, testNumberOfDays);
		
		Assert.assertEquals("2000-10-15", result);
		
	}
	
	@Test
	public void prepare_date_for_sql() {
		
		String result = reservationDao.prepareDateForSql("10/20/2022");
		
		Assert.assertEquals("2022-10-20", result);
	}
	
	@Test
	public void booking_reservation() {
		
		Venue testVenue = insertVenue();
		insertSpace(testVenue.getVenueId());
		List<Space> testList = spaceDao.getAllSpacesOfVenue(testVenue.getVenueId());
		
		Space testSpace = new Space();
		for (Space space : testList ) {
			if (space.getSpaceName().equalsIgnoreCase("TestName")) {
				testSpace = space;
			}
		}
		int testSpaceId = testSpace.getSpaceId();
		
		int testConfirmationNumber = reservationDao.bookReservation(testSpaceId, 10, "2023-10-15", "2023-10-20", "TestTest");
		
		Assert.assertTrue(testConfirmationNumber > 0);
		
	}
	
	
	

	private Venue insertVenue() {
		
		Venue testVenue = new Venue();
		
		
		String sqlState = "INSERT INTO state (abbreviation, name) VALUES (?, ?) RETURNING name";
		SqlRowSet rowState = jdbcTemplate.queryForRowSet(sqlState, "TT", "TestState");
		rowState.next();
		testVenue.setStateName(rowState.getString("name"));
		
		
		String sqlCity = "INSERT INTO city (id, name, state_abbreviation) VALUES (DEFAULT, ?, ?) RETURNING id";
		SqlRowSet rowCity = jdbcTemplate.queryForRowSet(sqlCity, "TestCity", "TT");
		rowCity.next();
		testVenue.setCityId(rowCity.getInt("id"));
		
		String sqlVenue = "INSERT INTO venue (id, name, city_id, description) VALUES (DEFAULT, ?, ?, ?) RETURNING id";
		SqlRowSet rowVenue = jdbcTemplate.queryForRowSet(sqlVenue, "TestVenue", testVenue.getCityId(), "TESTTESTTEST" );
		rowVenue.next();
		testVenue.setVenueId(rowVenue.getInt("id"));
		
		return testVenue;
	
	}
	
	
	private void insertSpace(int testVenueID) {
		
		String sql = "INSERT INTO space (id, venue_id, name, is_accessible, open_from, open_to, daily_rate, max_occupancy) VALUES (DEFAULT, ?, ?, ?, ?, ?, ?::numeric::MONEY, ?) RETURNING id";
		jdbcTemplate.queryForRowSet(sql, testVenueID, "TestName", false, 1, 11, 5555, 200);
		
	}
	
	
	
}
