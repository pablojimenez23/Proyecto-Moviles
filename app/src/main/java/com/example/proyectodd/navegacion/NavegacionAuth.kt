package com.example.proyectodd.navegacion

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.proyectodd.view.PantallaRegistro
import com.example.proyectodd.view.PantallaInicioSesion
import com.example.proyectodd.viewmodel.InicioSesionViewModel


import com.example.proyectodd.viewmodel.RegistroViewModel


@Composable
fun NavegacionAuth() {
    val controladorNav = rememberNavController()

    NavHost(
        navController = controladorNav,
        startDestination = "registro"
    ) {

        composable("registro") {
            val registroViewModel: RegistroViewModel = viewModel()


            PantallaRegistro(
                viewModel = registroViewModel,
                irAInicioSesion = { controladorNav.navigate("inicio_sesion") }
            )

                }

        composable("inicio_sesion") {
            val inicioSesionViewModel: InicioSesionViewModel = viewModel()
            PantallaInicioSesion(
                viewModel = inicioSesionViewModel,
                irARegistro = {
                    controladorNav.navigate("registro")
                }
            )
        }



        }
    }
