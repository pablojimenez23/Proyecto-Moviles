package com.example.proyectodd.pantallas

import androidx.compose.animation.core.*
import androidx.compose.ui.text.input.VisualTransformation
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyectodd.R

@Composable
fun PantallaRegistro(irAInicioSesion: () -> Unit) {
    var nombre by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var confirmarContrasena by remember { mutableStateOf("") }
    var mostrarContrasena by remember { mutableStateOf(false) }
    var mostrarConfirmarContrasena by remember { mutableStateOf(false) }

    // Estados de error
    var errorNombre by remember { mutableStateOf("") }
    var errorCorreo by remember { mutableStateOf("") }
    var errorContrasena by remember { mutableStateOf("") }
    var errorConfirmarContrasena by remember { mutableStateOf("") }

    //Colores a usar
    val negro = Color(0xFF000000)
    val gris900 = Color(0xFF111827)
    val rojo800 = Color(0xFF991B1B)
    val rojo900 = Color(0xFF7F1D1D)
    val rojo600 = Color(0xFFDC2626)
    val rojo500 = Color(0xFFEF4444)
    val rojo400 = Color(0xFFF87171)
    val rojo100 = Color(0xFFFEE2E2)
    val verde = Color(0xFF10B981)

    //Animaciones
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

    // Validación del nombre en tiempo real
    fun validarNombreEnTiempoReal() {
        errorNombre = when {
            nombre.isEmpty() -> "El nombre no puede estar vacío"
            nombre.length < 3 -> "El nombre debe tener al menos 3 caracteres"
            nombre.length > 30 -> "El nombre no puede tener más de 30 caracteres"
            !nombre.all { it.isLetter() || it.isWhitespace() } -> "El nombre solo puede contener letras"
            else -> ""
        }
    }

    // Validación del correo en tiempo real
    fun validarCorreoEnTiempoReal() {
        errorCorreo = when {
            correo.isEmpty() -> "El correo no puede estar vacío"
            correo.contains(" ") -> "El correo no puede contener espacios"
            !validarEmail(correo) -> "Formato de correo inválido"
            else -> ""
        }
    }

    // Validación de la contraseña en tiempo real
    fun validarContrasenaEnTiempoReal() {
        errorContrasena = when {
            contrasena.isEmpty() -> "La contraseña no puede estar vacía"
            contrasena.contains(" ") -> "La contraseña no puede contener espacios"
            contrasena.length < 8 -> "Mínimo 8 caracteres"
            !contrasena.any { it.isUpperCase() } -> "Debe tener al menos una mayúscula"
            !contrasena.any { it.isLowerCase() } -> "Debe tener al menos una minúscula"
            !contrasena.any { it.isDigit() } -> "Debe tener al menos un número"
            !contrasena.any { !it.isLetterOrDigit() } -> "Debe tener al menos un carácter especial"
            else -> ""
        }
    }

    // Validación de confirmar contraseña en tiempo real
    fun validarConfirmarContrasenaEnTiempoReal() {
        errorConfirmarContrasena = when {
            confirmarContrasena.isEmpty() -> "Debes confirmar la contraseña"
            contrasena != confirmarContrasena -> "Las contraseñas no coinciden"
            else -> ""
        }
    }

    // Función de validación del formulario completo
    fun validarFormulario(): Boolean {
        validarNombreEnTiempoReal()
        validarCorreoEnTiempoReal()
        validarContrasenaEnTiempoReal()
        validarConfirmarContrasenaEnTiempoReal()

        return errorNombre.isEmpty() &&
                errorCorreo.isEmpty() &&
                errorContrasena.isEmpty() &&
                errorConfirmarContrasena.isEmpty()
    }

    // Función para registrar
    fun registrar() {
        if (validarFormulario()) {
            // Aquí iría la lógica de registro
            println("Registrando usuario: $nombre - $correo")
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
                            painter = painterResource(id = R.drawable.ic_shield_outline),
                            contentDescription = "Escudo",
                            tint = rojo600,
                            modifier = Modifier
                                .size(64.dp)
                                .scale(scale)
                                .padding(bottom = 16.dp)
                        )

                        Text(
                            text = "CREAR PERSONAJE",
                            fontSize = 29.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Serif,
                            color = rojo600,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        Text(
                            text = "Comienza tu viaje legendario",
                            fontSize = 14.sp,
                            color = rojo400,
                            textAlign = TextAlign.Center
                        )
                    }

                    //Formulario de registro
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
                                //Nombre
                                Text(
                                    text = "NOMBRE DEL PERSONAJE",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = rojo500,
                                    letterSpacing = 2.sp,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )

                                OutlinedTextField(
                                    value = nombre,
                                    onValueChange = {
                                        nombre = it
                                        if (nombre.isNotEmpty()) {
                                            validarNombreEnTiempoReal()
                                        } else {
                                            errorNombre = ""
                                        }
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    placeholder = {
                                        Text("Aragorn el Valiente", color = Color.Gray)
                                    },
                                    leadingIcon = {
                                        Icon(Icons.Default.Person, "Nombre", tint = rojo500)
                                    },
                                    trailingIcon = {
                                        if (nombre.isNotEmpty() && errorNombre.isEmpty()) {
                                            Icon(
                                                Icons.Default.CheckCircle,
                                                "Válido",
                                                tint = verde
                                            )
                                        }
                                    },
                                    isError = errorNombre.isNotEmpty(),
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

                                if (errorNombre.isNotEmpty()) {
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
                                            text = errorNombre,
                                            color = rojo500,
                                            fontSize = 12.sp
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(24.dp))

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
                                        Icon(Icons.Default.Email, "Email", tint = rojo500)
                                    },
                                    trailingIcon = {
                                        if (correo.isNotEmpty() && errorCorreo.isEmpty()) {
                                            Icon(
                                                Icons.Default.CheckCircle,
                                                "Válido",
                                                tint = verde
                                            )
                                        }
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
                                        if (contrasena.isNotEmpty()) {
                                            validarContrasenaEnTiempoReal()
                                            // Revalidar confirmación si ya se llenó
                                            if (confirmarContrasena.isNotEmpty()) {
                                                validarConfirmarContrasenaEnTiempoReal()
                                            }
                                        } else {
                                            errorContrasena = ""
                                        }
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    placeholder = {
                                        Text("Crea una contraseña segura", color = Color.Gray)
                                    },
                                    leadingIcon = {
                                        Icon(Icons.Default.Lock, "Contraseña", tint = rojo500)
                                    },
                                    trailingIcon = {
                                        Row {
                                            if (contrasena.isNotEmpty() && errorContrasena.isEmpty()) {
                                                Icon(
                                                    Icons.Default.CheckCircle,
                                                    "Válido",
                                                    tint = verde,
                                                    modifier = Modifier.padding(end = 8.dp)
                                                )
                                            }
                                            IconButton(onClick = { mostrarContrasena = !mostrarContrasena }) {
                                                Icon(
                                                    if (mostrarContrasena) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                                    "Mostrar",
                                                    tint = rojo600
                                                )
                                            }
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
                                } else if (contrasena.isNotEmpty()) {
                                    // Indicador de fortaleza
                                    Text(
                                        text = "✓ Contraseña segura",
                                        color = verde,
                                        fontSize = 12.sp,
                                        modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.height(24.dp))

                                //Confirmar Contraseña
                                Text(
                                    text = "CONFIRMAR CONTRASEÑA",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = rojo500,
                                    letterSpacing = 2.sp,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )

                                OutlinedTextField(
                                    value = confirmarContrasena,
                                    onValueChange = {
                                        confirmarContrasena = it
                                        if (confirmarContrasena.isNotEmpty()) {
                                            validarConfirmarContrasenaEnTiempoReal()
                                        } else {
                                            errorConfirmarContrasena = ""
                                        }
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    placeholder = {
                                        Text("Confirma tu contraseña", color = Color.Gray)
                                    },
                                    leadingIcon = {
                                        Icon(Icons.Default.Lock, "Confirmar", tint = rojo500)
                                    },
                                    trailingIcon = {
                                        Row {
                                            if (confirmarContrasena.isNotEmpty() && errorConfirmarContrasena.isEmpty()) {
                                                Icon(
                                                    Icons.Default.CheckCircle,
                                                    "Válido",
                                                    tint = verde,
                                                    modifier = Modifier.padding(end = 8.dp)
                                                )
                                            }
                                            IconButton(onClick = { mostrarConfirmarContrasena = !mostrarConfirmarContrasena }) {
                                                Icon(
                                                    if (mostrarConfirmarContrasena) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                                    "Mostrar",
                                                    tint = rojo600
                                                )
                                            }
                                        }
                                    },
                                    isError = errorConfirmarContrasena.isNotEmpty(),
                                    visualTransformation = if (mostrarConfirmarContrasena)
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

                                if (errorConfirmarContrasena.isNotEmpty()) {
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
                                            text = errorConfirmarContrasena,
                                            color = rojo500,
                                            fontSize = 12.sp
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(24.dp))

                                // Requisitos de contraseña
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = CardDefaults.cardColors(
                                        containerColor = Color(0xFF1F1F1F)
                                    )
                                ) {
                                    Column(
                                        modifier = Modifier.padding(12.dp)
                                    ) {
                                        Text(
                                            text = "La contraseña debe cumplir:",
                                            fontSize = 12.sp,
                                            color = rojo400,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))

                                        RequisitoContrasena("Mínimo 8 caracteres", contrasena.length >= 8)
                                        RequisitoContrasena("Una mayúscula", contrasena.any { it.isUpperCase() })
                                        RequisitoContrasena("Una minúscula", contrasena.any { it.isLowerCase() })
                                        RequisitoContrasena("Un número", contrasena.any { it.isDigit() })
                                        RequisitoContrasena("Un carácter especial", contrasena.any { !it.isLetterOrDigit() })
                                    }
                                }

                                Spacer(modifier = Modifier.height(24.dp))

                                //Boton Crear
                                Button(
                                    onClick = { registrar() },
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
                                        painter = painterResource(id = R.drawable.ic_shield_outline),
                                        contentDescription = "Escudo",
                                        tint = Color.White,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        "CREAR AVENTURERO",
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

                                //Boton Login
                                OutlinedButton(
                                    onClick = irAInicioSesion,
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
                                        "YA TENGO UN PERSONAJE",
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

// Mostrar Requisitos
@Composable
fun RequisitoContrasena(texto: String, cumple: Boolean) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 2.dp)
    ) {
        Icon(
            if (cumple) Icons.Default.CheckCircle else Icons.Default.Cancel,
            contentDescription = null,
            tint = if (cumple) Color(0xFF10B981) else Color(0xFF6B7280),
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = texto,
            fontSize = 11.sp,
            color = if (cumple) Color(0xFF10B981) else Color(0xFF9CA3AF)
        )
    }
}