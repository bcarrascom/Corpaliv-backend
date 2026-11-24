package com.example.corpalivapp.data.remote

import com.example.corpalivapp.data.model.CambioEstado
import com.example.corpalivapp.data.model.Colaboracion
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

// Endpoints de ms-colaboraciones (puerto 8083) que usa la app.
interface ColaboracionApiService {

    // GET api/colaboraciones?correo=ana@mail.cl  (si correo es null, trae todas)
    @GET("api/colaboraciones")
    suspend fun listar(@Query("correo") correo: String?): List<Colaboracion>

    // POST api/colaboraciones  con la solicitud en el cuerpo (JSON)
    @POST("api/colaboraciones")
    suspend fun crear(@Body colaboracion: Colaboracion): Colaboracion

    // PUT api/colaboraciones/5  reemplaza los datos (el servidor no cambia el estado)
    @PUT("api/colaboraciones/{id}")
    suspend fun actualizar(@Path("id") id: Long, @Body colaboracion: Colaboracion): Colaboracion

    // PATCH api/colaboraciones/5/estado  con { "estado": "Contactado" }
    @PATCH("api/colaboraciones/{id}/estado")
    suspend fun cambiarEstado(@Path("id") id: Long, @Body cambio: CambioEstado): Colaboracion

    // DELETE api/colaboraciones/5  (el servidor responde 204, sin cuerpo)
    @DELETE("api/colaboraciones/{id}")
    suspend fun eliminar(@Path("id") id: Long)
}
