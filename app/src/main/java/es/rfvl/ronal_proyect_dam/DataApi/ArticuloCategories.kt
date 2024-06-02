package es.rfvl.ronal_proyect_dam.DataApi

import retrofit2.Response
import retrofit2.http.GET

interface ArticuloCategories {
    @GET("products/categories")
    suspend fun getListCategories(): Response<List<String>>
}