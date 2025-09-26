

Introducción y Contexto del Problema

La clínica veterinaria "Happy Feet" ha sido un pilar en su comunidad durante años, conocida por su trato compasivo y su excelente atención médica. Sin embargo, su éxito ha traído consigo un desafío abrumador: la gestión de sus operaciones. El sistema actual, una mezcla de fichas de papel, agendas manuales y hojas de cálculo, está colapsando. Los problemas diarios incluyen:

Historiales Clínicos Incompletos: Encontrar el historial de vacunas o las alergias de una mascota durante una emergencia es una carrera contra el tiempo revisando archivadores desorganizados, lo que pone en riesgo la salud del paciente.

Fugas de Inventario: No hay un control en tiempo real del stock de medicamentos. A menudo, un veterinario receta un medicamento para descubrir después que se ha agotado, forzando cambios de tratamiento de último minuto y generando desconfianza.

Agendamiento Caótico: Las citas se solapan o se registran incorrectamente, generando largos tiempos de espera y dueños frustrados que perciben una falta de organización y respeto por su tiempo.

Facturación Lenta y Propensa a Errores: Calcular manualmente los costos de una cirugía, incluyendo insumos y medicamentos, es un proceso lento que a menudo resulta en facturas incorrectas, causando pérdidas económicas y discusiones incómodas con los clientes.

"Happy Feet" necesita urgentemente un Sistema de Gestión Integral que centralice toda su operación, desde la ficha del paciente hasta la facturación, para poder seguir ofreciendo la atención de calidad que la caracteriza y para profesionalizar su gestión administrativa.

Módulos y Funcionalidades del Sistema

1. Módulo de Gestión de Pacientes (Mascotas y Dueños)

Este módulo es el núcleo del sistema, creando un registro centralizado y fiable de cada paciente y su responsable.

Registro de Mascotas: Se debe poder crear una ficha completa para cada mascota con la siguiente información:

Datos básicos: Nombre, especie, raza, fecha de nacimiento (o una edad estimada), y sexo.

Información médica: Un historial detallado que incluya alergias conocidas, condiciones preexistentes, peso actual, historial de vacunas, y procedimientos quirúrgicos previos.

Identificación única: Campos para registrar un número de microchip o tatuaje, y una URL a una foto para identificación visual.

Registro de Dueños: Se debe poder crear un perfil para cada dueño con:

Nombre completo, documento de identidad, dirección, teléfono y correo electrónico (que debe ser único).

Un campo opcional para un contacto de emergencia.

Relación Mascota-Dueño: El sistema debe garantizar que cada mascota esté asociada a un único dueño registrado y debe permitir la transferencia de propiedad de una mascota a otro dueño.

2. Módulo de Servicios Médicos y Citas

Este módulo gestiona el flujo de trabajo clínico, desde la programación de una cita hasta el registro del tratamiento.

Agenda de Citas: Un sistema para programar, consultar y gestionar el estado de las citas (ej. 'Programada', 'Finalizada', 'Cancelada'), asociando cada cita a una mascota.

Gestión de Consultas Médicas: Una interfaz para que el personal veterinario registre los detalles de cada consulta:

Fecha, hora y veterinario asignado.

Motivo de la visita, diagnóstico y recomendaciones.

Prescripción de medicamentos o procedimientos.

Registro de Procedimientos Especiales: Para cirugías o tratamientos complejos, se debe registrar información preoperatoria, el detalle del procedimiento y el seguimiento postoperatorio.

Regla de Negocio Clave: Al prescribir un medicamento o usar un insumo durante una consulta o cirugía, el sistema debe deducir automáticamente la cantidad correspondiente del inventario.

3. Módulo de Inventario y Farmacia

Este módulo controla los recursos físicos de la clínica, evitando fugas y asegurando la disponibilidad de insumos críticos.

Control de Stock: Gestión detallada de todos los insumos:

Medicamentos: Nombre, tipo, fabricante, cantidad en stock, stock mínimo para alerta, y fecha de vencimiento.

Vacunas: Tipo, lote, cantidad, fecha de vencimiento.

Material médico: Gasas, jeringas, etc., con su cantidad en stock.

Alertas Inteligentes: El sistema debe generar notificaciones o reportes automáticos para:

Productos cuyo stock esté por debajo del nivel mínimo definido.

Productos cercanos a su fecha de vencimiento.

El sistema debe restringir el uso de medicamentos o vacunas ya vencidos.

Gestión de Proveedores: Permitir el registro de proveedores para facilitar el reabastecimiento.

4. Módulo de Facturación y Reportes

Este módulo se encarga de la parte financiera y de inteligencia de negocio.

