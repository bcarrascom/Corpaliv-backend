package com.example.corpalivapp.data.model

// Cuerpo del PATCH de estado: { "estado": "Contactado" }.
// Es igual al record CambioEstado de ms-colaboraciones.
data class CambioEstado(
    val estado: String
)
