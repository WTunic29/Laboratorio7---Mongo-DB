# Laboratorio 7 - MongoDB

Repositorio correspondiente al desarrollo del Laboratorio 7 de Arquitectura de Software, enfocado en la persistencia de información utilizando MongoDB y servicios REST desarrollados en Java.

## Descripción

Este repositorio contiene dos implementaciones desarrolladas durante el laboratorio:

### 01. Implementación inicial: JPA + EclipseLink NoSQL

Primera aproximación a la solución utilizando JPA, EclipseLink NoSQL y MongoDB.

Se desarrollaron las entidades iniciales, la configuración de persistencia mediante `persistence.xml` y pruebas utilizando `EntityManager`.

Durante el proceso se presentaron dificultades de integración entre las dependencias y el entorno de ejecución, por lo que no se consiguió una implementación estable.

### 02. Implementación migrada: MongoDB Java Driver

Segunda implementación que utiliza directamente el controlador oficial de MongoDB para Java.

Se adoptó esta alternativa para conseguir una integración funcional con MongoDB Community Server 9.0.4.

Esta versión implementa y valida los requisitos funcionales de la actividad:

- Crear y almacenar productos con nombre, marca y fecha de compra.
- Asociar obligatoriamente un producto a cada competidor.
- Consultar todos los productos registrados.
- Obtener los nombres de los competidores que comienzan por la letra A.

## Tecnologías

| Tecnología | Implementación |
|---|---|
| Java | Ambas |
| Maven | Ambas |
| JPA | Implementación inicial |
| EclipseLink NoSQL | Implementación inicial |
| MongoDB Java Sync Driver | Implementación migrada |
| MongoDB Community Server | Ambas |
| Jersey / JAX-RS | Implementación migrada |
| Eclipse Jetty | Implementación migrada |

## Justificación de la migración

La migración se realizó después de varios intentos de integración entre JPA, EclipseLink NoSQL, MongoDB y JDK 26.

Debido a las dificultades encontradas con la configuración y compatibilidad de los componentes utilizados, se decidió sustituir la capa de persistencia basada en EclipseLink NoSQL por el controlador oficial de MongoDB para Java.

La migración conserva los requisitos funcionales de la actividad, aunque modifica el mecanismo de persistencia originalmente propuesto.

Por tanto, la segunda implementación no utiliza JPA ni EclipseLink NoSQL y debe entenderse como una adaptación tecnológica.

## Estructura del repositorio

- `01-Implementacion-JPA-EclipseLink`: contiene el intento inicial.
- `02-Implementacion-MongoDB-Driver`: contiene la implementación migrada y funcional (Dentro de este se encuentra el README del informe tecnico (una explicacion mas detallada.
- `evidencias`: contiene capturas de las pruebas realizadas.

## Documentación y ejecución

Para conocer las dependencias, configuración, instrucciones de ejecución y endpoints de cada implementación, consulta el README correspondiente dentro de cada carpeta.

## Estado del laboratorio

Los cuatro requisitos funcionales de la actividad fueron implementados y probados mediante Postman y MongoDB Compass en la segunda implementación.

Se deja constancia de que la arquitectura final difiere de la tecnología de persistencia originalmente solicitada en la guía académica.

## Autoría

Desarrollo académico realizado como parte del Laboratorio 7 de Arquitectura de Software.
