package com.example.proyectodd.pantallas

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.draw.scale
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
import com.example.proyectodd.R

@Composable
fun PantallaInicioSesion(irARegistro: () -> Unit) {
    var correo by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var mostrarContrasena by remember { mutableStateOf(false) }

    // Estados de error
    var errorCorreo by remember { mutableStateOf("") }
    var errorContrasena by remember { mutableStateOf("") }

    // Colores a usar
    val negro = Color(0xFF000000)
    val gris900 = Color(0xFF111827)
    val rojo800 = Color(0xFF991B1B)
    val rojo900 = Color(0xFF7F1D1D)
    val rojo600 = Color(0xFFDC2626)
    val rojo500 = Color(0xFFEF4444)
    val rojo400 = Color(0xFFF87171)
    val rojo100 = Color(0xFFFEE2E2)

    // Animaciones
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    // Función de validación de email
    fun validarEmail(email: String): Boolean {
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()
        return email.matches(emailRegex)
    }

    // Validación en tiempo real del correo
    fun validarCorreoEnTiempoReal() {
        errorCorreo = when {
            correo.isEmpty() -> "El correo no puede estar vacío"
            correo.contains(" ") -> "El correo no puede contener espacios"
            !validarEmail(correo) -> "Formato de correo inválido"
            else -> ""
        }
    }

    // Validación en tiempo real de la contraseña
    fun validarContrasenaEnTiempoReal() {
        errorContrasena = when {
            contrasena.isEmpty() -> "La contraseña no puede estar vacía"
            contrasena.contains(" ") -> "La contraseña no puede contener espacios"
            contrasena.length < 6 -> "La contraseña debe tener al menos 6 caracteres"
            else -> ""
        }
    }

    // Función de validación del formulario completo
    fun validarFormulario(): Boolean {
        validarCorreoEnTiempoReal()
        validarContrasenaEnTiempoReal()
        return errorCorreo.isEmpty() && errorContrasena.isEmpty()
    }

    // Función para iniciar sesión
    fun iniciarSesion() {
        if (validarFormulario()) {
            // Aquí iría la lógica de autenticación
            println("Iniciando sesión con: $correo")
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(negro)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 448.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    //Header
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 32.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_skull),
                            contentDescription = "Calavera",
                            tint = rojo600,
                            modifier = Modifier
                                .size(64.dp)
                                .scale(scale)
                                .padding(bottom = 16.dp)
                        )

                        Text(
                            text = "ENTRA A LA MAZMORRA",
                            fontSize = 29.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Serif,
                            color = rojo600,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        Text(
                            text = "Inicia sesión para comenzar tu aventura",
                            fontSize = 14.sp,
                            color = rojo400,
                            textAlign = TextAlign.Center
                        )
                    }

                    //Formulario
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(4.dp, rojo800, RoundedCornerShape(8.dp)),
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
                                        colors = listOf(gris900, negro)
                                    )
                                )
                                .padding(32.dp)
                        ) {
                            Column {
                                //Correo
                                Text(
                                    text = "CORREO ELECTRÓNICO",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = rojo500,
                                    letterSpacing = 2.sp,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )

                                OutlinedTextField(
                                    value = correo,
                                    onValueChange = {
                                        correo = it
                                        // Validar en tiempo real al escribir
                                        if (correo.isNotEmpty()) {
                                            validarCorreoEnTiempoReal()
                                        } else {
                                            errorCorreo = ""
                                        }
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    placeholder = {
                                        Text("aventurero@mazmorra.com", color = Color.Gray)
                                    },
                                    leadingIcon = {
                                        Icon(
                                            Icons.Default.Email,
                                            "Email",
                                            tint = if (errorCorreo.isEmpty()) rojo500 else rojo500
                                        )
                                    },
                                    isError = errorCorreo.isNotEmpty(),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = rojo600,
                                        unfocusedBorderColor = rojo900,
                                        errorBorderColor = rojo500,
                                        focusedTextColor = rojo100,
                                        unfocusedTextColor = rojo100,
                                        cursorColor = rojo600,
                                        focusedContainerColor = negro,
                                        unfocusedContainerColor = negro,
                                        errorContainerColor = negro
                                    ),
                                    shape = RoundedCornerShape(4.dp),
                                    singleLine = true
                                )

                                // Mensaje de error del correo
                                if (errorCorreo.isNotEmpty()) {
                                    Row(
                                        modifier = Modifier.padding(start = 16.dp, top = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            Icons.Default.Warning,
                                            contentDescription = "Error",
                                            tint = rojo500,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = errorCorreo,
                                            color = rojo500,
                                            fontSize = 12.sp
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(24.dp))

                                //Contraseña
                                Text(
                                    text = "CONTRASEÑA",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = rojo500,
                                    letterSpacing = 2.sp,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )

                                OutlinedTextField(
                                    value = contrasena,
                                    onValueChange = {
                                        contrasena = it
                                        // Validar en tiempo real al escribir
                                        if (contrasena.isNotEmpty()) {
                                            validarContrasenaEnTiempoReal()
                                        } else {
                                            errorContrasena = ""
                                        }
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    placeholder = {
                                        Text("Ingresa tu secreto", color = Color.Gray)
                                    },
                                    leadingIcon = {
                                        Icon(Icons.Default.Lock, "Contraseña", tint = rojo500)
                                    },
                                    trailingIcon = {
                                        IconButton(onClick = { mostrarContrasena = !mostrarContrasena }) {
                                            Icon(
                                                if (mostrarContrasena) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                                "Mostrar",
                                                tint = rojo600
                                            )
                                        }
                                    },
                                    isError = errorContrasena.isNotEmpty(),
                                    visualTransformation = if (mostrarContrasena)
                                        VisualTransformation.None
                                    else
                                        PasswordVisualTransformation(),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = rojo600,
                                        unfocusedBorderColor = rojo900,
                                        errorBorderColor = rojo500,
                                        focusedTextColor = rojo100,
                                        unfocusedTextColor = rojo100,
                                        cursorColor = rojo600,
                                        focusedContainerColor = negro,
                                        unfocusedContainerColor = negro,
                                        errorContainerColor = negro
                                    ),
                                    shape = RoundedCornerShape(4.dp),
                                    singleLine = true
                                )

                                // Mensaje de error de contraseña
                                if (errorContrasena.isNotEmpty()) {
                                    Row(
                                        modifier = Modifier.padding(start = 16.dp, top = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            Icons.Default.Warning,
                                            contentDescription = "Error",
                                            tint = rojo500,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = errorContrasena,
                                            color = rojo500,
                                            fontSize = 12.sp
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(24.dp))

                                //Boton de login
                                Button(
                                    onClick = { iniciarSesion() },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(48.dp)
                                        .border(2.dp, rojo600, RoundedCornerShape(4.dp)),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFF991B1B)
                                    ),
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_sword_cross),
                                        contentDescription = "Espadas",
                                        tint = Color.White,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        "ENTRAR A LA MAZMORRA",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 2.sp,
                                        color = Color.White
                                    )
                                }

                                Spacer(modifier = Modifier.height(24.dp))

                                //Separadores
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    HorizontalDivider(
                                        modifier = Modifier.weight(1f),
                                        color = rojo900
                                    )
                                    Text(
                                        "O",
                                        modifier = Modifier.padding(horizontal = 16.dp),
                                        color = Color(0xFF7F1D1D),
                                        fontSize = 14.sp
                                    )
                                    HorizontalDivider(
                                        modifier = Modifier.weight(1f),
                                        color = rojo900
                                    )
                                }

                                Spacer(modifier = Modifier.height(24.dp))

                                //Boton registro
                                OutlinedButton(
                                    onClick = irARegistro,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(48.dp),
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        containerColor = Color.Transparent,
                                        contentColor = rojo500
                                    ),
                                    border = BorderStroke(2.dp, rojo800),
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text(
                                        "CREAR NUEVO PERSONAJE",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 2.sp
                                    )
                                }
                            }
                        }
                    }

                    //Footer
                    Text(
                        text = "© 2025 Gremio de Maestros de Mazmorras",
                        fontSize = 12.sp,
                        color = Color(0xFF7F1D1D),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 24.dp)
                    )
                }
            }
        }
    }
}