package de.pse.kit.studywithme.model.auth

import de.pse.kit.studywithme.model.data.User

class FakeAuthenticator: AuthenticatorInterface {
    override var firebaseUID: String? = "dfg46thrge7fnd"
    override var user: User? = User(
        userID = 0,
        name = "max.mustermann",
        contact = "max.mustermann@mustermail.com",
        college = "Karlsruher Institut für Technologie",
        collegeID = 0,
        major = "Informatik B.Sc.",
        majorID = 0,
        firebaseUID = "dfg46thrge7fnd"
    )
    private var signedUpUsers = mutableMapOf("user@mail.com" to "password")

    override suspend fun getToken(refresh: Boolean): String? {
        TODO("Not yet implemented")
    }

    override suspend fun signUp(email: String, password: String): Boolean {
        signedUpUsers[email] = password
        user = User(
            userID = 0,
            name = "max.mustermann",
            contact = email,
            college = "Karlsruher Institut für Technologie",
            collegeID = 0,
            major = "Informatik B.Sc.",
            majorID = 0,
            firebaseUID = "dfg46thrge7fnd"
        )
        firebaseUID = "dfg46thrge7fnd"
        return true
    }

    override suspend fun signIn(email: String, password: String): Boolean {
        if (signedUpUsers[email] == password) {
            user = User(
                userID = 0,
                name = "max.mustermann",
                contact = email,
                college = "Karlsruher Institut für Technologie",
                collegeID = 0,
                major = "Informatik B.Sc.",
                majorID = 0,
                firebaseUID = "dfg46thrge7fnd"
            )
            firebaseUID = "dfg46thrge7fnd"
            return true
        }
        return false
    }

    override suspend fun resetPassword(email: String): Boolean {
        return true
    }

    override fun signOut() {
        user = User(
            userID = 0,
            name = "max.mustermann",
            contact = "",
            college = "Karlsruher Institut für Technologie",
            collegeID = 0,
            major = "Informatik B.Sc.",
            majorID = 0,
            firebaseUID = ""
        )
        firebaseUID = null
    }

    override suspend fun deleteFirebaseUser(password: String): Boolean {
        if (signedUpUsers[user?.contact] == password) {
            signOut()
            signedUpUsers.remove(user?.contact)
            firebaseUID = null
            return true
        }
        return false
    }
}