package com.example.proyectodd.data.repository

import com.example.proyectodd.model.Hechizo
import com.example.proyectodd.model.RespuestaConjuros
import com.example.proyectodd.repository.ConjurosRepository
import com.example.proyectodd.viewmodel.retrofit.ConjurosApi
import com.example.proyectodd.viewmodel.retrofit.RetrofitConjuros
import com.google.common.truth.Truth.assertThat
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * UBICACIÓN: app/src/test/java/com/example/proyectodd/ConjurosRepositoryTest.kt
 */
class ConjurosRepositoryTest {

    private lateinit var api: ConjurosApi
    private lateinit var repository: ConjurosRepository

    private val conjurosEjemplo = listOf(
        Hechizo("Fireball", "Lanza una bola de fuego", 3),
        Hechizo("Magic Missile", "Proyectiles mágicos", 1),
        Hechizo("Wish", "El conjuro más poderoso", 9)
    )

    @Before
    fun setup() {
        api = mockk()
        mockkObject(RetrofitConjuros)
        every { RetrofitConjuros.api } returns api
        repository = ConjurosRepository()
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `obtenerConjuros retorna lista de hechizos correctamente`() = runTest {
        val respuesta = RespuestaConjuros(results = conjurosEjemplo)
        coEvery { api.getConjuros() } returns respuesta

        val resultado = repository.obtenerConjuros()

        assertThat(resultado).hasSize(3)
        assertThat(resultado).containsExactlyElementsIn(conjurosEjemplo)
    }

    @Test
    fun `obtenerConjuros retorna lista vacia cuando no hay resultados`() = runTest {
        val respuestaVacia = RespuestaConjuros(results = emptyList())
        coEvery { api.getConjuros() } returns respuestaVacia

        val resultado = repository.obtenerConjuros()

        assertThat(resultado).isEmpty()
    }

    @Test
    fun `obtenerConjuros maneja diferentes niveles de conjuros`() = runTest {
        val conjurosVariados = listOf(
            Hechizo("Cantrip", "Truco de magia", 0),
            Hechizo("Cure Wounds", "Curar heridas", 1),
            Hechizo("Meteor Swarm", "Lluvia de meteoritos", 9)
        )
        val respuesta = RespuestaConjuros(results = conjurosVariados)
        coEvery { api.getConjuros() } returns respuesta

        val resultado = repository.obtenerConjuros()

        assertThat(resultado[0].level_int).isEqualTo(0)
        assertThat(resultado[1].level_int).isEqualTo(1)
        assertThat(resultado[2].level_int).isEqualTo(9)
    }

    @Test
    fun `obtenerConjuros propaga excepcion de red`() = runTest {
        coEvery { api.getConjuros() } throws Exception("Error de conexión")

        try {
            repository.obtenerConjuros()
            assert(false) { "Debería lanzar excepción" }
        } catch (e: Exception) {
            assertThat(e.message).isEqualTo("Error de conexión")
        }
    }
}