package de.pse.kit.studywithme.model.repository

import android.content.Context
import de.pse.kit.studywithme.model.data.Institution
import de.pse.kit.studywithme.model.data.Major
import de.pse.kit.studywithme.model.data.User
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow

interface UserRepositoryInterface {
    /**
     * Returns true if user is signed in
     *
     * @return true or false
     */
    @ExperimentalCoroutinesApi
    fun isSignedIn(): Boolean

    /**
     * Returns signed in user
     *
     * @return flow user object
     */
    @ExperimentalCoroutinesApi
    fun getSignedInUser(): Flow<User>

    /**
     * Edits the signed in user and retruns true if successful
     *
     * @param user
     * @return true or false
     */
    fun editSignedInUser(user: User): Boolean

    /**
     * Signs in the user with his given details and returns true if successful
     *
     * @param email
     * @param password
     * @return true or false
     */
    fun signIn(email: String, password: String): Boolean

    /**
     * Registers user with his given details and returns true if successful
     *
     * @param email
     * @param password
     * @param username
     * @param major
     * @param institution
     * @return true or false
     */
    fun signUp(email: String, password: String, username: String, major: String, institution: String): Boolean

    /**
     * Resets the password and returns true if successful
     *
     * @param email
     * @return true or false
     */
    fun resetPassword(email: String): Boolean

    /**
     * Signs out a user of the application and returns true if successful
     *
     * @return true or false
     */
    fun signOut(): Boolean

    /**
     * Deletes the acoount with the given details and returns true if successful
     *
     * @param email
     * @param password
     * @return true or false
     */
    @ExperimentalCoroutinesApi
    fun deleteAccount(email: String, password: String): Boolean

    /**
     * Returns list of major starting with the given prefix
     *
     * @param prefix
     * @return list of strings
     */
    fun getMajors(prefix: String): List<String>

    /**
     * Returns major with the given name or null if there is none
     *
     * @param name
     * @return major object or null
     */
    fun getMajor(name: String): Major?

    /**
     * Returns list of colleges starting with the given prefix
     *
     * @param prefix
     * @return list of strings
     */
    fun getColleges(prefix: String): List<String>

    /**
     * Returns a college with the given name or null if there is none
     *
     * @param name
     * @return institution object or null
     */
    fun getCollege(name: String): Institution?
}