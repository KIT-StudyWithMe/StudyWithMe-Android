package de.pse.kit.studywithme.model.network

import de.pse.kit.studywithme.model.data.Report
import de.pse.kit.studywithme.model.data.User
import io.ktor.client.*

class ReportServiceImpl(private val client: HttpClient): ReportService {
    override suspend fun getReports(): List<Report> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteUserReport(reportingUserID: Int, reportedUserID: Int, boxID: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteGroupReport(reportingUserID: Int, groupID: Int, boxID: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteSessionReport(reportingUserID: Int, sessionID: Int, boxID: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun reportGroup(groupID: Int, boxID: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun reportUser(userID: Int, boxID: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun reportSession(sessionID: Int, boxID: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun getBlockedUsers(): List<User> {
        TODO("Not yet implemented")
    }

    override suspend fun blockUser(uid: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun unblockUser(uid: Int) {
        TODO("Not yet implemented")
    }
}