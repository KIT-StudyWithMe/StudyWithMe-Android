package de.pse.kit.studywithme.model.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Data class for the object user
 *
 * @property userID
 * @property name
 * @property collegeID
 * @property college
 * @property majorID
 * @property major
 * @property contact
 * @property firebaseUID
 * @property isBLocked
 * @property isModerator
 * @constructor Create empty User
 */
@Serializable
@Entity
data class User(
    @PrimaryKey
    @SerialName("userID")
    @ColumnInfo(name = "user_ID")
    val userID: Int,

    @SerialName("name")
    @ColumnInfo(name = "username")
    val name: String,

    @SerialName("institutionID")
    @ColumnInfo(name = "college_ID")
    val collegeID: Int? = null,

    @SerialName("institutionName")
    @ColumnInfo(name = "college")
    val college: String? = null,

    @SerialName("majorID")
    @ColumnInfo(name = "major_ID")
    val majorID: Int? = null,

    @SerialName("majorName")
    @ColumnInfo(name = "major")
    val major: String? = null,

    @SerialName("contact")
    @ColumnInfo(name = "contact")
    val contact: String,

    @SerialName("firebaseUID")
    @ColumnInfo(name = "firebase_UID")
    val firebaseUID: String,

    @SerialName("isBlocked")
    @ColumnInfo(name = "is_blocked")
    val isBLocked: Boolean = false,

    @SerialName("isModerator")
    @ColumnInfo(name = "is_moderator")
    val isModerator: Boolean = false
)
