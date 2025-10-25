package com.example.proyectodd.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.proyectodd.model.Personaje
import com.example.proyectodd.viewmodel.PersonajeViewModel

@Composable
fun PersonajeGuardadoView(
    vm: PersonajeViewModel,
    onCrear: () -> Unit,
    onAbrir: (Long) -> Unit
) {
    val personajes by vm.lista.collectAsStateWithLifecycle()
    var toDelete by remember { mutableStateOf<Personaje?>(null) }

    Scaffold(
        floatingActionButton = { FloatingActionButton(onClick = onCrear) { Text("+") } }
    ) { pad ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(pad)
                .padding(16.dp)
        ) {
            Text("Mis personajes", style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(12.dp))

            if (personajes.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No hay personajes. Crea el primero con +")
                }
            } else {
                LazyColumn(Modifier.fillMaxSize()) {
                    items(personajes, key = { it.id }) { p: Personaje ->
                        ListItem(
                            headlineContent = { Text(p.nombre.ifBlank { "(Sin nombre)" }) },
                            supportingContent = {
                                Text("${p.raza} • ${p.clase} • Nv ${p.nivel} • HP ${p.hp}")
                            },
                            trailingContent = {
                                TextButton(onClick = { toDelete = p }) { Text("Eliminar") }
                            },
                            modifier = Modifier.clickable { onAbrir(p.id) }
                        )
                        HorizontalDivider() // <- en Material3 usa HorizontalDivider
                    }
                }
            }
        }
    }

    if (toDelete != null) {
        AlertDialog(
            onDismissRequest = { toDelete = null },
            title = { Text("Eliminar personaje") },
            text = { Text("¿Eliminar \"${toDelete?.nombre?.ifBlank { "(Sin nombre)" }}\"?") },
            confirmButton = {
                TextButton(onClick = {
                    toDelete?.let { p ->
                        vm.eliminar(p) {  }
                    }
                    toDelete = null
                }) { Text("Eliminar") }
            },
            dismissButton = {
                TextButton(onClick = { toDelete = null }) { Text("Cancelar") }
            }
        )
    }
}
