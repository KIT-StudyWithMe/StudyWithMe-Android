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
import de.pse.kit.studywithme.model.network.GroupService
import de.pse.kit.studywithme.model.network.HttpRoutes
import de.pse.kit.studywithme.model.network.SessionService
import de.pse.kit.studywithme.model.network.UserService
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
import org.junit.Rule
import org.junit.Test

@ExperimentalMaterial3Api
@ExperimentalMaterialApi
class ProfileViewTest {

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
        }
    }

    val signedInUser = mockUsers.filter { it.userID == 0 }[0]
    val mockEngine = MockEngine {
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

    @ExperimentalCoroutinesApi
    @Test
    fun newRepoTest() {
        lateinit var viewModel: ProfileViewModel
        composeTestRule.setContent {
            viewModel = ProfileViewModel(
                navController = rememberNavController(),
                userRepo = UserRepository.getInstance(
                    UserRepoConstructor(
                        context = context,
                        userDao = userDao,
                        userService = UserService.getInstance(Pair(mockEngine) { "" }),
                        auth = FakeAuthenticator()
                    )
                )
            )
            Log.d("TEST", "view")
            ProfileView(viewModel)
        }

        composeTestRule.onNodeWithContentDescription("ProfileView").assertExists()

        // Theoretisch nicht nötig aber wenn man sicher gehen will weils asynchron ist
        composeTestRule.waitUntil {
            viewModel.username != ""
        }

        composeTestRule.onNodeWithText(signedInUser.name).assertExists()
        composeTestRule.onNodeWithText(signedInUser.contact).assertExists()
        composeTestRule.onNodeWithText(signedInUser.college!!).assertExists()
        composeTestRule.onNodeWithText(signedInUser.major!!).assertExists()
    }

    /**
     * UI-Test /FA40/
     *
     */
    @ExperimentalCoroutinesApi
    @ExperimentalMaterial3Api
    @ExperimentalMaterialApi
    @Test
    fun editProfile() {
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
        editProfile.performClick()
        editCollege.performClick().performTextClearance()
        editCollege.performClick().performTextInput("KIT")
        editMajor.performClick().performTextClearance()
        editMajor.performClick().performTextInput("Info")
        editUsername.performClick().performTextClearance()
        editUsername.performClick().performTextInput("maxMustermann")
        editContact.performClick().performTextClearance()
        editContact.performClick().performTextInput("maxMustermann@mustermail.com")
        saveProfile.performClick()

    }
}

