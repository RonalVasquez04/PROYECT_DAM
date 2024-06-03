package es.rfvl.ronal_proyect_dam.Firebase

import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import es.rfvl.ronal_proyect_dam.classes.Comentario
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class ComentariosManager {
    val myDBComentarios: FirebaseFirestore = FirebaseFirestore.getInstance()

    suspend fun getComentarios(id: String): MutableList<Comentario> {
        val datos = mutableListOf<Comentario>()

        try {
            val subcoleccion = myDBComentarios.collection("comentarios").get().await()

            for (document in subcoleccion.documents) {
                val subcoleccionUsuarioFav = document.reference.collection("producto$id")
                val subcoleccionSnapshot = subcoleccionUsuarioFav.get().await()

                for (subdocument in subcoleccionSnapshot.documents) {
                    val usuario = subdocument.getString("usuario")
                    val comentario = subdocument.getString("usuario")
                    if (usuario != null && comentario != null) {
                        datos.add(Comentario(usuario, comentario))
                    }
                }
            }
        } catch (e: Exception) {
            println("Error al obtener comentarios: ${e.message}")
        }

        return datos
    }

    suspend fun addComentarioProducto(id: String, usuario: String, comentario: String) {
        try {
            val subcoleccion = myDBComentarios.collection("comentarios").get().await()

            for (document in subcoleccion.documents) {
                val subcoleccionComentario = document.reference.collection("producto$id")
                subcoleccionComentario.add(
                    mapOf(
                        "usuario" to usuario,
                        "hora" to Timestamp.now(),
                        "comentario" to comentario
                    )
                ).await()
            }
        } catch (e: Exception) {
            println("Error al guardar comentario: ${e.message}")
        }
    }
    suspend fun getComentariosFlow(id: String): Flow<List<Comentario>> = callbackFlow {
        val listenerRegistrations = mutableListOf<ListenerRegistration>()

        try {
            val subcoleccion = myDBComentarios.collection("comentarios").get().await()

            for (document in subcoleccion.documents) {
                val subcoleccionUsuarioFav = document.reference.collection("producto$id")

                val registration = subcoleccionUsuarioFav.orderBy("hora").addSnapshotListener { snapshot, _ ->
                    if (snapshot != null) {
                        val datos = mutableListOf<Comentario>()
                        for (subdocument in snapshot.documents) {
                            val usuario = subdocument.getString("usuario")
                            val comentario = subdocument.getString("comentario")
                            if (usuario != null && comentario != null) {
                                datos.add(Comentario(usuario, comentario))
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
}

