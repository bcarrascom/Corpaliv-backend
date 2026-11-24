package com.example.corpalivapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.corpalivapp.clima.TraductorClima
import com.example.corpalivapp.data.repository.ClimaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

// Clima actual en la sede (API externa Open-Meteo).
class ClimaViewModel(
    private val repository: ClimaRepository
) : ViewModel() {

    private val _estado = MutableStateFlow(ClimaEstado())
    val estado: StateFlow<ClimaEstado> = _estado.asStateFlow()

    fun cargarClima() {
        _estado.value = ClimaEstado(cargando = true)

        viewModelScope.launch {
            try {
                val clima = repository.obtenerClimaSede()
                _estado.value = ClimaEstado(
                    cargando = false,
                    temperatura = TraductorClima.formatearTemperatura(clima.temperatura),
                    descripcion = TraductorClima.describir(clima.codigoClima),
                    viento = TraductorClima.formatearViento(clima.viento)
                )
            } catch (e: IOException) {
                _estado.value = ClimaEstado(
                    cargando = false,
                    error = "No se pudo consultar el clima. Revisa tu conexión a internet."
                )
            } catch (e: HttpException) {
                _estado.value = ClimaEstado(
                    cargando = false,
                    error = "El servicio del clima respondió con un error (${e.code()})."
                )
            }
        }
    }
}
