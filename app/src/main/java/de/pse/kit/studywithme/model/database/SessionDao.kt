package de.pse.kit.studywithme.model.database

import androidx.room.*
import de.pse.kit.studywithme.model.data.Session

@Dao
interface SessionDao {

    @Query("SELECT * FROM session WHERE session_ID LIKE :sessionID")
    fun getSession(sessionID: Int): Session

    @Query("SELECT * FROM session WHERE group_ID LIKE :groupID")
    fun getSessions(groupID: Int): List<Session>

    @Insert
    fun saveSession(session: Session)

    @Update
    fun editSession(session: Session)

    @Delete
    fun removeSession(session: Session)
}