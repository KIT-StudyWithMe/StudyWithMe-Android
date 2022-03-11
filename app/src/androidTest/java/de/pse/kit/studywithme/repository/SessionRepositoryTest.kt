package de.pse.kit.studywithme.repository

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import de.pse.kit.studywithme.model.auth.FakeAuthenticator
import de.pse.kit.studywithme.model.data.Institution
import de.pse.kit.studywithme.model.data.Major
import de.pse.kit.studywithme.model.data.Session
import de.pse.kit.studywithme.model.data.SessionField
import de.pse.kit.studywithme.model.database.AppDatabase
import de.pse.kit.studywithme.model.network.HttpRoutes
import de.pse.kit.studywithme.model.network.ReportService
import de.pse.kit.studywithme.model.network.SessionService
import de.pse.kit.studywithme.model.repository.SessionRepoConstructor
import de.pse.kit.studywithme.model.repository.SessionRepository
import io.ktor.client.engine.mock.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Before
import org.junit.Test
import java.util.*

class SessionRepositoryTest {
    lateinit var repo: SessionRepository
    lateinit var engine: MockEngine

    @Before
    fun setUp() {
        val auth = FakeAuthenticator()
        val context: Context = ApplicationProvider.getApplicationContext()
        val db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        val dao = db.sessionDao()

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
            } else if (it.url.toString() == "${HttpRoutes.SESSIONS}0/participate/0"
                && it.method == HttpMethod.Put
            ) {
                Log.d("MOCK ENGINE", "PUT session attendee false response: ")

                respond(
                    content = "",
                    status = HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                )
            } else if (it.url.toString() == "${HttpRoutes.SESSIONS}0"
                && it.method == HttpMethod.Delete
            ) {
                Log.d("MOCK ENGINE", "DELETE session response: ")

                respond(
                    content = "",
                    status = HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                )
            } else {
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

        val sessionService = SessionService.newInstance(engine) { "" }
        val reportService = ReportService.newInstance(engine) { "" }
        repo = SessionRepository.newInstance(
            SessionRepoConstructor(
                context = context,
                sessionDao = dao,
                sessionService = sessionService,
                reportService = reportService,
                auth = auth
            )
        )
    }

    @Test
    fun reportSessionTest(): Unit = runBlocking {
        repo.reportSession(0, SessionField.PLACE)
    }

    @Test
    fun removeSessionAttendeeTest() = runBlocking {
        repo.removeAttendee(0)
    }

    @Test
    fun removeSessionTest() = runBlocking {
        repo.removeSession(Session(sessionID = 0, groupID = 0, location = "Hier", date = Date(), duration = 1))
    }
}