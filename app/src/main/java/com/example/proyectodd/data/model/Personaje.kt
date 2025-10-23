package com.example.proyectodd.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "personajes")
data class Personaje(
    @PrimaryKey val id: Int = 1,            // único registro (tu “hoja”)
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
    // URI de la imagen del “Portrait”
    val portraitUri: String? = null
)