package com.techelevator;

import java.sql.SQLException;
import java.util.List;

import org.junit.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.jdbc.JdbcVenueDao;
import com.techelevator.operations.Venue;
import com.techelevator.operations.VenueDao;

public class JdbcVenueDaoIntegrationTest extends DAOIntegrationTest{
	private VenueDao venueDao;
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
		venueDao = new JdbcVenueDao(getDataSource());
		jdbcTemplate = new JdbcTemplate(getDataSource());
		
		
		
	}
	
	@Test
	public void get_all_venues_test() {
		
		List<Venue> originalList = venueDao.getAllVenues();
		int originalCount = originalList.size();
		
		insertVenue();
		
		List<Venue> resultList = venueDao.getAllVenues();
		int resultCount = resultList.size();
		
		Assert.assertEquals(originalCount + 1, resultCount);
	}
	
	private void insertVenue() {
		
		Venue venue = new Venue();
		
		
		String sqlState = "INSERT INTO state (abbreviation, name) VALUES (?, ?) RETURNING name";
		SqlRowSet rowState = jdbcTemplate.queryForRowSet(sqlState, "TT", "TestState");
		rowState.next();
		venue.setStateName(rowState.getString("name"));
		
		
		String sqlCity = "INSERT INTO city (id, name, state_abbreviation) VALUES (DEFAULT, ?, ?) RETURNING id";
		SqlRowSet rowCity = jdbcTemplate.queryForRowSet(sqlCity, "TestCity", "TT");
		rowCity.next();
		venue.setCityId(rowCity.getInt("id"));
		
		String sqlVenue = "INSERT INTO venue (id, name, city_id, description) VALUES (DEFAULT, ?, ?, ?) RETURNING id";
		SqlRowSet rowVenue = jdbcTemplate.queryForRowSet(sqlVenue, "TestVenue", venue.getCityId(), "TESTTESTTEST" );
		rowVenue.next();
		venue.setVenueId(rowVenue.getInt("id"));
		
		String sqlCategory = "INSERT INTO category (id, name) VALUES (DEFAULT, ?) RETURNING id";
		SqlRowSet rowCategory = jdbcTemplate.queryForRowSet(sqlCategory, "TestCategory");
		rowCategory.next();
		venue.setCategoryId(rowCategory.getInt("id"));
		
		String sqlCategoryVenue = "INSERT INTO category_venue (venue_id, category_id) VALUES (?, ?)";
		SqlRowSet rowCategoryVenue = jdbcTemplate.queryForRowSet(sqlCategory, venue.getVenueId(), venue.getCategoryId());
	}
	
	
	private Venue createVenue() {
		
		Venue testVenue = new Venue();
		testVenue.setCategoryName("TestCat");
		testVenue.setCityName("TestCity");
		testVenue.setDescription("TESTESTTEST");
		testVenue.setStateName("TestState");
		testVenue.setVenueName("TestVenue");
		
		return null;
	}
}
