package de.pse.kit.studywithme.model.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import de.pse.kit.studywithme.model.network.DateSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.*

/**
 * Data class of the object session
 *
 * @property sessionID
 * @property groupID
 * @property location
 * @property date
 * @property duration
 * @constructor Create empty Session
 */
@Serializable
@Entity
data class Session(
    @PrimaryKey
    @SerialName("sessionID")
    @ColumnInfo(name = "session_ID")
    val sessionID: Int,

    @SerialName("groupID")
    @ColumnInfo(name = "group_ID")
    val groupID: Int,

    @SerialName("place")
    @ColumnInfo(name = "location")
    val location: String,

    @Serializable(with = DateSerializer::class)
    @SerialName("startTime")
    @ColumnInfo(name = "date")
    val date: Date,

    @SerialName("duration")
    @ColumnInfo(name = "duration")
    val duration: Int
)