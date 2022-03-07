package de.pse.kit.studywithme.model.repository

import android.content.Context
import de.pse.kit.studywithme.SingletonHolder
import de.pse.kit.studywithme.model.data.Report
import de.pse.kit.studywithme.model.data.User
import de.pse.kit.studywithme.model.network.ReportService
import de.pse.kit.studywithme.model.network.ReportServiceImpl
import de.pse.kit.studywithme.model.network.UserService
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import kotlinx.coroutines.runBlocking

/**
 * Report repository
 *
 * @constructor Create empty Report repository
 */
class ReportRepository private constructor() {
    private val reportService = ReportService.getInstance(Pair(Android.create()) { "" })

    fun getReports(): List<Report> {
        return runBlocking {
            return@runBlocking reportService.getReports()
        }
    }

    fun deleteUserReport(reportingUserID: Int, reportedUserID: Int, boxID: Int) {
        return runBlocking {
            return@runBlocking reportService.deleteUserReport(
                reportedUserID,
                reportingUserID,
                boxID
            )
        }
    }

    fun deleteGroupReport(reportingUserID: Int, groupID: Int, boxID: Int) {
        return runBlocking {
            return@runBlocking reportService.deleteGroupReport(reportingUserID, groupID, boxID)
        }
    }

    fun deleteSessionReport(reportingUserID: Int, sessionID: Int, boxID: Int) {
        return runBlocking {
            return@runBlocking reportService.deleteSessionReport(reportingUserID, sessionID, boxID)
        }
    }

    fun getBlockedUsers(): List<User> {
        return runBlocking {
            return@runBlocking reportService.getBlockedUsers()
        }
    }

    fun blockUser(uid: Int) {
        return runBlocking {
            return@runBlocking reportService.blockUser(uid)
        }
    }

    fun unblockUser(uid: Int) {
        return runBlocking {
            return@runBlocking reportService.unblockUser(uid)
        }
    }

    companion object {
        val instance: ReportRepository by lazy { ReportRepository() }
    }
}