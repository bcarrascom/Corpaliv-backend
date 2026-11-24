package com.example.corpalivapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.corpalivapp.data.model.Colaboracion
import com.example.corpalivapp.data.repository.ColaboracionRepository
import com.example.corpalivapp.validacion.ColaboracionValidador
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

// Maneja el formulario de colaboración y lo envía a ms-colaboraciones.
class ColaboracionViewModel(
    private val repository: ColaboracionRepository
) : ViewModel() {

    // Estado del formulario: solo el ViewModel lo modifica, la pantalla solo lo lee.
    private val _formulario = MutableStateFlow(FormularioColaboracionEstado())
    val formulario: StateFlow<FormularioColaboracionEstado> = _formulario.asStateFlow()

    // Cada cambio guarda el valor nuevo y lo valida en el momento.
    fun cambiarNombre(valor: String) {
        _formulario.value = _formulario.value.copy(
            nombre = valor,
            errorNombre = ColaboracionValidador.validarNombre(valor)
        )
    }

    fun cambiarOrganizacion(valor: String) {
        _formulario.value = _formulario.value.copy(
            organizacion = valor,
            errorOrganizacion = ColaboracionValidador.validarOrganizacion(valor)
        )
    }

    fun cambiarCorreo(valor: String) {
        _formulario.value = _formulario.value.copy(
            correo = valor,
            errorCorreo = ColaboracionValidador.validarCorreo(valor)
        )
    }

    fun cambiarTipo(valor: String) {
        _formulario.value = _formulario.value.copy(
            tipo = valor,
            errorTipo = ColaboracionValidador.validarTipo(valor)
        )
    }

    fun cambiarCantidad(valor: String) {
        _formulario.value = _formulario.value.copy(
            cantidadPersonas = valor,
            errorCantidad = ColaboracionValidador.validarCantidad(valor)
        )
    }

    fun cambiarFecha(valor: String) {
        _formulario.value = _formulario.value.copy(
            fecha = valor,
            errorFecha = ColaboracionValidador.validarFecha(valor)
        )
    }

    // El calendario entrega la fecha en milisegundos (UTC); se pasa a "dd/MM/aaaa".
    fun elegirFechaDelCalendario(milisegundos: Long) {
        val formateador = SimpleDateFormat("dd/MM/yyyy", Locale.ROOT)
        formateador.timeZone = TimeZone.getTimeZone("UTC")
        val texto = formateador.format(Date(milisegundos))
        cambiarFecha(texto)
    }

    fun cambiarAceptaContacto(valor: Boolean) {
        _formulario.value = _formulario.value.copy(
            aceptaContacto = valor,
            errorAceptaContacto = ColaboracionValidador.validarAceptaContacto(valor)
        )
    }

    // Valida todos los campos; si no hay errores, envía la solicitud al servidor.
    fun enviarFormulario() {
        val actual = _formulario.value
        val validado = actual.copy(
            errorNombre = ColaboracionValidador.validarNombre(actual.nombre),
            errorOrganizacion = ColaboracionValidador.validarOrganizacion(actual.organizacion),
            errorCorreo = ColaboracionValidador.validarCorreo(actual.correo),
            errorTipo = ColaboracionValidador.validarTipo(actual.tipo),
            errorCantidad = ColaboracionValidador.validarCantidad(actual.cantidadPersonas),
            errorFecha = ColaboracionValidador.validarFecha(actual.fecha),
            errorAceptaContacto = ColaboracionValidador.validarAceptaContacto(actual.aceptaContacto),
            errorEnvio = null
        )
        _formulario.value = validado

        if (tieneErrores(validado)) {
            return
        }

        val nuevaColaboracion = Colaboracion(
            nombreContacto = validado.nombre.trim(),
            organizacion = validado.organizacion.trim(),
            correo = validado.correo.trim(),
            tipoColaboracion = validado.tipo,
            cantidadPersonas = validado.cantidadPersonas.trim().toInt(),
            fechaContacto = validado.fecha.trim(),
            aceptaContacto = validado.aceptaContacto
        )

        _formulario.value = validado.copy(enviando = true)

        viewModelScope.launch {
            try {
                val guardada = repository.crear(nuevaColaboracion)
                // Formulario limpio + datos para la notificación.
                _formulario.value = FormularioColaboracionEstado(
                    guardadoConExito = true,
                    tipoGuardado = guardada.tipoColaboracion,
                    fechaGuardada = guardada.fechaContacto
                )
            } catch (e: IOException) {
                _formulario.value = _formulario.value.copy(
                    enviando = false,
                    errorEnvio = "No se pudo conectar con el servidor. Tu solicitud no se envió."
                )
            } catch (e: HttpException) {
                _formulario.value = _formulario.value.copy(
                    enviando = false,
                    errorEnvio = "El servidor rechazó la solicitud (${e.code()}). Revisa los datos."
                )
            }
        }
    }

    // La pantalla avisa que ya mostró la confirmación.
    fun ocultarMensajeExito() {
        _formulario.value = _formulario.value.copy(guardadoConExito = false)
    }

    private fun tieneErrores(estado: FormularioColaboracionEstado): Boolean {
        return estado.errorNombre != null ||
                estado.errorOrganizacion != null ||
                estado.errorCorreo != null ||
                estado.errorTipo != null ||
                estado.errorCantidad != null ||
                estado.errorFecha != null ||
                estado.errorAceptaContacto != null
    }
}
