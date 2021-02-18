package com.techelevator;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;

import com.techelevator.operations.ReservationDao;
import com.techelevator.operations.SpaceDao;
import com.techelevator.operations.VenueDao;

public class ExcelsiorCLI {
	
	private ReservationDao reservationDao;
	private SpaceDao spaceDao;
	private VenueDao venueDao;
	
	private Main main = new Main();
	

	public static void main(String[] args) {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/excelsior-venues");
		dataSource.setUsername("postgres");
		dataSource.setPassword("postgres1");

		ExcelsiorCLI application = new ExcelsiorCLI(dataSource);
		application.run();
	}

	public ExcelsiorCLI(DataSource datasource) {
		//reservationDao = new ReservationDao(dataSource);
	}

	public void run() {
		String mainMenuSelection = main.getMainMenuSelection();
		if (mainMenuSelection.equals("1")) {
			//view venu method
		}
	}
}
