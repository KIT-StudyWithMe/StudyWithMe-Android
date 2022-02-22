package de.pse.kit.studywithme.model.network

import android.util.Log
import de.pse.kit.studywithme.model.data.*
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

/**
 * The implementation of the functions from the group interface
 *
 * @property client
 * @constructor Create empty Group service impl
 */
class GroupServiceImpl(private var client: HttpClient): GroupService {
    override suspend fun getGroups(search: String): List<RemoteGroup>? {
        return try {
            client.get(HttpRoutes.GROUPS) {
                parameter("text", search)
                parameter("lecture", search)
                parameter("name", search)
            }
        } catch (e: RedirectResponseException) {
            println("GetGroups Redirect Error: ${e.response.status}")
            null
        } catch (e: ClientRequestException) {
            println("GetGroups Request Error: ${e.response.status}")
            null
        } catch (e: ServerResponseException) {
            println("GetGroups Response Error: ${e.response.status}")
            null
        } catch (e: Exception) {
            println("GetGroups Error: ${e.message}")
            null
        }
    }

    override suspend fun getJoinedGroups(uid: Int): List<RemoteGroup>? {
        return try {
            client.get(HttpRoutes.USERS + uid + "/groups")
        } catch (e: RedirectResponseException) {
            println("GetJoinedGroups Redirect Error: ${e.response.status}")
            null
        } catch (e: ClientRequestException) {
            println("GetJoinedGroups Request Error: ${e.response.status}")
            null
        } catch (e: ServerResponseException) {
            println("GetJoinedGroups Response Error: ${e.response.status}")
            null
        } catch (e: Exception) {
            println("GetJoinedGroups Error: ${e.message}")
            null
        }
    }

    override suspend fun getGroupSuggestions(uid: Int): List<RemoteGroup>? {
        return try {
            client.get(HttpRoutes.GROUPS + "suggestion/" + uid)
        } catch (e: RedirectResponseException) {
            println("GetJoinedSuggestions Redirect Error: ${e.response.status}")
            null
        } catch (e: ClientRequestException) {
            println("GetJoinedSuggestions Request Error: ${e.response.status}")
            null
        } catch (e: ServerResponseException) {
            println("GetJoinedSuggestions Response Error: ${e.response.status}")
            null
        } catch (e: Exception) {
            println("GetJoinedSuggestions Error: ${e.message}")
            null
        }
    }

    override suspend fun getGroup(groupID: Int): RemoteGroup? {
        return try {
            client.get(HttpRoutes.GROUPS + groupID)
        } catch (e: RedirectResponseException) {
            println("GetGroup Redirect Error: ${e.response.status}")
            null
        } catch (e: ClientRequestException) {
            println("GetGroup Request Error: ${e.response.status}")
            null
        } catch (e: ServerResponseException) {
            println("GetGroup Response Error: ${e.response.status}")
            null
        } catch (e: Exception) {
            println("GetGroup Error: ${e.message}")
            null
        }

    }

    override suspend fun getGroupMembers(groupID: Int): List<GroupMember>? {
        return try{
            client.get(HttpRoutes.GROUPS + groupID + "/users")
        } catch (e: RedirectResponseException) {
            println("GetGroupMembers Redirect Error: ${e.response.status}")
            null
        } catch (e: ClientRequestException) {
            println("GetGroupMembers Request Error: ${e.response.status}")
            null
        } catch (e: ServerResponseException) {
            println("GetGroupMembers Response Error: ${e.response.status}")
            null
        } catch (e: Exception) {
            println("GetGroupMembers Error: ${e.message}")
            null
        }
    }

