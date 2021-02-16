START TRANSACTION;
UPDATE department SET department_id = ?, name = ? WHERE department_id = ?;
ROLLBACK;

SELECT * FROM department;

INSERT INTO department (department_id, name) VALUES (DEFAULT, ?) RETURNING department_id;

SELECT department_id, name FROM department WHERE department_id = ?;
