package com.example.proyectodd.data.source

import com.example.proyectodd.model.Usuario
import com.example.proyectodd.model.data.local.dao.UsuarioDao
import com.example.proyectodd.model.data.source.AuthDataSource
import com.google.common.truth.Truth.assertThat
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test


class AuthDataSourceTest {

    private lateinit var usuarioDao: UsuarioDao
    private lateinit var authDataSource: AuthDataSource

    @Before
    fun setup() {
        usuarioDao = mockk()
        authDataSource = AuthDataSource(usuarioDao)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `hashearContrasena genera hash SHA-256 correcto`() {
        val contrasena = "password123"
        val hash = authDataSource.hashearContrasena(contrasena)
        val hashEsperado = "ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f"
        assertThat(hash).isEqualTo(hashEsperado)
    }

    @Test
    fun `hashearContrasena con misma entrada genera mismo hash`() {
        val contrasena = "test123"
        val hash1 = authDataSource.hashearContrasena(contrasena)
        val hash2 = authDataSource.hashearContrasena(contrasena)
        assertThat(hash1).isEqualTo(hash2)
    }

    @Test
    fun `hashearContrasena con diferentes entradas genera diferentes hashes`() {
        val hash1 = authDataSource.hashearContrasena("password1")
        val hash2 = authDataSource.hashearContrasena("password2")
        assertThat(hash1).isNotEqualTo(hash2)
    }

    @Test
    fun `hashearContrasena es case sensitive`() {
        val hash1 = authDataSource.hashearContrasena("Password")
        val hash2 = authDataSource.hashearContrasena("password")
        assertThat(hash1).isNotEqualTo(hash2)
    }

    @Test
    fun `hashearContrasena con string vacio genera hash`() {
        val hash = authDataSource.hashearContrasena("")
        val hashEsperado = "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855"
        assertThat(hash).isEqualTo(hashEsperado)
    }

    @Test
    fun `insertarUsuario llama al DAO correctamente`() = runTest {
        val usuario = Usuario(
            nombre = "Test User",
            correo = "test@example.com",
            contrasenaHash = "hash123"
        )
        val idEsperado = 1L

        coEvery { usuarioDao.insertarUsuario(usuario) } returns idEsperado

        val resultado = authDataSource.insertarUsuario(usuario)

        assertThat(resultado).isEqualTo(idEsperado)
        coVerify(exactly = 1) { usuarioDao.insertarUsuario(usuario) }
    }

    @Test
    fun `obtenerUsuarioPorCorreo retorna usuario cuando existe`() = runTest {
        val correo = "test@example.com"
        val usuarioEsperado = Usuario(
            id = 1L,
            nombre = "Test User",
            correo = correo,
            contrasenaHash = "hash123"
        )

        coEvery { usuarioDao.obtenerUsuarioPorCorreo(correo) } returns usuarioEsperado

        val resultado = authDataSource.obtenerUsuarioPorCorreo(correo)

        assertThat(resultado).isEqualTo(usuarioEsperado)
        coVerify(exactly = 1) { usuarioDao.obtenerUsuarioPorCorreo(correo) }
    }

    @Test
    fun `obtenerUsuarioPorCorreo retorna null cuando no existe`() = runTest {
        val correo = "noexiste@example.com"
        coEvery { usuarioDao.obtenerUsuarioPorCorreo(correo) } returns null
        val resultado = authDataSource.obtenerUsuarioPorCorreo(correo)
        assertThat(resultado).isNull()
    }

    @Test
    fun `correoExiste retorna true cuando el correo esta registrado`() = runTest {
        val correo = "existe@example.com"
        coEvery { usuarioDao.correoExiste(correo) } returns 1
        val resultado = authDataSource.correoExiste(correo)
        assertThat(resultado).isTrue()
    }

    @Test
    fun `correoExiste retorna false cuando el correo no esta registrado`() = runTest {
        val correo = "noexiste@example.com"
        coEvery { usuarioDao.correoExiste(correo) } returns 0
        val resultado = authDataSource.correoExiste(correo)
        assertThat(resultado).isFalse()
    }
}