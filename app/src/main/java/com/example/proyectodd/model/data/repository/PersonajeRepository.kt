package com.example.proyectodd.model.data.repository

import com.example.proyectodd.model.data.local.dao.PersonajeDao
import com.example.proyectodd.model.Personaje
import kotlinx.coroutines.flow.Flow

class PersonajeRepository(private val dao: PersonajeDao) {
    fun observe(): Flow<Personaje?> = dao.observePersonaje()
    suspend fun getOnce(): Personaje? = dao.getOnce()
    suspend fun upsert(p: Personaje) = dao.upsert(p)
}