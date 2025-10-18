package com.example.proyectodd.navegacion

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.proyectodd.data.AppDataBase
import com.example.proyectodd.view.PantallaRegistro
import com.example.proyectodd.view.PantallaInicioSesion
import com.example.proyectodd.viewmodel.InicioSesionViewModel


import com.example.proyectodd.viewmodel.RegistroViewModel
import com.example.proyectodd.viewmodel.UsuarioViewModelFactory


@Composable
fun NavegacionAuth() {
    val controladorNav = rememberNavController()

    NavHost(
        navController = controladorNav,
        startDestination = "inicio_sesion"
    ) {

        composable("registro") {

            val context = LocalContext.current
            val db = AppDataBase.getDatabase(context)
            val dao = db.usuarioDao()
            val factory = UsuarioViewModelFactory(dao)



            val registroViewModel: RegistroViewModel = viewModel(factory = factory)


            PantallaRegistro(
                registroViewModel = registroViewModel,
                irAInicioSesion = { controladorNav.navigate("inicio_sesion") }
            )

                }

        composable("inicio_sesion") {

            val context = LocalContext.current
            val db = AppDataBase.getDatabase(context)
            val dao = db.usuarioDao()
            val factory = UsuarioViewModelFactory(dao)

            val inicioSesionViewModel: InicioSesionViewModel = viewModel(factory = factory)

            PantallaInicioSesion(
                inicioSesionViewModel = inicioSesionViewModel,
                irARegistro = {
                    controladorNav.navigate("registro")
                }
            )
        }



        }
    }
