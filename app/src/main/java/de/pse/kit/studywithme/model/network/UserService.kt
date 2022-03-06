package de.pse.kit.studywithme.model.network

import de.pse.kit.studywithme.model.data.Institution
import de.pse.kit.studywithme.model.data.Major
import de.pse.kit.studywithme.model.data.User
import de.pse.kit.studywithme.model.data.UserLight
import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.engine.android.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*

interface UserService {

    /**
     * Returns a list of users
     *
     * @return list of user object
     */
    suspend fun getUsers(): List<User>?

    /**
     * Returns the user with the ID id
     *
     * @param id
     * @return
     */
    suspend fun getUser(id: Int): User?

    /**
     * Returns the user with the firebaseUID fbid
     *
     * @param fbid
     * @return
     */
    suspend fun getUser(fbid: String): UserLight?

    /**
     * Creates a new user
     *
     * @param user
     * @return user object or null
     */
    suspend fun newUser(user: User): User?

    /**
     * Removes a user and returns if it was successful with a boolean
     *
     * @param uid
     * @return true or false
     */
    suspend fun removeUser(uid: Int): Boolean

    /**
     * Edits the user with the ID uid
     *
     * @param uid
     * @param user
     * @return user object or null
     */
    suspend fun editUser(uid: Int, user: User): User?

    /**
     * Returns a list of colleges which start with the given prefix
     *
     * @param prefix
     * @return List of intsitution object or null
     */
    suspend fun getColleges(prefix: String): List<Institution>?

    /**
     * Creates a new institution
     *
     * @param institution
     * @return institution object or null
     */
    suspend fun newCollege(institution: Institution): Institution?

    /**
     * Returns a list of majors which start with the given prefix
     *
     * @param prefix
     * @return List of major object or null
     */
    suspend fun getMajors(prefix: String): List<Major>?

    /**
     * Creates a new major
     *
     * @param major
     * @return
     */
    suspend fun newMajor(major: Major): Major?



    companion object {
        val instance: UserService by lazy {
            client(Android.create())
        }

        fun client(engine: HttpClientEngine): UserService {
            return UserServiceImpl(client = HttpClient(engine) {
                install(JsonFeature) {
                    val json = kotlinx.serialization.json.Json { ignoreUnknownKeys = true }
                    serializer = KotlinxSerializer(json)
                }
            })
        }
    }
}