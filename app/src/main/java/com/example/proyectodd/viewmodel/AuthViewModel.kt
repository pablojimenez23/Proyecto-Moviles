package com.example.proyectodd.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectodd.model.Usuario
import com.example.proyectodd.model.data.local.database.AppDatabase
import com.example.proyectodd.model.data.repository.UsuarioRepositorio
import com.example.proyectodd.model.data.source.AuthDataSource
import com.example.proyectodd.viewmodel.state.AuthUIState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.obtenerBaseDatos(application)
    private val apiRepository = UsuarioRepositorio()

    private val _estadoAuth = MutableStateFlow<AuthUIState>(AuthUIState.Inactivo)
    val estadoAuth: StateFlow<AuthUIState> = _estadoAuth.asStateFlow()

    private val _usuarioActual = MutableStateFlow<Usuario?>(null)
    val usuarioActual: StateFlow<Usuario?> = _usuarioActual.asStateFlow()


    //  LOGIN HÍBRIDO (microservicio + persistencia local sincronizada)

    fun iniciarSesion(correo: String, contrasena: String) {
        viewModelScope.launch {
            try {
                _estadoAuth.value = AuthUIState.Cargando

                if (correo.isEmpty() || contrasena.isEmpty()) {
                    _estadoAuth.value = AuthUIState.Error("Completa todos los campos.")
                    return@launch
                }

                // 1) Login remoto usando microservicio
                val resultado = apiRepository.login(correo, contrasena)

                resultado.onSuccess {

                    // 2) Sincronización con usuario local
                    val dao = database.usuarioDao()
                    val authLocal = AuthDataSource(dao)

                    var usuarioLocal = authLocal.obtenerUsuarioPorCorreo(correo)

                    // 3) Si no existe localmente, crearlo
                    if (usuarioLocal == null) {
                        val contrasenaHash = authLocal.hashearContrasena(contrasena)

                        val idInsertado = authLocal.insertarUsuario(
                            Usuario(
                                nombre = "", // No se obtiene desde el microservicio
                                correo = correo,
                                contrasenaHash = contrasenaHash
                            )
                        )

                        usuarioLocal = dao.obtenerUsuarioPorId(idInsertado)
                    }

                    // 4) Establecer usuarioActual
                    _usuarioActual.value = usuarioLocal
                    _estadoAuth.value = AuthUIState.Exito(usuarioLocal!!)
                }

                resultado.onFailure { exception ->
                    _estadoAuth.value = AuthUIState.Error(
                        exception.message ?: "Error de autenticación."
                    )
                }

            } catch (e: Exception) {
                _estadoAuth.value = AuthUIState.Error("Error inesperado: ${e.message}")
            }
        }
    }




    //  REGISTRO HÍBRIDO (microservicio + creación automática en local)
    fun registrarUsuario(
        nombre: String,
        correo: String,
        contrasena: String,
        confirmarContrasena: String
    ) {
        viewModelScope.launch {
            try {
                _estadoAuth.value = AuthUIState.Cargando

                if (nombre.isEmpty() || correo.isEmpty() || contrasena.isEmpty() || confirmarContrasena.isEmpty()) {
                    _estadoAuth.value = AuthUIState.Error("Completa todos los campos.")
                    return@launch
                }

                if (contrasena != confirmarContrasena) {
                    _estadoAuth.value = AuthUIState.Error("Las contraseñas no coinciden.")
                    return@launch
                }

                // 1) Registro remoto
                val resultado = apiRepository.register(nombre, correo, contrasena)

                resultado.onSuccess {

                    // 2) Registrar también el usuario localmente
                    val dao = database.usuarioDao()
                    val authLocal = AuthDataSource(dao)

                    val contrasenaHash = authLocal.hashearContrasena(contrasena)

                    val idInsertado = authLocal.insertarUsuario(
                        Usuario(
                            nombre = nombre,
                            correo = correo,
                            contrasenaHash = contrasenaHash
                        )
                    )

                    val usuarioLocal = dao.obtenerUsuarioPorId(idInsertado)

                    // 3) Marcos usuarioActual
                    _usuarioActual.value = usuarioLocal
                    _estadoAuth.value = AuthUIState.Exito(usuarioLocal!!)
                }

                resultado.onFailure { exception ->
                    _estadoAuth.value = AuthUIState.Error(
                        exception.message ?: "Error desconocido en el registro."
                    )
                }

            } catch (e: Exception) {
                _estadoAuth.value = AuthUIState.Error("Error inesperado: ${e.message}")
            }
        }
    }


    // UTILIDADES
    fun resetearEstado() {
        _estadoAuth.value = AuthUIState.Inactivo
    }

    fun cerrarSesion() {
        _usuarioActual.value = null
        _estadoAuth.value = AuthUIState.Inactivo
    }
}