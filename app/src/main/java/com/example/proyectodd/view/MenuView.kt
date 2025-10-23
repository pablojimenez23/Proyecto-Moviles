package com.example.proyectodd.view

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyectodd.R
import com.example.proyectodd.data.model.Usuario
import kotlinx.coroutines.delay


@Composable
fun MenuPrincipalScreen(usuario: Usuario, cerrarSesion: () -> Unit, creacionpersonaje:()-> Unit) {
    val negro = Color(0xFF0D0D0D)
    val rojo600 = Color(0xFFB71C1C)
    val rojo800 = Color(0xFF7A0000)
    val rojo900 = Color(0xFF3D0000)
    val gris900 = Color(0xFF1E1E1E)



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
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 32.dp)
                    ) {

                        Icon(
                            painter = painterResource(id = R.drawable.ic_shield_outline),
                            contentDescription = "Logo del Gremio",
                            tint = rojo600,
                            modifier = Modifier
                                .size(80.dp)
                                .padding(bottom = 16.dp)
                        )

                        Text(
                            text = "MENÚ DEL AVENTURERO",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Serif,
                            color = rojo600,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Text(
                            text = "¡Bienvenido, ${usuario.nombre}!",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFDC2626)
                        )

                        Text(
                            text = "Elige tu próximo destino",
                            fontSize = 14.sp,
                            color = rojo900,
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
                                .background(
                                    brush = Brush.verticalGradient(
                                        colors = listOf(gris900, negro)
                                    )
                                )
                                .padding(32.dp)
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {


                                Button(
                                    onClick = { creacionpersonaje},
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(50.dp)
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
                                        "CREAR PERSONAJE",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 2.sp,
                                        color = Color.White
                                    )
                                }

                                Spacer(modifier = Modifier.height(24.dp))


                                Button(
                                    onClick = { /* TODO: Ver personajes guardados */ },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(50.dp)
                                        .border(2.dp, rojo600, RoundedCornerShape(4.dp)),
                                    colors = ButtonDefaults.buttonColors(containerColor = rojo900),
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_person_add),
                                        contentDescription = "Pergamino",
                                        tint = Color.White,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        "VER PERSONAJES GUARDADOS",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 2.sp,
                                        color = Color.White
                                    )
                                }

                                Spacer(modifier = Modifier.height(24.dp))


                                Button(
                                    onClick = { /* TODO: Cerrar sesión */ },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(50.dp)
                                        .border(2.dp, Color.Gray, RoundedCornerShape(4.dp)),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray),
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_skull),
                                        contentDescription = "Salir",
                                        tint = Color.White,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        "CERRAR SESIÓN",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 2.sp,
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    }


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