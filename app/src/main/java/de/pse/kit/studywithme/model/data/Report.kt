package de.pse.kit.studywithme.model.data

data class Report(
    val reportingUserID: Int,
    val reportMembership: ReportMembership,
    val membershipID: Int,
    val boxID: Int
)