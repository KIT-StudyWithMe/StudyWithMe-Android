package de.pse.kit.studywithme.model.auth

import android.util.Log
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import de.pse.kit.studywithme.GeneratedExclusion
import de.pse.kit.studywithme.model.data.User
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeout

/**
 * Object required for the authentification of a user
 *
 * @constructor Create empty Authenticator
 */
@GeneratedExclusion
object Authenticator: AuthenticatorInterface {
    private val auth = Firebase.auth
    private var firebaseUser: FirebaseUser? = auth.currentUser
    override val firebaseUID: String?
        get() = firebaseUser?.uid
    override var user: User? = null
    override val signedIn: Boolean
        get() = user != null && firebaseUID != null
    override val signInMail: String?
        get() = firebaseUser?.email

    override suspend fun getToken(refresh: Boolean): String? {
        try {
            Log.d("TOKEN","refresh: $refresh")
            return withTimeout(2000) {
                val result = firebaseUser?.getIdToken(refresh) ?: return@withTimeout null
                Log.d("TOKEN","user: $firebaseUser, result: $result")
                val t = result.await().token
                Log.d("TOKEN", "token: $t")
                t
            }
        } catch (e: Exception) {
            Log.w(TAG, "getIdToken:failure", e)
            return null
        }
    }

    override suspend fun signUp(email: String, password: String): Boolean {
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

    override suspend fun signIn(email: String, password: String): Boolean {
        try {
            auth.signInWithEmailAndPassword(email, password).await()
            firebaseUser = auth.currentUser
            Log.d(TAG, "signInWithEmail(${auth.uid}):success")
            Log.d(TAG, "signInWithEmail($firebaseUID):success")
            return true
        } catch (e: Exception) {
            Log.w(TAG, "signInWithEmail:failure", e)
            return false
        }
    }

    override suspend fun resetPassword(email: String): Boolean {
        try {
            auth.sendPasswordResetEmail(email).await()
            Log.d(TAG, "resetPasswortEmailSend:success")
            return true
        } catch (e: Exception) {
            Log.w(TAG, "resetPasswortEmailSend:failure", e)
            return false
        }
    }

    override fun signOut() {
        auth.signOut()
        firebaseUser = null
        user = null
    }

    override suspend fun deleteFirebaseUser(password: String): Boolean {
        if (firebaseUser == null) {
            return false
        }

        val credential = EmailAuthProvider.getCredential(firebaseUser!!.email!!, password)

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