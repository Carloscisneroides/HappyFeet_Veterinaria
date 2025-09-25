# HappyFeet Veterinaria

Análisis técnico detallado del estado actual del proyecto, arquitectura, componentes, base de datos y recomendaciones.

## 1. Resumen ejecutivo
HappyFeet Veterinaria es un proyecto Java (Maven) orientado a gestionar procesos de una clínica veterinaria: dueños, mascotas, citas, inventario, facturación y más. Actualmente se trata de una base de código con enfoque educativo/POC que incluye:
- Conexión JDBC a MySQL mediante un singleton.
- Entidades de dominio modeladas en Java con patrones como Builder y enums ricos (State/estrategias).
- Interfaces de repositorio y servicios (contratos), con una implementación de servicio completa para Dueño y Mascota.
- Esquema SQL completo y datos de ejemplo.

El proyecto aún no implementa persistencia completa (no hay repositorios JDBC/JPA), y existen algunas inconsistencias que deben abordarse (ver sección 9).

## 2. Arquitectura y capas
Estructura por capas propuesta e implementada parcialmente:
- Capa de utilidades
  - DatabaseConnection: Singleton que abre/cierra conexión JDBC (Driver MySQL 8.x) y carga propiedades desde `database.properties`.
- Capa de dominio (model)
  - Entidades: Dueno, Mascota, Especie, Raza, Cita, Servicio, Inventario, Factura, HistorialMedico, Veterinario, ItemFactura.
  - Enums enriquecidos: Urgencia (con lógica), CitaEstado (dos variantes, ver inconsistencias), SexoMascota, TipoMovimiento.
- Capa de repositorios
  - Contrato genérico `CrudRepository<T,ID>` y contratos específicos: DuenoRepository, MascotaRepository, InventarioRepository, CitaRepository (interfaz).
  - No se incluyen implementaciones JDBC/JPA para la mayoría salvo `impl/DuenoRepositoryImpl` (esqueleto presente en target pero no en src) — ver sección 8.
- Capa de servicios
  - Interfaces: DuenoService, MascotaService, InventarioService.
  - Implementaciones: DuenoServiceImpl, MascotaServiceImpl. Encapsulan validaciones de negocio, merges parciales y restricciones de unicidad (documento, microchip).
- Entrada/ejecución
  - Main: pruebas manuales de CRUD contra la tabla `especies` usando JDBC directo, a modo de smoke test.

## 3. Patrones y estilos de diseño
- Singleton: DatabaseConnection (double-checked locking, registro del driver). Provee reconexión bajo demanda y utilidades de diagnóstico.
- Builder: Dueno, Mascota, Especie cuentan con builders que fortalecen invariantes y legibilidad de construcción.
- Enums ricos (Strategy/State-like):
  - Urgencia define comportamiento por nivel (tiempos, recursos, programabilidad) con utilidades de comparación y filtro.
  - CitaEstado (versión avanzada en `model.entities.enums`) usa métodos abstractos por estado para validar transiciones y propiedades (final, modificable, siguientes válidos). Nota: ver duplicidad en sección 9.
- Validaciones de dominio: Servicios realizan validaciones coherentes (no null, no blank, formatos básicos) y lanzan excepciones semánticas internas.

## 4. Entidades principales (src/main/java/com/happyfeet/model/entities)
- Dueno: Modelo rico con builder interno y utilidades para dividir nombre completo. Campos típicos (documento, email, contacto, fechas). Equals/hashCode por identidad lógica. Validaciones en service.
- Mascota: Builder, enum interno Sexo (Macho/Hembra) y atributos clínicos (microchip, alergias, peso). Servicios gestionan merges y reglas.
- Especie: Builder simple que garantiza nombre requerido. Main usa Especie para pruebas JDBC.
- Cita: Estado por enum `com.happyfeet.model.enums.CitaEstado` (versión simple). Campos: idVet, idMascota, inicio/fin, motivo. Nota: hay una segunda definición más rica del enum en `model.entities.enums` no usada aquí.
- Inventario: Estructura mínima (id, código, nombre, precioVenta). Faltan campos y comportamiento de stock que el servicio asume.
- Otras entidades listadas (Factura, HistorialMedico, ItemFactura, Raza, Servicio, Veterinario) están definidas pero no todas se usan en código de servicios; el esquema SQL es más amplio que la capa de dominio actual.

## 5. Repositorios
- CrudRepository<T,ID>: contratos save/find/update/delete.
- DuenoRepository: consultas por nombre, documento, teléfono, email; helpers de compatibilidad con Long (existeById, findById(Long)).
- MascotaRepository: búsquedas por dueñoId, nombre, microchip; helpers de compatibilidad con Long.
- InventarioRepository: búsquedas por código y nombre; helpers Long y utilidades existsByCodigo.
- CitaRepository: interfaz presente en el árbol de archivos (compilado en target) pero no se provee implementación en src.

