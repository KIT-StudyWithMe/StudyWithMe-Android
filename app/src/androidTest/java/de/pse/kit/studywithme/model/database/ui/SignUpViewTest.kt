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

@ExperimentalMaterial3Api
@ExperimentalMaterialApi
class SignUpViewTest {
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

    @get:Rule
    val composeTestRule = createComposeRule()

    @ExperimentalCoroutinesApi
    @Before
    fun lateinit() {
        runBlocking {
            auth = FakeAuthenticator()
            auth.signOut()
            mockEngine = MockEngine {
                Log.d("MOCK ENGINE", "${it.method}: ${it.url}")
                when (it.method) {
                    HttpMethod.Post ->
                        when (it.url.toString()) {
                            HttpRoutes.USERS -> {
                                val outgoingUser = ByteReadChannel(it.body.toByteArray())
                                Log.d("MOCK ENGINE", "outgoing lecture: $outgoingUser")

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
                    HttpMethod.Get ->
                        when (it.url.toString()) {
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
            groupDao = db.groupDao()
            sessionDao = db.sessionDao()

            val reportService = ReportService.newInstance(mockEngine) { "" }
            val userService = UserService.newInstance(mockEngine) { "" }
            val groupService = GroupService.newInstance(mockEngine) { "" }
            val sessionService = SessionService.newInstance(mockEngine) { "" }

            groupRepo = GroupRepository.newInstance(groupDao, auth, reportService, groupService)
            userRepo = UserRepository.newInstance(userDao, userService, auth)
            sessionRepo =
                SessionRepository.newInstance(sessionDao, auth, sessionService, reportService)

            composeTestRule.setContent {
                MainView(
                    userRepo = userRepo,
                    groupRepo = groupRepo,
                    sessionRepo = sessionRepo
                )
            }
            composeTestRule.onNode(hasTestTag("RegistrierenSignIn")).performClick()
            composeTestRule.onNodeWithContentDescription("SignUpView")
        }
    }

    /**
     * UI-Test FA10
     *
     */
    @Test
    fun signUp() {
        composeTestRule.onNode(hasTestTag("Email-AdresseSignUp"))
            .performTextInput("hort2@lichter.de")
        composeTestRule.onNode(hasTestTag("NutzernameSignUp"))
            .performTextInput("Horst")
        composeTestRule.onNode(hasTestTag("UniSignUp"))
            .performClick().performTextInput("KIT")
        composeTestRule.onNode(hasTestTag("LectureSignUp"))
            .performTextInput("Info")
        composeTestRule.onNode(hasTestTag("PwSignUp"))
            .performTextInput("Mind6Zeichen")
        composeTestRule.onNode(hasTestTag("SignUp"))
            .performClick()

        composeTestRule.onNodeWithContentDescription("SearchGroupsView").assertExists("Sign Up failed.")
    }

}