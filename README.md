# Kata Números romanos

### Fase 1

Desarrollar un componente que permita convertir números enteros a romanos y viceversa según el siguiente esquema:

* 1 ➔ I
* 2 ➔ II
* 3 ➔ III
* 4 ➔ IV
* 5 ➔ V
* 9 ➔ IX
* 21 ➔ XXI
* 50 ➔ L
* 100 ➔ C
* 500 ➔ D
* 1000 ➔ M


En ambos métodos de conversión, el componente debe validar si se ingresa un valor no permitido y responder con una excepción personalizada.

**Plus Fase 1:** Aplicar TDD o al menos hacer Tests unitarios del componente probando al menos 2 border cases para cada método de conversión.


### Fase 2

Exponer el método del componente que convierte valores numéricos arábigos a romanos en un endpoint (GET)
Exponer el método del componente que convierte valores numéricos romanos a arábigos en un endpoint (GET)

**Plus Fase 2:** Aplicar TDD (Test de integración usando la suite de Spring).


### Requerimientos/Restricciones

**Fase 1 y 2:** Usar Java 17 o superior. Maven o Gradle para la gestión de dependencias.
Para los puntos plus de cada fase, en lo relacionado a la infraestructura de tests se pueden usar las siguientes herramientas JUnit5+Mockito o Spock y Spring Boot Testing.
**Fase 2:** Usar Spring boot 3+.

Completar y modificar este readme e incluirlo como parte del repositorio agregando detalles sobre cómo construir el proyecto desde cero y ponerlo en ejecución.

# Kata Números Romanos - Solución

## 📋 Descripción

Aplicación Spring Boot que proporciona servicios de conversión bidireccional entre números arábigos y romanos, siguiendo principios de TDD y arquitectura limpia.

## 🚀 Tecnologías

- Java 21
- Spring Boot 3.5.5
- Maven 3.9+
- JUnit 5
- Mockito
- Spring Boot Test

## 📁 Estructura del Proyecto

```
roman-numbers-kata/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/kata/romannumbers/
│   │   │       ├── RomanNumbersKataApplication.java
│   │   │       ├── application/
│   │   │       │   ├── ArabicToRomanNumberConverter.java
│   │   │       │   ├── ArabicToRomanNumberConverterUseCase.java
│   │   │       │   ├── RomanToArabicNumberConverter.java
│   │   │       │   ├── RomanToArabicNumberConverterUseCase.java
│   │   │       │   └── exception/
│   │   │       │       ├── InvalidArabicNumberException.java
│   │   │       │       └── InvalidRomanNumberException.java
│   │   │       ├── domain/
│   │   │       └── infrastructure/
│   │   │           └── input/
│   │   │               └── rest/
│   │   │                   ├── ArabicToRomanNumberGetAdapter.java
│   │   │                   ├── RomanToArabicNumberGetAdapter.java
│   │   │                   ├── exception/
│   │   │                   │   └── GlobalExceptionHandler.java
│   │   │                   └── model/
│   │   │                       ├── ArabicToRomanNumberResponse.java
│   │   │                       ├── ErrorResponse.java
│   │   │                       └── RomanToArabicNumberResponse.java
│   │   └── resources/
│   │       └── application.yml
│   └── test/
│       └── java/
│           └── com/kata/romannumbers/
│               ├── RomanNumbersKataApplicationTests.java
│               ├── application/
│               │   ├── ArabicToRomanNumberConverterTest.java
│               │   └── RomanToArabicNumberConverterTest.java
│               └── infrastructure/
│                   └── input/
│                       └── rest/
│                           ├── ArabicToRomanNumberGetAdapterTest.java
│                           └── RomanToArabicNumberGetAdapterTest.java
├── Dockerfile
├── docker-compose.yml
├── .dockerignore
├── pom.xml
└── README.md
```

## 🛠️ Instalación y Configuración

### Prerrequisitos

- JDK 21 o superior (El proyecto está configurado para Java 21)
- Maven 3.9 o superior (o usar el Maven wrapper incluido)

### Clonar el repositorio

```bash
git clone https://github.com/equi32/roman-numbers-kata.git
cd roman-numbers-kata
```

### Compilar el proyecto

```bash
# Usando Maven wrapper (recomendado)
./mvnw clean compile

# O usando Maven local
mvn clean compile
```

### Ejecutar tests

```bash
# Todos los tests
./mvnw test

# Solo tests unitarios
./mvnw test -Dtest=ArabicToRomanNumberConverterTest,RomanToArabicNumberConverterTest

# Solo tests de integración
./mvnw test -Dtest=ArabicToRomanNumberGetAdapterTest,RomanToArabicNumberGetAdapterTest
```

### Ejecutar la aplicación

```bash
# Opción 1: Usando Maven wrapper
./mvnw spring-boot:run

# Opción 2: Generando el JAR
./mvnw clean package
java -jar target/romannumbers-1.0-SNAPSHOT.jar
```

La aplicación estará disponible en: `http://localhost:8080`

## 🐳 Ejecución con Docker

### Prerrequisitos para Docker
- Docker Desktop o Docker Engine
- Docker Compose

### Construir y ejecutar con Docker Compose

```bash
# Construir y ejecutar la aplicación
docker-compose up --build

# Ejecutar en modo background (detached)
docker-compose up -d --build

# Ver logs de la aplicación
docker-compose logs -f roman-numbers-kata

# Parar la aplicación
docker-compose down
```

### Construir y ejecutar solo con Docker

```bash
# Construir la imagen
docker build -t roman-numbers-kata .

# Ejecutar el contenedor
docker run -p 8080:8080 --name roman-numbers-kata roman-numbers-kata
```

La aplicación estará disponible en: `http://localhost:8080`

