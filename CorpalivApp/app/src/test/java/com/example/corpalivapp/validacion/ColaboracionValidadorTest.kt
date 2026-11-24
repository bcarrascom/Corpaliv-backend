package com.example.corpalivapp.validacion

import com.example.corpalivapp.fechaDesdeHoy
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

// Pruebas del validador del formulario: lógica pura, sin mocks.
// Cada función devuelve el mensaje de error, o null si el valor es válido.
class ColaboracionValidadorTest {

    // ---------- Nombre ----------

    @Test
    fun nombreValidoNoTieneError() {
        assertNull(ColaboracionValidador.validarNombre("Ana Pérez"))
    }

    @Test
    fun nombreVacioEsObligatorio() {
        assertEquals("El nombre es obligatorio", ColaboracionValidador.validarNombre("   "))
    }

    @Test
    fun nombreMuyCortoDaError() {
        assertEquals(
            "El nombre debe tener al menos 3 letras",
            ColaboracionValidador.validarNombre("Al")
        )
    }

    @Test
    fun nombreConNumerosDaError() {
        assertEquals(
            "El nombre solo puede tener letras",
            ColaboracionValidador.validarNombre("Ana123")
        )
    }

    // ---------- Organización ----------

    @Test
    fun organizacionValidaNoTieneError() {
        assertNull(ColaboracionValidador.validarOrganizacion("Empresa Demo"))
    }

    @Test
    fun organizacionVaciaEsObligatoria() {
        assertEquals(
            "La organización es obligatoria",
            ColaboracionValidador.validarOrganizacion("")
        )
    }

    @Test
    fun organizacionDeUnaLetraDaError() {
        assertEquals(
            "La organización debe tener al menos 2 caracteres",
            ColaboracionValidador.validarOrganizacion("A")
        )
    }

    // ---------- Correo ----------

    @Test
    fun correoValidoNoTieneError() {
        assertNull(ColaboracionValidador.validarCorreo("ana.perez@ejemplo.cl"))
    }

    @Test
    fun correoVacioEsObligatorio() {
        assertEquals("El correo es obligatorio", ColaboracionValidador.validarCorreo(""))
    }

    @Test
    fun correoSinDominioDaError() {
        assertEquals(
            "Ingresa un correo válido, por ejemplo nombre@correo.cl",
            ColaboracionValidador.validarCorreo("ana@")
        )
    }

    // ---------- Tipo ----------

    @Test
    fun tipoDeLaListaNoTieneError() {
        assertNull(ColaboracionValidador.validarTipo("Voluntariado"))
    }

    @Test
    fun tipoVacioPideElegir() {
        assertEquals("Elige un tipo de colaboración", ColaboracionValidador.validarTipo(""))
    }

    @Test
    fun tipoFueraDeLaListaDaError() {
        assertEquals("Tipo de colaboración no válido", ColaboracionValidador.validarTipo("Regalo"))
    }

    // ---------- Cantidad de personas ----------

    @Test
    fun cantidadEnRangoNoTieneError() {
        assertNull(ColaboracionValidador.validarCantidad("25"))
    }

    @Test
    fun cantidadVaciaEsObligatoria() {
        assertEquals(
            "La cantidad de personas es obligatoria",
            ColaboracionValidador.validarCantidad("")
        )
    }

    @Test
    fun cantidadConLetrasDaError() {
        assertEquals("Ingresa un número entero", ColaboracionValidador.validarCantidad("diez"))
    }

    @Test
    fun cantidadFueraDeRangoDaError() {
        assertEquals("Debe ser entre 1 y 500 personas", ColaboracionValidador.validarCantidad("0"))
        assertEquals("Debe ser entre 1 y 500 personas", ColaboracionValidador.validarCantidad("501"))
    }

    // ---------- Fecha ----------

    @Test
    fun fechaDeHoyEsValida() {
        assertNull(ColaboracionValidador.validarFecha(fechaDesdeHoy(0)))
    }

    @Test
    fun fechaFuturaEsValida() {
        assertNull(ColaboracionValidador.validarFecha(fechaDesdeHoy(30)))
    }

    @Test
    fun fechaVaciaEsObligatoria() {
        assertEquals("La fecha es obligatoria", ColaboracionValidador.validarFecha(""))
    }

    @Test
    fun fechaConOtroFormatoDaError() {
        assertEquals("Usa el formato dd/MM/aaaa", ColaboracionValidador.validarFecha("2030-12-01"))
    }

    @Test
    fun fechaQueNoExisteDaError() {
        assertEquals("Esa fecha no existe", ColaboracionValidador.validarFecha("31/02/2030"))
    }

    @Test
    fun fechaPasadaDaError() {
        assertEquals(
            "La fecha no puede ser anterior a hoy",
            ColaboracionValidador.validarFecha(fechaDesdeHoy(-1))
        )
    }

    // ---------- Aceptación ----------

    @Test
    fun aceptarContactoNoTieneError() {
        assertNull(ColaboracionValidador.validarAceptaContacto(true))
    }

    @Test
    fun noAceptarContactoDaError() {
        assertEquals(
            "Debes aceptar ser contactado para enviar",
            ColaboracionValidador.validarAceptaContacto(false)
        )
    }
}
