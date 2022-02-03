package de.pse.kit.studywithme.model.network

import de.pse.kit.studywithme.model.data.Session
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*

interface SessionService {

    suspend fun getSessions(groupID: Int): List<Session>
    suspend fun newSession(session: Session): Session?
    suspend fun editSession(session: Session): Session?
    suspend fun removeSession(sessionID: Int)
    suspend fun newParticipant(sessionID: Int): Session?

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