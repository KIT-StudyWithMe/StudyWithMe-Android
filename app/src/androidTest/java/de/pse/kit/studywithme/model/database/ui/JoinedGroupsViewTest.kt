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
import de.pse.kit.studywithme.model.network.GroupService
import de.pse.kit.studywithme.model.network.HttpRoutes
import de.pse.kit.studywithme.model.network.ReportService
import de.pse.kit.studywithme.model.repository.FakeGroupRepository
import de.pse.kit.studywithme.model.repository.FakeSessionRepository
import de.pse.kit.studywithme.model.repository.FakeUserRepository
import de.pse.kit.studywithme.model.repository.GroupRepository
import de.pse.kit.studywithme.ui.view.group.JoinedGroupsView
import de.pse.kit.studywithme.ui.view.navigation.MainView
import de.pse.kit.studywithme.viewModel.group.JoinedGroupDetailsViewModel
import de.pse.kit.studywithme.viewModel.group.JoinedGroupDetailsViewModelFactory
import de.pse.kit.studywithme.viewModel.group.JoinedGroupsViewModel
import de.pse.kit.studywithme.viewModel.group.JoinedGroupsViewModelFactory
import io.ktor.client.engine.mock.*
import io.ktor.http.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.lang.Thread.sleep

@ExperimentalCoroutinesApi
@ExperimentalMaterial3Api
@ExperimentalMaterialApi
class JoinedGroupsViewTest {
    private val mockRemoteGroups = listOf(
        RemoteGroup(
            groupID = 0,
            name = "gfg",
            lectureID = 0,
            description = "lol",
            lectureChapter = 1,
            exercise = 1,
            memberCount = 2,
            sessionFrequency = SessionFrequency.MONTHLY,
            sessionType = SessionType.ONLINE
        ),
        RemoteGroup(
            groupID = 1,
            name = "sadas",
            lectureID = 1,
            description = "asdas",
            lectureChapter = 1,
            exercise = 1,
            memberCount = 2,
            sessionFrequency = SessionFrequency.MONTHLY,
            sessionType = SessionType.ONLINE
        )
    )
    private val mockLectures: List<Lecture> = listOf(
        Lecture(
            lectureID = 0,
            lectureName = "Lineare Algebra",
            majorID = 0
        ),
        Lecture(
            lectureID = 1,
            lectureName = "Programmieren",
            majorID = 0
        )
    )

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
    private val mockRemoteGroup = listOf(
        RemoteGroup(
            groupID = 0,
            name = "gfg",
            lectureID = 0,
            description = "lol",
            lectureChapter = 1,
            exercise = 1,
            memberCount = 2,
            sessionFrequency = SessionFrequency.MONTHLY,
            sessionType = SessionType.ONLINE
        ),
        RemoteGroup(
            groupID = 1,
            name = "sadas",
            lectureID = 0,
            description = "asdas",
            lectureChapter = 1,
            exercise = 1,
            memberCount = 2,
            sessionFrequency = SessionFrequency.MONTHLY,
            sessionType = SessionType.ONLINE
        )
    )

