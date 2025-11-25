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

    private val apiRepository = UsuarioRepositorio()


    private val _estadoAuth = MutableStateFlow<AuthUIState>(AuthUIState.Inactivo)
    val estadoAuth: StateFlow<AuthUIState> = _estadoAuth.asStateFlow()

    private val _usuarioActual = MutableStateFlow<Usuario?>(null)
    val usuarioActual: StateFlow<Usuario?> = _usuarioActual.asStateFlow()

    fun iniciarSesion(correo: String, contrasena: String) {
        viewModelScope.launch {
            try {
                _estadoAuth.value = AuthUIState.Cargando

                // 1. Hacemos un GET a la API para traer los usuarios
                val resultado = apiRepository.obtenerClientes()

                resultado.onSuccess { listaUsuarios ->

                    val usuarioEncontrado = listaUsuarios.find {
                        it.correo == correo && it.contrasena == contrasena
                    }

                    if (usuarioEncontrado != null) {
                        _usuarioActual.value = usuarioEncontrado
                        _estadoAuth.value = AuthUIState.Exito(usuarioEncontrado)
                    } else {
                        _estadoAuth.value = AuthUIState.Error("Credenciales incorrectas o usuario no encontrado en la Nube")
                    }
                }

                resultado.onFailure { exception ->
                    _estadoAuth.value = AuthUIState.Error(
                        "Error de conexión: ${exception.message}"
                    )
                }

            } catch (e: Exception) {
                _estadoAuth.value = AuthUIState.Error("Error inesperado: ${e.message}")
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


                if (contrasena != confirmarContrasena) {
                    _estadoAuth.value = AuthUIState.Error("Las contraseñas no coinciden")
                    return@launch
                }
                if (nombre.isEmpty() || correo.isEmpty()) {
                    _estadoAuth.value = AuthUIState.Error("Completa todos los campos")
                    return@launch
                }


                val nuevoUsuario = Usuario(
                    id = 0,
                    nombre = nombre,
                    correo = correo,
                    contrasena = contrasena

                )


                val resultado = apiRepository.agregarCliente(nuevoUsuario)

                resultado.onSuccess { usuarioCreado ->
                    _usuarioActual.value = usuarioCreado
                    _estadoAuth.value = AuthUIState.Exito(usuarioCreado)
                }

                resultado.onFailure { exception ->
                    _estadoAuth.value = AuthUIState.Error(
                        "Error al registrar: ${exception.message}"
                    )
                }

            } catch (e: Exception) {
                _estadoAuth.value = AuthUIState.Error("Error inesperado: ${e.message}")
            }
        }
    }

    fun resetearEstado() {
        _estadoAuth.value = AuthUIState.Inactivo }


    fun cerrarSesion() {
        _usuarioActual.value = null
        _estadoAuth.value = AuthUIState.Inactivo
    }
}