package de.pse.kit.studywithme.model.network

import de.pse.kit.studywithme.model.data.*
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*

interface GroupService {
    suspend fun getGroups(search: String): List<RemoteGroup>
    suspend fun getJoinedGroups(uid: Int): List<RemoteGroup>
    suspend fun getGroupSuggestions(uid: Int): List<RemoteGroup>
    suspend fun getGroup(groupID: Int): RemoteGroup?
    suspend fun getGroupMembers(groupID: Int): List<GroupMember>?
    suspend fun newGroup(group: RemoteGroup): RemoteGroup?
    suspend fun editGroup(groupID: Int, group: RemoteGroup): RemoteGroup?
    suspend fun removeGroup(groupID: Int)
    suspend fun newMember(groupID: Int, uid: Int): GroupMember?
    suspend fun joinRequest(groupID: Int)
    suspend fun removeMember(groupID: Int, uid: Int)
    suspend fun getLectures(prefix: String): List<Lecture>
    suspend fun getLecture(lectureID: Int): Lecture?
    suspend fun newLecture(lecture: Lecture): Lecture?
    suspend fun getMajor(majorID: Int): Major?

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