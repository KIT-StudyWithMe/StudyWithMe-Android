package de.pse.kit.studywithme.model.network

import de.pse.kit.studywithme.model.data.*
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.http.*

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
            client.put(HttpRoutes.GROUPS + groupID + "/reports/" + reporterID) {
                contentType(ContentType.Application.Json)
                body = groupField
            }
        } catch (e: RedirectResponseException) {
            println("Redirect Error: ${e.response.status.description}")
        } catch (e: ClientRequestException) {
            println("Request Error: ${e.response.status.description}")
        } catch (e: ServerResponseException) {
            println("Response Error: ${e.response.status.description}")
        } catch (e: Exception) {
            println("Error: ${e.message}")
        }
    }

    override suspend fun reportUser(userID: Int, userField: UserField, reporterID: Int) {
        try {
            client.put(HttpRoutes.USERS + userID + "/reports/" + reporterID) {
                contentType(ContentType.Application.Json)
                body = userField
            }
        } catch (e: RedirectResponseException) {
            println("Redirect Error: ${e.response.status.description}")
        } catch (e: ClientRequestException) {
            println("Request Error: ${e.response.status.description}")
        } catch (e: ServerResponseException) {
            println("Response Error: ${e.response.status.description}")
        } catch (e: Exception) {
            println("Error: ${e.message}")
        }
    }

    override suspend fun reportSession(sessionID: Int, sessionField: SessionField, reporterID: Int) {
        try {
            client.put(HttpRoutes.SESSIONS + sessionID + "/reports/" + reporterID) {
                contentType(ContentType.Application.Json)
                body = sessionField
            }
        } catch (e: RedirectResponseException) {
            println("Redirect Error: ${e.response.status.description}")
        } catch (e: ClientRequestException) {
            println("Request Error: ${e.response.status.description}")
        } catch (e: ServerResponseException) {
            println("Response Error: ${e.response.status.description}")
        } catch (e: Exception) {
            println("Error: ${e.message}")
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