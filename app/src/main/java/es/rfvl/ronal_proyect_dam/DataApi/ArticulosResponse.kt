package es.rfvl.ronal_proyect_dam.DataApi

import android.app.appsearch.SearchResults
import com.google.gson.annotations.SerializedName

data class ArticulosResponse(
    @SerializedName("search_result") val articulo: List<ResultsResponse>
)
