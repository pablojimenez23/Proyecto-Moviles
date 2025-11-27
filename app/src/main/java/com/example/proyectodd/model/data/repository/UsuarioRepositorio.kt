package com.example.proyectodd.model.data.repository

import com.example.proyectodd.model.Personaje
import com.example.proyectodd.model.Usuario
import com.example.proyectodd.viewmodel.retrofit.RetrofitUsuario

class UsuarioRepositorio {



    private fun crearCredencialesMap(email: String, password: String): Map<String, String> {
        return mapOf(
            "email" to email,

            "password" to password
        )
    }


    suspend fun login(email: String, password: String): Result<String> {
        return try {
            val credenciales = crearCredencialesMap(email, password)
            val respuesta = RetrofitUsuario.usuarioApi.loginUsuario(credenciales)

            // 1. MANEJO DEL RESULTADO
            val resultado: Result<String> = when (respuesta.code()) { // Declaramos el tipo explícitamente
                200 -> { // Éxito
                    Result.success(respuesta.body() ?: "LOGIN_OK")
                }

                401 -> { // Fallo de autenticación
                    val errorMessage = respuesta.errorBody()?.string() ?: "Credenciales inválidas."
                    Result.failure(Exception(errorMessage))
                }

                else -> { // Otros errores
                    val errorMessage = respuesta.errorBody()?.string() ?: "Error del servidor: ${respuesta.code()}"
                    Result.failure(Exception(errorMessage))
                }
            }

            // 2. RETORNA EL RESULTADO DEL WHEN
            resultado

        } catch (e: Exception) {
            // Fallo de conexión
            Result.failure(Exception("Error de conexión: ${e.localizedMessage}"))
        }
    }
    private fun crearRegistroMap(email: String, password: String, nombre: String): Map<String, String> {
        return mapOf(
            "email" to email,
            "password" to password,
            "nombre" to nombre //
        )
    }


    suspend fun register(nombre: String, email: String, password: String): Result<String> {
        return try {

            val credenciales = crearRegistroMap(email, password, nombre)

             val respuesta = RetrofitUsuario.usuarioApi.registrarUsuario(credenciales)


            when (respuesta.code()) {
                201 -> {

                    Result.success(respuesta.body() ?: "REGISTRO_OK")
                }

                409 -> {
                    val errorMessage = respuesta.errorBody()?.string() ?: "El email ya está registrado."
                    Result.failure(Exception(errorMessage))
                }

                else -> {
                    val errorMessage = respuesta.errorBody()?.string() ?: "Error del servidor: ${respuesta.code()}"
                    Result.failure(Exception(errorMessage))
                }
            }
        } catch (e: Exception) {

            Result.failure(Exception("Error de conexión: ${e.localizedMessage}"))
        }
    }
}





