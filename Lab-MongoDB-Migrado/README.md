# Laboratorio MongoDB - Proyecto migrado

## 1. Estado y decisión técnica

Esta variante conserva Java, Maven, Jetty embebido y Jersey/JAX-RS, pero utiliza el **driver oficial MongoDB Java Sync** para conectarse a MongoDB 9.0.4.

**Importante para la evaluación:** el PDF solicita explícitamente JPA/EclipseLink NoSQL. Esta versión prioriza una integración funcional y comprobada con MongoDB 9.0.4 y, por ello, no implementa JPA. Antes de entregar, confirma con el profesor si acepta el driver oficial en lugar de EclipseLink NoSQL. No presentes esta variante como una implementación JPA.

El driver 5.13.0 pertenece a la línea moderna del controlador Java de MongoDB y se utiliza en este proyecto para establecer la comunicación directa con el servidor.

## 2. Antecedentes y desarrollo inicial

Antes de realizar la migración, se desarrolló un primer proyecto denominado `Laboratorio7MongoDB`, utilizando Maven y una arquitectura basada en JPA y EclipseLink NoSQL, siguiendo el enfoque tecnológico propuesto en la guía académica.

El proyecto inicial utilizaba las siguientes dependencias:

| Componente | Versión o tecnología |
|---|---|
| Lenguaje | Java |
| JDK | 26 |
| Gestión de dependencias | Maven |
| Proveedor JPA | EclipseLink 4.0.9 |
| Extensión NoSQL | EclipseLink NoSQL 4.0.9 |
| Controlador MongoDB | MongoDB Driver Legacy 5.13.0 |
| API de persistencia | Jakarta Persistence 3.1 |
| Base de datos | MongoDB |

### 2.1. Configuración inicial de persistencia

Se creó una unidad de persistencia denominada `MongoPU` en el archivo `persistence.xml`.

La configuración contemplaba:

- `org.eclipse.persistence.jpa.PersistenceProvider` como proveedor de persistencia.
- `MongoPlatform` como plataforma de base de datos.
- `MongoConnectionSpec` como especificación de conexión.
- Host local: `localhost`.
- Puerto: `27017`.
- Base de datos: `ArquiSW`.

El propósito era gestionar las operaciones de persistencia mediante JPA y utilizar EclipseLink NoSQL como mecanismo de integración con MongoDB.

### 2.2. Modelo de entidades inicial

También se desarrollaron las siguientes clases:

| Clase | Responsabilidad |
|---|---|
| `Competitor.java` | Representar la entidad competidor mediante JPA y EclipseLink NoSQL. |
| `Vehicle.java` | Representar un objeto embebido asociado al competidor. |
| `CompetitorDTO.java` | Transportar información de los competidores. |
| `MongoConnectionTest.java` | Probar la persistencia mediante EntityManager y EntityManagerFactory. |

En particular, se utilizaron anotaciones como `@Entity`, `@NoSql` y `@Embeddable`, intentando conservar el modelo de persistencia planteado en el laboratorio.

Este primer desarrollo permitió estructurar la configuración de persistencia y el modelo de datos, aunque no se consiguió obtener una integración estable durante las pruebas de ejecución.

## 3. Dificultades técnicas identificadas

Durante los intentos de implementación se presentaron dificultades para conseguir una integración estable entre JPA, EclipseLink NoSQL, el controlador de MongoDB y el entorno Java utilizado.

Los principales aspectos que requirieron atención fueron:

**A. Integración entre EclipseLink NoSQL y MongoDB Driver**

La configuración inicial combinaba EclipseLink NoSQL 4.0.9 con MongoDB Driver Legacy 5.13.0.

La integración entre estos componentes requería verificar la compatibilidad de las interfaces y mecanismos de acceso a MongoDB esperados por el adaptador de EclipseLink.

**B. Entorno de ejecución JDK 26**

El proyecto inicial estaba configurado para compilar con Java 26:

```xml
<maven.compiler.release>26</maven.compiler.release>
```

