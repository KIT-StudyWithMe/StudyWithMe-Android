package de.pse.kit.studywithme.ui

import android.content.Context
import android.util.Log
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
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
import de.pse.kit.studywithme.ui.view.navigation.MainView
import de.pse.kit.studywithme.viewModel.group.JoinedGroupDetailsViewModel
import de.pse.kit.studywithme.viewModel.group.JoinedGroupDetailsViewModelFactory
import io.ktor.client.engine.mock.*
import io.ktor.http.*
import io.ktor.utils.io.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.*

@ExperimentalCoroutinesApi
@ExperimentalMaterial3Api
@ExperimentalMaterialApi
class JoinedGroupDetailsViewTest {
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
            college = "Karlsruher Institut f??r Technologie",
            collegeID = 0,
            major = "Informatik B.Sc.",
            majorID = 0,
            firebaseUID = "dfg46thrge7fnd"
        )
    )
    private val mockSessions = listOf(
        Session(
            sessionID = 0,
            groupID = 0,
            location = "Hier",
            date = Date(20000),
            duration = 1
        )
    )
    private lateinit var mockSessionAttendees: MutableList<SessionAttendee>
    private val mockLightUsers = listOf(
        UserLight(
            userID = 0,
            name = "max.mustermann"
        )
    )
    private val mockGroupMembers = listOf(
        GroupMember(
            groupID = 0,
            userID = 0,
            name = "max.mustermann",
            isAdmin = true
        ),
        GroupMember(
            groupID = 0,
            userID = 1,
            name = "max anders",
            isAdmin = false
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
    private val signedInUser = mockUsers.filter { it.userID == 0 }[0]
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
            mockSessionAttendees = mutableListOf()
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

                            "${HttpRoutes.SESSIONS}0/attendee" -> {
                                val attendees = mockSessionAttendees.filter { it.sessionID == 0 }
                                Log.d("MOCK", "response group: $attendees")
                                respond(
                                    content = Json.encodeToString(attendees),
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
                    HttpMethod.Put ->
                        when (it.url.toString()) {
                            "${HttpRoutes.SESSIONS}0/participate/0" -> {
                                mockSessionAttendees.add(
                                    SessionAttendee(
                                        sessionAttendeeID = 0,
                                        sessionID = 0,
                                        participates = true,
                                        userID = 0
                                    )
                                )
                                val outgoingPart = ByteReadChannel(it.body.toByteArray())
                                Log.d("MOCK ENGINE", "outgoing partisipate: $outgoingPart")
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
                    HttpMethod.Delete ->
                        when (it.url.toString()) {
                            "${HttpRoutes.GROUPS}0/users/1" -> {
                                Log.d("MOCK", "respond deletion")
                                respond(
                                    content = "",
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
            //navigate from JoinedGroupsView(MainView) to JoinedGroupDetailsView of group 'gfg'
            composeTestRule.onNode(hasContentDescription("SearchGroupResult") and hasText("gfg"))
                .performClick()
        }
    }

    /**
     * /FA120/
     * Before: user is on JoinedGroupDetailsView of group with
     * name "gfg", lecture "Lineare Algebra", admin "max.mustermann" , location "Hier",
     * sessionFrequency "Monthly", sessionType "Online", chapterNumber "1", exerciseNumber "1"
     * After: all information is shown correctly
     */
    @Test
    fun showDetailedGroupInformation() {
        composeTestRule.onNode(hasContentDescription("TopBarTitle") and hasText("gfg"))
            .assertExists()
        composeTestRule.onNode(hasContentDescription("TopBarSubTitle") and hasText("Lineare Algebra"))
            .assertExists()
        composeTestRule.onNode(hasText("max.mustermann"))
            .assertExists()
        composeTestRule.onNode(hasText("Hier"))
            .assertExists()
        composeTestRule.onNode(hasContentDescription("Chip") and hasText("Monthly"))
            .assertExists()
        composeTestRule.onNode(hasContentDescription("Chip") and hasText("Online"))
            .assertExists()
        composeTestRule.onNode(hasText("Vorlesung: Kapitel Nr. 1"))
            .assertExists()
        composeTestRule.onNode(hasText("??bungsblatt Nr. 1"))
            .assertExists()
    }

    /**
     * /FA200/
     * Before: user is on JoinedGroupDetailsView of group 'gfg' with an existing session
     * Test: user presses the participate button
     * After: user is on JoinedGroupDetailsView
     */
    @Test
    fun confirmSessionParticipation() {
        composeTestRule.onNode(hasContentDescription("Participate-Button") and hasText("Teilnehmen"))
            .assertExists()
        composeTestRule.onNode(hasContentDescription("Participate-Button") and hasText("Teilnehmen"))
            .performScrollTo()
        composeTestRule.onNode(hasContentDescription("Participate-Button") and hasText("Teilnehmen"))
            .performClick()
    }

    /**
     * test to leave a group
     * Before: user is on JoinedGroupDetailsView of group 'gfg'
     * Test: user presses the button to leave the group
     * After: user is on JoinedGroupsView
     */
    @Test
    fun leaveGroup() {
        composeTestRule.onNode(hasContentDescription("LeaveGroupButton"))
            .assertExists()
        composeTestRule.onNode(hasContentDescription("LeaveGroupButton"))
            .performScrollTo()
        composeTestRule.onNode(hasContentDescription("LeaveGroupButton"))
            .performClick()
        composeTestRule.onNodeWithContentDescription("JoinedGroupsView").assertExists()
    }

    /**
     * /FA140/ test to remove a group member
     * Before: user is on JoinedGroupDetailsView of group 'gfg' where he is admin, with other member "max anders"
     * Test: user presses the button on member "max anders" and then presses the remove member button and the confirm button
     * After: user is on JoinedGroupDetailsView
     */
    @Test
    fun removeGroupMemberTest() {
        composeTestRule.onNode(hasContentDescription("GroupMemberText") and hasText("max anders"))
            .assertExists()
        composeTestRule.onNode(hasContentDescription("GroupMemberText") and hasText("max anders"))
            .performClick()
        composeTestRule.onNodeWithContentDescription("RemoveMemberButton")
            .performClick()
        composeTestRule.onNodeWithContentDescription("ConfirmButton")
            .performClick()
        composeTestRule.onNodeWithContentDescription("JoinedGroupDetailsView")
            .assertExists()
    }

}