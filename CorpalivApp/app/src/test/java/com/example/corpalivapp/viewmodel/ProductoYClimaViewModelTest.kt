package com.example.corpalivapp.viewmodel

import com.example.corpalivapp.ReglaDispatcherPrincipal
import com.example.corpalivapp.data.model.ClimaActual
import com.example.corpalivapp.data.model.Producto
import com.example.corpalivapp.data.repository.ClimaRepository
import com.example.corpalivapp.data.repository.ProductoRepository
import com.example.corpalivapp.errorHttp
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import java.io.IOException

// Pruebas de los ViewModels de catálogo y clima.
// Se MOCKEA el repositorio para simular las tres respuestas posibles del servidor:
// éxito, sin conexión (IOException) y error HTTP (HttpException).
class ProductoYClimaViewModelTest {

    @get:Rule
    val reglaDispatcher = ReglaDispatcherPrincipal()

    private val repositorioProductos = mockk<ProductoRepository>()
    private val repositorioClima = mockk<ClimaRepository>()

    // ---------- Catálogo (ProductoViewModel) ----------

    @Test
    fun catalogoPartePorMostrarCargando() {
        val viewModel = ProductoViewModel(repositorioProductos)

        assertTrue(viewModel.estado.value.cargando)
    }

    @Test
    fun catalogoConExitoMuestraLosProductos() {
        val productos = listOf(
            Producto(1, "Velas", "Artesanales", 6990, "Producto", true),
            Producto(2, "Taller", "Para empresas", null, "Servicio", true)
        )
        coEvery { repositorioProductos.obtenerTodos() } returns productos
        val viewModel = ProductoViewModel(repositorioProductos)

        viewModel.cargarProductos()

        val estado = viewModel.estado.value
        assertFalse(estado.cargando)
        assertEquals(productos, estado.productos)
        assertNull(estado.error)
    }

    @Test
    fun catalogoSinConexionMuestraMensajeDeConexion() {
        coEvery { repositorioProductos.obtenerTodos() } throws IOException()
        val viewModel = ProductoViewModel(repositorioProductos)

        viewModel.cargarProductos()

        val estado = viewModel.estado.value
        assertFalse(estado.cargando)
        assertEquals("No se pudo conectar con el servidor. Revisa tu conexión.", estado.error)
        assertTrue(estado.productos.isEmpty())
    }

    @Test
    fun catalogoConErrorHttpMuestraElCodigo() {
        coEvery { repositorioProductos.obtenerTodos() } throws errorHttp(500)
        val viewModel = ProductoViewModel(repositorioProductos)

        viewModel.cargarProductos()

        assertEquals("El servidor respondió con un error (500).", viewModel.estado.value.error)
    }

    // ---------- Clima (ClimaViewModel) ----------

    @Test
    fun climaConExitoEntregaLosTextosListos() {
        coEvery { repositorioClima.obtenerClimaSede() } returns
                ClimaActual("2030-01-01T12:00", 18.5, 0, 7.3)
        val viewModel = ClimaViewModel(repositorioClima)

        viewModel.cargarClima()

        val estado = viewModel.estado.value
        assertFalse(estado.cargando)
        assertEquals("18,5 °C", estado.temperatura)
        assertEquals("Despejado", estado.descripcion)
        assertEquals("Viento: 7,3 km/h", estado.viento)
        assertNull(estado.error)
    }

    @Test
    fun climaSinInternetMuestraError() {
        coEvery { repositorioClima.obtenerClimaSede() } throws IOException()
        val viewModel = ClimaViewModel(repositorioClima)

        viewModel.cargarClima()

        assertEquals(
            "No se pudo consultar el clima. Revisa tu conexión a internet.",
            viewModel.estado.value.error
        )
    }

    @Test
    fun climaConErrorHttpMuestraElCodigo() {
        coEvery { repositorioClima.obtenerClimaSede() } throws errorHttp(503)
        val viewModel = ClimaViewModel(repositorioClima)

        viewModel.cargarClima()

        assertEquals(
            "El servicio del clima respondió con un error (503).",
            viewModel.estado.value.error
        )
    }
}
