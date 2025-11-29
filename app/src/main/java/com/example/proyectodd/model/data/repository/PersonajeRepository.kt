package com.example.proyectodd.model.data.repository

import com.example.proyectodd.model.data.local.dao.PersonajeDao
import com.example.proyectodd.model.Personaje
import kotlinx.coroutines.flow.Flow

class PersonajeRepository(private val dao: PersonajeDao) {

    // LISTA REACTIVA DE PERSONAJES
    fun observeByUsuario(uid: Long) = dao.observeByUsuario(uid)

    // OBTENER UNO POR ID
    suspend fun getById(id: Long): Personaje? = dao.getById(id)

    // CREAR NUEVO (devuelve id generado por Room)
    suspend fun insert(p: Personaje): Long = dao.insert(p)

    // ACTUALIZAR EXISTENTE
    suspend fun update(p: Personaje) = dao.update(p)

    // ELIMINAR (por si lo usamos luego)
    suspend fun delete(p: Personaje) = dao.delete(p)
}
