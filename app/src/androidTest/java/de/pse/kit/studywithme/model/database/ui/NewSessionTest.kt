package de.pse.kit.studywithme.model.database.ui

import android.content.Context
import android.util.Log
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import de.pse.kit.studywithme.model.auth.FakeAuthenticator
import de.pse.kit.studywithme.model.data.*
import de.pse.kit.studywithme.model.database.AppDatabase
import de.pse.kit.studywithme.model.database.GroupDao
import de.pse.kit.studywithme.model.database.SessionDao
import de.pse.kit.studywithme.model.database.UserDao
import de.pse.kit.studywithme.model.network.*
import de.pse.kit.studywithme.model.repository.*
import de.pse.kit.studywithme.ui.view.group.JoinedGroupDetailsView
import de.pse.kit.studywithme.ui.view.group.JoinedGroupsView
import de.pse.kit.studywithme.ui.view.navigation.MainView
import io.ktor.client.engine.mock.*
import io.ktor.http.*
import io.ktor.utils.io.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.BeforeAll
import java.util.*

@ExperimentalCoroutinesApi
@ExperimentalMaterial3Api
@ExperimentalMaterialApi
class NewSessionTest {
    private lateinit var context: Context
    private lateinit var userDao: UserDao
    private lateinit var groupDao: GroupDao
    private lateinit var sessionDao: SessionDao
    private lateinit var groupRepo: GroupRepository
    private lateinit var userRepo: UserRepository
    private lateinit var sessionRepo: SessionRepository
    private lateinit var db: AppDatabase
    private lateinit var auth: FakeAuthenticator
    private lateinit var mockEngine: MockEngine
    private val mockUsers = listOf(
        User(
            userID = 0,
            name = "max.mustermann",
            contact = "max.mustermann@mustermail.com",
            college = "Karlsruher Institut für Technologie",
            collegeID = 0,
            major = "Informatik B.Sc.",
            majorID = 0,
            firebaseUID = "dfg46thrge7fnd"
        )
    )
    private val mockLightUsers = listOf(
        UserLight(
            userID = 0,
            name = "max.mustermann"
        )
    )
    private val signedInUser = mockUsers.filter { it.userID == 0 }[0]
    private val mockSessions = emptyList<Session>()
    private val mockGroupMembers = listOf(
        GroupMember(
            groupID = 0,
            userID = 0,
            name = "max.mustermann",
            isAdmin = true
        )
    )
    private val mockLectures: List<Lecture> = listOf(
        Lecture(
            lectureID = 0,
            lectureName = "Lineare Algebra",
            majorID = 0
        )
    )
    private val mockMajors: List<Major> = listOf(
        Major(
            majorID = 0,
            name = "Informatik"
        )
    )
    private val mockRemoteGroup = listOf(
        RemoteGroup(
            groupID = 0,
            name = "gfg",
            lectureID = 0,
            description = "lol",
            lectureChapter = 1,
            exercise = 1,
            memberCount = 1,
            sessionFrequency = SessionFrequency.MONTHLY,
            sessionType = SessionType.ONLINE
        )
    )

    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun initLocalDatabase() {
        runBlocking {
            context = ApplicationProvider.getApplicationContext()
            auth = FakeAuthenticator()
            mockEngine = MockEngine {
                Log.d("MOCK ENGINE", "${it.method}: ${it.url}")
                when (it.method) {
                    HttpMethod.Get ->
                        when (it.url.toString()) {
                            "${HttpRoutes.USERS}?FUID=dfg46thrge7fnd" -> {
                                val responseUser = mockLightUsers.filter { it.userID.toInt() == 0 }
                                Log.d("MOCK", "response user $responseUser")
                                respond(
                                    content = Json.encodeToString(mockLightUsers.filter { it.userID.toInt() == 0 }),
                                    status = HttpStatusCode.OK,
                                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                                )
                            }

                            "${HttpRoutes.USERS}0/detail" -> {
                                Log.d("MOCK", "response user $signedInUser")
                                respond(
                                    content = Json.encodeToString(signedInUser),
                                    status = HttpStatusCode.OK,
                                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                                )
                            }

                            "${HttpRoutes.USERS}0/groups" -> {
                                val groups = mockRemoteGroup.filter { it.groupID == 0 }
                                Log.d("MOCK", "response groups: $groups")
                                respond(
                                    content = Json.encodeToString(groups),
                                    status = HttpStatusCode.OK,
                                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                                )
                            }

                            "${HttpRoutes.GROUPS}0" -> {
                                val group = mockRemoteGroup.filter { it.groupID == 0 }[0]
                                Log.d("MOCK", "response group: $group")
                                respond(
                                    content = Json.encodeToString(group),
                                    status = HttpStatusCode.OK,
                                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                                )
                            }

                            "${HttpRoutes.GROUPS}0/users" -> {
                                val members = mockGroupMembers.filter { it.groupID == 0 }
                                Log.d("MOCK", "response members: $members")
                                respond(
                                    content = Json.encodeToString(members),
                                    status = HttpStatusCode.OK,
                                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                                )
                            }

                            "${HttpRoutes.GROUPS}0/sessions" -> {
                                val sessions = mockSessions.filter { it.groupID == 0 }
                                Log.d("MOCK", "response sessions: $sessions")
                                respond(
                                    content = Json.encodeToString(sessions),
                                    status = HttpStatusCode.OK,
                                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                                )
                            }

                            "${HttpRoutes.LECTURES}0" -> {
                                val lecture = mockLectures.filter { it.lectureID == 0 }[0]
                                Log.d("MOCK", "response lecture: $lecture")
                                respond(
                                    content = Json.encodeToString(lecture),
                                    status = HttpStatusCode.OK,
                                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                                )
                            }

                            "${HttpRoutes.MAJORS}0" -> {
                                val major = mockMajors.filter { it.majorID == 0L }[0]
                                Log.d("MOCK", "response major: $major")
                                respond(
                                    content = Json.encodeToString(major),
                                    status = HttpStatusCode.OK,
                                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                                )
                            }

                            "${HttpRoutes.GROUPS}0/requests" -> {
                                Log.d("MOCK", "response requests: []")
                                respond(
                                    content = Json.encodeToString(emptyList<UserLight>()),
                                    status = HttpStatusCode.OK,
                                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                                )
                            }

                            else -> {
                                Log.d("MOCK", "respond undefined")
                                respond(
                                    content = "",
                                    status = HttpStatusCode.OK,
                                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                                )
                            }
                        }
                    HttpMethod.Post ->
                        when (it.url.toString()) {
                            "${HttpRoutes.GROUPS}0/sessions" -> {
                                val outgoingPart = ByteReadChannel(it.body.toByteArray())
                                Log.d("MOCK ENGINE", "outgoing session: $outgoingPart")
                                respond(
                                    content = outgoingPart,
                                    status = HttpStatusCode.OK,
                                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                                )
                            }

                            else -> {
                                Log.d("MOCK", "respond undefined")
                                respond(
                                    content = "",
                                    status = HttpStatusCode.OK,
                                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                                )
                            }
                        }
                    else -> {
                        Log.d("MOCK", "respond undefined")
                        respond(
                            content = "",
                            status = HttpStatusCode.OK,
                            headers = headersOf(HttpHeaders.ContentType, "application/json")
                        )
                    }
                }
            }
            db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
            userDao = db.userDao()
            groupDao = db.groupDao()
            sessionDao = db.sessionDao()
            mockUsers.map { userDao.saveUser(it) }
            mockRemoteGroup.map { groupDao.saveGroup(it) }

            val reportService = ReportService.newInstance(mockEngine) { "" }
            val userService = UserService.newInstance(mockEngine) { "" }
            val groupService = GroupService.newInstance(mockEngine) { "" }
            val sessionService = SessionService.newInstance(mockEngine) { "" }

            groupRepo = GroupRepository.newInstance(
                GroupRepoConstructor(
                    context,
                    groupDao,
                    auth,
                    reportService,
                    groupService
                )
            )
            userRepo =
                UserRepository.newInstance(UserRepoConstructor(context, userDao, userService, auth))
            sessionRepo = SessionRepository.newInstance(
                SessionRepoConstructor(
                    context,
                    sessionDao,
                    auth,
                    sessionService,
                    reportService
                )
            )

            composeTestRule.setContent {
                MainView(
                    userRepo = userRepo,
                    groupRepo = groupRepo,
                    sessionRepo = sessionRepo
                )
            }
            composeTestRule.onRoot().printToLog("TUR")
            //navigate to new session view of group "gfg"
            composeTestRule.onNode(hasContentDescription("SearchGroupResult") and hasText("gfg"))
                .performClick()
            composeTestRule.onNodeWithContentDescription("NewSessionButton").performClick()
            composeTestRule.onNodeWithContentDescription("NewSessionView")
                .assertExists("Navigation to new session failed.")
        }
    }

    /**
     * /FA180/ test to create a new session
     * Before: user is in NewSessionView of group "gfg"
     * Test: user performs input in place field and duration field, then presses the save button
     * After: user is now in JoinedGroupDetailsView
     */
    @Test
    fun newSessionTest() {
        composeTestRule.onNodeWithContentDescription("PlaceField").performTextInput("Hier")
        composeTestRule.onNodeWithContentDescription("DurationField").performTextInput("1")
        composeTestRule.onNodeWithContentDescription("SaveSessionButton").performClick()
        composeTestRule.onNodeWithContentDescription("JoinedGroupDetailsView").assertExists()
    }
}