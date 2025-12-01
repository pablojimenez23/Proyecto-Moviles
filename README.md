# Card_DnD: Sistema de Autenticación y Gestión de Clientes

Este proyecto corresponde a una aplicación móvil (Android) desarrollada con Kotlin y Jetpack Compose, que se conecta a un microservicio (Backend) en Spring Boot para gestionar la autenticación de usuarios.

---

## 1. Información del Proyecto

* Nombre de la App:* Card DnD
* **Integrantes:**
    * Felix Elias Vargas Olivares
    * Raul Andres Fuenzalida Sierralta
    * Pablo Andres Jimenez Fredes
* **Fecha de Entrega:** 30/11/2025

---

## 2. Funcionalidades Implementadas

* **Autenticación de Usuario (Login):** Permite a un cliente registrado iniciar sesión utilizando su email y contraseña.
* **Registro de Usuario (Register):** Permite a un nuevo cliente crear una cuenta proporcionando nombre, email y contraseña.
* **Validación de Credenciales:** Verificación de campos obligatorios (nombre, email, contraseña) en el lado del cliente (Android) y validación de existencia/coincidencia en el lado del servidor.
* **Comunicación Cliente-Servidor:** Conexión estable mediante Retrofit y manejo de respuestas de texto plano (usando ScalarsConverterFactory).
* **Persistencia Local (Room):** [Si aplica] Gestión de datos locales (p. ej., Personajes o sesiones de usuario).

---

## 3. Endpoints Utilizados (Microservicio)

Esta aplicación utiliza los siguientes *endpoints* expuestos por el microservicio de Spring Boot:

| Propósito | Método | URL Base | Tipo de Respuesta |
| :--- | :--- | :--- | :--- |
| **Login** | `POST` | `http://localhost:8080/login` | Texto plano (`"LOGIN_OK"`) / Error (`401`) |
| **Registro** | `POST` | `http://localhost:8080/register` | Texto plano (`"REGISTRO_OK"`) / Error (`409`, `500`) |

* **Código Fuente del Backend (Microservicio):**
    `https://github.com/BrazenGhost630/Encargo_3_Moviles_Back_end.git`

---

## 4. Instrucciones de Ejecución

Para ejecutar el proyecto de principio a fin, sigue estos pasos:

### 4.1. Configuración del Backend (Spring Boot / XAMPP)

1.  **Iniciar Base de Datos:** Abre **XAMPP** (o el servidor de base de datos que utilices).
2.  **Activar Servicios:** Activa **Apache** y **MySQL**.
3.  **Creación de la Base de Datos:** Accede a `http://localhost/phpmyadmin` y crea la base de datos llamada **`usuarios_moviles`**.
4.  **Ejecutar Backend:** Abre el proyecto de Spring Boot (`Encargo_3_Moviles_Back_end`) en tu IDE (IntelliJ/STS) y **ejecuta la aplicación principal** (`DemoAplicaciones`). El backend estará listo en `http://localhost:8080`.

### 4.2. Configuración de la App Móvil (Android Studio)

1.  **Identificar IP Local:** En la PC donde se está ejecutando el backend, abre la terminal y ejecuta `ipconfig` (Windows) o `ifconfig` (Mac/Linux) para obtener tu dirección **IPv4 local** (ejemplo: `192.168.1.11`).
2.  **Conexión de Red:** Asegúrate de que el **teléfono/emulador** y la **PC** estén conectados a la **MISMA red Wi-Fi**.
3.  **Actualizar URL del Backend:** En Android Studio, navega al archivo `app/src/main/java/com/example/proyectodd/viewmodel/retrofit/RetrofitUsuario.kt`.
4.  **Cambiar BASE\_URL:** Modifica la constante `BASE_URL` para usar la IP local de tu PC:
    ```kotlin
    private const val BASE_URL = "http://[TU_IP_LOCAL]:8080/"
    ```
    (Ejemplo: `http://192.168.1.11:8080/`)
5.  **Ejecutar la App:** Ejecuta la aplicación en el dispositivo/emulador. La aplicación ya podrá comunicarse con el backend.

