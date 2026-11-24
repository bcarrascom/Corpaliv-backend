package com.example.corpalivapp.data.remote

// Direcciones de los microservicios.
// 10.0.2.2 es el PC donde corren, visto desde el emulador de Android Studio.
// Para un teléfono físico se reemplaza por la IP del PC en la red Wi-Fi.
object ApiConfig {
    const val URL_PRODUCTOS = "http://10.0.2.2:8081/"
    const val URL_PROGRAMAS = "http://10.0.2.2:8082/"
    const val URL_COLABORACIONES = "http://10.0.2.2:8083/"

    // API externa del clima (internet, HTTPS). No depende del PC ni de los microservicios.
    const val URL_CLIMA = "https://api.open-meteo.com/"
}
