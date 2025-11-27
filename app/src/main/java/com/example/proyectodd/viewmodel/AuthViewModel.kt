package com.example.proyectodd.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectodd.model.data.local.database.AppDatabase
import com.example.proyectodd.model.Usuario
import com.example.proyectodd.model.data.repository.AuthRepository
import com.example.proyectodd.model.data.repository.UsuarioRepositorio
import com.example.proyectodd.model.data.source.AuthDataSource
import com.example.proyectodd.viewmodel.domain.usecase.IniciarSesionUseCase
import com.example.proyectodd.viewmodel.domain.usecase.RegistrarUsuarioUseCase
import com.example.proyectodd.viewmodel.state.AuthUIState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.obtenerBaseDatos(application)
    private val dataSource = AuthDataSource(database.usuarioDao())
    private val repository = AuthRepository(dataSource)


    private val apiRepository = UsuarioRepositorio()


    private val _estadoAuth = MutableStateFlow<AuthUIState>(AuthUIState.Inactivo)
    val estadoAuth: StateFlow<AuthUIState> = _estadoAuth.asStateFlow()

    private val _usuarioActual = MutableStateFlow<Usuario?>(null)
    val usuarioActual: StateFlow<Usuario?> = _usuarioActual.asStateFlow()

    fun iniciarSesion(correo: String, contrasena: String) {
        viewModelScope.launch {
            try {
                _estadoAuth.value = AuthUIState.Cargando


                if (correo.isEmpty() || contrasena.isEmpty()) {
                    _estadoAuth.value = AuthUIState.Error("Completa todos los campos.")
                    return@launch
                }


                val resultado = apiRepository.login(correo, contrasena)


                withContext(Dispatchers.Main) {

                    resultado.onSuccess { mensaje ->
                        // Éxito: El servidor devolvió 200 OK.
                        // Nota: En un proyecto real, el servidor devolvería el Usuario o un Token JWT aquí.
                        val usuarioLogueado = Usuario(id = 0, nombre = "", correo = correo, contrasena = "")

                        _usuarioActual.value = usuarioLogueado
                        _estadoAuth.value = AuthUIState.Exito(usuarioLogueado)
                    }

                    resultado.onFailure { exception ->
                        // Fallo: Error 401, 409, 500, o de conexión.
                        _estadoAuth.value = AuthUIState.Error(
                            // El mensaje ya viene del errorBody del Repositorio (ej: "Credenciales inválidas.")
                            exception.message ?: "Error de autenticación."
                        )
                    }
                }
            } catch (e: Exception) {

                _estadoAuth.value = AuthUIState.Error("Error inesperado: ${e.message}")
            } finally {

                if (_estadoAuth.value == AuthUIState.Cargando) {
                    _estadoAuth.value =
                        AuthUIState.Error("Tiempo de espera agotado o error de red.")
                }
            }
        }
    }
    fun registrarUsuario(
        nombre: String,
        correo: String,
        contrasena: String,
        confirmarContrasena: String
    ) {
        viewModelScope.launch {
            try {
                // Ponemos el estado en cargando
                _estadoAuth.value = AuthUIState.Cargando


                if (nombre.isEmpty() || correo.isEmpty() || contrasena.isEmpty() || confirmarContrasena.isEmpty()) {
                    _estadoAuth.value = AuthUIState.Error("Completa todos los campos.")
                    return@launch
                }
                if (contrasena != confirmarContrasena) {
                    _estadoAuth.value = AuthUIState.Error("Las contraseñas no coinciden.")
                    return@launch
                }


                val resultado = apiRepository.register(nombre, correo, contrasena)


                resultado.onSuccess { mensaje ->

                    val usuarioExito = Usuario(id = 0, nombre = nombre, correo = correo, contrasena = "")
                    _estadoAuth.value = AuthUIState.Exito(usuarioExito)
                }

                resultado.onFailure { exception ->
                    // Fallo: Muestra el mensaje de error de la API (409 Conflict, 500, o Timeout)
                    _estadoAuth.value = AuthUIState.Error(
                        exception.message ?: "Error desconocido en el registro."
                    )
                }

            } catch (e: Exception) {
                // Captura errores inesperados de Kotlin
                _estadoAuth.value = AuthUIState.Error("Error inesperado en la aplicación: ${e.message}")
            }
        }
    }

        fun resetearEstado() {
            _estadoAuth.value = AuthUIState.Inactivo
        }


        fun cerrarSesion() {
            _usuarioActual.value = null
            _estadoAuth.value = AuthUIState.Inactivo
        }
    }
