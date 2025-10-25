package com.example.proyectodd.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.example.proyectodd.model.Usuario
import com.example.proyectodd.viewmodel.PersonajeViewModel

@Composable
fun PersonajeCrearView(
    vm: PersonajeViewModel,
    usuario: Usuario,
    onCancel: () -> Unit,
    onSaved: () -> Unit
) {

    LaunchedEffect(Unit) { vm.nuevo() }


    CardDndForm(
        usuario = usuario,
        vmExternal = vm,
        onCancel = onCancel,
        onSaved = onSaved
    )
}
