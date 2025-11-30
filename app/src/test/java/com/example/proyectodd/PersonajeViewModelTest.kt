package com.example.proyectodd.viewmodel

import android.net.Uri
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.proyectodd.helpers.MainDispatcherRule
import com.example.proyectodd.model.Personaje
import com.example.proyectodd.model.Usuario
import com.example.proyectodd.model.data.repository.PersonajeRepository
import com.google.common.truth.Truth.assertThat
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * UBICACIÓN: app/src/test/java/com/example/proyectodd/viewmodel/PersonajeViewModelTest.kt
 */
@ExperimentalCoroutinesApi
class PersonajeViewModelTest {

    @get:Rule val instantTaskExecutorRule = InstantTaskExecutorRule()
    @get:Rule val mainDispatcherRule = MainDispatcherRule()

    private lateinit var repository: PersonajeRepository
    private lateinit var authViewModel: AuthViewModel
    private lateinit var viewModel: PersonajeViewModel

    private val usuarioEjemplo = Usuario(100L, "Test User", "test@example.com", "hash123")
    private val personajeEjemplo = Personaje(
        id = 1L, usuarioId = 100L, nombre = "Aragorn", clase = "Guerrero",
        raza = "Humano", trasfondo = "Noble", alineamiento = "Neutral Bueno",
        nivel = 5, hp = 45, ac = 18, iniciativa = 3, velocidad = 30,
        competencia = 3, percepcionPasiva = 12, str = 16, dex = 14,
        con = 15, intg = 10, sab = 12, car = 14, saveDC = 13,
        playerName = "Juan", portraitUri = null
    )