Generación de Factura en Texto Plano: Al finalizar una consulta, procedimiento o venta, el sistema debe ser capaz de generar una factura detallada en un formato de texto limpio y profesional, incluyendo datos de la clínica y del cliente, un desglose de cada servicio y producto con su valor, y un resumen con subtotal, impuestos y total a pagar.

Reportes Gerenciales: El sistema debe poder generar reportes en consola como:

Servicios más solicitados.

Desempeño del equipo veterinario.

Estado del inventario (productos a vencer, necesidad de reabastecimiento).

Análisis de facturación por período.

5. Módulo de Actividades Especiales

Este módulo añade funcionalidades que le dan un valor agregado a la clínica y a su comunidad.

Días de Adopción: Registro de mascotas disponibles para adopción y generación de un contrato simple en texto.

Jornadas de Vacunación: Una interfaz optimizada para el registro masivo y rápido de mascotas atendidas durante estas campañas.

Club de Mascotas Frecuentes: Un sistema de acumulación de puntos por cada servicio o compra, y gestión de beneficios para clientes leales.

Requerimientos Técnicos y No Funcionales

Tecnología: Uso de JDK 17 de Java y MySQL para el backend, empleando JDBC para la conexión a la base de datos.

Arquitectura y Diseño:

Implementación estricta del modelo MVC (Modelo-Vista-Controlador).

Aplicación obligatoria de los principios de diseño SOLID.

Uso de al menos 5 patrones de diseño según la necesidad del problema.

Programación Funcional: Uso de expresiones lambda y la API Stream para operaciones eficientes sobre colecciones.

Manejo de Errores: Implementación de un manejo adecuado de errores y excepciones, generando logs en un archivo de texto en la ruta principal del proyecto.

Anexo A: Modelo de Base de Datos (Script SQL)

Para garantizar la integridad, consistencia y escalabilidad de los datos, el esquema de la base de datos ha sido completamente normalizado.

La entrega final del proyecto debe ser un producto de software profesional, bien documentado y organizado. A continuación, se detallan los componentes y estándares obligatorios. La entrega final consiste en la URL del repositorio privado de GitHub y el hash del último commit realizado antes de la fecha y hora límite.



1. Estructura del Proyecto

El proyecto debe seguir una estructura de paquetes lógica y ordenada que refleje la arquitectura MVC. El objetivo es tener un código desacoplado y fácil de navegar.



Ejemplo de Estructura General:

/HappyFeet_Veterinaria_ApellidoNombre |-- /database |   |-- schema.sql |   |-- data.sql |-- /src |   |-- /main/java/com/happyfeet |       |-- /controller        // Controladores que manejan el flujo de la aplicación. |       |-- /model |       |   |-- /entities      // Clases que mapean las tablas de la BD (Mascota, Dueno). |       |   |-- /enums         // Enumeraciones si se utilizan. |       |-- /repository        // O 'dao', para las clases de acceso a datos (JDBC). |       |-- /service           // Lógica de negocio (ej. calcular factura, validar stock). |       |-- /view              // Clases responsables de la interfaz de consola. |       |-- /util              // Clases de utilidad (ej. formateadores de fecha). |       |-- Main.java          // Punto de entrada de la aplicación. |-- .gitignore |-- pom.xml |-- README.md

2. Gestión de Versiones y Buenas Prácticas con Git

La gestión del repositorio es un reflejo de la disciplina y profesionalismo del desarrollador.

Archivo .gitignore: El repositorio debe incluir un archivo .gitignore bien configurado desde el inicio. Este archivo es crucial para evitar que se suban al control de versiones archivos innecesarios que son generados por el entorno de desarrollo o el sistema de compilación. Debe excluir, como mínimo:

La carpeta de compilación de Maven (/target/).

Archivos de clase de Java (*.class).

Archivos de configuración del IDE (ej. .idea/, .vscode/, *.iml).

Archivos de log generados por la aplicación (*.log).

Flujo de Trabajo (Git Flow): Se debe seguir la metodología Git Flow para la gestión de ramas. Esto implica:

La rama main debe contener únicamente versiones estables y de producción.

La rama develop es la rama de integración principal donde se fusionan las nuevas funcionalidades.

Cada nueva funcionalidad (ej. "Módulo de Inventario", "Módulo de Facturación") debe desarrollarse en su propia rama feature, partiendo de develop. Por ejemplo: feature/inventory-module.

Confirmaciones (Conventional Commits): Los mensajes de commit deben seguir la especificación de Conventional Commits. Esto aporta claridad al historial del proyecto. El formato es <tipo>: <descripción>.

feat: para una nueva funcionalidad.

fix: para la corrección de un error.

docs: para cambios en la documentación.

style: para cambios de formato que no afectan la lógica.

