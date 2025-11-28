package com.example.proyectodd.model

data class Hechizo (
    val name: String,
    val desc: String,
    val level_int: Int
)
data class RespuestaConjuros(
    val results: List<Hechizo>
)