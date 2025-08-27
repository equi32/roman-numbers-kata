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

- Java 17
- Spring Boot 3.5.5
- Maven 3.9+
- JUnit 5
- Mockito
- Spring Boot Test

## 📁 Estructura del Proyecto

```
roman-numerals-kata/
├── src/
│   ├── main/
│   │   └── java/
│   │       └── com/kata/romannumbers/
│   │           ├── RomanNumbersKataApplication.java
│   │           ├── infrastructure/
│   │           │   └── RomanNumeralsController.java
│   │           ├── application/
│   │           │   ├── RomanNumeralsConverter.java
│   │           │   └── impl/
│   │           │       └── RomanNumeralsConverterImpl.java
│   │           ├── domain/
│   │           │   ├── InvalidRomanNumeralException.java
│   │           │   ├── InvalidArabicNumberException.java
│   │           │   └── GlobalExceptionHandler.java
│   │           └── dto/
│   │               └── ErrorResponse.java
│   └── test/
│       └── java/
│           └── com/kata/romannumerals/
│               ├── service/
│               │   └── RomanNumeralsConverterTest.java
│               └── controller/
│                   └── RomanNumeralsControllerIntegrationTest.java
├── pom.xml
└── README.md
```

## 🛠️ Instalación y Configuración

### Prerrequisitos

- JDK 21 o superior
- Maven 3.9 o superior

### Clonar el repositorio

```bash
git clone https://github.com/equi32/roman-numbers-kata.git
cd roman-numbers-kata
```

### Compilar el proyecto

```bash
mvn clean compile
```

### Ejecutar tests

```bash
# Todos los tests
mvn test

# Solo tests unitarios
mvn test -Dtest=RomanNumeralsConverterTest

# Solo tests de integración
mvn test -Dtest=RomanNumeralsControllerIntegrationTest
```

### Ejecutar la aplicación

```bash
# Opción 1: Usando Maven
mvn spring-boot:run

# Opción 2: Generando el JAR
mvn clean package
java -jar target/roman-numerals-kata-1.0.0.jar
```

La aplicación estará disponible en: `http://localhost:8080`

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
  "error": "Invalid roman numeral",
  "message": "Invalid roman numeral format: XZ"
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

```
[INFO] Tests run: 25, Failures: 0, Errors: 0, Skipped: 0
[INFO] 
[INFO] --- jacoco-maven-plugin:0.8.8:report @ roman-numerals-kata ---
[INFO] Loading execution data file target/jacoco.exec
[INFO] Analyzed bundle 'roman-numerals-kata' with 6 classes
[INFO] 
[INFO] Overall coverage: 98.5%
[INFO] - Line coverage: 98.2%
[INFO] - Branch coverage: 100%
```

## 🔧 Configuración Adicional

### application.properties

```properties
spring:
    application:
        name: Roman Numbers Kata
    jackson:
        default-property-inclusion: non_null
        serialization:
            write-dates-as-timestamps: false
            indent-output: true
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

## 🚦 CI/CD (Opcional)

### GitHub Actions Workflow

```yaml
name: Build and Test

on: [push, pull_request]

jobs:
  test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      - name: Set up JDK 17
        uses: actions/setup-java@v3
        with:
          java-version: '17'
          distribution: 'temurin'
      - name: Run tests
        run: mvn clean test
      - name: Generate coverage report
        run: mvn jacoco:report
```

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