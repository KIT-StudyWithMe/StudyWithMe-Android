package de.pse.kit.studywithme.model.repository

import de.pse.kit.studywithme.model.data.Session
import de.pse.kit.studywithme.model.data.SessionAttendee
import de.pse.kit.studywithme.model.data.SessionField
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow

interface SessionRepositoryInterface {

    /**
     * Returns list of session of the group with ID groupID
     *
     * @param groupID
     * @return flow list of session object
     */
    @ExperimentalCoroutinesApi
    fun getSessions(groupID: Int): Flow<List<Session>>

    /**
     * Returns a session with the ID sessionID
     *
     * @param sessionID
     * @return flow session object
     */
    fun getSession(sessionID: Int): Flow<Session>

    /**
     * Creates a new session with the given session object and returns true if successful
     *
     * @param session
     * @return true or false
     */
    fun newSession(session: Session): Boolean

    /**
     * Edits group in database with the given edited session object and returns true if successful
     *
     * @param session
     * @return true or false
     */
    fun editSession(session: Session): Boolean

    /**
     * Removes the given session
     *
     * @param session
     */
    fun removeSession(session: Session)

    /**
     * Creates a new attendant of the session with ID sessionID and returns true if successful
     *
     * @param sessionID
     * @return true or false
     */
    fun newAttendee(sessionID: Int): Boolean

    /**
     * Removes an attendant of the session with ID sessionID
     *
     * @param sessionID
     */
    fun removeAttendee(sessionID: Int)

    /**
     * Returns all attendants which attend the session with the ID sessionID
     *
     * @param sessionID
     * @return flow list of sessionattendee object
     */
    fun getAttendees(sessionID: Int): Flow<List<SessionAttendee>>

    /**
     * Creates a detailed report of the session with ID sessionID
     *
     * @param sessionID
     * @param sessionfield
     */
    fun reportSession(sessionID: Int, sessionfield: SessionField)
}