package de.pse.kit.studywithme.repository

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import de.pse.kit.studywithme.model.auth.FakeAuthenticator
import de.pse.kit.studywithme.model.data.*
import de.pse.kit.studywithme.model.database.AppDatabase
import de.pse.kit.studywithme.model.network.GroupService
import de.pse.kit.studywithme.model.network.HttpRoutes
import de.pse.kit.studywithme.model.network.ReportService
import de.pse.kit.studywithme.model.repository.GroupRepoConstructor
import de.pse.kit.studywithme.model.repository.GroupRepository

import io.ktor.client.engine.mock.*
import io.ktor.http.*
import io.ktor.utils.io.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Before
import org.junit.Test

class GroupRepositoryTest {
    lateinit var repo: GroupRepository
    lateinit var auth: FakeAuthenticator

    @Before
    fun setUp() {
        auth = FakeAuthenticator()
        val context: Context = ApplicationProvider.getApplicationContext()
        val db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        val dao = db.groupDao()

        val engine = MockEngine {
            Log.d("MOCK ENGINE", "${it.method}: ${it.url}")

            if (it.url.toString() == "${HttpRoutes.GROUPS}0/users/0"
                && it.method == HttpMethod.Delete
            ) {
                Log.d("MOCK ENGINE", "DELETE member response: ")

                respond(
                    content = "",
                    status = HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                )
            } else if (it.url.toString() == "${HttpRoutes.GROUPS}0/users/0/membership"
                && it.method == HttpMethod.Put
            ) {
                val member = GroupMember(groupID = 0, userID = 0, isAdmin = false, name = "test")
                Log.d("MOCK ENGINE", "Change member response: member")

                respond(
                    content = Json.encodeToString(member),
                    status = HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                )
            } else if (it.url.toString() == "${HttpRoutes.GROUPS}suggestion/0"
                && it.method == HttpMethod.Get
            ) {
                val groups = listOf(
                    RemoteGroup(
                        groupID = 0,
                        name = "Gruppe",
                        lectureID = 0,
                        description = "desc",
                        sessionType = SessionType.PRESENCE,
                        sessionFrequency = SessionFrequency.WEEKLY,
                        lectureChapter = 1,
                        exercise = 1,
                        memberCount = 1
                    )
                )

                Log.d("MOCK ENGINE", "Group suggestions response: $groups")

                respond(
                    content = Json.encodeToString(groups),
                    status = HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                )
            } else if (it.url.toString() == "${HttpRoutes.USERS}0/groups"
                && it.method == HttpMethod.Get
            ) {
                val groups = emptyList<RemoteGroup>()
                Log.d("MOCK ENGINE", "Joined Groups response: $groups")

                respond(
                    content = Json.encodeToString(groups),
                    status = HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                )
            } else if (it.url.toString() == "${HttpRoutes.LECTURES}0"
                && it.method == HttpMethod.Get
            ) {
                val lecture = Lecture(lectureID = 0, lectureName = "Lineare Algebra I", majorID = 0)

                Log.d("MOCK ENGINE", "Lecture 0 response: $lecture")

                respond(
                    content = Json.encodeToString(lecture),
                    status = HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                )
            } else if (it.url.toString() == "${HttpRoutes.MAJORS}0/lectures?name=Lineare+Algebra"
                && it.method == HttpMethod.Get
            ) {
                val lectures = listOf(
                    Lecture(lectureID = 0, lectureName = "Lineare Algebra I", majorID = 0),
                    Lecture(lectureID = 1, lectureName = "Lineare Algebra II", majorID = 0)
                )

                Log.d("MOCK ENGINE", "Lectures response: $lectures")

                respond(
                    content = Json.encodeToString(lectures),
                    status = HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                )
            } else if (it.url.toString() == "${HttpRoutes.MAJORS}0"
                && it.method == HttpMethod.Get
            ) {
                val major = Major(majorID = 0, name = "Info")

                Log.d("MOCK ENGINE", "Major 0 response: $major")

                respond(
                    content = Json.encodeToString(major),
                    status = HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                )
            } else {
                assert(false) {
                    "GroupRepository calls false service method. Service requests -> ${it.method}: ${it.url}"
                }
                respond(
                    content = "",
                    status = HttpStatusCode.BadRequest,
                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                )
            }
        }

        val groupService = GroupService.newInstance(engine) { "" }
        val reportService = ReportService.newInstance(engine) { "" }
        repo = GroupRepository.newInstance(
            GroupRepoConstructor(
                context = context,
                groupDao = dao,
                groupService = groupService,
                reportService = reportService,
                auth = auth
            )
        )
    }

    @Test
    fun newMember(): Unit = runBlocking {
        assert(repo.newMember(0, 0))
    }

    @Test
    fun leaveGroup(): Unit = runBlocking {
        repo.leaveGroup(0)
    }

    @Test
    fun getGroupSuggestion(): Unit = runBlocking {
        repo.getGroupSuggestions()
            .forEach { assert(it.major?.majorID?.toInt() == auth.user?.majorID) }
    }

    @Test
    fun declineMember(): Unit = runBlocking {
        assert(repo.declineMember(0, 0))
    }

    @Test
    fun getLectures(): Unit = runBlocking {
        val prefix = "Lineare Algebra"
        repo.getLectures(prefix).collect {
            it.forEach { assert(it.lectureName.startsWith(prefix)) }
        }
    }

    @Test
    fun exitGroup(): Unit = runBlocking {
        repo.exitGroup(0, 0)
    }
}