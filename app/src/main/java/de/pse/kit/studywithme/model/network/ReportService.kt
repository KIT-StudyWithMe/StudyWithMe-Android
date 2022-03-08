package de.pse.kit.studywithme.model.network

import de.pse.kit.studywithme.SingletonHolder
import de.pse.kit.studywithme.model.data.*
import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.engine.android.*
import io.ktor.client.features.auth.*
import io.ktor.client.features.auth.providers.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import kotlinx.serialization.descriptors.PrimitiveKind

interface ReportService {
    /**
     * Returns list of all reports
     *
     * @return List of report object
     */
    suspend fun getReports(): List<Report>

    /**
     * Delete the report of a user
     *
     * @param reportingUserID
     * @param reportedUserID
     * @param boxID
     */
    suspend fun deleteUserReport(reportingUserID: Int, reportedUserID: Int, boxID: Int)

    /**
     * Delete report of a group
     *
     * @param reportingUserID
     * @param groupID
     * @param boxID
     */
    suspend fun deleteGroupReport(reportingUserID: Int, groupID: Int, boxID: Int)

    /**
     * Delete report of a session
     *
     * @param reportingUserID
     * @param sessionID
     * @param boxID
     */
    suspend fun deleteSessionReport(reportingUserID: Int, sessionID: Int, boxID: Int)

    /**
     * The report of a group with details which section got reported
     *
     * @param groupID
     * @param groupField
     * @param reporterID
     */
    suspend fun reportGroup(groupID: Int, groupField: GroupField, reporterID: Int)

    /**
     * The report of a user with details which section got reported
     *
     * @param userID
     * @param userField
     * @param reporterID
     */
    suspend fun reportUser(userID: Int, userField: UserField, reporterID: Int)

    /**
     * The report of a session with details which section got reported
     *
     * @param sessionID
     * @param sessionField
     * @param reporterID
     */
    suspend fun reportSession(sessionID: Int, sessionField: SessionField, reporterID: Int)

    /**
     * List of all blocked users
     *
     * @return List of user object
     */
    suspend fun getBlockedUsers(): List<User>

    /**
     * Blocking a user by his ID
     *
     * @param uid
     */
    suspend fun blockUser(uid: Int)

    /**
     * Unblock a user by his id
     *
     * @param uid
     */
    suspend fun unblockUser(uid: Int)

    companion object :
        SingletonHolder<ReportServiceImpl, Pair<HttpClientEngine, suspend () -> String?>>(
            {
                val engine = it.first
                val token = it.second

                ReportServiceImpl(client = HttpClient(engine) {
                    install(JsonFeature) {
                        val json = kotlinx.serialization.json.Json { ignoreUnknownKeys = true }
                        serializer = KotlinxSerializer(json)
                    }
                    install(Auth) {
                        bearer {
                            loadTokens {
                                BearerTokens(
                                    accessToken = token() ?: "",
                                    refreshToken = token() ?: ""
                                )
                            }
                        }
                    }
                })
            })
}