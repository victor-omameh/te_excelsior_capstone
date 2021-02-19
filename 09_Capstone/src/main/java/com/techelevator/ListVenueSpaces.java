package com.techelevator;

import java.util.List;

import com.techelevator.operations.Space;
import com.techelevator.view.Menu;

public class ListVenueSpaces {
	
	private Menu menu = new Menu();
	
	public void listVenueSpaces(String venueName, List<Space> spacesList) {
		menu.displayVenueSpacesList(venueName, spacesList);
	}
	
	public int getUserSelection() {
		return menu.displayVenueSpaceOptions();
	}
	
}
