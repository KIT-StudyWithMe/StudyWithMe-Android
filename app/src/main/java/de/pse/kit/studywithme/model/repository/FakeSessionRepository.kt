package de.pse.kit.studywithme.model.repository

import android.os.Build
import androidx.annotation.RequiresApi
import de.pse.kit.studywithme.model.data.Session
import de.pse.kit.studywithme.model.data.SessionAttendee
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.*

class FakeSessionRepository() : SessionRepositoryInterface {

    @RequiresApi(Build.VERSION_CODES.O)
    var session = Session(
        sessionID = 0,
        groupID = 0,
        location = "Bibliothek",
        date = Date(2022,12,31),
        duration = 2
    )
    var sessionAttendee = SessionAttendee(
        sessionID = 0,
        userID = 0,
        participates = true
    )

    @ExperimentalCoroutinesApi
    override fun getSessions(groupID: Int): Flow<List<Session>> {
        return flow { emit(listOf(session)) } //TODO
    }

    override fun newSession(session: Session): Boolean {
        this.session = session
        return true
    }

    override fun editSession(session: Session): Boolean {
        this.session = session
        return true
    }

    override fun removeSession(session: Session) {
        this.session = session
        return
    }

    override fun newAttendee(sessionID: Int): Boolean {

        return true
    }

    override fun removeAttendee(sessionID: Int) {

    }

    override fun getAttendees(sessionID: Int): Flow<List<SessionAttendee>> {
        return flow { emit(listOf(sessionAttendee)) } //TODO
    }
}