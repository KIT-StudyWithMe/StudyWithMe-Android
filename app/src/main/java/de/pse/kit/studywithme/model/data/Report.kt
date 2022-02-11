package de.pse.kit.studywithme.model.data

/**
 * Data class for the object report
 *
 * @property reportingUserID
 * @property reportMembership
 * @property membershipID
 * @property boxID
 * @constructor Create empty Report
 */
data class Report(
    val reportingUserID: Int,
    val reportMembership: ReportMembership,
    val membershipID: Int,
    val boxID: Int
)