package de.pse.kit.studywithme.model.network

import de.pse.kit.studywithme.model.data.Group
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*

interface GroupService {
    suspend fun getGroups(search: String): List<Group>
    suspend fun getJoinedGroups(uid: Int): List<Group>
    suspend fun getGroupSuggestions(uid: Int): List<Group>
    suspend fun getGroup(groupID: Int): Group
    suspend fun newGroup(group: Group)
    suspend fun editGroup(groupID: Int, group: Group)
    suspend fun removeGroup(groupID: Int)
    suspend fun newMember(groupID: Int, uid: Int)
    suspend fun joinRequest(groupID: Int)
    suspend fun removeMember(groupID: Int, uid: Int)

    companion object {
        var service: GroupServiceImpl? = null

        fun create(): GroupServiceImpl {
            if (service == null) {
                service = GroupServiceImpl(client = HttpClient(Android) {
                    install(JsonFeature) {
                        serializer = KotlinxSerializer()
                    }
                })
            }
            return service!!
        }
    }
}