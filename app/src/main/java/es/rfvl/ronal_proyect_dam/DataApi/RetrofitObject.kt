package es.rfvl.ronal_proyect_dam.DataApi

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitObject {
    companion object {
        private const val BASE_URL = "https://api.bluecartapi.com/"

        // Configuración de OkHttpClient con timeouts
        private val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS) // Tiempo máximo para establecer la conexión
            .readTimeout(30, TimeUnit.SECONDS) // Tiempo máximo para leer datos
            .writeTimeout(30, TimeUnit.SECONDS) // Tiempo máximo para escribir datos
            .build()

        // Construcción de la instancia de Retrofit
        private val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient) // Asignación del OkHttpClient configurado
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        // Método para obtener la instancia de Retrofit
        fun getInstance(): Retrofit {
            return retrofit
        }
    }
}