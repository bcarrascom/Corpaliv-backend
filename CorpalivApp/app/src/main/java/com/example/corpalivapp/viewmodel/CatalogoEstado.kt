package com.example.corpalivapp.viewmodel

import com.example.corpalivapp.data.model.Producto

// Todo lo que la pantalla del catálogo necesita para dibujarse.
data class CatalogoEstado(
    // true mientras se espera la respuesta de ms-productos
    val cargando: Boolean = false,
    val productos: List<Producto> = emptyList(),
    // Mensaje para mostrar si falló la carga (null = sin error)
    val error: String? = null
)
