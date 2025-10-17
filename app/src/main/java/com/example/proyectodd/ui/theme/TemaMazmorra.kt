package com.example.proyectodd.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val EsquemaColorOscuro = darkColorScheme(
    primary = Color(0xFFDC2626),
    secondary = Color(0xFF7F1D1D),
    tertiary = Color(0xFFEF4444),
    background = Color(0xFF000000),
    surface = Color(0xFF1F1F1F)
)

@Composable
fun TemaMazmorra(
    contenido: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = EsquemaColorOscuro,
        content = contenido
    )
}

