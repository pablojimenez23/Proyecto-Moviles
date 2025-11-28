package com.example.proyectodd.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectodd.model.Hechizo
import com.example.proyectodd.repository.ConjurosRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ConjurosViewModel : ViewModel() {

    private val repository = ConjurosRepository()

    private val _listaConjuros = MutableStateFlow<Map<Int, List<Hechizo>>>(emptyMap())
    val listaConjuros: StateFlow<Map<Int, List<Hechizo>>> = _listaConjuros.asStateFlow()

    init {
        cargarConjuros()
    }

    private fun cargarConjuros() {
        viewModelScope.launch {
            try {
                val datos = repository.obtenerConjuros()

                _listaConjuros.value = datos.groupBy { it.level_int }

            } catch (e: Exception) {
                println("Error al obtener conjuros: ${e.localizedMessage}")
            }
        }
    }
}
