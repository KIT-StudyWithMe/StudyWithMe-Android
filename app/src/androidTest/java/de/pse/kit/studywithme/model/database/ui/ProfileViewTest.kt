package de.pse.kit.studywithme.model.database.ui

import android.content.Context
import android.util.Log
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
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
import de.pse.kit.studywithme.ui.view.profile.ProfileView
import de.pse.kit.studywithme.viewModel.profile.ProfileViewModel
import io.ktor.client.engine.mock.*
import io.ktor.http.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Before
import de.pse.kit.studywithme.model.repository.FakeGroupRepository
import de.pse.kit.studywithme.model.repository.FakeSessionRepository
import de.pse.kit.studywithme.model.repository.FakeUserRepository
import de.pse.kit.studywithme.ui.view.navigation.MainView
import io.ktor.utils.io.*
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test

@ExperimentalMaterial3Api
@ExperimentalMaterialApi
class ProfileViewTest {
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
    private val mockDatabase: List<RemoteGroup> = emptyList()
    private val mockUser = User(
        userID = 0,
        name = "max.mustermann",
        contact = "max.mustermann@mustermail.com",
        college = "Karlsruher Institut für Technologie",
        collegeID = 0,
        major = "Informatik B.Sc.",
        majorID = 0,
        firebaseUID = "dfg46thrge7fnd"
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
    fun lateinit() {
        runBlocking {
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
                                Log.d("MOCK ENGINE", "respond user by fuid: $mockLightUsers")

                                respond(
                                    content = Json.encodeToString(mockLightUsers),
                                    status = HttpStatusCode.OK,
                                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                                )
                            }

                            "${HttpRoutes.USERS}0/detail" -> {
                                Log.d("MOCK ENGINE", "respond user by id: $mockUser")
                                respond(
                                    content = Json.encodeToString(mockUser),
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

                            "${HttpRoutes.INSTITUTIONS}?name=KIT" -> {
                                val institutions = listOf(Institution(0, "KIT"))
                                Log.d("MOCK ENGINE", "respond institutions by name KIT: ${institutions}")
                                respond(
                                    content = Json.encodeToString(institutions),
                                    status = HttpStatusCode.OK,
                                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                                )
                            }

                            "${HttpRoutes.MAJORS}?name=Info" -> {
                                val majors = listOf(Major(0, "Info"))
                                Log.d("MOCK ENGINE", "respond institutions by name KIT: ${majors}")
                                respond(
                                    content = Json.encodeToString(majors),
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
                            "${HttpRoutes.USERS}0/detail" -> {
                                val outgoingUser = ByteReadChannel(it.body.toByteArray())
                                Log.d("MOCK ENGINE", "outgoing user: $outgoingUser")

                                respond(
                                    content = outgoingUser,
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
            userDao.saveUser(mockUser)
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
    }

    /**
     * test to show the profile data of a user
     * Before: user is on ProfileView
     * After: user profile information is shown correctly
     */
    @ExperimentalCoroutinesApi
    @Test
    fun fetchAndDisplayProfileData() {
        lateinit var viewModel: ProfileViewModel
        composeTestRule.setContent {
            viewModel = ProfileViewModel(
                navController = rememberNavController(),
                userRepo = userRepo
            )
            Log.d("TEST", "view")
            ProfileView(viewModel)
        }

        composeTestRule.onNodeWithContentDescription("ProfileView").assertExists()
        composeTestRule.onRoot().printToLog("PROFILE VIEW")

        composeTestRule.onNodeWithText(mockUser.name).assertExists()
        composeTestRule.onNodeWithText(mockUser.contact).assertExists()
        composeTestRule.onNodeWithText(mockUser.college!!).assertExists()
        composeTestRule.onNodeWithText(mockUser.major!!).assertExists()
    }

    /**
     * /FA40/ test to edit the own profile of a user
     * Before: user is on JoinedGroupsView (MainView)
     * Test: user presses the profile tab in the navigation bar, user gets on ProfileView,
     * user clicks on the button to edit the profile, user gets on EditProfileVIew,
     * user performs the input in the text fields and presses the safe button
     * After: user is on ProfileView
     */
    @ExperimentalCoroutinesApi
    @ExperimentalMaterial3Api
    @ExperimentalMaterialApi
    @Test
    fun editProfile() {
        composeTestRule.setContent {
            MainView(
                userRepo = userRepo,
                groupRepo = groupRepo,
                sessionRepo = sessionRepo
            )
        }

        //For debugging
        composeTestRule.onRoot().printToLog("PROFILE_VIEW")

        val editProfile = composeTestRule.onNodeWithContentDescription("EditGroupButton")
        val editCollege = composeTestRule.onNode(hasTestTag("Uni"))
        val editMajor = composeTestRule.onNode(hasTestTag("Studiengang"))
        val editUsername = composeTestRule.onNode(hasTestTag("Nutzername"))
        val editContact = composeTestRule.onNode(hasTestTag("Kontaktinfo"))
        val myProfileTab = composeTestRule.onNodeWithContentDescription("ProfileTab")
        val saveProfile = composeTestRule.onNode(hasTestTag("Speichern"))

        myProfileTab.assertExists()
        myProfileTab.performClick()
        composeTestRule.onNodeWithContentDescription("ProfileView").assertExists()
        editProfile.performClick()
        composeTestRule.onNodeWithContentDescription("EditProfileView").assertExists()
        editCollege.performClick().performTextClearance()
        editCollege.performClick().performTextInput("KIT")
        editMajor.performClick().performTextClearance()
        editMajor.performClick().performTextInput("Info")
        editUsername.performClick().performTextClearance()
        editUsername.performClick().performTextInput("maxMustermann")
        editContact.performClick().performTextClearance()
        editContact.performClick().performTextInput("maxMustermann@mustermail.com")
        saveProfile.performClick()
        composeTestRule.onRoot().printToLog("PROFILE_VIEW")
        composeTestRule.onNodeWithContentDescription("ProfileView").assertExists()
    }

    /**
     * /FA50/ test to log out of the application
     * Before: user is on JoinedGroupsView(MainView)
     * Test: user presses the profile tab in the navigation bar,
     * user is in the ProfileView now and presses the log out button
     * After: user is in the SignInView
     */
    @ExperimentalCoroutinesApi
    @ExperimentalMaterial3Api
    @ExperimentalMaterialApi
    @Test
    fun logout() {
        composeTestRule.setContent {
            MainView(
                userRepo = userRepo,
                groupRepo = groupRepo,
                sessionRepo = sessionRepo
            )
        }
        val myProfileTab = composeTestRule.onNodeWithContentDescription("ProfileTab")
        myProfileTab.assertExists()
        myProfileTab.performClick()
        composeTestRule.onNodeWithContentDescription("ProfileView").assertExists()

        val logout = composeTestRule.onNode(hasTestTag("Abmelden"))
        logout.performClick()

        composeTestRule.onNodeWithContentDescription("SignInView").assertExists()
    }
}

