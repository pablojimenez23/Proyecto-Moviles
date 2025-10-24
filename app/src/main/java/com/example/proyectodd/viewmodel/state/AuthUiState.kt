package com.example.proyectodd.viewmodel.state

import com.example.proyectodd.model.Usuario

sealed class AuthUIState {
    object Inactivo : AuthUIState()
    object Cargando : AuthUIState()
    data class Exito(val usuario: Usuario) : AuthUIState()
    data class Error(val mensaje: String) : AuthUIState()
}