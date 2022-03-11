package de.pse.kit.studywithme.model.network

import de.pse.kit.studywithme.model.data.Session
import de.pse.kit.studywithme.model.data.SessionAttendee
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.client.statement.request

/**
 * The implementation of the functions from the session interface
 *
 * @property client
 * @constructor Create empty Session service impl
 */
class SessionServiceImpl(private val client: HttpClient) : SessionService {
    override suspend fun getSessions(groupID: Int): List<Session>? {
        return try {
            client.get(HttpRoutes.GROUPS + groupID + "/sessions")
        } catch (e: ResponseException) {
            println("GetSessions Error: ${e.response.status}")
            null
        } catch (e: Exception) {
            println("GetSessions Error: ${e.message}")
            null
        }
    }

    override suspend fun getSession(sessionID: Int): Session? {
        return try {
            client.get(HttpRoutes.SESSIONS + sessionID)
        } catch (e: ResponseException) {
            println("GetSession Error: ${e.response.status}")
            null
        } catch (e: Exception) {
            println("GetSession Error: ${e.message}")
            null
        }
    }

    override suspend fun newSession(session: Session): Session? {
        return try {
            client.post(HttpRoutes.GROUPS + session.groupID + "/sessions") {
                contentType(ContentType.Application.Json)
                body = session
            }
        } catch (e: ResponseException) {
            println("NewSession Error: ${e.response.status}")
            null
        } catch (e: Exception) {
            println("NewSession Error: ${e.message}")
            null
        }
    }

    override suspend fun editSession(session: Session): Session? {
        return try {
            client.put(HttpRoutes.SESSIONS + session.sessionID) {
                contentType(ContentType.Application.Json)
                body = session
            }
        } catch (e: ResponseException) {
            println("EditSession Error: ${e.response.status}")
            null
        } catch (e: Exception) {
            println("EditSession Error: ${e.message}")
            null
        }
    }

    override suspend fun removeSession(sessionID: Int) {
        try {
            client.delete(HttpRoutes.SESSIONS + sessionID)
        } catch (e: ResponseException) {
            println("RemoveSession Error: ${e.response.status}")
        } catch (e: Exception) {
            println("RemoveSession Error: ${e.message}")
        }
    }

    override suspend fun newAttendee(userID: Int, sessionID: Int): Boolean {
        return try {
            client.put<HttpResponse>(HttpRoutes.SESSIONS + sessionID + "/participate/" +userID) {
                contentType(ContentType.Application.Json)
                body = userID
            }
            true
        } catch (e: ResponseException) {
            println("NewAttendee Error: ${e.response.status}")
            false
        } catch (e: Exception) {
            println("NewAttendee Error: ${e.message}")
            false
        }
    }

    override suspend fun getAttendees(sessionID: Int): List<SessionAttendee>? {
        return try {
            client.get(HttpRoutes.SESSIONS + sessionID + "/attendee")
        } catch (e: ResponseException) {
            println("GetAttendees Error: ${e.response.status}")
            null
        } catch (e: Exception) {
            println("GetAttendees Error: ${e.message}")
            null
        }
    }

    override suspend fun removeAttendee(userID: Int, sessionID: Int) {
        try {
            client.put(HttpRoutes.SESSIONS + sessionID + "/participate/" + userID) {
                contentType(ContentType.Application.Json)
                body = false
            }
        } catch (e: ResponseException) {
            println("RemoveAttendees Error: ${e.response.status}")
        } catch (e: Exception) {
            println("RemoveAttendees Error: ${e.message}")
        }
    }
}
