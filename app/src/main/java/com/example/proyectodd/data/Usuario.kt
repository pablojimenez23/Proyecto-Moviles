package com.example.proyectodd.data


import androidx.room.Entity

import androidx.room.PrimaryKey


@Entity(tableName = "usuario")
data class Usuario(@PrimaryKey(autoGenerate = true) val id: Int = 0,
                   val nombre: String,
                   val correo: String,
                   val contrasena: String)
