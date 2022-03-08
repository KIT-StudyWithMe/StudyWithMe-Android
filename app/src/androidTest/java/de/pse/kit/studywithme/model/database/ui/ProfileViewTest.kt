package de.pse.kit.studywithme.model.database.ui

import android.content.Context
import android.util.Log
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import de.pse.kit.studywithme.model.auth.FakeAuthenticator
import de.pse.kit.studywithme.model.data.*
import de.pse.kit.studywithme.model.database.AppDatabase
import de.pse.kit.studywithme.model.database.UserDao
import de.pse.kit.studywithme.model.network.HttpRoutes
import de.pse.kit.studywithme.model.network.UserService
import de.pse.kit.studywithme.model.repository.*
import de.pse.kit.studywithme.ui.view.profile.ProfileView
import de.pse.kit.studywithme.viewModel.profile.ProfileViewModel
import de.pse.kit.studywithme.viewModel.profile.ProfileViewModelFactory
import io.ktor.client.engine.mock.*
import io.ktor.http.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalMaterial3Api
@ExperimentalMaterialApi
class ProfileViewTest {

    lateinit var context: Context
    lateinit var userDao: UserDao
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
            mockUsers.map { userDao.saveUser(it) }
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun newRepoTest() {
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

        lateinit var viewModel: ProfileViewModel
        composeTestRule.setContent {
            viewModel = ProfileViewModel(
                navController = rememberNavController(),
                userRepo = UserRepository.getInstance(
                    UserRepoConstructor(
                        context = context,
                        userDao = userDao,
                        userService = UserService.getInstance(Pair(mockEngine) { "" }),
                        auth = FakeAuthenticator
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
}
