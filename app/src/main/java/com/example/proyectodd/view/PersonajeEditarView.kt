package com.example.proyectodd.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.example.proyectodd.model.Usuario
import com.example.proyectodd.viewmodel.PersonajeViewModel

@Composable
fun PersonajeEditarView(
    vm: PersonajeViewModel,
    usuario: Usuario,
    personajeId: Long,
    onCancel: () -> Unit,
    onSaved: () -> Unit
) {

    LaunchedEffect(personajeId) { vm.cargar(personajeId) }


    CardDndForm(
        usuario = usuario,
        vmExternal = vm,
        onCancel = onCancel,
        onSaved = onSaved
    )
}