refactor: para cambios en el código que no corrigen un error ni añaden una funcionalidad.

test: para añadir o corregir pruebas.

3. Documentación (Archivo README.md)

El archivo README.md es el manual de usuario y la tarjeta de presentación del proyecto. Debe ser claro, completo y profesional, conteniendo las siguientes secciones:

Título del Proyecto: Nombre completo del sistema.

Descripción del Contexto: Un resumen del problema que el sistema resuelve para la veterinaria "Happy Feet".

Tecnologías Utilizadas: Listado de las tecnologías principales (Java 17, MySQL, JDBC, Maven, etc.).

Funcionalidades Implementadas: Un listado detallado de lo que el sistema puede hacer, explicando los diferentes módulos y las reglas de negocio más importantes.

Modelo de la Base de Datos: Una breve explicación de las tablas y sus relaciones. Se recomienda incluir una imagen del diagrama Entidad-Relación.

Instrucciones de Instalación y Ejecución: Una guía paso a paso para que un tercero pueda ejecutar el proyecto sin problemas:

Requisitos Previos: JDK, Maven, MySQL Server.

Clonación: Comando git clone.

Configuración de la Base de Datos: Explicar cómo configurar la conexión (usuario, contraseña, URL de la BD), idealmente en un archivo de propiedades.

Ejecución de Scripts: Indicar el orden para ejecutar los scripts schema.sql y data.sql.

Ejecución del Proyecto: Comando para compilar y ejecutar la aplicación (ej. mvn exec:java).

Guía de Uso: Breve explicación de cómo navegar por los menús principales.

Autor(es): Nombre del o los estudiantes.

4. Scripts de Base de Datos

Se deben entregar dos archivos SQL en la carpeta /database:

schema.sql: Contiene todas las sentencias CREATE TABLE para construir la estructura completa de la base de datos desde cero.

data.sql: Contiene las sentencias INSERT INTO para poblar la base de datos con la cantidad y calidad de datos solicitada.

estos son mi requirimientos quiero que me ayudes a terminar el proyecto 100% funcoonal y que cumpla con cdad uno de los requerimiento ya mencionados# HappyFeet Veterinaria

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


## 10. Mejoras que presenta el proyecto (estado actual)

A continuación, un resumen práctico de las principales mejoras y fortalezas que hoy presenta el proyecto, pensando en su ejecución por consola, mantenibilidad y futura evolución:

- Arquitectura por capas clara y coherente:
  - Util, Model (entidades), Repository (contratos e implementaciones JDBC), Service (negocio/validaciones), Controller, View. Esto facilita aislar responsabilidades y probar por capas.
- Modelo de dominio expresivo:
  - Entidades con Builders que refuerzan invariantes (Dueno, Mascota, Cita, etc.).
  - Equals/hashCode y utilidades (por ejemplo, Dueno divide nombre/apellido desde nombreCompleto).
- Enums ricos (State/Strategy):
  - CitaEstado en com.happyfeet.model.entities.enums implementa reglas de transición, estados finales/modificables, validaciones y helpers (porId, getEstadoInicial…).
- Servicios con reglas de negocio y validación consistente:
  - DuenoServiceImpl y MascotaServiceImpl validan requeridos, unicidades y aplican merge seguro de cambios.
  - CitaServiceImpl centraliza el flujo de estados (programar, confirmar, iniciar, finalizar, cancelar), reprogramación y verificación de solapes.
- Repositorios JDBC listos para uso real:
  - DuenoRepositoryImpl, MascotaRepositoryImpl y CitaRepositoryImpl con mapeo cuidadoso (null-safe), consultas específicas y uso de PreparedStatement.
- Conexión a BD centralizada y configurable:
  - DatabaseConnection singleton y archivo database.properties en raíz del proyecto.
- Ejecución por consola disponible:
  - MainController con menú interactivo (MenuPrincipal) para Dueños, Mascotas y Citas.
- Pruebas unitarias útiles sin BD:
  - CitaServiceImplTest con repositorio en memoria, cubre creación, flujo de estados, solape de horarios y listados por fecha/estado.
- Esquema SQL robusto y datos semilla:
  - database/schema.sql y database/data.sql listos para levantar el entorno.
- Compatibilidad y pequeños refinamientos recientes:
  - Unificación del uso de CitaEstado en el paquete entities.enums; controladores ajustados para evitar conflictos de tipos.
  - Defaults razonables (por ejemplo, razaId=1 y estado inicial PROGRAMADA).
  - Correcciones de nombres y contratos en servicios (crearDueno, actualizarDueno, buscarPorDueno). 
  - InventarioService mantiene compatibilidad retro (descontarStostck) y expone API con nombre correcto (descontarStock).

