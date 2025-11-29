package com.example.proyectodd.model.data.local.dao

import androidx.room.*
import com.example.proyectodd.model.Usuario

@Dao
interface UsuarioDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertarUsuario(usuario: Usuario): Long

    @Query("SELECT * FROM usuarios WHERE correo = :correo LIMIT 1")
    suspend fun obtenerUsuarioPorCorreo(correo: String): Usuario?

    @Query("SELECT COUNT(*) FROM usuarios WHERE correo = :correo")
    suspend fun correoExiste(correo: String): Int

    @Query("SELECT * FROM usuarios WHERE id = :userId LIMIT 1")
    suspend fun obtenerUsuarioPorId(userId: Long): Usuario?

    @Query("SELECT * FROM usuarios")
    suspend fun obtenerTodosLosUsuarios(): List<Usuario>

    @Query("DELETE FROM usuarios WHERE id = :userId")
    suspend fun eliminarUsuario(userId: Long)
}