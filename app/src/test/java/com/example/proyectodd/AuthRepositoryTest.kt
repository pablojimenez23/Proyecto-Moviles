package com.example.proyectodd.data.repository

import com.example.proyectodd.model.Usuario
import com.example.proyectodd.model.data.repository.AuthRepository
import com.example.proyectodd.model.data.source.AuthDataSource
import com.google.common.truth.Truth.assertThat
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * UBICACIÓN: app/src/test/java/com/example/proyectodd/AuthRepositoryTest.kt
 */
class AuthRepositoryTest {

    private lateinit var dataSource: AuthDataSource
    private lateinit var repository: AuthRepository

    @Before
    fun setup() {
        dataSource = mockk()
        repository = AuthRepository(dataSource)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `registrarUsuario con datos validos retorna Success`() = runTest {
        val nombre = "Juan Pérez"
        val correo = "juan@example.com"
        val contrasena = "password123"
        val contrasenaHash = "hash123"
        val idGenerado = 1L

        coEvery { dataSource.correoExiste(correo) } returns false
        coEvery { dataSource.hashearContrasena(contrasena) } returns contrasenaHash
        coEvery { dataSource.insertarUsuario(any()) } returns idGenerado

        val resultado = repository.registrarUsuario(nombre, correo, contrasena)

        assertThat(resultado.isSuccess).isTrue()
        resultado.onSuccess { usuario ->
            assertThat(usuario.id).isEqualTo(idGenerado)
            assertThat(usuario.nombre).isEqualTo(nombre)
            assertThat(usuario.correo).isEqualTo(correo)
            assertThat(usuario.contrasenaHash).isEqualTo(contrasenaHash)
        }
    }

    @Test
    fun `registrarUsuario con correo existente retorna Failure`() = runTest {
        val correo = "existente@example.com"
        coEvery { dataSource.correoExiste(correo) } returns true

        val resultado = repository.registrarUsuario("Nombre", correo, "password")

        assertThat(resultado.isFailure).isTrue()
        resultado.onFailure { exception ->
            assertThat(exception.message).contains("ya está registrado")
        }
    }

    @Test
    fun `registrarUsuario con excepcion retorna Failure`() = runTest {
        val nombre = "Test"
        val correo = "test@example.com"
        val contrasena = "password123"

        coEvery { dataSource.correoExiste(correo) } returns false
        coEvery { dataSource.hashearContrasena(contrasena) } returns "hash"
        coEvery { dataSource.insertarUsuario(any()) } throws Exception("Error de BD")

        val resultado = repository.registrarUsuario(nombre, correo, contrasena)

        assertThat(resultado.isFailure).isTrue()
    }

    @Test
    fun `iniciarSesion con credenciales correctas retorna Success`() = runTest {
        val correo = "test@example.com"
        val contrasena = "password123"
        val contrasenaHash = "hash123"

        val usuarioExistente = Usuario(
            id = 1L,
            nombre = "Test User",
            correo = correo,
            contrasenaHash = contrasenaHash
        )

        coEvery { dataSource.obtenerUsuarioPorCorreo(correo) } returns usuarioExistente
        coEvery { dataSource.hashearContrasena(contrasena) } returns contrasenaHash

        val resultado = repository.iniciarSesion(correo, contrasena)

        assertThat(resultado.isSuccess).isTrue()
    }

    @Test
    fun `iniciarSesion con usuario no encontrado retorna Failure`() = runTest {
        val correo = "noexiste@example.com"
        coEvery { dataSource.obtenerUsuarioPorCorreo(correo) } returns null

        val resultado = repository.iniciarSesion(correo, "password")

        assertThat(resultado.isFailure).isTrue()
    }

    @Test
    fun `iniciarSesion con contrasena incorrecta retorna Failure`() = runTest {
        val correo = "test@example.com"
        val usuarioExistente = Usuario(
            id = 1L,
            nombre = "Test User",
            correo = correo,
            contrasenaHash = "hash_correcto"
        )

        coEvery { dataSource.obtenerUsuarioPorCorreo(correo) } returns usuarioExistente
        coEvery { dataSource.hashearContrasena(any()) } returns "hash_incorrecto"

        val resultado = repository.iniciarSesion(correo, "wrongpass")

        assertThat(resultado.isFailure).isTrue()
    }

    @Test
    fun `iniciarSesion con excepcion retorna Failure`() = runTest {
        coEvery { dataSource.obtenerUsuarioPorCorreo(any()) } throws Exception("Error de red")

        val resultado = repository.iniciarSesion("test@example.com", "pass")

        assertThat(resultado.isFailure).isTrue()
    }
}