# PharmaSoft - GA7-220501096-AA2-EV01

Módulo de gestión de productos para el sistema de inventario de una droguería.

## Funcionalidades

- Registrar productos.
- Consultar todos los productos.
- Buscar un producto por código.
- Actualizar productos.
- Eliminar productos.
- Conexión a MySQL mediante JDBC.

## Tecnologías

- Java 17
- Maven
- MySQL
- JDBC
- Swing

## Requisitos

1. JDK 17 o superior.
2. Maven.
3. MySQL Server.
4. Un IDE como IntelliJ IDEA, Eclipse o NetBeans.

## Configuración de la base de datos

1. Iniciar MySQL.
2. Ejecutar el archivo `database/pharmasoft.sql`.
3. Por defecto, la aplicación usa:
   - Servidor: localhost
   - Puerto: 3306
   - Base de datos: pharmasoft
   - Usuario: root
   - Contraseña: root

Si tu instalación de MySQL utiliza otra contraseña, edita `src/main/java/com/pharmasoft/conexion/ConexionBD.java`.

## Ejecutar

Desde la carpeta del proyecto:

```bash
mvn clean compile
mvn exec:java
```

También puedes abrir el proyecto desde un IDE y ejecutar `PharmasoftApp`.

## Nota

Este proyecto corresponde al módulo CRUD de productos de PharmaSoft y sirve como base para continuar con módulos de proveedores, entradas, salidas, usuarios y reportes.
