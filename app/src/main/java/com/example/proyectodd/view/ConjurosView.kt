package com.example.proyectodd.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.proyectodd.viewmodel.ConjurosViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConjurosView(
    navController: NavController,
    viewModel: ConjurosViewModel = viewModel()
) {
    // El ViewModel expone los conjuros agrupados por nivel
    val niveles = viewModel.listaConjuros.collectAsState().value

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Lista de Conjuros") }
            )
        }
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize()
        ) {

            // Recorremos los niveles en orden (0,1,2,3...)
            niveles.keys.sorted().forEach { nivel ->

                // Encabezado del nivel
                item {
                    Text(
                        text = "Nivel $nivel",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                // Lista de hechizos del nivel correspondiente
                items(niveles[nivel]!!) { hechizo ->

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = hechizo.name,
                                style = MaterialTheme.typography.titleMedium
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = hechizo.desc,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }

                // Espacio entre niveles
                item { Spacer(modifier = Modifier.height(16.dp)) }
            }
        }
    }
}
