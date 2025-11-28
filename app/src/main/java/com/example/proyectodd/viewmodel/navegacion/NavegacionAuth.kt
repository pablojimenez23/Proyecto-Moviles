package com.example.proyectodd.viewmodel.navegacion

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.runtime.*
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.proyectodd.view.*
import com.example.proyectodd.viewmodel.AuthViewModel

import com.example.proyectodd.viewmodel.PersonajeViewModel
import com.example.proyectodd.model.data.repository.PersonajeRepository
import com.example.proyectodd.model.data.local.database.AppDatabase
import com.example.proyectodd.viewmodel.PersonajeViewModelFactory


sealed class Rutas(val ruta: String) {
    object Splash : Rutas("splash")
    object Login : Rutas("login")
    object Registro : Rutas("registro")
    object Inicio : Rutas("inicio")


    object ListaPersonaje : Rutas("personajes")

    // NUEVAS subrutas para crear/editar
    object PersonajeCrear : Rutas("personajes/crear")
    object PersonajeEditar : Rutas("personajes/editar/{id}") {
        const val ARG_ID = "id"
        fun of(id: Long) = "personajes/editar/$id"
    }
    object Conjuros : Rutas("conjuros")


}

@Composable
fun NavegacionAuth(viewModel: AuthViewModel) {
    val navController = rememberNavController()
    val usuarioActual by viewModel.usuarioActual.collectAsStateWithLifecycle()


    val ctx = androidx.compose.ui.platform.LocalContext.current
    val personajesVM: PersonajeViewModel = viewModel(
        factory = PersonajeViewModelFactory(
            PersonajeRepository(
                dao = AppDatabase.obtenerBaseDatos(ctx).personajeDao()
            )
        )
    )

    NavHost(
        navController = navController,
        startDestination = Rutas.Splash.ruta,
        enterTransition = { fadeIn(animationSpec = tween(800)) },
        exitTransition = { fadeOut(animationSpec = tween(800)) }
    ) {
        // SPLASH
        composable(
            route = Rutas.Splash.ruta,
            exitTransition = { fadeOut(animationSpec = tween(500)) }
        ) {
            PantallaSplash(
                onSplashFinished = {
                    navController.navigate(Rutas.Login.ruta) {
                        popUpTo(Rutas.Splash.ruta) { inclusive = true }
                    }
                }
            )
        }

        // LOGIN
        composable(
            route = Rutas.Login.ruta,
            enterTransition = { fadeIn(animationSpec = tween(600)) },
            exitTransition = { fadeOut(animationSpec = tween(600)) }
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

        // REGISTRO
        composable(
            route = Rutas.Registro.ruta,
            enterTransition = { fadeIn(animationSpec = tween(600)) },
            exitTransition = { fadeOut(animationSpec = tween(600)) }
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

        // INICIO (menú principal)
        composable(
            route = Rutas.Inicio.ruta,
            enterTransition = { fadeIn(animationSpec = tween(600)) },
            exitTransition = { fadeOut(animationSpec = tween(600)) }
        ) {
            usuarioActual?.let { usuario ->
                MenuPrincipalScreen(
                    usuario = usuario,
                    cerrarSesion = {
                        viewModel.cerrarSesion()
                        navController.navigate(Rutas.Login.ruta) {
                            popUpTo(Rutas.Inicio.ruta) { inclusive = true }
                        }
                    },
                    irPersonajeGuardado = {
                        navController.navigate(Rutas.ListaPersonaje.ruta) {
                            popUpTo(Rutas.Inicio.ruta) { inclusive = true }
                        }
                    },
                    creacionpersonaje = {

                        navController.navigate(Rutas.PersonajeCrear.ruta) {
                            popUpTo(Rutas.Inicio.ruta) { inclusive = true }
                        }


                    },
                    irAConjuros = {
                        navController.navigate(Rutas.Conjuros.ruta)
                    }

                )
            }
        }

        // LISTA DE PERSONAJES
        composable(
            route = Rutas.ListaPersonaje.ruta,
            enterTransition = { fadeIn(animationSpec = tween(600)) },
            exitTransition = { fadeOut(animationSpec = tween(600)) }
        ) {

            val usuario = usuarioActual
            PersonajeGuardadoView(
                vm = personajesVM,

                onCrear = { navController.navigate(Rutas.PersonajeCrear.ruta) },
                onAbrir = { id -> navController.navigate(Rutas.PersonajeEditar.of(id)) }
            )
        }

        // CREAR PERSONAJE
        composable(Rutas.PersonajeCrear.ruta) {
            val usuario = usuarioActual ?: return@composable
            PersonajeCrearView(
                vm = personajesVM,
                usuario = usuario,
                onCancel = { navController.popBackStack() },
                onSaved  = { navController.navigate(Rutas.Inicio.ruta) {
                    popUpTo(Rutas.PersonajeCrear.ruta) { inclusive = true }
                } }
            )
        }

        // EDITAR PERSONAJE
        composable(
            route = Rutas.PersonajeEditar.ruta,
            arguments = listOf(navArgument(Rutas.PersonajeEditar.ARG_ID){ type = NavType.LongType })
        ) { backStackEntry ->
            val usuario = usuarioActual ?: return@composable
            val id = backStackEntry.arguments?.getLong(Rutas.PersonajeEditar.ARG_ID) ?: -1L
            PersonajeEditarView(
                vm = personajesVM,
                usuario = usuario,
                personajeId = id,
                onCancel = { navController.popBackStack() },
                onSaved  = { navController.popBackStack() }
            )
        }

        // CONJUROS
        composable(
            route = Rutas.Conjuros.ruta,
            enterTransition = { fadeIn(animationSpec = tween(600)) },
            exitTransition = { fadeOut(animationSpec = tween(600)) }
        ) {
            ConjurosView(navController)
        }

    }
}
