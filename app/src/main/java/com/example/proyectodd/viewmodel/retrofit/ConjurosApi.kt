package com.example.proyectodd.viewmodel.retrofit

import com.example.proyectodd.model.RespuestaConjuros
import retrofit2.http.GET

interface ConjurosApi {

    @GET("spells")
    suspend fun getConjuros(): RespuestaConjuros
}