### Próximas mejoras recomendadas (priorizadas)

1) Persistencia completa y consistencia de dominio de Inventario:
   - Completar métodos de dominio (estaVencido, tieneStock, descontarStock) y asegurar su reflejo en la entidad y BD.
   - Ampliar repositorio/servicio de Inventario y cubrirlo con pruebas.
2) Transacciones y pool de conexiones:
   - Incorporar HikariCP y manejo transaccional donde corresponda (p. ej., operaciones compuestas).
3) Logging estructurado:
   - Agregar SLF4J + Logback con niveles, trazas para repos/servicios y mensajes de auditoría mínimos.
4) Tests adicionales y de integración:
   - Pruebas unitarias para DuenoService y MascotaService (mock repos o in-memory) y tests de integración con una BD temporal.
5) Endurecer validaciones referenciales en servicios:
   - Verificar existencia de veterinario/mascota/servicio al crear/reprogramar una cita (consultas previas o constraints manejadas).
6) CI/CD y calidad:
   - GitHub Actions para build y tests; perfiles Maven (dev/test/prod) y checks de estilo (Spotless/Checkstyle).
7) Observabilidad en consola:
   - Mensajes de error más amigables y centralización de manejo de excepciones en la capa Controller/View.
8) Documentación operativa:
   - Pasos de despliegue, troubleshooting y escenarios de ejemplo desde el menú por consola.



# 11. Cumplimiento de Requerimientos (Resumen)

Este apartado verifica, a alto nivel, el cumplimiento de los requerimientos iniciales provistos para el sistema "Happy Feet".

- Módulo de Gestión de Pacientes (Mascotas y Dueños): Parcial
  - Registro de Dueños: Implementado vía consola (crear, actualizar, eliminar, listar, buscar) con validaciones en servicio y persistencia JDBC.
  - Registro de Mascotas: Implementado vía consola (crear, actualizar, eliminar, listar, buscar) y persistencia JDBC.
  - Información clínica de mascota (alergias, peso, condiciones, microchip, foto): Soportada en la entidad; ingreso completo por consola pendiente (Parcial).
  - Relación Mascota-Dueño: Implementada (FK en BD y campo duenoId). Transferencia de propiedad: pendiente interfaz específica.

- Módulo de Servicios Médicos y Citas: Parcial/Casi completo
  - Agenda de Citas: Implementada (crear, listar por fecha/estado, reprogramar, confirmar, iniciar, finalizar, cancelar) con reglas de estado (State) y verificación de solapes.
  - Gestión de Consultas Médicas y Procedimientos Especiales: Estructura en BD (historial_medico, evento_tipos) presente; interfaz de registro en consola no implementada aún.
  - Regla de negocio clave (descontar inventario al prescribir/usar): Métodos de dominio en Inventario (estaVencido, tieneStock, descontarStock) y servicio listos; integración automática con consultas/cirugías pendiente (Parcial).

- Módulo de Inventario y Farmacia: Parcial
  - Control de stock, stock mínimo, vencimiento: Soportado en entidad y en BD; repositorio y lógica de alerta básica presentes (observer), interfaz de consola pendiente.
  - Alertas inteligentes (bajo stock, próximos a vencer, bloqueo si vencido): Lógica base disponible; interfaz/reportes de alerta pendientes.
  - Gestión de proveedores: Soportada en BD; interfaz/repositorio de proveedor pendiente.

- Módulo de Facturación y Reportes: Parcial
  - Facturación: Entidades y servicios base presentes (Factura, ItemFactura, FacturaService); generación en texto plano posible a partir de los datos (composición de líneas) pero falta integrar al flujo de consulta desde la consola.
  - Reportes en consola: Servicio de reporte existente a nivel de código; menús dedicados pendientes.

- Módulo de Actividades Especiales: Pendiente
  - Días de adopción, jornadas de vacunación masivas, club de puntos: Estructura parcial en BD (puntos_cliente); lógica e interfaz aún no implementadas.

- Requerimientos Técnicos y No Funcionales: Mayormente Cumplidos
  - Tecnología: Java 17 (POM actualizado), MySQL y JDBC (DatabaseConnection, repos JDBC). Cumple.
  - Arquitectura MVC y SOLID: Capas separadas (model/repository/service/controller/view) y reglas de negocio en servicios. Cumple.
  - Patrones de diseño (≥5): Singleton (DatabaseConnection), Builder (entidades), State/Strategy (CitaEstado), Composite (Factura/Items), Repository; además Observer para alertas de inventario. Cumple.
  - Programación funcional (Streams/Lambdas): Usada en servicios para filtrados y ordenamiento. Cumple.
  - Manejo de errores y logs: Servicios lanzan excepciones de dominio y ahora se generan logs en archivo happyfeet.log mediante util FileLogger. Cumple.
  - Git y buenas prácticas: Se agregó .gitignore con exclusiones estándar. Git Flow y Conventional Commits documentados y recomendados para el trabajo en equipo.


