package com.example.proyectodd.navegacion

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.proyectodd.pantallas.PantallaInicioSesion
import com.example.proyectodd.pantallas.PantallaRegistro

@Composable
fun NavegacionAuth() {
    val controladorNav = rememberNavController()

    NavHost(
        navController = controladorNav,
        startDestination = "inicio_sesion"
    ) {
        composable("inicio_sesion") {
            PantallaInicioSesion(
                irARegistro = {
                    controladorNav.navigate("registro")
                }
            )
        }

        composable("registro") {
            PantallaRegistro(
                irAInicioSesion = {
                    controladorNav.navigate("inicio_sesion")
                }
            )
        }
    }
}