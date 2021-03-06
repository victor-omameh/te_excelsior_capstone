package com.techelevator.view;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import com.techelevator.operations.Space;
import com.techelevator.operations.Venue;

public class Menu {
	
	private Scanner userInput = new Scanner(System.in);
	private double totalCost;
	
//MAIN MENU	
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

	
//VIEW VENUE MENU
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
		System.out.println();
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
	
	
//VENUE DETAILS
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

	
//LIST VENUE SPACES
	public void displayVenueSpacesList(String venueName, List<Space> venueSpaces) {
			System.out.println();
			System.out.println(venueName + " Spaces");
			System.out.println();
			System.out.printf("%3s %-4s %-30s %-6s %-6s %-14s %-15s%n" , "", "", "Name", "Open", "Close", "Daily Rate", "Max.Occupancy");
		
			DecimalFormat formatter = new DecimalFormat("####.00");
			for(Space space : venueSpaces) {
			
		
			System.out.printf("%3s #%-3s %-30s %-6s %-6s $%-13s %-15s%n", "", space.getMenuID(), space.getSpaceName(), space.getOpenDate(), space.getCloseDate(), formatter.format(space.getDailyRate()), space.getMaxOccupancy());
		}
	}
	
	public int displayVenueSpaceOptions() {
		int userSelection = 0;
		boolean incorrectSelection = true;
		
		while(incorrectSelection) {
			System.out.println();
			System.out.println("What would you like to do next?");
			System.out.printf("%3s %-20s%n", "", "1) Reserve a Space");
			System.out.printf("%3s %-20s%n", "", "R) Return to Previous Screen");
			String selectedInput = userInput.nextLine();
			userSelection = returnUserSelection(selectedInput);
			
			if (userSelection > 1 || userSelection < 0) {
				System.out.println(wrongSelection());
			} else {
				incorrectSelection = false; 
			}
		}
		return userSelection;
	}

	
//RESERVE A SPACE
	public String getStartingDate() {
		
		String requestedDate = null;
		boolean incorrectDate = true;
		while (incorrectDate) {
			System.out.println();
			System.out.print("When do you need the space? ");
			requestedDate = userInput.nextLine();
			
			if (checkDateFormat(requestedDate)) {
				incorrectDate = false;
			} else {
				System.out.println("** Please Enter <MM/dd/yyyy>");
			}
		}
		
		return requestedDate;
	}
	
	public int numberOfDaysRequested() {
		
		int numberOfDays = 0;
		
		boolean incorrectNumberOfDays = true;
		while (incorrectNumberOfDays) {
			
			
			System.out.print("How many days will you need the space? ");
			String inputAsString = userInput.nextLine();
			
			try {
				numberOfDays = Integer.parseInt(inputAsString);
				if (numberOfDays <= 0) {
					System.out.println("**Invalid number of days**");
				} else {
					incorrectNumberOfDays = false;
				}
			} catch (NumberFormatException e) {
				System.out.println("**Invalid number of days**");
			}
		}
		return numberOfDays;
	}
	
	public int numberOfPeopleRequested() {
		int numberOfPeople = 0;
		
		boolean incorrectNumberOfPeople = true;
		while (incorrectNumberOfPeople) {
		
			System.out.print("How many people will be in attendace? ");
			String inputAsString = userInput.nextLine();
			
			try {
				numberOfPeople = Integer.parseInt(inputAsString);
				if (numberOfPeople <= 0) {
					System.out.println("**Invalid number of people**");
				} else {
					incorrectNumberOfPeople = false;
				}
			} catch (NumberFormatException e) {
				System.out.println("**Invalid number of people**");
			}
		}
		return numberOfPeople;
	}
	
