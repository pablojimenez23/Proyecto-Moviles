package com.example.proyectodd.model.data.repository

import com.example.proyectodd.model.Personaje
import com.example.proyectodd.model.Usuario
import com.example.proyectodd.viewmodel.retrofit.RetrofitUsuario

class UsuarioRepositorio {

    suspend fun obtenerTodos(): List<Usuario> {
        return try {
            val res = RetrofitUsuario.usuarioApi.obtenerClientes()

            if (res.isSuccessful) res.body() ?: emptyList() else emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }


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
    suspend fun agregarCliente(usuario: Usuario): Result<Usuario> {
        return try {
            val respuesta = RetrofitUsuario.usuarioApi.crearCliente(usuario)

            if (respuesta.isSuccessful) {
                // Si la respuesta es exitosa (200 o 201), obtenemos el usuario creado
                val usuarioCreado = respuesta.body()

                if (usuarioCreado != null) {
                    Result.success(usuarioCreado)
                } else {
                    // Caso raro: El servidor dice OK pero no devolvió datos
                    Result.failure(Exception("El servidor respondió OK pero sin cuerpo"))
                }
            } else {
                val codigo = respuesta.code()
                Result.failure(Exception("Error al guardar: $codigo"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error de conexión: ${e.localizedMessage}"))
        }
    }
}