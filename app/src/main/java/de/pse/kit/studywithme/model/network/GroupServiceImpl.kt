package de.pse.kit.studywithme.model.network

import de.pse.kit.studywithme.model.data.Group
import de.pse.kit.studywithme.model.data.User
import io.ktor.client.*

class GroupServiceImpl(private var client: HttpClient): GroupService {
    override suspend fun getGroups(search: String): List<Group> {
        TODO("Not yet implemented")
    }

    override suspend fun getJoinedGroups(uid: Int): List<Group> {
        TODO("Not yet implemented")
    }

    override suspend fun getGroupSuggestions(uid: Int): List<Group> {
        TODO("Not yet implemented")
    }

    override suspend fun getGroup(groupID: Int): Group {
        TODO("Not yet implemented")
    }

    override suspend fun getGroupMembers(groupID: Int): List<User>? {
        TODO("Not yet implemented")
    }

    override suspend fun newGroup(group: Group) {
        TODO("Not yet implemented")
    }

    override suspend fun editGroup(groupID: Int, group: Group) {
        TODO("Not yet implemented")
    }

    override suspend fun removeGroup(groupID: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun newMember(groupID: Int, uid: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun joinRequest(groupID: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun removeMember(groupID: Int, uid: Int) {
        TODO("Not yet implemented")
    }

}