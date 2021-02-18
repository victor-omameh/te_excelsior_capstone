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
		
		String sql = "SELECT venue.id, venue.name AS venue_name, venue.description, city.name AS city_name, state.name AS state_name, category.name AS category_name FROM venue " + 
				"JOIN city ON venue.city_id = city.id " + 
				"JOIN state ON city.state_abbreviation = state.abbreviation " + 
				"JOIN category_venue ON venue.id = category_venue.venue_id " + 
				"JOIN category ON category_venue.category_id = category.id";
		
		SqlRowSet rows = jdbcTemplate.queryForRowSet(sql);
		
		List<Venue> venueList = new ArrayList<Venue>();
		
		while(rows.next()) {
			venueList.add(rowToVenue(rows));
		}
				
		return venueList;
	}
	

	private Venue rowToVenue(SqlRowSet row) {
		Venue venue = new Venue();
		
		venue.setVenueName(row.getString("venue_name"));
		venue.setVenueId(row.getInt("id"));
		venue.setDescription(row.getString("description"));
		venue.setCityName(row.getString("city_name"));
		venue.setStateName(row.getString("state_name"));
		venue.setCategoryName(row.getString("category_name"));
		
		
		return venue;
	}
	
}
