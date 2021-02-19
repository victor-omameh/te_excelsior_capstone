package com.techelevator;

import com.techelevator.view.Menu;

public class Main {
	
	private Menu menu = new Menu();
	
	public String getMainMenuSelection() {
		
		return menu.mainMenuSelection();
	}
	
	public void systemQuit() {
		menu.goodbyeMessage();
	}
	
}
