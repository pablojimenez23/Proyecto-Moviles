package com.example.proyectodd.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyectodd.model.Usuario

@Composable
fun PantallaInicio(usuario: Usuario, cerrarSesion: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(24.dp)
        ) {
            Text(
                text = "¡Bienvenido, ${usuario.nombre}!",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFDC2626)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Has entrado exitosamente a la mazmorra",
                fontSize = 16.sp,
                color = Color(0xFFEF4444)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(onClick = cerrarSesion) {
                Text("Cerrar Sesión")
            }
        }
    }
}