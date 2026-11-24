package com.example.corpalivapp.clima

import java.util.Locale

// Convierte los datos numéricos de Open-Meteo en textos para mostrar.
// Códigos WMO: https://open-meteo.com/en/docs (sección "WMO Weather interpretation codes").
object TraductorClima {

    fun describir(codigo: Int): String {
        return when (codigo) {
            0 -> "Despejado"
            1, 2 -> "Parcialmente nublado"
            3 -> "Nublado"
            45, 48 -> "Niebla"
            in 51..57 -> "Llovizna"
            in 61..67 -> "Lluvia"
            in 71..77 -> "Nieve"
            in 80..82 -> "Chubascos"
            85, 86 -> "Chubascos de nieve"
            in 95..99 -> "Tormenta eléctrica"
            else -> "Sin información"
        }
    }

    // 18.5 -> "18,5 °C" (coma decimal, como en Chile)
    fun formatearTemperatura(grados: Double): String {
        return String.format(Locale.forLanguageTag("es-CL"), "%.1f °C", grados)
    }

    // 7.3 -> "Viento: 7,3 km/h"
    fun formatearViento(kilometrosPorHora: Double): String {
        return String.format(Locale.forLanguageTag("es-CL"), "Viento: %.1f km/h", kilometrosPorHora)
    }
}
