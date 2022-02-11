package de.pse.kit.studywithme.model.network

import de.pse.kit.studywithme.model.data.*
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.http.*

class GroupServiceImpl(private var client: HttpClient): GroupService {
    override suspend fun getGroups(search: String): List<RemoteGroup>? {
        return try {
            client.get(HttpRoutes.GROUPS) {
                parameter("text", search)
                parameter("lecture", search)
                parameter("name", search)
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

    override suspend fun getJoinedGroups(uid: Int): List<RemoteGroup>? {
        return try {
            client.get(HttpRoutes.USERS + uid + "/groups")
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

    override suspend fun getGroupSuggestions(uid: Int): List<RemoteGroup> {
        TODO("Not yet implemented")
    }

    override suspend fun getGroup(groupID: Int): RemoteGroup? {
        return try {
            client.get(HttpRoutes.GROUPS + groupID)
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

    override suspend fun getGroupMembers(groupID: Int): List<GroupMember>? {
        return try{
            client.get(HttpRoutes.GROUPS + groupID + "/users")
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

    override suspend fun newGroup(group: RemoteGroup, groupID: Int): RemoteGroup? {
        return try {
            client.post(HttpRoutes.GROUPS + groupID) {
                contentType(ContentType.Application.Json)
                body = group
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

    override suspend fun editGroup(groupID: Int, group: RemoteGroup): RemoteGroup? {
        return try {
            client.put(HttpRoutes.GROUPS + groupID) {
                contentType(ContentType.Application.Json)
                body = group
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

    override suspend fun removeGroup(groupID: Int): Boolean {
        return try {
            client.delete(HttpRoutes.GROUPS + groupID)
        } catch (e: RedirectResponseException) {
            println("Redirect Error: ${e.response.status.description}")
            false
        } catch (e: ClientRequestException) {
            println("Request Error: ${e.response.status.description}")
            false
        } catch (e: ServerResponseException) {
            println("Response Error: ${e.response.status.description}")
            false
        } catch (e: Exception) {
            println("Error: ${e.message}")
            false
        }
    }

    override suspend fun hideGroup(groupID: Int): Boolean {
        TODO("Not yet implemented")
    }

    // Erstmal auslassen
    override suspend fun newMember(groupID: Int, uid: Int): GroupMember? {
        TODO("Not yet implemented")
    }

    override suspend fun joinRequest(groupID: Int, uid: Int): Boolean {
        return try {
            client.put(HttpRoutes.GROUPS + groupID + "/join/" + uid)
        } catch (e: RedirectResponseException) {
            println("Redirect Error: ${e.response.status.description}")
            false
        } catch (e: ClientRequestException) {
            println("Request Error: ${e.response.status.description}")
            false
        } catch (e: ServerResponseException) {
            println("Response Error: ${e.response.status.description}")
            false
        } catch (e: Exception) {
            println("Error: ${e.message}")
            false
        }
    }


    override suspend fun getJoinRequests(groupID: Int): List<UserLight>? {
        return try {
            client.get(HttpRoutes.GROUPS + groupID + "/requests")
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

    override suspend fun removeMember(groupID: Int, uid: Int): Boolean {
        return try {
            client.delete(HttpRoutes.GROUPS + "/users/" + uid)
        } catch (e: RedirectResponseException) {
            println("Redirect Error: ${e.response.status.description}")
            false
        } catch (e: ClientRequestException) {
            println("Request Error: ${e.response.status.description}")
            false
        } catch (e: ServerResponseException) {
            println("Response Error: ${e.response.status.description}")
            false
        } catch (e: Exception) {
            println("Error: ${e.message}")
            false
        }
    }

    override suspend fun getLectures(majorID: Int, prefix: String): List<Lecture>? {
        return try {
            client.get(HttpRoutes.MAJORS + "$majorID/lectures") {
                parameter("name", prefix)
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

    override suspend fun getLecture(lectureID: Int): Lecture? {
        TODO("Not yet implemented")
    }

    override suspend fun newLecture(lecture: Lecture): Lecture? {
        TODO("Not yet implemented")
    }

    override suspend fun getMajor(majorID: Int): Major? {
        TODO("Not yet implemented")
    }

}