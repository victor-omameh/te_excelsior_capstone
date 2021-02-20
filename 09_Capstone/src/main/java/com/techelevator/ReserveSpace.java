package com.techelevator;

import java.util.List;

import com.techelevator.operations.Space;
import com.techelevator.view.Menu;

public class ReserveSpace {
	
	private Menu menu = new Menu();
	
	private String requestedStartingDate;
	private int requestedNumberOfDays;
	private int requestedNumberOfPeolple;
	
	public String getRequestedStartingDate() {
		return requestedStartingDate;
	}

	public int getRequestedNumberOfDays() {
		return requestedNumberOfDays;
	}

	public int getRequestedNumberOfPeolple() {
		return requestedNumberOfPeolple;
	}

	
	public void askStartingDate() {
		this.requestedStartingDate = menu.getStartingDate();
	}
	
	public void askNumberOfDays() {
		this.requestedNumberOfDays = menu.numberOfDaysRequested();
	}
	
	public void askNumberOfPeople() {
		this.requestedNumberOfPeolple = menu.numberOfPeopleRequested();
	}
	
	public void displayAvailableSpace(List<Space> venueSpaces, int numberOfDays) {
		menu.displayAvailableSpaces(venueSpaces, numberOfDays);
	}
	
}
