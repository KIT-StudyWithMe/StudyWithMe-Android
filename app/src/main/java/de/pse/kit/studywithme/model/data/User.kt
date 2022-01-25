package de.pse.kit.studywithme.model.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey
    @ColumnInfo(name = "user_ID") val userID: Int,
    @ColumnInfo(name = "Nutzername") val name: String?,
    @ColumnInfo(name = "ID-Universität") val collegeID: Int,
    @ColumnInfo(name = "ID-Studiengang") val major: Int,
    @ColumnInfo(name = "Kontaktinformation") val contact: String,

    @Ignore
    @ColumnInfo(name = "Login-UID") val loginUID: Int,
    @Ignore
    @ColumnInfo(name = "is-gesperrt") val isBlocked: Boolean,
    @Ignore
    @ColumnInfo(name = "moderator") val isMod: Boolean
)
