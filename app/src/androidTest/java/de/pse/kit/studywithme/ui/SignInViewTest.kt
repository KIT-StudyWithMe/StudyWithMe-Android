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
import java.lang.Thread.sleep

@ExperimentalMaterial3Api
@ExperimentalMaterialApi
class SignInViewTest {
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
            // set SignInView(MainView)
            composeTestRule.setContent {
                MainView(
                    userRepo = userRepo,
                    groupRepo = groupRepo,
                    sessionRepo = sessionRepo
                )
            }
            composeTestRule.onRoot().printToLog("SIGN_IN_VIEW")
        }
    }

    /**
     * test to check if an error is displayed when trying to sign in with non existing email
     * Before: user is not signed in and on the SignInView
     * Test: user writes input in the text fields for e-mail and passwort which doesn't belong to an account
     * After: error message is shown
     */
    @Test
    fun displayErrorAfterSignInWithNonExistingUser() {
        val buttonEmail = composeTestRule.onNode(hasTestTag("Email-Adresse"))
        val buttonPw = composeTestRule.onNode(hasTestTag("Passwort"))
        val buttonLogin = composeTestRule.onNode(hasTestTag("Anmelden"))

        //LogIn of a non-existing and existing User
        composeTestRule.onNodeWithContentDescription("ErrorMessage").assertDoesNotExist()
        buttonEmail.performScrollTo()
        buttonEmail.performTextInput("hort2@lichter.de")
        buttonPw.performScrollTo()
        buttonPw.performTextInput("passworthorst98")
        buttonLogin.performScrollTo()
        buttonLogin.performClick()
        composeTestRule.onNodeWithContentDescription("ErrorMessage").assertExists()

    }

    /**
     * /FA20/ test to check if the sign in works with the data of an existing account
     * Before: user is not signed in and on the SignInView,
     * an acount with mail "user@mail.com" and password "password" exists
     * Test: user writes "user@mail.com" in the mail text field and "password" in the password field
     * After: user is now in the JoinedGroupsView
     */
    @Test
    fun signInWithExistingUser() {
        val buttonEmail = composeTestRule.onNode(hasTestTag("Email-Adresse"))
        val buttonPw = composeTestRule.onNode(hasTestTag("Passwort"))
        val buttonLogin = composeTestRule.onNode(hasTestTag("Anmelden"))

        buttonEmail.performScrollTo()
        buttonEmail.performClick().performTextInput("user@mail.com")
        buttonPw.performScrollTo()
        buttonPw.performClick().performTextClearance()
        buttonPw.performClick().performTextInput("password")
        buttonLogin.performScrollTo()
        buttonLogin.performClick()
        sleep(1000)
        composeTestRule.onNodeWithContentDescription("JoinedGroupsView")
            .assertExists("Sign in failed.")
    }

    /**
     * /FA30/ test to check the functionality in case of a forgotten password
     * Before: user is not signed in and on the SignInView,
     * an account with mail "dieter.bohlen@dsds.de" exists
     * Test: user presses the forget password button,
     * user writes "dieter.bohlen@dsds.de" in the mail text field,
     * user presses the forget password button
     * After: an error message "Eine Email zum zurücksetzen wurde gesendet" is shown
     * and the user is still in the sign in view
     */
    @Test
    fun forgotPw() {
        val buttonPwForget = composeTestRule.onNode(hasTestTag("Passwort vergessen"))
        val buttonEmail = composeTestRule.onNode(hasTestTag("Email-Adresse"))

        //Forgot Pw Button is being pressed
        buttonPwForget.performScrollTo()
        buttonPwForget.performClick()
        buttonEmail.performScrollTo()
        buttonEmail.performClick().performTextInput("dieter.bohlen@dsds.de")
        buttonPwForget.performScrollTo()
        buttonPwForget.performClick()
        composeTestRule.onNodeWithText("Eine Email zum zurücksetzen wurde gesendet").assertExists()
        composeTestRule.onNodeWithContentDescription("SignInView").assertExists()
    }
}