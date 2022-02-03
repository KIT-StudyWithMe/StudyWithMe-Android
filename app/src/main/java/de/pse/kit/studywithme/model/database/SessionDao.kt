package de.pse.kit.studywithme.model.database

import androidx.room.*
import de.pse.kit.studywithme.model.data.Session
import de.pse.kit.studywithme.model.data.SessionAttendee

@Dao
interface SessionDao {

    @Query("SELECT * FROM session WHERE session_ID LIKE :sessionID")
    fun getSession(sessionID: Int): Session

    @Query("SELECT * FROM session WHERE group_ID LIKE :groupID")
    fun getSessions(groupID: Int): List<Session>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveSession(session: Session)

    @Update
    fun editSession(session: Session)

    @Delete
    fun removeSession(session: Session)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveSessionAttendee(sessionAttendee: SessionAttendee)

    @Query("SELECT * FROM sessionattendee WHERE session_ID LIKE :sessionID")
    fun getSessionAttendees(sessionID: Int): List<SessionAttendee>

    @Delete
    fun removeSessionAttendee(sessionAttendee: SessionAttendee)
}