package de.pse.kit.studywithme.repository

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import de.pse.kit.studywithme.model.auth.FakeAuthenticator
import de.pse.kit.studywithme.model.data.Institution
import de.pse.kit.studywithme.model.data.Major
import de.pse.kit.studywithme.model.database.AppDatabase
import de.pse.kit.studywithme.model.network.HttpRoutes
import de.pse.kit.studywithme.model.network.UserService
import de.pse.kit.studywithme.model.repository.UserRepoConstructor
import de.pse.kit.studywithme.model.repository.UserRepository
import io.ktor.client.engine.mock.*
import io.ktor.http.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Before
import org.junit.Test

class UserRepositoryTest {
    lateinit var repo: UserRepository
    lateinit var engine: MockEngine

    @Before
    fun setUp() {
        val auth = FakeAuthenticator()
        val context: Context = ApplicationProvider.getApplicationContext()
        val db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        val dao = db.userDao()

        engine = MockEngine {
            Log.d("MOCK ENGINE", "${it.method}: ${it.url}")

            if (it.url.toString() == "${HttpRoutes.MAJORS}?name=Informatik"
                && it.method == HttpMethod.Get
            ) {
                val majors = listOf(
                    Major(majorID = 0, name = "Informatik B.Sc."),
                    Major(majorID = 1, name = "Informatik M.Sc.")
                )
                Log.d("MOCK ENGINE", "GET majors response: $majors")

                respond(
                    content = Json.encodeToString(majors),
                    status = HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                )
            } else if (it.url.toString() == "${HttpRoutes.INSTITUTIONS}?name=Karlsruhe"
                && it.method == HttpMethod.Get
            ) {
                val institutions = listOf(
                    Institution(institutionID = 0, name = "Karlsruher Institut für Technologie"),
                    Institution(institutionID = 1, name = "Karlsruher Hochschule")
                )
                Log.d("MOCK ENGINE", "GET institutions response: $institutions")

                respond(
                    content = Json.encodeToString(institutions),
                    status = HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                )
            } else if (it.url.toString() == "${HttpRoutes.USERS}0"
                && it.method == HttpMethod.Delete
            ) {
                Log.d("MOCK ENGINE", "Delete user response: ")
                respond(
                    content = "",
                    status = HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                )
            } else {
                assert(false) {
                    "UserRepository calls false service method. Service requests -> ${it.method}: ${it.url}"
                }
                respond(
                    content = "",
                    status = HttpStatusCode.BadRequest,
                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                )
            }
        }

        val service = UserService.newInstance(engine) { "" }
        repo = UserRepository.newInstance(
            UserRepoConstructor(
                context = context, userDao = dao, userService = service, auth = auth
            )
        )
    }

    @Test
    fun getMajorsTest(): Unit = runBlocking {
        val prefix = "Informatik"
        val majors = repo.getMajors(prefix)
        majors.forEach { assert(it.startsWith(prefix)) { "UserRepository returns majors with wrong prefix." } }
    }

    @Test
    fun getInstitutionsTest(): Unit = runBlocking {
        val prefix = "Karlsruhe"
        val institutions = repo.getColleges(prefix)
        institutions.forEach { assert(it.startsWith(prefix)) { "UserRepository returns institutions with wrong prefix." } }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun deleteAccountTest() = runBlocking {
        val success = repo.deleteAccount("password")
        assert(success)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun deleteAccountWithWrongMailTest() = runBlocking {
        val success = repo.deleteAccount("wrongpassword")
        assert(!success)
    }
}