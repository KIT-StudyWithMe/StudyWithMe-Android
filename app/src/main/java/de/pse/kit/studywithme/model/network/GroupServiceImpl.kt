package de.pse.kit.studywithme.model.network

import de.pse.kit.studywithme.model.data.*
import io.ktor.client.*

class GroupServiceImpl(private var client: HttpClient): GroupService {
    override suspend fun getGroups(search: String): List<RemoteGroup> {
        TODO("Not yet implemented")
    }

    override suspend fun getJoinedGroups(uid: Int): List<RemoteGroup> {
        TODO("Not yet implemented")
    }

    override suspend fun getGroupSuggestions(uid: Int): List<RemoteGroup> {
        TODO("Not yet implemented")
    }

    override suspend fun getGroup(groupID: Int): RemoteGroup? {
        TODO("Not yet implemented")
    }

    override suspend fun getGroupMembers(groupID: Int): List<GroupMember>? {
        TODO("Not yet implemented")
    }

    override suspend fun newGroup(group: RemoteGroup): RemoteGroup? {
        TODO("Not yet implemented")
    }

    override suspend fun editGroup(groupID: Int, group: RemoteGroup): RemoteGroup {
        TODO("Not yet implemented")
    }

    override suspend fun removeGroup(groupID: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun newMember(groupID: Int, uid: Int): GroupMember? {
        TODO("Not yet implemented")
    }

    override suspend fun joinRequest(groupID: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun removeMember(groupID: Int, uid: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun getLectures(prefix: String): List<Lecture> {
        TODO("Not yet implemented")
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