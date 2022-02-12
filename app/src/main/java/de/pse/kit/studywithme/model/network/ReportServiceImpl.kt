package de.pse.kit.studywithme.model.network

import de.pse.kit.studywithme.model.data.*
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.http.*

/**
 * The implementation of the functions from the report interface
 *
 * @property client
 * @constructor Create empty Report service impl
 */
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

    override suspend fun reportGroup(groupID: Int, groupField: GroupField, reporterID: Int) {
        try {
            client.put(HttpRoutes.GROUPS + groupID + "/report/" + reporterID) {
                contentType(ContentType.Application.Json)
                body = groupField
            }
        } catch (e: RedirectResponseException) {
            println("ReportGroup Redirect Error: ${e.response.status.description}")
        } catch (e: ClientRequestException) {
            println("ReportGroup Request Error: ${e.response.status.description}")
        } catch (e: ServerResponseException) {
            println("ReportGroup Response Error: ${e.response.status.description}")
        } catch (e: Exception) {
            println("ReportGroup Error: ${e.message}")
        }
    }

    override suspend fun reportUser(userID: Int, userField: UserField, reporterID: Int) {
        try {
            client.put(HttpRoutes.USERS + userID + "/report/" + reporterID) {
                contentType(ContentType.Application.Json)
                body = userField
            }
        } catch (e: RedirectResponseException) {
            println("ReportUser Redirect Error: ${e.response.status.description}")
        } catch (e: ClientRequestException) {
            println("ReportUser Request Error: ${e.response.status.description}")
        } catch (e: ServerResponseException) {
            println("ReportUser Response Error: ${e.response.status.description}")
        } catch (e: Exception) {
            println("ReportUser Error: ${e.message}")
        }
    }

    override suspend fun reportSession(sessionID: Int, sessionField: SessionField, reporterID: Int) {
        try {
            client.put(HttpRoutes.SESSIONS + sessionID + "/report/" + reporterID) {
                contentType(ContentType.Application.Json)
                body = sessionField
            }
        } catch (e: RedirectResponseException) {
            println("ReportSession Redirect Error: ${e.response.status.description}")
        } catch (e: ClientRequestException) {
            println("ReportSession Request Error: ${e.response.status.description}")
        } catch (e: ServerResponseException) {
            println("ReportSession Response Error: ${e.response.status.description}")
        } catch (e: Exception) {
            println("ReportSession Error: ${e.message}")
        }
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