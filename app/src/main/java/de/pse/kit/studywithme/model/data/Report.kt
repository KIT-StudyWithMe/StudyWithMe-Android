package de.pse.kit.studywithme.model.data

import androidx.room.ColumnInfo

data class Report(
    val reportingUserID: Int,
    val reportMembership: ReportMembership,
    val membershipID: Int,
    val boxID: Int
)