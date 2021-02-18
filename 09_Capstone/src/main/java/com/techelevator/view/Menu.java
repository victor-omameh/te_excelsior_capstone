package com.techelevator.view;

import java.util.List;
import java.util.Scanner;

import com.techelevator.operations.Venue;

public class Menu {
	
	private Scanner userInput = new Scanner(System.in);
	
	public String mainMenuSelection() {
		boolean incorrectSelection = true;
		String  mainMenuSelection = null;
		while(incorrectSelection) {
			
			System.out.println("What would you like to do?");
			System.out.printf("%3s %-20s%n", "", "1) List Venues");
			System.out.printf("%3s %-20s%n", "", "Q) Quit");
//			System.out.println();
			mainMenuSelection = userInput.nextLine();
			
			if (mainMenuSelection.equals("1") || mainMenuSelection.equalsIgnoreCase("Q")) {
				incorrectSelection = false;
			} else {
				System.out.println("**Incorrect selection**");
			}
		}
		
		return mainMenuSelection;
	}
	
	public String venueSelection (List<Venue> venueList) {
		String venueSelection = null;
		
		boolean incorrectSelection = true;
		
		while(incorrectSelection) {
			
			System.out.println("Which venue would you like to view?");
			
			for(Venue venue : venueList) {
				//get name of each venue
			}
			
			System.out.printf("%3s %-20s%n", "", "R) Return to Previous Screen");
			
		}
		
		return venueSelection;
	}
	
}
