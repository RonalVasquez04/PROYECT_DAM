package es.rfvl.ronal_proyect_dam.DataApi

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ArticuloService {

    // Cambiar los response
    @GET("products")
    suspend fun getListArticles(): Response<List<ProductResponse>>
}