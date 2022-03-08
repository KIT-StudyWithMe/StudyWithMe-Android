package de.pse.kit.studywithme

import com.google.common.truth.Truth.assertThat
import de.pse.kit.studywithme.model.data.Session
import de.pse.kit.studywithme.model.network.SessionService
import junit.framework.Assert.assertNotNull
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Ignore
import org.junit.Test
import java.util.*

class SessionServiceTest {
    /*

    private val service = SessionService.getInstance(Pair())
    private var sessionTest = Session(5, 1, "Bibliothek", Date(1608336000000), 2)
    private val sessionB = Session(30, 1, "Bib", Date(1608336000000), 2)
    @Ignore
    @Test
    fun sessionSerializationTest() {
        val session = Session(0, 1, "Bibliothek", Date(1608336000000), 2)
        val encodedSession = Json.encodeToString(session)
        assertThat(encodedSession).matches("\\{\"sessionId\":0,\"groupId\":0,\"location\":\"Bibliothek\",\"date\":1608336000000,\"duration\":2}")

        val decodedSession = Json.decodeFromString<Session>(encodedSession)
        assertThat(decodedSession).isEqualTo(session)
    }
    @Ignore
    @Test
     fun createAndDeleteSession() {

        runBlocking {
            val saveSessionB = service.newSession(sessionB)
            assertNotNull(saveSessionB)
            assert(service.getSession(saveSessionB!!.sessionID) == saveSessionB)
            service.removeSession(saveSessionB!!.sessionID)
        }
    }
    @Ignore
    @Test
    fun getAndEditSession() {
        runBlocking {
            sessionTest = service.newSession(sessionTest)!!
        }
    }
    @Ignore
    @Test
    fun getSessions() {
        runBlocking {
            val sessionX = service.newSession(sessionTest)
           println(service.getSessions(sessionX!!.groupID))
            println(service.getSession(sessionX!!.sessionID))
            service.removeSession(sessionX.sessionID)
            println(service.getSessions(sessionX!!.groupID))
            println(service.getSession(sessionX!!.sessionID))

        }
    }
    */


}