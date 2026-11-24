package com.example.corpalivapp.data.model

// Producto o servicio tal como llega en el JSON de ms-productos.
// Los nombres de los campos deben ser iguales a los del JSON para que Gson los llene.
data class Producto(
    val id: Long,
    val nombre: String,
    val descripcion: String,
    // Pesos chilenos. null = precio a convenir (servicios).
    val precio: Int?,
    // "Producto" o "Servicio"
    val categoria: String,
    val disponible: Boolean
)
