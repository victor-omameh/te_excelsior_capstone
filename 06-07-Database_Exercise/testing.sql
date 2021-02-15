SELECT contact_id, first_name, last_name FROM contact
WHERE contact_id = ?;


INSERT INTO contact (contact_id, first_name, last_name) VALUES (DEFAULT, ?, ?) RETURNING contact_id;

UPDATE contact SET first_name = ?, last_name = ? WHERE contact_id = ?;

DELETE FROM contact WHERE contact_id = ?;

SELECT department_id, name FROM department;
WHERE name ILIKE 'Department of Redundancy Department';

