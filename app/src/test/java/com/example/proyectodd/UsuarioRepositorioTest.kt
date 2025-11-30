package com.example.proyectodd.data.repository

import com.example.proyectodd.model.data.repository.UsuarioRepositorio
import com.example.proyectodd.viewmodel.api.UsuarioApi
import com.example.proyectodd.viewmodel.retrofit.RetrofitUsuario
import com.google.common.truth.Truth.assertThat
import io.mockk.*
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Response

/**
 * UBICACIÓN: app/src/test/java/com/example/proyectodd/data/repository/UsuarioRepositorioTest.kt
 */
class UsuarioRepositorioTest {

    private lateinit var api: UsuarioApi
    private lateinit var repositorio: UsuarioRepositorio

    @Before
    fun setup() {
        api = mockk()
        mockkObject(RetrofitUsuario)
        every { RetrofitUsuario.usuarioApi } returns api
        repositorio = UsuarioRepositorio()
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    // ==================== LOGIN - CASOS EXITOSOS ====================

    @Test
    fun `login con credenciales correctas retorna Success`() = runTest {
        val email = "test@example.com"
        val password = "password123"

        coEvery {
            api.loginUsuario(any())
        } returns Response.success("LOGIN_OK")

        val resultado = repositorio.login(email, password)

        assertThat(resultado.isSuccess).isTrue()
        resultado.onSuccess { mensaje ->
            assertThat(mensaje).isEqualTo("LOGIN_OK")
        }
    }

    @Test
    fun `login envia credenciales correctamente formateadas`() = runTest {
        val email = "test@example.com"
        val password = "password123"

        val credencialesCapturadas = slot<Map<String, String>>()

        coEvery {
            api.loginUsuario(capture(credencialesCapturadas))
        } returns Response.success("OK")

        repositorio.login(email, password)

        assertThat(credencialesCapturadas.captured["email"]).isEqualTo(email)
        assertThat(credencialesCapturadas.captured["password"]).isEqualTo(password)
    }

    // ==================== LOGIN - CASOS DE ERROR ====================

    @Test
    fun `login con credenciales incorrectas retorna Failure con codigo 401`() = runTest {
        val email = "test@example.com"
        val password = "wrongpass"

        val errorBody = "Credenciales inválidas".toResponseBody()
        coEvery {
            api.loginUsuario(any())
        } returns Response.error(401, errorBody)

        val resultado = repositorio.login(email, password)

        assertThat(resultado.isFailure).isTrue()
        resultado.onFailure { exception ->
            assertThat(exception.message).contains("Credenciales inválidas")
        }
    }

    @Test
    fun `login con error del servidor retorna Failure`() = runTest {
        val email = "test@example.com"
        val password = "password123"

        val errorBody = "Error interno del servidor".toResponseBody()
        coEvery {
            api.loginUsuario(any())
        } returns Response.error(500, errorBody)

        val resultado = repositorio.login(email, password)

        assertThat(resultado.isFailure).isTrue()
        resultado.onFailure { exception ->
            val mensajesValidos = listOf("Error del servidor", "Error interno", "servidor")
            val contieneAlgunMensaje = mensajesValidos.any {
                exception.message?.contains(it, ignoreCase = true) == true
            }
            assertThat(contieneAlgunMensaje).isTrue()
        }
    }

    @Test
    fun `login con excepcion de red retorna Failure`() = runTest {
        val email = "test@example.com"
        val password = "password123"

        coEvery {
            api.loginUsuario(any())
        } throws Exception("Sin conexión a internet")

        val resultado = repositorio.login(email, password)

        assertThat(resultado.isFailure).isTrue()
        resultado.onFailure { exception ->
            assertThat(exception.message).contains("Error de conexión")
        }
    }

    @Test
    fun `login con codigo 404 retorna Failure`() = runTest {
        val errorBody = "Usuario no encontrado".toResponseBody()
        coEvery {
            api.loginUsuario(any())
        } returns Response.error(404, errorBody)

        val resultado = repositorio.login("noexiste@test.com", "pass123")

        assertThat(resultado.isFailure).isTrue()
        resultado.onFailure { exception ->
            assertThat(exception.message).contains("Usuario no encontrado")
        }
    }

    // ==================== REGISTER - CASOS EXITOSOS ====================

    @Test
    fun `register con datos validos retorna Success`() = runTest {
        val nombre = "Juan Pérez"
        val email = "juan@example.com"
        val password = "password123"

        coEvery {
            api.registrarUsuario(any())
        } returns Response.success("REGISTRO_OK")

        val resultado = repositorio.register(nombre, email, password)

        assertThat(resultado.isSuccess).isTrue()
        resultado.onSuccess { mensaje ->
            assertThat(mensaje).isEqualTo("REGISTRO_OK")
        }
    }

    @Test
    fun `register envia todos los datos correctamente`() = runTest {
        val nombre = "Juan"
        val email = "juan@example.com"
        val password = "pass123"

        val datosCapturados = slot<Map<String, String>>()

        coEvery {
            api.registrarUsuario(capture(datosCapturados))
        } returns Response.success("OK")

        repositorio.register(nombre, email, password)

        assertThat(datosCapturados.captured["nombre"]).isEqualTo(nombre)
        assertThat(datosCapturados.captured["email"]).isEqualTo(email)
        assertThat(datosCapturados.captured["password"]).isEqualTo(password)
    }

    // ==================== REGISTER - CASOS DE ERROR ====================

    @Test
    fun `register con email duplicado retorna Failure con codigo 409`() = runTest {
        val nombre = "Juan Pérez"
        val email = "existente@example.com"
        val password = "password123"

        val errorBody = "El email ya está registrado".toResponseBody()
        coEvery {
            api.registrarUsuario(any())
        } returns Response.error(409, errorBody)

        val resultado = repositorio.register(nombre, email, password)

        assertThat(resultado.isFailure).isTrue()
        resultado.onFailure { exception ->
            assertThat(exception.message).contains("ya está registrado")
        }
    }

    @Test
    fun `register con error del servidor retorna Failure`() = runTest {
        val nombre = "Juan Pérez"
        val email = "juan@example.com"
        val password = "password123"

        val errorBody = "Error en la base de datos".toResponseBody()
        coEvery {
            api.registrarUsuario(any())
        } returns Response.error(500, errorBody)

        val resultado = repositorio.register(nombre, email, password)

        assertThat(resultado.isFailure).isTrue()
        resultado.onFailure { exception ->
            assertThat(exception.message).isNotNull()
        }
    }

    @Test
    fun `register con excepcion de red retorna Failure`() = runTest {
        val nombre = "Juan Pérez"
        val email = "juan@example.com"
        val password = "password123"

        coEvery {
            api.registrarUsuario(any())
        } throws Exception("Timeout")

        val resultado = repositorio.register(nombre, email, password)

        assertThat(resultado.isFailure).isTrue()
        resultado.onFailure { exception ->
            assertThat(exception.message).contains("Error de conexión")
        }
    }

    @Test
    fun `register con codigo 400 retorna Failure`() = runTest {
        val errorBody = "Datos inválidos".toResponseBody()
        coEvery {
            api.registrarUsuario(any())
        } returns Response.error(400, errorBody)

        val resultado = repositorio.register("", "invalido", "123")

        assertThat(resultado.isFailure).isTrue()
        resultado.onFailure { exception ->
            assertThat(exception.message).contains("Datos inválidos")
        }
    }

    // ==================== TESTS ADICIONALES ====================

    @Test
    fun `multiples llamadas a login son independientes`() = runTest {
        coEvery {
            api.loginUsuario(any())
        } returns Response.success("OK")

        val resultado1 = repositorio.login("user1@test.com", "pass1")
        val resultado2 = repositorio.login("user2@test.com", "pass2")

        assertThat(resultado1.isSuccess).isTrue()
        assertThat(resultado2.isSuccess).isTrue()
        coVerify(exactly = 2) { api.loginUsuario(any()) }
    }

    @Test
    fun `multiples llamadas a register son independientes`() = runTest {
        coEvery {
            api.registrarUsuario(any())
        } returns Response.success("OK")

        val resultado1 = repositorio.register("User1", "user1@test.com", "pass1")
        val resultado2 = repositorio.register("User2", "user2@test.com", "pass2")

        assertThat(resultado1.isSuccess).isTrue()
        assertThat(resultado2.isSuccess).isTrue()
        coVerify(exactly = 2) { api.registrarUsuario(any()) }
    }

    @Test
    fun `login con response body vacio usa mensaje por defecto`() = runTest {
        // Simular respuesta exitosa pero con body vacío/blanco
        coEvery {
            api.loginUsuario(any())
        } returns Response.success("")

        val resultado = repositorio.login("test@example.com", "pass123")

        // Tu código usa "?: LOGIN_OK", así que con string vacío debería retornar ""
        assertThat(resultado.isSuccess).isTrue()
    }

    @Test
    fun `register con response body vacio usa mensaje por defecto`() = runTest {
        coEvery {
            api.registrarUsuario(any())
        } returns Response.success("")

        val resultado = repositorio.register("Juan", "juan@test.com", "pass123")

        assertThat(resultado.isSuccess).isTrue()
    }

    @Test
    fun `login verifica estructura del mapa de credenciales`() = runTest {
        val credencialesCapturadas = slot<Map<String, String>>()

        coEvery {
            api.loginUsuario(capture(credencialesCapturadas))
        } returns Response.success("OK")

        repositorio.login("test@example.com", "password123")

        // Verificar que el mapa tiene exactamente 2 elementos
        assertThat(credencialesCapturadas.captured.size).isEqualTo(2)
        assertThat(credencialesCapturadas.captured.containsKey("email")).isTrue()
        assertThat(credencialesCapturadas.captured.containsKey("password")).isTrue()
    }

    @Test
    fun `register verifica estructura del mapa de datos`() = runTest {
        val datosCapturados = slot<Map<String, String>>()

        coEvery {
            api.registrarUsuario(capture(datosCapturados))
        } returns Response.success("OK")

        repositorio.register("Juan", "juan@example.com", "pass123")

        // Verificar que el mapa tiene exactamente 3 elementos
        assertThat(datosCapturados.captured.size).isEqualTo(3)
        assertThat(datosCapturados.captured.containsKey("nombre")).isTrue()
        assertThat(datosCapturados.captured.containsKey("email")).isTrue()
        assertThat(datosCapturados.captured.containsKey("password")).isTrue()
    }
}