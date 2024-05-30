package com.activiza.activiza.data

data class UsuarioData(
    val token: String,
    var nombre: String,
    val password: String,
    val entrenador: Boolean
)
