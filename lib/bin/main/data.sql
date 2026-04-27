INSERT INTO medicos (id, nombre, apellido1, apellido2, especialidad, email, password, activo, dni, rol)
VALUES (1, 'Carlos', 'García', 'López', 'Cardiología', 'carlos@hospital.com', '1234', true, '11111111A', 'MEDICO');

INSERT INTO medicos (id, nombre, apellido1, apellido2, especialidad, email, password, activo, dni, rol)
VALUES (2, 'Laura', 'Sánchez', 'Pérez', 'Pediatría', 'laura@hospital.com', '1234', true, '22222222B', 'MEDICO');

INSERT INTO pacientes (id, nombre, apellido1, apellido2, dni, email, password, activo, rol)
VALUES (1, 'Ana', 'Martínez', 'Ruiz', '12345678A', 'ana@email.com', '1234', true, 'PACIENTE');

INSERT INTO pacientes (id, nombre, apellido1, apellido2, dni, email, password, activo, rol)
VALUES (2, 'Pedro', 'López', 'Gómez', '87654321B', 'pedro@email.com', '1234', true, 'PACIENTE');