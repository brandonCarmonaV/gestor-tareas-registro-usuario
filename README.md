# Backend de paneles

Servicio REST para crear y administrar paneles de tareas asociados al usuario autenticado. Esta guía cubre exclusivamente el proyecto `backend-paneles/`.

El backend valida la cookie `access_token` consultando un servicio de autenticación externo y puede enviar notificaciones a un backend de reportes. Estos servicios externos no forman parte de este proyecto.

## Tecnologías

- Java 21
- Spring Boot 3.5
- Spring Web
- Spring Data JPA e Hibernate
- PostgreSQL
- Maven
- Docker

## Requisitos

- JDK 21
- Maven
- Una base de datos PostgreSQL accesible
- Un backend de autenticación accesible para validar los tokens
- Un backend de reportes accesible si se necesitan las notificaciones por webhook
- Docker para construir y ejecutar la imagen

## Configuración

La configuración de la aplicación se encuentra en `src/main/resources/application.yml`. Puede configurarse mediante estas variables de entorno:

| Variable | Uso | Valor predeterminado |
|---|---|---|
| `DB_URL` | URL JDBC de PostgreSQL | Valor definido en `application.yml` |
| `DB_USERNAME` | Usuario de PostgreSQL | `postgres` |
| `DB_PASSWORD` | Contraseña de PostgreSQL | Valor definido en `application.yml` |
| `AUTH_BACK_HOST` | Host del backend de autenticación | `localhost` |
| `AUTH_BACK_PORT` | Puerto del backend de autenticación | `8090` |
| `REPORT_BACK_HOST` | Host del backend de reportes | `localhost` |
| `REPORT_BACK_PORT` | Puerto del backend de reportes | `8083` |
| `REPORTES_URL` | URL completa del webhook de reportes | Se construye con el host y puerto anteriores |

Configura `DB_PASSWORD` y las demás credenciales por un mecanismo seguro del entorno; no uses valores por defecto inseguros ni subas secretos al repositorio.

Si se ejecuta en Docker junto a otros servicios, `localhost` apunta al contenedor de paneles. Usa el nombre DNS del servicio correspondiente en la red compartida de contenedores. El repositorio no incluye un archivo Docker Compose.

## Ejecución local

Desde este directorio (`backend-paneles/`), configura las variables requeridas y ejecuta:

```bash
mvn spring-boot:run
```

La aplicación escucha en el puerto `8081`.

## API

Todas las rutas tienen el prefijo `/api/paneles`. Las operaciones requieren la cookie `access_token` válida; el servicio la valida con el backend de autenticación.

| Método | Ruta | Descripción |
|---|---|---|
| `POST` | `/api/paneles` | Crea un panel para el usuario autenticado |
| `GET` | `/api/paneles` | Lista los paneles del usuario autenticado |
| `PUT` | `/api/paneles/{id}` | Actualiza un panel |
| `DELETE` | `/api/paneles/{id}` | Elimina un panel |

### Crear un panel

```http
POST /api/paneles
Content-Type: application/json
Cookie: access_token=<token>
```

```json
{
  "nombre": "Proyecto inicial",
  "color": "#4F46E5",
  "fechaInicio": "2026-01-15",
  "fechaFin": "2026-02-15",
  "prioridad": 1,
  "descripcion": "Panel de ejemplo"
}
```

El campo `nombre` es obligatorio. Las fechas usan el formato `YYYY-MM-DD`.

### Actualizar un panel

La ruta recibe el identificador del panel (`/api/paneles/{id}`). En la implementación actual, el servicio obtiene el identificador de búsqueda de `panelId` en el cuerpo JSON. Incluye ese campo en la solicitud hasta que la ruta y el servicio se alineen para usar el ID de la URL:

```json
{
  "panelId": "id-del-panel",
  "nombre": "Nombre actualizado",
  "color": "#16A34A",
  "fechaInicio": "2026-01-15",
  "fechaFin": "2026-02-20",
  "prioridad": 2,
  "descripcion": "Descripción actualizada"
}
```

## Compilación y pruebas

Desde `backend-paneles/`:

```bash
mvn test
mvn package
```

El JAR ejecutable se genera en `target/`.

## Docker

Desde la raíz del repositorio, construye la imagen con `backend-paneles/` como contexto:

```bash
docker build -f backend-paneles/dockerfile -t backend-paneles:local backend-paneles
```

Inicia el contenedor configurando los hosts de los servicios según la red donde se ejecuta:

```bash
docker run --rm -p 8081:8081 `
  -e DB_URL="<jdbc-url>" `
  -e DB_USERNAME="<database-user>" `
  -e DB_PASSWORD="<database-password>" `
  -e AUTH_BACK_HOST="<auth-host-reachable-from-container>" `
  -e AUTH_BACK_PORT="8090" `
  -e REPORT_BACK_HOST="<reports-host-reachable-from-container>" `
  -e REPORT_BACK_PORT="8083" `
  backend-paneles:local
```

El Dockerfile usa una etapa Maven para compilar y una imagen JRE para ejecutar la aplicación como usuario no root. Expone el puerto `8081`.

## Estructura del proyecto

```text
backend-paneles/
├── src/
│   ├── main/
│   │   ├── java/com/gestortareas/paneles/
│   │   │   ├── application/       # Casos de uso y servicios
│   │   │   ├── domain/            # Modelo, excepciones y puertos
│   │   │   └── infrastructure/    # REST, persistencia e integraciones
│   │   └── resources/
│   │       └── application.yml    # Configuración de Spring
│   └── test/                      # Pruebas
├── dockerfile
└── pom.xml
```
