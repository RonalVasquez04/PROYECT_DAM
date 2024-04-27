package es.rfvl.ronal_proyect_dam.DataApi

import com.google.gson.annotations.SerializedName

data class ResultsResponse(
    @SerializedName("position") val position: Int,
    @SerializedName("product") val product: ProductResponse,
    @SerializedName("offers") val offers: OffersResponse
)
