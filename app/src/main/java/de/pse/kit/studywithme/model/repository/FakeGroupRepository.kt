package de.pse.kit.studywithme.model.repository

import android.content.Context
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.ui.platform.LocalContext
import androidx.room.Room
import de.pse.kit.studywithme.model.data.*
import de.pse.kit.studywithme.model.database.AppDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.streams.toList

class FakeGroupRepository() : GroupRepositoryInterface {
    private var groups = mutableListOf(
        Group(
            groupID = 1,
            name = "Die lahmen Coder",
            lectureID = 0,
            lecture = Lecture(
                lectureID = 0,
                lectureName = "Programmieren",
                majorID = 0
            ),
            major = Major(
                majorID = 0,
                name = "Informatik"
            ),
            description = "Cool bleiben.",
            sessionFrequency = SessionFrequency.MONTHLY,
            sessionType = SessionType.ONLINE,
            lectureChapter = 1,
            exercise = 1,
        ),
        Group(
            groupID = 2,
            name = "Die sehr lahmen Coder",
            lectureID = 0,
            lecture = Lecture(
                lectureID = 0,
                lectureName = "Lineare Algebra II",
                majorID = 0
            ),
            major = Major(
                majorID = 1,
                name = "Mathematik"
            ),
            description = "Cool bleiben.",
            sessionFrequency = SessionFrequency.MONTHLY,
            sessionType = SessionType.ONLINE,
            lectureChapter = 1,
            exercise = 1,
        ),
        Group(
            groupID = 0,
            name = "Die wilden Coder",
            lectureID = 0,
            lecture = Lecture(
                lectureID = 0,
                lectureName = "Programmieren",
                majorID = 0
            ),
            major = Major(
                majorID = 0,
                name = "Informatik"
            ),
            description = "wir sind die coolsten wenn wir cruisen, wenn wir durch die city cruisen. djfg djhgjfngjfngjnfjngjfngjnfjgnfjngjfngjnfjnj",
            sessionFrequency = SessionFrequency.WEEKLY,
            sessionType = SessionType.HYBRID,
            lectureChapter = 8,
            exercise = 8,
        )
    )

    private var groupMembers = listOf(
        GroupMember(
            groupID = 2,
            userID = 1,
            name = "max.fake",
            isAdmin = true
        ),
        GroupMember(
            groupID = 0,
            userID = 1,
            name = "max.fake",
            isAdmin = false
        ),
        GroupMember(
            groupID = 0,
            userID = 0,
            name = "max.mustermann",
            isAdmin = true
        ),
        GroupMember(
            groupID = 1,
            userID = 0,
            name = "max.mustermann",
            isAdmin = true
        ),
        GroupMember(
            groupID = 2,
            userID = 0,
            name = "max.mustermann",
            isAdmin = false
        )

    )


    override fun getGroups(search: String): List<Group> {
        return groups.filter {
            val match = Regex(search).find(it.name)?.range?.start
            match == 0
        }
    }

    override fun getJoinedGroups(): Flow<List<Group>> {
        return flow { emit(groups) }
    }

    override fun getGroupSuggestions(): List<Group> {
        return groups
    }

    override fun getGroup(groupID: Int): Flow<Group> {
        return flow {
            emit(groups.filter {
                it.groupID == groupID
            }.get(0))
        }
    }

    override fun newGroup(group: Group): Boolean {
        groups.add(group)
        return true
    }

    override fun editGroup(group: Group): Boolean {
        groups.minus(groups.filter {
            it.groupID == group.groupID
        }.get(0))
        groups.add(group)
        return true
    }

    override fun exitGroup(groupID: Int, uid: Int) {
    }

    override fun deleteGroup(group: Group): Boolean {
        return groups.remove(groups.filter {
            it.groupID == group.groupID
        }.get(0))
    }

    override fun hideGroup(groupID: Int): Boolean {
        TODO("Not yet implemented")
    }

    override fun newMember(groupID: Int, uid: Int): Boolean {
        TODO("Not yet implemented")
    }

    override fun joinRequest(groupID: Int): Boolean {
        TODO("Not yet implemented")
    }

    override fun getJoinRequests(groupID: Int): List<UserLight> {
        TODO("Not yet implemented")
    }

    override fun removeMember(groupID: Int, uid: Int) {
        TODO("Not yet implemented")
    }

    override fun getGroupMembers(groupID: Int): Flow<List<GroupMember>> {
        return flow {
            emit(groupMembers.filter {
                it.groupID == groupID
            })
        }
    }
    override fun getGroupAdmins(groupID: Int): Flow<List<GroupMember>> {
        return flow {
            emit(groupMembers.filter {
                it.isAdmin
                it.groupID == groupID
            })
        }
    }

    override fun isSignedInUserAdmin(groupID: Int): Flow<Boolean> {
        return flow {
            if (groupID == 2) {
                emit(false)
            } else {
                emit(true)
            }
        }
    }

    override fun getLectures(prefix: String): Flow<List<Lecture>> {
        TODO("Not yet implemented")
    }

    override fun getLecture(name: String): Lecture? {
        TODO("Not yet implemented")
    }
}