    override suspend fun newGroup(group: RemoteGroup, userID: Int): RemoteGroup? {
        return try {
            client.post(HttpRoutes.GROUPS + userID) {
                contentType(ContentType.Application.Json)
                body = group
            }
        } catch (e: RedirectResponseException) {
            println("NewGroup Redirect Error: ${e.response.status}")
            null
        } catch (e: ClientRequestException) {
            println("NewGroup Request Error: ${e.response.status}")
            null
        } catch (e: ServerResponseException) {
            println("NewGroup Response Error: ${e.response.status}")
            null
        } catch (e: Exception) {
            println("NewGroup Error: ${e.message}")
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
            println("EditGroup Redirect Error: ${e.response.status}")
            null
        } catch (e: ClientRequestException) {
            println("EditGroup Request Error: ${e.response.status}")
            null
        } catch (e: ServerResponseException) {
            println("EditGroup Response Error: ${e.response.status}")
            null
        } catch (e: Exception) {
            println("EditGroup Error: ${e.message}")
            null
        }
    }

    override suspend fun removeGroup(groupID: Int): Boolean {
        return try {
            client.delete<HttpResponse>(HttpRoutes.GROUPS + groupID)
            true
        } catch (e: RedirectResponseException) {
            println("RemoveGroup Redirect Error: ${e.response.status}")
            false
        } catch (e: ClientRequestException) {
            println("RemoveGroup Request Error: ${e.response.status}")
            false
        } catch (e: ServerResponseException) {
            println("RemoveGroup Response Error: ${e.response.status}")
            false
        } catch (e: Exception) {
            println("RemoveGroup Error: ${e.message}")
            false
        }
    }

    override suspend fun hideGroup(groupID: Int, hidden:Boolean): Boolean {
        return try {
            val response = client.post<HttpResponse>(HttpRoutes.GROUPS + groupID + "/hide") {
                contentType(ContentType.Application.Json)
                body = hidden
            }
            println(response.status)
            true
        } catch (e: RedirectResponseException) {
            println("HideGroup Redirect Error: ${e.response.status}")
            false
        } catch (e: ClientRequestException) {
            println("HideGroup Request Error: ${e.response.status}")
            false
        } catch (e: ServerResponseException) {
            println("HideGroup Response Error: ${e.response.status}")
            false
        } catch (e: Exception) {
            println("HideGroup Error: ${e.message}")
            false
        }
    }

    override suspend fun newMember(groupID: Int, uid: Int): GroupMember? {
        return try {
            client.put(HttpRoutes.GROUPS + groupID + "/users/" + uid + "/membership") {
                contentType(ContentType.Application.Json)
                body = true
            }
        } catch (e: RedirectResponseException) {
            println("NewMember Redirect Error: ${e.response.status}")
            null
        } catch (e: ClientRequestException) {
            println("NewMember Request Error: ${e.response.status}")
            null
        } catch (e: ServerResponseException) {
            println("NewMember Response Error: ${e.response.status}")
            null
        } catch (e: Exception) {
            println("NewMember Error: ${e.message}")
            null
        }
    }

    override suspend fun declineMember(groupID: Int, uid: Int): Boolean {
        return try {
            client.put<HttpResponse>(HttpRoutes.GROUPS + groupID + "/users/" + uid + "/membership") {
                contentType(ContentType.Application.Json)
                body = false
            }
            true
        } catch (e: RedirectResponseException) {
            println("NewMember Redirect Error: ${e.response.status}")
            false
        } catch (e: ClientRequestException) {
            println("NewMember Request Error: ${e.response.status}")
            false
        } catch (e: ServerResponseException) {
            println("NewMember Response Error: ${e.response.status}")
            false
        } catch (e: Exception) {
            println("NewMember Error: ${e.message}")
            false
        }
    }

    override suspend fun joinRequest(groupID: Int, uid: Int): Boolean {
        return try {
            client.put<HttpResponse>(HttpRoutes.GROUPS + groupID + "/join/" + uid)
            true
        } catch (e: RedirectResponseException) {
            println("JoinRequest Redirect Error: ${e.response.status}")
            false
        } catch (e: ClientRequestException) {
            println("JoinRequest Request Error: ${e.response.status}")
            false
        } catch (e: ServerResponseException) {
            println("JoinRequest Response Error: ${e.response.status}")
            false
        } catch (e: Exception) {
            println("JoinRequest Error: ${e.message}")
            false
        }
    }


