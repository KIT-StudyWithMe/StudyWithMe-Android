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

/**
 * The implementation of the functions from the user interface
 *
 * @property client
 * @constructor Create empty User service impl
 */
class UserServiceImpl(private var client: HttpClient) : UserService {

    override suspend fun getUsers(): List<User>? {
        return try {
            client.get(HttpRoutes.USERS)
        } catch (e: RedirectResponseException) {
            println("GetUsers Redirect Error: ${e.response.status}")
            null
        } catch (e: ClientRequestException) {
            println("GetUsers Request Error: ${e.response.status}")
            null
        } catch (e: ServerResponseException) {
            println("GetUsers Response Error: ${e.response.status}")
            null
        } catch (e: Exception) {
            println("GetUsers Error: ${e.message}")
            null
        }
    }

    override suspend fun getUser(id: Int): User? {
        return try {
            client.get(HttpRoutes.USERS + id + "/detail")
        } catch (e: RedirectResponseException) {
            println("GetUser Redirect Error: ${e.response.status}")
            null
        } catch (e: ClientRequestException) {
            println("GetUser Request Error: ${e.response.status}")
            null
        } catch (e: ServerResponseException) {
            println("GetUser Response Error: ${e.response.status}")
            null
        } catch (e: Exception) {
            println("GetUser Error: ${e.message}")
            null
        }
    }

    override suspend fun getUser(fbid: String): UserLight? {
        return try {
            val users: List<UserLight> = client.get(HttpRoutes.USERS) {
                parameter("FUID", fbid)
            }
            println("test: "+ users)
            return users.last()
        } catch (e: RedirectResponseException) {
            println("GetUserByFUID Redirect Error: ${e.response.status}")
            null
        } catch (e: ClientRequestException) {
            println("GetUserByFUID Request Error: ${e.response.status}")
            null
        } catch (e: ServerResponseException) {
            println("GetUserByFUID Response Error: ${e.response.status}")
            null
        } catch (e: Exception) {
            println("GetUserByFUID Error: ${e.message}")
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
            println("NewUser Redirect Error: ${e.response.status}")
            null
        } catch (e: ClientRequestException) {
            println("NewUser Request Error: ${e.response.status}")
            null
        } catch (e: ServerResponseException) {
            println("NewUser Response Error: ${e.response.status}")
            null
        } catch (e: Exception) {
            println("NewUser Error: ${e.message}")
            null
        }
    }

    override suspend fun removeUser(uid: Int): Boolean {
        return try {
            client.delete<HttpResponse>(HttpRoutes.USERS + uid)
            true
        } catch (e: RedirectResponseException) {
            println("RemoveUser Redirect Error: ${e.response.status}")
            false
        } catch (e: ClientRequestException) {
            println("RemoveUser Request Error: ${e.response.status}")
            false
        } catch (e: ServerResponseException) {
            println("RemoveUser Response Error: ${e.response.status}")
            false
        } catch (e: Exception) {
            println("RemoveUser Error: ${e.message}")
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
            println("EditUser Redirect Error: ${e.response.status}")
            null
        } catch (e: ClientRequestException) {
            println("EditUser Request Error: ${e.response.status}")
            null
        } catch (e: ServerResponseException) {
            println("EditUser Response Error: ${e.response.status}")
            null
        } catch (e: Exception) {
            println("EditUser Error: ${e.message}")
            null
        }
    }

    override suspend fun getColleges(prefix: String): List<Institution>? {
        return try {
            client.get(HttpRoutes.INSTITUTIONS) {
                parameter("name", prefix)
            }
        } catch (e: RedirectResponseException) {
            println("GetColleges Redirect Error: ${e.response.status}")
            null
        } catch (e: ClientRequestException) {
            println("GetColleges Request Error: ${e.response.status}")
            null
        } catch (e: ServerResponseException) {
            println("GetColleges Response Error: ${e.response.status}")
            null
        } catch (e: Exception) {
            println("GetColleges Error: ${e.message}")
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
            println("NewCollege Redirect Error: ${e.response.status}")
            null
        } catch (e: ClientRequestException) {
            println("NewCollege Request Error: ${e.response.status}")
            null
        } catch (e: ServerResponseException) {
            println("NewCollege Response Error: ${e.response.status}")
            null
        } catch (e: Exception) {
            println("NewCollege Error: ${e.message}")
            null
        }
    }

    override suspend fun getMajors(prefix: String): List<Major>? {
        return try {
            client.get(HttpRoutes.MAJORS) {
                parameter("name", prefix)
            }
        } catch (e: RedirectResponseException) {
            println("GetMajors Redirect Error: ${e.response.status}")
            null
        } catch (e: ClientRequestException) {
            println("GetMajors Request Error: ${e.response.status}")
            null
        } catch (e: ServerResponseException) {
            println("GetMajors Response Error: ${e.response.status}")
            null
        } catch (e: Exception) {
            println("GetMajors Error: ${e.message}")
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
            println("NewMajor Redirect Error: ${e.response.status}")
            null
        } catch (e: ClientRequestException) {
            println("NewMajor Request Error: ${e.response.status}")
            null
        } catch (e: ServerResponseException) {
            println("NewMajor Response Error: ${e.response.status}")
            null
        } catch (e: Exception) {
            println("NewMajor Error: ${e.message}")
            null
        }
    }
}