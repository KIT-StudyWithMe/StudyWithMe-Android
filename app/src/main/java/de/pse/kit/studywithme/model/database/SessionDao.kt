package de.pse.kit.studywithme.model.database

import androidx.room.*
import de.pse.kit.studywithme.model.data.Session
import de.pse.kit.studywithme.model.data.SessionAttendee

/**
 * Interface for management of local database for object session
 *
 * @constructor Create empty Session dao
 */
@Dao
interface SessionDao {

    /**
     * Returns the session with the ID sessionID or null if there is none with this ID
     *
     * @param sessionID
     * @return Session object or null
     */
    @Query("SELECT * FROM session WHERE session_ID LIKE :sessionID")
    suspend fun getSession(sessionID: Int): Session?

    /**
     * Returns a list of all the sessions the group with groupID has planned yet or null if they haven't planned any
     *
     * @param groupID
     * @return List of session objects or null
     */
    @Query("SELECT * FROM session WHERE group_ID LIKE :groupID")
    suspend fun getSessions(groupID: Int): List<Session>?

    /**
     * Saves a session in the database
     *
     * @param session
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveSession(session: Session)

    /**
     * Edits a existing session in the database
     *
     * @param session
     */
    @Update
    suspend fun editSession(session: Session)

    /**
     * Removes a session from the database
     *
     * @param session
     */
    @Delete
    suspend fun removeSession(session: Session)

    /**
     * Saves groupmembers which attend a planned session
     *
     * @param sessionAttendee
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveSessionAttendee(sessionAttendee: SessionAttendee)

    /**
     * Returns a list of all attendants of a planned session or null if there are no attendants
     *
     * @param sessionID
     * @return List of sessionattendee objects or null
     */
    @Query("SELECT * FROM sessionattendee WHERE session_ID LIKE :sessionID")
    suspend fun getSessionAttendees(sessionID: Int): List<SessionAttendee>?

    /**
     * Removes a attendant of a planned session on which he planned to participate
     *
     * @param sessionAttendee
     */
    @Delete
    suspend fun removeSessionAttendee(sessionAttendee: SessionAttendee)
}