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

class SessionServiceImpl(private val client: HttpClient) : SessionService {
    override suspend fun getSessions(groupID: Int): List<Session> {
        return try {
            client.get(HttpRoutes.GROUPS + groupID + "/sessions")
        } catch (e: RedirectResponseException) {
            println("GetSessions Redirect Error: ${e.response.status}")
            emptyList()
        } catch (e: ClientRequestException) {
            println("GetSessions Request Error: ${e.response.status}")
            emptyList()
        } catch (e: ServerResponseException) {
            println("GetSessions Response Error: ${e.response.status}")
            emptyList()
        } catch (e: Exception) {
            println("GetSessions Error: ${e.message}")
            emptyList()
        }
    }

    override suspend fun getSession(sessionID: Int): Session? {
        return try {
            client.get(HttpRoutes.SESSIONS + sessionID)
        } catch (e: RedirectResponseException) {
            println("GetSession Redirect Error: ${e.response.status}")
            null
        } catch (e: ClientRequestException) {
            println("GetSession Request Error: ${e.response.status}")
            null
        } catch (e: ServerResponseException) {
            println("GetSession Response Error: ${e.response.status}")
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
        } catch (e: RedirectResponseException) {
            println("NewSession Redirect Error: ${e.response.status}")
            null
        } catch (e: ClientRequestException) {
            println("NewSession Request Error: ${e.response.status}")
            null
        } catch (e: ServerResponseException) {
            println("NewSession Response Error: ${e.response.status}")
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
        } catch (e: RedirectResponseException) {
            println("EditSession Redirect Error: ${e.response.status}")
            null
        } catch (e: ClientRequestException) {
            println("EditSession Request Error: ${e.response.status}")
            null
        } catch (e: ServerResponseException) {
            println("EditSession Response Error: ${e.response.status}")
            null
        } catch (e: Exception) {
            println("EditSession Error: ${e.message}")
            null
        }
    }

    override suspend fun removeSession(sessionID: Int) {
        try {
            client.delete(HttpRoutes.SESSIONS + sessionID)
        } catch (e: RedirectResponseException) {
            println("RemoveSession Redirect Error: ${e.response.status}")
        } catch (e: ClientRequestException) {
            println("RemoveSession Request Error: ${e.response.status}")
        } catch (e: ServerResponseException) {
            println("RemoveSession Response Error: ${e.response.status}")
        } catch (e: Exception) {
            println("RemoveSession Error: ${e.message}")
        }
    }

    override suspend fun newAttendee(userID: Int, sessionID: Int): SessionAttendee? {
        return try {
            client.put(HttpRoutes.SESSIONS + sessionID + "/participate") {
                contentType(ContentType.Application.Json)
                body = userID
            }
        } catch (e: RedirectResponseException) {
            println("NewAttendee Redirect Error: ${e.response.status}")
            null
        } catch (e: ClientRequestException) {
            println("NewAttendee Request Error: ${e.response.status}")
            null
        } catch (e: ServerResponseException) {
            println("NewAttendee Response Error: ${e.response.status}")
            null
        } catch (e: Exception) {
            println("NewAttendee Error: ${e.message}")
            null
        }
    }

    override suspend fun getAttendees(sessionID: Int): List<SessionAttendee>? {
        TODO("Not yet implemented")
    }

    override suspend fun removeAttendee(userID: Int, sessionID: Int) {
        try {
            client.put(HttpRoutes.SESSIONS + sessionID + "/participate/" + userID) {
                contentType(ContentType.Application.Json)
                body = false
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
}
