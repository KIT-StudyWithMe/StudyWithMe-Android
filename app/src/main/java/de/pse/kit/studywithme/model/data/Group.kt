package de.pse.kit.studywithme.model.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Group(
    @PrimaryKey
    @ColumnInfo(name = "group_ID")
    val groupID: Int,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "lecture_ID")
    val lectureID: Int,

    @ColumnInfo(name = "description")
    val description: String?,

    @ColumnInfo(name = "session_frequency")
    val sessionFrequency: SessionFrequency?,

    @ColumnInfo(name = "session_type")
    val sessionType: SessionType?,

    @ColumnInfo(name = "lecture_chapter")
    val lectureChapter: Int?,

    @ColumnInfo(name = "exercise")
    val exercise: Int?,

    @ColumnInfo(name = "group_admin_ID")
    val groupAdminID: Int,

    @ColumnInfo(name = "participants_sum")
    val participantsSum: Int
)