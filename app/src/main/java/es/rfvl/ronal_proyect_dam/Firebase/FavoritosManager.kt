package es.rfvl.ronal_proyect_dam.Firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.QuerySnapshot
import es.rfvl.ronal_proyect_dam.classes.Articulo
import es.rfvl.ronal_proyect_dam.classes.Comentario
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
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

    suspend fun getProductosFavFlow(usuario: String): Flow<List<String>> = callbackFlow {
        val listenerRegistrations = mutableListOf<ListenerRegistration>()

        try {
            val subcoleccion = myDBFavoritos.collection("favoritos").get().await()

            for (document in subcoleccion.documents) {
                val subcoleccionUsuarioFav = document.reference.collection("usuariofav_$usuario")

                val registration = subcoleccionUsuarioFav.addSnapshotListener { snapshot, _ ->
                    if (snapshot != null) {
                        val datos = mutableListOf<String>()
                        for (subdocument in snapshot.documents) {
                            val idProducto = subdocument.getString("idProducto")
                            if (idProducto != null) {
                                datos.add(idProducto)
                            }
                        }
                        trySend(datos).isSuccess
                    }
                }
                listenerRegistrations.add(registration)
            }

            awaitClose {
                listenerRegistrations.forEach { it.remove() }
            }
        } catch (e: Throwable) {
            close(e)
        }
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