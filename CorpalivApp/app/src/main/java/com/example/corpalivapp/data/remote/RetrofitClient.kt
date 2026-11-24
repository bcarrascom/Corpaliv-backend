package com.example.corpalivapp.data.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

// Crea los servicios Retrofit de la app. Es un object: hay una sola instancia para toda la app.
object RetrofitClient {

    // Cliente HTTP compartido. Si el servidor no responde en 10 segundos, se corta.
    private val clienteHttp = OkHttpClient.Builder()
        .addInterceptor(crearRegistro())
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .build()

    // Muestra en el Logcat cada petición y respuesta (útil para depurar y en la defensa).
    private fun crearRegistro(): HttpLoggingInterceptor {
        val registro = HttpLoggingInterceptor()
        registro.level = HttpLoggingInterceptor.Level.BODY
        return registro
    }

    // Arma un Retrofit para una URL base. Gson convierte el JSON en objetos Kotlin.
    private fun crearRetrofit(urlBase: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(urlBase)
            .client(clienteHttp)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // "by lazy": se crea la primera vez que se usa, no al abrir la app.
    val productoApi: ProductoApiService by lazy {
        crearRetrofit(ApiConfig.URL_PRODUCTOS).create(ProductoApiService::class.java)
    }
}
