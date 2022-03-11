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
import de.pse.kit.studywithme.ui.view.navigation.MainView
import io.ktor.utils.io.*
import org.junit.Rule
import org.junit.Test

@ExperimentalMaterial3Api
@ExperimentalMaterialApi
class ProfileViewTest {
    private lateinit var auth: FakeAuthenticator

    @get:Rule
    val composeTestRule = createComposeRule()

    @ExperimentalCoroutinesApi
    @Before
    fun lateInit(): Unit = runBlocking {
        auth = FakeAuthenticator()

        val context: Context = ApplicationProvider.getApplicationContext()
        val db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        val userDao = db.userDao()
        val groupDao = db.groupDao()
        val sessionDao = db.sessionDao()
        userDao.saveUser(auth.user!!)

        val mockEngine = MockEngine {
            Log.d("MOCK ENGINE", "${it.method}: ${it.url}")
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
                            Log.d("MOCK ENGINE", "respond user by fuid: ${auth.user}")
                            val user = listOf(UserLight(
                                userID = auth.user!!.userID.toLong(),
                                name = auth.user!!.name
                            ))

                            respond(
                                content = Json.encodeToString(user),
                                status = HttpStatusCode.OK,
                                headers = headersOf(HttpHeaders.ContentType, "application/json")
                            )
                        }

                        "${HttpRoutes.USERS}0/detail" -> {
                            Log.d("MOCK ENGINE", "respond user by id: ${auth.user}")
                            respond(
                                content = Json.encodeToString(auth.user),
                                status = HttpStatusCode.OK,
                                headers = headersOf(HttpHeaders.ContentType, "application/json")
                            )
                        }

                        "${HttpRoutes.USERS}0/groups" -> {
                            Log.d("MOCK ENGINE", "respond groups by uid: []")
                            respond(
                                content = Json.encodeToString(emptyList<RemoteGroup>()),
                                status = HttpStatusCode.OK,
                                headers = headersOf(HttpHeaders.ContentType, "application/json")
                            )
                        }

                        "${HttpRoutes.INSTITUTIONS}?name=KIT" -> {
                            val institutions = listOf(Institution(0, "KIT"))
                            Log.d(
                                "MOCK ENGINE",
                                "respond institutions by name KIT: ${institutions}"
                            )
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
                else -> {
                    respond(
                        content = "",
                        status = HttpStatusCode.BadRequest,
                        headers = headersOf(HttpHeaders.ContentType, "application/json")
                    )
                }
            }
        }
        val reportService = ReportService.newInstance(mockEngine) { "" }
        val userService = UserService.newInstance(mockEngine) { "" }
        val groupService = GroupService.newInstance(mockEngine) { "" }
        val sessionService = SessionService.newInstance(mockEngine) { "" }

        val groupRepo = GroupRepository.newInstance(
            GroupRepoConstructor(
                context,
                groupDao,
                auth,
                reportService,
                groupService
            )
        )
        val userRepo =
            UserRepository.newInstance(UserRepoConstructor(context, userDao, userService, auth))
        val sessionRepo = SessionRepository.newInstance(
            SessionRepoConstructor(
                context,
                sessionDao,
                auth,
                sessionService,
                reportService
            )
        )

        composeTestRule.setContent {
            MainView(userRepo, groupRepo, sessionRepo)
        }
        composeTestRule.onNodeWithContentDescription("ProfileTab").performClick()
        composeTestRule.onNodeWithContentDescription("ProfileView").assertExists()
        composeTestRule.onRoot().printToLog("PROFILE VIEW")
    }

    /**
     * test to show the profile data of a user
     * Before: user is on ProfileView
     * After: user profile information is shown correctly
     */
    @ExperimentalCoroutinesApi
    @Test
    fun fetchAndDisplayProfileDataTest() {
        composeTestRule.onNodeWithText(auth.user!!.name).assertExists()
        composeTestRule.onAllNodesWithText(auth.user!!.contact).assertCountEquals(2)
        composeTestRule.onNodeWithText(auth.user!!.college!!).assertExists()
        composeTestRule.onNodeWithText(auth.user!!.major!!).assertExists()
    }

    /**
     * /FA50/ test to log out of the application
     * Before: user is on the Profile View
     * Test: user is on the ProfileView and presses the log out button
     * After: user is on the SignInView
     */
    @ExperimentalCoroutinesApi
    @ExperimentalMaterial3Api
    @ExperimentalMaterialApi
    @Test
    fun logoutTest() {
        val logout = composeTestRule.onNode(hasTestTag("Abmelden"))
        logout.performClick()

        composeTestRule.onNodeWithContentDescription("SignInView").assertExists()
    }
}

