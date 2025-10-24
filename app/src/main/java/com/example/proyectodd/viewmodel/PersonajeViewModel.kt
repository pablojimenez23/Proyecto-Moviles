package com.example.proyectodd.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectodd.model.Personaje
import com.example.proyectodd.model.data.repository.PersonajeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PersonajeViewModel(
    private val repository: PersonajeRepository
) : ViewModel() {

    private val _estado = MutableStateFlow(Personaje())
    val estado: StateFlow<Personaje> = _estado.asStateFlow()

    init {
        viewModelScope.launch {
            repository.observe().collect { guardado ->
                _estado.value = guardado ?: Personaje()
            }
        }
    }

    fun setNombre(v: String) = _estado.update { it.copy(nombre = v) }
    fun setClase(v: String) = _estado.update { it.copy(clase = v) }
    fun setRaza(v: String) = _estado.update { it.copy(raza = v) }
    fun setTrasfondo(v: String) = _estado.update { it.copy(trasfondo = v) }
    fun setAlineamiento(v: String) = _estado.update { it.copy(alineamiento = v) }
    fun setNivel(v: String) = _estado.update { it.copy(nivel = v.toIntOrNull() ?: 0) }
    fun setHP(v: String) = _estado.update { it.copy(hp = v.toIntOrNull() ?: 0) }
    fun setAC(v: String) = _estado.update { it.copy(ac = v.toIntOrNull() ?: 0) }
    fun setIniciativa(v: String) = _estado.update { it.copy(iniciativa = v.toIntOrNull() ?: 0) }
    fun setVelocidad(v: String) = _estado.update { it.copy(velocidad = v.toIntOrNull() ?: 0) }
    fun setCompetencia(v: String) = _estado.update { it.copy(competencia = v.toIntOrNull() ?: 0) }
    fun setPercepcionPasiva(v: String) = _estado.update { it.copy(percepcionPasiva = v.toIntOrNull() ?: 0) }
    fun setStr(v: String) = _estado.update { it.copy(str = v.toIntOrNull() ?: 0) }
    fun setDex(v: String) = _estado.update { it.copy(dex = v.toIntOrNull() ?: 0) }
    fun setCon(v: String) = _estado.update { it.copy(con = v.toIntOrNull() ?: 0) }
    fun setIntg(v: String) = _estado.update { it.copy(intg = v.toIntOrNull() ?: 0) }
    fun setSab(v: String) = _estado.update { it.copy(sab = v.toIntOrNull() ?: 0) }
    fun setCar(v: String) = _estado.update { it.copy(car = v.toIntOrNull() ?: 0) }
    fun setSaveDC(v: String) = _estado.update { it.copy(saveDC = v.toIntOrNull() ?: 0) }
    fun setPlayerName(v: String) = _estado.update { it.copy(playerName = v) }

    fun setPortrait(uri: Uri?) = _estado.update { it.copy(portraitUri = uri?.toString()) }

    fun guardar() {
        viewModelScope.launch {
            repository.upsert(_estado.value.copy(id = 1))
        }
    }
}
