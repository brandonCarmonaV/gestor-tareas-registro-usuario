# Backend Paneles

Backend del modulo de paneles (equivalente a proyectos) de la aplicacion de gestion de tareas.

## Organizacion

- `domain`: modelos y puertos que representan las reglas y contratos del dominio.
- `application`: servicios que orquestan los casos de uso.
- `infrastructure`: adaptadores REST, RMI y persistencia JPA, junto con la configuracion.

La estructura sigue arquitectura hexagonal: el dominio define los puertos y la infraestructura proporciona sus adaptadores.

## Ejecucion local

Requiere Java 21 y Maven. Configura `DB_URL`, `DB_USERNAME`, `DB_PASSWORD` y las propiedades RMI mediante variables de entorno si necesitas valores distintos a los predeterminados.

```bash
./mvnw spring-boot:run
```

En Windows puede usarse `mvnw.cmd spring-boot:run`.

## Perfil de pruebas con H2

El perfil `test` usa una base H2 en memoria y crea las tablas automáticamente. No requiere PostgreSQL:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=test
```

Para ejecutar la suite Robot Framework, inicia la aplicación en el puerto usado por la suite y ejecuta RobotCode:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=test -Dspring-boot.run.arguments="--server.port=8082 --rmi.registry-port=1101"
robotcode robot robot
```

La suite usa `X-User-Id` como identidad temporal, por lo que no necesita que el servicio Auth esté disponible. Los resultados se generan en `results/`.