    @Before
    fun setup() {
        repository = mockk(relaxed = true)
        authViewModel = mockk(relaxed = true)
        every { authViewModel.usuarioActual } returns MutableStateFlow(usuarioEjemplo)
        every { repository.observeByUsuario(any()) } returns flowOf(listOf(personajeEjemplo))
        viewModel = PersonajeViewModel(repository, authViewModel)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    // ==================== INICIALIZACIÓN ====================

    @Test
    fun `viewModel se inicializa correctamente con usuario actual`() {
        assertThat(viewModel.usuarioActual).isEqualTo(usuarioEjemplo)
    }

    @Test
    fun `viewModel carga personajes del usuario al inicializar`() = runTest {
        advanceUntilIdle()
        assertThat(viewModel.lista.value).hasSize(1)
        assertThat(viewModel.lista.value[0]).isEqualTo(personajeEjemplo)
    }

    // ==================== OPERACIONES CRUD ====================

    @Test
    fun `nuevo crea personaje vacio con usuarioId correcto`() {
        viewModel.nuevo()
        assertThat(viewModel.estado.value.id).isEqualTo(0L)
        assertThat(viewModel.estado.value.usuarioId).isEqualTo(100L)
        assertThat(viewModel.estado.value.nombre).isEmpty()
    }

    @Test
    fun `cargar obtiene personaje por id y actualiza estado`() = runTest {
        coEvery { repository.getById(1L) } returns personajeEjemplo
        viewModel.cargar(1L)
        advanceUntilIdle()
        assertThat(viewModel.estado.value).isEqualTo(personajeEjemplo)
    }

    @Test
    fun `cargar con id inexistente no cambia estado`() = runTest {
        val estadoInicial = viewModel.estado.value
        coEvery { repository.getById(999L) } returns null
        viewModel.cargar(999L)
        advanceUntilIdle()
        assertThat(viewModel.estado.value).isEqualTo(estadoInicial)
    }

    @Test
    fun `guardar inserta nuevo personaje cuando id es 0`() = runTest {
        viewModel.nuevo()
        var callbackLlamado = false
        coEvery { repository.insert(any()) } returns 5L
        viewModel.guardar { callbackLlamado = true }
        advanceUntilIdle()
        assertThat(callbackLlamado).isTrue()
        coVerify { repository.insert(any()) }
    }

    @Test
    fun `guardar actualiza personaje existente cuando id mayor a 0`() = runTest {
        coEvery { repository.getById(1L) } returns personajeEjemplo
        viewModel.cargar(1L)
        advanceUntilIdle()
        var callbackLlamado = false
        viewModel.guardar { callbackLlamado = true }
        advanceUntilIdle()
        assertThat(callbackLlamado).isTrue()
        coVerify { repository.update(any()) }
    }

    @Test
    fun `eliminar llama al repository y ejecuta callback`() = runTest {
        var callbackLlamado = false
        coEvery { repository.delete(personajeEjemplo) } just Runs
        viewModel.eliminar(personajeEjemplo) { callbackLlamado = true }
        advanceUntilIdle()
        assertThat(callbackLlamado).isTrue()
        coVerify { repository.delete(personajeEjemplo) }
    }

    @Test
    fun `eliminarActual elimina personaje del estado actual`() = runTest {
        coEvery { repository.getById(1L) } returns personajeEjemplo
        viewModel.cargar(1L)
        advanceUntilIdle()

        coEvery { repository.delete(any()) } just Runs

        var callbackLlamado = false
        viewModel.eliminarActual { callbackLlamado = true }
        advanceUntilIdle()

        assertThat(callbackLlamado).isTrue()
        coVerify(exactly = 1) { repository.delete(personajeEjemplo) }
    }

    // ==================== SETTERS - INFORMACIÓN BÁSICA ====================

    @Test
    fun `setNombre actualiza nombre correctamente`() {
        viewModel.setNombre("Legolas")
        assertThat(viewModel.estado.value.nombre).isEqualTo("Legolas")
    }

    @Test
    fun `setNombre con string vacio actualiza correctamente`() {
        viewModel.setNombre("")
        assertThat(viewModel.estado.value.nombre).isEmpty()
    }

    @Test
    fun `setRaza actualiza raza correctamente`() {
        viewModel.setRaza("Elfo")
        assertThat(viewModel.estado.value.raza).isEqualTo("Elfo")
    }

    @Test
    fun `setClase actualiza clase correctamente`() {
        viewModel.setClase("Mago")
        assertThat(viewModel.estado.value.clase).isEqualTo("Mago")
    }

    @Test
    fun `setTrasfondo actualiza trasfondo correctamente`() {
        viewModel.setTrasfondo("Criminal")
        assertThat(viewModel.estado.value.trasfondo).isEqualTo("Criminal")
    }

    @Test
    fun `setAlineamiento actualiza alineamiento correctamente`() {
        viewModel.setAlineamiento("Caótico Neutral")
        assertThat(viewModel.estado.value.alineamiento).isEqualTo("Caótico Neutral")
    }

    @Test
    fun `setPlayerName actualiza nombre del jugador`() {
        viewModel.setPlayerName("Carlos")
        assertThat(viewModel.estado.value.playerName).isEqualTo("Carlos")
    }

    // ==================== SETTERS - ATRIBUTOS NUMÉRICOS ====================

    @Test
    fun `setNivel actualiza nivel correctamente`() {
        viewModel.setNivel("15")
        assertThat(viewModel.estado.value.nivel).isEqualTo(15)
    }

    @Test
    fun `setNivel con texto invalido establece 0`() {
        viewModel.setNivel("texto")
        assertThat(viewModel.estado.value.nivel).isEqualTo(0)
    }

    @Test
    fun `setNivel con string vacio establece 0`() {
        viewModel.setNivel("")
        assertThat(viewModel.estado.value.nivel).isEqualTo(0)
    }

    @Test
    fun `setHP actualiza puntos de vida correctamente`() {
        viewModel.setHP("50")
        assertThat(viewModel.estado.value.hp).isEqualTo(50)
    }

    @Test
    fun `setHP con valor invalido establece 0`() {
        viewModel.setHP("abc")
        assertThat(viewModel.estado.value.hp).isEqualTo(0)
    }

    @Test
    fun `setAC actualiza armadura correctamente`() {
        viewModel.setAC("20")
        assertThat(viewModel.estado.value.ac).isEqualTo(20)
    }

    @Test
    fun `setIniciativa actualiza iniciativa correctamente`() {
        viewModel.setIniciativa("5")
        assertThat(viewModel.estado.value.iniciativa).isEqualTo(5)
    }

    @Test
    fun `setVelocidad actualiza velocidad correctamente`() {
        viewModel.setVelocidad("35")
        assertThat(viewModel.estado.value.velocidad).isEqualTo(35)
    }

    @Test
    fun `setPercepcionPasiva actualiza percepcion pasiva correctamente`() {
        viewModel.setPercepcionPasiva("18")
        assertThat(viewModel.estado.value.percepcionPasiva).isEqualTo(18)
    }

    @Test
    fun `setSaveDC actualiza DC de salvacion correctamente`() {
        viewModel.setSaveDC("15")
        assertThat(viewModel.estado.value.saveDC).isEqualTo(15)
    }

    // ==================== SETTERS - ATRIBUTOS (STR, DEX, CON, INT, WIS, CHA) ====================

    @Test
    fun `setStr actualiza fuerza correctamente`() {
        viewModel.setStr("18")
        assertThat(viewModel.estado.value.str).isEqualTo(18)
    }

    @Test
    fun `setStr con valor invalido establece 0`() {
        viewModel.setStr("invalid")
        assertThat(viewModel.estado.value.str).isEqualTo(0)
    }

    @Test
    fun `setDex actualiza destreza correctamente`() {
        viewModel.setDex("16")
        assertThat(viewModel.estado.value.dex).isEqualTo(16)
    }

    @Test
    fun `setCon actualiza constitucion correctamente`() {
        viewModel.setCon("14")
        assertThat(viewModel.estado.value.con).isEqualTo(14)
    }

    @Test
    fun `setIntg actualiza inteligencia correctamente`() {
        viewModel.setIntg("12")
        assertThat(viewModel.estado.value.intg).isEqualTo(12)
    }

    @Test
    fun `setSab actualiza sabiduria correctamente`() {
        viewModel.setSab("15")
        assertThat(viewModel.estado.value.sab).isEqualTo(15)
    }

    @Test
    fun `setCar actualiza carisma correctamente`() {
        viewModel.setCar("10")
        assertThat(viewModel.estado.value.car).isEqualTo(10)
    }

    // ==================== SETTERS - PORTRAIT ====================

    @Test
    fun `setPortrait actualiza URI del retrato`() {
        val uri = mockk<Uri>()
        every { uri.toString() } returns "content://test/image.jpg"
        viewModel.setPortrait(uri)
        assertThat(viewModel.estado.value.portraitUri).isEqualTo("content://test/image.jpg")
    }

    @Test
    fun `setPortrait con null limpia el retrato`() {
        viewModel.setPortrait(null)
        assertThat(viewModel.estado.value.portraitUri).isNull()
    }

    // ==================== TESTS ADICIONALES ====================

    @Test
    fun `multiples actualizaciones funcionan correctamente`() {
        viewModel.nuevo()
        viewModel.setNombre("Gandalf")
        viewModel.setClase("Mago")
        viewModel.setNivel("20")
        viewModel.setHP("100")

        val estado = viewModel.estado.value
        assertThat(estado.nombre).isEqualTo("Gandalf")
        assertThat(estado.clase).isEqualTo("Mago")
        assertThat(estado.nivel).isEqualTo(20)
        assertThat(estado.hp).isEqualTo(100)
    }
}