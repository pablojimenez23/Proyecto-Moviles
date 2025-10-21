package com.example.proyectodd.data.source

import com.example.proyectodd.data.local.dao.UsuarioDao
import com.example.proyectodd.data.model.Usuario
import java.security.MessageDigest

class AuthDataSource(private val usuarioDao: UsuarioDao) {

    fun hashearContrasena(contrasena: String): String {
        val bytes = contrasena.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        return digest.fold("") { str, it -> str + "%02x".format(it) }
    }

    suspend fun insertarUsuario(usuario: Usuario): Long {
        return usuarioDao.insertarUsuario(usuario)
    }

    suspend fun obtenerUsuarioPorCorreo(correo: String): Usuario? {
        return usuarioDao.obtenerUsuarioPorCorreo(correo)
    }

    suspend fun correoExiste(correo: String): Boolean {
        return usuarioDao.correoExiste(correo) > 0
    }
}