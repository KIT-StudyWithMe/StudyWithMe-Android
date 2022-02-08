package de.pse.kit.studywithme.model.repository

import de.pse.kit.studywithme.model.data.Session
import de.pse.kit.studywithme.model.data.SessionAttendee
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow

interface SessionRepositoryInterface {

    @ExperimentalCoroutinesApi
    fun getSessions(groupID: Int): Flow<List<Session>>

    fun getSession(sessionID: Int): Flow<Session>

    fun newSession(session: Session): Boolean

    fun editSession(session: Session): Boolean

    fun removeSession(session: Session)

    fun newAttendee(sessionID: Int): Boolean

    fun removeAttendee(sessionID: Int)

    fun getAttendees(sessionID: Int): Flow<List<SessionAttendee>>
}