package com.example.proyectodd.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.proyectodd.data.repository.PersonajeRepository

class PersonajeViewModelFactory(
    private val repo: PersonajeRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PersonajeViewModel(repo) as T
    }
}
