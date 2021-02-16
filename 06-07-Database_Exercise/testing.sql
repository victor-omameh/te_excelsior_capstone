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

select * 
from employee
join project_employee on employee.employee_id = project_employee.employee_id; 
