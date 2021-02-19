package com.techelevator;

import java.util.List;

import javax.sql.DataSource;

//import com.techelevator.jdbc.JdbcVenueDao;
import com.techelevator.operations.Venue;
//import com.techelevator.operations.VenueDao;
import com.techelevator.view.Menu;

public class ViewVenue {

	public int viewVenueSelection(List<Venue> venueList) {
		Menu menu = new Menu();
		return menu.venueSelection(venueList);
		
	}
	
//	public Venue selectedVenue(DataSource dataSource, int venueSelection) {
//		VenueDao venueDao = new JdbcVenueDao(dataSource);
//		Venue selectedVenue = venueDao.matchVenueWithUserSelection(venueSelection);
//		return  selectedVenue;
//	}
	
}
