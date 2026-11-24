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

// "Mis solicitudes": busca por correo en ms-colaboraciones y permite eliminar.
class SolicitudesViewModel(
    private val repository: ColaboracionRepository
) : ViewModel() {

    private val _estado = MutableStateFlow(SolicitudesEstado())
    val estado: StateFlow<SolicitudesEstado> = _estado.asStateFlow()

    fun cambiarCorreo(valor: String) {
        _estado.value = _estado.value.copy(correo = valor, errorCorreo = null)
    }

    // Botón "Buscar": valida el correo con el mismo validador del formulario.
    fun buscar() {
        val correo = _estado.value.correo.trim()
        val error = ColaboracionValidador.validarCorreo(correo)
        if (error != null) {
            _estado.value = _estado.value.copy(errorCorreo = error)
            return
        }
        cargar(correo)
    }

    // Al volver a la pantalla o con "Reintentar": repite la última búsqueda, si hubo una.
    fun recargar() {
        val correo = _estado.value.correoBuscado
        if (correo.isBlank()) {
            return
        }
        cargar(correo)
    }

    fun eliminar(colaboracion: Colaboracion) {
        viewModelScope.launch {
            try {
                repository.eliminar(colaboracion.id)
                _estado.value = _estado.value.copy(mensaje = "Solicitud eliminada.")
                recargar()
            } catch (e: IOException) {
                _estado.value = _estado.value.copy(
                    mensaje = "No se pudo eliminar: sin conexión con el servidor."
                )
            } catch (e: HttpException) {
                _estado.value = _estado.value.copy(
                    mensaje = "No se pudo eliminar (error ${e.code()})."
                )
            }
        }
    }

    // La pantalla avisa que ya mostró el Snackbar.
    fun mensajeMostrado() {
        _estado.value = _estado.value.copy(mensaje = null)
    }

    private fun cargar(correo: String) {
        _estado.value = _estado.value.copy(
            cargando = true,
            error = null,
            correoBuscado = correo
        )

        viewModelScope.launch {
            try {
                val lista = repository.obtenerPorCorreo(correo)
                _estado.value = _estado.value.copy(cargando = false, solicitudes = lista)
            } catch (e: IOException) {
                _estado.value = _estado.value.copy(
                    cargando = false,
                    error = "No se pudo conectar con el servidor. Revisa tu conexión."
                )
            } catch (e: HttpException) {
                _estado.value = _estado.value.copy(
                    cargando = false,
                    error = "El servidor respondió con un error (${e.code()})."
                )
            }
        }
    }
}
