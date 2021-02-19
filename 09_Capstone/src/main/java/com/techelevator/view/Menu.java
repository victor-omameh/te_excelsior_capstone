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

			mainMenuSelection = userInput.nextLine();
			
			if (mainMenuSelection.equals("1") || mainMenuSelection.equalsIgnoreCase("Q")) {
				incorrectSelection = false;
			} else {
				System.out.println(wrongSelection());
			}
		}
		
		return mainMenuSelection;
	}
	
	public int venueSelection (List<Venue> venueList) {
		int venueSelection = 0;
		boolean incorrectSelection = true;
		
		while(incorrectSelection) {
			int i = 0;
			System.out.println();
			System.out.println("Which venue would you like to view?");
			for(Venue venue : venueList) {
				i++;
				System.out.printf("%3s %2s%1s %-20s%n", "", i, ") ", venue.getVenueName());
			}
			
			System.out.printf("%3s %-20s%n", "", " R)  Return to Previous Screen");
			String userSelection = userInput.nextLine();
			
			try {
				venueSelection = Integer.parseInt(userSelection);
				if (venueSelection == 0) {
					venueSelection = -1;
				}
			} catch (NumberFormatException e) {
				if (userSelection.equalsIgnoreCase("R")) {
					venueSelection = 0;
				} else {
					venueSelection = -1;
				}
			}
			if (venueSelection > i || venueSelection < 0) {
				System.out.println(wrongSelection());
			} else {
				incorrectSelection = false; 
			}
			i = 0;
		}
		return venueSelection;
	}
	
	public void displaySelectedVenue(Venue selectedVenue, List<String> categories) {
		System.out.println(selectedVenue.getVenueName());
		System.out.println("Location: " + selectedVenue.getCityName() + ", " + selectedVenue.getStateName());
		
		System.out.print("Categories: ");
		for (int i = 0; i < categories.size(); i++) {
			if (i > 0) {
				System.out.print(", ");
			}
			System.out.print(categories.get(i));	
		}
		System.out.println();
		System.out.println();
		System.out.println(selectedVenue.getDescription());
		System.out.println();
	}
	
	public int displayVenueDetailOptions() {
		int userSelection = 0;
		boolean incorrectSelection = true;
		
		while(incorrectSelection) {
		
			System.out.println("What would you like to do next?");
			System.out.printf("%3s %-20s%n", "", "1) View Spaces");
			System.out.printf("%3s %-20s%n", "", "2) Search for Reservation");
			System.out.printf("%3s %-20s%n", "", "R) Return to Previous Screen");
			String selectedInput = userInput.nextLine();
			userSelection = returnUserSelection(selectedInput);
			
			if (userSelection > 2 || userSelection < 0) {
				System.out.println(wrongSelection());
			} else {
				incorrectSelection = false; 
			}
		}
		return userSelection;
	}
	
	private int returnUserSelection (String userInput) {
		int userSelection = 0;
		try {
			userSelection = Integer.parseInt(userInput);
			if (userSelection == 0) {
				userSelection = -1;
			}
		} catch (NumberFormatException e) {
			if (userInput.equalsIgnoreCase("R")) {
				userSelection = 0;
			} else {
				userSelection = -1;
			}
		}
		return userSelection;
	}
	
	private String wrongSelection() {
		return "***PLEASE ENTER A VALID SELECTION***";
	}
	
	public void goodbyeMessage() {
		System.out.println("Thank you, goodbye!");
	}
}
