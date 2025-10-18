package com.example.proyectodd.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectodd.data.Usuario
import com.example.proyectodd.data.UsuarioDao

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch



class RegistroViewModel(private val usuarioDao: UsuarioDao) : ViewModel() {
    var nombre = mutableStateOf("")
    var correo = mutableStateOf("")
    var contrasena = mutableStateOf("")


    var confirmarContrasena = mutableStateOf("")

    var errorNombre = mutableStateOf("")
    var errorCorreo = mutableStateOf("")
    var errorContrasena = mutableStateOf("")
    var errorConfirmarContrasena = mutableStateOf("")

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje




private fun validarEmail(email: String): Boolean {
    val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()
    return email.matches(emailRegex)



}

    fun validarCorreoEnTiempoReal() {
        errorCorreo.value = when {
            correo.value.isEmpty() -> "El correo no puede estar vacío"
            correo.value.contains(" ") -> "El correo no puede contener espacios"
            !validarEmail(correo.value) -> "Formato de correo inválido"
            else -> ""
        }
    }

    fun validarNombreEnTiempoReal() {
        errorNombre.value = when {
        nombre.value.isEmpty() -> "El nombre no puede estar vacío"
        nombre.value.length < 3 -> "El nombre debe tener al menos 3 caracteres"
        nombre.value.length > 30 -> "El nombre no puede tener más de 30 caracteres"
        !nombre.value.all { it.isLetter() || it.isWhitespace() } -> "El nombre solo puede contener letras"
        else -> ""
        }
    }
    fun validarContrasenaEnTiempoReal() {
        errorContrasena.value = when {
            contrasena.value.isEmpty() -> "La contraseña no puede estar vacía"
            contrasena.value.contains(" ") -> "La contraseña no puede contener espacios"
            contrasena.value.length < 8 -> "Mínimo 8 caracteres"
            !contrasena.value.any { it.isUpperCase() } -> "Debe tener al menos una mayúscula"
            !contrasena.value.any { it.isDigit() } -> "Debe tener al menos un número"

            else -> ""
        }
    }
    fun validarConfirmarContrasenaEnTiempoReal() {
        errorConfirmarContrasena.value = when {
            confirmarContrasena.value.isEmpty() -> "Debes confirmar la contraseña"
            contrasena.value != confirmarContrasena.value -> "Las contraseñas no coinciden"
            else -> ""
        }
    }
    fun validarFormulario(): Boolean {
        validarNombreEnTiempoReal()
        validarCorreoEnTiempoReal()
        validarContrasenaEnTiempoReal()
        validarConfirmarContrasenaEnTiempoReal()

        return errorNombre.value.isEmpty() &&
                errorCorreo.value.isEmpty() &&
                errorContrasena.value.isEmpty() &&
                errorConfirmarContrasena.value.isEmpty()
    }
    fun registrarUsuario(nombre: String, correo: String, contrasena: String) {
        viewModelScope.launch {
            val usuarioExistente = usuarioDao.buscarPorCorreo(correo)
            if (usuarioExistente != null) {
                _mensaje.value = "El correo ya está registrado"
            } else {
                val nuevoUsuario = Usuario(nombre = nombre, correo = correo, contrasena = contrasena)
                usuarioDao.insertarUsuario(nuevoUsuario)
                _mensaje.value = "Registro exitoso"

            }
        }
    }
}

