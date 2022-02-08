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
    private var groups = listOf(
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
            hidden = false
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
            hidden = false
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
            description = "",
            sessionFrequency = SessionFrequency.WEEKLY,
            sessionType = SessionType.HYBRID,
            lectureChapter = 8,
            exercise = 8,
            hidden = false
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

    override fun newGroup(group: Group, newLecture: Boolean): Boolean {
        groups.plus(group)
        return true
    }

    override fun editGroup(group: Group, newLecture: Boolean): Boolean {
        groups.minus(groups.filter {
            it.groupID == group.groupID
        }.get(0))
        groups.plus(group)
        return true
    }

    override fun exitGroup(groupID: Int, uid: Int) {
    }

    override fun deleteGroup(group: Group) {
        groups.minus(groups.filter {
            it.groupID == group.groupID
        }.get(0))
    }

    override fun newMember(groupID: Int, uid: Int): Boolean {
        TODO("Not yet implemented")
    }

    override fun joinRequest(groupID: Int) {
        TODO("Not yet implemented")
    }

    override fun removeMember(groupID: Int, uid: Int) {
        TODO("Not yet implemented")
    }

    override fun getGroupMembers(groupID: Int): Flow<List<User>> {
        TODO("Not yet implemented")
    }

    override fun getLectures(prefix: String): Flow<List<Lecture>> {
        TODO("Not yet implemented")
    }
}