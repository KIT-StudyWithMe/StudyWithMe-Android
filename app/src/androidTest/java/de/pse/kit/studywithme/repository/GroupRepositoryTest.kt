package de.pse.kit.studywithme.repository

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import de.pse.kit.studywithme.model.auth.FakeAuthenticator
import de.pse.kit.studywithme.model.database.AppDatabase
import de.pse.kit.studywithme.model.network.GroupService
import de.pse.kit.studywithme.model.network.HttpRoutes
import de.pse.kit.studywithme.model.network.ReportService
import de.pse.kit.studywithme.model.repository.GroupRepoConstructor
import de.pse.kit.studywithme.model.repository.GroupRepository

import io.ktor.client.engine.mock.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class GroupRepositoryTest {
    lateinit var repo: GroupRepository
    lateinit var engine: MockEngine

    @Before
    fun setUp() {
        val auth = FakeAuthenticator()
        val context: Context = ApplicationProvider.getApplicationContext()
        val db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        val dao = db.groupDao()

        engine = MockEngine {
            Log.d("MOCK ENGINE", "${it.method}: ${it.url}")

            if (it.url.toString() == "${HttpRoutes.SESSIONS}0/report/0"
                && it.method == HttpMethod.Put
            ) {
                Log.d("MOCK ENGINE", "PUT sessions report response: ")

                respond(
                    content = "",
                    status = HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                )
            }
            else {
                assert(false) {
                    "SessionRepository calls false service method. Service requests -> ${it.method}: ${it.url}"
                }
                respond(
                    content = "",
                    status = HttpStatusCode.BadRequest,
                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                )
            }
        }

        val groupService = GroupService.newInstance(engine) { "" }
        val reportService = ReportService.newInstance(engine) { "" }
        repo = GroupRepository.newInstance(
            GroupRepoConstructor(
                context = context,
                groupDao = dao,
                groupService = groupService,
                reportService = reportService,
                auth = auth
            )
        )
    }
    @Test
    fun newMember(): Unit = runBlocking{
        repo.newMember(0, 0)
    }
    @Test
    fun leaveGroup(): Unit = runBlocking{
        repo.leaveGroup(0)
    }
    @Test
    fun getGroupSuggestion(): Unit = runBlocking {
        repo.getGroupSuggestions()
    }
    @Test
    fun declineMember(): Unit = runBlocking {
        repo.declineMember(0,0)
    }
    @Test
    fun getLectures(): Unit = runBlocking {
        repo.getLectures("")
    }
    @Test
    fun exitGroup(): Unit = runBlocking {
        repo.exitGroup(0, 0)
    }
}