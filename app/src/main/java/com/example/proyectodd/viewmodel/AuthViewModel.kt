package com.example.proyectodd.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectodd.model.data.local.database.AppDatabase
import com.example.proyectodd.model.Usuario
import com.example.proyectodd.model.data.repository.AuthRepository
import com.example.proyectodd.model.data.source.AuthDataSource
import com.example.proyectodd.viewmodel.domain.usecase.IniciarSesionUseCase
import com.example.proyectodd.viewmodel.domain.usecase.RegistrarUsuarioUseCase
import com.example.proyectodd.viewmodel.state.AuthUIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.obtenerBaseDatos(application)
    private val dataSource = AuthDataSource(database.usuarioDao())
    private val repository = AuthRepository(dataSource)
    private val iniciarSesionUseCase = IniciarSesionUseCase(repository)
    private val registrarUsuarioUseCase = RegistrarUsuarioUseCase(repository)

    private val _estadoAuth = MutableStateFlow<AuthUIState>(AuthUIState.Inactivo)
    val estadoAuth: StateFlow<AuthUIState> = _estadoAuth.asStateFlow()

    private val _usuarioActual = MutableStateFlow<Usuario?>(null)
    val usuarioActual: StateFlow<Usuario?> = _usuarioActual.asStateFlow()

    fun iniciarSesion(correo: String, contrasena: String) {
        viewModelScope.launch {
            try {
                _estadoAuth.value = AuthUIState.Cargando

                val resultado = iniciarSesionUseCase(correo, contrasena)

                resultado.onSuccess { usuario ->
                    _usuarioActual.value = usuario
                    _estadoAuth.value = AuthUIState.Exito(usuario)
                }.onFailure { exception ->
                    _estadoAuth.value = AuthUIState.Error(
                        exception.message ?: "Error desconocido"
                    )
                }
            } catch (e: Exception) {
                _estadoAuth.value = AuthUIState.Error(
                    "Error inesperado: ${e.message}"
                )
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
                _estadoAuth.value = AuthUIState.Cargando

                val resultado = registrarUsuarioUseCase(
                    nombre, correo, contrasena, confirmarContrasena
                )

                resultado.onSuccess { usuario ->
                    _usuarioActual.value = usuario
                    _estadoAuth.value = AuthUIState.Exito(usuario)
                }.onFailure { exception ->
                    _estadoAuth.value = AuthUIState.Error(
                        exception.message ?: "Error desconocido"
                    )
                }
            } catch (e: Exception) {
                _estadoAuth.value = AuthUIState.Error(
                    "Error inesperado: ${e.message}"
                )
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