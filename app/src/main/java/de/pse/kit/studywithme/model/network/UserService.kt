package de.pse.kit.studywithme.model.network

import de.pse.kit.studywithme.model.data.Institution
import de.pse.kit.studywithme.model.data.Major
import de.pse.kit.studywithme.model.data.User
import de.pse.kit.studywithme.model.data.UserLight
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*

interface UserService {

    suspend fun getUsers(): List<User>?
    suspend fun getUser(id: Int): User?
    suspend fun getUser(fbid: String): UserLight?
    suspend fun newUser(user: User): User?
    suspend fun removeUser(uid: Int): Boolean
    suspend fun editUser(uid: Int, user: User): User?
    suspend fun getColleges(prefix: String): List<Institution>?
    suspend fun newCollege(institution: Institution): Institution?
    suspend fun getMajors(prefix: String): List<Major>?
    suspend fun newMajor(major: Major): Major?



    companion object {
        val instance: UserService by lazy {
            UserServiceImpl(client = HttpClient(Android) {
                install(JsonFeature) {
                    serializer = KotlinxSerializer()
                }
            })
        }
    }
}