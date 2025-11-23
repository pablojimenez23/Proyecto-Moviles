package com.example.proyectodd.viewmodel

import com.example.proyectodd.model.Usuario
import com.example.proyectodd.viewmodel.retrofit.RetrofitUsuario
import com.example.proyectodd.viewmodel.api.UsuarioApi


class UsuarioRepositorio {

    suspend fun obtenerClientes(): Result<List<Usuario>> {
        return try {

            val respuesta = RetrofitUsuario.usuarioApi.obtenerClientes()

            if (respuesta.isSuccessful) {

                val listaUsuarios = respuesta.body() ?: emptyList<Usuario>()
                Result.success(listaUsuarios)
            } else {

                val codigo = respuesta.code()
                val mensajeError = "Error del servidor: $codigo"
                Result.failure(Exception(mensajeError))
            }
        } catch (e: Exception) {

            Result.failure(Exception("Error de conexión: ${e.localizedMessage}"))
        }
    }
}


