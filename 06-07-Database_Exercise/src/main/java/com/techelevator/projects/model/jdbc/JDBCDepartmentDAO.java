package com.techelevator.projects.model.jdbc;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.projects.model.Department;
import com.techelevator.projects.model.DepartmentDAO;

public class JDBCDepartmentDAO implements DepartmentDAO {
	
	private JdbcTemplate jdbcTemplate;

	public JDBCDepartmentDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public List<Department> getAllDepartments() {
		
		String sql = "SELECT department_id, name FROM department";
		SqlRowSet rows = jdbcTemplate.queryForRowSet(sql);
		
		List<Department> departments = new ArrayList<Department>();
		
		while (rows.next()) {
			departments.add(rowToDepartment(rows));
		}
		
		return departments;
	}

	@Override
	public List<Department> searchDepartmentsByName(String nameSearch) {
		
		String sql = "SELECT department_id, name FROM department" + 
				" WHERE name ILIKE ?";
		SqlRowSet rows = jdbcTemplate.queryForRowSet(sql, "%" + nameSearch + "%");
		
		List<Department> departmentsFound = new ArrayList<Department>();
		while (rows.next()) {
			departmentsFound.add(rowToDepartment(rows));
		}

		return departmentsFound;
	}

	@Override
	public void saveDepartment(Department updatedDepartment) {
		
		String sql = "UPDATE department SET name = ? WHERE department_id = ?";
		jdbcTemplate.update(sql, updatedDepartment.getName(), updatedDepartment.getId());
	
	}

	@Override
	public Department createDepartment(Department newDepartment) {
		
		String sql = "INSERT INTO department (department_id, name) VALUES (DEFAULT, ?) RETURNING department_id";
		SqlRowSet rows = jdbcTemplate.queryForRowSet(sql, newDepartment.getName());
		
		
		if (rows.next()) {
			newDepartment.setId(rows.getLong("department_id"));
		}
		
		return newDepartment;
	}

	@Override
	public Department getDepartmentById(Long id) {
		String sql = "SELECT department_id, name FROM department WHERE department_id = ?";
		
		SqlRowSet rows = jdbcTemplate.queryForRowSet(sql, id);
		
		if (rows.next()) {
			return rowToDepartment(rows);
		}
		
		return null;
	}
	
	private Department rowToDepartment (SqlRowSet row) {
		Department department = new Department();
		
		department.setName(row.getString("name"));
		department.setId(row.getLong("department_id"));
		
		return department;
		
	}

}
