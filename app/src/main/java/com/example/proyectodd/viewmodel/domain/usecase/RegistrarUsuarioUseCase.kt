package com.example.proyectodd.viewmodel.domain.usecase

import com.example.proyectodd.model.Usuario
import com.example.proyectodd.model.data.repository.AuthRepository
import com.example.proyectodd.viewmodel.domain.validator.AuthValidator

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