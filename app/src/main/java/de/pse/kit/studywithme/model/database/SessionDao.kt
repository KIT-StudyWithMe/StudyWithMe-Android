package de.pse.kit.studywithme.model.database

import androidx.room.*
import de.pse.kit.studywithme.model.data.Session
import de.pse.kit.studywithme.model.data.SessionAttendee

@Dao
interface SessionDao {

    @Query("SELECT * FROM session WHERE session_ID LIKE :sessionID")
    suspend fun getSession(sessionID: Int): Session

    @Query("SELECT * FROM session WHERE group_ID LIKE :groupID")
    suspend fun getSessions(groupID: Int): List<Session>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveSession(session: Session)

    @Update
    suspend fun editSession(session: Session)

    @Delete
    suspend fun removeSession(session: Session)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveSessionAttendee(sessionAttendee: SessionAttendee)

    @Query("SELECT * FROM sessionattendee WHERE session_ID LIKE :sessionID")
    suspend fun getSessionAttendees(sessionID: Int): List<SessionAttendee>

    @Delete
    suspend fun removeSessionAttendee(sessionAttendee: SessionAttendee)
}