package es.rfvl.ronal_proyect_dam.DataApi

import com.google.gson.annotations.SerializedName

data class ProductRatingResponse(
    @SerializedName("rate") val rate: Double,
    @SerializedName("count") val count: Int
)