Por tanto, también era necesario verificar la compatibilidad de las dependencias de persistencia y sus componentes internos con el entorno de ejecución utilizado.

**C. Integración de Jakarta Persistence**

La implementación utilizaba Jakarta Persistence 3.1 y EclipseLink 4.0.9. Esto requería que el proveedor, las anotaciones, el adaptador NoSQL y las dependencias del controlador funcionaran correctamente en conjunto.

**D. Desarrollo funcional incompleto**

Aunque se había avanzado en la estructura de persistencia y las entidades iniciales, todavía era necesario completar los requisitos funcionales de la actividad, incluyendo la incorporación de la entidad Producto y los servicios de consulta solicitados.

Es importante aclarar que estas dificultades corresponden a la combinación concreta de versiones y configuración utilizada. No significan que JPA, EclipseLink y MongoDB sean incompatibles en términos generales.

## 4. Justificación de la migración tecnológica

Después de realizar varios intentos de configuración y ejecución, se decidió cambiar la estrategia de persistencia y utilizar directamente el controlador oficial de MongoDB para Java.

La decisión tuvo como objetivos:

- Reducir la complejidad de integración entre el proveedor JPA y el adaptador NoSQL.
- Establecer una conexión directa entre la aplicación Java y MongoDB.
- Garantizar la persistencia de la información.
- Implementar los servicios REST requeridos por el laboratorio.
- Facilitar la ejecución y validación del proyecto.
- Obtener una solución funcional en el entorno de desarrollo disponible.

La migración modificó específicamente la capa de persistencia. Se conservaron Java, Maven, MongoDB y los objetivos funcionales de la actividad.

No se considera que el controlador nativo sea equivalente a JPA/EclipseLink NoSQL. Se trata de una adaptación tecnológica que utiliza otro mecanismo de acceso a los datos.

## 5. Arquitectura tecnológica adoptada

La implementación migrada utiliza las siguientes tecnologías:

| Componente | Tecnología |
|---|---|
| Lenguaje | Java |
| Entorno de desarrollo | Apache NetBeans IDE 32 |
| JDK | 26 |
| Gestión de dependencias | Maven |
| Base de datos | MongoDB Community Server 9.0.4 |
| Persistencia | MongoDB Java Sync Driver 5.13.0 |
| API REST | Jersey 2.41 |
| Servidor HTTP | Eclipse Jetty 9.4.56 |

### 5.1. Arquitectura de persistencia

La aplicación utiliza `MongoClient` para establecer la conexión con MongoDB y realizar operaciones directamente sobre las colecciones.

La base de datos utilizada es `ArquiSW`.

Se definieron dos colecciones:

- `competitors`: almacena los datos de los competidores y la referencia al producto asociado.
- `productos`: almacena los productos registrados.

La relación entre ambas colecciones se establece mediante el identificador `productoId`.

### 5.2. Asociación entre competidores y productos

El registro de un competidor exige que se proporcione un producto asociado.

La operación de creación realiza los siguientes pasos:

1. Recibe los datos del competidor y del producto mediante una solicitud HTTP POST.
2. Valida la información obligatoria del producto.
3. Inserta el producto en la colección `productos`.
4. Obtiene el identificador generado para el producto.
5. Inserta el competidor en la colección `competitors`, incluyendo el campo `productoId`.
6. Si falla la inserción del competidor, intenta eliminar el producto recién creado para evitar que quede huérfano.

Esta estrategia funciona con MongoDB local standalone y no requiere transacciones multidocumento.

MongoDB no impone claves foráneas como una base de datos relacional. Por esta razón, la integridad de la asociación se controla mediante la lógica del servicio.

## 6. Requisitos

- JDK 17 o superior.
- Apache NetBeans IDE 32.
- Maven 3.9 o superior (NetBeans puede utilizar su versión integrada).
- MongoDB Community Server 9.0.4 en ejecución en `localhost:27017`.

El proyecto está configurado para compilar con Java 17 como versión objetivo, por lo que puede ejecutarse con un JDK compatible igual o superior.

