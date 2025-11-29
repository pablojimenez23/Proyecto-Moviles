package com.example.proyectodd.model.data.local.dao

import androidx.room.*
import com.example.proyectodd.model.Personaje
import kotlinx.coroutines.flow.Flow

@Dao
interface PersonajeDao {

    // LISTADO REACTIVO
    @Query("SELECT * FROM personajes WHERE usuarioId = :uid ORDER BY LOWER(nombre) ASC")
    fun observeByUsuario(uid: Long): Flow<List<Personaje>>

    // OBTENER UNO POR ID
    @Query("SELECT * FROM personajes WHERE id = :id LIMIT 1")
    suspend fun getById(id: Long): Personaje?

    // INSERTAR NUEVO
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(p: Personaje): Long

    // ACTUALIZAR EXISTENTE
    @Update
    suspend fun update(p: Personaje)

    // ELIMINAR (opcional, por si luego agregamos borrar)
    @Delete
    suspend fun delete(p: Personaje)
}
