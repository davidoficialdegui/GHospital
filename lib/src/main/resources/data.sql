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

-- Enfermeros
INSERT INTO enfermeros (id, nombre, apellido1, apellido2, dni, email, password,
                        activo, rol, unidad, turno, numero_colegiado)
VALUES (1, 'Lucía', 'Hernández', 'Vega', '55555555E', 'enfermeria@hospital.com',
        '1234', true, 'ENFERMERO', 'Cardiología', 'MAÑANA', 'COL-12345');

INSERT INTO enfermeros (id, nombre, apellido1, apellido2, dni, email, password,
                        activo, rol, unidad, turno, numero_colegiado)
VALUES (2, 'Javier', 'Romero', 'Díaz', '66666666F', 'javier.enf@hospital.com',
        '1234', true, 'ENFERMERO', 'Urgencias', 'TARDE', 'COL-12346');

ALTER TABLE enfermeros ALTER COLUMN id RESTART WITH 10;

-- Farmacéuticos
INSERT INTO farmaceuticos (id, nombre, apellido1, apellido2, dni, email, password,
                           activo, rol, numero_colegiado, turno)
VALUES (1, 'Marta', 'Ruiz', 'Navarro', '77777777G', 'farmacia@hospital.com',
        '1234', true, 'FARMACEUTICO', 'COL-99001', 'MAÑANA');

ALTER TABLE farmaceuticos ALTER COLUMN id RESTART WITH 10;

-- Recetas de demo (emitidas por Carlos, paciente Ana)
INSERT INTO recetas (id, paciente_id, medico_id, medicamento, dosis, posologia, duracion_dias, instrucciones, fecha_emision, fecha_creacion)
VALUES (1, 1, 1, 'Ibuprofeno', '600mg', '1 comprimido cada 8 horas con las comidas', 7, 'No superar 3 comprimidos al día', CURRENT_DATE, CURRENT_TIMESTAMP);

INSERT INTO recetas (id, paciente_id, medico_id, medicamento, dosis, posologia, duracion_dias, instrucciones, fecha_emision, fecha_creacion)
VALUES (2, 1, 1, 'Amoxicilina', '500mg', '1 cápsula cada 8 horas', 10, 'Completar el tratamiento aunque mejore', CURRENT_DATE, CURRENT_TIMESTAMP);

INSERT INTO recetas (id, paciente_id, medico_id, medicamento, dosis, posologia, duracion_dias, instrucciones, fecha_emision, fecha_creacion)
VALUES (3, 2, 2, 'Paracetamol', '1g', '1 comprimido cada 6 horas si hay dolor', 5, 'Máximo 4 comprimidos al día', CURRENT_DATE, CURRENT_TIMESTAMP);

ALTER TABLE recetas ALTER COLUMN id RESTART WITH 10;

-- Dispensaciones de demo (farmacéutico Marta entrega receta 1 a Ana)
INSERT INTO dispensaciones (id, receta_id, farmaceutico_id, cantidad_dispensada, estado, observaciones, fecha_dispensacion, fecha_creacion)
VALUES (1, 1, 1, 1, 'DISPENSADO', 'Entrega completa sin incidencias', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

ALTER TABLE dispensaciones ALTER COLUMN id RESTART WITH 10;