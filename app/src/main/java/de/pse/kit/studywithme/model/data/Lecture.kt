package de.pse.kit.studywithme.model.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["lecture_name"])])
data class Lecture(
    @PrimaryKey
    @ColumnInfo(name = "lecture_ID")
    val lectureID: Int,

    @ColumnInfo(name = "lecture_name")
    val lectureName: String
)