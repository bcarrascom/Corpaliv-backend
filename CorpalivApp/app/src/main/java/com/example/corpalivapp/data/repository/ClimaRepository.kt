package com.example.corpalivapp.data.repository

import com.example.corpalivapp.data.model.ClimaActual
import com.example.corpalivapp.data.remote.ClimaApiService

// Pide a Open-Meteo el clima actual en la sede de CORPALIV.
class ClimaRepository(
    private val api: ClimaApiService
) {
    companion object {
        // Coordenadas de ejemplo de la sede, en Santiago de Chile.
        const val LATITUD_SEDE = -33.45
        const val LONGITUD_SEDE = -70.66
        private const val VARIABLES = "temperature_2m,weather_code,wind_speed_10m"
        private const val ZONA_HORARIA = "America/Santiago"
    }

    suspend fun obtenerClimaSede(): ClimaActual {
        val respuesta = api.obtenerClimaActual(
            latitud = LATITUD_SEDE,
            longitud = LONGITUD_SEDE,
            variables = VARIABLES,
            zonaHoraria = ZONA_HORARIA
        )
        return respuesta.actual
    }
}
