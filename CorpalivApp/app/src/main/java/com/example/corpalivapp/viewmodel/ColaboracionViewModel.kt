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

    // CREAR: valida todo y, si no hay errores, envía la solicitud nueva (POST).
    fun enviarFormulario() {
        val validado = validarTodo()
        if (tieneErrores(validado)) {
            return
        }

        val nuevaColaboracion = crearColaboracion(validado)
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
                marcarErrorEnvio("No se pudo conectar con el servidor. Tu solicitud no se envió.")
            } catch (e: HttpException) {
                marcarErrorEnvio("El servidor rechazó la solicitud (${e.code()}). Revisa los datos.")
            }
        }
    }

    // EDITAR (paso 1): llena el formulario con los datos de una solicitud existente.
    fun cargarParaEditar(colaboracion: Colaboracion) {
        _formulario.value = FormularioColaboracionEstado(
            nombre = colaboracion.nombreContacto,
            organizacion = colaboracion.organizacion,
            correo = colaboracion.correo,
            tipo = colaboracion.tipoColaboracion,
            cantidadPersonas = colaboracion.cantidadPersonas.toString(),
            fecha = colaboracion.fechaContacto,
            aceptaContacto = colaboracion.aceptaContacto,
            idEditando = colaboracion.id
        )
    }

    // EDITAR (paso 2): valida igual que al crear y guarda los cambios (PUT).
    fun guardarCambios() {
        val validado = validarTodo()
        val id = validado.idEditando
        if (tieneErrores(validado) || id == null) {
            return
        }

        val cambios = crearColaboracion(validado)
        _formulario.value = validado.copy(enviando = true)

        viewModelScope.launch {
            try {
                repository.actualizar(id, cambios)
                _formulario.value = _formulario.value.copy(
                    enviando = false,
                    cambiosGuardados = true
                )
            } catch (e: IOException) {
                marcarErrorEnvio("No se pudo conectar con el servidor. Los cambios no se guardaron.")
            } catch (e: HttpException) {
                marcarErrorEnvio("El servidor rechazó los cambios (${e.code()}). Revisa los datos.")
            }
        }
    }

    // La pantalla de edición avisa que ya volvió a "Mis solicitudes": se limpia el formulario.
    fun terminarEdicion() {
        _formulario.value = FormularioColaboracionEstado()
    }

    // La pantalla avisa que ya mostró la confirmación.
    fun ocultarMensajeExito() {
        _formulario.value = _formulario.value.copy(guardadoConExito = false)
    }

    // Valida todos los campos, guarda los errores en el estado y devuelve el estado validado.
    private fun validarTodo(): FormularioColaboracionEstado {
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
        return validado
    }

    // Pasa los textos del formulario (ya validados) al objeto que viaja al servidor.
    private fun crearColaboracion(validado: FormularioColaboracionEstado): Colaboracion {
        return Colaboracion(
            nombreContacto = validado.nombre.trim(),
            organizacion = validado.organizacion.trim(),
            correo = validado.correo.trim(),
            tipoColaboracion = validado.tipo,
            cantidadPersonas = validado.cantidadPersonas.trim().toInt(),
            fechaContacto = validado.fecha.trim(),
            aceptaContacto = validado.aceptaContacto
        )
    }

    private fun marcarErrorEnvio(mensaje: String) {
        _formulario.value = _formulario.value.copy(enviando = false, errorEnvio = mensaje)
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
