package com.example.proyectodd.viewmodel.api
import com.example.proyectodd.model.Usuario
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT


    interface UsuarioApi {
        @POST("clientes/login")
        suspend fun loginUsuario(@Body credenciales: Map<String, String>): Response<String> // Devuelve String (el mensaje de éxito/error)

        @POST("clientes/register")
        suspend fun registrarUsuario(@Body credenciales: Map<String, String>): Response<String> // Devuelve String (el mensaje de éxito/error)



    }



