-- Médicos
INSERT INTO medicos (id, nombre, apellido1, apellido2, especialidad, email, password, activo, dni, rol)
VALUES (1, 'Carlos', 'García', 'López', 'Cardiología', 'carlos@hospital.com', '1234', true, '11111111A', 'MEDICO');

INSERT INTO medicos (id, nombre, apellido1, apellido2, especialidad, email, password, activo, dni, rol)
VALUES (2, 'Laura', 'Sánchez', 'Pérez', 'Pediatría', 'laura@hospital.com', '1234', true, '22222222B', 'MEDICO');

-- Pacientes
INSERT INTO pacientes (id, nombre, apellido1, apellido2, dni, email, password, activo, rol)
VALUES (1, 'Ana', 'Martínez', 'Ruiz', '12345678A', 'ana@email.com', '1234', true, 'PACIENTE');

INSERT INTO pacientes (id, nombre, apellido1, apellido2, dni, email, password, activo, rol)
VALUES (2, 'Pedro', 'López', 'Gómez', '87654321B', 'pedro@email.com', '1234', true, 'PACIENTE');

-- Recepcionistas (rol ADMIN)
INSERT INTO recepcionistas (id, nombre, apellido1, apellido2, dni, email, password, activo, rol, departamento_asignado, turno)
VALUES (1, 'David', 'Oficialdegui', 'Fernández', '33333333C', 'admin@hospital.com', '1234', true, 'ADMIN', 'Administración', 'MAÑANA');

INSERT INTO recepcionistas (id, nombre, apellido1, apellido2, dni, email, password, activo, rol, departamento_asignado, turno)
VALUES (2, 'María', 'Torres', 'Alba', '44444444D', 'recepcion@hospital.com', '1234', true, 'RECEPCIONISTA', 'Urgencias', 'TARDE');

-- Citas para Ana (paciente_id=1)
INSERT INTO citas (id, fecha_hora, estado, motivo, especialidad, paciente_id, medico_id, recepcionista_id, fecha_creacion, fecha_actualizacion)
VALUES (1, '2026-04-20 09:00:00', 'REALIZADA', 'Revisión anual', 'Cardiología', 1, 1, 1, '2026-04-01 10:00:00', '2026-04-01 10:00:00');

INSERT INTO citas (id, fecha_hora, estado, motivo, especialidad, paciente_id, medico_id, recepcionista_id, fecha_creacion, fecha_actualizacion)
VALUES (2, '2026-04-28 11:00:00', 'CONFIRMADA', 'Dolor de cabeza recurrente', 'Cardiología', 1, 1, 1, '2026-04-10 09:00:00', '2026-04-10 09:00:00');

INSERT INTO citas (id, fecha_hora, estado, motivo, especialidad, paciente_id, medico_id, recepcionista_id, fecha_creacion, fecha_actualizacion)
VALUES (3, '2026-05-05 10:30:00', 'PENDIENTE', 'Control de tensión', 'Cardiología', 1, 2, 2, '2026-04-15 08:00:00', '2026-04-15 08:00:00');

INSERT INTO citas (id, fecha_hora, estado, motivo, especialidad, paciente_id, medico_id, fecha_creacion, fecha_actualizacion)
VALUES (4, '2026-03-10 16:00:00', 'CANCELADA', 'Vacuna anual', 'Pediatría', 1, 2, '2026-03-01 12:00:00', '2026-03-01 12:00:00');

-- Resetear secuencias para que el auto-increment no colisione con los datos iniciales
ALTER TABLE pacientes ALTER COLUMN id RESTART WITH 10;
ALTER TABLE medicos ALTER COLUMN id RESTART WITH 10;
ALTER TABLE recepcionistas ALTER COLUMN id RESTART WITH 10;
ALTER TABLE citas ALTER COLUMN id RESTART WITH 10;