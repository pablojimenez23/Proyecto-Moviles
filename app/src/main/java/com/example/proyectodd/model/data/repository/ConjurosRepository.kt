package com.example.proyectodd.repository

import com.example.proyectodd.model.Hechizo
import com.example.proyectodd.viewmodel.retrofit.RetrofitConjuros

class ConjurosRepository {

    suspend fun obtenerConjuros(): List<Hechizo> {
        return RetrofitConjuros.api.getConjuros().results
    }
}