    override suspend fun getJoinRequests(groupID: Int): List<UserLight>? {
        return try {
            client.get(HttpRoutes.GROUPS + groupID + "/requests")
        } catch (e: RedirectResponseException) {
            println("GetJoinRequests Redirect Error: ${e.response.status}")
            null
        } catch (e: ClientRequestException) {
            println("GetJoinRequests Request Error: ${e.response.status}")
            null
        } catch (e: ServerResponseException) {
            println("GetJoinRequests Response Error: ${e.response.status}")
            null
        } catch (e: Exception) {
            println("GetJoinRequests Error: ${e.message}")
            null
        }
    }

    override suspend fun removeMember(groupID: Int, uid: Int): Boolean {
        return try {
            client.delete<HttpResponse>(HttpRoutes.GROUPS + groupID + "/users/" + uid)
            true
        } catch (e: RedirectResponseException) {
            println("RemoveMember Redirect Error: ${e.response.status}")
            false
        } catch (e: ClientRequestException) {
            println("RemoveMember Request Error: ${e.response.status}")
            false
        } catch (e: ServerResponseException) {
            println("RemoveMember Response Error: ${e.response.status}")
            false
        } catch (e: Exception) {
            println("RemoveMember Error: ${e.message}")
            false
        }
    }

    override suspend fun getLectures(majorID: Int, prefix: String): List<Lecture>? {
        return try {
            client.get(HttpRoutes.MAJORS + "$majorID/lectures") {
                parameter("name", prefix)
           }
        } catch (e: RedirectResponseException) {
            println("GetLectures Redirect Error: ${e.response.status}")
            null
        } catch (e: ClientRequestException) {
            println("GetLectures Request Error: ${e.response.status}")
            null
        } catch (e: ServerResponseException) {
            println("GetLectures Response Error: ${e.response.status}")
            null
        } catch (e: Exception) {
            println("GetLectures Error: ${e.message}")
            null
        }
    }

    override suspend fun getLecture(lectureID: Int): Lecture? {
        return try {
            val lecture: Lecture? = client.get(HttpRoutes.LECTURES + lectureID)
            println(lecture)
            return lecture
        } catch (e: RedirectResponseException) {
            println("GetLecture Redirect Error: ${e.response.status}")
            null
        } catch (e: ClientRequestException) {
            println("GetLecture Request Error: ${e.response.status}")
            null
        } catch (e: ServerResponseException) {
            println("GetLecture Response Error: ${e.response.status}")
            null
        } catch (e: Exception) {
            println("GetLecture Error: ${e.message}")
            null
        }
    }

    override suspend fun newLecture(lecture: Lecture, majorID: Int): Lecture? {
        return try {
            client.post(HttpRoutes.MAJORS + majorID + "/lectures") {
                contentType(ContentType.Application.Json)
                body = lecture
            }
        } catch (e: RedirectResponseException) {
            println("NewLecture Redirect Error: ${e.response.status}")
            null
        } catch (e: ClientRequestException) {
            println("NewLecture Request Error: ${e.response.status}")
            null
        } catch (e: ServerResponseException) {
            println("NewLecture Response Error: ${e.response.status}")
            null
        } catch (e: Exception) {
            println("NewLecture Error: ${e.message}")
            null
        }
    }

    override suspend fun getMajor(majorID: Int): Major? {
        return try {
            client.get(HttpRoutes.MAJORS + majorID)
        } catch (e: RedirectResponseException) {
            println("GetMajor Redirect Error: ${e.response.status}")
            null
        } catch (e: ClientRequestException) {
            println("GetMajor Request Error: ${e.response.status}")
            null
        } catch (e: ServerResponseException) {
            println("GetMajor Response Error: ${e.response.status}")
            null
        } catch (e: Exception) {
            println("GetMajor Error: ${e.message}")
            null
        }
    }

}