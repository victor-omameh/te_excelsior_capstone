package com.techelevator;

import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import com.techelevator.jdbc.JdbcSpaceDao;
import com.techelevator.jdbc.JdbcVenueDao;
import com.techelevator.operations.ReservationDao;
import com.techelevator.operations.Space;
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
	private ListVenueSpaces listVenueSpaces = new ListVenueSpaces();
	private ReserveSpace reserveSpace = new ReserveSpace();
	
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
		spaceDao = new JdbcSpaceDao(datasource);
	}

	public void run() {
		
		boolean systemRunning = true;
		while (systemRunning) {
			
			String mainMenuSelection = main.getMainMenuSelection();
			if (mainMenuSelection.equals("1")) {
				
				boolean choosingVenue = true;
				while (choosingVenue) {
					
					int menuID = viewVenue.viewVenueSelection(venueDao.getAllVenues());
					if (menuID > 0) {
						
						Venue selectedVenue = venueDao.matchVenueWithUserSelection(menuID);
												
						boolean selectingVenueDetailOptions = true;
						while (selectingVenueDetailOptions) {
				
							venueDetails.venueDetails(selectedVenue, venueDao.getCategories(selectedVenue.getVenueId()));
							
							int venueDetailSelection = venueDetails.getUserSelection();
							if (venueDetailSelection == 1) {
								
								boolean chooseToReserve = true;
								while (chooseToReserve) {
									
									listVenueSpaces.listVenueSpaces(selectedVenue.getVenueName(), spaceDao.getAllSpacesOfVenue(selectedVenue.getVenueId()));
									int reserveSpaceSelection = listVenueSpaces.getUserSelection();
									if (reserveSpaceSelection == 1) {
										String startingDateFromUser = reserveSpace.getStartingDateAsString();
										int numberOfDays = reserveSpace.getNumberOfDayRequested();
										int numberOfPeople = reserveSpace.getNumberOfPeopleRequested();
										
										String endingDateFormatted = reservationDao.getEndDate(startingDateFromUser, numberOfDays);
										String startingDateFormatted = reservationDao.prepareDateForSql(startingDateFromUser);
										
										List<Space> matchingSpaces = spaceDao.getAllMatchingSpaces(selectedVenue.getVenueId(), reservationDao.getStartingMonth(), reservationDao.getEndMonth(), numberOfPeople);
										
										List<Space> availableSpaces = reservationDao.getAvailableSpaces(matchingSpaces, startingDateFormatted, endingDateFormatted);
										
										
										
									} else {
										chooseToReserve = false;
									}
								}
							//END CHOOSING TO RESERVE LOOP	
								
							}
							if (venueDetailSelection == 2) {
								//search for reservation
								
							} else if (venueDetailSelection == 0){
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
