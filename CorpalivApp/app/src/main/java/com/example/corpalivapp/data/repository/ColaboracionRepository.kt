package com.example.corpalivapp.data.repository

import com.example.corpalivapp.data.model.Colaboracion
import com.example.corpalivapp.data.remote.ColaboracionApiService

// Intermediario entre los ViewModels y ms-colaboraciones.
class ColaboracionRepository(
    private val api: ColaboracionApiService
) {
    suspend fun obtenerPorCorreo(correo: String): List<Colaboracion> {
        return api.listar(correo)
    }

    suspend fun crear(colaboracion: Colaboracion): Colaboracion {
        return api.crear(colaboracion)
    }

    suspend fun eliminar(id: Long) {
        api.eliminar(id)
    }
}