# 12. Instrucciones de Instalación y Ejecución (actualizado)

Requisitos previos:
- JDK 17
- Maven 3.8+
- MySQL Server 8.x (con un usuario con permisos de creación de BD)

Pasos:
1) Crear la base de datos y datos semilla:
   - Ejecuta primero `database/schema.sql` y luego `database/data.sql` en tu MySQL.
2) Configurar conexión:
   - Edita `database.properties` en la raíz con tus credenciales y URL (por defecto: jdbc:mysql://localhost:3306/happy_feet_veterinaria).
3) Compilar (puedes omitir tests si lo deseas):
   - `mvn -DskipTests package`
4) Ejecutar por consola el menú principal:
   - Opción A (IDE): Ejecuta la clase `com.happyfeet.controller.MainController` (tiene método `main`).
   - Opción B (CLI): `mvn exec:java -Dexec.mainClass=com.happyfeet.controller.MainController`
5) Logs:
   - Se generan en el archivo `happyfeet.log` en el directorio de trabajo del proyecto.

Guía de uso (resumen):
- Menú Principal → Dueños: CRUD completo y búsqueda por término.
- Menú Principal → Mascotas: CRUD y búsquedas por dueño/nombre.
- Menú Principal → Citas: Agendar, listar por fecha/estado, reprogramar, confirmar, iniciar, finalizar, cancelar y validar solape.

Autores:
- Carlos Cisneros (ajustar si corresponde)

Notas:
- Para la integración completa de Inventario con Consultas/Facturación y módulos especiales, ver la sección "Próximas mejoras recomendadas" y el checklist anterior.



Introducción y Contexto del Problema

La clínica veterinaria "Happy Feet" ha sido un pilar en su comunidad durante años, conocida por su trato compasivo y su excelente atención médica. Sin embargo, su éxito ha traído consigo un desafío abrumador: la gestión de sus operaciones. El sistema actual, una mezcla de fichas de papel, agendas manuales y hojas de cálculo, está colapsando. Los problemas diarios incluyen:

Historiales Clínicos Incompletos: Encontrar el historial de vacunas o las alergias de una mascota durante una emergencia es una carrera contra el tiempo revisando archivadores desorganizados, lo que pone en riesgo la salud del paciente.

Fugas de Inventario: No hay un control en tiempo real del stock de medicamentos. A menudo, un veterinario receta un medicamento para descubrir después que se ha agotado, forzando cambios de tratamiento de último minuto y generando desconfianza.

Agendamiento Caótico: Las citas se solapan o se registran incorrectamente, generando largos tiempos de espera y dueños frustrados que perciben una falta de organización y respeto por su tiempo.

Facturación Lenta y Propensa a Errores: Calcular manualmente los costos de una cirugía, incluyendo insumos y medicamentos, es un proceso lento que a menudo resulta en facturas incorrectas, causando pérdidas económicas y discusiones incómodas con los clientes.

"Happy Feet" necesita urgentemente un Sistema de Gestión Integral que centralice toda su operación, desde la ficha del paciente hasta la facturación, para poder seguir ofreciendo la atención de calidad que la caracteriza y para profesionalizar su gestión administrativa.

Módulos y Funcionalidades del Sistema

1. Módulo de Gestión de Pacientes (Mascotas y Dueños)

Este módulo es el núcleo del sistema, creando un registro centralizado y fiable de cada paciente y su responsable.

Registro de Mascotas: Se debe poder crear una ficha completa para cada mascota con la siguiente información:

Datos básicos: Nombre, especie, raza, fecha de nacimiento (o una edad estimada), y sexo.

Información médica: Un historial detallado que incluya alergias conocidas, condiciones preexistentes, peso actual, historial de vacunas, y procedimientos quirúrgicos previos.

Identificación única: Campos para registrar un número de microchip o tatuaje, y una URL a una foto para identificación visual.

Registro de Dueños: Se debe poder crear un perfil para cada dueño con:

Nombre completo, documento de identidad, dirección, teléfono y correo electrónico (que debe ser único).

Un campo opcional para un contacto de emergencia.

Relación Mascota-Dueño: El sistema debe garantizar que cada mascota esté asociada a un único dueño registrado y debe permitir la transferencia de propiedad de una mascota a otro dueño.

2. Módulo de Servicios Médicos y Citas

Este módulo gestiona el flujo de trabajo clínico, desde la programación de una cita hasta el registro del tratamiento.

