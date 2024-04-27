package es.rfvl.ronal_proyect_dam.DataApi

import com.google.gson.annotations.SerializedName

data class ProductResponse(
    @SerializedName("title") val title: String,
    @SerializedName("link") val link: String,
    @SerializedName("item_id") val itemId: String,
    @SerializedName("product_id") val productId: String,
    //@SerializedName("images") val itemId: String,
    @SerializedName("main_image") val mainImage: String,
    @SerializedName("rating") val rating: Double
)
