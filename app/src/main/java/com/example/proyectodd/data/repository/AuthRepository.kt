package com.example.proyectodd.data.repository

import com.example.proyectodd.data.model.Usuario
import com.example.proyectodd.data.source.AuthDataSource

class AuthRepository(private val dataSource: AuthDataSource) {

    suspend fun registrarUsuario(
        nombre: String,
        correo: String,
        contrasena: String
    ): Result<Usuario> {
        return try {
            if (dataSource.correoExiste(correo)) {
                return Result.failure(Exception("Este correo ya está registrado"))
            }

            val contrasenaHash = dataSource.hashearContrasena(contrasena)
            val nuevoUsuario = Usuario(
                nombre = nombre,
                correo = correo,
                contrasenaHash = contrasenaHash
            )

            val userId = dataSource.insertarUsuario(nuevoUsuario)
            val usuarioCreado = nuevoUsuario.copy(id = userId.toInt())

            Result.success(usuarioCreado)
        } catch (e: Exception) {
            Result.failure(Exception("Error al registrar usuario: ${e.message}"))
        }
    }

    suspend fun iniciarSesion(correo: String, contrasena: String): Result<Usuario> {
        return try {
            val usuario = dataSource.obtenerUsuarioPorCorreo(correo)
                ?: return Result.failure(Exception("Usuario no encontrado"))

            val contrasenaHash = dataSource.hashearContrasena(contrasena)

            if (usuario.contrasenaHash == contrasenaHash) {
                Result.success(usuario)
            } else {
                Result.failure(Exception("Contraseña incorrecta"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error al iniciar sesión: ${e.message}"))
        }
    }
}