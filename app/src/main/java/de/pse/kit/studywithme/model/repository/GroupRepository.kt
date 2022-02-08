package de.pse.kit.studywithme.model.repository

import android.content.Context
import android.util.Log
import de.pse.kit.studywithme.SingletonHolder
import de.pse.kit.studywithme.model.data.*
import de.pse.kit.studywithme.model.database.AppDatabase

import de.pse.kit.studywithme.model.network.GroupService
import de.pse.kit.studywithme.model.network.UserService
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.concurrent.atomic.AtomicBoolean

class GroupRepository private constructor(context: Context): GroupRepositoryInterface {
    private val groupService = GroupService.instance
    private val userService = UserService.instance
    private val groupDao = AppDatabase.getInstance(context).groupDao()
    private val auth = Authenticator

    override fun getGroups(search: String): List<Group> {
        if (auth.firebaseUID == null) {
            // TODO: Explicit exception class
            throw Exception("Authentication Error: No local user signed in.")
        }

        return runBlocking {
            val remoteGroups = groupService.getGroups(search)
            return@runBlocking remoteGroups.map {
                val lecture = groupService.getLecture(it.lectureID)
                val major = if (lecture != null) groupService.getMajor(lecture.majorID) else null
                val group = RemoteGroup.toGroup(it, lecture = lecture, major = major)
                return@map group
            }
        }
    }

    override fun getJoinedGroups(): Flow<List<Group>> {
        if (auth.firebaseUID == null) {
            // TODO: Explicit exception class
            throw Exception("Authentication Error: No local user signed in.")
        }

        return channelFlow {
            val truthWasSend = AtomicBoolean(false)

            launch {
                val remoteGroups = groupService.getJoinedGroups(auth.user!!.userID)
                val groups = remoteGroups.map {
                    val lecture = groupService.getLecture(it.lectureID)
                    val major = if (lecture != null) groupService.getMajor(lecture.majorID) else null
                    val group = RemoteGroup.toGroup(it, lecture = lecture, major = major)
                    return@map group
                }
                send (groups)
                truthWasSend.set(true)

                groups.forEach {
                    groupDao.saveGroup(RemoteGroup.toRemoteGroup(it))
                    if (it.lecture != null) groupDao.saveLecture(it.lecture)
                    if (it.major != null) groupDao.saveMajor(it.major)
                }
            }
            launch {
                val localGroups = groupDao.getGroups()
                val groups = localGroups.map {
                    val lecture = groupDao.getLecture(it.lectureID)
                    val major = groupDao.getMajor(lecture.majorID)
                    val group = RemoteGroup.toGroup(it, lecture = lecture, major = major)
                    return@map group
                }
                 if(!truthWasSend.get()) send(groups)
            }
        }.filterNotNull()
    }

    override fun getGroupSuggestions(): List<Group> {
        if (auth.firebaseUID == null) {
            // TODO: Explicit exception class
            throw Exception("Authentication Error: No local user signed in.")
        }

        return runBlocking {
            val remoteGroups = groupService.getGroupSuggestions(auth.user!!.userID)
            return@runBlocking remoteGroups.map {
                val lecture = groupService.getLecture(it.lectureID)
                val major = if (lecture != null) groupService.getMajor(lecture.majorID) else null
                val group = RemoteGroup.toGroup(it, lecture = lecture, major = major)
                return@map group
            }
        }
    }

    override fun getGroup(groupID: Int): Flow<Group> {
        if (auth.firebaseUID == null) {
            // TODO: Explicit exception class
            throw Exception("Authentication Error: No local user signed in.")
        }

        return channelFlow {
            val truthWasSend = AtomicBoolean(false)

            launch {
                val remoteGroup = groupService.getGroup(groupID)
                if (remoteGroup == null) cancel()

                val lecture = groupService.getLecture(remoteGroup!!.lectureID)
                val major = if (lecture != null) groupService.getMajor(lecture.majorID) else null
                val group = RemoteGroup.toGroup(remoteGroup, lecture = lecture, major = major)
                send(group)
                truthWasSend.set(true)
            }
            launch {
                val localGroup = groupDao.getGroup(groupID)
                val lecture = groupDao.getLecture(localGroup.lectureID)
                val major = groupDao.getMajor(lecture.majorID)
                val group = RemoteGroup.toGroup(localGroup, lecture = lecture, major = major)
                if (!truthWasSend.get()) {
                    send(group)
                }
            }
        }.filterNotNull()
    }

