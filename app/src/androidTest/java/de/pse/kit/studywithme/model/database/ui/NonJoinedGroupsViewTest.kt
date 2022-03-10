package de.pse.kit.studywithme.model.database.ui

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
import de.pse.kit.studywithme.model.network.GroupService
import de.pse.kit.studywithme.model.network.HttpRoutes
import de.pse.kit.studywithme.model.network.SessionService
import de.pse.kit.studywithme.model.network.UserService
import de.pse.kit.studywithme.model.repository.*
import de.pse.kit.studywithme.ui.view.navigation.MainView
import io.ktor.client.engine.mock.*
import io.ktor.http.*
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
    lateinit var context: Context
    lateinit var userDao: UserDao
    lateinit var groupDao: GroupDao
    lateinit var sessionDao: SessionDao
    lateinit var db: AppDatabase

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

    private val mockGroup = listOf(
        Group(
            groupID = 0,
            name = "gfg",
            lectureID = 0,
            lecture = null,
            major = null,
            description = "lol",
            lectureChapter = 1,
            exercise = 1,
            memberCount = 2,
            sessionFrequency = SessionFrequency.MONTHLY,
            sessionType = SessionType.ONLINE
        )
    )

    private val mockLightUsers = listOf(
        UserLight(
            userID = 0,
            name = "max.mustermann"
        )
    )

    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun initLocalDatabase() {
        runBlocking {
            context = ApplicationProvider.getApplicationContext()
            db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
            userDao = db.userDao()
            groupDao = db.groupDao()
            sessionDao = db.sessionDao()
            mockUsers.map { userDao.saveUser(it) }
            mockRemoteGroup.filter { it.groupID == 0 }.map { groupDao.saveGroup(it) }
        }
    }

    val signedInUser = mockUsers.filter { it.userID == 0 }[0]
    val mockEngine = MockEngine {
        Log.d("MOCK", it.url.toString())
        if (it.method == HttpMethod.Get) {
            when (it.url.toString()) {
                "${HttpRoutes.USERS}?FUID=dfg46thrge7fnd" -> {
                    Log.d("MOCK", "respond a")
                    respond(
                        content = Json.encodeToString(mockLightUsers.filter { it.userID.toInt() == 0 }),
                        status = HttpStatusCode.OK,
                        headers = headersOf(HttpHeaders.ContentType, "application/json")
                    )
                }

                "${HttpRoutes.USERS}0/detail" -> {
                    Log.d("MOCK", "respond b")
                    respond(
                        content = Json.encodeToString(signedInUser),
                        status = HttpStatusCode.OK,
                        headers = headersOf(HttpHeaders.ContentType, "application/json")
                    )
                }

                "${HttpRoutes.GROUPS}?text=sadas" -> {
                    Log.d("MOCK", "respond c")
                    respond(
                        content = Json.encodeToString(mockRemoteGroup.filter { it.groupID == 1 }),
                        status = HttpStatusCode.OK,
                        headers = headersOf(HttpHeaders.ContentType, "application/json")
                    )
                }

                else -> {
                    Log.d("MOCK", "respond while else")
                    respond(
                        content = "",
                        status = HttpStatusCode.OK,
                        headers = headersOf(HttpHeaders.ContentType, "application/json")
                    )
                }
            }
        } else {
            Log.d("MOCK", "respond else")
            respond(
                content = "",
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }
    }

    @Ignore
    @Test
    fun report() {
        val auth = FakeAuthenticator()

        composeTestRule.setContent {
            MainView(
                userRepo = UserRepository.getInstance(
                    UserRepoConstructor(
                        context = context,
                        userDao = userDao,
                        userService = UserService.getInstance(Pair(mockEngine) { "" }),
                        auth = auth
                    )
                ),
                groupRepo = GroupRepository.getInstance(
                    GroupRepoConstructor(
                        context = context,
                        groupDao = groupDao,
                        groupService = GroupService.getInstance(Pair(mockEngine) { "" }),
                        auth = auth
                    )
                ),
                sessionRepo = SessionRepository.getInstance(
                    SessionRepoConstructor(
                        context = context,
                        sessionDao = sessionDao,
                        sessionService = SessionService.getInstance(Pair(mockEngine) { "" }),
                        auth = auth
                    )
                )
            )
        }

        //For debugging
        composeTestRule.onRoot().printToLog("NON_JOINED_GROUPS_VIEW")

        val report = composeTestRule.onNode(hasTestTag("Melden"))
        val search = composeTestRule.onNode(hasTestTag("Suche Gruppen"))
        val searchGroupsTab = composeTestRule.onNodeWithContentDescription("SearchGroupsTab")

        searchGroupsTab.performClick()
        composeTestRule.onNodeWithContentDescription("SearchGroupsView").assertExists()
        search.performClick().performTextInput("sadas")
        composeTestRule.onRoot().printToLog("NON_JOINED_GROUPS_VIEW")
        composeTestRule.onAllNodes(hasContentDescription("SearchGroupResult") and hasText("sadas"))[0].performClick()
    }
}