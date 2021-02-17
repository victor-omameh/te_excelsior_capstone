package com.techelevator.projects.model.jdbc;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.projects.model.Employee;
import com.techelevator.projects.model.Project;
import com.techelevator.projects.model.ProjectDAO;

public class JDBCProjectDAO implements ProjectDAO {

	private JdbcTemplate jdbcTemplate;

	public JDBCProjectDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Override
	public List<Project> getAllActiveProjects() {
		String sql = "SELECT project_id, name, from_date, to_date FROM project";
		SqlRowSet rows = jdbcTemplate.queryForRowSet(sql);
		
		List<Project> projects = new ArrayList<Project>();
		
			while (rows.next()) {
			projects.add(rowToProject(rows));
			}
		
			return projects;
	}

	@Override
	public void removeEmployeeFromProject(Long projectId, Long employeeId) {
		String sql = "DELETE FROM project_employee WHERE project_id = ? AND employee_id = ?";
		
		jdbcTemplate.update(sql, projectId, employeeId);
	}

	@Override
	public void addEmployeeToProject(Long projectId, Long employeeId) {
		String sql = "INSERT INTO project_employee (project_id, employee_id) VALUES (?, ?)";
		
		jdbcTemplate.update(sql, projectId, employeeId);
	}
	
	private Project rowToProject(SqlRowSet row) {
		Project project = new Project();
		
		project.setName(row.getString("name"));
		project.setId(row.getLong("project_id"));
		
		if (row.getDate("to_date") == null) {
			project.setEndDate(null);
		} else {
			project.setEndDate(row.getDate("to_date").toLocalDate());
		}
		
		if (row.getDate("from_date") == null) {
			project.setStartDate(null);
		} else {
			project.setStartDate(row.getDate("from_date").toLocalDate());
		}
		
		return project;
	}

}
