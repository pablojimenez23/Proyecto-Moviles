package com.example.proyectodd.data.repository

import com.example.proyectodd.model.Personaje
import com.example.proyectodd.model.data.local.dao.PersonajeDao
import com.example.proyectodd.model.data.repository.PersonajeRepository
import com.google.common.truth.Truth.assertThat
import io.mockk.*
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * UBICACIÓN: app/src/test/java/com/example/proyectodd/PersonajeRepositoryTest.kt
 */
class PersonajeRepositoryTest {

    private lateinit var dao: PersonajeDao
    private lateinit var repository: PersonajeRepository

    private val personajeEjemplo = Personaje(
        id = 1L, usuarioId = 100L, nombre = "Gandalf", clase = "Mago",
        raza = "Humano", trasfondo = "Sabio", alineamiento = "Neutral Bueno",
        nivel = 10, hp = 50, ac = 15, iniciativa = 2, velocidad = 30,
        competencia = 4, percepcionPasiva = 14, str = 10, dex = 14,
        con = 12, intg = 18, sab = 16, car = 14, saveDC = 16,
        playerName = "Juan", portraitUri = null
    )

    @Before
    fun setup() {
        dao = mockk(relaxed = true)
        repository = PersonajeRepository(dao)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `observeByUsuario retorna flow de personajes del usuario`() = runTest {
        val uid = 100L
        val listaPersonajes = listOf(personajeEjemplo)
        every { dao.observeByUsuario(uid) } returns flowOf(listaPersonajes)

        val flow = repository.observeByUsuario(uid)
        flow.collect { personajes ->
            assertThat(personajes).hasSize(1)
            assertThat(personajes[0]).isEqualTo(personajeEjemplo)
        }
    }

    @Test
    fun `observeByUsuario retorna lista vacia cuando no hay personajes`() = runTest {
        val uid = 100L
        every { dao.observeByUsuario(uid) } returns flowOf(emptyList())

        val flow = repository.observeByUsuario(uid)
        flow.collect { personajes ->
            assertThat(personajes).isEmpty()
        }
    }

    @Test
    fun `getById retorna personaje cuando existe`() = runTest {
        val id = 1L
        coEvery { dao.getById(id) } returns personajeEjemplo

        val resultado = repository.getById(id)

        assertThat(resultado).isEqualTo(personajeEjemplo)
    }

    @Test
    fun `getById retorna null cuando no existe`() = runTest {
        val id = 999L
        coEvery { dao.getById(id) } returns null

        val resultado = repository.getById(id)

        assertThat(resultado).isNull()
    }

    @Test
    fun `insert guarda personaje y retorna id generado`() = runTest {
        val idGenerado = 5L
        val personajeNuevo = personajeEjemplo.copy(id = 0L)
        coEvery { dao.insert(personajeNuevo) } returns idGenerado

        val resultado = repository.insert(personajeNuevo)

        assertThat(resultado).isEqualTo(idGenerado)
    }

    @Test
    fun `update actualiza personaje existente`() = runTest {
        val personajeActualizado = personajeEjemplo.copy(nivel = 11, hp = 60)
        coEvery { dao.update(personajeActualizado) } just Runs

        repository.update(personajeActualizado)

        coVerify(exactly = 1) { dao.update(personajeActualizado) }
    }

    @Test
    fun `delete elimina personaje`() = runTest {
        coEvery { dao.delete(personajeEjemplo) } just Runs

        repository.delete(personajeEjemplo)

        coVerify(exactly = 1) { dao.delete(personajeEjemplo) }
    }

    @Test
    fun `delete con personaje diferente llama al DAO correctamente`() = runTest {
        val otroPersonaje = personajeEjemplo.copy(id = 2L, nombre = "Legolas")
        coEvery { dao.delete(otroPersonaje) } just Runs

        repository.delete(otroPersonaje)

        coVerify(exactly = 1) { dao.delete(otroPersonaje) }
    }
}