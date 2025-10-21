package com.example.proyectodd

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.proyectodd.navegacion.NavegacionAuth
import com.example.proyectodd.ui.theme.TemaZazmorra
import com.example.proyectodd.viewmodel.AuthViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TemaZazmorra {
                val viewModel: AuthViewModel = viewModel()
                NavegacionAuth(viewModel = viewModel)
            }
        }
    }
}