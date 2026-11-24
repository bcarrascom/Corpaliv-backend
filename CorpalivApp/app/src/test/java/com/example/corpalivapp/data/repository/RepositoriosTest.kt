package com.example.corpalivapp.data.repository

import com.example.corpalivapp.data.model.CambioEstado
import com.example.corpalivapp.data.model.ClimaActual
import com.example.corpalivapp.data.model.Colaboracion
import com.example.corpalivapp.data.model.Producto
import com.example.corpalivapp.data.model.RespuestaClima
import com.example.corpalivapp.data.remote.ClimaApiService
import com.example.corpalivapp.data.remote.ColaboracionApiService
import com.example.corpalivapp.data.remote.ProductoApiService
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

// Pruebas de los repositorios.
// Se MOCKEA la interfaz de Retrofit (la API): no se llama a ningún servidor real.
// Se revisa que cada método del repositorio llame al endpoint correcto con los datos correctos.
class RepositoriosTest {

    private val solicitud = Colaboracion(
        id = 7,
        nombreContacto = "Ana Pérez",
        organizacion = "Empresa Demo",
        correo = "ana@ejemplo.cl",
        tipoColaboracion = "Alianza",
        cantidadPersonas = 3,
        fechaContacto = "15/12/2030",
        aceptaContacto = true
    )

    // ---------- ProductoRepository ----------

    @Test
    fun productoRepositoryDevuelveLoQueEntregaLaApi() = runTest {
        val api = mockk<ProductoApiService>()
        val productos = listOf(Producto(1, "Velas", "Artesanales", 6990, "Producto", true))
        coEvery { api.listar() } returns productos

        val resultado = ProductoRepository(api).obtenerTodos()

        assertEquals(productos, resultado)
    }

    // ---------- ColaboracionRepository ----------

    @Test
    fun obtenerTodasPideLaListaSinFiltrarPorCorreo() = runTest {
        val api = mockk<ColaboracionApiService>()
        coEvery { api.listar(null) } returns listOf(solicitud)

        val resultado = ColaboracionRepository(api).obtenerTodas()

        assertEquals(listOf(solicitud), resultado)
        coVerify { api.listar(null) }
    }

    @Test
    fun obtenerPorCorreoEnviaElCorreoComoFiltro() = runTest {
        val api = mockk<ColaboracionApiService>()
        coEvery { api.listar("ana@ejemplo.cl") } returns listOf(solicitud)

        ColaboracionRepository(api).obtenerPorCorreo("ana@ejemplo.cl")

        coVerify { api.listar("ana@ejemplo.cl") }
    }

    @Test
    fun crearActualizarYEliminarLlamanAlEndpointCorrecto() = runTest {
        val api = mockk<ColaboracionApiService>()
        coEvery { api.crear(solicitud) } returns solicitud
        coEvery { api.actualizar(7, solicitud) } returns solicitud
        coEvery { api.eliminar(7) } returns Unit
        val repositorio = ColaboracionRepository(api)

        repositorio.crear(solicitud)
        repositorio.actualizar(7, solicitud)
        repositorio.eliminar(7)

        coVerify { api.crear(solicitud) }
        coVerify { api.actualizar(7, solicitud) }
        coVerify { api.eliminar(7) }
    }

    @Test
    fun cambiarEstadoArmaElCuerpoDelPatch() = runTest {
        val api = mockk<ColaboracionApiService>()
        val cerrada = solicitud.copy(estado = "Cerrada")
        coEvery { api.cambiarEstado(7, CambioEstado("Cerrada")) } returns cerrada

        val resultado = ColaboracionRepository(api).cambiarEstado(7, "Cerrada")

        assertEquals("Cerrada", resultado.estado)
        coVerify { api.cambiarEstado(7, CambioEstado("Cerrada")) }
    }

    // ---------- ClimaRepository ----------

    @Test
    fun climaRepositoryPideLasCoordenadasDeLaSedeYDevuelveElClimaActual() = runTest {
        val api = mockk<ClimaApiService>()
        val actual = ClimaActual("2030-01-01T12:00", 21.4, 0, 9.8)
        coEvery {
            api.obtenerClimaActual(
                ClimaRepository.LATITUD_SEDE,
                ClimaRepository.LONGITUD_SEDE,
                "temperature_2m,weather_code,wind_speed_10m",
                "America/Santiago"
            )
        } returns RespuestaClima(actual)

        val resultado = ClimaRepository(api).obtenerClimaSede()

        assertEquals(actual, resultado)
    }
}
