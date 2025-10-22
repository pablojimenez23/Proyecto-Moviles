package com.example.proyectodd.pantallas

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.example.proyectodd.R
import kotlinx.coroutines.delay

@Composable
fun PantallaSplash(onSplashFinished: () -> Unit) {
    var startAnimation by remember { mutableStateOf(false) }

    //animacion inicial
    val alphaAnim = animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(
            durationMillis = 1000,
            easing = FastOutSlowInEasing
        ),
        label = "alpha"
    )

    //animacion final
    var fadeOut by remember { mutableStateOf(false) }
    val fadeOutAnim = animateFloatAsState(
        targetValue = if (fadeOut) 0f else 1f,
        animationSpec = tween(
            durationMillis = 800,
            easing = FastOutSlowInEasing
        ),
        label = "fadeOut"
    )

    //animacion de escala suave
    val scaleAnim = animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0.85f,
        animationSpec = tween(
            durationMillis = 1000,
            easing = FastOutSlowInEasing
        ),
        label = "scale"
    )

    //secuencia de animaciones
    LaunchedEffect(Unit) {
        startAnimation = true
        delay(2500)
        fadeOut = true
        delay(800)
        onSplashFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .alpha(fadeOutAnim.value),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.dnd_logo),
            contentDescription = "Dungeons & Dragons Logo",
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .scale(scaleAnim.value)
                .alpha(alphaAnim.value)
        )
    }
}