## 7. Configuración

Por defecto, la aplicación utiliza:

- URI: `mongodb://localhost:27017`
- Base de datos: `ArquiSW`
- Colecciones: `competitors` y `productos`

Se pueden cambiar mediante variables de entorno:

- `MONGO_URI`
- `MONGO_DATABASE`
- `PORT` (por defecto 8080).

La base de datos y las colecciones se crean automáticamente al insertar el primer documento.

## 8. Abrir en NetBeans

1. Descomprime el ZIP.
2. En NetBeans selecciona **File > Open Project**.
3. Selecciona la carpeta `Lab-MongoDB-Migrado`.
4. Verifica que NetBeans detecte el proyecto Maven.
5. Configura el JDK del proyecto en 17 o superior.
6. Ejecuta **Clean and Build**.
7. Ejecuta la clase `com.example.Main`.

Desde terminal, en la carpeta del proyecto:

```bash
mvn clean package
java -cp "target/classes;target/dependency/*" com.example.Main
```

En Windows PowerShell el separador del classpath es `;`. En Linux/macOS usa `:`.

Cuando la aplicación inicia correctamente, se puede acceder a:

```text
http://localhost:8080
```

## 9. Endpoints

Base URL: `http://localhost:8080`

| Método | Ruta | Función |
|---|---|---|
| GET | `/competitors` | Lista competidores. |
| POST | `/competitors` | Crea un competidor y su producto obligatorio. |
| GET | `/competitors/starts-with-a` | Devuelve los nombres que comienzan por A. |
| GET | `/products` | Lista todos los productos. |

### 9.1. POST /competitors

Headers:

```text
Content-Type: application/json
```

Body de ejemplo:

```json
{
  "name": "Ana",
  "surname": "Gomez",
  "age": 22,
  "telephone": "6012345",
  "cellphone": "3001234567",
  "address": "Calle 42 #18",
  "city": "Bogota",
  "country": "Colombia",
  "producto": {
    "nombre": "Portatil",
    "marca": "Lenovo",
    "fechaCompra": "2026-11-09T00:00:00.000Z"
  }
}
```

La API crea primero el producto en `productos` y luego crea el competidor con el campo `productoId` que referencia el documento del producto.

Si falla la inserción del competidor, intenta eliminar el producto recién creado para no dejarlo huérfano.

Si se omite el producto o alguno de sus campos obligatorios, la API responde HTTP 400.

## 10. Pruebas sugeridas en Postman

Para validar el funcionamiento de los servicios, se recomienda realizar las siguientes solicitudes:

1. **Crear competidor:** POST `http://localhost:8080/competitors` con el JSON anterior. Resultado esperado: HTTP 201 Created.
2. **Listar competidores:** GET `http://localhost:8080/competitors`. Debe aparecer el competidor registrado y su `productoId`.
3. **Listar productos:** GET `http://localhost:8080/products`. Debe aparecer el producto creado.
4. **Buscar nombres que comienzan por A:** GET `http://localhost:8080/competitors/starts-with-a`. Debe aparecer el nombre del competidor correspondiente.

### 10.1. Resultados de las pruebas realizadas

Las pruebas funcionales se ejecutaron localmente utilizando Postman y MongoDB Compass.

| Prueba | Resultado obtenido |
|---|---|
| Conexión con MongoDB 9.0.4 | Exitosa |
| Inicio del servidor Jetty | Exitoso |
| Registro de competidor con producto | HTTP 201 Created |
| Consulta de competidores | HTTP 200 OK |
| Consulta de productos | HTTP 200 OK |
| Búsqueda sin coincidencias | HTTP 200 OK, lista vacía |
| Búsqueda de nombres que comienzan por A | HTTP 200 OK, coincidencia encontrada |

Se realizaron dos registros de prueba:

- Luisa Gomez, asociada al producto Portátil Lenovo.
- Andres Rodriguez, asociado a un producto Mouse Logitech.

En la consulta de competidores se recuperó la información de Luisa Gomez y su referencia al producto.

