package com.techelevator;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import com.techelevator.jdbc.JdbcVenueDao;
import com.techelevator.operations.ReservationDao;
import com.techelevator.operations.SpaceDao;
import com.techelevator.operations.Venue;
import com.techelevator.operations.VenueDao;

public class ExcelsiorCLI {
	
	private ReservationDao reservationDao;
	private SpaceDao spaceDao;
	private VenueDao venueDao;
	
	private Main main = new Main();
	private ViewVenue viewVenue = new ViewVenue();
	private VenueDetails venueDetails = new VenueDetails();
	
	//private static SingleConnectionDataSource dataSource;

	public static void main(String[] args) {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/excelsior-venues");
		dataSource.setUsername("postgres");
		dataSource.setPassword("postgres1");

		ExcelsiorCLI application = new ExcelsiorCLI(dataSource);
		application.run();
	}

	public ExcelsiorCLI(DataSource datasource) {
		venueDao = new JdbcVenueDao(datasource);
	}

	public void run() {
		
		boolean systemRunning = true;
		while (systemRunning) {
			
			String mainMenuSelection = main.getMainMenuSelection();
			if (mainMenuSelection.equals("1")) {
				
				boolean choosingVenue = true;
				while (choosingVenue) {
					
					int viewVenueSelection = viewVenue.viewVenueSelection(venueDao.getAllVenues());
					if (viewVenueSelection > 0) {
						
						Venue selectedVenue = venueDao.matchVenueWithUserSelection(viewVenueSelection);
						
						venueDetails.venueDetails(selectedVenue, venueDao.getCategories(selectedVenue.getVenueId()));
						
						boolean selectingVenueDetailOptions = true;
						while (selectingVenueDetailOptions) {
				
							int venueDetaiOptionSelection = venueDetails.getUserSelection();
							if (venueDetaiOptionSelection == 1) {
								
								
							}
							if (venueDetaiOptionSelection == 2) {
								
								
							} else {
								selectingVenueDetailOptions = false;
							}
						}
//END SELECTING VENUE DETAIL OPTIONS						
						
					} else {
						choosingVenue = false;
					}
				}
//END CHOOSING VENUE LOOP			
			} else {
				main.systemQuit();
				systemRunning = false;
			}
		}
//END SYSTEM RUNNING LOOP
	}
}
