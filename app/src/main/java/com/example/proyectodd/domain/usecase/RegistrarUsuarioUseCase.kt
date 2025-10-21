package com.example.proyectodd.domain.usecase

import com.example.proyectodd.data.model.Usuario
import com.example.proyectodd.data.repository.AuthRepository
import com.example.proyectodd.domain.validator.AuthValidator

class RegistrarUsuarioUseCase(private val repository: AuthRepository) {

    suspend operator fun invoke(
        nombre: String,
        correo: String,
        contrasena: String,
        confirmarContrasena: String
    ): Result<Usuario> {
        AuthValidator.validarNombre(nombre)?.let {
            return Result.failure(Exception(it))
        }

        AuthValidator.validarCorreo(correo)?.let {
            return Result.failure(Exception(it))
        }

        AuthValidator.validarContrasena(contrasena)?.let {
            return Result.failure(Exception(it))
        }

        AuthValidator.validarConfirmacionContrasena(contrasena, confirmarContrasena)?.let {
            return Result.failure(Exception(it))
        }

        return repository.registrarUsuario(nombre, correo, contrasena)
    }
}