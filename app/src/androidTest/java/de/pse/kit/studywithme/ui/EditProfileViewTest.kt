package de.pse.kit.studywithme.ui

import android.content.Context
import android.util.Log
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import de.pse.kit.studywithme.model.auth.FakeAuthenticator
import de.pse.kit.studywithme.model.data.Institution
import de.pse.kit.studywithme.model.data.Major
import de.pse.kit.studywithme.model.data.UserLight
import de.pse.kit.studywithme.model.database.AppDatabase
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

class EditProfileViewTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @ExperimentalCoroutinesApi
    @ExperimentalMaterial3Api
    @ExperimentalMaterialApi
    @Before
    fun lateInit(): Unit = runBlocking {
        val auth = FakeAuthenticator()

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
                        else -> {
                            assert(false) { "Wrong request -> ${it.method}: ${it.url}" }
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
                                content = Json.encodeToString(emptyList<UserLight>()),
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
                            assert(false) { "Wrong request -> ${it.method}: ${it.url}" }
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
                            assert(false) { "Wrong request -> ${it.method}: ${it.url}" }
                            respond(
                                content = "",
                                status = HttpStatusCode.BadRequest,
                                headers = headersOf(HttpHeaders.ContentType, "application/json")
                            )
                        }
                    }
                HttpMethod.Delete -> {
                    when (it.url.toString()) {
                        "${HttpRoutes.USERS}0" -> {
                            Log.d("MOCK ENGINE", "respond delete user: ")
                            respond(
                                content = "",
                                status = HttpStatusCode.BadRequest,
                                headers = headersOf(HttpHeaders.ContentType, "application/json")
                            )
                        }
                        else -> {
                            assert(false) { "Wrong request -> ${it.method}: ${it.url}" }
                            respond(
                                content = "",
                                status = HttpStatusCode.BadRequest,
                                headers = headersOf(HttpHeaders.ContentType, "application/json")
                            )
                        }
                    }
                }
                else -> {
                    assert(false) { "Wrong request -> ${it.method}: ${it.url}" }
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
            MainView(userRepo = userRepo, sessionRepo = sessionRepo, groupRepo = groupRepo)
        }
        composeTestRule.onNodeWithContentDescription("ProfileTab").performClick()
        composeTestRule.onNodeWithContentDescription("EditProfileButton").performClick()
        composeTestRule.onNodeWithContentDescription("EditProfileView").assertExists("Navigation to Edit Profile View failed.")
    }

    /**
     * /FA40/ test to edit the own profile of a user
     * Before: user is on EditProfileView
     * Test: user performs the input in the text fields and presses the safe button
     * After: user is on ProfileView
     */
    @ExperimentalCoroutinesApi
    @ExperimentalMaterial3Api
    @ExperimentalMaterialApi
    @Test
    fun editProfileTest() {
        val editCollege = composeTestRule.onNode(hasTestTag("Uni"))
        val editMajor = composeTestRule.onNode(hasTestTag("Studiengang"))
        val editUsername = composeTestRule.onNode(hasTestTag("Nutzername"))
        val editContact = composeTestRule.onNode(hasTestTag("Kontaktinfo"))
        val saveProfile = composeTestRule.onNode(hasTestTag("Speichern"))

        editCollege.performClick().performTextClearance()
        editCollege.performClick().performTextInput("KIT")

        editMajor.performClick().performTextClearance()
        editMajor.performClick().performTextInput("Info")

        editUsername.performClick().performTextClearance()
        editUsername.performClick().performTextInput("maxMustermann")

        editContact.performClick().performTextClearance()
        editContact.performClick().performTextInput("maxMustermann@mustermail.com")

        saveProfile.performClick()

        composeTestRule.onNodeWithContentDescription("ProfileView").assertExists()
    }

    /**
     * /FA60/ Test to delete the user data
     * Before: user is on EditProfileView
     * After: user is on SignInVIew
     */
    @Test
    fun deleteAccountTest() {
        composeTestRule.onNodeWithContentDescription("DeleteAccountButton").performClick()
        composeTestRule.onNodeWithContentDescription("PasswordField")
            .performTextInput("password")
        composeTestRule.onNodeWithContentDescription("ConfirmButton").performClick()
        composeTestRule.onNodeWithContentDescription("SignInView").assertExists()
    }
}