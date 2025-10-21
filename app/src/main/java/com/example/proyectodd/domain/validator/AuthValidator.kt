package com.example.proyectodd.domain.validator

object AuthValidator {

    fun validarNombre(nombre: String): String? {
        return when {
            nombre.isBlank() -> "El nombre no puede estar vacío"
            nombre.length < 3 -> "El nombre debe tener al menos 3 caracteres"
            else -> null
        }
    }

    fun validarCorreo(correo: String): String? {
        return when {
            correo.isBlank() -> "El correo no puede estar vacío"
            !android.util.Patterns.EMAIL_ADDRESS.matcher(correo).matches() ->
                "Correo electrónico inválido"
            else -> null
        }
    }

    fun validarContrasena(contrasena: String): String? {
        return when {
            contrasena.isBlank() -> "La contraseña no puede estar vacía"
            contrasena.length < 6 -> "La contraseña debe tener al menos 6 caracteres"
            else -> null
        }
    }

    fun validarConfirmacionContrasena(contrasena: String, confirmacion: String): String? {
        return when {
            confirmacion.isBlank() -> "Debes confirmar la contraseña"
            contrasena != confirmacion -> "Las contraseñas no coinciden"
            else -> null
        }
    }
}