### Health Check
Docker incluye un health check que verifica el endpoint `/actuator/health` cada 30 segundos.

## 📡 API Endpoints

### 1. Convertir Arábigo a Romano

**Endpoint:** `GET /api/v1/roman-numbers/arabic-to-roman`

**Parámetros:**
- `number` (int): Número arábigo a convertir (1-3999)

**Ejemplo:**
```bash
curl "http://localhost:8080/api/v1/roman-numbers/arabic-to-roman?number=42"
```

**Respuesta exitosa (200):**
```json
{
  "arabic": 42,
  "roman": "XLII"
}
```

**Respuesta error (400):**
```json
{
  "error": "Invalid arabic number",
  "message": "Number must be between 1 and 3999"
}
```

### 2. Convertir Romano a Arábigo

**Endpoint:** `GET /api/v1/roman-numbers/roman-to-arabic`

**Parámetros:**
- `roman` (String): Número romano a convertir

**Ejemplo:**
```bash
curl "http://localhost:8080/api/v1/roman-numbers/roman-to-arabic?roman=XLII"
```

**Respuesta exitosa (200):**
```json
{
  "roman": "XLII",
  "arabic": 42
}
```

**Respuesta error (400):**
```json
{
  "error": "Invalid Roman Number",
  "message": "Invalid roman number format: XZ"
}
```

## 🧪 Testing

### Tests Unitarios (Fase 1)

El proyecto incluye tests unitarios exhaustivos para el servicio de conversión:

- **Casos normales:** Conversión de valores típicos
- **Casos límite (Border cases):**
    - Valores mínimos (1, I)
    - Valores máximos (3999, MMMCMXCIX)
    - Números con notación sustractiva (4, 9, 40, 90, 400, 900)
    - Cadenas vacías y nulas
    - Valores fuera de rango

### Tests de Integración (Fase 2)

Tests de integración usando Spring Boot Test:

- Validación de endpoints REST
- Manejo de errores HTTP
- Serialización/deserialización JSON
- Validación de códigos de estado HTTP

## 🏗️ Arquitectura y Decisiones de Diseño

### Patrón de Arquitectura

- **Arquitectura en capas:** Controller → Service → Domain
- **Separación de responsabilidades:** Cada capa tiene una responsabilidad única
- **Inversión de dependencias:** Uso de interfaces para desacoplar implementaciones

### Principios SOLID Aplicados

- **Single Responsibility:** Cada clase tiene una única razón para cambiar
- **Open/Closed:** Extensible para nuevas funcionalidades sin modificar código existente
- **Dependency Inversion:** Dependencia de abstracciones, no de implementaciones concretas

### Algoritmo de Conversión

#### Arábigo a Romano
1. Validación del rango (1-3999)
2. Descomposición usando valores base ordenados descendentemente
3. Construcción incremental del resultado

#### Romano a Arábigo
1. Validación del formato usando expresión regular
2. Procesamiento de izquierda a derecha
3. Aplicación de regla sustractiva cuando el valor actual < valor siguiente

### Manejo de Excepciones

- Excepciones personalizadas para cada tipo de error
- GlobalExceptionHandler para respuestas HTTP consistentes
- Mensajes de error descriptivos

## 📊 Cobertura de Tests

El proyecto incluye cobertura exhaustiva de pruebas con JaCoCo configurado para generar reportes automáticos:

```bash
# Ejecutar tests con reporte de cobertura
./mvnw test

# Ver reporte de cobertura (generado automáticamente)
# Localizado en: target/site/jacoco/index.html
```

**Test Coverage incluye:**
- Tests unitarios: 40+ casos de prueba
- Tests de integración: Endpoints REST completos
- Border cases: Valores límite (1, 3999, null, empty, invalid formats)
- Casos de error: Excepciones personalizadas
- Validaciones: Entrada inválida y formatos incorrectos

## 🔧 Configuración Adicional

### application.yml

```yaml
spring:
  application:
    name: Roman Numbers Kata
  jackson:
    default-property-inclusion: non_null
    serialization:
      write-dates-as-timestamps: false
      indent-output: true

server:
  port: 8080
  servlet:
    context-path: /
    encoding:
      enabled: true
      force: true
  error:
    include-messages: always

management:
  endpoint:
    health:
      show-details: always
  endpoints:
    web:
      exposure:
        include: health,info
```

## 📝 Notas de Implementación

### Validaciones Implementadas

1. **Números Arábigos:**
    - Rango válido: 1-3999 (limitación histórica de números romanos)
    - Validación de tipo de dato

2. **Números Romanos:**
    - Caracteres válidos: I, V, X, L, C, D, M
    - Secuencias válidas (no más de 3 caracteres iguales consecutivos, excepto M)
    - Orden correcto de símbolos

### Optimizaciones

- Uso de `StringBuilder` para construcción eficiente de strings
- Precálculo de valores en arrays estáticos
- Validación temprana para fail-fast

## 👥 Autor

Ezequiel Aparicio

## 📄 Licencia

Este proyecto es parte de un challenge técnico.

---

## 🎯 Checklist de Requerimientos

- ✅ **Fase 1:**
    - ✅ Conversión de enteros a romanos
    - ✅ Conversión de romanos a enteros
    - ✅ Validación con excepciones personalizadas
    - ✅ Tests unitarios con TDD
    - ✅ Tests de border cases

- ✅ **Fase 2:**
    - ✅ Endpoint GET para conversión a romano
    - ✅ Endpoint GET para conversión a arábigo
    - ✅ Tests de integración con Spring Boot Test

- ✅ **Requerimientos técnicos:**
    - ✅ Java 17+
    - ✅ Maven para gestión de dependencias
    - ✅ Spring Boot 3+
    - ✅ JUnit 5 + Mockito
    - ✅ README completo con instrucciones