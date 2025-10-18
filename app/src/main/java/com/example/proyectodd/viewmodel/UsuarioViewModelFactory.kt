package com.example.proyectodd.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.proyectodd.data.UsuarioDao

class UsuarioViewModelFactory(private val usuarioDao: UsuarioDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(RegistroViewModel::class.java) -> {
                RegistroViewModel(usuarioDao) as T
            }
            modelClass.isAssignableFrom(InicioSesionViewModel::class.java) -> {
                InicioSesionViewModel(usuarioDao) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}