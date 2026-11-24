package com.example.corpalivapp.data.model

import com.google.gson.annotations.SerializedName

// Respuesta de Open-Meteo. Solo se declaran los campos que usa la app; Gson ignora el resto.
// @SerializedName une el nombre del JSON (en inglés) con el nombre en Kotlin (en español).
data class RespuestaClima(
    @SerializedName("current")
    val actual: ClimaActual
)

data class ClimaActual(
    // Hora de la medición, por ejemplo "2026-11-23T22:00"
    @SerializedName("time")
    val hora: String,

    // Grados Celsius
    @SerializedName("temperature_2m")
    val temperatura: Double,

    // Código WMO del estado del tiempo (0 = despejado, 61 = lluvia, ...)
    @SerializedName("weather_code")
    val codigoClima: Int,

    // Kilómetros por hora
    @SerializedName("wind_speed_10m")
    val viento: Double
)
