package com.example.proyectodd.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectodd.model.Personaje
import com.example.proyectodd.model.data.repository.PersonajeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PersonajeViewModel(
    private val repo: PersonajeRepository
) : ViewModel() {


    val lista: StateFlow<List<Personaje>> =
        repo.observeAll()
            .map { it.sortedBy { p -> p.nombre.lowercase() } }
            .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())


    private val _estado = MutableStateFlow(vacio())
    val estado: StateFlow<Personaje> = _estado.asStateFlow()

    fun nuevo() { _estado.value = vacio() }

    fun cargar(id: Long) {
        viewModelScope.launch {
            repo.getById(id)?.let { _estado.value = it }
        }
    }

    fun guardar(onDone: () -> Unit) {
        viewModelScope.launch {
            val p = _estado.value
            if (p.id == 0L) repo.insert(p) else repo.update(p)
            onDone()
        }
    }

    fun eliminarActual(onDone: () -> Unit) {
        viewModelScope.launch {
            repo.delete(_estado.value)
            onDone()
        }
    }


    fun setNombre(v: String)            { _estado.value = _estado.value.copy(nombre = v) }
    fun setNivel(v: String)             { _estado.value = _estado.value.copy(nivel = v.toIntOrNull() ?: 0) }
    fun setRaza(v: String)              { _estado.value = _estado.value.copy(raza = v) }
    fun setClase(v: String)             { _estado.value = _estado.value.copy(clase = v) }
    fun setTrasfondo(v: String)         { _estado.value = _estado.value.copy(trasfondo = v) }
    fun setAlineamiento(v: String)      { _estado.value = _estado.value.copy(alineamiento = v) }

    fun setStr(v: String)               { _estado.value = _estado.value.copy(str = v.toIntOrNull() ?: 0) }
    fun setDex(v: String)               { _estado.value = _estado.value.copy(dex = v.toIntOrNull() ?: 0) }
    fun setCon(v: String)               { _estado.value = _estado.value.copy(con = v.toIntOrNull() ?: 0) }
    fun setIntg(v: String)              { _estado.value = _estado.value.copy(intg = v.toIntOrNull() ?: 0) }
    fun setSab(v: String)               { _estado.value = _estado.value.copy(sab = v.toIntOrNull() ?: 0) }
    fun setCar(v: String)               { _estado.value = _estado.value.copy(car = v.toIntOrNull() ?: 0) }

    fun setAC(v: String)                { _estado.value = _estado.value.copy(ac = v.toIntOrNull() ?: 0) }
    fun setIniciativa(v: String)        { _estado.value = _estado.value.copy(iniciativa = v.toIntOrNull() ?: 0) }
    fun setVelocidad(v: String)         { _estado.value = _estado.value.copy(velocidad = v.toIntOrNull() ?: 0) }
    fun setPercepcionPasiva(v: String)  { _estado.value = _estado.value.copy(percepcionPasiva = v.toIntOrNull() ?: 0) }
    fun setSaveDC(v: String)            { _estado.value = _estado.value.copy(saveDC = v.toIntOrNull() ?: 0) }
    fun setHP(v: String)                { _estado.value = _estado.value.copy(hp = v.toIntOrNull() ?: 0) }

    fun setPlayerName(v: String)        { _estado.value = _estado.value.copy(playerName = v) }
    fun setPortrait(uri: Uri?)          { _estado.value = _estado.value.copy(portraitUri = uri?.toString()) }

    private fun vacio() = Personaje(
        id = 0L,
        nombre = "",
        clase = "",
        raza = "",
        trasfondo = "",
        alineamiento = "",
        nivel = 0,
        hp = 0,
        ac = 0,
        iniciativa = 0,
        velocidad = 0,
        competencia = 0,
        percepcionPasiva = 0,
        str = 0, dex = 0, con = 0, intg = 0, sab = 0, car = 0,
        saveDC = 0,
        playerName = "",
        portraitUri = null
    )
    fun eliminar(personaje: Personaje, onDone: () -> Unit = {}) {
        viewModelScope.launch {
            repo.delete(personaje)
            onDone()
        }
    }

}
