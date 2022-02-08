package de.pse.kit.studywithme.model.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import de.pse.kit.studywithme.model.network.DateSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
@Entity
data class Session(
    @PrimaryKey
    @SerialName("sessionId")
    @ColumnInfo(name = "session_ID")
    val sessionID: Int,

    @SerialName("groupId")
    @ColumnInfo(name = "group_ID")
    val groupID: Int,

    @SerialName("location")
    @ColumnInfo(name = "location")
    val location: String,

    @Serializable(with = DateSerializer::class)
    @SerialName("date")
    @ColumnInfo(name = "date")
    val date: Date,

    @SerialName("duration")
    @ColumnInfo(name = "duration")
    val duration: Int
)