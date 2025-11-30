package com.example.proyectodd.viewmodel

import android.app.Application
import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.proyectodd.helpers.MainDispatcherRule
import com.example.proyectodd.model.Usuario
import com.example.proyectodd.model.data.local.dao.UsuarioDao
import com.example.proyectodd.model.data.local.database.AppDatabase
import com.example.proyectodd.model.data.repository.UsuarioRepositorio
import com.example.proyectodd.viewmodel.state.AuthUIState
import com.google.common.truth.Truth.assertThat
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * UBICACIÓN: app/src/test/java/com/example/proyectodd/viewmodel/AuthViewModelTest.kt
 */
@ExperimentalCoroutinesApi
class AuthViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var application: Application
    private lateinit var mockContext: Context
    private lateinit var database: AppDatabase
    private lateinit var usuarioDao: UsuarioDao
    private lateinit var viewModel: AuthViewModel

    @Before
    fun setup() {
        application = mockk(relaxed = true)
        mockContext = mockk(relaxed = true)
        every { application.applicationContext } returns mockContext

        database = mockk(relaxed = true)
        usuarioDao = mockk(relaxed = true)
        every { database.usuarioDao() } returns usuarioDao

        mockkObject(AppDatabase.Companion)
        every { AppDatabase.obtenerBaseDatos(any()) } returns database

        viewModel = AuthViewModel(application)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    // ==================== ESTADO INICIAL ====================

    @Test
    fun `estadoAuth inicia en Inactivo`() {
        assertThat(viewModel.estadoAuth.value).isInstanceOf(AuthUIState.Inactivo::class.java)
    }

    @Test
    fun `usuarioActual inicia en null`() {
        assertThat(viewModel.usuarioActual.value).isNull()
    }

    // ==================== INICIAR SESIÓN - CASOS BÁSICOS ====================

    @Test
    fun `iniciarSesion con campos vacios muestra error`() = runTest {
        viewModel.iniciarSesion("", "")
        advanceUntilIdle()

        val estado = viewModel.estadoAuth.value
        assertThat(estado).isInstanceOf(AuthUIState.Error::class.java)
        assertThat((estado as AuthUIState.Error).mensaje).contains("Completa todos los campos")
    }

    @Test
    fun `iniciarSesion con correo vacio muestra error`() = runTest {
        viewModel.iniciarSesion("", "password123")
        advanceUntilIdle()

        val estado = viewModel.estadoAuth.value
        assertThat(estado).isInstanceOf(AuthUIState.Error::class.java)
    }

    @Test
    fun `iniciarSesion con contrasena vacia muestra error`() = runTest {
        viewModel.iniciarSesion("test@example.com", "")
        advanceUntilIdle()

        val estado = viewModel.estadoAuth.value
        assertThat(estado).isInstanceOf(AuthUIState.Error::class.java)
    }

    @Test
    fun `iniciarSesion establece estado Cargando`() = runTest {
        viewModel.iniciarSesion("test@example.com", "password123")
        val estado = viewModel.estadoAuth.value
        assertThat(estado).isInstanceOf(AuthUIState.Cargando::class.java)
    }

    // ==================== INICIAR SESIÓN - CASOS EXITOSOS ====================

    @Test
    fun `iniciarSesion exitoso con usuario local existente actualiza estados`() = runTest {
        val correo = "test@example.com"
        val contrasena = "password123"
        val usuario = Usuario(1L, "Test User", correo, "hash123")

        // Mock UsuarioRepositorio
        mockkConstructor(UsuarioRepositorio::class)
        coEvery {
            anyConstructed<UsuarioRepositorio>().login(correo, contrasena)
        } returns Result.success("LOGIN_OK")

        // Usuario ya existe localmente
        coEvery { usuarioDao.obtenerUsuarioPorCorreo(correo) } returns usuario

        viewModel.iniciarSesion(correo, contrasena)
        advanceUntilIdle()

        val estado = viewModel.estadoAuth.value
        assertThat(estado).isInstanceOf(AuthUIState.Exito::class.java)
        assertThat((estado as AuthUIState.Exito).usuario).isEqualTo(usuario)
        assertThat(viewModel.usuarioActual.value).isEqualTo(usuario)
    }

    @Test
    fun `iniciarSesion exitoso sin usuario local lo crea automaticamente`() = runTest {
        val correo = "nuevo@example.com"
        val contrasena = "password123"
        val idGenerado = 5L
        val usuarioCreado = Usuario(idGenerado, "", correo, "hashcreado")

        mockkConstructor(UsuarioRepositorio::class)
        coEvery {
            anyConstructed<UsuarioRepositorio>().login(correo, contrasena)
        } returns Result.success("LOGIN_OK")

        // Usuario NO existe localmente
        coEvery { usuarioDao.obtenerUsuarioPorCorreo(correo) } returns null

        // Mock inserción y hash
        mockkStatic("com.example.proyectodd.model.data.source.AuthDataSource")
        every { any<String>().hashCode() } returns "hashcreado".hashCode()

        coEvery { usuarioDao.insertarUsuario(any()) } returns idGenerado
        coEvery { usuarioDao.obtenerUsuarioPorId(idGenerado) } returns usuarioCreado

        viewModel.iniciarSesion(correo, contrasena)
        advanceUntilIdle()

        val estado = viewModel.estadoAuth.value
        assertThat(estado).isInstanceOf(AuthUIState.Exito::class.java)
        assertThat(viewModel.usuarioActual.value).isEqualTo(usuarioCreado)

        // Verificar que se insertó el usuario
        coVerify { usuarioDao.insertarUsuario(any()) }
    }

    // ==================== INICIAR SESIÓN - CASOS DE ERROR ====================

    @Test
    fun `iniciarSesion con fallo de API muestra error`() = runTest {
        mockkConstructor(UsuarioRepositorio::class)
        coEvery {
            anyConstructed<UsuarioRepositorio>().login(any(), any())
        } returns Result.failure(Exception("Credenciales inválidas"))

        viewModel.iniciarSesion("test@example.com", "wrongpass")
        advanceUntilIdle()

        val estado = viewModel.estadoAuth.value
        assertThat(estado).isInstanceOf(AuthUIState.Error::class.java)
        assertThat((estado as AuthUIState.Error).mensaje).contains("Credenciales inválidas")
    }

    @Test
    fun `iniciarSesion con excepcion inesperada muestra error generico`() = runTest {
        mockkConstructor(UsuarioRepositorio::class)
        coEvery {
            anyConstructed<UsuarioRepositorio>().login(any(), any())
        } throws RuntimeException("Error de red")

        viewModel.iniciarSesion("test@example.com", "pass123")
        advanceUntilIdle()

        val estado = viewModel.estadoAuth.value
        assertThat(estado).isInstanceOf(AuthUIState.Error::class.java)
        assertThat((estado as AuthUIState.Error).mensaje).contains("Error inesperado")
    }

    // ==================== REGISTRAR USUARIO - CASOS BÁSICOS ====================

    @Test
    fun `registrarUsuario con campos vacios muestra error`() = runTest {
        viewModel.registrarUsuario("", "", "", "")
        advanceUntilIdle()

        val estado = viewModel.estadoAuth.value
        assertThat(estado).isInstanceOf(AuthUIState.Error::class.java)
        assertThat((estado as AuthUIState.Error).mensaje).contains("Completa todos los campos")
    }

    @Test
    fun `registrarUsuario con contrasenas diferentes muestra error`() = runTest {
        viewModel.registrarUsuario("Juan", "juan@example.com", "pass123", "pass456")
        advanceUntilIdle()

        val estado = viewModel.estadoAuth.value
        assertThat(estado).isInstanceOf(AuthUIState.Error::class.java)
        assertThat((estado as AuthUIState.Error).mensaje).contains("no coinciden")
    }

    @Test
    fun `registrarUsuario con nombre vacio muestra error`() = runTest {
        viewModel.registrarUsuario("", "juan@example.com", "pass123", "pass123")
        advanceUntilIdle()

        val estado = viewModel.estadoAuth.value
        assertThat(estado).isInstanceOf(AuthUIState.Error::class.java)
    }

    // ==================== REGISTRAR USUARIO - CASOS EXITOSOS ====================

    @Test
    fun `registrarUsuario exitoso crea usuario local y remoto`() = runTest {
        val nombre = "Juan Pérez"
        val correo = "juan@example.com"
        val contrasena = "password123"
        val idGenerado = 10L
        val usuarioCreado = Usuario(idGenerado, nombre, correo, "hash123")

        mockkConstructor(UsuarioRepositorio::class)
        coEvery {
            anyConstructed<UsuarioRepositorio>().register(nombre, correo, contrasena)
        } returns Result.success("REGISTRO_OK")

        coEvery { usuarioDao.insertarUsuario(any()) } returns idGenerado
        coEvery { usuarioDao.obtenerUsuarioPorId(idGenerado) } returns usuarioCreado

        viewModel.registrarUsuario(nombre, correo, contrasena, contrasena)
        advanceUntilIdle()

        val estado = viewModel.estadoAuth.value
        assertThat(estado).isInstanceOf(AuthUIState.Exito::class.java)
        assertThat((estado as AuthUIState.Exito).usuario).isEqualTo(usuarioCreado)
        assertThat(viewModel.usuarioActual.value).isEqualTo(usuarioCreado)

        coVerify { usuarioDao.insertarUsuario(any()) }
    }

    // ==================== REGISTRAR USUARIO - CASOS DE ERROR ====================

    @Test
    fun `registrarUsuario con fallo de API muestra error`() = runTest {
        mockkConstructor(UsuarioRepositorio::class)
        coEvery {
            anyConstructed<UsuarioRepositorio>().register(any(), any(), any())
        } returns Result.failure(Exception("El email ya está registrado"))

        viewModel.registrarUsuario("Juan", "existente@example.com", "pass123", "pass123")
        advanceUntilIdle()

        val estado = viewModel.estadoAuth.value
        assertThat(estado).isInstanceOf(AuthUIState.Error::class.java)
        assertThat((estado as AuthUIState.Error).mensaje).contains("ya está registrado")
    }

    @Test
    fun `registrarUsuario con excepcion inesperada muestra error generico`() = runTest {
        mockkConstructor(UsuarioRepositorio::class)
        coEvery {
            anyConstructed<UsuarioRepositorio>().register(any(), any(), any())
        } throws RuntimeException("Error de base de datos")

        viewModel.registrarUsuario("Test", "test@example.com", "pass123", "pass123")
        advanceUntilIdle()

        val estado = viewModel.estadoAuth.value
        assertThat(estado).isInstanceOf(AuthUIState.Error::class.java)
        assertThat((estado as AuthUIState.Error).mensaje).contains("Error inesperado")
    }

    // ==================== UTILIDADES ====================

    @Test
    fun `resetearEstado vuelve a Inactivo`() = runTest {
        viewModel.resetearEstado()
        advanceUntilIdle()

        assertThat(viewModel.estadoAuth.value).isInstanceOf(AuthUIState.Inactivo::class.java)
    }

    @Test
    fun `cerrarSesion limpia usuarioActual y resetea estado`() = runTest {
        viewModel.cerrarSesion()
        advanceUntilIdle()

        assertThat(viewModel.usuarioActual.value).isNull()
        assertThat(viewModel.estadoAuth.value).isInstanceOf(AuthUIState.Inactivo::class.java)
    }
}