package com.techelevator.operations;

public class Space {
	 private String spaceName;
	 private int spaceId;
	 private boolean isAccessible;
	 private String openDate;
	 private String closeDate;
	 private double dailyRate;
	 private int maxOccupancy;
	 private int menuID;

	 
	public String getSpaceName() {
		return spaceName;
	}
	public void setSpaceName(String spaceName) {
		this.spaceName = spaceName;
	}
	public int getSpaceId() {
		return spaceId;
	}
	public void setSpaceId(int spaceId) {
		this.spaceId = spaceId;
	}
	public boolean isAccessible() {
		return isAccessible;
	}
	public void setAccessible(boolean isAccessible) {
		this.isAccessible = isAccessible;
	}
	public String getOpenDate() {
		return openDate;
	}
	public void setOpenDate(String openDate) {
		this.openDate = openDate;
	}
	public String getCloseDate() {
		return closeDate;
	}
	public void setCloseDate(String closeDate) {
		this.closeDate = closeDate;
	}
	public double getDailyRate() {
		return dailyRate;
	}
	public void setDailyRate(double dailyRate) {
		this.dailyRate = dailyRate;
	}
	public int getMaxOccupancy() {
		return maxOccupancy;
	}
	public void setMaxOccupancy(int maxOccupancy) {
		this.maxOccupancy = maxOccupancy;
	}
	public int getMenuID() {
		return menuID;
	}
	public void setMenuID(int menuID) {
		this.menuID = menuID;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((closeDate == null) ? 0 : closeDate.hashCode());
		long temp;
		temp = Double.doubleToLongBits(dailyRate);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + (isAccessible ? 1231 : 1237);
		result = prime * result + maxOccupancy;
		result = prime * result + menuID;
		result = prime * result + ((openDate == null) ? 0 : openDate.hashCode());
		result = prime * result + spaceId;
		result = prime * result + ((spaceName == null) ? 0 : spaceName.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Space other = (Space) obj;
		if (closeDate == null) {
			if (other.closeDate != null)
				return false;
		} else if (!closeDate.equals(other.closeDate))
			return false;
		if (Double.doubleToLongBits(dailyRate) != Double.doubleToLongBits(other.dailyRate))
			return false;
		if (isAccessible != other.isAccessible)
			return false;
		if (maxOccupancy != other.maxOccupancy)
			return false;
		if (menuID != other.menuID)
			return false;
		if (openDate == null) {
			if (other.openDate != null)
				return false;
		} else if (!openDate.equals(other.openDate))
			return false;
		if (spaceId != other.spaceId)
			return false;
		if (spaceName == null) {
			if (other.spaceName != null)
				return false;
		} else if (!spaceName.equals(other.spaceName))
			return false;
		return true;
	}
	
	
	
}
