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
    var shouldNavigate by remember { mutableStateOf(false) }

    // Animación inicial
    val alphaAnim = animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(
            durationMillis = 1000,
            easing = FastOutSlowInEasing
        ),
        label = "alpha"
    )

    // Animación de escala suave
    val scaleAnim = animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0.85f,
        animationSpec = tween(
            durationMillis = 1000,
            easing = FastOutSlowInEasing
        ),
        label = "scale"
    )

    // Secuencia de animaciones
    LaunchedEffect(Unit) {
        startAnimation = true
        delay(2500)
        startAnimation = false
        delay(1000)
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
        // Logo D&D
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