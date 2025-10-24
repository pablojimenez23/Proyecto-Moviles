package com.example.proyectodd.viewmodel.navegacion

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.runtime.*
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.proyectodd.view.*
import com.example.proyectodd.viewmodel.AuthViewModel

sealed class Rutas(val ruta: String) {
    object Splash : Rutas("splash")
    object Login : Rutas("login")
    object Registro : Rutas("registro")
    object Inicio : Rutas("inicio")
    object  CrearPersonaje : Rutas("personajes")
}

@Composable
fun NavegacionAuth(viewModel: AuthViewModel) {
    val navController = rememberNavController()
    val usuarioActual by viewModel.usuarioActual.collectAsStateWithLifecycle()

    NavHost(
        navController = navController,
        startDestination = Rutas.Splash.ruta,
        enterTransition = {
            fadeIn(animationSpec = tween(800))
        },
        exitTransition = {
            fadeOut(animationSpec = tween(800))
        }
    ) {
        // PANTALLA SPLASH
        composable(
            route = Rutas.Splash.ruta,
            exitTransition = {
                fadeOut(animationSpec = tween(500))
            }
        ) {
            PantallaSplash(
                onSplashFinished = {
                    navController.navigate(Rutas.Login.ruta) {
                        popUpTo(Rutas.Splash.ruta) { inclusive = true }
                    }
                }
            )
        }

        // PANTALLA LOGIN
        composable(
            route = Rutas.Login.ruta,
            enterTransition = {
                fadeIn(animationSpec = tween(600))
            },
            exitTransition = {
                fadeOut(animationSpec = tween(600))
            }
        ) {
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

        // PANTALLA REGISTRO
        composable(
            route = Rutas.Registro.ruta,
            enterTransition = {
                fadeIn(animationSpec = tween(600))
            },
            exitTransition = {
                fadeOut(animationSpec = tween(600))
            }
        ) {
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

        // PANTALLA INICIO
        composable(
            route = Rutas.Inicio.ruta,
            enterTransition = {
                fadeIn(animationSpec = tween(600))
            },
            exitTransition = {
                fadeOut(animationSpec = tween(600))
            }
        ) {
            usuarioActual?.let { usuario ->
                MenuPrincipalScreen (
                    usuario = usuario,
                    cerrarSesion = {
                        viewModel.cerrarSesion()
                        navController.navigate(Rutas.Login.ruta) {
                            popUpTo(Rutas.Inicio.ruta) { inclusive = true }
                        }
                    }, creacionpersonaje = {

                            navController.navigate(Rutas.CrearPersonaje.ruta) {
                                popUpTo(Rutas.Inicio.ruta) { inclusive = true }
                            }
                    }
                )
            }
        }
        // CREADOR DE PERSONAJES
        composable(
            route = Rutas.CrearPersonaje.ruta,
            enterTransition = {
                fadeIn(animationSpec = tween(600))
            },
            exitTransition = {
                fadeOut(animationSpec = tween(600))
            }
        ) {
            usuarioActual?.let { usuario ->
                CardDndForm (
                    usuario = usuario

                )
            }
        }
    }
}