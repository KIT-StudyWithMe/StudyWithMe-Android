package de.pse.kit.studywithme.model.network

import de.pse.kit.studywithme.model.data.Session
import de.pse.kit.studywithme.model.data.SessionAttendee
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.http.*

class SessionServiceImpl(private val client: HttpClient): SessionService {
    override suspend fun getSessions(groupID: Int): List<Session> {
        return try {
            client.get(HttpRoutes.GROUPS + groupID + "/detail/sessions")
        } catch (e: RedirectResponseException) {
            println("Redirect Error: ${e.response.status.description}")
            emptyList()
        } catch (e: ClientRequestException) {
            println("Request Error: ${e.response.status.description}")
            emptyList()
        } catch (e: ServerResponseException) {
            println("Response Error: ${e.response.status.description}")
            emptyList()
        } catch (e: Exception) {
            println("Error: ${e.message}")
            emptyList()
        }
    }

    override suspend fun getSession(sessionID: Int): Session? {
        return try {
            client.get(HttpRoutes.SESSIONS + sessionID)
        } catch (e: RedirectResponseException) {
            println("Redirect Error: ${e.response.status.description}")
            null
        } catch (e: ClientRequestException) {
            println("Request Error: ${e.response.status.description}")
            null
        } catch (e: ServerResponseException) {
            println("Response Error: ${e.response.status.description}")
            null
        } catch (e: Exception) {
            println("Error: ${e.message}")
            null
        }
    }

    override suspend fun newSession(session: Session): Session? {
        return try {
            client.post(HttpRoutes.GROUPS + session.groupID + "/detail/sessions") {
                contentType(ContentType.Application.Json)
                body = session
            }
        } catch (e: RedirectResponseException) {
            println("Redirect Error: ${e.response.status.description}")
            null
        } catch (e: ClientRequestException) {
            println("Request Error: ${e.response.status.description}")
            null
        } catch (e: ServerResponseException) {
            println("Response Error: ${e.response.status.description}")
            null
        } catch (e: Exception) {
            println("Error: ${e.message}")
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
            println("Redirect Error: ${e.response.status.description}")
            null
        } catch (e: ClientRequestException) {
            println("Request Error: ${e.response.status.description}")
            null
        } catch (e: ServerResponseException) {
            println("Response Error: ${e.response.status.description}")
            null
        } catch (e: Exception) {
            println("Error: ${e.message}")
            null
        }
    }

    override suspend fun removeSession(sessionID: Int) {
        return try {
            client.delete(HttpRoutes.SESSIONS + sessionID)
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

    override suspend fun newAttendee(userID: Int, sessionID: Int): SessionAttendee? {
        return try {
            client.put(HttpRoutes.SESSIONS + sessionID + "/participate") {
                contentType(ContentType.Application.Json)
                body = userID
            }
        } catch (e: RedirectResponseException) {
            println("Redirect Error: ${e.response.status.description}")
            null
        } catch (e: ClientRequestException) {
            println("Request Error: ${e.response.status.description}")
            null
        } catch (e: ServerResponseException) {
            println("Response Error: ${e.response.status.description}")
            null
        } catch (e: Exception) {
            println("Error: ${e.message}")
            null
        }
    }

    override suspend fun getAttendees(sessionID: Int): List<SessionAttendee>? {
        TODO("Not yet implemented")
    }

    override suspend fun removeAttendee(userID: Int, sessionID: Int) {
        TODO("Not yet implemented")
    }
}
