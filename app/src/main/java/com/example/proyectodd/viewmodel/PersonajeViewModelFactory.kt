package com.example.proyectodd.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.proyectodd.model.data.local.database.AppDatabase
import com.example.proyectodd.model.data.repository.PersonajeRepository

class PersonajeViewModelFactory(
    private val repo: PersonajeRepository,
    private val authVM: AuthViewModel
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        if (modelClass.isAssignableFrom(PersonajeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PersonajeViewModel(repo, authVM) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PersonajeViewModel::class.java)) {
            return PersonajeViewModel(repo, authVM) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }

    companion object {
        fun from(context: Context, authVM: AuthViewModel): PersonajeViewModelFactory {
            val db = AppDatabase.obtenerBaseDatos(context)
            val repo = PersonajeRepository(db.personajeDao())
            return PersonajeViewModelFactory(repo, authVM)
        }
    }
}
