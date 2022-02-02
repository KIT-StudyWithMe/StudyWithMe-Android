package de.pse.kit.studywithme.model.repository

import android.content.Context
import de.pse.kit.studywithme.SingletonHolder
import de.pse.kit.studywithme.model.data.Session
import de.pse.kit.studywithme.model.network.SessionService
import kotlinx.coroutines.runBlocking

class SessionRepository private constructor(context: Context) {
    private val sessionService = SessionService.instance

    fun getSessions(groupID: Int): List<Session> {
        return runBlocking {
            return@runBlocking sessionService.getSessions(groupID)
        }
    }

    fun newSession(session: Session) {
        return runBlocking {
            return@runBlocking sessionService.newSession(session)
        }
    }

    fun editSession(sessionID: Int, session: Session) {
        return runBlocking {
            return@runBlocking sessionService.editSession(sessionID, session)
        }
    }

    fun removeSession(sessionID: Int) {
        return runBlocking {
            return@runBlocking sessionService.removeSession(sessionID)
        }
    }

    fun newParticipant(sessionID: Int) {
        return runBlocking {
            return@runBlocking sessionService.newParticipant(sessionID)
        }
    }

    companion object: SingletonHolder<SessionRepository, Context>({ SessionRepository(it) })
}