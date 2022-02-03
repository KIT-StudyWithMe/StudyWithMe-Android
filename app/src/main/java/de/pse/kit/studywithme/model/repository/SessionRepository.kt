package de.pse.kit.studywithme.model.repository

import android.content.Context
import android.util.Log
import de.pse.kit.studywithme.SingletonHolder
import de.pse.kit.studywithme.model.data.Session
import de.pse.kit.studywithme.model.database.AppDatabase
import de.pse.kit.studywithme.model.network.SessionService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.concurrent.atomic.AtomicBoolean

class SessionRepository private constructor(context: Context) {
    private val sessionService = SessionService.instance
    private val sessionDao = AppDatabase.getInstance(context).sessionDao()
    private val auth = Authenticator

    fun getSessions(groupID: Int): Flow<List<Session>> {
        if (auth.firebaseUID == null) {
            // TODO: Explicit exception class
            throw Exception("Authentication Error: No local user signed in.")
        }

        return channelFlow {
            val truthWasSend = AtomicBoolean(false)

            launch {
                val remoteSession = sessionService.getSessions(groupID)
                send(remoteSession)
                truthWasSend.set(true)
            }
            launch {
                val localSession = sessionDao.getSessions(groupID)
                if (!truthWasSend.get()) {
                    send(localSession)
                }
            }
        }.filterNotNull()
    }

    fun newSession(session: Session): Boolean {
        if (auth.firebaseUID == null) {
            // TODO: Explicit exception class
            throw Exception("Authentication Error: No local user signed in.")
        }

        return runBlocking {
            val remoteSession = sessionService.newSession(session)
            if (remoteSession != null) {
                Log.d(auth.TAG, "Remote Database Session Post:success")
                sessionDao.saveSession(remoteSession)
                return@runBlocking true
            } else {
                return@runBlocking false
            }
        }
    }

    fun editSession(session: Session): Boolean {
        if (auth.firebaseUID == null) {
            // TODO: Explicit exception class
            throw Exception("Authentication Error: No local user signed in.")
        }

        return runBlocking {
            val remoteSession = sessionService.editSession(session)

            if (remoteSession == session) {
                sessionDao.editSession(session)
                return@runBlocking true
            } else {
                return@runBlocking false
            }
        }
    }

    fun removeSession(session: Session) {
        if (auth.firebaseUID == null) {
            // TODO: Explicit exception class
            throw Exception("Authentication Error: No local user signed in.")
        }

        runBlocking {
            launch {
                sessionDao.removeSession(session)
            }
            launch {
                sessionService.removeSession(session.sessionID)
            }
        }
    }

    fun newParticipant(sessionID: Int): Boolean {
        if (auth.firebaseUID == null) {
            // TODO: Explicit exception class
            throw Exception("Authentication Error: No local user signed in.")
        }
        
        return runBlocking {
            val remoteSession = sessionService.newParticipant(sessionID)
            if (remoteSession != null) {
                Log.d(auth.TAG, "Remote Database Session Post:success")
                sessionDao.saveSession(remoteSession)
                return@runBlocking true
            } else {
                return@runBlocking false
            }
        }
    }

    companion object : SingletonHolder<SessionRepository, Context>({ SessionRepository(it) })
}