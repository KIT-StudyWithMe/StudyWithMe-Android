package de.pse.kit.studywithme.model.network

import de.pse.kit.studywithme.model.data.Session
import io.ktor.client.*

class SessionServiceImpl(private val client: HttpClient): SessionService {
    override suspend fun getSessions(groupID: Int): List<Session> {
        TODO("Not yet implemented")
    }

    override suspend fun newSession(session: Session): Session? {
        TODO("Not yet implemented")
    }

    override suspend fun editSession(session: Session): Session? {
        TODO("Not yet implemented")
    }

    override suspend fun removeSession(sessionID: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun newParticipant(sessionID: Int): Session? {
        TODO("Not yet implemented")
    }

}
