package de.pse.kit.studywithme.model.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Group (
    @PrimaryKey @ColumnInfo(name = "group_ID") val group_ID: Int,
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "lecture_ID") val lecture_ID: Int?,
    @ColumnInfo(name = "description") val description: String?,
    @ColumnInfo(name = "sessionFrequency") val sessionFrequency: SessionFrequency?,
    @ColumnInfo(name = "sessionType") val sessionType: SessionType?,
    @ColumnInfo(name = "lectureChapter") val lectureChapter: Int?,
    @ColumnInfo(name = "exercise") val exercise: Int?,
    @ColumnInfo(name = "groupAdmin_ID") val groupAdmin_ID: Int?,
    @ColumnInfo(name = "participantsSum") val participantsSum: Int?,
        )