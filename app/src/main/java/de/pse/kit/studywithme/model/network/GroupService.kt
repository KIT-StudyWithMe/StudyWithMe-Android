package de.pse.kit.studywithme.model.network

import de.pse.kit.studywithme.model.data.*
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*

interface GroupService {
    suspend fun getGroups(search: String): List<RemoteGroup>?
    suspend fun getJoinedGroups(uid: Int): List<RemoteGroup>?
    suspend fun getGroupSuggestions(uid: Int): List<RemoteGroup>?
    suspend fun getGroup(groupID: Int): RemoteGroup?
    suspend fun getGroupMembers(groupID: Int): List<GroupMember>?
    suspend fun newGroup(group: RemoteGroup, groupID: Int): RemoteGroup?
    suspend fun editGroup(groupID: Int, group: RemoteGroup): RemoteGroup?
    suspend fun removeGroup(groupID: Int): Boolean
    suspend fun newMember(groupID: Int, uid: Int): GroupMember?
    suspend fun joinRequest(groupID: Int, uid:Int): Boolean
    suspend fun getJoinRequests(groupID: Int): List<UserLight>?
    suspend fun removeMember(groupID: Int, uid: Int): Boolean
    suspend fun getLectures(prefix: String, majorID: Int): List<Lecture>?
    suspend fun getLecture(lectureID: Int): Lecture?
    suspend fun newLecture(lecture: Lecture, groupID: Int): Lecture?
    suspend fun getMajor(majorID: Int): Major?
    suspend fun hideGroup(hidden:Boolean, groupID: Int) :Boolean

    companion object {
        val instance: GroupService by lazy {
            GroupServiceImpl(client = HttpClient(Android) {
                install(JsonFeature) {
                    serializer = KotlinxSerializer()
                }
            })
        }
    }
}