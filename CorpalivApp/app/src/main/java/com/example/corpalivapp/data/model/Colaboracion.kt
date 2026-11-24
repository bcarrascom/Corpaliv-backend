package com.example.corpalivapp.data.model

// Solicitud de colaboración tal como viaja en el JSON de ms-colaboraciones.
// Al crear, id y estado se ignoran: los asigna el servidor.
data class Colaboracion(
    val id: Long = 0,
    val nombreContacto: String,
    val organizacion: String,
    val correo: String,
    val tipoColaboracion: String,
    val cantidadPersonas: Int,
    // Formato "dd/MM/yyyy", igual que en el servidor.
    val fechaContacto: String,
    val aceptaContacto: Boolean,
    // "Pendiente", "Contactado" o "Cerrada".
    val estado: String = "Pendiente"
)
