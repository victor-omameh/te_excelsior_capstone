package com.techelevator.jdbc;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.operations.ReservationDao;
import com.techelevator.operations.Space;

public class JdbcReservationDao implements ReservationDao {

	private String startingMonth;
	private String endMonth;
	
	
	public String getEndMonth() {
		return endMonth;
	}

	public String getStartingMonth() {
		return startingMonth;
	}


	private JdbcTemplate jdbcTemplate;
	
	public JdbcReservationDao(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	public boolean isAvailable(int spaceId, String startingDate, String endDate) {
		
		
		String sql = "SELECT reservation_id, space_id, number_of_attendees, start_date, end_date, reserved_for FROM reservation " + 
				"WHERE space_id = ? " + 
				"AND (CAST (? AS DATE) BETWEEN start_date AND end_date " + 
				"OR CAST (? AS DATE) BETWEEN start_date AND end_date)";
		SqlRowSet row = jdbcTemplate.queryForRowSet(sql, spaceId, startingDate, endDate);
		
		int i = 0;
		while(row.next()) {
			i++;
		}
		
		if (i > 0 ) {
			return false;
		} else {
			return true;
		}
		
	}
	
	public List<Space> getAvailableSpaces(List<Space> matchingSpaces, String startingDate, String endDate) {
		
		List<Space> availableSpaces = new ArrayList();
		
		for(Space space: matchingSpaces) {
			
			if (isAvailable(space.getSpaceId(), startingDate, endDate )) {
				availableSpaces.add(space);
			}
			
		}
		
		return availableSpaces;
	}
	

	public String getEndDate(String startingDate, int numberOfDays) {
		

		String[] startingDateStringArray = startingDate.split("/");
		this.startingMonth = startingDateStringArray[0];
		String day = startingDateStringArray[1];
		String year = startingDateStringArray[2];
		
		int monthInt = Integer.parseInt(this.startingMonth);
		int dayInt = Integer.parseInt(day);
		int yearInt = Integer.parseInt(year);
		
		LocalDate endDateLocal = LocalDate.of(yearInt, monthInt, dayInt).plusDays(numberOfDays);
		
		this.endMonth = endDateLocal.getMonth().toString();
		
		String endDate = endDateLocal.toString();
	    	
		return endDate;
	}
	
	
	@Override
	public String prepareDateForSql(String startingDate) {
		
		String[] dateStringArray = startingDate.split("/");
		this.startingMonth = dateStringArray[0];
		String day = dateStringArray[1];
		String year = dateStringArray[2];
		
		String sqlReady = year + "-" + startingMonth + "-" + day;
		
		return sqlReady;
	}
	
}
