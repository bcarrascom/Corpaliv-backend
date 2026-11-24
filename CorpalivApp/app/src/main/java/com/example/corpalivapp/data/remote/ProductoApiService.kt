package com.example.corpalivapp.data.remote

import com.example.corpalivapp.data.model.Producto
import retrofit2.http.GET

// Endpoints de ms-productos que usa la app. Retrofit genera la implementación.
interface ProductoApiService {

    // GET http://10.0.2.2:8081/api/productos
    @GET("api/productos")
    suspend fun listar(): List<Producto>
}
