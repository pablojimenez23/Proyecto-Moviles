package com.example.proyectodd.pantallas

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.proyectodd.R
import com.example.proyectodd.ui.state.AuthUIState
import com.example.proyectodd.viewmodel.AuthViewModel

@Composable
fun PantallaRegistro(
    viewModel: AuthViewModel,
    irAInicioSesion: () -> Unit,
    alExito: () -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var confirmarContrasena by remember { mutableStateOf("") }
    var mostrarContrasena by remember { mutableStateOf(false) }

    val estadoAuth by viewModel.estadoAuth.collectAsStateWithLifecycle()

    // Colores
    val rojoOscuro = Color(0xFF991B1B)
    val rojoMedio = Color(0xFFDC2626)
    val rojoClaro = Color(0xFFEF4444)
    val rojoMuyOscuro = Color(0xFF7F1D1D)
    val negro = Color(0xFF000000)
    val grisOscuro = Color(0xFF1F1F1F)

    LaunchedEffect(estadoAuth) {
        if (estadoAuth is AuthUIState.Exito) {
            alExito()
            viewModel.resetearEstado()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(negro),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            //header
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(bottom = 32.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_shield_outline),
                    contentDescription = "Escudo",
                    tint = rojoMedio,
                    modifier = Modifier.size(64.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "CREAR PERSONAJE",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Serif,
                    color = rojoMedio,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Comienza tu viaje legendario",
                    fontSize = 14.sp,
                    color = rojoClaro,
                    textAlign = TextAlign.Center
                )
            }

            //formulario de registro
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .border(4.dp, rojoOscuro, RoundedCornerShape(8.dp)),
                colors = CardDefaults.cardColors(
                    containerColor = Color.Transparent
                ),
                shape = RoundedCornerShape(8.dp),
                elevation = CardDefaults.cardElevation(24.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(grisOscuro, negro)
                            )
                        )
                ) {
                    Column(
                        modifier = Modifier.padding(32.dp)
                    ) {
                        //nombre
                        Text(
                            text = "NOMBRE",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = rojoClaro,
                            letterSpacing = 2.sp,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        OutlinedTextField(
                            value = nombre,
                            onValueChange = { nombre = it },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = {
                                Text("Aragorn el Valiente", color = Color(0xFFFCA5A5))
                            },
                            leadingIcon = {
                                Icon(
                                    Icons.Default.Person,
                                    contentDescription = "Nombre",
                                    tint = rojoClaro,
                                    modifier = Modifier.size(16.dp)
                                )
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = rojoMedio,
                                unfocusedBorderColor = rojoMuyOscuro,
                                focusedTextColor = Color(0xFFFCA5A5),
                                unfocusedTextColor = Color(0xFFFCA5A5),
                                cursorColor = rojoMedio,
                                focusedContainerColor = negro,
                                unfocusedContainerColor = negro
                            ),
                            shape = RoundedCornerShape(4.dp),
                            singleLine = true,
                            enabled = estadoAuth !is AuthUIState.Cargando
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        //Correo
                        Text(
                            text = "CORREO ELECTRÓNICO",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = rojoClaro,
                            letterSpacing = 2.sp,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        OutlinedTextField(
                            value = correo,
                            onValueChange = { correo = it },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = {
                                Text("aventurero@mazmorra.com", color = Color(0xFFFCA5A5))
                            },
                            leadingIcon = {
                                Icon(
                                    Icons.Default.Email,
                                    contentDescription = "Email",
                                    tint = rojoClaro,
                                    modifier = Modifier.size(16.dp)
                                )
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = rojoMedio,
                                unfocusedBorderColor = rojoMuyOscuro,
                                focusedTextColor = Color(0xFFFCA5A5),
                                unfocusedTextColor = Color(0xFFFCA5A5),
                                cursorColor = rojoMedio,
                                focusedContainerColor = negro,
                                unfocusedContainerColor = negro
                            ),
                            shape = RoundedCornerShape(4.dp),
                            singleLine = true,
                            enabled = estadoAuth !is AuthUIState.Cargando
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        //contraseña
                        Text(
                            text = "CONTRASEÑA",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = rojoClaro,
                            letterSpacing = 2.sp,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        OutlinedTextField(
                            value = contrasena,
                            onValueChange = { contrasena = it },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = {
                                Text("Crea una contraseña segura", color = Color(0xFFFCA5A5))
                            },
                            leadingIcon = {
                                Icon(
                                    Icons.Default.Lock,
                                    contentDescription = "Contraseña",
                                    tint = rojoClaro,
                                    modifier = Modifier.size(16.dp)
                                )
                            },
                            trailingIcon = {
                                IconButton(onClick = { mostrarContrasena = !mostrarContrasena }) {
                                    Icon(
                                        if (mostrarContrasena) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                        contentDescription = "Toggle",
                                        tint = rojoMedio
                                    )
                                }
                            },
                            visualTransformation = if (mostrarContrasena)
                                VisualTransformation.None
                            else
                                PasswordVisualTransformation(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = rojoMedio,
                                unfocusedBorderColor = rojoMuyOscuro,
                                focusedTextColor = Color(0xFFFCA5A5),
                                unfocusedTextColor = Color(0xFFFCA5A5),
                                cursorColor = rojoMedio,
                                focusedContainerColor = negro,
                                unfocusedContainerColor = negro
                            ),
                            shape = RoundedCornerShape(4.dp),
                            singleLine = true,
                            enabled = estadoAuth !is AuthUIState.Cargando
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        //confirmar contraseña
                        Text(
                            text = "CONFIRMAR CONTRASEÑA",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = rojoClaro,
                            letterSpacing = 2.sp,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        OutlinedTextField(
                            value = confirmarContrasena,
                            onValueChange = { confirmarContrasena = it },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = {
                                Text("Confirma tu contraseña", color = Color(0xFFFCA5A5))
                            },
                            leadingIcon = {
                                Icon(
                                    Icons.Default.Lock,
                                    contentDescription = "Confirmar",
                                    tint = rojoClaro,
                                    modifier = Modifier.size(16.dp)
                                )
                            },
                            visualTransformation = PasswordVisualTransformation(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = rojoMedio,
                                unfocusedBorderColor = rojoMuyOscuro,
                                focusedTextColor = Color(0xFFFCA5A5),
                                unfocusedTextColor = Color(0xFFFCA5A5),
                                cursorColor = rojoMedio,
                                focusedContainerColor = negro,
                                unfocusedContainerColor = negro
                            ),
                            shape = RoundedCornerShape(4.dp),
                            singleLine = true,
                            enabled = estadoAuth !is AuthUIState.Cargando
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        //boton de registro
                        Button(
                            onClick = {
                                viewModel.registrarUsuario(
                                    nombre, correo, contrasena, confirmarContrasena
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                                .border(2.dp, rojoMedio, RoundedCornerShape(4.dp)),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = rojoOscuro,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(4.dp),
                            enabled = estadoAuth !is AuthUIState.Cargando
                        ) {
                            if (estadoAuth is AuthUIState.Cargando) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    color = Color.White,
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_shield_outline),
                                    contentDescription = "Crear",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    "CREAR AVENTURERO",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.5.sp,
                                    color = Color.White
                                )
                            }
                        }

                        if (estadoAuth is AuthUIState.Error) {
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = (estadoAuth as AuthUIState.Error).mensaje,
                                color = rojoClaro,
                                fontSize = 12.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        //divisor
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(1.dp)
                                    .background(rojoMuyOscuro)
                            )
                            Text(
                                "O",
                                modifier = Modifier.padding(horizontal = 16.dp),
                                color = Color(0xFF7F1D1D),
                                fontSize = 14.sp
                            )
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(1.dp)
                                    .background(rojoMuyOscuro)
                            )
                        }

                        Spacer(modifier = Modifier.height(24.dp))
                        OutlinedButton(
                            onClick = irAInicioSesion,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = rojoClaro,
                                containerColor = Color.Transparent
                            ),
                            border = androidx.compose.foundation.BorderStroke(2.dp, rojoOscuro),
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                "YA TENGO UN PERSONAJE",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.5.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            //footer
            Text(
                text = "© 2025 Gremio de Maestros de Mazmorras",
                fontSize = 10.sp,
                color = rojoMuyOscuro,
                textAlign = TextAlign.Center
            )
        }
    }
}