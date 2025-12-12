# MiniInventario - Gestión de Productos

Aplicación web desarrollada en Java para la gestión de un inventario básico de productos, implementando el patrón de diseño MVC, PRG (Post/Redirect/Get) y persistencia de datos con Hibernate sobre Oracle Database.

## 📋 Descripción

El objetivo de este proyecto es gestionar de forma sencilla un inventario. La aplicación permite registrar productos indicando nombre, categoría, precio y stock.

El sistema controla automáticamente el estado del producto:
* Si el stock inicial es 0, el producto se marca automáticamente como 'agotado'.
* Permite alternar manualmente el estado de agotado.

## 🚀 Tecnologías Utilizadas

El proyecto sigue una arquitectura **MVC tradicional**pero ha sido modernizado en la capa de datos:

* **Lenguaje:** Java (JDK 17/21 recomendado).
* **Web:** Jakarta EE (Servlets, JSP).
* **Vistas:** JSP con JSTL para evitar scriptlets.
* **Persistencia (ORM):** **Hibernate** (sustituyendo a JDBC puro).
* **Base de Datos:** **Oracle Database**.
* **Construcción:** Maven.
* **Servidor de Aplicaciones:** Payara / GlassFish.

## ⚙️ Funcionalidades

### 1. Listado de Productos
* Visualización de ID, nombre, categoría, precio, stock y estado.
* **Filtros:** Por categoría (select dinámico) y productos agotados (checkbox).
* Los filtros se mantienen activos tras realizar acciones sobre los productos.

### 2. Acciones Rápidas (Patrón PRG)
Desde el listado se pueden realizar operaciones directas mediante formularios POST:
* **Stock:** Aumentar (+1) o disminuir (-1) unidades.
* **Estado:** Alternar entre Agotado/Disponible (Toggle).
* **Eliminar:** Borrar productos (solo si no tienen existencias).
* **Mensajes Flash:** Feedback visual de éxito o error tras cada acción.

### 3. Alta de Productos
Formulario validado que requiere:
* Nombre (Obligatorio).
* Categoría (Obligatoria).
* Precio (> 0).
* Stock (>= 0).

## 🗄️ Modelo de Datos (Oracle)

La persistencia se gestiona mediante la entidad `Producto` mapeada con Hibernate hacia la tabla:

| Campo | Tipo SQL (Oracle) | Descripción |
| :--- | :--- | :--- |
| **id** | `NUMBER` (PK) | [cite_start]Identificador autonumérico (Sequence/Identity) [cite: 39] |
| **nombre** | `VARCHAR2(100)` | [cite_start]Nombre del producto [cite: 39] |
| **categoria** | `VARCHAR2(50)` | [cite_start]Categoría del producto [cite: 39] |
| **precio** | `NUMBER(10,2)` | [cite_start]Precio en euros [cite: 39] |
| **stock** | `NUMBER` | [cite_start]Unidades disponibles [cite: 39] |
| **agotado** | `CHAR(1)` | [cite_start]'S' (Sí) o 'N' (No) [cite: 39] |

## 🛠️ Configuración e Instalación

1.  **Clonar el repositorio:**
    ```bash
    git clone [https://github.com/TU_USUARIO/mini-inventario.git](https://github.com/xni0/mini-inventario.git)
    ```
2.  **Base de Datos:**
    * Asegúrate de tener una instancia de Oracle corriendo.
    * Configura las credenciales en el archivo `persistence.xml`.
3.  **Ejecución:**
    * Construir el proyecto: `mvn clean install`.
    * Desplegar el archivo `.war` generado en tu servidor Payara o GlassFish.

## 🏗️ Estructura del Proyecto

El código está organizado por paquetes, separando la lógica original (JDBC) de la nueva implementación con Hibernate (JPA):

```text
src/main/java/
├── conexion/       # Gestión de conexión manual (Legacy JDBC)
├── controller/     # Controlador principal (Servlet ProductoController)
├── dao/            # Interfaces y DAO base
├── daoJPA/         # Implementaciones del patrón DAO usando Hibernate/JPA
├── model/          # Modelo de datos original (POJOs)
├── modeloJPA/      # Entidades persistentes anotadas (@Entity)
├── testJPA/        # Clases de prueba para verificar la persistencia y relaciones
└── utils/          # JPAUtil (Gestión del EntityManagerFactory)
```
### ¿Qué ha cambiado con la migración a Hibernate?

1.  **El DAO:** En lugar de usar `Connection`, `PreparedStatement` y `ResultSet`, ahora el DAO usará `Session` o `EntityManager` y JPQL.
2.  **Modelo:** La clase `Producto.java` ahora tendrá anotaciones como `@Entity`, `@Table(name="PRODUCTO")`, `@Id`, etc.
3.  **Conexión:** En vez de la clase `Conexion.java` manual, se usará un archivo de configuración (`persistence.xml`) y una clase `JPAUtilUtil`.

--------
## Autor 👨‍💻

Proyecto realizado para el módulo de DWES (Desarrollo Web en Entorno Servidor).
