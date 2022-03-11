package de.pse.kit.studywithme.model.database.ui

import android.content.Context
import android.util.Log
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import de.pse.kit.studywithme.model.auth.FakeAuthenticator
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
                            val user = UserLight(
                                userID = auth.user!!.userID.toLong(),
                                name = auth.user!!.name
                            )

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
        }

        @Test
        fun deleteAccount() {
            composeTestRule.onNodeWithContentDescription("DeleteAccountButton").performClick()
            composeTestRule.onNodeWithContentDescription("PasswordField")
                .performTextInput("password")
            composeTestRule.onNodeWithContentDescription("ConfirmButton").performClick()
            composeTestRule.onNodeWithContentDescription("SignInView").assertExists()
        }
    }