package de.pse.kit.studywithme.model.network

import de.pse.kit.studywithme.model.data.User
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*

interface UserService {

    suspend fun getUsers(): List<User>
    suspend fun getUser(id: Int): User?
    suspend fun getJoinRequests(groupID: Int): List<User>
    suspend fun newUser(user: User): User?
    suspend fun removeUser(uid: Int)
    suspend fun editUser(uid: Int, user: User): User?
    suspend fun getColleges(authorization: String, prefix: String): List<String>
    suspend fun getLectures(prefix: String): List<String>?

    companion object {
        var service: UserServiceImpl? = null

        fun create(): UserServiceImpl {
            if (service == null) {
                service = UserServiceImpl(client = HttpClient(Android) {
                    install(JsonFeature) {
                        serializer = KotlinxSerializer()
                    }
                })
            }
            return service!!
        }
    }
}