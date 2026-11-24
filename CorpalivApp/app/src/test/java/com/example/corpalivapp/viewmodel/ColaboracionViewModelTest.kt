package com.example.corpalivapp.viewmodel

import com.example.corpalivapp.ReglaDispatcherPrincipal
import com.example.corpalivapp.data.model.Colaboracion
import com.example.corpalivapp.data.repository.ColaboracionRepository
import com.example.corpalivapp.errorHttp
import com.example.corpalivapp.fechaDesdeHoy
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import java.io.IOException

// Pruebas del formulario de colaboración: validación por campo, crear (POST) y editar (PUT).
// El repositorio está MOCKEADO: se controla qué "responde el servidor" y se verifica qué se le envió.
class ColaboracionViewModelTest {

    @get:Rule
    val reglaDispatcher = ReglaDispatcherPrincipal()

    private val repositorio = mockk<ColaboracionRepository>()
    private val fechaFutura = fechaDesdeHoy(30)

    // Lo que el servidor devuelve al guardar.
    private val guardada = Colaboracion(
        id = 10,
        nombreContacto = "Ana Pérez",
        organizacion = "Empresa Demo",
        correo = "ana@ejemplo.cl",
        tipoColaboracion = "Alianza",
        cantidadPersonas = 3,
        fechaContacto = fechaFutura,
        aceptaContacto = true
    )

    // Completa todos los campos con datos válidos, como lo haría la persona en la pantalla.
    private fun llenarFormularioValido(viewModel: ColaboracionViewModel) {
        viewModel.cambiarNombre("Ana Pérez")
        viewModel.cambiarOrganizacion("Empresa Demo")
        viewModel.cambiarCorreo("ana@ejemplo.cl")
        viewModel.cambiarTipo("Alianza")
        viewModel.cambiarCantidad("3")
        viewModel.cambiarFecha(fechaFutura)
        viewModel.cambiarAceptaContacto(true)
    }

    // ---------- Validación mientras se escribe ----------

    @Test
    fun cadaCampoSeValidaAlCambiar() {
        val viewModel = ColaboracionViewModel(repositorio)

        viewModel.cambiarNombre("A1")
        viewModel.cambiarOrganizacion("")
        viewModel.cambiarCorreo("ana@")
        viewModel.cambiarTipo("Regalo")
        viewModel.cambiarCantidad("0")
        viewModel.cambiarFecha("hoy")
        viewModel.cambiarAceptaContacto(false)

        val formulario = viewModel.formulario.value
        assertNotNull(formulario.errorNombre)
        assertNotNull(formulario.errorOrganizacion)
        assertNotNull(formulario.errorCorreo)
        assertNotNull(formulario.errorTipo)
        assertNotNull(formulario.errorCantidad)
        assertNotNull(formulario.errorFecha)
        assertNotNull(formulario.errorAceptaContacto)
    }

    @Test
    fun laFechaDelCalendarioSeConvierteADiaMesAnio() {
        val viewModel = ColaboracionViewModel(repositorio)

        // 1 de enero de 2030 a medianoche UTC, en milisegundos.
        viewModel.elegirFechaDelCalendario(1893456000000L)

        assertEquals("01/01/2030", viewModel.formulario.value.fecha)
    }

    // ---------- Crear (POST) ----------

    @Test
    fun formularioConErroresNoSeEnviaAlServidor() {
        val viewModel = ColaboracionViewModel(repositorio)

        viewModel.enviarFormulario()

        assertNotNull(viewModel.formulario.value.errorNombre)
        coVerify(exactly = 0) { repositorio.crear(any()) }
    }

    @Test
    fun formularioValidoSeEnviaYQuedaLimpio() {
        coEvery { repositorio.crear(any()) } returns guardada
        val viewModel = ColaboracionViewModel(repositorio)
        llenarFormularioValido(viewModel)

        viewModel.enviarFormulario()

        val formulario = viewModel.formulario.value
        assertTrue(formulario.guardadoConExito)
        assertEquals("Alianza", formulario.tipoGuardado)
        assertEquals(fechaFutura, formulario.fechaGuardada)
        assertEquals("", formulario.nombre)
        coVerify { repositorio.crear(match { enviada -> enviada.cantidadPersonas == 3 }) }
    }