Actualmente NO existen implementaciones concretas (JDBC/JPA) en `src` para la mayoría de repositorios, por lo que los servicios no persisten realmente a una BD, salvo que el usuario provea implementaciones fuera de `src`.

## 6. Servicios
- DuenoServiceImpl
  - Reglas: documento único, email con @, longitudes razonables; ordena listados por Apellido/Nombre/Id.
  - Merge seguro: solo aplica cambios no nulos/blank. Excepciones de dominio: ValidacionException, RecursoNoEncontradoException, ConflictoDeDatosException.
- MascotaServiceImpl
  - Reglas: nombre, duenoId y sexo requeridos; unicidad de microchip; merges controlados; ordenamiento por nombre.
  - Misma familia de excepciones que Dueño.
- InventarioService
  - Método: descontarStostck(productoId, cantidad) [nota: typo en nombre]. Asume métodos de dominio en Inventario: estaVencido(), tieneStock(cantidad), descontarStock(cantidad). Estos NO existen en la entidad actual — inconsistencia importante (ver sección 9).

## 7. Base de datos
- Configuración: `database.properties` (local) con claves db.url, db.username, db.password. Si no existe, DatabaseConnection usa valores por defecto `jdbc:mysql://localhost:3306/happy_feet_veterinaria`, usuario root y crea un mensaje guía.
- Esquema: `database/schema.sql` define una base robusta con tablas:
  - Configuración: especies, razas, producto_tipos, evento_tipos, cita_estados.
  - Maestras: proveedores, veterinarios, servicios.
  - Operacionales: duenos, mascotas, citas, historial_medico, inventario, movimientos_inventario, facturas, items_factura, pagos.
  - Seguridad/usuarios: usuarios, sesiones.
  - Adicionales: puntos_cliente, recordatorios.
- Datos de ejemplo: `database/data.sql` inserta especies, razas, tipos de producto, tipos de evento, estados de cita, proveedores, veterinarios y servicios.
- Observaciones:
  - El schema es más completo que el modelo Java actual (p. ej., usuarios, sesiones, puntos_cliente, recordatorios no tienen entidad/servicio). Esto es aceptable en etapa temprana, pero debe converger.

## 8. Ejecución y pruebas manuales
- Requisitos: Java 17, MySQL activo y accesible (usuario y base configurados), Maven.
- Pasos sugeridos:
  1) Crear BD: ejecutar `database/schema.sql` y luego `database/data.sql` en MySQL.
  2) Configurar `database.properties` en la raíz del proyecto (misma carpeta que `pom.xml`).
  3) mvn -q -DskipTests package
  4) Ejecutar `com.happyfeet.Main` (muestra pruebas CRUD sobre `especies`).
- Pruebas automatizadas: El POM incluye JUnit 5 pero no hay tests en `src/test`. Se recomienda añadir pruebas para servicios y utilidades.

## 9. Inconsistencias y riesgos detectados
1) Duplicidad de enum CitaEstado
   - `com.happyfeet.model.enums.CitaEstado` (simple: PROGRAMADA, COMPLETADA, CANCELADA) es la que usa la entidad Cita.
   - `com.happyfeet.model.entities.enums.CitaEstado` (avanzada con reglas de transición, emojis y métodos) convive en el proyecto pero no se usa en `Cita`. Además existen clases compiladas relacionadas en `target/classes` (Cita$CitaEstado...), sugiriendo historia de refactor.
   - Riesgo: ambigüedad de importaciones, divergencia de lógica, errores sutiles al compilar o evolucionar.
   - Recomendación: unificar en un único paquete/nombre. Preferible conservar la versión avanzada y adaptar la entidad `Cita` para usarla, o eliminar la avanzada si el alcance es simple.

2) InventarioService vs Inventario
   - `InventarioService.descontarStostck` asume lógica de dominio inexistente en `Inventario` (estaVencido, tieneStock, descontarStock).
   - Riesgo: el método no compilaría si se implementa una llamada real, y la intención de negocio queda a medias.
   - Recomendación: implementar dichos métodos en la entidad Inventario (agregar campos stock, stockMinimo, fechaVencimiento) o mover la lógica al servicio y actualizar el repositorio/DB acorde.

3) Repositorios sin implementación
   - Solo hay interfaces; no hay clases JDBC/JPA en `src`. Main usa JDBC directo solo para `especies`.
   - Riesgo: servicios no funcionales en runtime. Requiere inyectar mocks o implementar repos.
   - Recomendación: crear repos JDBC simples (p. ej., para Dueno y Mascota) o incorporar JPA/Hibernate y mapear entidades.

4) Estado del esquema vs dominio Java
   - El esquema contempla facturación, pagos, seguridad, recordatorios; el dominio Java no cubre todos esos módulos.
   - Recomendación: priorizar módulos necesarios y alinear entidad-servicio-repositorio con el esquema o viceversa (iterar el modelo).

