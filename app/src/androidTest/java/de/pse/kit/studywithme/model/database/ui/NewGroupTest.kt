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
import de.pse.kit.studywithme.model.network.*
import de.pse.kit.studywithme.model.repository.*
import de.pse.kit.studywithme.ui.view.navigation.MainView
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

class NewGroupTest {
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
    private lateinit var mockDatabase: MutableList<RemoteGroup>

    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun lateinit() {
        runBlocking {
            mockDatabase = mutableListOf()
            auth = FakeAuthenticator()
            mockEngine = MockEngine {
                Log.d("MOCK ENGINE", "${it.method}: ${it.url}")
                //Log.d("MOCK ENGINE", "${HttpRoutes.USERS}?FUID=${auth.firebaseUID}")
                when (it.method) {
                    HttpMethod.Post ->
                        when (it.url.toString()) {
                            "${HttpRoutes.GROUPS}${auth.user?.userID}" -> {
                                val outgoingGroup = ByteReadChannel(it.body.toByteArray())
                                Log.d("MOCK ENGINE", "outgoing group: $outgoingGroup")

                                respond(
                                    content = outgoingGroup,
                                    status = HttpStatusCode.OK,
                                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                                )
                            }

                            "${HttpRoutes.MAJORS}${auth.user?.majorID}/lectures" -> {
                                val outgoingLecture = ByteReadChannel(it.body.toByteArray())
                                Log.d("MOCK ENGINE", "outgoing lecture: $outgoingLecture")

                                respond(
                                    content = outgoingLecture,
                                    status = HttpStatusCode.OK,
                                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                                )
                            }

                            else -> {
                                respond(
                                    content = "",
                                    status = HttpStatusCode.BadRequest,
                                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                                )
                            }
                        }
                    HttpMethod.Get ->
                        when (it.url.toString()) {
                            "${HttpRoutes.MAJORS}${auth.user?.majorID}/lectures?name=Lineare+Algebra" -> {
                                val lecture = Lecture(
                                    lectureID = 0,
                                    lectureName = "Lineare Algebra",
                                    majorID = auth.user!!.userID
                                )
                                Log.d("MOCK ENGINE", "Respond Lecture: $lecture")
                                respond(
                                    content = Json.encodeToString(listOf(lecture)),
                                    status = HttpStatusCode.OK,
                                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                                )
                            }

                            "${HttpRoutes.MAJORS}${auth.user?.majorID}/lectures?name=Algorithmen" -> {
                                Log.d("MOCK ENGINE", "Respond no lectures")
                                respond(
                                    content = Json.encodeToString(emptyList<Lecture>()),
                                    status = HttpStatusCode.OK,
                                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                                )
                            }

                            "${HttpRoutes.USERS}?FUID=${auth.firebaseUID}" -> {
                                val user =  UserLight(
                                    userID = 0,
                                    name = "max.mustermann"
                                )
                                Log.d("MOCK ENGINE", "respond user by fuid: $user")

                                respond(
                                    content = Json.encodeToString(listOf(user)),
                                    status = HttpStatusCode.OK,
                                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                                )
                            }

                            "${HttpRoutes.USERS}0/detail" -> {
                                val user = User(
                                    userID = 0,
                                    name = "max.mustermann",
                                    contact = "max.mustermann@mustermail.com",
                                    college = "Karlsruher Institut für Technologie",
                                    collegeID = 0,
                                    major = "Informatik B.Sc.",
                                    majorID = 0,
                                    firebaseUID = "dfg46thrge7fnd"
                                )

                                Log.d("MOCK ENGINE", "respond user by id: ${user}")
                                respond(
                                    content = Json.encodeToString(user),
                                    status = HttpStatusCode.OK,
                                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                                )
                            }

                            "${HttpRoutes.USERS}0/groups" -> {
                                Log.d("MOCK ENGINE", "respond groups by uid: ${mockDatabase}")
                                respond(
                                    content = Json.encodeToString(mockDatabase),
                                    status = HttpStatusCode.OK,
                                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                                )
                            }

                            else -> {
                                respond(
                                    content = "",
                                    status = HttpStatusCode.BadRequest,
                                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                                )
                            }
                        }

                    else -> {
                        respond(
                            content = "",
                            status = HttpStatusCode.BadRequest,
                            headers = headersOf(HttpHeaders.ContentType, "application/json")
                        )
                    }
                }
            }
            context = ApplicationProvider.getApplicationContext()
            db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
            userDao = db.userDao()
            groupDao = db.groupDao()
            sessionDao = db.sessionDao()

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
     * Test to create a new group with group-name, lecture, description, frequency of meetings,
     * type of meeting and a learning progress. (/FA80/)
     *
     */
    @ExperimentalCoroutinesApi
    @ExperimentalMaterial3Api
    @ExperimentalMaterialApi
    @Test
    fun createNewGroup() {
        composeTestRule.setContent {
            MainView(
                userRepo = userRepo,
                groupRepo = groupRepo,
                sessionRepo = sessionRepo
            )
        }
        composeTestRule.onRoot().printToLog("MAIN VIEW")
        composeTestRule.onNodeWithContentDescription("JoinedGroupsView").assertExists("Correct view(Joined Groups) after sign-in failed.")

        composeTestRule.onNodeWithContentDescription("SearchGroupsTab").performClick()
        composeTestRule.onRoot().printToLog("SEARCH GROUPS VIEW")
        composeTestRule.onNodeWithContentDescription("SearchGroupsView").assertExists("Navigation to Search Groups failed.")

        composeTestRule.onNodeWithContentDescription("NewGroupButton").performClick()
        composeTestRule.onRoot().printToLog("NEW GROUP VIEW")
        composeTestRule.onNodeWithContentDescription("NewGroupView").assertExists("Navigation to New Group failed.")

        // Fill form for new group
        composeTestRule.onNodeWithContentDescription("GroupNameField").performTextInput("Test Gruppe")
        composeTestRule.onNodeWithContentDescription("LectureNameField").performTextInput("Lineare Algebra")
        composeTestRule.onNodeWithContentDescription("DescriptionField").performTextInput("Das ist unsere Testgruppe")
        composeTestRule.onNode(hasText("Einmalig")and hasContentDescription("Chip")).performClick()
        composeTestRule.onNode(hasText("Präsenz")and hasContentDescription("Chip")).performClick()
        composeTestRule.onNodeWithContentDescription("ChapterNrField").performTextInput("1")
        composeTestRule.onNodeWithContentDescription("ExerciseNrField").performTextInput("1")
        composeTestRule.onNodeWithContentDescription("SaveGroupButton").performClick()

        composeTestRule.onRoot().printToLog("SEARCH GROUPS VIEW")
        composeTestRule.onNodeWithContentDescription("SearchGroupsView").assertExists("Group Creation failed")
    }
}