    @Test
    fun sinConexionSeMuestraErrorYSeConservanLosDatos() {
        coEvery { repositorio.crear(any()) } throws IOException()
        val viewModel = ColaboracionViewModel(repositorio)
        llenarFormularioValido(viewModel)

        viewModel.enviarFormulario()

        val formulario = viewModel.formulario.value
        assertFalse(formulario.enviando)
        assertFalse(formulario.guardadoConExito)
        assertEquals("No se pudo conectar con el servidor. Tu solicitud no se envió.", formulario.errorEnvio)
        assertEquals("Ana Pérez", formulario.nombre)
    }

    @Test
    fun siElServidorRechazaSeMuestraElCodigo() {
        coEvery { repositorio.crear(any()) } throws errorHttp(400)
        val viewModel = ColaboracionViewModel(repositorio)
        llenarFormularioValido(viewModel)

        viewModel.enviarFormulario()

        assertEquals(
            "El servidor rechazó la solicitud (400). Revisa los datos.",
            viewModel.formulario.value.errorEnvio
        )
    }

    @Test
    fun ocultarMensajeExitoApagaLaConfirmacion() {
        coEvery { repositorio.crear(any()) } returns guardada
        val viewModel = ColaboracionViewModel(repositorio)
        llenarFormularioValido(viewModel)
        viewModel.enviarFormulario()

        viewModel.ocultarMensajeExito()

        assertFalse(viewModel.formulario.value.guardadoConExito)
    }

    // ---------- Editar (PUT) ----------

    @Test
    fun cargarParaEditarLlenaElFormulario() {
        val viewModel = ColaboracionViewModel(repositorio)

        viewModel.cargarParaEditar(guardada)

        val formulario = viewModel.formulario.value
        assertEquals(10L, formulario.idEditando)
        assertEquals("Ana Pérez", formulario.nombre)
        assertEquals("3", formulario.cantidadPersonas)
        assertEquals(fechaFutura, formulario.fecha)
    }

    @Test
    fun guardarCambiosEnviaElPutConElIdCorrecto() {
        coEvery { repositorio.actualizar(10, any()) } returns guardada
        val viewModel = ColaboracionViewModel(repositorio)
        viewModel.cargarParaEditar(guardada)
        viewModel.cambiarCantidad("8")

        viewModel.guardarCambios()

        assertTrue(viewModel.formulario.value.cambiosGuardados)
        coVerify { repositorio.actualizar(10, match { cambios -> cambios.cantidadPersonas == 8 }) }
    }

    @Test
    fun guardarCambiosSinSolicitudElegidaNoHaceNada() {
        val viewModel = ColaboracionViewModel(repositorio)
        llenarFormularioValido(viewModel)

        viewModel.guardarCambios()

        coVerify(exactly = 0) { repositorio.actualizar(any(), any()) }
    }

    @Test
    fun guardarCambiosSinConexionMuestraError() {
        coEvery { repositorio.actualizar(10, any()) } throws IOException()
        val viewModel = ColaboracionViewModel(repositorio)
        viewModel.cargarParaEditar(guardada)

        viewModel.guardarCambios()

        val formulario = viewModel.formulario.value
        assertFalse(formulario.cambiosGuardados)
        assertEquals("No se pudo conectar con el servidor. Los cambios no se guardaron.", formulario.errorEnvio)
    }

    @Test
    fun guardarCambiosRechazadoMuestraElCodigo() {
        coEvery { repositorio.actualizar(10, any()) } throws errorHttp(404)
        val viewModel = ColaboracionViewModel(repositorio)
        viewModel.cargarParaEditar(guardada)

        viewModel.guardarCambios()

        assertEquals(
            "El servidor rechazó los cambios (404). Revisa los datos.",
            viewModel.formulario.value.errorEnvio
        )
    }

    @Test
    fun terminarEdicionDejaElFormularioVacio() {
        val viewModel = ColaboracionViewModel(repositorio)
        viewModel.cargarParaEditar(guardada)

        viewModel.terminarEdicion()

        val formulario = viewModel.formulario.value
        assertNull(formulario.idEditando)
        assertEquals("", formulario.nombre)
    }
}
