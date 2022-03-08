package de.pse.kit.studywithme.model.repository

import android.content.Context
import android.util.Log
import de.pse.kit.studywithme.SingletonHolder
import de.pse.kit.studywithme.model.data.Session
import de.pse.kit.studywithme.model.data.SessionAttendee
import de.pse.kit.studywithme.model.data.SessionField
import de.pse.kit.studywithme.model.database.AppDatabase
import de.pse.kit.studywithme.model.network.ReportService
import de.pse.kit.studywithme.model.network.SessionService
import io.ktor.client.engine.android.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Session repository
 *
 * @constructor
 *
 * @param context
 */
class SessionRepository private constructor(context: Context) : SessionRepositoryInterface {
    private val sessionDao = AppDatabase.getInstance(context).sessionDao()
    private val auth = Authenticator
    private val sessionService = SessionService.getInstance(Pair(Android.create()) { auth.firebaseUID ?: "" })
    private val reportService = ReportService.getInstance(Pair(Android.create()) { auth.firebaseUID ?: "" })

    @ExperimentalCoroutinesApi
    override suspend fun getSessions(groupID: Int): Flow<List<Session>> = channelFlow {
        if (auth.firebaseUID == null) {
            // TODO: Explicit exception class
            throw Exception("Authentication Error: No local user signed in.")
        }
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

    override suspend fun getSession(sessionID: Int): Flow<Session> = channelFlow {
        if (auth.firebaseUID == null) {
            // TODO: Explicit exception class
            throw Exception("Authentication Error: No local user signed in.")
        }
        val truthWasSend = AtomicBoolean(false)

        launch {
            val remoteSession = sessionService.getSession(sessionID)
            send(remoteSession)
            truthWasSend.set(true)
        }
        launch {
            val localSession = sessionDao.getSession(sessionID)
            if (!truthWasSend.get()) {
                send(localSession)
            }
        }
    }.filterNotNull()

    override suspend fun newSession(session: Session): Boolean {
        if (auth.firebaseUID == null) {
            // TODO: Explicit exception class
            throw Exception("Authentication Error: No local user signed in.")
        }
        val remoteSession = sessionService.newSession(session)
        if (remoteSession != null) {
            Log.d(auth.TAG, "Remote Database Session Post:success")
            sessionDao.saveSession(remoteSession)
            return true
        } else {
            return false
        }
    }

    override suspend fun editSession(session: Session): Boolean {
        if (auth.firebaseUID == null) {
            // TODO: Explicit exception class
            throw Exception("Authentication Error: No local user signed in.")
        }
        val remoteSession = sessionService.editSession(session)

        if (remoteSession == session) {
            sessionDao.editSession(session)
            return true
        } else {
            return false
        }
    }

    override suspend fun removeSession(session: Session): Unit = coroutineScope {
        if (auth.firebaseUID == null) {
            // TODO: Explicit exception class
            throw Exception("Authentication Error: No local user signed in.")
        }

        launch {
            sessionDao.removeSession(session)
        }
        launch {
            sessionService.removeSession(session.sessionID)
        }
    }

    override suspend fun newAttendee(sessionID: Int): Boolean {
        if (auth.firebaseUID == null) {
            // TODO: Explicit exception class
            throw Exception("Authentication Error: No local user signed in.")
        }
        val remoteSessionAttendee = sessionService.newAttendee(auth.user!!.userID, sessionID)

        if (remoteSessionAttendee) {
            //sessionDao.saveSessionAttendee(remoteSessionAttendee)
            return true
        } else {
            return false
        }
    }

    override suspend fun removeAttendee(sessionID: Int) {
        if (auth.firebaseUID == null) {
            // TODO: Explicit exception class
            throw Exception("Authentication Error: No local user signed in.")
        }
        sessionService.removeAttendee(auth.user!!.userID, sessionID)
    }

    override suspend fun getAttendees(sessionID: Int): Flow<List<SessionAttendee>> = channelFlow {
        if (auth.firebaseUID == null) {
            // TODO: Explicit exception class
            throw Exception("Authentication Error: No local user signed in.")
        }
        val truthWasSend = AtomicBoolean(false)

        launch {
            val remoteSessionAttendees = sessionService.getAttendees(sessionID)
            send(remoteSessionAttendees)
            truthWasSend.set(true)
        }
        launch {
            val localSessionAttendees = sessionDao.getSessionAttendees(sessionID)
            if (!truthWasSend.get()) {
                send(localSessionAttendees)
            }
        }
    }.filterNotNull()

    override suspend fun reportSession(sessionID: Int, sessionfield: SessionField) {
        if (auth.firebaseUID == null) {
            // TODO: Explicit exception class
            throw Exception("Authentication Error: No local user signed in.")
        }
        reportService.reportSession(sessionID, sessionfield, auth.user!!.userID)
    }

    companion object : SingletonHolder<SessionRepository, Context>({ SessionRepository(it) })
}