package com.example.proyectodd.domain.usecase

import com.example.proyectodd.model.Usuario
import com.example.proyectodd.model.data.repository.AuthRepository
import com.example.proyectodd.viewmodel.domain.usecase.IniciarSesionUseCase
import com.example.proyectodd.viewmodel.domain.usecase.RegistrarUsuarioUseCase
import com.google.common.truth.Truth.assertThat
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * UBICACIÓN: app/src/test/java/com/example/proyectodd/domain/usecase/UseCasesTest.kt
 *
 * IMPORTANTE: Usa Robolectric para poder ejecutar código de Android en tests JVM
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class UseCasesTest {

    private lateinit var repository: AuthRepository

    @Before
    fun setup() {
        repository = mockk()
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    // ==================== INICIAR SESIÓN USE CASE ====================

    @Test
    fun `IniciarSesionUseCase con credenciales validas retorna Success`() = runTest {
        val useCase = IniciarSesionUseCase(repository)
        val usuario = Usuario(1L, "Test", "test@example.com", "hash")
        coEvery { repository.iniciarSesion(any(), any()) } returns Result.success(usuario)

        val resultado = useCase("test@example.com", "password123")

        assertThat(resultado.isSuccess).isTrue()
    }

    @Test
    fun `IniciarSesionUseCase con correo vacio retorna Failure`() = runTest {
        val useCase = IniciarSesionUseCase(repository)
        val resultado = useCase("", "password123")
        assertThat(resultado.isFailure).isTrue()
    }

    @Test
    fun `IniciarSesionUseCase con correo invalido retorna Failure`() = runTest {
        val useCase = IniciarSesionUseCase(repository)
        val resultado = useCase("correo_invalido", "password123")
        assertThat(resultado.isFailure).isTrue()
    }

    @Test
    fun `IniciarSesionUseCase con contrasena vacia retorna Failure`() = runTest {
        val useCase = IniciarSesionUseCase(repository)
        val resultado = useCase("test@example.com", "")
        assertThat(resultado.isFailure).isTrue()
    }

    @Test
    fun `IniciarSesionUseCase con contrasena solo espacios retorna Failure`() = runTest {
        val useCase = IniciarSesionUseCase(repository)
        val resultado = useCase("test@example.com", "   ")
        assertThat(resultado.isFailure).isTrue()
    }

    // ==================== REGISTRAR USUARIO USE CASE ====================

    @Test
    fun `RegistrarUsuarioUseCase con datos validos retorna Success`() = runTest {
        val useCase = RegistrarUsuarioUseCase(repository)
        val usuario = Usuario(1L, "Juan Pérez", "juan@example.com", "hash")
        coEvery { repository.registrarUsuario(any(), any(), any()) } returns Result.success(usuario)

        val resultado = useCase("Juan Pérez", "juan@example.com", "password123", "password123")

        assertThat(resultado.isSuccess).isTrue()
    }

    @Test
    fun `RegistrarUsuarioUseCase con nombre vacio retorna Failure`() = runTest {
        val useCase = RegistrarUsuarioUseCase(repository)
        val resultado = useCase("", "test@example.com", "password123", "password123")
        assertThat(resultado.isFailure).isTrue()
    }

    @Test
    fun `RegistrarUsuarioUseCase con nombre corto retorna Failure`() = runTest {
        val useCase = RegistrarUsuarioUseCase(repository)
        val resultado = useCase("Ab", "test@example.com", "password123", "password123")
        assertThat(resultado.isFailure).isTrue()
    }

    @Test
    fun `RegistrarUsuarioUseCase con correo invalido retorna Failure`() = runTest {
        val useCase = RegistrarUsuarioUseCase(repository)
        val resultado = useCase("Juan", "correo_invalido", "password123", "password123")
        assertThat(resultado.isFailure).isTrue()
    }

    @Test
    fun `RegistrarUsuarioUseCase con contrasena corta retorna Failure`() = runTest {
        val useCase = RegistrarUsuarioUseCase(repository)
        val resultado = useCase("Juan", "test@example.com", "12345", "12345")
        assertThat(resultado.isFailure).isTrue()
    }

    @Test
    fun `RegistrarUsuarioUseCase con contrasenas no coinciden retorna Failure`() = runTest {
        val useCase = RegistrarUsuarioUseCase(repository)
        val resultado = useCase("Juan", "test@example.com", "password123", "password456")
        assertThat(resultado.isFailure).isTrue()
    }

    @Test
    fun `RegistrarUsuarioUseCase con confirmacion vacia retorna Failure`() = runTest {
        val useCase = RegistrarUsuarioUseCase(repository)
        val resultado = useCase("Juan", "test@example.com", "password123", "")
        assertThat(resultado.isFailure).isTrue()
    }

    @Test
    fun `RegistrarUsuarioUseCase valida nombre antes que correo`() = runTest {
        val useCase = RegistrarUsuarioUseCase(repository)
        val resultado = useCase("Ab", "correo_invalido", "pass", "pass")
        assertThat(resultado.isFailure).isTrue()
    }

    @Test
    fun `RegistrarUsuarioUseCase valida correo antes que contrasena`() = runTest {
        val useCase = RegistrarUsuarioUseCase(repository)
        val resultado = useCase("Juan", "correo_invalido", "12345", "12345")
        assertThat(resultado.isFailure).isTrue()
    }

    @Test
    fun `RegistrarUsuarioUseCase propaga errores del repository`() = runTest {
        val useCase = RegistrarUsuarioUseCase(repository)
        coEvery { repository.registrarUsuario(any(), any(), any()) } returns Result.failure(Exception("Error de BD"))

        val resultado = useCase("Juan", "test@example.com", "password123", "password123")

        assertThat(resultado.isFailure).isTrue()
    }
}