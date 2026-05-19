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
| Administrador | admin@hospital.com | 1234 |
| Médico | carlos@hospital.com | 1234 |
| Médico | laura@hospital.com | 1234 |
| Recepcionista | recepcion@hospital.com | 1234 |
| Enfermero | enfermeria@hospital.com | 1234 |
| Farmacéutico | farmacia@hospital.com | 1234 |
| Paciente | ana@email.com | 1234 |
| Paciente | pedro@email.com | 1234 |

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

### Farmacéutico
- Consultar las recetas médicas emitidas buscando por DNI del paciente
- Registrar la entrega de medicamentos (dispensación) a partir del ID de receta

### Paciente
- Consultar su propio historial médico
- Ver y cancelar sus citas
- Descargar su informe médico en PDF

---

## Verificación de las historias de usuario del farmacéutico

### HU1 — Como farmacéutico, quiero consultar las recetas médicas emitidas

**Prerequisito:** debe existir al menos una receta para el paciente Ana (DNI `12345678A`).  
Si no hay ninguna, créala primero siguiendo los pasos de la HU0 más abajo.

1. Inicia sesión con `farmacia@hospital.com` / `1234`
2. El sistema redirige automáticamente a `/farmaceutico/inicio`
3. En la sección **"Consultar recetas médicas"**, introduce el DNI `12345678A` y pulsa **Buscar recetas**
4. Se muestra la lista de recetas del paciente con medicamento, dosis, posología, duración e instrucciones
5. Cada receta incluye el botón **"Dispensar medicamento"**

**Verificación por API:**
```
GET http://localhost:8080/api/recetas/paciente/dni/12345678A
GET http://localhost:8080/api/recetas
```

---

### HU2 — Como farmacéutico, quiero registrar la entrega de medicamentos

1. Desde la lista de recetas (paso anterior), pulsa **"Dispensar medicamento"** en la receta deseada
2. El formulario se precarga con el ID de la receta y el ID del farmacéutico en sesión
3. Rellena la **cantidad dispensada** (mínimo 1), selecciona el **estado** (DISPENSADO / PARCIAL / PENDIENTE) y añade observaciones si procede
4. Pulsa **"Registrar dispensación"**
5. Se muestra el resumen con medicamento, paciente, cantidad y estado

También puedes acceder directamente desde el panel con el ID de la receta:  
`/farmaceutico/dispensar?recetaId=<ID>`

**Verificación por API:**
```
POST http://localhost:8080/api/dispensaciones
Content-Type: application/json

{
  "recetaId": 1,
  "farmaceuticoId": 1,
  "cantidadDispensada": 2,
  "estado": "DISPENSADO",
  "observaciones": "Entrega correcta"
}
```

---

### HU0 — Crear una receta de prueba (prerequisito)

1. Inicia sesión como médico con `carlos@hospital.com` / `1234`
2. En la agenda, localiza una cita de Ana Martínez
3. Ve a **"Nueva receta"** e introduce: medicamento, dosis, posología y duración
4. Guarda la receta — ya estará disponible para el farmacéutico

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
| `RecetaServiceTest` | Unitario | 14 tests: crear receta, obtener por id/paciente/DNI |
| `RecetaControllerIntegrationTest` | Integración | 6 tests: endpoints GET y POST de recetas |
| `DispensacionServiceTest` | Unitario | 11 tests: registrar dispensación, obtener por farmacéutico/paciente/receta |
| `DispensacionControllerIntegrationTest` | Integración | 4 tests: endpoints POST y GET de dispensaciones |

### Ejecutar todos los tests

```bash
gradlew.bat test
```

---

## Integración Continua

El proyecto usa **GitHub Actions**. En cada push a `master` se ejecuta automáticamente la compilación y los tests.

Configuración en: `.github/workflows/main.yml`
