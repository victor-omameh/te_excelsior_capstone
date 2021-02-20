package com.techelevator.jdbc;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.operations.ReservationDao;
import com.techelevator.operations.Space;

public class JdbcReservationDao implements ReservationDao {

	private String startingMonth;
	private int endMonth;
	private List<Space> availableSpaces;
	private String customerEndDate;
	
	
	public int getEndMonth() {
		return endMonth;
	}

	public String getStartingMonth() {
		return startingMonth;
	}


	private JdbcTemplate jdbcTemplate;
	
	public JdbcReservationDao(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Override
	public boolean isAvailable(int spaceId, String startingDate, String endDate) {
		
		boolean hasAvailablilty = false;
		
		String sql = "SELECT reservation_id, space_id, number_of_attendees, start_date, end_date, reserved_for FROM reservation " + 
				"WHERE space_id = ? " + 
				"AND (CAST (? AS DATE) BETWEEN start_date AND end_date " + 
				"OR CAST (? AS DATE) BETWEEN start_date AND end_date)";
		SqlRowSet row = jdbcTemplate.queryForRowSet(sql, spaceId, startingDate, endDate);
		
		int i = 0;
		while(row.next()) {
			
			if (row.getInt("reservation_id") > 0) {
				i++;
				break;
			}
		}
		if (i == 0 ) {
			hasAvailablilty = true;
		}
		return hasAvailablilty;
	}
	
	@Override
	public List<Space> getAvailableSpaces(List<Space> matchingSpaces, String startingDate, String endDate) {
		
		List<Space> availableSpaces = new ArrayList<Space>();
		
		for(Space space: matchingSpaces) {
			
			if (isAvailable(space.getSpaceId(), startingDate, endDate )) {
				availableSpaces.add(space);
			}
		}
		
		this.availableSpaces = availableSpaces;
		return availableSpaces;
	}
	
	@Override
	public String getEndDate(String startingDate, int numberOfDays) {
		
		String[] startingDateStringArray = startingDate.split("/");
		String month = startingDateStringArray[0];
		String day = startingDateStringArray[1];
		String year = startingDateStringArray[2];
		
		int monthInt = Integer.parseInt(month);
		int dayInt = Integer.parseInt(day);
		int yearInt = Integer.parseInt(year);
		
		LocalDate endDateLocal = LocalDate.of(yearInt, monthInt, dayInt).plusDays(numberOfDays);
		
		this.endMonth = endDateLocal.getMonthValue();
		
		String endDate = endDateLocal.toString();
		
		String[] endingDateStringArray = endDate.split("-");
		String endMonth = endingDateStringArray[1];
		String endDay = endingDateStringArray[2];
		String endYear = endingDateStringArray[0];
		
		
		char endMonthAsChar = endMonth.charAt(1);
		char endDayAsChar = endDay.charAt(1);
		
		
		if ((endMonth.charAt(0) == '0') && (endDay.charAt(0) == '0')) {
			this.customerEndDate = endMonthAsChar + "/" + endDayAsChar + "/" + endYear;
			
		} else if (!(endMonth.charAt(0) == '0') || (endDay.charAt(0) == '0')) {
			this.customerEndDate = endMonth + "/" + endDayAsChar + "/" + endYear;
			
		} else if ((endMonth.charAt(0) == '0') || !(endDay.charAt(0) == '0')) {
			this.customerEndDate = endMonthAsChar + "/" + endDay + "/" + endYear;
			
		} else {
			this.customerEndDate = endMonth + "/" + endDay + "/" + endYear;
		}
	    	
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
	
	
	@Override
	public int bookReservation(int spaceId, int numberOfPeople, String startDate, String endDate, String customerName) {
		
		int confirmationNumber = 0;
		
		String sql = "INSERT INTO reservation (reservation_id, space_id, number_of_attendees, start_date, end_date, reserved_for) VALUES (DEFAULT, ?, ?, CAST (? AS DATE), CAST(? AS DATE), ?) RETURNING reservation_id";
		SqlRowSet row = jdbcTemplate.queryForRowSet(sql, spaceId, numberOfPeople, startDate, endDate, customerName);
		row.next();
		confirmationNumber = row.getInt("reservation_id");
		
		return confirmationNumber;
	}

	@Override
	public String getCustomerEndDate() {
		return this.customerEndDate;
	}
	
	@Override
	public boolean verifySpaceAvailabilty() {
		if (this.availableSpaces.size() > 0) {
			return true;
		} else {
			return false;
		}
	}

	
}
