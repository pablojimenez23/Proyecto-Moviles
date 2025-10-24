package com.example.proyectodd.model.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.proyectodd.model.Personaje
import kotlinx.coroutines.flow.Flow

@Dao
interface PersonajeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(personaje: Personaje)

    @Query("SELECT * FROM personajes WHERE id = 1 LIMIT 1")
    fun observePersonaje(): Flow<Personaje?>

    @Query("SELECT * FROM personajes WHERE id = 1 LIMIT 1")
    suspend fun getOnce(): Personaje?
}