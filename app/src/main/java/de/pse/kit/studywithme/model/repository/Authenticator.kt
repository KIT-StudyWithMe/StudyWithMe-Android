package de.pse.kit.studywithme.model.repository

import android.util.Log
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import de.pse.kit.studywithme.model.data.User
import kotlinx.coroutines.tasks.await

object Authenticator {
    val TAG = "AUTH"
    private val auth = Firebase.auth
    private var firebaseUser: FirebaseUser? = auth.currentUser
    val firebaseUID: String?
        get() = firebaseUser?.uid
    var user: User? = null


    suspend fun signUp(email: String, password: String): Boolean {
        try {
            auth.createUserWithEmailAndPassword(email, password).await()
            firebaseUser = auth.currentUser
            Log.d(TAG, "createUserWithEmail(${auth.uid}):success")
            Log.d(TAG, "createUserWithEmail($firebaseUID):success")
            return true
        } catch (e: Exception) {
            Log.w(TAG, "createUserWithEmail:failure", e)
            return false
        }
    }

    suspend fun signIn(email: String, password: String): Boolean {
        try {
            auth.signInWithEmailAndPassword(email, password).await()
            firebaseUser = auth.currentUser
            Log.d(TAG, "signInWithEmail(${auth.uid}):success")
            Log.d(TAG, "signInWithEmail(${firebaseUID}):success")
            return true
        } catch (e: Exception) {
            Log.w(TAG, "signInWithEmail:failure", e)
            return false
        }
    }

    suspend fun resetPassword(email: String): Boolean {
        try {
            auth.sendPasswordResetEmail(email).await()
            Log.d(TAG, "resetPasswortEmailSend:success")
            return true
        } catch (e: Exception) {
            Log.w(TAG, "resetPasswortEmailSend:failure", e)
            return false
        }
    }

    fun signOut() {
        auth.signOut()
        firebaseUser = null
        //user = null
    }

    suspend fun deleteFirebaseUser(email: String, password: String): Boolean {
        if (firebaseUser == null) {
            if (!signIn(email, password)) {
                return false
            }
        }

        val credential = EmailAuthProvider.getCredential(email, password)

        try {
            firebaseUser!!.reauthenticate(credential).await()
            Log.d(TAG, "re-authenticate:success")

            firebaseUser!!.delete().await()
            Log.d(TAG, "deleteFirebaseUser:success")
            return true
        } catch (e: Exception) {
            Log.w(TAG, "deleteFirebaseUser:failure", e)
            return false
        }
    }
}