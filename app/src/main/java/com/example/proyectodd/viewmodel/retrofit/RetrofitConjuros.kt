package com.example.proyectodd.viewmodel.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitConjuros {

    val api: ConjurosApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.open5e.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ConjurosApi::class.java)
    }
}
