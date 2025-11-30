package com.example.proyectodd.viewmodel

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.proyectodd.helpers.MainDispatcherRule
import com.example.proyectodd.model.data.local.dao.PersonajeDao
import com.example.proyectodd.model.data.local.database.AppDatabase
import com.example.proyectodd.model.data.repository.PersonajeRepository
import com.google.common.truth.Truth.assertThat
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.robolectric.annotation.Config

@Config(sdk = [34])
@ExperimentalCoroutinesApi
class PersonajeViewModelFactoryTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var repository: PersonajeRepository
    private lateinit var authViewModel: AuthViewModel
    private lateinit var factory: PersonajeViewModelFactory

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        repository = mockk(relaxed = true)
        authViewModel = mockk(relaxed = true)
        factory = PersonajeViewModelFactory(repository, authViewModel)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    @Test
    fun `create con PersonajeViewModel retorna instancia correcta`() {
        val viewModel = factory.create(PersonajeViewModel::class.java)

        assertThat(viewModel).isInstanceOf(PersonajeViewModel::class.java)
    }

    @Test
    fun `create con clase incorrecta lanza excepcion`() {
        try {
            factory.create(AuthViewModel::class.java)
            assert(false) { "Debería lanzar IllegalArgumentException" }
        } catch (e: IllegalArgumentException) {
            assertThat(e.message).contains("Unknown ViewModel class")
        }
    }

    @Test
    fun `from crea factory con dependencias correctas`() {
        // Crear mocks separados para context y applicationContext
        val mockContext = mockk<Context>(relaxed = true)
        val mockApplicationContext = mockk<Context>(relaxed = true)
        val authVM = mockk<AuthViewModel>(relaxed = true)
        val database = mockk<AppDatabase>(relaxed = true)
        val dao = mockk<PersonajeDao>(relaxed = true)

        // Configurar el applicationContext PRIMERO
        every { mockContext.applicationContext } returns mockApplicationContext

        // Mock del companion object de AppDatabase
        mockkObject(AppDatabase.Companion)
        every {
            AppDatabase.obtenerBaseDatos(mockApplicationContext)
        } returns database

        every { database.personajeDao() } returns dao

        val factoryCreated = PersonajeViewModelFactory.from(mockContext, authVM)

        // Verificaciones
        assertThat(factoryCreated).isNotNull()
        assertThat(factoryCreated).isInstanceOf(PersonajeViewModelFactory::class.java)

        // Verificar que se llamaron los métodos correctos
        verify { mockContext.applicationContext }
        verify { AppDatabase.obtenerBaseDatos(mockApplicationContext) }
        verify { database.personajeDao() }
    }
}