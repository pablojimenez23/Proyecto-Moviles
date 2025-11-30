package com.example.proyectodd.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.proyectodd.helpers.MainDispatcherRule
import com.example.proyectodd.model.Hechizo
import com.example.proyectodd.repository.ConjurosRepository
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
 * UBICACIÓN: app/src/test/java/com/example/proyectodd/ConjurosViewModelTest.kt
 */
@ExperimentalCoroutinesApi
class ConjurosViewModelTest {

    @get:Rule val instantTaskExecutorRule = InstantTaskExecutorRule()
    @get:Rule val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: ConjurosViewModel

    private val conjurosEjemplo = listOf(
        Hechizo(name = "Fireball", desc = "Bola de fuego explosiva", level_int = 3),
        Hechizo(name = "Magic Missile", desc = "Proyectiles mágicos", level_int = 1),
        Hechizo(name = "Shield", desc = "Escudo mágico", level_int = 1),
        Hechizo(name = "Wish", desc = "El conjuro definitivo", level_int = 9),
        Hechizo(name = "Prestidigitation", desc = "Truco de magia menor", level_int = 0)
    )

    @Before
    fun setup() {
        // Mockear el constructor de ConjurosRepository
        mockkConstructor(ConjurosRepository::class)

        // Configurar comportamiento por defecto
        coEvery {
            anyConstructed<ConjurosRepository>().obtenerConjuros()
        } returns conjurosEjemplo
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `viewModel se inicializa y carga conjuros correctamente`() = runTest {
        coEvery {
            anyConstructed<ConjurosRepository>().obtenerConjuros()
        } returns conjurosEjemplo

        viewModel = ConjurosViewModel()
        advanceUntilIdle()

        assertThat(viewModel.listaConjuros.value).isNotEmpty()
    }

    @Test
    fun `viewModel agrupa conjuros por nivel correctamente`() = runTest {
        coEvery {
            anyConstructed<ConjurosRepository>().obtenerConjuros()
        } returns conjurosEjemplo

        viewModel = ConjurosViewModel()
        advanceUntilIdle()

        val grupos = viewModel.listaConjuros.value
        assertThat(grupos.keys).containsAtLeast(0, 1, 3, 9)

        val nivel0 = grupos[0]
        assertThat(nivel0).isNotNull()
        assertThat(nivel0).hasSize(1)

        val nivel1 = grupos[1]
        assertThat(nivel1).isNotNull()
        assertThat(nivel1).hasSize(2)
    }

    @Test
    fun `viewModel agrupa conjuros de nivel 0 (cantrips)`() = runTest {
        val cantrips = listOf(
            Hechizo(name = "Light", desc = "Crear luz", level_int = 0),
            Hechizo(name = "Mage Hand", desc = "Mano mágica", level_int = 0)
        )
        coEvery {
            anyConstructed<ConjurosRepository>().obtenerConjuros()
        } returns cantrips

        viewModel = ConjurosViewModel()
        advanceUntilIdle()

        val nivel0 = viewModel.listaConjuros.value[0]
        assertThat(nivel0).isNotNull()
        assertThat(nivel0).hasSize(2)
    }

    @Test
    fun `viewModel maneja lista vacia de conjuros`() = runTest {
        coEvery {
            anyConstructed<ConjurosRepository>().obtenerConjuros()
        } returns emptyList()

        viewModel = ConjurosViewModel()
        advanceUntilIdle()

        assertThat(viewModel.listaConjuros.value).isEmpty()
    }

    @Test
    fun `viewModel maneja error en carga sin crashear`() = runTest {
        coEvery {
            anyConstructed<ConjurosRepository>().obtenerConjuros()
        } throws Exception("Error de red")

        viewModel = ConjurosViewModel()
        advanceUntilIdle()

        assertThat(viewModel.listaConjuros.value).isEmpty()
    }

    @Test
    fun `viewModel agrupa correctamente conjuros de nivel 1`() = runTest {
        val nivel1 = listOf(
            Hechizo(name = "Cure Wounds", desc = "Curar heridas", level_int = 1),
            Hechizo(name = "Detect Magic", desc = "Detectar magia", level_int = 1),
            Hechizo(name = "Sleep", desc = "Dormir", level_int = 1)
        )
        coEvery {
            anyConstructed<ConjurosRepository>().obtenerConjuros()
        } returns nivel1

        viewModel = ConjurosViewModel()
        advanceUntilIdle()

        val nivel1Conjuros = viewModel.listaConjuros.value[1]
        assertThat(nivel1Conjuros).isNotNull()
        assertThat(nivel1Conjuros).hasSize(3)
    }

    @Test
    fun `viewModel mantiene el orden de conjuros dentro de cada nivel`() = runTest {
        val conjurosOrdenados = listOf(
            Hechizo(name = "Alpha", desc = "Primer conjuro", level_int = 1),
            Hechizo(name = "Beta", desc = "Segundo conjuro", level_int = 1),
            Hechizo(name = "Gamma", desc = "Tercer conjuro", level_int = 1)
        )
        coEvery {
            anyConstructed<ConjurosRepository>().obtenerConjuros()
        } returns conjurosOrdenados

        viewModel = ConjurosViewModel()
        advanceUntilIdle()

        val nivel1 = viewModel.listaConjuros.value[1]
        assertThat(nivel1).isNotNull()
        assertThat(nivel1?.get(0)?.name).isEqualTo("Alpha")
        assertThat(nivel1?.get(1)?.name).isEqualTo("Beta")
        assertThat(nivel1?.get(2)?.name).isEqualTo("Gamma")
    }
}