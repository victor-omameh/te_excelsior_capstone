package com.techelevator.jdbc;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.operations.Venue;
import com.techelevator.operations.VenueDao;

public class JdbcVenueDao implements VenueDao {
	
	private JdbcTemplate jdbcTemplate;
	
	public JdbcVenueDao(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Override
	public List<Venue> getAllVenues() {
		
		String sql = "SELECT venue.id, venue.name AS venue_name, venue.description, city.name AS city_name, city.state_abbreviation FROM venue " + 
				"JOIN city ON venue.city_id = city.id";
			 
		SqlRowSet rows = jdbcTemplate.queryForRowSet(sql);
		
		List<Venue> venueList = new ArrayList<Venue>();
		
		int menuID = 0;
		while(rows.next()) {
			menuID++;
			venueList.add(rowToVenue(rows, menuID));
		}
				
		return venueList;
	}
	
	@Override
	public List<String> getCategories(int venueId) {
		List<String> venueCategories = new ArrayList<String>();
		
		String sql = "SELECT category.name FROM category " + 
				"JOIN category_venue on category.id = category_venue.category_id " + 
				"WHERE category_venue.venue_id = ?";
		SqlRowSet row = jdbcTemplate.queryForRowSet(sql, venueId);
		
		while (row.next()) {
			venueCategories.add(row.getString("name"));
		}
		return venueCategories;
	}
	
	@Override
	public Venue matchVenueWithUserSelection(int venueSelection) {
		Venue selectedVenue = new Venue();
		List<Venue> venueList = getAllVenues();
		for (Venue venue : venueList) {
			if (venue.getMenuID() == venueSelection) {
				selectedVenue = venue;
				break;
			}
		}
		return selectedVenue;
	}
	
	
	
	
	private Venue rowToVenue(SqlRowSet row, int menuID) {
		Venue venue = new Venue();
		
		venue.setVenueName(row.getString("venue_name"));
		venue.setVenueId(row.getInt("id"));
		venue.setDescription(row.getString("description"));
		venue.setCityName(row.getString("city_name"));
		venue.setStateName(row.getString("state_abbreviation"));
		venue.setMenuID(menuID);
		
		return venue;
	}

	
}
