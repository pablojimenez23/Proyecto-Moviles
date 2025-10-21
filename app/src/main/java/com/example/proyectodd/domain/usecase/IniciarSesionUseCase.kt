package com.example.proyectodd.domain.usecase

import com.example.proyectodd.data.model.Usuario
import com.example.proyectodd.data.repository.AuthRepository
import com.example.proyectodd.domain.validator.AuthValidator

class IniciarSesionUseCase(private val repository: AuthRepository) {

    suspend operator fun invoke(correo: String, contrasena: String): Result<Usuario> {
        AuthValidator.validarCorreo(correo)?.let {
            return Result.failure(Exception(it))
        }

        if (contrasena.isBlank()) {
            return Result.failure(Exception("La contraseña no puede estar vacía"))
        }

        return repository.iniciarSesion(correo, contrasena)
    }
}