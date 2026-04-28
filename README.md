# GHospital — Sistema de Gestión Hospitalaria

Aplicación web desarrollada con **Spring Boot** para la gestión de pacientes, médicos, citas y diagnósticos en un entorno hospitalario.

---

## Tecnologías

| Tecnología | Versión |
|---|---|
| Java | 21 |
| Spring Boot | 3.2.5 |
| Spring Data JPA | - |
| Thymeleaf | - |
| H2 Database (en memoria) | - |
| OpenPDF | 1.3.30 |
| JUnit 5 | - |
| Mockito | - |
| Gradle | 8.9 |

---

## Arquitectura

El proyecto sigue una **arquitectura de 3 capas**:

```
Presentación  →  Controller  (Spring MVC + Thymeleaf)
Negocio       →  Facade / Service
Datos         →  Repository  (Spring Data JPA) + Entity
```

Paquetes principales:
- `controller` — Controladores MVC y REST
- `facade` — Capa de fachada entre controladores y servicios
- `service` — Lógica de negocio
- `repository` — Acceso a datos (JPA)
- `entity` — Entidades JPA
- `dto` — Objetos de transferencia de datos
- `config` — Configuración de la aplicación

---

## Requisitos previos

- **Java 21** o superior
- **Git**

---

## Pasos para construir y ejecutar

### 1. Clonar el repositorio

```bash
git clone https://github.com/davidoficialdegui/GHospital.git
cd GHospital
```

### 2. Ejecutar los tests

```bash
./gradlew test
```

En Windows:
```bash
gradlew.bat test
```

### 3. Arrancar la aplicación

```bash
./gradlew :lib:bootRun
```

En Windows:
```bash
gradlew.bat :lib:bootRun
```

### 4. Acceder a la aplicación

Abrir el navegador en:

```
http://localhost:8080/login
```

---

## Credenciales de acceso

| Rol | Email | Contraseña |
|---|---|---|
| Administrador | admin@hospital.com | 123 |
| Médico | carlos@hospital.com | 123 |
| Médico | laura@hospital.com | 123 |
| Recepcionista | recep@hospital.com | 123 |
| Paciente | ana@email.com | 123 |
| Paciente | pedro@email.com | 123 |

---

## Funcionalidades por rol

### Administrador
- Gestión de usuarios (pacientes, médicos, recepcionistas)
- Editar y eliminar usuarios
- Asignar y cambiar roles
- Ver estadísticas del sistema

### Recepcionista
- Gestor de citas: ver todas las citas del sistema
- Crear nuevas citas
- Cambiar el estado de las citas (Pendiente, Confirmada, Realizada, Cancelada)

### Médico
- Ver su propia agenda de citas
- Consultar el historial clínico de sus pacientes
- Registrar diagnósticos y tratamientos
- Descargar informes médicos en PDF

### Paciente
- Consultar su propio historial médico
- Ver y cancelar sus citas
- Descargar su informe médico en PDF

---

## Base de datos

La aplicación usa **H2 en memoria**. Los datos se inicializan automáticamente al arrancar desde `data.sql` y `DataInitializer.java`.

La base de datos se resetea en cada reinicio de la aplicación.

---

## Tests

Los tests se encuentran en `lib/src/test/java/com/gestionHospitalaria/`:

| Fichero | Tipo | Descripción |
|---|---|---|
| `AdminServiceTest` | Unitario | 27 tests: editar, borrar, asignar rol y estadísticas |
| `CitaServiceCancelacionTest` | Unitario | Cancelación de citas |
| `CitaServiceAgendaTest` | Unitario | Agenda del médico |
| `DiagnosticoServiceTest` | Unitario | Registro de diagnósticos |
| `PacienteServiceHistorialTest` | Unitario | Historial del paciente |
| `InformeMedicoServiceTest` | Unitario | Generación de informes PDF |
| `DiagnosticoControllerIntegrationTest` | Integración | Test de integración del controlador |

### Ejecutar todos los tests

```bash
gradlew.bat test
```

---

## Integración Continua

El proyecto usa **GitHub Actions**. En cada push a `master` se ejecuta automáticamente la compilación y los tests.

Configuración en: `.github/workflows/main.yml`