Agenda de Citas: Un sistema para programar, consultar y gestionar el estado de las citas (ej. 'Programada', 'Finalizada', 'Cancelada'), asociando cada cita a una mascota.

Gestión de Consultas Médicas: Una interfaz para que el personal veterinario registre los detalles de cada consulta:

Fecha, hora y veterinario asignado.

Motivo de la visita, diagnóstico y recomendaciones.

Prescripción de medicamentos o procedimientos.

Registro de Procedimientos Especiales: Para cirugías o tratamientos complejos, se debe registrar información preoperatoria, el detalle del procedimiento y el seguimiento postoperatorio.

Regla de Negocio Clave: Al prescribir un medicamento o usar un insumo durante una consulta o cirugía, el sistema debe deducir automáticamente la cantidad correspondiente del inventario.

3. Módulo de Inventario y Farmacia

Este módulo controla los recursos físicos de la clínica, evitando fugas y asegurando la disponibilidad de insumos críticos.

Control de Stock: Gestión detallada de todos los insumos:

Medicamentos: Nombre, tipo, fabricante, cantidad en stock, stock mínimo para alerta, y fecha de vencimiento.

Vacunas: Tipo, lote, cantidad, fecha de vencimiento.

Material médico: Gasas, jeringas, etc., con su cantidad en stock.

Alertas Inteligentes: El sistema debe generar notificaciones o reportes automáticos para:

Productos cuyo stock esté por debajo del nivel mínimo definido.

Productos cercanos a su fecha de vencimiento.

El sistema debe restringir el uso de medicamentos o vacunas ya vencidos.

Gestión de Proveedores: Permitir el registro de proveedores para facilitar el reabastecimiento.

4. Módulo de Facturación y Reportes

Este módulo se encarga de la parte financiera y de inteligencia de negocio.

Generación de Factura en Texto Plano: Al finalizar una consulta, procedimiento o venta, el sistema debe ser capaz de generar una factura detallada en un formato de texto limpio y profesional, incluyendo datos de la clínica y del cliente, un desglose de cada servicio y producto con su valor, y un resumen con subtotal, impuestos y total a pagar.

Reportes Gerenciales: El sistema debe poder generar reportes en consola como:

Servicios más solicitados.

Desempeño del equipo veterinario.

Estado del inventario (productos a vencer, necesidad de reabastecimiento).

Análisis de facturación por período.

5. Módulo de Actividades Especiales

Este módulo añade funcionalidades que le dan un valor agregado a la clínica y a su comunidad.

Días de Adopción: Registro de mascotas disponibles para adopción y generación de un contrato simple en texto.

Jornadas de Vacunación: Una interfaz optimizada para el registro masivo y rápido de mascotas atendidas durante estas campañas.

Club de Mascotas Frecuentes: Un sistema de acumulación de puntos por cada servicio o compra, y gestión de beneficios para clientes leales.

Requerimientos Técnicos y No Funcionales

Tecnología: Uso de JDK 17 de Java y MySQL para el backend, empleando JDBC para la conexión a la base de datos.

Arquitectura y Diseño:

Implementación estricta del modelo MVC (Modelo-Vista-Controlador).

Aplicación obligatoria de los principios de diseño SOLID.

Uso de al menos 5 patrones de diseño según la necesidad del problema.

Programación Funcional: Uso de expresiones lambda y la API Stream para operaciones eficientes sobre colecciones.

Manejo de Errores: Implementación de un manejo adecuado de errores y excepciones, generando logs en un archivo de texto en la ruta principal del proyecto.

Anexo A: Modelo de Base de Datos (Script SQL)

Para garantizar la integridad, consistencia y escalabilidad de los datos, el esquema de la base de datos ha sido completamente normalizado.

La entrega final del proyecto debe ser un producto de software profesional, bien documentado y organizado. A continuación, se detallan los componentes y estándares obligatorios. La entrega final consiste en la URL del repositorio privado de GitHub y el hash del último commit realizado antes de la fecha y hora límite.



1. Estructura del Proyecto

El proyecto debe seguir una estructura de paquetes lógica y ordenada que refleje la arquitectura MVC. El objetivo es tener un código desacoplado y fácil de navegar.



Ejemplo de Estructura General:

/HappyFeet_Veterinaria_ApellidoNombre |-- /database |   |-- schema.sql |   |-- data.sql |-- /src |   |-- /main/java/com/happyfeet |       |-- /controller        // Controladores que manejan el flujo de la aplicación. |       |-- /model |       |   |-- /entities      // Clases que mapean las tablas de la BD (Mascota, Dueno). |       |   |-- /enums         // Enumeraciones si se utilizan. |       |-- /repository        // O 'dao', para las clases de acceso a datos (JDBC). |       |-- /service           // Lógica de negocio (ej. calcular factura, validar stock). |       |-- /view              // Clases responsables de la interfaz de consola. |       |-- /util              // Clases de utilidad (ej. formateadores de fecha). |       |-- Main.java          // Punto de entrada de la aplicación. |-- .gitignore |-- pom.xml |-- README.md 

