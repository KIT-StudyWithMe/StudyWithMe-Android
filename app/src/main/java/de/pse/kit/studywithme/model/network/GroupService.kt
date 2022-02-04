package de.pse.kit.studywithme.model.network

import de.pse.kit.studywithme.model.data.Group
import de.pse.kit.studywithme.model.data.Lecture
import de.pse.kit.studywithme.model.data.User
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*

interface GroupService {
    suspend fun getGroups(search: String): List<Group>
    suspend fun getJoinedGroups(uid: Int): List<Group>
    suspend fun getGroupSuggestions(uid: Int): List<Group>
    suspend fun getGroup(groupID: Int): Group?
    suspend fun getGroupMembers(groupID: Int): List<User>?
    suspend fun newGroup(group: Group): Group?
    suspend fun editGroup(groupID: Int, group: Group)
    suspend fun removeGroup(groupID: Int)
    suspend fun newMember(groupID: Int, uid: Int): Group?
    suspend fun joinRequest(groupID: Int)
    suspend fun removeMember(groupID: Int, uid: Int)
    suspend fun getLectures(prefix: String): List<Lecture>

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