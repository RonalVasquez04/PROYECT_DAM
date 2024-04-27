package es.rfvl.ronal_proyect_dam.DataApi

import com.google.gson.annotations.SerializedName

data class PrimaryResponse(
    @SerializedName("price") val price: Double
)
