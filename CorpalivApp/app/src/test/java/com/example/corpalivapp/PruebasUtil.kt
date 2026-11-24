package com.example.corpalivapp

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import retrofit2.HttpException
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

// Regla de JUnit para probar ViewModels.
// viewModelScope usa Dispatchers.Main, que en el PC no existe (solo en el teléfono).
// Esta regla lo reemplaza por un dispatcher de pruebas antes de cada test y lo restaura después.
// "Unconfined" ejecuta las corrutinas al instante, así el resultado ya está listo al revisar.
@OptIn(ExperimentalCoroutinesApi::class)
class ReglaDispatcherPrincipal : TestWatcher() {

    private val dispatcherDePrueba = UnconfinedTestDispatcher()

    override fun starting(description: Description) {
        Dispatchers.setMain(dispatcherDePrueba)
    }

    override fun finished(description: Description) {
        Dispatchers.resetMain()
    }
}

// Crea el mismo error que lanza Retrofit cuando el servidor responde con un código 4xx o 5xx.
fun errorHttp(codigo: Int): HttpException {
    val cuerpo = "{}".toResponseBody("application/json".toMediaType())
    val respuesta = Response.error<Any>(codigo, cuerpo)
    return HttpException(respuesta)
}

// Fecha en formato "dd/MM/yyyy" sumando días a hoy (negativo = días atrás).
fun fechaDesdeHoy(dias: Int): String {
    val calendario = Calendar.getInstance()
    calendario.add(Calendar.DAY_OF_YEAR, dias)
    val formato = SimpleDateFormat("dd/MM/yyyy", Locale.ROOT)
    return formato.format(calendario.time)
}
