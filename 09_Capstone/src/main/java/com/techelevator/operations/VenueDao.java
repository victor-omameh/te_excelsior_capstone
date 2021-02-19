package com.techelevator.operations;

import java.util.List;

public interface VenueDao {

	public List<Venue> getAllVenues();
	public Venue matchVenueWithUserSelection(int venueSelection);
}
