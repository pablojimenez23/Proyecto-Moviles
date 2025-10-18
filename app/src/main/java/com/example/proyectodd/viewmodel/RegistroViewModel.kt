package com.example.proyectodd.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.proyectodd.model.Personaje

class RegistroViewModel : ViewModel() {
    var nombre = mutableStateOf("")
    var correo = mutableStateOf("")
    var contrasena = mutableStateOf("")

    var errorMensaje = mutableStateOf("")
    var confirmarContrasena = mutableStateOf("")

    var errorNombre = mutableStateOf("")
    var errorCorreo = mutableStateOf("")
    var errorContrasena = mutableStateOf("")
    var errorConfirmarContrasena = mutableStateOf("")



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
            !contrasena.value.any { it.isLowerCase() } -> "Debe tener al menos una minúscula"
            !contrasena.value.any { it.isDigit() } -> "Debe tener al menos un número"
            !contrasena.value.any { !it.isLetterOrDigit() } -> "Debe tener al menos un carácter especial"
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
    fun registrar() {
        if (validarFormulario()) {
            val nuevoPersonaje = Personaje(
                nombre = nombre.value,
                correo = correo.value,
                contrasena = contrasena.value
            )
            println("Personaje creado: $nuevoPersonaje")
        }
    }
}

