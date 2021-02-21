package com.techelevator;

import java.sql.SQLException;
import java.util.ArrayList;
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
	
	@Test
	public void match_venue_with_user_selection() {
		insertVenue();
		Venue testVenue = new Venue();
		List<Venue> venuesAfterInsert = venueDao.getAllVenues();
		
		for (Venue venue : venuesAfterInsert) {
			if (venue.getVenueName().equalsIgnoreCase("TestVenue")) {
				testVenue = venue;
			}
		}
		Venue result = venueDao.matchVenueWithUserSelection(testVenue.getMenuID());
		
		Assert.assertEquals(testVenue.getMenuID(), result.getMenuID());
	}
	
	@Test
	public void return_categories_from_selected_venue() {
		
		List<String> test = new ArrayList();
		test.add("TestCategory");
		
		insertVenue();
		
		List<Venue> venuesAfterInsert = venueDao.getAllVenues();
		Venue testVenue = new Venue();
		for (Venue venue : venuesAfterInsert) {
			if (venue.getVenueName().equalsIgnoreCase("TestVenue")) {
				testVenue = venue;
			}
		}
		
		int testVenueID = testVenue.getVenueId();
		
		String insertCat = "INSERT INTO category (id, name) VALUES (DEFAULT, ?) RETURNING id";
		SqlRowSet catRow = jdbcTemplate.queryForRowSet(insertCat, "TestCategory");
		catRow.next();
		int catID = catRow.getInt("id");
		
		String insertCatVenue = "INSERT INTO category_venue (venue_id, category_id) VALUES (?, ?) RETURNING venue_id";
		jdbcTemplate.queryForRowSet(insertCatVenue, testVenueID, catID);
		
		List<String> result = venueDao.getCategories(testVenueID);
		
		Assert.assertEquals(test, result);
		
		
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
	
	}
	
	
	
//	private Venue createVenue() {
//		
//		Venue testVenue = new Venue();
//		testVenue.setCategoryName("TestCat");
//		testVenue.setCityName("TestCity");
//		testVenue.setDescription("TESTESTTEST");
//		testVenue.setStateName("TestState");
//		testVenue.setVenueName("TestVenue");
//		
//		return testVenue;
//	}
}
