package com.example.proyectodd.navegacion

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.proyectodd.pantallas.PantallaInicio
import com.example.proyectodd.pantallas.PantallaInicioSesion
import com.example.proyectodd.pantallas.PantallaRegistro
import com.example.proyectodd.viewmodel.AuthViewModel

sealed class Rutas(val ruta: String) {
    object Login : Rutas("login")
    object Registro : Rutas("registro")
    object Inicio : Rutas("inicio")
}

@Composable
fun NavegacionAuth(viewModel: AuthViewModel) {
    val navController = rememberNavController()
    val usuarioActual by viewModel.usuarioActual.collectAsStateWithLifecycle()

    NavHost(
        navController = navController,
        startDestination = Rutas.Login.ruta
    ) {
        composable(Rutas.Login.ruta) {
            PantallaInicioSesion(
                viewModel = viewModel,
                irARegistro = {
                    navController.navigate(Rutas.Registro.ruta) {
                        popUpTo(Rutas.Login.ruta) { inclusive = false }
                    }
                },
                alExito = {
                    navController.navigate(Rutas.Inicio.ruta) {
                        popUpTo(Rutas.Login.ruta) { inclusive = true }
                    }
                }
            )
        }

        composable(Rutas.Registro.ruta) {
            PantallaRegistro(
                viewModel = viewModel,
                irAInicioSesion = {
                    navController.navigate(Rutas.Login.ruta) {
                        popUpTo(Rutas.Registro.ruta) { inclusive = true }
                    }
                },
                alExito = {
                    navController.navigate(Rutas.Inicio.ruta) {
                        popUpTo(Rutas.Registro.ruta) { inclusive = true }
                    }
                }
            )
        }

        composable(Rutas.Inicio.ruta) {
            usuarioActual?.let { usuario ->
                PantallaInicio(
                    usuario = usuario,
                    cerrarSesion = {
                        viewModel.cerrarSesion()
                        navController.navigate(Rutas.Login.ruta) {
                            popUpTo(Rutas.Inicio.ruta) { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}