    override fun newGroup(group: Group, newLecture: Boolean): Boolean {
        if (auth.firebaseUID == null) {
            // TODO: Explicit exception class
            throw Exception("Authentication Error: No local user signed in.")
        }
        return runBlocking {
            if (group.lecture == null) {
                return@runBlocking false
            }

            if (newLecture) {
                val remoteLecture = groupService.newLecture(group.lecture)
                if (remoteLecture == null) return@runBlocking false
                group.lectureID = remoteLecture.lectureID
            }

            val remoteGroup = groupService.newGroup(RemoteGroup.toRemoteGroup(group))
            if (remoteGroup != null) {
                Log.d(auth.TAG, "Remote Database Group Post:success")
                groupDao.saveGroup(remoteGroup)
                return@runBlocking true
            } else {
                return@runBlocking false
            }
        }
    }

    override fun editGroup(group: Group, newLecture: Boolean): Boolean {
        if (auth.firebaseUID == null) {
            // TODO: Explicit exception class
            throw Exception("Authentication Error: No local user signed in.")
        }

        return runBlocking {
            if (group.lecture == null) {
                return@runBlocking false
            }

            if (newLecture) {
                val remoteLecture = groupService.newLecture(group.lecture)
                if (remoteLecture == null) return@runBlocking false
                group.lectureID = remoteLecture.lectureID
            }

            val editedGroup = RemoteGroup.toRemoteGroup(group)
            val remoteGroup = groupService.editGroup(group.groupID, editedGroup)
            if (remoteGroup == editedGroup) {
                groupDao.editGroup(editedGroup)
                return@runBlocking true
            } else {
                return@runBlocking false
            }
        }
    }

    override fun exitGroup(groupID: Int, uid: Int){
        if (auth.firebaseUID == null) {
            // TODO: Explicit exception class
            throw Exception("Authentication Error: No local user signed in.")
        }
        runBlocking {
            launch {
                groupDao.removeMember(groupID, uid)
            }
            launch {
                groupService.removeMember(groupID, uid)
            }
        }
    }

    override fun deleteGroup(group: Group){
        if (auth.firebaseUID == null) {
            // TODO: Explicit exception class
            throw Exception("Authentication Error: No local user signed in.")
        }
        runBlocking {
            launch {
                groupDao.removeGroup(RemoteGroup.toRemoteGroup(group))
            }
            launch {
                groupService.removeGroup(group.groupID)
            }
        }
    }

    override fun newMember(groupID: Int, uid: Int): Boolean {
        if (auth.firebaseUID == null) {
            // TODO: Explicit exception class
            throw Exception("Authentication Error: No local user signed in.")
        }

        return runBlocking {
            val remoteGroupMember = groupService.newMember(groupID, uid)
            if (remoteGroupMember != null) {
                Log.d(auth.TAG, "Remote Database Session Post:success")
                groupDao.newMember(remoteGroupMember)
                return@runBlocking true
            } else {
                return@runBlocking false
            }
        }
    }

    override fun joinRequest(groupID: Int) {
        return runBlocking {
            return@runBlocking groupService.joinRequest(groupID)
        }
    }

    override fun removeMember(groupID: Int, uid: Int) {
        if (auth.firebaseUID == null) {
            // TODO: Explicit exception class
            throw Exception("Authentication Error: No local user signed in.")
        }
        runBlocking {
            launch {
                groupDao.removeMember(groupID, uid)
            }
            launch {
                groupService.removeMember(groupID, uid)
            }
        }
    }

    override fun getGroupMembers(groupID: Int): Flow<List<User>> {
        if (auth.firebaseUID == null) {
            // TODO: Explicit exception class
            throw Exception("Authentication Error: No local user signed in.")
        }

        return channelFlow {
            val truthWasSend = AtomicBoolean(false)

            launch {
                val remoteGroupMembers = groupService.getGroupMembers(groupID)
                val groupMembers = remoteGroupMembers?.map {
                    userService.getUser(it.userID)
                }?.filterNotNull()
                send(groupMembers)
                truthWasSend.set(true)
            }
            launch {
                val localGroupMembers = groupDao.getGroupMembers(groupID)
                val groupMembers = localGroupMembers.map {
                    userService.getUser(it.userID)
                }.filterNotNull()

                if (!truthWasSend.get()) {
                    send(groupMembers)
                }
            }
        }.filterNotNull()
    }

    override fun getLectures(prefix: String): Flow<List<Lecture>> {
        if (auth.firebaseUID == null) {
            // TODO: Explicit exception class
            throw Exception("Authentication Error: No local user signed in.")
        }
        return flow {
            val remoteLectures = groupService.getLectures(prefix).filter {
                it.majorID == auth.user!!.majorID
            }
            emit(remoteLectures)
        }.filterNotNull()
    }


    companion object : SingletonHolder<GroupRepository, Context>({ GroupRepository(it) })
}