package de.pse.kit.studywithme.model.network

import de.pse.kit.studywithme.model.data.*
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import kotlinx.serialization.descriptors.PrimitiveKind

interface ReportService {
    suspend fun getReports(): List<Report>
    suspend fun deleteUserReport(reportingUserID: Int, reportedUserID: Int, boxID: Int)
    suspend fun deleteGroupReport(reportingUserID: Int, groupID: Int, boxID: Int)
    suspend fun deleteSessionReport(reportingUserID: Int, sessionID: Int, boxID: Int)
    suspend fun reportGroup(groupID: Int, groupField: GroupField, reporterID: Int)
    suspend fun reportUser(userID: Int, userField: UserField, reporterID: Int)
    suspend fun reportSession(sessionID: Int, sessionField: SessionField, reporterID: Int)
    suspend fun getBlockedUsers(): List<User>
    suspend fun blockUser(uid: Int)
    suspend fun unblockUser(uid: Int)

    companion object {
        val instance: ReportServiceImpl by lazy {
            ReportServiceImpl(client = HttpClient(Android) {
                install(JsonFeature) {
                    serializer = KotlinxSerializer()
                }
            })
        }
    }
}