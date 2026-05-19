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

### HU0 — Recetas precargadas (no requiere acción manual)

Al arrancar la aplicación ya existen **3 recetas de demo** en la base de datos:

| ID | Paciente | Medicamento | Estado |
|---|---|---|---|
| 1 | Ana (DNI `12345678A`) | Ibuprofeno 600mg | Ya dispensada |
| 2 | Ana (DNI `12345678A`) | Amoxicilina 500mg | Pendiente de dispensar |
| 3 | Pedro (DNI `87654321B`) | Paracetamol 1g | Pendiente de dispensar |

Si quieres crear más recetas: inicia sesión como médico (`carlos@hospital.com` / `1234`), ve a la agenda y usa **"Nueva receta"**.

---

## Base de datos y datos de demo

La aplicación usa **H2 en memoria**. Los datos se inicializan automáticamente al arrancar desde `data.sql` y `DataInitializer.java`.

**La base de datos se resetea en cada reinicio** de la aplicación.

### Datos precargados al arrancar

| Tipo | Cantidad | Detalle |
|---|---|---|
| Médicos | 2 | Carlos (Cardiología), Laura (Pediatría) |
| Pacientes | 2 | Ana (DNI `12345678A`), Pedro (DNI `87654321B`) |
| Recepcionistas | 2 | Admin y Recepcionista |
| Enfermeros | 2 | Lucía (Cardiología), Javier (Urgencias) |
| Farmacéuticos | 1 | Marta |
| Citas | 4 | En distintos estados (REALIZADA, CONFIRMADA, PENDIENTE, CANCELADA) |
| Recetas | 3 | Ibuprofeno y Amoxicilina para Ana, Paracetamol para Pedro |
| Dispensaciones | 1 | Marta dispensó Ibuprofeno a Ana |

Con estos datos se puede probar **todo el flujo** sin crear datos manualmente:
- El farmacéutico busca por DNI `12345678A` y ve las recetas de Ana
- Puede dispensar la receta 2 (Amoxicilina) que aún no ha sido entregada
- El médico ve las recetas emitidas desde su panel
- La consola H2 está disponible en `http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:mem:hospitaldb`)

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

## API REST — Swagger / OpenAPI

Con la aplicación en marcha, la documentación interactiva del API está disponible en:

```
http://localhost:8080/swagger-ui/index.html
```

El JSON de la especificación OpenAPI 3.0:

```
http://localhost:8080/v3/api-docs
```

Los endpoints están agrupados por etiqueta:

| Etiqueta | Endpoints |
|---|---|
| Citas | `/api/citas` |
| Diagnósticos | `/api/diagnosticos` |
| Recetas | `/api/recetas` |
| Dispensaciones | `/api/dispensaciones` |
| Constantes Vitales | `/api/constantes` |
| Pacientes | `/api/pacientes` |

---

## Documentación Javadoc

La documentación Javadoc se genera automáticamente y se publica en **GitHub Pages** en cada push a `master`.

Para generarla localmente:

```bash
./gradlew :lib:javadoc
```

El resultado se guarda en `lib/build/docs/javadoc/index.html`.

---

## Integración Continua

El proyecto usa **GitHub Actions**. En cada push a `master` se ejecuta automáticamente:
1. Compilación
2. Tests unitarios
3. Generación de Javadoc
4. Publicación del Javadoc en GitHub Pages

Configuración en: `.github/workflows/main.yml`
