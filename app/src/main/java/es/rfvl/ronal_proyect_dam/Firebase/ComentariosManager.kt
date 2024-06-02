package es.rfvl.ronal_proyect_dam.Firebase

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class ComentariosManager {
    val myDBComentarios: FirebaseFirestore = FirebaseFirestore.getInstance()

    val coleccionComentarios = myDBComentarios.collection("comentarios")


}