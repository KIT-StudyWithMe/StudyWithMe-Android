package de.pse.kit.studywithme.model.network

import de.pse.kit.studywithme.model.data.Session
import de.pse.kit.studywithme.model.data.SessionAttendee
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

    override suspend fun newAttendee(userID: Int, sessionID: Int): SessionAttendee? {
        TODO("Not yet implemented")
    }

    override suspend fun getAttendees(sessionID: Int): List<SessionAttendee>? {
        TODO("Not yet implemented")
    }

    override suspend fun removeAttendee(userID: Int, sessionID: Int) {
        TODO("Not yet implemented")
    }
}
