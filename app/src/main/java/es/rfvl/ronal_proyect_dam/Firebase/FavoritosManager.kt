package es.rfvl.ronal_proyect_dam.Firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import es.rfvl.ronal_proyect_dam.classes.Articulo
import kotlinx.coroutines.tasks.await

class FavoritosManager {
    val myDBFavoritos: FirebaseFirestore = FirebaseFirestore.getInstance()

    val coleccionFavoritos = myDBFavoritos.collection("favoritos")

    suspend fun getProductosFavoritos(usuario: String): MutableList<String> {
        val datos = mutableListOf<String>()

        try {
            val subcoleccion = myDBFavoritos.collection("favoritos").get().await()

            for (document in subcoleccion.documents) {
                val subcoleccionUsuarioFav = document.reference.collection("usuariofav_$usuario")
                val subcoleccionSnapshot = subcoleccionUsuarioFav.get().await()

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

    suspend fun addProductoFavorito(id: String, usuario: String){

        try {
            val subcoleccion = myDBFavoritos.collection("favoritos").get().await()

            for (document in subcoleccion.documents) {
                val subcoleccionUsuarioFav = document.reference.collection("usuariofav_$usuario")
                val subcoleccionSnapshot = subcoleccionUsuarioFav.get().await()

                subcoleccionUsuarioFav.add(mapOf(
                    "idProducto" to id
                ))

            }
        } catch (e: Exception) {
            println("Error al obtener productos favoritos: ${e.message}")
        }

    }

    suspend fun deleteProductoFavorito(id: String, usuario: String){
        try {
            val subcoleccion = myDBFavoritos.collection("favoritos").get().await()

            for (document in subcoleccion.documents) {
                val subcoleccionUsuarioFav = document.reference.collection("usuariofav_$usuario")
                val subcoleccionSnapshot = subcoleccionUsuarioFav.get().await()

                for (producto in subcoleccionSnapshot.documents) {
                    if (producto.getString("idProducto") == id) {
                        producto.reference.delete().await()
                        println("Producto favorito eliminado correctamente")
                        return
                    }
                }

            }
        } catch (e: Exception) {
            println("Error al eliminar el producto: ${e.message}")
        }
    }
}