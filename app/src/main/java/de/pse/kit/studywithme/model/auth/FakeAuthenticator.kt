package de.pse.kit.studywithme.model.auth

import de.pse.kit.studywithme.model.data.User

object FakeAuthenticator: AuthenticatorInterface {
    override val firebaseUID: String
        get() = "dfg46thrge7fnd"
    override var user: User? = null
    private var signedInUser = "user@mail.com"
    private var signedUpUsers = mutableMapOf("user@mail.com" to "password")

    override suspend fun getToken(refresh: Boolean): String? {
        TODO("Not yet implemented")
    }

    override suspend fun signUp(email: String, password: String): Boolean {
        signedUpUsers[email] = password
        signedInUser = email
        return true
    }

    override suspend fun signIn(email: String, password: String): Boolean {
        return signedInUser == email && signedUpUsers[email] == password
    }

    override suspend fun resetPassword(email: String): Boolean {
        return true
    }

    override fun signOut() {
        signedInUser = ""
    }

    override suspend fun deleteFirebaseUser(password: String): Boolean {
        if (signedUpUsers[signedInUser] == password) {
            signedUpUsers.remove(signedInUser)
            return true
        }
        return false
    }
}