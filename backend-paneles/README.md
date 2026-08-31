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
