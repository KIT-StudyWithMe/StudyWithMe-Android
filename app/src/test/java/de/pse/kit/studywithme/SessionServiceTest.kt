package de.pse.kit.studywithme

import com.google.common.truth.Truth.assertThat
import de.pse.kit.studywithme.model.data.Session
import de.pse.kit.studywithme.model.data.SessionAttendee
import de.pse.kit.studywithme.model.network.SessionService
import junit.framework.Assert.assertNotNull
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.util.*

class SessionServiceTest {

    private val service = SessionService.instance
    private var session = Session(0, 1, "Bibliothek", Date(1608336000000), 2)
    private val sessionB = Session(10, 1, "Bib", Date(1608336000000), 2)

    @Before
    fun initialize() {
        runBlocking {
            session = service.newSession(session)!!
        }
    }
    @Test
    fun sessionSerializationTest() {
        val session = Session(0, 0, "Bibliothek", Date(1608336000000), 2)
        val encodedSession = Json.encodeToString(session)
        assertThat(encodedSession).matches("\\{\"sessionId\":0,\"groupId\":0,\"location\":\"Bibliothek\",\"date\":1608336000000,\"duration\":2}")

        val decodedSession = Json.decodeFromString<Session>(encodedSession)
        assertThat(decodedSession).isEqualTo(session)
    }

    @Test
     fun createAndDeleteSession() {

        runBlocking {
            val saveSessionB = service.newSession(sessionB)
            assertNotNull(saveSessionB)
            service.removeSession(saveSessionB!!.sessionID)
            assert(service.getSession(saveSessionB.sessionID) != saveSessionB)
        }
    }
    @Test
    fun getAndEditSession() {
        runBlocking {
            val editedSession = Session(session!!.sessionID, 1, "Campus", Date(1608336000000), 2)
            service.editSession(editedSession)
            assert(service.getSession(session.sessionID) == editedSession )
        }
    }
    @Test
    fun getSessions() {
        runBlocking {
            assert(service.getSessions(session!!.groupID)!!.contains(session))

        }
    }

    @Test
    fun attendees() {
        runBlocking {
            val sessionAttendee = service.newAttendee(8, session.sessionID)
            assert (service.getAttendees(session.sessionID)!!.contains(sessionAttendee))
            service.removeAttendee(sessionAttendee!!.userID, sessionAttendee.sessionID)
            assert(!(service.getAttendees(session.sessionID)!!.contains(sessionAttendee)))
        }
    }
    @After
    fun tearDown() {
        runBlocking {
            service.removeSession(session.sessionID)
        }
    }
}