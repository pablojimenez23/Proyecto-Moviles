package com.example.proyectodd.ui.state

import com.example.proyectodd.data.model.Usuario

sealed class AuthUIState {
    object Inactivo : AuthUIState()
    object Cargando : AuthUIState()
    data class Exito(val usuario: Usuario) : AuthUIState()
    data class Error(val mensaje: String) : AuthUIState()
}