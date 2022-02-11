package de.pse.kit.studywithme.model.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Data class for the object major
 *
 * @property majorID
 * @property name
 * @constructor Create empty Major
 */
@Serializable
@Entity
data class Major (
    @PrimaryKey
    @SerialName(value = "majorID")
    @ColumnInfo(name = "major_ID")
    val majorID: Long,

    @SerialName(value = "name")
    @ColumnInfo(name = "name")
    val name: String
)

