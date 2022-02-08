package de.pse.kit.studywithme.model.network

import de.pse.kit.studywithme.model.data.Session
import de.pse.kit.studywithme.model.data.SessionAttendee
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*

interface SessionService {

    suspend fun getSessions(groupID: Int): List<Session>?
    suspend fun getSession(sessionID: Int): Session?
    suspend fun newSession(session: Session): Session?
    suspend fun editSession(session: Session): Session?
    suspend fun removeSession(sessionID: Int)
    suspend fun newAttendee(userID: Int, sessionID: Int): SessionAttendee?
    suspend fun getAttendees(sessionID: Int): List<SessionAttendee>?
    suspend fun removeAttendee(userID: Int, sessionID: Int)

    companion object {
        val instance: SessionService by lazy {
            SessionServiceImpl(client = HttpClient(Android) {
                install(JsonFeature) {
                    serializer = KotlinxSerializer()
                }
            })
        }
    }
}