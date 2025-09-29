# HappyFeet - Configuración para cualquier IDE

Este proyecto está configurado para funcionar en cualquier IDE sin dependencias de Maven.

## Requisitos
- Java 17 o superior
- MySQL Server (para la base de datos)

## Estructura del Proyecto
```
HappyFeet_Integrated_Carlos_clean/
├── src/
│   ├── main/java/          # Código fuente principal
│   └── test/java/          # Código de pruebas
├── lib/                    # Dependencias JAR
│   └── mysql-connector-j-8.0.33.jar
├── target/                 # Archivos compilados
│   ├── classes/           # Clases principales compiladas
│   └── test-classes/      # Clases de prueba compiladas
├── database.properties     # Configuración de BD
├── compile.bat/sh         # Scripts de compilación
└── run.bat/sh            # Scripts de ejecución
```

## Compilación y Ejecución

### En Windows:
```bash
# Compilar
compile.bat

# Ejecutar
run.bat
```

### En Linux/Mac:
```bash
# Compilar
./compile.sh

# Ejecutar
./run.sh
```

### Compilación Manual:
```bash
# Compilar código principal
javac -cp "lib/*" -d target/classes -sourcepath src/main/java src/main/java/com/happyfeet/**/*.java

# Copiar recursos
cp database.properties target/classes/

# Ejecutar
java -cp "lib/*:target/classes" com.happyfeet.HappyFeetApplication
```

## Configuración en IDEs

### IntelliJ IDEA:
1. Abrir el proyecto como directorio
2. Marcar `src/main/java` como Sources Root
3. Marcar `src/test/java` como Test Sources Root
4. Agregar `lib/mysql-connector-j-8.0.33.jar` a las librerías del proyecto
5. Configurar Project SDK a Java 17

### Eclipse:
1. Import → Existing Projects into Workspace
2. Seleccionar el directorio del proyecto
3. Right-click proyecto → Properties → Java Build Path
4. Sources tab: Agregar `src/main/java` y `src/test/java`
5. Libraries tab: Add JARs → Seleccionar `lib/mysql-connector-j-8.0.33.jar`

### VS Code:
1. Abrir la carpeta del proyecto
2. Instalar extensión "Extension Pack for Java"
3. El proyecto debería configurarse automáticamente
4. Si no, agregar lib/*.jar al classpath en `.vscode/settings.json`

### NetBeans:
1. File → Open Project
2. Seleccionar el directorio del proyecto
3. Right-click proyecto → Properties
4. Libraries → Add JAR/Folder → Seleccionar `lib/mysql-connector-j-8.0.33.jar`

## Base de Datos
Configurar MySQL y ejecutar el script `create_database.sql` antes de ejecutar la aplicación.

## Dependencias Incluidas
- MySQL Connector/J 8.0.33 (en lib/)
- Todas las dependencias necesarias están incluidas localmente