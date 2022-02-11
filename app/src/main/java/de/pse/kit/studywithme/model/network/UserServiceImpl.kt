package de.pse.kit.studywithme.model.network

import de.pse.kit.studywithme.model.data.Institution
import de.pse.kit.studywithme.model.data.Major
import de.pse.kit.studywithme.model.data.User
import de.pse.kit.studywithme.model.data.UserLight
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

class UserServiceImpl(private var client: HttpClient) : UserService {

    override suspend fun getUsers(): List<User>? {
        return try {
            client.get(HttpRoutes.USERS)
        } catch (e: RedirectResponseException) {
            println("Redirect Error: ${e.response.status}")
            null
        } catch (e: ClientRequestException) {
            println("Request Error: ${e.response.status}")
            null
        } catch (e: ServerResponseException) {
            println("Response Error: ${e.response.status}")
            null
        } catch (e: Exception) {
            println("Error: ${e.message}")
            null
        }
    }

    override suspend fun getUser(id: Int): User? {
        return try {
            client.get(HttpRoutes.USERS + id + "/detail")
        } catch (e: RedirectResponseException) {
            println("Redirect Error: ${e.response.status}")
            null
        } catch (e: ClientRequestException) {
            println("Request Error: ${e.response.status}")
            null
        } catch (e: ServerResponseException) {
            println("Response Error: ${e.response.status}")
            null
        } catch (e: Exception) {
            println("Error: ${e.message}")
            null
        }
    }

    override suspend fun getUser(fbid: String): UserLight? {
        return try {
            val users: List<UserLight> = client.get(HttpRoutes.USERS) {
                parameter("fuid", fbid)
            }
            return users.last()
        } catch (e: RedirectResponseException) {
            println("Redirect Error: ${e.response.status}")
            null
        } catch (e: ClientRequestException) {
            println("Request Error: ${e.response.status}")
            null
        } catch (e: ServerResponseException) {
            println("Response Error: ${e.response.status}")
            null
        } catch (e: Exception) {
            println("Error: ${e.message}")
            null
        }
    }

    override suspend fun newUser(user: User): User? {
        return try {
            client.post(HttpRoutes.USERS) {
                contentType(ContentType.Application.Json)
                body = user
            }
        } catch (e: RedirectResponseException) {
            println("Redirect Error: ${e.response.status}")
            null
        } catch (e: ClientRequestException) {
            println("Request Error: ${e.response.status}")
            null
        } catch (e: ServerResponseException) {
            println("Response Error: ${e.response.status}")
            null
        } catch (e: Exception) {
            println("Error: ${e.message}")
            null
        }
    }

    override suspend fun removeUser(uid: Int): Boolean {
        return try {
            client.delete<HttpResponse>(HttpRoutes.USERS + uid)
            true
        } catch (e: RedirectResponseException) {
            println("Redirect Error: ${e.response.status}")
            false
        } catch (e: ClientRequestException) {
            println("Request Error: ${e.response.status}")
            false
        } catch (e: ServerResponseException) {
            println("Response Error: ${e.response.status}")
            false
        } catch (e: Exception) {
            println("Error: ${e.message}")
            false
        }
    }

    override suspend fun editUser(uid: Int, user: User): User? {
        return try {
            client.put(HttpRoutes.USERS + uid + "/detail") {
                contentType(ContentType.Application.Json)
                body = user
            }
        } catch (e: RedirectResponseException) {
            println("Redirect Error: ${e.response.status}")
            null
        } catch (e: ClientRequestException) {
            println("Request Error: ${e.response.status}")
            null
        } catch (e: ServerResponseException) {
            println("Response Error: ${e.response.status}")
            null
        } catch (e: Exception) {
            println("Error: ${e.message}")
            null
        }
    }

    override suspend fun getColleges(prefix: String): List<Institution>? {
        return try {
            client.get(HttpRoutes.INSTITUTIONS) {
                parameter("name", prefix)
            }
        } catch (e: RedirectResponseException) {
            println("Redirect Error: ${e.response.status}")
            null
        } catch (e: ClientRequestException) {
            println("Request Error: ${e.response.status}")
            null
        } catch (e: ServerResponseException) {
            println("Response Error: ${e.response.status}")
            null
        } catch (e: Exception) {
            println("Error: ${e.message}")
            null
        }
    }

    override suspend fun newCollege(institution: Institution): Institution? {
        return try {
            client.post(HttpRoutes.INSTITUTIONS) {
                contentType(ContentType.Application.Json)
                body = institution
            }
        } catch (e: RedirectResponseException) {
            println("Redirect Error: ${e.response.status}")
            null
        } catch (e: ClientRequestException) {
            println("Request Error: ${e.response.status}")
            null
        } catch (e: ServerResponseException) {
            println("Response Error: ${e.response.status}")
            null
        } catch (e: Exception) {
            println("Error: ${e.message}")
            null
        }
    }

    override suspend fun getMajors(prefix: String): List<Major>? {
        return try {
            client.get(HttpRoutes.MAJORS) {
                parameter("name", prefix)
            }
        } catch (e: RedirectResponseException) {
            println("Redirect Error: ${e.response.status}")
            null
        } catch (e: ClientRequestException) {
            println("Request Error: ${e.response.status}")
            null
        } catch (e: ServerResponseException) {
            println("Response Error: ${e.response.status}")
            null
        } catch (e: Exception) {
            println("Error: ${e.message}")
            null
        }
    }

    override suspend fun newMajor(major: Major): Major? {
        return try {
            client.post(HttpRoutes.MAJORS) {
                contentType(ContentType.Application.Json)
                body = major
            }
        } catch (e: RedirectResponseException) {
            println("Redirect Error: ${e.response.status}")
            null
        } catch (e: ClientRequestException) {
            println("Request Error: ${e.response.status}")
            null
        } catch (e: ServerResponseException) {
            println("Response Error: ${e.response.status}")
            null
        } catch (e: Exception) {
            println("Error: ${e.message}")
            null
        }
    }
}