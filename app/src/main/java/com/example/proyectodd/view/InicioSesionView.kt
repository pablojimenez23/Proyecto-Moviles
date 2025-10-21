package com.example.proyectodd.view

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyectodd.R
import com.example.proyectodd.viewmodel.InicioSesionViewModel

@Composable
fun PantallaInicioSesion(

    inicioSesionViewModel: InicioSesionViewModel,
    irARegistro: () -> Unit = {}
) {
    var correo = inicioSesionViewModel.correo
    var contrasena = inicioSesionViewModel.contrasena
    val mensaje by inicioSesionViewModel.mensaje.collectAsState()


    val negro = Color(0xFF000000)
    val gris900 = Color(0xFF111827)
    val rojo800 = Color(0xFF991B1B)
    val rojo900 = Color(0xFF7F1D1D)
    val rojo600 = Color(0xFFDC2626)
    val rojo500 = Color(0xFFEF4444)
    val rojo400 = Color(0xFFF87171)

    val infiniteTransition = rememberInfiniteTransition()
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    var mostrarContrasena by remember { mutableStateOf(false) }

    fun iniciarSesion() {
        if (correo.isNotEmpty() && contrasena.isNotEmpty()) {
            inicioSesionViewModel.iniciarSesion()
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
                Column(horizontalAlignment = Alignment.CenterHorizontally) {


                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_skull),
                            contentDescription = "Calavera",
                            tint = rojo600,
                            modifier = Modifier.size(64.dp).scale(scale).padding(bottom = 16.dp)
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


                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(4.dp, rojo800, RoundedCornerShape(8.dp)),
                        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                        shape = RoundedCornerShape(8.dp),
                        elevation = CardDefaults.cardElevation(24.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(brush = Brush.verticalGradient(colors = listOf(gris900, negro)))
                                .padding(32.dp)
                        ) {
                            Column {


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
                                    onValueChange = { inicioSesionViewModel.correo = it },
                                    modifier = Modifier.fillMaxWidth(),
                                    placeholder = { Text("aventurero@mazmorra.com", color = Color.Gray) },
                                    leadingIcon = { Icon(Icons.Default.Email, "Email", tint = rojo500) },
                                    singleLine = true,
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = rojo600,
                                        unfocusedBorderColor = rojo900,
                                        cursorColor = rojo600,
                                        focusedContainerColor = negro,
                                        unfocusedContainerColor = negro
                                    ),
                                    shape = RoundedCornerShape(4.dp)
                                )

                                Spacer(modifier = Modifier.height(24.dp))


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
                                    onValueChange = { inicioSesionViewModel.contrasena = it },
                                    modifier = Modifier.fillMaxWidth(),
                                    placeholder = { Text("Ingresa tu secreto", color = Color.Gray) },
                                    leadingIcon = { Icon(Icons.Default.Lock, "Contraseña", tint = rojo500) },
                                    trailingIcon = {
                                        IconButton(onClick = { mostrarContrasena = !mostrarContrasena }) {
                                            Icon(
                                                if (mostrarContrasena) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                                "Mostrar",
                                                tint = rojo600
                                            )
                                        }
                                    },
                                    visualTransformation = if (mostrarContrasena) VisualTransformation.None else PasswordVisualTransformation(),
                                    singleLine = true,
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = rojo600,
                                        unfocusedBorderColor = rojo900,
                                        cursorColor = rojo600,
                                        focusedContainerColor = negro,
                                        unfocusedContainerColor = negro
                                    ),
                                    shape = RoundedCornerShape(4.dp)
                                )

                                Spacer(modifier = Modifier.height(24.dp))


                                Button(
                                    onClick = { inicioSesionViewModel.iniciarSesion() },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(48.dp)
                                        .border(2.dp, rojo600, RoundedCornerShape(4.dp)),
                                    colors = ButtonDefaults.buttonColors(containerColor = rojo800),
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
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 2.sp,
                                        color = Color.White
                                    )
                                }

                                Spacer(modifier = Modifier.height(24.dp))
                                if (mensaje != null) {
                                    Text(
                                        text = mensaje ?: "" ,
                                        fontSize = 12.sp,
                                        color = Color.Red,
                                        modifier = Modifier.padding(top = 8.dp)
                                    )
                                }

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                     Divider(modifier = Modifier.weight(1f), color = rojo900)
                                    Text("O", modifier = Modifier.padding(horizontal = 16.dp), color = rojo900)
                                     Divider(modifier = Modifier.weight(1f), color = rojo900)
                                }

                                Spacer(modifier = Modifier.height(24.dp))


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
                                    Text("CREAR NUEVO USUARIO", fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 2.sp)
                                }
                            }
                        }
                    }

                    // Footer
                    Text(
                        text = "© 2025 Gremio de Maestros de Mazmorras",
                        fontSize = 12.sp,
                        color = rojo900,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 24.dp)
                    )
                }
            }
        }
    }
}