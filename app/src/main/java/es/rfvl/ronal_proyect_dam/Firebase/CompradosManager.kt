package es.rfvl.ronal_proyect_dam.Firebase

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class CompradosManager {
    val myDBComprados: FirebaseFirestore = FirebaseFirestore.getInstance()

    val coleccionComprados = myDBComprados.collection("comprados")

    suspend fun getProductosComprados(usuario: String): MutableList<String> {
        val datos = mutableListOf<String>()

        try {
            val subcoleccion = myDBComprados.collection("comprados").get().await()

            for (document in subcoleccion.documents) {
                val subcoleccionUsuario = document.reference.collection("usuario_$usuario")
                val subcoleccionSnapshot = subcoleccionUsuario.get().await()

                for (subdocument in subcoleccionSnapshot.documents) {
                    val idProducto = subdocument.getString("idProducto")
                    if (idProducto != null) {
                        datos.add(idProducto)
                    }
                }
            }
        } catch (e: Exception) {
            println("Error al obtener productos favoritos: ${e.message}")
        }

        return datos
    }
}