package es.rfvl.ronal_proyect_dam.DataApi

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ArticulosByCategory {
    @GET("products/category/{categoria}")
    suspend fun getListCategoriesProducts(@Path("categoria") categoria: String): Response<List<ProductResponse>>
}