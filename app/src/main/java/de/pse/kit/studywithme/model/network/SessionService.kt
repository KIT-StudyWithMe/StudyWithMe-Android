package de.pse.kit.studywithme.model.network

import de.pse.kit.studywithme.model.data.Session
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*

interface SessionService {

    suspend fun getSessions(groupID: Int): List<Session>
    suspend fun newSession(session: Session)
    suspend fun editSession(sessionID: Int, session: Session)
    suspend fun removeSession(sessionID: Int)
    suspend fun newParticipant(sessionID: Int)

    companion object {
        var service: SessionServiceImpl? = null

        fun create(): SessionServiceImpl {
            if (service == null) {
                service = SessionServiceImpl(client = HttpClient(Android) {
                    install(JsonFeature) {
                        serializer = KotlinxSerializer()
                    }
                })
            }
            return service!!
        }
    }
}