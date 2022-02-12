package de.pse.kit.studywithme.model.repository

import android.content.Context
import android.util.Log
import de.pse.kit.studywithme.SingletonHolder
import de.pse.kit.studywithme.model.data.*
import de.pse.kit.studywithme.model.database.AppDatabase

import de.pse.kit.studywithme.model.network.GroupService
import de.pse.kit.studywithme.model.network.ReportService
import de.pse.kit.studywithme.model.network.UserService
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Group repository
 *
 * @constructor
 *
 * @param context
 */
class GroupRepository private constructor(context: Context) : GroupRepositoryInterface {
    private val groupService = GroupService.instance
    private val reportService = ReportService.instance
    private val groupDao = AppDatabase.getInstance(context).groupDao()
    private val auth = Authenticator

    override fun getGroups(search: String): List<Group> {
        if (auth.firebaseUID == null) {
            // TODO: Explicit exception class
            throw Exception("Authentication Error: No local user signed in.")
        }

        return runBlocking {
            val remoteGroups = async { groupService.getGroups(search) }
            val remoteJoinedGroups = async { groupService.getJoinedGroups(auth.user!!.userID) }

            val filteredGroups = remoteGroups.await()?.filter {
                remoteJoinedGroups.await()?.map {
                    it.groupID
                }?.contains(it.groupID) == false
            }?.map {
                val lecture = groupService.getLecture(it.lectureID)
                val major = if (lecture != null) groupService.getMajor(lecture.majorID) else null
                val group = RemoteGroup.toGroup(it, lecture = lecture, major = major)
                return@map group
            }

            return@runBlocking filteredGroups ?: emptyList()
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
                if (remoteGroups != null) {
                    val groups = remoteGroups.map {
                        val lecture = groupService.getLecture(it.lectureID)
                        val major =
                            if (lecture != null) groupService.getMajor(lecture.majorID) else null
                        val group = RemoteGroup.toGroup(it, lecture = lecture, major = major)
                        return@map group
                    }
                    send(groups)
                    truthWasSend.set(true)
                    groups.forEach {
                        groupDao.saveGroup(RemoteGroup.toRemoteGroup(it))
                        if (it.lecture != null) groupDao.saveLecture(it.lecture)
                        if (it.major != null) groupDao.saveMajor(it.major)
                    }
                }
            }
            launch {
                val localGroups = groupDao.getGroups()
                val groups = localGroups?.map {
                    val lecture = groupDao.getLecture(it.lectureID)
                    val major =
                        if (lecture != null) groupDao.getMajor(lecture.majorID) else null
                    val group = RemoteGroup.toGroup(it, lecture = lecture, major = major)
                    return@map group
                }
                if (!truthWasSend.get()) send(groups)
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
                ?: return@runBlocking emptyList()
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
                if (remoteGroup == null) return@launch

                val lecture = groupService.getLecture(remoteGroup.lectureID)
                val major = if (lecture != null) groupService.getMajor(lecture.majorID) else null
                val group = RemoteGroup.toGroup(remoteGroup, lecture = lecture, major = major)
                send(group)
                truthWasSend.set(true)
            }
            launch {
                val localGroup = groupDao.getGroup(groupID)
                if (localGroup == null) return@launch

                val lecture = groupDao.getLecture(localGroup.lectureID)
                val major = if (lecture != null) groupService.getMajor(lecture.majorID) else null
                val group = RemoteGroup.toGroup(localGroup, lecture = lecture, major = major)
                if (!truthWasSend.get()) {
                    send(group)
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
            if (group.lecture == null) {
                return@runBlocking false
            }

            val remoteLecture = getLecture(group.lecture.lectureName)
                ?: return@runBlocking false
            group.lectureID = remoteLecture.lectureID

            val remoteGroup =
                groupService.newGroup(RemoteGroup.toRemoteGroup(group), auth.user!!.userID)
            if (remoteGroup != null) {
                Log.d(auth.TAG, "Remote Database Group Post:success")
                groupDao.saveGroup(remoteGroup)
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
            if (group.lecture == null) {
                return@runBlocking false
            }

            val remoteLecture = getLecture(group.lecture.lectureName)
                ?: return@runBlocking false
            group.lectureID = remoteLecture.lectureID

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

    override fun exitGroup(groupID: Int, uid: Int) {
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

    override fun deleteGroup(group: Group): Boolean {
        if (auth.firebaseUID == null) {
            // TODO: Explicit exception class
            throw Exception("Authentication Error: No local user signed in.")
        }
        return runBlocking {
            launch {
                groupDao.removeGroup(RemoteGroup.toRemoteGroup(group))
            }
            return@runBlocking groupService.removeGroup(group.groupID)
        }
    }

    override fun hideGroup(groupID: Int, hidden: Boolean): Boolean {
        if (auth.firebaseUID == null) {
            // TODO: Explicit exception class
            throw Exception("Authentication Error: No local user signed in.")
        }
        return runBlocking {
            return@runBlocking groupService.hideGroup(groupID, hidden)
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
                Log.d(auth.TAG, "Remote Database Member Post:success")
                groupDao.newMember(remoteGroupMember)
                return@runBlocking true
            } else {
                return@runBlocking false
            }
        }
    }

    override fun declineMember(groupID: Int, uid: Int): Boolean {
        if (auth.firebaseUID == null) {
            // TODO: Explicit exception class
            throw Exception("Authentication Error: No local user signed in.")
        }

        return runBlocking {
            return@runBlocking groupService.declineMember(groupID, uid)
        }
    }

    override fun joinRequest(groupID: Int): Boolean {
        if (auth.firebaseUID == null) {
            // TODO: Explicit exception class
            throw Exception("Authentication Error: No local user signed in.")
        }
        return runBlocking {
            return@runBlocking groupService.joinRequest(groupID, auth.user!!.userID)
        }
    }

    override fun getJoinRequests(groupID: Int): List<UserLight> {
        if (auth.firebaseUID == null) {
            // TODO: Explicit exception class
            throw Exception("Authentication Error: No local user signed in.")
        }

        return runBlocking {
            return@runBlocking groupService.getJoinRequests(groupID) ?: emptyList()
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

    override fun leaveGroup(groupID: Int) {
        if (auth.firebaseUID == null) {
            // TODO: Explicit exception class
            throw Exception("Authentication Error: No local user signed in.")
        }

        removeMember(groupID, auth.user!!.userID)
    }

    override fun getGroupMembers(groupID: Int): Flow<List<GroupMember>> {
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

    override fun getGroupAdmins(groupID: Int): Flow<List<GroupMember>> {
        if (auth.firebaseUID == null) {
            // TODO: Explicit exception class
            throw Exception("Authentication Error: No local user signed in.")
        }

        return channelFlow {
            val truthWasSend = AtomicBoolean(false)

            launch {
                val remoteGroupMembers = groupService.getGroupMembers(groupID)
                send(remoteGroupMembers?.filter {
                    it.isAdmin
                })
                truthWasSend.set(true)
            }
            launch {
                val localGroupMembers = groupDao.getGroupMembers(groupID)
                if (!truthWasSend.get()) {
                    send(localGroupMembers?.filter {
                        it.isAdmin
                    })
                }
            }
        }.filterNotNull()
    }

    override fun isSignedInUserAdmin(groupID: Int): Flow<Boolean> {
        if (auth.firebaseUID == null) {
            // TODO: Explicit exception class
            throw Exception("Authentication Error: No local user signed in.")
        }

        return flow {
            getGroupAdmins(groupID).collect {
                val admin = it.map {
                    it.groupID
                }.contains(groupID)
                emit(admin)
            }
        }
    }

    override fun hasSignedInUserJoinRequested(groupID: Int): Boolean {
        if (auth.firebaseUID == null) {
            // TODO: Explicit exception class
            throw Exception("Authentication Error: No local user signed in.")
        }
        val userIDs = getJoinRequests(groupID).map {
            it.userID
        }
        if (userIDs.contains(auth.user!!.userID.toLong())) {
            return true
        }
        return false
    }


override fun getLectures(prefix: String): Flow<List<Lecture>> {
    if (auth.firebaseUID == null) {
        // TODO: Explicit exception class
        throw Exception("Authentication Error: No local user signed in.")
    }
    return flow {
        var remoteLectures = groupService.getLectures(majorID = auth.user!!.majorID!!, prefix)
        if (remoteLectures != null) {
            remoteLectures = remoteLectures.filter {
                it.majorID == auth.user!!.majorID
            }
            emit(remoteLectures)
        }
    }.filterNotNull()
}

override fun getLecture(name: String): Lecture? {
    return runBlocking {
        val lectures = groupService.getLectures(majorID = auth.user!!.majorID!!, name)
        if (lectures?.map {
                it.lectureName
            }?.contains(name) != true) {
            return@runBlocking groupService.newLecture(
                Lecture(
                    -1,
                    name,
                    majorID = auth.user!!.majorID!!
                ), auth.user!!.majorID!!
            )
        } else {
            return@runBlocking lectures.last {
                it.lectureName == name
            }
        }
    }
}

override fun reportGroup(groupID: Int, groupField: GroupField) {
    if (auth.firebaseUID == null) {
        // TODO: Explicit exception class
        throw Exception("Authentication Error: No local user signed in.")
    }
    return runBlocking { reportService.reportGroup(groupID, groupField, auth.user!!.userID) }
}

override fun reportUser(userID: Int, userField: UserField) {
    if (auth.firebaseUID == null) {
        // TODO: Explicit exception class
        throw Exception("Authentication Error: No local user signed in.")
    }
    return runBlocking {
        reportService.reportUser(userID, userField, auth.user!!.userID)
    }
}


companion object : SingletonHolder<GroupRepository, Context>({ GroupRepository(it) })
}