	public void displayAvailableSpaces(List<Space> venueSpaces, int numberOfDays) {
		System.out.println();
		System.out.println("The following spaces are available based on your needs: ");
		System.out.println();
		System.out.printf("%3s %-11s %-25s %-15s %-15s %-15s %-15s%n" , "", "Space #", "Name", "Daily Rate", "Max Occup.", "Accessible?", "Total Cost");
		
		for(Space space : venueSpaces) {
		String isAccessible = null;
		
		if (space.isAccessible()) {
			isAccessible = "Yes";
		} else {
			isAccessible = "No";
		}
		DecimalFormat formatter = new DecimalFormat("#,###");
		this.totalCost = (space.getDailyRate() * numberOfDays);
			
		System.out.printf("%3s #%-10s %-25s $%-14s %-15s %-15s $%-15s%n", 
				"", space.getMenuID(), space.getSpaceName(), formatter.format(space.getDailyRate()), space.getMaxOccupancy(), isAccessible, formatter.format(totalCost));
		
		}
	}
	
	
	public boolean noAvailableSpace() {
		
		boolean userChoice = false;
		
		System.out.println();
		System.out.println("There are no available spaces.");
		
		boolean wrongInput = true;
		while(wrongInput) {
			System.out.println("Would you like to try a different search?(Y/N) ");
			String choiceAsString = userInput.nextLine();
			if (choiceAsString.equalsIgnoreCase("Y") || choiceAsString.equalsIgnoreCase("N")) {
				if (choiceAsString.equalsIgnoreCase("Y")) {
					userChoice = true;
				}
				wrongInput = false;
			} else {
				System.out.println();
				System.out.println("***Invalid Response - Please eneter (Y or N)");
			}
			
		}
		return userChoice;
	}
	
	public int selectingSpace(List<Space> availableSpaces) {
		
		int menuIdSelection = 0;
		
		boolean incorrectMenuId = true;
		while (incorrectMenuId) {
			
			System.out.println();
			System.out.print("Which space would you like to reserve (enter 0 to cancel)? ");
			String inputAsString = userInput.nextLine();
			
			try {
				menuIdSelection = Integer.parseInt(inputAsString);
				if (menuIdSelection < 0) {
					System.out.println("**Invalid Selection**");
				} else if (menuIdSelection > 0){
					for (Space availableSpace : availableSpaces) {
						if (availableSpace.getMenuID() == menuIdSelection) {
							incorrectMenuId = false;
							break;
						}
					}
				} else if (menuIdSelection == 0) {
					incorrectMenuId = false;
				}
			} catch (NumberFormatException e) {
				System.out.println("**Invalid Selection**");
			}
		}
		return menuIdSelection;
	}
	
	
	public String customerName() {
		String name = null;
		
		boolean nameNull = true;
		while (nameNull) {
			
			System.out.print("Who is this reservation for? ");
			name = userInput.nextLine();
			
			if (!(name.equals(" ")) || !(name.equals(null))) {
				nameNull = false;
			}
		}
		
		return name;
	}
	
	public void displayConfirmationReceipt(int confirmationNumber, String venueName, String spaceName, String customerName, int numberOfPeople, String startDate, String endDate, double spacePrice, int numberOfDays) {
		DecimalFormat formatter = new DecimalFormat("$#,###");
		
		System.out.println();
		System.out.println("Thanks for submitting your reservation! The details for your event are listed below: ");
		System.out.println();
		System.out.printf("%16s %-35s%n", "Confirmation #:" , confirmationNumber);
		System.out.printf("%16s %-35s%n", "Venue:" , venueName);
		System.out.printf("%16s %-35s%n", "Space:" , spaceName);
		System.out.printf("%16s %-35s%n", "Reserved for:" , customerName);
		System.out.printf("%16s %-35s%n", "Attendees:" , numberOfPeople);
		System.out.printf("%16s %-35s%n", "Arrival Date:" , startDate);
		System.out.printf("%16s %-35s%n", "Depart Date:" , endDate);
		System.out.printf("%16s %-35s%n", "Total Cost:" , formatter.format(spacePrice*numberOfDays));
		System.out.println();
		System.out.println();
		
	}
	
	
	
//SYSTEM QUIT
	public void goodbyeMessage() {
		System.out.println("Thank you, goodbye!");
	}
	
	
//PRIVATE METHODS	
	
	private boolean checkDateFormat (String requestedDate) {
		boolean formatCorrect = false;
		Date date = new Date();
		if (requestedDate.trim().equals("")){
		    return formatCorrect;
		    
		} else {
			
			 SimpleDateFormat sdfrmt = new SimpleDateFormat("MM/dd/yyyy");
			    sdfrmt.setLenient(false);
			    
			    try {
			    	Date selectedDate = sdfrmt.parse(requestedDate);
			    	if (selectedDate.after(date) || selectedDate.equals(date)) {
			    		formatCorrect = true;
			    	} else {
			    		System.out.println(requestedDate+" is an invalid Date");
			    	}
			    }
			    catch (ParseException e)
			    {
			        System.out.println(requestedDate+" is Invalid Date format");
			        return formatCorrect;
			    }
		}
		
		return formatCorrect;
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
	
}
