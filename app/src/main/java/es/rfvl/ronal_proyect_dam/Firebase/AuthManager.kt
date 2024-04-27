package es.rfvl.ronal_proyect_dam.Firebase

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class AuthManager {
    private val auth: FirebaseAuth by lazy { Firebase.auth}

    suspend fun createUser(email: String, password: String): FirebaseUser?{
        return try {
            val result = auth.createUserWithEmailAndPassword(email,password).await()
            result.user
        }catch (er: Exception){
            null
        }
    }

    suspend fun login(email: String, password: String): FirebaseUser?{
        return try {
            val authResult = auth.signInWithEmailAndPassword(email, password).await()
            authResult.user
        }catch (e: Exception){
            Log.e("AuthManager", "Error al iniciar sesión: ${e.message}", e)
            null
        }
    }

    suspend fun resetPassword(email: String){
        try {
            auth.sendPasswordResetEmail(email).await()
        }catch (e: Exception){
            null
        }

    }


    fun logOut(){
        auth.signOut()
    }
    fun getCurrentUser(): FirebaseUser? {
        return  auth.currentUser
    }
}