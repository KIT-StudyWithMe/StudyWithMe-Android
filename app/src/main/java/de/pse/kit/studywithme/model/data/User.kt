package de.pse.kit.studywithme.model.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey
    @ColumnInfo(name = "user_ID")
    val userID: Int,

    @ColumnInfo(name = "username")
    val name: String,

    @ColumnInfo(name = "college_ID")
    val collegeID: Int,

    @ColumnInfo(name = "major_ID")
    val major: Int,

    @ColumnInfo(name = "contact")
    val contact: String,

    @Ignore
    val loginUID: Int?,

    @Ignore
    val isBlocked: Boolean?,

    @Ignore
    val isMod: Boolean?
)
