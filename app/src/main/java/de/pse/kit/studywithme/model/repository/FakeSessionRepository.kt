package de.pse.kit.studywithme.model.repository


import de.pse.kit.studywithme.GeneratedExclusion
import de.pse.kit.studywithme.model.data.Session
import de.pse.kit.studywithme.model.data.SessionAttendee
import de.pse.kit.studywithme.model.data.SessionField
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.*

/**
 * Repository class for internal tests of session components
 *
 * @constructor Create empty Fake session repository
 */
@GeneratedExclusion
class FakeSessionRepository() : SessionRepositoryInterface {
    var session = Session(
        sessionID = 0,
        groupID = 1,
        location = "Bibliothek",
        date = Date(),
        duration = 2
    )
    var sessionAttendee = SessionAttendee(
        sessionAttendeeID = 0,
        sessionID = 0,
        userID = 0,
        participates = true
    )
    var secondSessionAttendee = SessionAttendee(
        sessionAttendeeID = 1,
        sessionID = 0,
        userID = 1,
        participates = true
    )

    @ExperimentalCoroutinesApi
    override suspend fun getSessions(groupID: Int): Flow<List<Session>> {
        return flow { emit(listOf(session)) }
    }

    override suspend fun getSession(sessionID: Int): Flow<Session> {
        return flow { emit(session) }
    }

    override suspend fun newSession(session: Session): Boolean {
        this.session = session
        return true
    }

    override suspend fun editSession(session: Session): Boolean {
        this.session = session
        return true
    }

    override suspend fun removeSession(session: Session) {
        this.session = session
        return
    }

    override suspend fun newAttendee(sessionID: Int): Boolean {
        return true
    }

    override suspend fun removeAttendee(sessionID: Int) {

    }

    override suspend fun getAttendees(sessionID: Int): Flow<List<SessionAttendee>> {
        return flow { emit(listOf(sessionAttendee)) }
    }



    override suspend fun reportSession(sessionID: Int, sessionfield: SessionField) {
        TODO("Not yet implemented")
    }
}