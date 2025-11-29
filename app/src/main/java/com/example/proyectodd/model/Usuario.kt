package com.example.proyectodd.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuarios")
data class Usuario(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val nombre: String,
    val correo: String,
    val contrasenaHash: String,
    val fechaCreacion: Long = System.currentTimeMillis()
)