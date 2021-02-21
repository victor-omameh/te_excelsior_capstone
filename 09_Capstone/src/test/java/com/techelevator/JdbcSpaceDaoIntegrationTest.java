package com.techelevator;

import java.sql.SQLException;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.jdbc.JdbcSpaceDao;
import com.techelevator.operations.Space;
import com.techelevator.operations.SpaceDao;
import com.techelevator.operations.Venue;

public class JdbcSpaceDaoIntegrationTest extends DAOIntegrationTest{
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
		jdbcTemplate = new JdbcTemplate(getDataSource());
		
		
	}
	
	
	@Test
	public void get_all_spaces_of_venue_test() {
		
		Venue testVenue = insertVenue();
		
		List<Space> originalList = spaceDao.getAllSpacesOfVenue(testVenue.getVenueId());
		int originalCount = originalList.size();
		
		
		insertSpace(testVenue.getVenueId());
		
		List<Space> resultList = spaceDao.getAllSpacesOfVenue(testVenue.getVenueId());
		int resultCount = resultList.size();
		
		Assert.assertEquals(originalCount + 1, resultCount);
	}
	
	
	@Test
	public void get_all_matching_spaces() {
		
		Venue testVenue = insertVenue();
		
		insertSpace(testVenue.getVenueId());
		
		List<Space> resultList = spaceDao.getAllMatchingSpaces(testVenue.getVenueId(), "2", 3, 50);
		
		String result = null;
		for (Space resultSpace : resultList) {
			result = resultSpace.getSpaceName();
		}
		
		Assert.assertEquals("TestName", result);
		
	}
	
	@Test
	public void get_booked_space() {
		
		Venue testVenue = insertVenue();
		insertSpace(testVenue.getVenueId());
		List<Space> testList = spaceDao.getAllSpacesOfVenue(testVenue.getVenueId());
		
		Space testSpace = new Space();
		for (Space space : testList ) {
			if (space.getSpaceName().equalsIgnoreCase("TestName")) {
				testSpace = space;
			}
		}
		
		Space resultSpace = spaceDao.getBookedSpace(testList, testSpace.getMenuID());
		
		Assert.assertEquals(testSpace, resultSpace);
		
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
