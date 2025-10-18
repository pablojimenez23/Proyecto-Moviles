package com.example.proyectodd.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class InicioSesionViewModel: ViewModel() {

    var correo = mutableStateOf("")
    var contrasena = mutableStateOf("")
    var errorCorreo = mutableStateOf("")
    var errorContrasena = mutableStateOf("")

    fun iniciarSesion() {
        // Aquí iría la lógica de autenticación
        println("Iniciando sesión con: ${correo.value}")
    }

}

