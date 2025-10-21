package com.example.proyectodd.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [Usuario::class], version = 2)
abstract class AppDataBase : RoomDatabase(){

    abstract fun usuarioDao(): UsuarioDao

    companion object {
        @Volatile
        private var INSTANCE: AppDataBase? = null

        fun getDatabase(context: Context): AppDataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDataBase::class.java,
                    "usuarios_db"
                ).fallbackToDestructiveMigration()
                    // 🔥 esto borra la base de datos completamente cada vez que se inicia
                    .allowMainThreadQueries() // (solo mientras desarrollas)
                    .build()
                INSTANCE = instance
                instance
            }
        }


    }



}