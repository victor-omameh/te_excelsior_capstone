SELECT contact_id, first_name, last_name FROM contact
WHERE contact_id = ?;


INSERT INTO contact (contact_id, first_name, last_name) VALUES (DEFAULT, ?, ?) RETURNING contact_id;

UPDATE contact SET first_name = ?, last_name = ? WHERE contact_id = ?;

DELETE FROM contact WHERE contact_id = ?;

SELECT department_id, name FROM department
WHERE name ILIKE ?;


INSERT INTO department (department_id, name) VALUES (DEFAULT, ?) RETURNING department_id;

select * from contact;

START TRANSACTION;

TRUNCATE contact CASCADE;

ROLLBACK;

SELECT * FROM contact WHERE contact_id = ?;


SELECT employee_id FROM project_employee WHERE project_id = null;

select e.employee_id, e.department_id, e.first_name, e.last_name, e.birth_date, e.gender, e.hire_date
from employee e
join project_employee on e.employee_id = project_employee.employee_id
where project_id = ?; 

SELECT project_id, name, from_date, to_date FROM project;

SELECT project_id, employee_id FROM project_employee;

start transaction;
INSERT INTO department (department_id, name) VALUES (DEFAULT, ?);
SELECT department_id, name FROM department;
rollback;
SELECT project_id, employee_id FROM project_employee;
SELECT employee_id, department_id, first_name, last_name, birth_date, gender, hire_date FROM employee;
