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
		
		List<Space> originalList = spaceDao.getAllSpacesOfVenue(1);
		int originalCount = originalList.size();
		
		insertSpace();
		
		List<Space> resultList = spaceDao.getAllSpacesOfVenue(1);
		int resultCount = resultList.size();
		
		Assert.assertEquals(originalCount + 1, resultCount);
	}

	
	private void insertSpace() {
		
		String sql = "INSERT INTO space (id, venue_id, name, is_accessible, open_from, open_to, daily_rate, max_occupancy) VALUES (DEFAULT, ?, ?, ?, ?, ?, ?::numeric::MONEY, ?) RETURNING id";
		SqlRowSet row = jdbcTemplate.queryForRowSet(sql, 1, "TestName", false, 1, 11, 5555, 200);
		row.next();
		
	}

	
}
