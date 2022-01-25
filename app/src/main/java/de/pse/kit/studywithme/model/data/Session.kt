package de.pse.kit.studywithme.model.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Session(
    @PrimaryKey
    @ColumnInfo(name = "session_ID")
    val sessionID: Int,

    @ColumnInfo(name = "group_ID")
    val groupID: Int,

    @ColumnInfo(name = "location")
    val location: String,

    @ColumnInfo(name = "date")
    val date: Date,

    @ColumnInfo(name = "duration")
    val duration: Int
)