En la consulta de productos se verificó la existencia del Portátil Lenovo.

Finalmente, se ejecutó el filtro de nombres que comienzan por A. Inicialmente devolvió una lista vacía porque no existían coincidencias. Después de registrar a Andres, el servicio devolvió:

```json
[
  "Andres"
]
```

Estos resultados permitieron comprobar el funcionamiento de los servicios y la persistencia de los documentos.

### 10.2. Verificación en MongoDB Compass

En MongoDB Compass se debe abrir la base de datos `ArquiSW` y revisar las colecciones:

- `competitors`
- `productos`

En las pruebas realizadas se comprobó que ambas colecciones contenían documentos después del registro de los competidores.

## 11. Consultas en mongosh

Para consultar directamente la información almacenada en MongoDB se pueden utilizar los siguientes comandos:

```javascript
use ArquiSW

db.competitors.find().pretty()

db.productos.find().pretty()

db.competitors.find({ name: /^A/i })
```

## 12. Diferencias frente al PDF original

El PDF propone una arquitectura basada en:

- JPA mediante `@Entity`.
- EclipseLink NoSQL mediante `@NoSql`.
- Objetos embebidos mediante `@Embedded`.
- Configuración de persistencia en `persistence.xml`.
- EclipseLink NoSQL 2.5.0.
- MongoDB Java Driver 2.12.3.

La combinación de dependencias indicada en la guía corresponde a una configuración tecnológica heredada.

En la implementación migrada se utiliza el controlador oficial de MongoDB para Java 5.13.0, y la persistencia se realiza directamente mediante `MongoClient`.

También se modificó el modelo de asociación:

| Aspecto | Propuesta original | Implementación migrada |
|---|---|---|
| Persistencia | JPA/EclipseLink NoSQL | MongoDB Java Sync Driver |
| Configuración | `persistence.xml` | `MongoClient` |
| Acceso a datos | `EntityManager` | Colecciones de MongoDB |
| Asociación del producto | Modelo de entidades/objetos embebidos | Referencia mediante `productoId` |
| Colecciones | Según el mapeo NoSQL | `competitors` y `productos` |

La migración guarda Producto en una colección independiente y enlaza cada competidor mediante `productoId`, lo que permite implementar claramente el listado de productos solicitado.

La asociación se valida desde el endpoint: no se permite crear un competidor sin producto. La integridad se garantiza mediante la lógica del servicio y no mediante una restricción de clave foránea de MongoDB.

## 13. Limitaciones y consideraciones

La implementación final permite demostrar los requisitos funcionales de persistencia y consulta mediante MongoDB, pero no reproduce literalmente el mecanismo de persistencia solicitado en el PDF.

Por lo tanto:

- No utiliza JPA.
- No utiliza EclipseLink NoSQL.
- No utiliza `EntityManager` para las operaciones de persistencia.
- Utiliza directamente el controlador oficial de MongoDB para Java.
- La asociación entre competidores y productos se gestiona mediante referencias entre documentos.

La migración debe entenderse como una adaptación tecnológica y no como una implementación equivalente a la arquitectura original.

La aceptación de esta alternativa queda sujeta a la evaluación del docente.

## 14. Conclusión

El desarrollo del laboratorio permitió experimentar con dos estrategias diferentes de persistencia en MongoDB.

Inicialmente se intentó implementar la solución mediante JPA y EclipseLink NoSQL, siguiendo la arquitectura propuesta en la guía. Sin embargo, las dificultades de integración entre las dependencias y el entorno de ejecución utilizado impidieron obtener una implementación estable.

Como alternativa, se adoptó el controlador nativo de MongoDB para Java, lo que permitió establecer una conexión funcional con MongoDB 9.0.4, almacenar y consultar documentos, relacionar competidores con productos y validar los servicios REST mediante Postman.

La decisión de migración permitió completar las pruebas funcionales y obtener una solución verificable, dejando documentadas las diferencias respecto a la tecnología originalmente solicitada.
