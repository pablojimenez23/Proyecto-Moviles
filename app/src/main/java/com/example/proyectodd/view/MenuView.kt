package com.example.proyectodd.view

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyectodd.R
import com.example.proyectodd.model.Usuario


@Composable
fun MenuPrincipalScreen(usuario: Usuario, cerrarSesion: () -> Unit, creacionpersonaje: () -> Unit) {
    val negro = Color(0xFF000000)
    val rojoOscuro = Color(0xFF991B1B)
    val rojoMedio = Color(0xFFDC2626)
    val rojoClaro = Color(0xFFEF4444)
    val rojoMuyOscuro = Color(0xFF7F1D1D)
    val grisOscuro = Color(0xFF1F1F1F)

    var iniciandoCierreSesion by remember { mutableStateOf(false) }
    val alphaAnimacion by animateFloatAsState(
        targetValue = if (iniciandoCierreSesion) 0f else 1f,
        animationSpec = tween(durationMillis = 800),
        label = "fade_out"
    )

    LaunchedEffect(iniciandoCierreSesion) {
        if (iniciandoCierreSesion) {
            kotlinx.coroutines.delay(800)
            cerrarSesion()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(negro)
            .graphicsLayer(alpha = alphaAnimacion)
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

                    //Header
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 32.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_scroll),
                            contentDescription = "Pergamino del Gremio",
                            tint = rojoMedio,
                            modifier = Modifier
                                .size(80.dp)
                                .padding(bottom = 16.dp)
                        )

                        Text(
                            text = "MENÚ DEL AVENTURERO",
                            fontSize = 29.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Serif,
                            color = rojoMedio,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        Text(
                            text = "¡Bienvenido, ${usuario.nombre}!",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Serif,
                            color = rojoClaro,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Elige tu próximo destino",
                            fontSize = 14.sp,
                            color = rojoClaro,
                            textAlign = TextAlign.Center
                        )
                    }

                    //Menu con opciones
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(4.dp, rojoOscuro, RoundedCornerShape(8.dp)),
                        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
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
                                .padding(32.dp)
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                //Boton crear personajes
                                Button(
                                    onClick = { creacionpersonaje() },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(52.dp)
                                        .border(2.dp, rojoMedio, RoundedCornerShape(4.dp)),
                                    colors = ButtonDefaults.buttonColors(containerColor = rojoOscuro),
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
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 1.5.sp,
                                        color = Color.White
                                    )
                                }

                                Spacer(modifier = Modifier.height(24.dp))

                                //Boton ver personajes
                                Button(
                                    onClick = { /* TODO: Ver personajes guardados */ },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(52.dp)
                                        .border(2.dp, rojoMedio, RoundedCornerShape(4.dp)),
                                    colors = ButtonDefaults.buttonColors(containerColor = rojoMuyOscuro),
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_person_add),
                                        contentDescription = "Personajes",
                                        tint = Color.White,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        "VER PERSONAJES GUARDADOS",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 1.5.sp,
                                        color = Color.White
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
                                        "•",
                                        modifier = Modifier.padding(horizontal = 16.dp),
                                        color = rojoMuyOscuro,
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

                                //boton cerrar sesion
                                OutlinedButton(
                                    onClick = { iniciandoCierreSesion = true },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(52.dp),
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        contentColor = rojoClaro,
                                        containerColor = Color.Transparent
                                    ),
                                    border = BorderStroke(2.dp, rojoOscuro),
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_skull),
                                        contentDescription = "Salir",
                                        tint = rojoClaro,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        "CERRAR SESIÓN",
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
    }
}