package com.example.proyectodd.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.proyectodd.data.local.dao.PersonajeDao
import com.example.proyectodd.data.local.dao.UsuarioDao
import com.example.proyectodd.data.model.Usuario
import com.example.proyectodd.data.model.Personaje

@Database(
    entities = [Usuario::class, Personaje::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun usuarioDao(): UsuarioDao
    abstract fun personajeDao(): PersonajeDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun obtenerBaseDatos(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instancia = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "dnd_auth_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instancia
                instancia
            }
        }
    }
}