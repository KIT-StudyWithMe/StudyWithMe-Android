package de.pse.kit.studywithme.model.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Entity
data class User(
    @PrimaryKey
    @SerialName("userID")
    @ColumnInfo(name = "user_ID")
    val userID: Int,

    @SerialName("userName")
    @ColumnInfo(name = "username")
    val name: String,

    @SerialName("collegeID")
    @ColumnInfo(name = "college_ID")
    val collegeID: Int? = null,

    @SerialName("college")
    @ColumnInfo(name = "college")
    val college: String? = null,

    @SerialName("majorID")
    @ColumnInfo(name = "major_ID")
    val majorID: Int? = null,

    @SerialName("major")
    @ColumnInfo(name = "major")
    val major: String? = null,

    @SerialName("contact")
    @ColumnInfo(name = "contact")
    val contact: String,

    @SerialName("firebaseUID")
    @ColumnInfo(name = "firebase_UID")
    val firebaseUID: String
)
