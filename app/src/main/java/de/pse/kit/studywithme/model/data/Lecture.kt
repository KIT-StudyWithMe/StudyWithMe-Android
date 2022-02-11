package de.pse.kit.studywithme.model.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Data class for the object lecture
 *
 * @property lectureID
 * @property lectureName
 * @property majorID
 * @constructor Create empty Lecture
 */
@Serializable
@Entity(indices = [Index(value = ["lecture_name"])])
data class Lecture(
    @PrimaryKey
    @SerialName(value = "lectureID")
    @ColumnInfo(name = "lecture_ID")
    val lectureID: Int,

    @SerialName(value = "name")
    @ColumnInfo(name = "lecture_name")
    val lectureName: String,

    @SerialName(value = "majorID")
    @ColumnInfo(name = "major_ID")
    val majorID: Int
)