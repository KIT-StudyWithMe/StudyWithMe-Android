package de.pse.kit.studywithme.model.repository

import android.content.Context
import android.util.Log
import de.pse.kit.studywithme.SingletonHolder
import de.pse.kit.studywithme.model.data.Group
import de.pse.kit.studywithme.model.data.GroupMember
import de.pse.kit.studywithme.model.data.Lecture
import de.pse.kit.studywithme.model.data.User
import de.pse.kit.studywithme.model.database.AppDatabase

import de.pse.kit.studywithme.model.network.GroupService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.concurrent.atomic.AtomicBoolean

class GroupRepository private constructor(context: Context): GroupRepositoryInterface {
    private val groupService = GroupService.instance
    private val groupDao = AppDatabase.getInstance(context).groupDao()
    private val auth = Authenticator

    override fun getGroups(search: String): List<Group> {
        return runBlocking {
            return@runBlocking groupService.getGroups(search)
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
                val remoteGroups = groupService.getJoinedGroups(auth.uid!!)
                send (remoteGroups)
                truthWasSend.set(true)
            }
            launch {
                val localGroups = groupDao.getJoinedGroups(auth.uid!!)
                if (!truthWasSend.get()) {
                    send(localGroups)
                }
            }
        }.filterNotNull()
    }

    override fun getGroupSuggestions(): List<Group> {
        return runBlocking {
            return@runBlocking groupService.getGroupSuggestions(auth.uid!!)
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
                send(remoteGroup)
                truthWasSend.set(true)
            }
            launch {
                val localGroup = groupDao.getGroup(groupID)
                if (!truthWasSend.get()) {
                    send(localGroup)
                }
            }
        }.filterNotNull()
    }

    override fun newGroup(group: Group): Boolean {
        if (auth.firebaseUID == null) {
            // TODO: Explicit exception class
            throw Exception("Authentication Error: No local user signed in.")
        }
        return runBlocking {
            val remoteGroup = groupService.newGroup(group)
            if (remoteGroup != null) {
                Log.d(auth.TAG, "Remote Database Group Post:success")
                groupDao.saveGroup(group)
                return@runBlocking true
            } else {
                return@runBlocking false
            }
        }
    }

    override fun editGroup(group: Group): Boolean {
        if (auth.firebaseUID == null) {
            // TODO: Explicit exception class
            throw Exception("Authentication Error: No local user signed in.")
        }

        return runBlocking {
            val remoteGroup = groupService.getGroup(group.groupID)
            if (remoteGroup == group) {
                groupDao.editGroup(group)
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
                groupDao.removeGroup(group)
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
            val remoteGroup = groupService.newMember(groupID, uid)
            if (remoteGroup != null) {
                Log.d(auth.TAG, "Remote Database Session Post:success")
                groupDao.newMember(GroupMember(groupID, uid, false, true))
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
                send(remoteGroupMembers)
                truthWasSend.set(true)
            }
            launch {
                val localGroupMembers = groupDao.getGroupMembers(groupID)
                if (!truthWasSend.get()) {
                    send(localGroupMembers)
                }
            }
        }.filterNotNull()
    }

    override fun getLectures(prefix: String): Flow<List<Lecture>> {
        if (auth.firebaseUID == null) {
            // TODO: Explicit exception class
            throw Exception("Authentication Error: No local user signed in.")
        }
        return channelFlow {
            val truthWasSend = AtomicBoolean(false)

            launch {
                val remoteLectures = groupService.getLectures(prefix)
                send(remoteLectures)
                truthWasSend.set(true)
            }
            launch {
                val localLectures = groupDao.getLectures(prefix)
                if (!truthWasSend.get()) {
                    send(localLectures)
                }
            }
        }.filterNotNull()
    }


    companion object : SingletonHolder<GroupRepository, Context>({ GroupRepository(it) })
}