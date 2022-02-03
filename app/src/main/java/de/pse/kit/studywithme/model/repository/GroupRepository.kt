package de.pse.kit.studywithme.model.repository

import android.content.Context
import de.pse.kit.studywithme.SingletonHolder
import de.pse.kit.studywithme.model.data.Group
import de.pse.kit.studywithme.model.data.Lecture
import de.pse.kit.studywithme.model.data.User

import de.pse.kit.studywithme.model.network.GroupService
import kotlinx.coroutines.runBlocking

class GroupRepository private constructor(context: Context) {
    private val groupService = GroupService.instance

    fun getGroups(search: String): List<Group> {
        return runBlocking {
            return@runBlocking groupService.getGroups(search)
        }
    }

    fun getJoinedGroups(uid: Int): List<Group> {
        return runBlocking {
            return@runBlocking groupService.getJoinedGroups(uid)
        }
    }

    fun getGroupSuggestions(uid: Int): List<Group> {
        return runBlocking {
            return@runBlocking groupService.getGroupSuggestions(uid)
        }
    }

    fun getGroup(groupID: Int): Group {
        return runBlocking {
            return@runBlocking groupService.getGroup(groupID)
        }
    }

    fun newGroup(group: Group) {
        return runBlocking {
            return@runBlocking groupService.newGroup(group)
        }
    }

    fun editGroup(groupID: Int, group: Group) {
        return runBlocking {
            return@runBlocking groupService.editGroup(groupID, group)
        }
    }

    fun exitGroup(groupID: Int){
        //TODO
    }

    fun deleteGroup(groupID: Int) {
        return runBlocking {
            return@runBlocking groupService.removeGroup(groupID)
        }
    }

    fun newMember(groupID: Int, uid: Int) {
        return runBlocking {
            return@runBlocking groupService.newMember(groupID, uid)
        }
    }

    fun joinRequest(groupID: Int) {
        return runBlocking {
            return@runBlocking groupService.joinRequest(groupID)
        }
    }

    fun removeMember(groupID: Int, uid: Int) {
        return runBlocking {
            return@runBlocking groupService.removeMember(groupID, uid)
        }
    }

    fun getGroupMember(groupID: Int): List<User>? {
        return runBlocking {
            return@runBlocking groupService.getGroupMembers(groupID)
        }
    }

    fun getLectures(prefix: String): List<Lecture> {
        return runBlocking {
            return@runBlocking groupService.getLectures(prefix)
        }
    }

    companion object : SingletonHolder<GroupRepository, Context>({ GroupRepository(it) })
}