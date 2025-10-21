package com.example.proyectodd.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = RojoTexto,
    secondary = RojoBoton,
    tertiary = RojoBorde,
    background = Negro,
    surface = GrisOscuro,
    error = RojoTexto
)

@Composable
fun TemaZazmorra(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}