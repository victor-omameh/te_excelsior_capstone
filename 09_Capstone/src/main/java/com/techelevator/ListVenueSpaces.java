package com.techelevator;

import java.util.List;

import com.techelevator.operations.Space;
import com.techelevator.view.Menu;

public class ListVenueSpaces {
	
	public void listVenueSpaces(String venueName, List<Space> spacesList) {
		Menu menu = new Menu();
		menu.displayVenueSpacesList(venueName, spacesList);
	}
	
}
