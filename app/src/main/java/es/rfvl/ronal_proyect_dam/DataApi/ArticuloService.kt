package es.rfvl.ronal_proyect_dam.DataApi

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ArticuloService {

    // SOLUCIÓN : BUSCAR OTRA API
    //@GET("request?api_key=1BA925726E31431BAC9CB6C51F14408D&search_term={searchTerm}&type=search")
    //suspend fun getListArticles(@Path("searchTerm") searchTerm: String): Response<ArticulosResponse>

    @GET("request")
    suspend fun getListArticles(
        @Query("api_key") apiKey: String,
        @Query("search_term") searchTerm: String,
        @Query("type") type: String = "search"
    ): Response<ArticulosResponse>
}