2. Gestión de Versiones y Buenas Prácticas con Git

La gestión del repositorio es un reflejo de la disciplina y profesionalismo del desarrollador.

Archivo .gitignore: El repositorio debe incluir un archivo .gitignore bien configurado desde el inicio. Este archivo es crucial para evitar que se suban al control de versiones archivos innecesarios que son generados por el entorno de desarrollo o el sistema de compilación. Debe excluir, como mínimo:

La carpeta de compilación de Maven (/target/).

Archivos de clase de Java (*.class).

Archivos de configuración del IDE (ej. .idea/, .vscode/, *.iml).

Archivos de log generados por la aplicación (*.log).

Flujo de Trabajo (Git Flow): Se debe seguir la metodología Git Flow para la gestión de ramas. Esto implica:

La rama main debe contener únicamente versiones estables y de producción.

La rama develop es la rama de integración principal donde se fusionan las nuevas funcionalidades.

Cada nueva funcionalidad (ej. "Módulo de Inventario", "Módulo de Facturación") debe desarrollarse en su propia rama feature, partiendo de develop. Por ejemplo: feature/inventory-module.

Confirmaciones (Conventional Commits): Los mensajes de commit deben seguir la especificación de Conventional Commits. Esto aporta claridad al historial del proyecto. El formato es <tipo>: <descripción>.

feat: para una nueva funcionalidad.

fix: para la corrección de un error.

docs: para cambios en la documentación.

style: para cambios de formato que no afectan la lógica.

refactor: para cambios en el código que no corrigen un error ni añaden una funcionalidad.

test: para añadir o corregir pruebas.

3. Documentación (Archivo README.md)

El archivo README.md es el manual de usuario y la tarjeta de presentación del proyecto. Debe ser claro, completo y profesional, conteniendo las siguientes secciones:

Título del Proyecto: Nombre completo del sistema.

Descripción del Contexto: Un resumen del problema que el sistema resuelve para la veterinaria "Happy Feet".

Tecnologías Utilizadas: Listado de las tecnologías principales (Java 17, MySQL, JDBC, Maven, etc.).

Funcionalidades Implementadas: Un listado detallado de lo que el sistema puede hacer, explicando los diferentes módulos y las reglas de negocio más importantes.

Modelo de la Base de Datos: Una breve explicación de las tablas y sus relaciones. Se recomienda incluir una imagen del diagrama Entidad-Relación.

Instrucciones de Instalación y Ejecución: Una guía paso a paso para que un tercero pueda ejecutar el proyecto sin problemas:

Requisitos Previos: JDK, Maven, MySQL Server.

Clonación: Comando git clone.

Configuración de la Base de Datos: Explicar cómo configurar la conexión (usuario, contraseña, URL de la BD), idealmente en un archivo de propiedades.

Ejecución de Scripts: Indicar el orden para ejecutar los scripts schema.sql y data.sql.

Ejecución del Proyecto: Comando para compilar y ejecutar la aplicación (ej. mvn exec:java).

Guía de Uso: Breve explicación de cómo navegar por los menús principales.

Autor(es): Nombre del o los estudiantes.

4. Scripts de Base de Datos

Se deben entregar dos archivos SQL en la carpeta /database:

schema.sql: Contiene todas las sentencias CREATE TABLE para construir la estructura completa de la base de datos desde cero.

data.sql: Contiene las sentencias INSERT INTO para poblar la base de datos con la cantidad y calidad de datos solicitada.

  estos son mi requirimientos quiero que me ayudes a terminar el proyecto 100% funcoonal y que cumpla con cdad uno de los requerimiento ya mencionados

La entrega final del proyecto debe ser un producto de software profesional, bien documentado y organizado. A continuación, se detallan los componentes y estándares obligatorios. La entrega final consiste en la **URL del repositorio privado de GitHub** y el **hash del último commit** realizado antes de la fecha y hora límite.



#### **1. Estructura del Proyecto**

El proyecto debe seguir una estructura de paquetes lógica y ordenada que refleje la arquitectura MVC. El objetivo es tener un código desacoplado y fácil de navegar.



**Ejemplo de Estructura General:**

