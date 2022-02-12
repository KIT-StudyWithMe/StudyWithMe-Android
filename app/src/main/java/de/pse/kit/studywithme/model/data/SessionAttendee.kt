package de.pse.kit.studywithme.model.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Data class for the object sessionattendee
 *
 * @property sessionAttendeeID
 * @property sessionID
 * @property userID
 * @property participates
 * @constructor Create empty Session attendee
 */
@Serializable
@Entity
data class SessionAttendee(
    @PrimaryKey
    @SerialName(value = "sessionAttendeeID")
    @ColumnInfo(name = "session_attendee_ID")
    val sessionAttendeeID: Int,

    @SerialName(value = "sessionID")
    @ColumnInfo(name = "session_ID")
    val sessionID: Int,

    @SerialName(value = "userID")
    @ColumnInfo(name = "user_ID")
    val userID: Int,

    @SerialName(value = "participates")
    @ColumnInfo(name = "participates")
    val participates: Boolean
)