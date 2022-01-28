package de.pse.kit.studywithme.model.network

import de.pse.kit.studywithme.model.data.User
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.http.*

class UserServiceImpl(private var client: HttpClient) : UserService {

    override suspend fun getUsers(): List<User> {
        return try {
            client.get(HttpRoutes.USERS)
        } catch (e: RedirectResponseException) {
            println("Error: ${e.response.status.description}")
            emptyList()
        } catch (e: ClientRequestException) {
            println("Error: ${e.response.status.description}")
            emptyList()
        } catch (e: ServerResponseException) {
            println("Error: ${e.response.status.description}")
            emptyList()
        } catch (e: Exception) {
            println("Error: ${e.message}")
            emptyList()
        }
    }

    override suspend fun getUser(id: Int): User? {
        return try {
            client.get(HttpRoutes.USERS + id)
        } catch (e: RedirectResponseException) {
            println("Error: ${e.response.status.description}")
            null
        } catch (e: ClientRequestException) {
            println("Error: ${e.response.status.description}")
            null
        } catch (e: ServerResponseException) {
            println("Error: ${e.response.status.description}")
            null
        } catch (e: Exception) {
            println("Error: ${e.message}")
            null
        }
    }

    override suspend fun getJoinRequests(groupID: Int): List<User> {
        TODO("Not yet implemented")
    }

    override suspend fun newUser(user: User): User? {
        return try {
            client.post(HttpRoutes.USERS) {
                contentType(ContentType.Application.Json)
                body = user
            }
        } catch (e: RedirectResponseException) {
            println("Redirect Error: ${e.response.status.description}")
            null
        } catch (e: ClientRequestException) {
            println("Request Error: ${e.response.status.description}")
            null
        } catch (e: ServerResponseException) {
            println("Response Error: ${e.response.status.description}")
            null
        } catch (e: Exception) {
            println("Error: ${e.message}")
            null
        }
    }

    override suspend fun removeUser(uid: Int) {
        return try {
            client.delete(HttpRoutes.USERS + uid)
        } catch (e: RedirectResponseException) {
            println("Redirect Error: ${e.response.status.description}")
        } catch (e: ClientRequestException) {
            println("Request Error: ${e.response.status.description}")
        } catch (e: ServerResponseException) {
            println("Response Error: ${e.response.status.description}")
        } catch (e: Exception) {
            println("Error: ${e.message}")
        }
    }

    override suspend fun editUser(uid: Int, user: User): User? {
        return try {
            client.put(HttpRoutes.USERS + uid) {
                contentType(ContentType.Application.Json)
                body = user
            }
        } catch (e: RedirectResponseException) {
            println("Redirect Error: ${e.response.status.description}")
            null
        } catch (e: ClientRequestException) {
            println("Request Error: ${e.response.status.description}")
            null
        } catch (e: ServerResponseException) {
            println("Response Error: ${e.response.status.description}")
            null
        } catch (e: Exception) {
            println("Error: ${e.message}")
            null
        }
    }

    override suspend fun getColleges(authorization: String, prefix: String): List<String> {
        TODO("Not yet implemented")
    }

    override suspend fun getLectures(prefix: String): List<String> {
        TODO("Not yet implemented")
    }
}