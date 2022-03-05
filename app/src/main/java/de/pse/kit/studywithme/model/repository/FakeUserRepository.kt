package de.pse.kit.studywithme.model.repository

import de.pse.kit.studywithme.model.data.Institution
import de.pse.kit.studywithme.model.data.Major
import de.pse.kit.studywithme.model.data.User
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Repository class for internal tests of user components
 *
 * @property signedIn
 * @constructor Create empty Fake user repository
 */
class FakeUserRepository(var signedIn: Boolean) : UserRepositoryInterface {

    var user = User(
        userID = 0,
        name = "max.mustermann",
        contact = "max.mustermann@mustermail.com",
        college = "Karlsruher Institut für Technologie",
        major = "Informatik B.Sc.",
        firebaseUID = "0"
    )

    @ExperimentalCoroutinesApi
    override suspend fun isSignedIn(): Boolean {
        return signedIn
    }

    @ExperimentalCoroutinesApi
    override suspend fun getSignedInUser(): Flow<User> {
        return flow {
            emit(user)
        }
    }

    override suspend fun editSignedInUser(user: User): Boolean {
        this.user = user
        return true
    }

    override suspend fun signIn(email: String, password: String): Boolean {
        signedIn = true
        return true
    }

    override suspend fun signUp(
        email: String,
        password: String,
        username: String,
        major: String,
        institution: String
    ): Boolean {
        signedIn = true
        return true
    }

    override suspend fun resetPassword(email: String): Boolean {
        return true
    }

    override suspend fun signOut(): Boolean {
        signedIn = false
        return true
    }

    @ExperimentalCoroutinesApi
    override suspend fun deleteAccount(password: String): Boolean {
        signedIn = false
        return true
    }

    override suspend fun getMajors(prefix: String): List<String> {
        return listOf("Informatik B.Sc.", "Informatik M.Sc.")
    }

    override suspend fun getMajor(name: String): Major? {
        TODO("Not yet implemented")
    }

    override suspend fun getColleges(prefix: String): List<String> {
        return listOf("Karlsruher Institut für Technologie")
    }

    override suspend fun getCollege(name: String): Institution? {
        TODO("Not yet implemented")
    }

}