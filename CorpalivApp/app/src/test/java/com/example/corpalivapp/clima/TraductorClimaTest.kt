package com.example.corpalivapp.clima

import org.junit.Assert.assertEquals
import org.junit.Test

// Pruebas de la traducción de los datos de Open-Meteo: lógica pura, sin mocks.
class TraductorClimaTest {

    @Test
    fun cadaGrupoDeCodigosTieneSuDescripcion() {
        assertEquals("Despejado", TraductorClima.describir(0))
        assertEquals("Parcialmente nublado", TraductorClima.describir(2))
        assertEquals("Nublado", TraductorClima.describir(3))
        assertEquals("Niebla", TraductorClima.describir(45))
        assertEquals("Llovizna", TraductorClima.describir(53))
        assertEquals("Lluvia", TraductorClima.describir(63))
        assertEquals("Nieve", TraductorClima.describir(73))
        assertEquals("Chubascos", TraductorClima.describir(81))
        assertEquals("Chubascos de nieve", TraductorClima.describir(86))
        assertEquals("Tormenta eléctrica", TraductorClima.describir(95))
    }

    @Test
    fun codigoDesconocidoNoRompeLaApp() {
        assertEquals("Sin información", TraductorClima.describir(1234))
    }

    @Test
    fun temperaturaUsaComaDecimalYUnaCifra() {
        assertEquals("18,5 °C", TraductorClima.formatearTemperatura(18.5))
        assertEquals("-2,0 °C", TraductorClima.formatearTemperatura(-2.0))
    }

    @Test
    fun vientoSeMuestraEnKilometrosPorHora() {
        assertEquals("Viento: 7,3 km/h", TraductorClima.formatearViento(7.3))
    }
}