5) Typos y consistencia
   - Método `descontarStostck` contiene un error tipográfico en el nombre (debería ser `descontarStock`).
   - Recomendación: renombrar con migración segura o sobrecargar conservando compatibilidad.

6) Empaquetado de enums y nombres
   - Enums de dominio están repartidos entre `model.enums` y `model.entities.enums`. Mantener una convención única para evitar confusión.

## 10. Recomendaciones de evolución
- Persistencia
  - Elegir estrategia: JDBC simple vs JPA/Hibernate. Para rapidez, crear repos JDBC para Dueno y Mascota con mapeos explícitos y pruebas.
- Dominio de Citas
  - Unificar `CitaEstado` y, si se requiere flujo complejo, conservar enum avanzado con métodos: puedeTransicionarA, esModificable, esFinal, siguientesEstados.
  - Agregar `Urgencia` a la entidad Cita si se quiere reflejar el schema.
- Inventario
  - Completar entidad con stock, stockMinimo, fechaVencimiento, banderas, y métodos de dominio; registrar movimientos de stock.
- Pruebas
  - Agregar pruebas unitarias para servicios (Dueno/Mascota) y de integración para DatabaseConnection y repos JDBC.
- Configuración
  - Documentar variables de entorno para credenciales; evitar logs de credenciales en consola.
- Observabilidad
  - Centralizar logs (SLF4J+Logback) en lugar de System.out/err.

## 11. Cómo ejecutar localmente
1) MySQL: crear usuario/DB y ejecutar schema+data.
2) Configurar database.properties:
```
db.url=jdbc:mysql://localhost:3306/happy_feet_veterinaria
db.username=root
db.password=tu_password
```
3) Compilar: `mvn clean package`
4) Ejecutar: `java -cp target/veterinaria-happy-feet-1.0.0.jar com.happyfeet.Main` (o desde tu IDE).

## 12. Roadmap sugerido (alto nivel)
- v1.0: Repos JDBC para Dueño y Mascota + unificación de CitaEstado + corrección InventarioService.
- v1.1: Módulo de Citas completo (servicio, repos, validaciones de solape y estado, uso de Urgencia).
- v1.2: Inventario con movimientos y alertas por stock/vencimiento.
- v1.3: Facturación y pagos básicos con pruebas.
- v1.4: Seguridad/usuarios, sesiones y recordatorios.

---
Última actualización: 2025-09-25 08:22

## 13. Diagrama de arquitectura objetivo
El siguiente diagrama muestra cómo debería quedar el proyecto terminado a nivel de arquitectura por capas, componentes y flujo principal.

```mermaid
flowchart TD
  %% Capas
  subgraph UI["Capa Presentación / API"]
    API[REST API / CLI / App Escritorio]
  end

  subgraph Servicios["Capa Servicios (Reglas de Negocio)"]
    DS[DuenoService]
    MS[MascotaService]
    CS[CitaService]
    IS[InventarioService]
    FS[FacturaService]
  end

  subgraph Repos["Capa Repositorios (Persistencia)"]
    DR[DuenoRepositoryImpl]
    MR[MascotaRepositoryImpl]
    CR[CitaRepositoryImpl]
    IR[InventarioRepositoryImpl]
    FR[FacturaRepositoryImpl]
  end

  subgraph Dominio["Capa Dominio (Entidades + Enums)"]
    Dueno[Dueno]
    Mascota[Mascota]
    Cita[Cita]
    Inventario[Inventario]
    Factura[Factura]
    ItemFactura[ItemFactura]
    Veterinario[Veterinario]
    Servicio[Servicio]
    Enums[CitaEstado • Urgencia • SexoMascota • TipoMovimiento]
  end

  subgraph Infra["Infraestructura"]
    DBConn[DatabaseConnection (JDBC)]
    SQL[(MySQL)]
  end

  %% Flujo principal
  API --> DS
  API --> MS
  API --> CS
  API --> IS
  API --> FS

  DS --> DR
  MS --> MR
  CS --> CR
  IS --> IR
  FS --> FR

  DR --> DBConn
  MR --> DBConn
  CR --> DBConn
  IR --> DBConn
  FR --> DBConn

  DBConn --> SQL

  DS --- Dominio
  MS --- Dominio
  CS --- Dominio
  IS --- Dominio
  FS --- Dominio
```

Notas:
- La capa UI puede materializarse como REST (p. ej., con Spring Boot) o una interfaz CLI/Escritorio según necesidad.
- La persistencia puede implementarse con JDBC (como está el proyecto) o migrarse a JPA/Hibernate manteniendo la misma separación por repositorios.
- El enum CitaEstado se unifica en `com.happyfeet.model.entities.enums.CitaEstado` y es el que utiliza la entidad `Cita`.
- Inventario incorpora reglas de dominio (vencimiento, stock mínimo, descuentos) y registra movimientos mediante la capa de repositorios.
