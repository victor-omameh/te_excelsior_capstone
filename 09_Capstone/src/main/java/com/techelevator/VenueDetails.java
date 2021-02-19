package com.techelevator;

import com.techelevator.operations.Venue;
import com.techelevator.view.Menu;

public class VenueDetails {

	
	private Menu menu = new Menu();
	public void venueDetails(Venue selectedVenue) {
		menu.displaySelectedVenue(selectedVenue);
	}
	
	public int getUserSelection() {
		return menu.displayVenueDetailOptions();
	}
	
}
