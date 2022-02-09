package de.pse.kit.studywithme

import com.google.common.truth.Truth.assertThat
import de.pse.kit.studywithme.model.data.Session
import de.pse.kit.studywithme.model.network.SessionService
import de.pse.kit.studywithme.model.network.SessionServiceImpl
import io.ktor.client.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Test
import java.util.*

class SessionServiceTest {

    private val service = SessionService.instance

    @Test
    fun sessionSerializationTest() {
        val session = Session(0, 0, "Bibliothek", Date(1608336000000), 2)
        val encodedSession = Json.encodeToString(session)
        assertThat(encodedSession).matches("\\{\"sessionId\":0,\"groupId\":0,\"location\":\"Bibliothek\",\"date\":1608336000000,\"duration\":2}")

        val decodedSession = Json.decodeFromString<Session>(encodedSession)
        assertThat(decodedSession).isEqualTo(session)
    }

    @Test
     fun createSession() {
        val session = Session(0, 0, "Bibliothek", Date(1608336000000), 2)

        runBlocking {
            val saveSession = service.newSession(session)
            println(saveSession)
        }
    }
}