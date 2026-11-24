package com.example.corpalivapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.corpalivapp.data.repository.ProductoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class ProductoViewModel(
    private val repository: ProductoRepository
) : ViewModel() {

    // Solo el ViewModel cambia el estado; la pantalla solo lo lee.
    // Parte "cargando" para que no se vea la lista vacía antes de la primera respuesta.
    private val _estado = MutableStateFlow(CatalogoEstado(cargando = true))
    val estado: StateFlow<CatalogoEstado> = _estado.asStateFlow()

    // La llama la pantalla cada vez que se abre, y el botón "Reintentar".
    fun cargarProductos() {
        _estado.value = CatalogoEstado(cargando = true)

        viewModelScope.launch {
            try {
                val productos = repository.obtenerTodos()
                _estado.value = CatalogoEstado(productos = productos)
            } catch (e: IOException) {
                // Sin internet, servidor apagado o tiempo de espera agotado.
                _estado.value = CatalogoEstado(
                    error = "No se pudo conectar con el servidor. Revisa tu conexión."
                )
            } catch (e: HttpException) {
                // El servidor respondió, pero con un código de error (404, 500...).
                _estado.value = CatalogoEstado(
                    error = "El servidor respondió con un error (${e.code()})."
                )
            }
        }
    }
}
