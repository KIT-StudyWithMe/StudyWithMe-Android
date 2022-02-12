package de.pse.kit.studywithme.model.repository

import de.pse.kit.studywithme.model.data.*
import kotlinx.coroutines.flow.Flow

interface GroupRepositoryInterface {

    fun getGroups(search: String): List<Group>

    fun getJoinedGroups(): Flow<List<Group>>

    fun getGroupSuggestions(): List<Group>

    fun getGroup(groupID: Int): Flow<Group>

    fun newGroup(group: Group): Boolean

    fun editGroup(group: Group): Boolean

    fun exitGroup(groupID: Int, uid: Int)

    fun deleteGroup(group: Group): Boolean

    fun hideGroup(groupID: Int, hidden:Boolean): Boolean

    fun newMember(groupID: Int, uid: Int): Boolean

    fun joinRequest(groupID: Int): Boolean

    fun getJoinRequests(groupID: Int): List<UserLight>

    fun removeMember(groupID: Int, uid: Int)

    fun leaveGroup(groupID: Int)

    fun getGroupMembers(groupID: Int): Flow<List<GroupMember>>

    fun getGroupAdmins(groupID: Int): Flow<List<GroupMember>>

    fun isSignedInUserAdmin(groupID: Int): Flow<Boolean>

    fun hasSignedInUserJoinRequested(groupID: Int): Boolean

    fun getLectures(prefix: String): Flow<List<Lecture>>

    fun getLecture(name: String): Lecture?

    fun reportGroup(groupID: Int, groupField: GroupField)

    fun reportUser(userID: Int, userField: UserField)
}