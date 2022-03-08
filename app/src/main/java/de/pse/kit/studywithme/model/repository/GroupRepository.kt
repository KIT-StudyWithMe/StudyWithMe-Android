package de.pse.kit.studywithme.model.repository

import android.content.Context
import android.util.Log
import de.pse.kit.studywithme.SingletonHolder
import de.pse.kit.studywithme.model.data.*
import de.pse.kit.studywithme.model.database.AppDatabase
import de.pse.kit.studywithme.model.network.*

import io.ktor.client.engine.android.*
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Group repository
 *
 * @constructor
 *
 * @param context
 */
class GroupRepository private constructor(context: Context) : GroupRepositoryInterface {
    private val groupDao = AppDatabase.getInstance(context).groupDao()
    private val auth = Authenticator
    private val reportService = ReportService.getInstance(Pair(Android.create()) { auth.getToken() })
    private val groupService = GroupService.getInstance(Pair(Android.create()) { auth.getToken() })

    override suspend fun getGroups(search: String): List<Group> = coroutineScope {
        if (auth.firebaseUID == null) {
            // TODO: Explicit exception class
            throw Exception("Authentication Error: No local user signed in.")
        }

        if (search.matches("\\s+".toRegex()) || search == "") {
            Log.d("REPO", "WHITE")
            return@coroutineScope emptyList()
        }

        val remoteGroups = async { groupService.getGroups(search) }
        val remoteJoinedGroups = async { groupService.getJoinedGroups(auth.user!!.userID) }

        val filteredGroups = remoteGroups.await()?.filter {
            remoteJoinedGroups.await()?.map {
                it.groupID
            }?.contains(it.groupID) == false
        }?.map {
            val lecture = groupService.getLecture(it.lectureID)
            val major = if (lecture != null) groupService.getMajor(lecture.majorID) else null
            return@map RemoteGroup.toGroup(it, lecture = lecture, major = major)
        }

        return@coroutineScope filteredGroups ?: emptyList()
    }

