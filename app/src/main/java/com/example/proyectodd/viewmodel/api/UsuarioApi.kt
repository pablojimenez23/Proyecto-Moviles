package com.example.proyectodd.viewmodel.api
import com.example.proyectodd.model.Usuario
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT


    interface UsuarioApi {
        @GET("clientes")
        suspend fun obtenerClientes(): Response<List<Usuario>>

        @POST("clientes")
        suspend fun crearCliente(@Body cliente: Usuario): Response<Usuario>


        @PUT("clientes")
        suspend fun actualizarCliente(@Body cliente: Usuario): Response<Usuario>


    }



