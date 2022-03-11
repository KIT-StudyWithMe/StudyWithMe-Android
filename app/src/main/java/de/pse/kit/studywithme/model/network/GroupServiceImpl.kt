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
            }
        } catch (e: ResponseException) {
            println("GetGroups Error: ${e.response.status}")
            null
        } catch (e: Exception) {
            println("GetGroups Error: ${e.message}")
            null
        }
    }

    override suspend fun getJoinedGroups(uid: Int): List<RemoteGroup>? {
        return try {
            client.get(HttpRoutes.USERS + uid + "/groups")
        } catch (e: ResponseException) {
            println("GetJoinedGroups Error: ${e.response.status}")
            null
        } catch (e: Exception) {
            println("GetJoinedGroups Error: ${e.message}")
            null
        }
    }

    override suspend fun getGroupSuggestions(uid: Int): List<RemoteGroup>? {
        return try {
            client.get(HttpRoutes.GROUPS + "suggestion/" + uid)
        } catch (e: ResponseException) {
            println("GetGroupSuggestions Error: ${e.response.status}")
            null
        } catch (e: Exception) {
            println("GetGroupSuggestions Error: ${e.message}")
            null
        }
    }

    override suspend fun getGroup(groupID: Int): RemoteGroup? {
        return try {
            client.get(HttpRoutes.GROUPS + groupID)
        } catch (e: ResponseException) {
            println("GetGroup Error: ${e.response.status}")
            null
        } catch (e: Exception) {
            println("GetGroup Error: ${e.message}")
            null
        }
    }

    override suspend fun getGroupMembers(groupID: Int): List<GroupMember>? {
        return try{
            client.get(HttpRoutes.GROUPS + groupID + "/users")
        } catch (e: ResponseException) {
            println("GetGroupMembers Error: ${e.response.status}")
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
        } catch (e: ResponseException) {
            println("NewGroup Error: ${e.response.status}")
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
        } catch (e: ResponseException) {
            println("EditGroup Error: ${e.response.status}")
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
        } catch (e: ResponseException) {
            println("RemoveGroup Error: ${e.response.status}")
            false
        } catch (e: Exception) {
            println("RemoveGroup Error: ${e.message}")
            false
        }
    }

    override suspend fun hideGroup(groupID: Int): Boolean {
        return try {
            client.post<HttpResponse>(HttpRoutes.GROUPS + groupID + "/hide")
            true
        } catch (e: ResponseException) {
            println("HideGroup Error: ${e.response.status}")
            false
        } catch (e: Exception) {
            println("HideGroup Error: ${e.message}")
            false
        }
    }

    override suspend fun isGroupHidden(groupID: Int): Boolean? {
        return try {
            client.get(HttpRoutes.GROUPS + groupID + "/hide")
        } catch (e: ResponseException) {
            println("IsGroupHidden Error: ${e.response.status}")
            null
        } catch (e: Exception) {
            println("IsGroupHidden Error: ${e.message}")
            null
        }
    }

    override suspend fun newMember(groupID: Int, uid: Int): GroupMember? {
        return try {
            client.put(HttpRoutes.GROUPS + groupID + "/users/" + uid + "/membership") {
                contentType(ContentType.Application.Json)
                body = true
            }
        } catch (e: ResponseException) {
            println("NewMember Error: ${e.response.status}")
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
        } catch (e: ResponseException) {
            println("DeclineMember Error: ${e.response.status}")
            false
        } catch (e: Exception) {
            println("DeclineMember Error: ${e.message}")
            false
        }
    }

    override suspend fun joinRequest(groupID: Int, uid: Int): Boolean {
        return try {
            client.put<HttpResponse>(HttpRoutes.GROUPS + groupID + "/join/" + uid)
            true
        } catch (e: ResponseException) {
            println("JoinRequest Error: ${e.response.status}")
            false
        } catch (e: Exception) {
            println("JoinRequest Error: ${e.message}")
            false
        }
    }

    override suspend fun getJoinRequests(groupID: Int): List<UserLight>? {
        return try {
            client.get(HttpRoutes.GROUPS + groupID + "/requests")
        } catch (e: ResponseException) {
            println("GetJoinRequests Error: ${e.response.status}")
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
        } catch (e: ResponseException) {
            println("RemoveMember Error: ${e.response.status}")
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
        } catch (e: ResponseException) {
            println("GetLectures Error: ${e.response.status}")
            null
        } catch (e: Exception) {
            println("GetLectures Error: ${e.message}")
            null
        }
    }

    override suspend fun getLecture(lectureID: Int): Lecture? {
        return try {
            val lecture: Lecture? = client.get(HttpRoutes.LECTURES + lectureID)
            return lecture
        }catch (e: ResponseException) {
            println("GetLecture Error: ${e.response.status}")
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
        } catch (e: ResponseException) {
            println("NewLecture Error: ${e.response.status}")
            null
        } catch (e: Exception) {
            println("NewLecture Error: ${e.message}")
            null
        }
    }

    override suspend fun getMajor(majorID: Int): Major? {
        return try {
            client.get(HttpRoutes.MAJORS + majorID)
        } catch (e: ResponseException) {
            println("GetMajor Error: ${e.response.status}")
            null
        } catch (e: Exception) {
            println("GetMajor Error: ${e.message}")
            null
        }
    }

}