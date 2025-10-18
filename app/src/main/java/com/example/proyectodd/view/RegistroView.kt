package com.example.proyectodd.view

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.proyectodd.viewmodel.RegistroViewModel

@Composable
fun PantallaRegistro(
    viewModel: RegistroViewModel = viewModel(),
    irAInicioSesion: () -> Unit = {}
) {
    val nombre by viewModel.nombre
    val correo by viewModel.correo
    val contrasena by viewModel.contrasena
    val confirmarContrasena by viewModel.confirmarContrasena

    val errorNombre by viewModel.errorNombre
    val errorCorreo by viewModel.errorCorreo
    val errorContrasena by viewModel.errorContrasena
    val errorConfirmarContrasena by viewModel.errorConfirmarContrasena


    val negro = Color(0xFF000000)
    val rojo600 = Color(0xFFDC2626)
    val rojo500 = Color(0xFFEF4444)
    val rojo400 = Color(0xFFF87171)
    val verde = Color(0xFF10B981)


    val infiniteTransition = rememberInfiniteTransition()
    val scale by infiniteTransition.animateFloat(
        1f, 1.1f,
        animationSpec = infiniteRepeatable(
            tween(1000, easing = FastOutSlowInEasing),
            RepeatMode.Reverse
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(negro)
            .padding(16.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            Icon(
                Icons.Default.Shield,
                contentDescription = "Escudo",
                tint = rojo600,
                modifier = Modifier.size(64.dp).scale(scale).padding(bottom = 16.dp)
            )

            Text("CREAR PERSONAJE", fontSize = 29.sp, fontWeight = FontWeight.Bold, color = rojo600)
            Text("Comienza tu viaje legendario", fontSize = 14.sp, color = rojo400, modifier = Modifier.padding(bottom = 16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {


                    OutlinedTextField(
                        value = nombre,
                        onValueChange = {
                            viewModel.nombre.value = it
                            viewModel.validarNombreEnTiempoReal()
                        },
                        label = { Text("Nombre del personaje") },
                        isError = errorNombre.isNotEmpty(),
                        singleLine = true
                    )
                    if (errorNombre.isNotEmpty()) Text(errorNombre, color = rojo500, fontSize = 12.sp)

                    Spacer(modifier = Modifier.height(16.dp))


                    OutlinedTextField(
                        value = correo,
                        onValueChange = {
                            viewModel.correo.value = it
                            viewModel.validarCorreoEnTiempoReal()
                        },
                        label = { Text("Correo electrónico") },
                        isError = errorCorreo.isNotEmpty(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                    )
                    if (errorCorreo.isNotEmpty()) Text(errorCorreo, color = rojo500, fontSize = 12.sp)

                    Spacer(modifier = Modifier.height(16.dp))


                    var mostrarContrasena by remember { mutableStateOf(false) }
                    OutlinedTextField(
                        value = contrasena,
                        onValueChange = {
                            viewModel.contrasena.value = it
                            viewModel.validarContrasenaEnTiempoReal()
                        },
                        label = { Text("Contraseña") },
                        isError = errorContrasena.isNotEmpty(),
                        visualTransformation = if (mostrarContrasena) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = { mostrarContrasena = !mostrarContrasena }) {
                                Icon(if (mostrarContrasena) Icons.Default.VisibilityOff else Icons.Default.Visibility, "")
                            }
                        },
                        singleLine = true
                    )
                    if (errorContrasena.isNotEmpty()) Text(errorContrasena, color = rojo500, fontSize = 12.sp)
                    else if (contrasena.isNotEmpty()) Text("✓ Contraseña segura", color = verde, fontSize = 12.sp)

                    Spacer(modifier = Modifier.height(16.dp))


                    var mostrarConfirmar by remember { mutableStateOf(false) }
                    OutlinedTextField(
                        value = confirmarContrasena,
                        onValueChange = {
                            viewModel.confirmarContrasena.value = it
                            viewModel.validarConfirmarContrasenaEnTiempoReal()
                        },
                        label = { Text("Confirmar contraseña") },
                        isError = errorConfirmarContrasena.isNotEmpty(),
                        visualTransformation = if (mostrarConfirmar) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = { mostrarConfirmar = !mostrarConfirmar }) {
                                Icon(if (mostrarConfirmar) Icons.Default.VisibilityOff else Icons.Default.Visibility, "")
                            }
                        },
                        singleLine = true
                    )
                    if (errorConfirmarContrasena.isNotEmpty()) Text(errorConfirmarContrasena, color = rojo500, fontSize = 12.sp)

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = { viewModel.registrar() },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("CREAR AVENTURERO")
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    TextButton(onClick = { irAInicioSesion() }) {
                        Text("¿Ya tienes una cuenta? Inicia sesión", color = rojo400)
                    }
                }
            }
        }
    }
}