    override suspend fun getJoinedGroups(): Flow<List<Group>> = channelFlow {
        if (auth.firebaseUID == null) {
            // TODO: Explicit exception class
            throw Exception("Authentication Error: No local user signed in.")
        }
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

    override suspend fun getGroupSuggestions(): List<Group> {
        if (auth.firebaseUID == null) {
            // TODO: Explicit exception class
            throw Exception("Authentication Error: No local user signed in.")
        }

        val remoteGroups = groupService.getGroupSuggestions(auth.user!!.userID)
            ?: return emptyList()

        return remoteGroups.map {
            val lecture = groupService.getLecture(it.lectureID)
            val major = if (lecture != null) groupService.getMajor(lecture.majorID) else null
            val group = RemoteGroup.toGroup(it, lecture = lecture, major = major)
            return@map group
        }
    }

    override suspend fun getGroup(groupID: Int): Flow<Group> = channelFlow {
        if (auth.firebaseUID == null) {
            // TODO: Explicit exception class
            throw Exception("Authentication Error: No local user signed in.")
        }
        val truthWasSend = AtomicBoolean(false)

        launch {
            val remoteGroup = groupService.getGroup(groupID) ?: return@launch

            val lecture = groupService.getLecture(remoteGroup.lectureID)
            val major = if (lecture != null) groupService.getMajor(lecture.majorID) else null
            val group = RemoteGroup.toGroup(remoteGroup, lecture = lecture, major = major)
            send(group)
            truthWasSend.set(true)
        }
        launch {
            val localGroup = groupDao.getGroup(groupID) ?: return@launch

            val lecture = groupDao.getLecture(localGroup.lectureID)
            val major = if (lecture != null) groupService.getMajor(lecture.majorID) else null
            val group = RemoteGroup.toGroup(localGroup, lecture = lecture, major = major)
            if (!truthWasSend.get()) {
                send(group)
            }
        }
    }.filterNotNull()

    override suspend fun isGroupHidden(groupID: Int): Boolean {
        return groupService.isGroupHidden(groupID) ?: false
    }

    override suspend fun newGroup(group: Group): Boolean {
        if (auth.firebaseUID == null) {
            // TODO: Explicit exception class
            throw Exception("Authentication Error: No local user signed in.")
        }
        if (group.lecture == null) {
            return false
        }

        val remoteLecture = getLecture(group.lecture.lectureName) ?: return false
        group.lectureID = remoteLecture.lectureID

        val remoteGroup =
            groupService.newGroup(RemoteGroup.toRemoteGroup(group), auth.user!!.userID)
        if (remoteGroup != null) {
            Log.d(auth.TAG, "Remote Database Group Post:success")
            groupDao.saveGroup(remoteGroup)
            return true
        } else {
            return false
        }
    }

    override suspend fun editGroup(group: Group): Boolean {
        if (auth.firebaseUID == null) {
            // TODO: Explicit exception class
            throw Exception("Authentication Error: No local user signed in.")
        }

        if (group.lecture == null) {
            return false
        }

        val remoteLecture = getLecture(group.lecture.lectureName) ?: return false
        group.lectureID = remoteLecture.lectureID

        val editedGroup = RemoteGroup.toRemoteGroup(group)
        val remoteGroup = groupService.editGroup(group.groupID, editedGroup)
        if (remoteGroup == editedGroup) {
            groupDao.editGroup(editedGroup)
            return true
        } else {
            return false
        }
    }

    override suspend fun exitGroup(groupID: Int, uid: Int): Unit = coroutineScope {
        if (auth.firebaseUID == null) {
            // TODO: Explicit exception class
            throw Exception("Authentication Error: No local user signed in.")
        }

        launch {
            groupDao.removeMember(groupID, uid)
        }
        launch {
            groupService.removeMember(groupID, uid)
        }
    }

    override suspend fun deleteGroup(group: Group): Boolean = coroutineScope {
        if (auth.firebaseUID == null) {
            // TODO: Explicit exception class
            throw Exception("Authentication Error: No local user signed in.")
        }
        launch {
            groupDao.removeGroup(RemoteGroup.toRemoteGroup(group))
        }
        return@coroutineScope groupService.removeGroup(group.groupID)
    }

    override suspend fun hideGroup(groupID: Int): Boolean {
        if (auth.firebaseUID == null) {
            // TODO: Explicit exception class
            throw Exception("Authentication Error: No local user signed in.")
        }
        return groupService.hideGroup(groupID)
    }

    override suspend fun newMember(groupID: Int, uid: Int): Boolean {
        if (auth.firebaseUID == null) {
            // TODO: Explicit exception class
            throw Exception("Authentication Error: No local user signed in.")
        }
        val remoteGroupMember = groupService.newMember(groupID, uid)
        if (remoteGroupMember != null) {
            Log.d(auth.TAG, "Remote Database Member Post:success")
            groupDao.newMember(remoteGroupMember)
            return true
        } else {
            return false
        }
    }

    override suspend fun declineMember(groupID: Int, uid: Int): Boolean {
        if (auth.firebaseUID == null) {
            // TODO: Explicit exception class
            throw Exception("Authentication Error: No local user signed in.")
        }
        return groupService.declineMember(groupID, uid)
    }

    override suspend fun joinRequest(groupID: Int): Boolean {
        if (auth.firebaseUID == null) {
            // TODO: Explicit exception class
            throw Exception("Authentication Error: No local user signed in.")
        }
        return groupService.joinRequest(groupID, auth.user!!.userID)
    }

    override suspend fun getJoinRequests(groupID: Int): List<UserLight> {
        if (auth.firebaseUID == null) {
            // TODO: Explicit exception class
            throw Exception("Authentication Error: No local user signed in.")
        }
        return groupService.getJoinRequests(groupID) ?: emptyList()
    }

    override suspend fun removeMember(groupID: Int, uid: Int): Unit = coroutineScope {
        if (auth.firebaseUID == null) {
            // TODO: Explicit exception class
            throw Exception("Authentication Error: No local user signed in.")
        }
        launch {
            groupDao.removeMember(groupID, uid)
        }
        launch {
            groupService.removeMember(groupID, uid)
        }
    }

    override suspend fun leaveGroup(groupID: Int) {
        if (auth.firebaseUID == null) {
            // TODO: Explicit exception class
            throw Exception("Authentication Error: No local user signed in.")
        }

        removeMember(groupID, auth.user!!.userID)
    }

    override suspend fun getGroupMembers(groupID: Int): Flow<List<GroupMember>> = channelFlow {
        if (auth.firebaseUID == null) {
            // TODO: Explicit exception class
            throw Exception("Authentication Error: No local user signed in.")
        }
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

    override suspend fun getGroupAdmins(groupID: Int): Flow<List<GroupMember>> = channelFlow {
        if (auth.firebaseUID == null) {
            // TODO: Explicit exception class
            throw Exception("Authentication Error: No local user signed in.")
        }
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

    override suspend fun isSignedInUserAdmin(groupID: Int): Flow<Boolean> = flow {
        if (auth.firebaseUID == null) {
            // TODO: Explicit exception class
            throw Exception("Authentication Error: No local user signed in.")
        }
        getGroupAdmins(groupID).collect {
            val admin = it.map {
                it.groupID
            }.contains(groupID)
            emit(admin)
        }
    }

    override suspend fun hasSignedInUserJoinRequested(groupID: Int): Boolean {
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


    override suspend fun getLectures(prefix: String): Flow<List<Lecture>> = flow {
        if (auth.firebaseUID == null) {
            // TODO: Explicit exception class
            throw Exception("Authentication Error: No local user signed in.")
        }
        var remoteLectures = groupService.getLectures(majorID = auth.user!!.majorID!!, prefix)
        if (remoteLectures != null) {
            remoteLectures = remoteLectures.filter {
                it.majorID == auth.user!!.majorID
            }
            emit(remoteLectures)
        }
    }.filterNotNull()

    override suspend fun getLecture(name: String): Lecture? {
        val lectures = groupService.getLectures(majorID = auth.user!!.majorID!!, name)
        if (lectures?.map {
                it.lectureName
            }?.contains(name) != true) {
            return groupService.newLecture(
                Lecture(
                    -1,
                    name,
                    majorID = auth.user!!.majorID!!
                ), auth.user!!.majorID!!
            )
        } else {
            return lectures.last {
                it.lectureName == name
            }
        }
    }

    override suspend fun reportGroup(groupID: Int, groupField: GroupField): Unit {
        if (auth.firebaseUID == null) {
            // TODO: Explicit exception class
            throw Exception("Authentication Error: No local user signed in.")
        }
        return reportService.reportGroup(groupID, groupField, auth.user!!.userID)
    }

    override suspend fun reportUser(userID: Int, userField: UserField): Unit {
        if (auth.firebaseUID == null) {
            // TODO: Explicit exception class
            throw Exception("Authentication Error: No local user signed in.")
        }
        return reportService.reportUser(userID, userField, auth.user!!.userID)
    }


    companion object : SingletonHolder<GroupRepository, Context>({ GroupRepository(it) })
}