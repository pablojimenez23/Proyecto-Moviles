package com.example.proyectodd.viewmodel.retrofit

import com.example.proyectodd.viewmodel.api.UsuarioApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitUsuario {
    // USA 10.0.2.2 SI ES EMULADOR
    // USA 192.168.X.X SI ES CELULAR FÍSICO
    private const val BASE_URL = "http://192.168.1.9:8080/"

    val usuarioApi: UsuarioApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UsuarioApi::class.java)
    }
}