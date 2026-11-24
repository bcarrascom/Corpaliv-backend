package com.example.corpalivapp.viewmodel

// Lo que la tarjeta del clima necesita para dibujarse. Los textos ya vienen listos para mostrar.
data class ClimaEstado(
    val cargando: Boolean = true,
    val temperatura: String = "",
    val descripcion: String = "",
    val viento: String = "",
    // Mensaje si no se pudo consultar la API (null = sin error)
    val error: String? = null
)
