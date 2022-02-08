package de.pse.kit.studywithme.model.repository

import de.pse.kit.studywithme.model.data.Group
import de.pse.kit.studywithme.model.data.Lecture
import de.pse.kit.studywithme.model.data.User
import kotlinx.coroutines.flow.Flow

interface GroupRepositoryInterface {

    fun getGroups(search: String): List<Group>

    fun getJoinedGroups(): Flow<List<Group>>

    fun getGroupSuggestions(): List<Group>

    fun getGroup(groupID: Int): Flow<Group>

    fun newGroup(group: Group): Boolean

    fun editGroup(group: Group): Boolean

    fun exitGroup(groupID: Int, uid: Int)

    fun deleteGroup(group: Group)

    fun newMember(groupID: Int, uid: Int): Boolean

    fun joinRequest(groupID: Int)

    fun removeMember(groupID: Int, uid: Int)

    fun getGroupMembers(groupID: Int): Flow<List<User>>

    fun getLectures(prefix: String): Flow<List<Lecture>>
}