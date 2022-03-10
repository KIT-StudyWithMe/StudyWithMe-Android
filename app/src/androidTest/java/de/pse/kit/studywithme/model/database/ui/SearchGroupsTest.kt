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
class SearchGroupsTest {
    private lateinit var context: Context
    private lateinit var userDao: UserDao
    private lateinit var groupDao: GroupDao
    private lateinit var sessionDao: SessionDao
    private lateinit var groupRepo: GroupRepository
    private lateinit var db: AppDatabase
    private lateinit var auth: FakeAuthenticator
    private lateinit var mockEngine: MockEngine
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
            mockRemoteGroup.filter { it.groupID == 0 }.map { groupDao.saveGroup(it) }

            val reportService = ReportService.newInstance(mockEngine) { "" }
            val groupService = GroupService.newInstance(mockEngine) { "" }

            groupRepo = GroupRepository.newInstance(groupDao, auth, reportService, groupService)
        }
    }

    /**
     * Test to search and list groups.
     *
     */
    @ExperimentalCoroutinesApi
    @Test
    fun searchGroupsTest() {
        composeTestRule.setContent {
            val viewModel: SearchGroupsViewModel = viewModel(
                factory = SearchGroupsViewModelFactory(
                    navController = rememberNavController(),
                    groupRepo = groupRepo
                )
            )

            SearchGroupsView(viewModel)
        }
        composeTestRule.onRoot().printToLog("SEARCH GROUPS VIEW")
        composeTestRule.onNodeWithContentDescription("SearchGroupsView").assertExists()

        composeTestRule.onNode(hasTestTag("Suche Gruppen")).performTextInput("sadas")
        composeTestRule.onRoot().printToLog("SEARCH GROUPS VIEW")
        composeTestRule.onNode(hasContentDescription("SearchGroupResult") and hasText("sadas")).assertExists()
    }
}