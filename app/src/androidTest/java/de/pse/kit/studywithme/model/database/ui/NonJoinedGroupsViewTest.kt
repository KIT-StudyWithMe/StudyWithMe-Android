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
import de.pse.kit.studywithme.ui.view.group.NonJoinedGroupDetailsView
import de.pse.kit.studywithme.ui.view.navigation.MainView
import de.pse.kit.studywithme.viewModel.group.NonJoinedGroupDetailsViewModel
import de.pse.kit.studywithme.viewModel.group.NonJoinedGroupDetailsViewModelFactory
import de.pse.kit.studywithme.ui.view.group.SearchGroupsView
import de.pse.kit.studywithme.ui.view.navigation.MainView
import de.pse.kit.studywithme.viewModel.group.SearchGroupsViewModel
import de.pse.kit.studywithme.viewModel.group.SearchGroupsViewModelFactory
import io.ktor.client.engine.mock.*
import io.ktor.http.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test

@ExperimentalMaterial3Api
@ExperimentalMaterialApi
class NonJoinedGroupsViewTest {
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

    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun initLocalDatabase() {
        runBlocking {
            context = ApplicationProvider.getApplicationContext()
            auth = FakeAuthenticator()
            mockEngine = MockEngine {
                Log.d("MOCK ENGINE", "${it.method}: ${it.url}")
                if (it.method == HttpMethod.Get) {
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

                        "${HttpRoutes.GROUPS}?text=sadas" -> {
                            val groups = mockRemoteGroup.filter { it.name.startsWith("sadas") }
                            Log.d("MOCK", "response groups: $groups")
                            respond(
                                content = Json.encodeToString(groups),
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

                        "${HttpRoutes.GROUPS}1" -> {
                            val group = mockRemoteGroup.filter { it.groupID == 1 }[0]
                            Log.d("MOCK", "response group: $group")
                            respond(
                                content = Json.encodeToString(group),
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
                } else {
                    Log.d("MOCK", "respond undefined")
                    respond(
                        content = "",
                        status = HttpStatusCode.OK,
                        headers = headersOf(HttpHeaders.ContentType, "application/json")
                    )
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

            groupRepo = GroupRepository.newInstance(groupDao, auth, reportService, groupService)
            userRepo = UserRepository.newInstance(userDao, userService, auth)
            sessionRepo = SessionRepository.newInstance(sessionDao, auth, sessionService, reportService)
        }
    }

    /**
     * /FA160/ UI-Test
     *
     */
    @Test
    fun reportGroup() {
        val auth = FakeAuthenticator()

        composeTestRule.setContent {
            val viewmodel: NonJoinedGroupDetailsViewModel = viewModel(
                factory = NonJoinedGroupDetailsViewModelFactory(
                    navController = rememberNavController(),
                    groupID = 1,
                    groupRepo = groupRepo
                )
            )
            NonJoinedGroupDetailsView(viewmodel)
        }

        //For debugging
        composeTestRule.onRoot().printToLog("NON_JOINED_GROUPS_VIEW")

        val report = composeTestRule.onNode(hasTestTag("Melden"))
        val reportGroupName = composeTestRule.onNode(hasTestTag("Gruppenname melden"))
        val confirm = composeTestRule.onNode(hasTestTag("Bestätigen"))

        report.performClick()
        reportGroupName.performClick()
        confirm.performClick()
        composeTestRule.onNodeWithContentDescription("NonJoinedGroupDetailsView").assertExists()
    }
}