    private val mockGroupMember = listOf(
        GroupMember(
            groupID = 0,
            userID = 0,
            name = "max.mustermann",
            isAdmin = true
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
                    HttpMethod.Get -> {
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
                                val groups = mockRemoteGroups
                                Log.d("MOCK", "response groups: $groups")
                                respond(
                                    content = Json.encodeToString(groups),
                                    status = HttpStatusCode.OK,
                                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                                )
                            }

                            "${HttpRoutes.GROUPS}?text=sadas" -> {
                                val groups = mockRemoteGroup.filter { it.name.startsWith("sadas") }
                                Log.d("MOCK", "response groups: $groups")
                                respond(
                                    content = Json.encodeToString(groups),
                                    status = HttpStatusCode.OK,
                                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                                )
                            }

                            "${HttpRoutes.MAJORS}0/lectures" -> {
                                val lectures = mockLectures.filter { it.majorID == 0 }
                                Log.d("MOCK", "response lectures: $lectures")
                                respond(
                                    content = Json.encodeToString(lectures),
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

                            "${HttpRoutes.GROUPS}1" -> {
                                val group = mockRemoteGroup.filter { it.groupID == 1 }[0]
                                Log.d("MOCK", "response group: $group")
                                respond(
                                    content = Json.encodeToString(group),
                                    status = HttpStatusCode.OK,
                                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                                )
                            }

                            "${HttpRoutes.LECTURES}1" -> {
                                val lecture = mockLectures.filter { it.lectureID == 1 }[0]
                                Log.d("MOCK", "response lecture: $lecture")
                                respond(
                                    content = Json.encodeToString(lecture),
                                    status = HttpStatusCode.OK,
                                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                                )
                            }

                            "${HttpRoutes.GROUPS}0/users" -> {
                                val groupMember = mockGroupMember.filter { it.groupID == 0 }
                                Log.d("MOCK", "respond members: $groupMember")
                                respond(
                                    content = Json.encodeToString(groupMember),
                                    status = HttpStatusCode.OK,
                                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                                )
                            }

                            "${HttpRoutes.MAJORS}0/lectures/Lineare+Algebra" -> {
                                val lectures = mockLectures.filter {
                                    it.majorID == 0 && it.lectureName.startsWith("Lineare Algebra")
                                }
                                Log.d("MOCK", "response lectures: $lectures")
                                respond(
                                    content = Json.encodeToString(lectures),
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
                    }
                    HttpMethod.Put -> {
                        when (it.url.toString()) {
                            "${HttpRoutes.USERS}0/report/0" -> {
                                Log.d("MOCK", "respond report")
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
            mockRemoteGroup.filter { it.groupID == 0 }.map { groupDao.saveGroup(it) }

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
            mockRemoteGroups.map { groupDao.saveGroup(it) }
        }
    }

    @Test
    fun navigateToSearchGroupViewAndBackTest() {
        composeTestRule.setContent {
            MainView(
                userRepo = FakeUserRepository(true),
                groupRepo = FakeGroupRepository(),
                sessionRepo = FakeSessionRepository()
            )
        }
        // For debugging
        composeTestRule.onRoot().printToLog("NAVIGATION_VIEW")

        val searchGroupsTab = composeTestRule.onNodeWithContentDescription("SearchGroupsTab")
        searchGroupsTab.assertExists()
        searchGroupsTab.performClick()

        composeTestRule.onNodeWithContentDescription("SearchGroupsView").assertExists()

        val myGroupsTab = composeTestRule.onNodeWithContentDescription("MyGroupsTab")
        myGroupsTab.assertExists()
        myGroupsTab.performClick()

        composeTestRule.onNodeWithContentDescription("JoinedGroupsView").assertExists()
    }

    @Test
    fun filterJoinedGroupsTest() {
        composeTestRule.setContent {
            val viewModel: JoinedGroupsViewModel = viewModel(
                factory = JoinedGroupsViewModelFactory(
                    rememberNavController(),
                    groupRepo
                )
            )

            JoinedGroupsView(viewModel)
        }
        // For debugging
        composeTestRule.onRoot().printToLog("JOINED GROUPS VIEW")
        sleep(1000)
        composeTestRule.onNode(hasContentDescription("SearchGroupResult") and hasText("Programmieren")).assertExists()

        composeTestRule.onNode(hasContentDescription("Chip") and hasText("Lineare Algebra"))
            .assertExists()
        composeTestRule.onNode(hasContentDescription("Chip") and hasText("Lineare Algebra"))
            .performScrollTo()
        composeTestRule.onNode(hasContentDescription("Chip") and hasText("Lineare Algebra"))
            .performClick()

        composeTestRule.onNode(hasContentDescription("SearchGroupResult") and hasText("Programmieren"))
            .assertDoesNotExist()
        composeTestRule.onAllNodes(hasContentDescription("SearchGroupResult") and hasText("Lineare Algebra"))[0].assertExists()
    }

    /**
     * FA70 UI-Test
     *
     */
    @Test
    fun reportUser() {
        composeTestRule.setContent {
            val viewModel: JoinedGroupDetailsViewModel = viewModel(
                factory = JoinedGroupDetailsViewModelFactory(
                    rememberNavController(),
                    groupID = 0,
                    groupRepo,
                    sessionRepo
                )
            )

            JoinedGroupDetailsView(viewModel)
        }

        val reportAdmin = composeTestRule.onNode(hasTestTag("Admin klicken"))
        val reportUserName = composeTestRule.onNode(hasTestTag("Nutzername melden"))
        val confirm = composeTestRule.onNode(hasTestTag("Bestätigen"))
        reportAdmin.performClick()
        reportUserName.performClick()
        confirm.performClick()
        composeTestRule.onNodeWithContentDescription("JoinedGroupDetailsView").assertExists()
    }
}