package com.techelevator.projects.model.jdbc;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.projects.model.Department;
import com.techelevator.projects.model.Employee;
import com.techelevator.projects.model.EmployeeDAO;

public class JDBCEmployeeDAO implements EmployeeDAO {

	private JdbcTemplate jdbcTemplate;

	public JDBCEmployeeDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Override
	public List<Employee> getAllEmployees() {
		
		String sql = "SELECT employee_id, department_id, first_name, last_name, birth_date, gender, hire_date FROM employee";
		SqlRowSet rows = jdbcTemplate.queryForRowSet(sql);
		
		List<Employee> employees = new ArrayList<Employee>();
		
			while (rows.next()) {
			employees.add(rowToEmployee(rows));
			}
		
			return employees;
		
	}

	@Override
	public List<Employee> searchEmployeesByName(String firstNameSearch, String lastNameSearch) {
		
		String sql = "SELECT employee_id, department_id, first_name, last_name, birth_date, gender, hire_date FROM employee"
				+ " WHERE first_name ILIKE ? OR last_name ILIKE ?";
		SqlRowSet rows = jdbcTemplate.queryForRowSet(sql, "%" + firstNameSearch + "%", "%" + lastNameSearch + "%");
		
		List<Employee> employeesFound = new ArrayList<Employee>();
		while (rows.next()) {
			employeesFound.add(rowToEmployee(rows));
		}

		return employeesFound;
		
	}

	@Override
	public List<Employee> getEmployeesByDepartmentId(long id) {
		
		String sql = "SELECT employee_id, department_id, first_name, last_name, birth_date, gender, hire_date FROM employee"
				+ " WHERE department_id = ?";
		SqlRowSet rows = jdbcTemplate.queryForRowSet(sql, id);
		
		List<Employee> employeesFound = new ArrayList<Employee>();
		while (rows.next()) {
			employeesFound.add(rowToEmployee(rows));
		}

		return employeesFound;
		
	}

	@Override
	public List<Employee> getEmployeesWithoutProjects() {
		
		String sql = "select e.employee_id, e.department_id, e.first_name, e.last_name, e.birth_date, e.gender, e.hire_date from employee e" + 
				" left join project_employee on e.employee_id = project_employee.employee_id" + 
				" where project_id is null";
		SqlRowSet rows = jdbcTemplate.queryForRowSet(sql);
		
		List<Employee> employeesFound = new ArrayList<Employee>();
		while (rows.next()) {
			employeesFound.add(rowToEmployee(rows));
		}

		return employeesFound;
		
	}

	@Override
	public List<Employee> getEmployeesByProjectId(Long projectId) {
		String sql = "SELECT e.employee_id, e.department_id, e.first_name, e.last_name, e.birth_date, e.gender, e.hire_date FROM employee e " + 
				"JOIN project_employee ON e.employee_id = project_employee.employee_id " + 
				"WHERE project_id = ?";
		SqlRowSet rows = jdbcTemplate.queryForRowSet(sql, projectId);
		
		List<Employee> employeesFound = new ArrayList<Employee>();
		while (rows.next()) {
			employeesFound.add(rowToEmployee(rows));
		}

		return employeesFound;
	}

	@Override
	public void changeEmployeeDepartment(Long employeeId, Long departmentId) {
		String sql = "UPDATE employee " + 
				"SET department_id = ? " + 
				"WHERE employee_id = ?";
		jdbcTemplate.update(sql, departmentId, employeeId);
	}
	
	private Employee rowToEmployee(SqlRowSet row) {
		Employee employee = new Employee();		
		
		employee.setBirthDay(row.getDate("birth_date").toLocalDate());
		employee.setFirstName(row.getString("first_name"));
		employee.setLastName(row.getString("last_name"));
		employee.setGender(row.getString("gender").charAt(0));
		employee.setHireDate(row.getDate("hire_date").toLocalDate());
		employee.setDepartmentId(row.getLong("department_id"));
		employee.setId(row.getLong("employee_id"));
		
		return employee;
	}

}
