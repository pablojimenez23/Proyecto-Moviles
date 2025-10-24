package com.example.proyectodd.view

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyectodd.R
import kotlinx.coroutines.delay

@Composable
fun PantallaSplash(onSplashFinished: () -> Unit) {
    var startAnimation by remember { mutableStateOf(false) }
    var shouldNavigate by remember { mutableStateOf(false) }
    var progress by remember { mutableIntStateOf(0) }

    val alphaAnim = animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(
            durationMillis = 1000,
            easing = FastOutSlowInEasing
        ),
        label = "alpha"
    )

    val scaleAnim = animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0.85f,
        animationSpec = tween(
            durationMillis = 1000,
            easing = FastOutSlowInEasing
        ),
        label = "scale"
    )

    //Animacon del dragon
    val infiniteTransition = rememberInfiniteTransition(label = "dragon")
    val dragonRotation by infiniteTransition.animateFloat(
        initialValue = -5f,
        targetValue = 5f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = EaseInOutCubic),
            repeatMode = RepeatMode.Reverse
        ),
        label = "rotation"
    )

    //Escala
    val dragonScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = EaseInOutCubic),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    //Brillo del dragon
    val dragonAlpha by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = EaseInOutCubic),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )

    //Pulso de la barra
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = EaseInOutCubic),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    //Animacion del proceso
    LaunchedEffect(Unit) {
        startAnimation = true
        //Incrementar barra de carga
        for (i in 1..100) {
            progress = i
            delay(25)
        }
        startAnimation = false
        delay(800)
        shouldNavigate = true
    }

    LaunchedEffect(shouldNavigate) {
        if (shouldNavigate) {
            onSplashFinished()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            //Logo D&D
            Image(
                painter = painterResource(id = R.drawable.dnd_logo),
                contentDescription = "Dungeons & Dragons Logo",
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .scale(scaleAnim.value)
                    .alpha(alphaAnim.value)
            )

            Spacer(modifier = Modifier.height(60.dp))

            Box(
                modifier = Modifier
                    .size(80.dp)
                    .alpha(alphaAnim.value),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.size(80.dp)) {
                    val centerX = size.width / 2
                    val centerY = size.height / 2
                    val radius = size.minDimension / 2

                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                Color(0xFFDC2626).copy(alpha = dragonAlpha * 0.3f),
                                Color(0xFFEF4444).copy(alpha = dragonAlpha * 0.2f),
                                Color.Transparent
                            )
                        ),
                        radius = radius * 0.8f,
                        center = Offset(centerX, centerY)
                    )
                    for (i in 0 until 8) {
                        val angle = (i * 45f + (dragonRotation * 10)) * (Math.PI / 180f)
                        val distance = radius * 0.7f
                        val x = centerX + (distance * kotlin.math.cos(angle)).toFloat()
                        val y = centerY + (distance * kotlin.math.sin(angle)).toFloat()

                        drawCircle(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    Color(0xFFEF4444).copy(alpha = dragonAlpha * 0.8f),
                                    Color.Transparent
                                )
                            ),
                            radius = 3.dp.toPx(),
                            center = Offset(x, y)
                        )
                    }
                }

                //Icono de dragon
                Image(
                    painter = painterResource(id = R.drawable.dragon_icon),
                    contentDescription = "Dragón",
                    modifier = Modifier
                        .size(50.dp)
                        .scale(dragonScale)
                        .rotate(dragonRotation)
                        .alpha(dragonAlpha)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            //Barra de carga estilo
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .alpha(alphaAnim.value)
            ) {
               //texto superior
                Text(
                    text = "PREPARANDO AVENTURA",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Serif,
                    color = Color(0xFFEF4444),
                    letterSpacing = 3.sp,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(28.dp)
                        .border(
                            width = 2.dp,
                            color = Color(0xFF991B1B),
                            shape = RoundedCornerShape(4.dp)
                        )
                        .background(
                            color = Color(0xFF1A0000),
                            shape = RoundedCornerShape(4.dp)
                        )
                ) {
                    //Barra de carga
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(progress / 100f)
                            .fillMaxHeight()
                            .background(
                                brush = Brush.horizontalGradient(
                                    colors = listOf(
                                        Color(0xFF7F1D1D),
                                        Color(0xFF991B1B),
                                        Color(0xFFDC2626),
                                        Color(0xFFEF4444)
                                    )
                                ),
                                shape = RoundedCornerShape(2.dp)
                            )
                    )

                    //Brillo en la barra
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val barWidth = size.width * (progress / 100f)
                        if (barWidth > 0) {
                            // Efecto de brillo/reflejo
                            drawRect(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        Color.White.copy(alpha = 0.3f * pulseAlpha),
                                        Color.Transparent,
                                        Color.Transparent
                                    )
                                ),
                                topLeft = Offset(0f, 0f),
                                size = Size(barWidth, size.height * 0.5f)
                            )
                        }
                    }

                    //Numero de progreso
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "$progress%",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Serif,
                            color = Color.White,
                            style = androidx.compose.ui.text.TextStyle(
                                shadow = androidx.compose.ui.graphics.Shadow(
                                    color = Color.Black,
                                    offset = Offset(1f, 1f),
                                    blurRadius = 3f
                                )
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                //Texto inferior
                Text(
                    text = when {
                        progress < 30 -> "Despertando al dragón..."
                        progress < 60 -> "Explorando calabozos..."
                        progress < 90 -> "Invocando magia antigua..."
                        else -> "¡Aventura lista!"
                    },
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = FontFamily.Serif,
                    color = Color(0xFFEF4444).copy(alpha = pulseAlpha),
                    letterSpacing = 1.5.sp
                )
            }
        }
    }
}