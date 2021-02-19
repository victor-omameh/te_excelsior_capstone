package com.techelevator.operations;

import java.util.List;

public interface ReservationDao {

	public String prepareDateForSql(String startingDate);
	public String getEndDate(String startingDate, int numberOfDays);
	public boolean isAvailable(int spaceId, String startingDate, String endDate);
	public String getEndMonth();
	public String getStartingMonth();
	public List<Space> getAvailableSpaces(List<Space> matchingSpaces, String startingDate, String endDate);
}
