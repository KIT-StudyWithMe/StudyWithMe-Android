package de.pse.kit.studywithme.model.auth

import de.pse.kit.studywithme.model.data.User

interface AuthenticatorInterface {

    val TAG: String
        get() = "AUTH"
    val firebaseUID: String?
    var user: User?
    val signedIn: Boolean

    suspend fun getToken(refresh: Boolean): String?

    suspend fun signUp(email: String, password: String): Boolean

    suspend fun signIn(email: String, password: String): Boolean

    suspend fun resetPassword(email: String): Boolean

    fun signOut()

    suspend fun deleteFirebaseUser(password: String): Boolean
}