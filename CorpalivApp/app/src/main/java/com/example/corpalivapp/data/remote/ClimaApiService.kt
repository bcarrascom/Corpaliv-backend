package com.example.corpalivapp.data.remote

import com.example.corpalivapp.data.model.RespuestaClima
import retrofit2.http.GET
import retrofit2.http.Query

// API externa Open-Meteo (https://open-meteo.com). Gratuita, sin clave y por HTTPS.
// No es nuestra: no podemos cambiar sus endpoints ni su JSON, solo adaptarnos a ellos.
interface ClimaApiService {

    // GET https://api.open-meteo.com/v1/forecast?latitude=..&longitude=..&current=..&timezone=..
    @GET("v1/forecast")
    suspend fun obtenerClimaActual(
        @Query("latitude") latitud: Double,
        @Query("longitude") longitud: Double,
        // Variables que queremos, separadas por coma
        @Query("current") variables: String,
        @Query("timezone") zonaHoraria: String
    ): RespuestaClima
}
