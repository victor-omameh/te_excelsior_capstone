package com.techelevator.jdbc;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.operations.Space;
import com.techelevator.operations.SpaceDao;

public class JdbcSpaceDao implements SpaceDao {
	
	private JdbcTemplate jdbcTemplate;
	
	public JdbcSpaceDao(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public List<Space> getAllSpacesOfVenue(int venueId) {
		
		List<Space> spacesList = new ArrayList<Space>();
		
		String sql = "SELECT id, venue_id, name, is_accessible, open_from, open_to, CAST (daily_rate AS VARCHAR), max_occupancy FROM space WHERE venue_id = ?";
		SqlRowSet row = jdbcTemplate.queryForRowSet(sql, venueId);
		
		int menuID = 0;
		while(row.next()) {
			menuID++;
			spacesList.add(rowToSpace(row, menuID));
		}
		
		return spacesList;
	}
	
	public Space rowToSpace(SqlRowSet row, int menuID) {
		Space space = new Space();
		
		space.setSpaceName(row.getString("name"));
		space.setAccessible(row.getBoolean("is_accessible"));
		//handle null
		if (row.getInt("open_from") > 0) {
			space.setOpenDate(convertIntegerToDateString(row.getInt("open_from")));
			space.setCloseDate(convertIntegerToDateString(row.getInt("open_to")));
		} else {
			space.setOpenDate("");
			space.setCloseDate("");
		}
		
		space.setDailyRate(convertStringToDouble(row.getString("daily_rate")));
		space.setMaxOccupancy(row.getInt("max_occupancy"));
		space.setMenuID(menuID);
		
		return space;
	}
	
	private String convertIntegerToDateString(int dateInteger) {
		 
		 if (dateInteger == 1) {
			 return "Jan.";
		 } else if (dateInteger == 2) {
			 return "Feb.";
		 } else if (dateInteger == 3) {
			 return "Mar.";
		 } else if (dateInteger == 4) {
			 return "Apr.";
		 } else if (dateInteger == 5) {
			 return "May";
		 } else if (dateInteger == 6) {
			 return "Jun.";
		 } else if (dateInteger == 7) {
			 return "Jul.";
		 } else if (dateInteger == 8) {
			 return "Aug.";
		 } else if (dateInteger == 9) {
			 return "Sep.";
		 } else if (dateInteger == 10) {
			 return "Oct.";
		 } else if (dateInteger == 11) {
			 return "Nov.";
		 } else if (dateInteger == 12) {
			 return "Dec.";
		 }
		 
		 return null;
	 }
	
	
	 //method to convert dailyRate from Money => String => double
	private double convertStringToDouble(String dailyRate) {
		String dailyRateWithoutDollarSign = dailyRate.substring(1);
		String[] number = dailyRateWithoutDollarSign.split(",");
		String joinedNumbers = String.join("", number);
		double dailyRateAsDouble = Double.parseDouble(joinedNumbers);
		//double dailyRateDouble = (double) numbers;
		
		return dailyRateAsDouble;
	}
	
}
