package com.techelevator.jdbc;

import com.techelevator.operations.ReservationDao;

public class JdbcReservationDao implements ReservationDao {

	private String month;
	@Override
	public String prepareDateForSql(String startingDate) {
		
		String[] dateStringArray = startingDate.split("/");
		this.month = dateStringArray[0];
		String day = dateStringArray[1];
		String year = dateStringArray[2];
		
		String sqlReady = year + "-" + month + "-" + day;
		
		return sqlReady;
	}

}
