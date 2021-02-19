package com.techelevator;

import java.util.List;

import com.techelevator.operations.Venue;
import com.techelevator.view.Menu;

public class VenueDetails {

	
	private Menu menu = new Menu();
	public void venueDetails(Venue selectedVenue, List<String> categories) {
		menu.displaySelectedVenue(selectedVenue, categories);
	}
	
	public int getUserSelection() {
		return menu.displayVenueDetailOptions();
	}
	
}
