package com.example.proyectodd.viewmodel.retrofit

import com.example.proyectodd.viewmodel.api.UsuarioApi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitUsuario {
    // USA 10.0.2.2 SI ES EMULADOR
    // USA 192.168.X.X SI ES CELULAR FÍSICO
    private const val BASE_URL = "http://192.168.56.1:8080/"
    private val client = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS) // 15 segundos para conectar
        .readTimeout(15, TimeUnit.SECONDS)    // 15 segundos para leer
        .build()
    val usuarioApi: UsuarioApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(UsuarioApi::class.java)
    }
}