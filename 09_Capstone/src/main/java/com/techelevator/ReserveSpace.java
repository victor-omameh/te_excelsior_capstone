package com.techelevator;

import com.techelevator.view.Menu;

public class ReserveSpace {

	
	private Menu menu = new Menu();
	
	public String getStartingDateAsString() {
		return menu.getStartingDate();
	}
	
	public int getNumberOfDayRequested() {
		return menu.numberOfDaysRequested();
	}
	
	public int getNumberOfPeopleRequested() {
		return menu.numberOfPeopleRequested();
	}
	
}
