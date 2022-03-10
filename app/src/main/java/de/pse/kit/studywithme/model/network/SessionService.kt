package de.pse.kit.studywithme.model.network

import de.pse.kit.studywithme.SingletonHolder
import de.pse.kit.studywithme.model.data.Session
import de.pse.kit.studywithme.model.data.SessionAttendee
import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.engine.android.*
import io.ktor.client.features.auth.*
import io.ktor.client.features.auth.providers.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*

interface SessionService {
    /**
     * Returns a list of all the sessions the group with groupID has planned yet or null if they haven't planned any
     *
     * @param groupID
     * @return List of session object or null
     */
    suspend fun getSessions(groupID: Int): List<Session>?

    /**
     * Returns the session with the ID sessionID or null if there is none with this ID
     *
     * @param sessionID
     * @return
     */
    suspend fun getSession(sessionID: Int): Session?

    /**
     * Creates a new session
     *
     * @param session
     * @return session object or null
     */
    suspend fun newSession(session: Session): Session?

    /**
     * Edits a session
     *
     * @param session
     * @return session object or null
     */
    suspend fun editSession(session: Session): Session?

    /**
     * Removes a session
     *
     * @param sessionID
     */
    suspend fun removeSession(sessionID: Int)

    /**
     * Returns a user with ID userID if he attends the session with ID sessionID otherwise returns null
     *
     * @param userID
     * @param sessionID
     * @return true or false
     */
    suspend fun newAttendee(userID: Int, sessionID: Int): Boolean

    /**
     * Returns a list of users that attend the session with the ID sessionID or null if the session has no attendee
     *
     * @param sessionID
     * @return list of sessionattendee object or null
     */
    suspend fun getAttendees(sessionID: Int): List<SessionAttendee>?

    /**
     * Removes a user from the session with the ID sessionID
     *
     * @param userID
     * @param sessionID
     */
    suspend fun removeAttendee(userID: Int, sessionID: Int)

    companion object :
        SingletonHolder<SessionServiceImpl, Pair<HttpClientEngine, suspend (Boolean) -> String>>(
            {
                val engine = it.first
                val token = it.second

                SessionService.newInstance(engine, token)
            }) {
        fun newInstance(
            engine: HttpClientEngine,
            token: suspend (Boolean) -> String
        ) = SessionServiceImpl(client = HttpClient(engine) {
            install(JsonFeature) {
                val json = kotlinx.serialization.json.Json { ignoreUnknownKeys = true }
                serializer = KotlinxSerializer(json)
            }

            install(Auth) {
                var tokenInfo: String

                bearer {
                    loadTokens {
                        tokenInfo = token(false)
                        BearerTokens(
                            accessToken = tokenInfo,
                            refreshToken = tokenInfo
                        )
                    }
                    refreshTokens {
                        tokenInfo = token(true)

                        BearerTokens(
                            accessToken = tokenInfo,
                            refreshToken = tokenInfo
                        )
                    }
                }
            }
        })
    }
}