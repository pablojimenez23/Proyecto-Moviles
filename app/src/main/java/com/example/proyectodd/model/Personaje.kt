package com.example.proyectodd.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "personajes")
data class Personaje(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val nombre: String = "",
    val clase: String = "",
    val raza: String = "",
    val trasfondo: String = "",
    val alineamiento: String = "",
    val nivel: Int = 0,
    val hp: Int = 0,
    val ac: Int = 0,
    val iniciativa: Int = 0,
    val velocidad: Int = 0,
    val competencia: Int = 0,
    val percepcionPasiva: Int = 0,
    val str: Int = 0,
    val dex: Int = 0,
    val con: Int = 0,
    val intg: Int = 0,
    val sab: Int = 0,
    val car: Int = 0,
    val saveDC: Int = 0,
    val playerName: String = "",
    val portraitUri: String? = null
)
