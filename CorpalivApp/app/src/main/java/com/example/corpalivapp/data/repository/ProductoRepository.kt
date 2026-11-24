package com.example.corpalivapp.data.repository

import com.example.corpalivapp.data.model.Producto
import com.example.corpalivapp.data.remote.ProductoApiService

// Intermediario entre el ViewModel y ms-productos.
// El ViewModel no sabe si los datos vienen de internet o de otra fuente.
class ProductoRepository(
    private val api: ProductoApiService
) {
    suspend fun obtenerTodos(): List<Producto> {
        return api.listar()
    }
}
