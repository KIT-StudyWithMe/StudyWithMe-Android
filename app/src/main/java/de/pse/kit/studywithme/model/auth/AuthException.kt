package de.pse.kit.studywithme.model.auth

class AuthException: Exception() {
    override val message: String
        get() = "No user is signed in."
}