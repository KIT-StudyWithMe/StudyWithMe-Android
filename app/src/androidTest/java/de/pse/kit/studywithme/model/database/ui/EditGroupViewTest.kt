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
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import java.lang.Thread.sleep

@ExperimentalCoroutinesApi
@ExperimentalMaterial3Api
@ExperimentalMaterialApi
class EditGroupViewTest {
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
    private var group = RemoteGroup(groupID = 0, name = "Test Gruppe",
        lectureID = 0,
        description = "Das ist unsere Testgruppe",
        sessionFrequency = SessionFrequency.ONCE,
        sessionType = SessionType.PRESENCE,
        lectureChapter = 1,
        exercise = 1,
        memberCount = 1,
    )
    private var hidden = false
    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setUp() {
        runBlocking {
            mockDatabase = mutableListOf()
            auth = FakeAuthenticator()
            mockEngine = MockEngine {
                Log.d("MOCK ENGINE", "${it.method}: ${it.url}")
                when (it.method) {
                    HttpMethod.Post ->
                        when (it.url.toString()) {
                            "${HttpRoutes.MAJORS}${auth.user?.majorID}/lectures" -> {
                                val outgoingLecture = ByteReadChannel(it.body.toByteArray())
                                Log.d("MOCK ENGINE", "outgoing lecture: $outgoingLecture")

                                respond(
                                    content = outgoingLecture,
                                    status = HttpStatusCode.OK,
                                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                                )
                            }
                            "${HttpRoutes.GROUPS}0/hide" -> {
                                Log.d("MOCK ENGINE", "respond hide by: true")
                                hidden = !hidden
                                respond(
                                    content = Json.encodeToString(true),
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
                    HttpMethod.Delete ->
                        when (it.url.toString()) {
                            "${HttpRoutes.GROUPS}0" -> {
                                Log.d("MOCK ENGINE", "respond delete by: true")

                                respond(
                                    content = Json.encodeToString(true),
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
                    HttpMethod.Put ->
                        when (it.url.toString()) {
                            "${HttpRoutes.GROUPS}0" -> {
                                val outgoingGroup = ByteReadChannel(it.body.toByteArray())
                                Log.d("MOCK ENGINE", "outgoing group: $outgoingGroup")

                                respond(
                                    content = outgoingGroup,
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
                            "${HttpRoutes.GROUPS}0/hide" -> {
                                Log.d("MOCK ENGINE", "respond hide by: $hidden")

                                respond(
                                    content = Json.encodeToString(hidden),
                                    status = HttpStatusCode.OK,
                                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                                )
                            }
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
                            "${HttpRoutes.LECTURES}0" -> {
                                val lecture = Lecture(
                                    lectureID = 0,
                                    lectureName = "Lineare Algebra",
                                    majorID = auth.user!!.userID
                                )

                                Log.d("MOCK ENGINE", "respond lecture by: $lecture")

                                respond(
                                    content = Json.encodeToString(lecture),
                                    status = HttpStatusCode.OK,
                                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                                )
                            }
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

                                Log.d("MOCK ENGINE", "respond groups by uid: ${listOf(group)}")

                                respond(
                                    content = Json.encodeToString(listOf(group)),
                                    status = HttpStatusCode.OK,
                                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                                )
                            }
                            "${HttpRoutes.USERS}groups/0" -> {

                                Log.d("MOCK ENGINE", "respond groups by uid: ${listOf(group)}")
                                respond(
                                    content = Json.encodeToString(listOf(group)),
                                    status = HttpStatusCode.OK,
                                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                                )
                            }
                            "${HttpRoutes.MAJORS}${auth.user?.majorID}" -> {
                                val outgoingMajor = ByteReadChannel(it.body.toByteArray())
                                Log.d("MOCK ENGINE", "outgoing lecture: $outgoingMajor")

                                respond(
                                    content = outgoingMajor,
                                    status = HttpStatusCode.OK,
                                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                                )
                            }
                            "${HttpRoutes.GROUPS}0/users" -> {
                                val groupMember = GroupMember(0, 0, "max.mustermann", true)
                                Log.d("MOCK ENGINE", "respond user: $groupMember")

                                respond(
                                    content = Json.encodeToString(listOf(groupMember)),
                                    status = HttpStatusCode.OK,
                                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                                )
                            }
                            "${HttpRoutes.GROUPS}0" -> {
                                Log.d("MOCK ENGINE", "respond group: ${listOf(group)}")
                                respond(
                                    content = Json.encodeToString(listOf(group)),
                                    status = HttpStatusCode.OK,
                                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                                )
                            }
                            "${HttpRoutes.GROUPS}0/sessions" -> {
                                Log.d("MOCK ENGINE", "respond sessions by gid: ${emptyList<Session>()}")
                                respond(
                                    content = Json.encodeToString(emptyList<Session>()),
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

            groupRepo = GroupRepository.newInstance(GroupRepoConstructor(context, groupDao, auth, reportService, groupService))
            userRepo = UserRepository.newInstance(UserRepoConstructor(context, userDao, userService, auth))
            sessionRepo = SessionRepository.newInstance(SessionRepoConstructor(context, sessionDao, auth, sessionService, reportService))
        }
        composeTestRule.setContent {
            MainView(
                userRepo = userRepo,
                groupRepo = groupRepo,
                sessionRepo = sessionRepo
            )
        }
        //navigate from JoinedGroupsView(MainView) to EditGroupView of "Test Gruppe"
        composeTestRule.onRoot().printToLog("MY GROUPS VIEW")
        composeTestRule.onNode(hasContentDescription("SearchGroupResult") and hasText("Test Gruppe"))
            .assertExists()
        composeTestRule.onNode(hasContentDescription("SearchGroupResult") and hasText("Test Gruppe"))
            .performScrollTo()
        composeTestRule.onNode(hasContentDescription("SearchGroupResult") and hasText("Test Gruppe"))
            .performClick()
        sleep(1000)

        composeTestRule.onNode(hasContentDescription("JoinedGroupDetailsView"))
            .assertExists()
        composeTestRule.onNode(hasContentDescription("Knopf um die Gruppe zu editieren"))
            .assertExists()
        composeTestRule.onNode(hasText("Test Gruppe"))
            .assertExists()
        composeTestRule.onNode(hasContentDescription("Knopf um die Gruppe zu editieren"))
            .performClick()

        composeTestRule.onNode(hasContentDescription("EditGroupView"))
            .assertExists("Navigation zu EditGroupView fehlgeschlagen")
        composeTestRule.onRoot().printToLog("EDIT GROUP VIEW")
    }

    /**
     * /FA 130/ test to change group information
     * Before: user is on EditGroupView of an existing group where he is admin
     * Test: user puts new input in field for group name and presses the save button
     * After: user is on JoinedGroupDetailsView and new name of group is shown
     */
    @Test
    fun changeGroupInformation() {
        val groupNameField = composeTestRule.onNode(hasContentDescription("GroupNameField"))

        val saveButton = composeTestRule.onNode(hasTestTag("SaveButton"))
        groupNameField.assertExists()
        groupNameField.performScrollTo()
        groupNameField.performClick().performTextClearance()
        groupNameField.performClick().performTextInput("NewGroupName")

        saveButton.assertExists()
        saveButton.performClick()
        sleep(1000)
        composeTestRule.onNode(hasContentDescription("JoinedGroupDetailsView")).assertExists("Navigation to JoinedGroupDetailsView failed")
        composeTestRule.onNode(hasContentDescription("TopBarTitle") and hasText("NewGroupName")).assertExists("Save failed")

    }
    /**
     * /FA 150/ test to hide a group as admin
     * Before: user is on EditGroupView of an existing group where he is admin, hide button showing 'Für andere ausblenden'
     * Test: user presses the button to hide the group
     * After: user stays on EditGroupView with the hide button showing 'Für andere einblenden'
     */
    @Test
    fun hideGroup() {
        val hideButton = composeTestRule.onNode(hasTestTag("HideButton"))
        hideButton.assertExists()
        hideButton.performScrollTo()
        composeTestRule.onNode(hasTestTag("HideButton") and hasText("Für andere ausblenden")).assertExists()
        composeTestRule.onNode(hasTestTag("HideButton") and hasText("Für andere einblenden")).assertDoesNotExist()
        hideButton.performClick()
        composeTestRule.onNode(hasTestTag("HideButton") and hasText("Für andere ausblenden")).assertDoesNotExist()
        composeTestRule.onNode(hasTestTag("HideButton") and hasText("Für andere einblenden")).assertExists()
        composeTestRule.onNode(hasContentDescription("EditGroupView")).assertExists()
    }

    /**
     * /FA 170/ test to delete a group as admin
     * Before: user is on EditGroupView of an existing group where he is admin
     * Test: user presses the button to delete the group
     * After: user is on JoinedGroupsView
     */
    @Test
    fun deleteGroup() {
        val deleteButton = composeTestRule.onNode(hasTestTag("DeleteButton"))
        deleteButton.assertExists()
        deleteButton.performScrollTo()
        deleteButton.performClick()
        composeTestRule.onNode(hasContentDescription("JoinedGroupsView")).assertExists("Delete failed")
    }
}