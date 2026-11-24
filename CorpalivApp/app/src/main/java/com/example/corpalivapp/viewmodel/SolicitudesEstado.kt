package com.example.corpalivapp.viewmodel

import com.example.corpalivapp.data.model.Colaboracion

// Todo lo que la pantalla "Mis solicitudes" necesita para dibujarse.
data class SolicitudesEstado(
    // Lo que la persona escribe en el buscador y su error de validación
    val correo: String = "",
    val errorCorreo: String? = null,
    // Último correo buscado ("" = todavía no ha buscado)
    val correoBuscado: String = "",
    val cargando: Boolean = false,
    val solicitudes: List<Colaboracion> = emptyList(),
    // Error al cargar la lista (null = sin error)
    val error: String? = null,
    // Aviso corto para el Snackbar, por ejemplo "Solicitud eliminada" (null = nada que mostrar)
    val mensaje: String? = null
)
