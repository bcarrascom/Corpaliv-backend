package com.example.corpalivapp.viewmodel

import com.example.corpalivapp.ReglaDispatcherPrincipal
import com.example.corpalivapp.data.model.Colaboracion
import com.example.corpalivapp.data.repository.ColaboracionRepository
import com.example.corpalivapp.errorHttp
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Rule
import org.junit.Test
import java.io.IOException

// Pruebas de "Mis solicitudes" (buscar por correo, eliminar) y del panel (todas, cambiar estado).
class SolicitudesViewModelTest {

    @get:Rule
    val reglaDispatcher = ReglaDispatcherPrincipal()

    private val repositorio = mockk<ColaboracionRepository>()

    private val solicitudAna = Colaboracion(
        id = 1,
        nombreContacto = "Ana Pérez",
        organizacion = "Empresa Demo",
        correo = "ana@ejemplo.cl",
        tipoColaboracion = "Alianza",
        cantidadPersonas = 3,
        fechaContacto = "15/12/2030",
        aceptaContacto = true
    )
    private val solicitudLuis = solicitudAna.copy(id = 2, nombreContacto = "Luis Soto", correo = "luis@ejemplo.cl")

    // ---------- Buscar por correo ----------

    @Test
    fun correoInvalidoNoBuscaEnElServidor() {
        val viewModel = SolicitudesViewModel(repositorio)
        viewModel.cambiarCorreo("ana@")

        viewModel.buscar()

        assertEquals(
            "Ingresa un correo válido, por ejemplo nombre@correo.cl",
            viewModel.estado.value.errorCorreo
        )
        coVerify(exactly = 0) { repositorio.obtenerPorCorreo(any()) }
    }

    @Test
    fun buscarConCorreoValidoMuestraSusSolicitudes() {
        coEvery { repositorio.obtenerPorCorreo("ana@ejemplo.cl") } returns listOf(solicitudAna)
        val viewModel = SolicitudesViewModel(repositorio)
        viewModel.cambiarCorreo("  ana@ejemplo.cl  ")

        viewModel.buscar()

        val estado = viewModel.estado.value
        assertFalse(estado.cargando)
        assertEquals("ana@ejemplo.cl", estado.correoBuscado)
        assertEquals(listOf(solicitudAna), estado.solicitudes)
    }

    @Test
    fun buscarSinConexionMuestraError() {
        coEvery { repositorio.obtenerPorCorreo(any()) } throws IOException()
        val viewModel = SolicitudesViewModel(repositorio)
        viewModel.cambiarCorreo("ana@ejemplo.cl")

        viewModel.buscar()

        assertEquals(
            "No se pudo conectar con el servidor. Revisa tu conexión.",
            viewModel.estado.value.error
        )
    }

    @Test
    fun buscarConErrorHttpMuestraElCodigo() {
        coEvery { repositorio.obtenerPorCorreo(any()) } throws errorHttp(500)
        val viewModel = SolicitudesViewModel(repositorio)
        viewModel.cambiarCorreo("ana@ejemplo.cl")

        viewModel.buscar()

        assertEquals("El servidor respondió con un error (500).", viewModel.estado.value.error)
    }

    @Test
    fun recargarSinBusquedaPreviaNoLlamaAlServidor() {
        val viewModel = SolicitudesViewModel(repositorio)

        viewModel.recargar()

        coVerify(exactly = 0) { repositorio.obtenerPorCorreo(any()) }
    }

    // ---------- Eliminar ----------

    @Test
    fun eliminarBorraEnElServidorYRecargaLaLista() {
        coEvery { repositorio.obtenerPorCorreo("ana@ejemplo.cl") } returnsMany
                listOf(listOf(solicitudAna), emptyList())
        coEvery { repositorio.eliminar(1) } returns Unit
        val viewModel = SolicitudesViewModel(repositorio)
        viewModel.cambiarCorreo("ana@ejemplo.cl")
        viewModel.buscar()

        viewModel.eliminar(solicitudAna)

        val estado = viewModel.estado.value
        assertEquals("Solicitud eliminada.", estado.mensaje)
        assertEquals(emptyList<Colaboracion>(), estado.solicitudes)
        coVerify { repositorio.eliminar(1) }
    }

    @Test
    fun eliminarSinConexionAvisa() {
        coEvery { repositorio.eliminar(1) } throws IOException()
        val viewModel = SolicitudesViewModel(repositorio)

        viewModel.eliminar(solicitudAna)

        assertEquals(
            "No se pudo eliminar: sin conexión con el servidor.",
            viewModel.estado.value.mensaje
        )
    }

    @Test
    fun eliminarConErrorHttpAvisaElCodigo() {
        coEvery { repositorio.eliminar(1) } throws errorHttp(404)
        val viewModel = SolicitudesViewModel(repositorio)

        viewModel.eliminar(solicitudAna)

        assertEquals("No se pudo eliminar (error 404).", viewModel.estado.value.mensaje)
    }

    @Test
    fun mensajeMostradoBorraElAviso() {
        coEvery { repositorio.eliminar(1) } throws IOException()
        val viewModel = SolicitudesViewModel(repositorio)
        viewModel.eliminar(solicitudAna)

        viewModel.mensajeMostrado()

        assertNull(viewModel.estado.value.mensaje)
    }

    // ---------- Panel de vinculación ----------

    @Test
    fun cargarTodasTraeLaListaCompleta() {
        coEvery { repositorio.obtenerTodas() } returns listOf(solicitudAna, solicitudLuis)
        val viewModel = SolicitudesViewModel(repositorio)

        viewModel.cargarTodas()

        val estado = viewModel.estado.value
        assertEquals(2, estado.solicitudes.size)
        assertEquals("", estado.correoBuscado)
    }

    @Test
    fun cambiarEstadoReemplazaSoloEsaSolicitud() {
        coEvery { repositorio.obtenerTodas() } returns listOf(solicitudAna, solicitudLuis)
        coEvery { repositorio.cambiarEstado(2, "Cerrada") } returns solicitudLuis.copy(estado = "Cerrada")
        val viewModel = SolicitudesViewModel(repositorio)
        viewModel.cargarTodas()

        viewModel.cambiarEstado(solicitudLuis, "Cerrada")

        val estado = viewModel.estado.value
        assertEquals("Pendiente", estado.solicitudes[0].estado)
        assertEquals("Cerrada", estado.solicitudes[1].estado)
        assertEquals("Solicitud de Luis Soto: Cerrada.", estado.mensaje)
    }

    @Test
    fun cambiarEstadoSinConexionAvisa() {
        coEvery { repositorio.cambiarEstado(any(), any()) } throws IOException()
        val viewModel = SolicitudesViewModel(repositorio)

        viewModel.cambiarEstado(solicitudAna, "Contactado")

        assertEquals(
            "No se pudo cambiar el estado: sin conexión con el servidor.",
            viewModel.estado.value.mensaje
        )
    }

    @Test
    fun cambiarEstadoConErrorHttpAvisaElCodigo() {
        coEvery { repositorio.cambiarEstado(any(), any()) } throws errorHttp(400)
        val viewModel = SolicitudesViewModel(repositorio)

        viewModel.cambiarEstado(solicitudAna, "Contactado")

        assertEquals("No se pudo cambiar el estado (error 400).", viewModel.estado.value.mensaje)
    }
}