```
/HappyFeet_Veterinaria_ApellidoNombre
|-- /database
|   |-- schema.sql
|   |-- data.sql
|-- /src
|   |-- /main/java/com/happyfeet
|       |-- /controller        // Controladores que manejan el flujo de la aplicación.
|       |-- /model
|       |   |-- /entities      // Clases que mapean las tablas de la BD (Mascota, Dueno).
|       |   |-- /enums         // Enumeraciones si se utilizan.
|       |-- /repository        // O 'dao', para las clases de acceso a datos (JDBC).
|       |-- /service           // Lógica de negocio (ej. calcular factura, validar stock).
|       |-- /view              // Clases responsables de la interfaz de consola.
|       |-- /util              // Clases de utilidad (ej. formateadores de fecha).
|       |-- Main.java          // Punto de entrada de la aplicación.
|-- .gitignore
|-- pom.xml
|-- README.md
```

#### **2. Gestión de Versiones y Buenas Prácticas con Git**

La gestión del repositorio es un reflejo de la disciplina y profesionalismo del desarrollador.

- **Archivo** `**.gitignore**`**:** El repositorio debe incluir un archivo `.gitignore` bien configurado desde el inicio. Este archivo es **crucial** para evitar que se suban al control de versiones archivos innecesarios que son generados por el entorno de desarrollo o el sistema de compilación. Debe excluir, como mínimo:
- La carpeta de compilación de Maven (`/target/`).
- Archivos de clase de Java (`*.class`).
- Archivos de configuración del IDE (ej. `.idea/`, `.vscode/`, `*.iml`).
- Archivos de log generados por la aplicación (`*.log`).
- **Flujo de Trabajo (Git Flow):** Se debe seguir la metodología Git Flow para la gestión de ramas. Esto implica:
- La rama `main` debe contener únicamente versiones estables y de producción.
- La rama `develop` es la rama de integración principal donde se fusionan las nuevas funcionalidades.
- Cada nueva funcionalidad (ej. "Módulo de Inventario", "Módulo de Facturación") debe desarrollarse en su propia rama `feature`, partiendo de `develop`. Por ejemplo: `feature/inventory-module`.
- **Confirmaciones (Conventional Commits):** Los mensajes de commit deben seguir la especificación de **Conventional Commits**. Esto aporta claridad al historial del proyecto. El formato es `<tipo>: <descripción>`.
- `**feat**`**:** para una nueva funcionalidad.
- `**fix**`**:** para la corrección de un error.
- `**docs**`**:** para cambios en la documentación.
- `**style**`**:** para cambios de formato que no afectan la lógica.
- `**refactor**`**:** para cambios en el código que no corrigen un error ni añaden una funcionalidad.
- `**test**`**:** para añadir o corregir pruebas.

####  

#### **3. Documentación (Archivo** `**README.md**`**)**

El archivo `README.md` es el manual de usuario y la tarjeta de presentación del proyecto. Debe ser claro, completo y profesional, conteniendo las siguientes secciones:

1. **Título del Proyecto:** Nombre completo del sistema.
2. **Descripción del Contexto:** Un resumen del problema que el sistema resuelve para la veterinaria "Happy Feet".
3. **Tecnologías Utilizadas:** Listado de las tecnologías principales (Java 17, MySQL, JDBC, Maven, etc.).
4. **Funcionalidades Implementadas:** Un listado detallado de lo que el sistema puede hacer, explicando los diferentes módulos y las reglas de negocio más importantes.
5. **Modelo de la Base de Datos:** Una breve explicación de las tablas y sus relaciones. Se recomienda incluir una imagen del diagrama Entidad-Relación.
6. **Instrucciones de Instalación y Ejecución:** Una guía paso a paso para que un tercero pueda ejecutar el proyecto sin problemas:

- **Requisitos Previos:** JDK, Maven, MySQL Server.
- **Clonación:** Comando `git clone`.
- **Configuración de la Base de Datos:** Explicar cómo configurar la conexión (usuario, contraseña, URL de la BD), idealmente en un archivo de propiedades.
- **Ejecución de Scripts:** Indicar el orden para ejecutar los scripts `schema.sql` y `data.sql`.
- **Ejecución del Proyecto:** Comando para compilar y ejecutar la aplicación (ej. `mvn exec:java`).

1. **Guía de Uso:** Breve explicación de cómo navegar por los menús principales.
2. **Autor(es):** Nombre del o los estudiantes.

####  

#### **4. Scripts de Base de Datos**

Se deben entregar dos archivos SQL en la carpeta `/database`:

- `**schema.sql**`**:** Contiene todas las sentencias `CREATE TABLE` para construir la estructura completa de la base de datos desde cero.

- `**data.sql**`**:** Contiene las sentencias `INSERT INTO` para poblar la base de datos con la cantidad y calidad de datos solicitada.

  
