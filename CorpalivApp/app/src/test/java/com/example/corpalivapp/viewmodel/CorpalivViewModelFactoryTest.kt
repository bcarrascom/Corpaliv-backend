package com.example.corpalivapp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.corpalivapp.ReglaDispatcherPrincipal
import com.example.corpalivapp.data.local.CorpalivDatabase
import io.mockk.mockk
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

// Pruebas de la fábrica: entrega el ViewModel correcto para cada pantalla.
// La base de datos se MOCKEA ("relaxed" = responde valores vacíos a todo) porque no se usa de verdad.
class CorpalivViewModelFactoryTest {

    @get:Rule
    val reglaDispatcher = ReglaDispatcherPrincipal()

    private val fabrica = CorpalivViewModelFactory(mockk<CorpalivDatabase>(relaxed = true))

    @Test
    fun creaCadaViewModelDeLaApp() {
        assertTrue(fabrica.create(ProgramaViewModel::class.java) is ProgramaViewModel)
        assertTrue(fabrica.create(ProductoViewModel::class.java) is ProductoViewModel)
        assertTrue(fabrica.create(ColaboracionViewModel::class.java) is ColaboracionViewModel)
        assertTrue(fabrica.create(ClimaViewModel::class.java) is ClimaViewModel)
        assertTrue(fabrica.create(SolicitudesViewModel::class.java) is SolicitudesViewModel)
    }

    // Una clase cualquiera que la fábrica no conoce.
    private class ViewModelDesconocido : ViewModel()

    @Test(expected = IllegalArgumentException::class)
    fun viewModelDesconocidoLanzaError() {
        fabrica.create(ViewModelDesconocido::class.java)
    }
}
