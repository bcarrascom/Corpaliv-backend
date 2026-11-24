package com.example.corpalivapp.data.repository

import com.example.corpalivapp.data.model.CambioEstado
import com.example.corpalivapp.data.model.Colaboracion
import com.example.corpalivapp.data.remote.ColaboracionApiService

// Intermediario entre los ViewModels y ms-colaboraciones.
class ColaboracionRepository(
    private val api: ColaboracionApiService
) {
    // Panel de vinculación: todas las solicitudes.
    suspend fun obtenerTodas(): List<Colaboracion> {
        return api.listar(null)
    }

    // "Mis solicitudes": solo las de un correo.
    suspend fun obtenerPorCorreo(correo: String): List<Colaboracion> {
        return api.listar(correo)
    }

    suspend fun crear(colaboracion: Colaboracion): Colaboracion {
        return api.crear(colaboracion)
    }

    suspend fun actualizar(id: Long, colaboracion: Colaboracion): Colaboracion {
        return api.actualizar(id, colaboracion)
    }

    suspend fun cambiarEstado(id: Long, nuevoEstado: String): Colaboracion {
        return api.cambiarEstado(id, CambioEstado(nuevoEstado))
    }

    suspend fun eliminar(id: Long) {
        api.eliminar(id)
    }
}
