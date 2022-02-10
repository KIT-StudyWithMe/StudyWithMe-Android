package de.pse.kit.studywithme.model.repository

import android.content.Context
import de.pse.kit.studywithme.model.data.Institution
import de.pse.kit.studywithme.model.data.Major
import de.pse.kit.studywithme.model.data.User
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow

interface UserRepositoryInterface {
    @ExperimentalCoroutinesApi
    fun isSignedIn(): Boolean

    @ExperimentalCoroutinesApi
    fun getSignedInUser(): Flow<User>

    fun editSignedInUser(user: User): Boolean

    fun signIn(email: String, password: String): Boolean

    fun signUp(email: String, password: String, username: String, major: String, institution: String): Boolean

    fun resetPassword(email: String): Boolean

    fun signOut()

    @ExperimentalCoroutinesApi
    fun deleteAccount(email: String, password: String): Boolean

    fun getMajors(prefix: String): List<String>

    fun getMajor(name: String): Major?

    fun getColleges(prefix: String): List<String>

    fun getCollege(name: String): Institution?
}