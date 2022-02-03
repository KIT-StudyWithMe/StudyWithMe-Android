package de.pse.kit.studywithme.model.data

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(primaryKeys = ["session_ID", "user_ID"])
data class SessionAttendee(
    @ColumnInfo(name = "session_ID")
    val sessionID: Int,

    @ColumnInfo(name = "user_ID")
    val userID: Int,

    @ColumnInfo(name = "participates")
    val participates: Boolean
)