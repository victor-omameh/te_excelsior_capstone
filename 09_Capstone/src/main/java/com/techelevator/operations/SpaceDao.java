package com.techelevator.operations;

import java.util.List;

public interface SpaceDao {
	
	public List<Space> getAllSpacesOfVenue(int venueId);
	public List<Space> getAllMatchingSpaces(int venueId, String startMonth, int endMonth, int numberOfPeople);
	public Space getBookedSpace(List<Space> availableList, int menuID);
}
