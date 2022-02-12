package de.pse.kit.studywithme.model.network

import de.pse.kit.studywithme.model.data.Session
import de.pse.kit.studywithme.model.data.SessionAttendee
import io.ktor.client.*
import io.ktor.client.engine.android.*
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
     * Edit a session
     *
     * @param session
     * @return session object or null
     */
    suspend fun editSession(session: Session): Session?

    /**
     * Remove a session
     *
     * @param sessionID
     */
    suspend fun removeSession(sessionID: Int)

    /**
     * Returns a user with ID userID if he attends the session with ID sessionID otherwise returns null
     *
     * @param userID
     * @param sessionID
     * @return sessionattendee object or null
     */
    suspend fun newAttendee(userID: Int, sessionID: Int): SessionAttendee?

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

    companion object {
        val instance: SessionService by lazy {
            SessionServiceImpl(client = HttpClient(Android) {
                install(JsonFeature) {
                    serializer = KotlinxSerializer()
                }
            })
        }
    }
}