package com.example.proyectodd.viewmodel.retrofit

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitConjuros {
    private val conjuros = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS) // 15 segundos para conectar
        .readTimeout(15, TimeUnit.SECONDS)    // 15 segundos para leer
        .build()
    val api: ConjurosApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.open5e.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(conjuros)
            .build()
            .create(ConjurosApi::class.java)
    }
}
