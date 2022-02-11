package de.pse.kit.studywithme.model.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import de.pse.kit.studywithme.model.data.Session
import kotlinx.coroutines.runBlocking
import org.junit.*
import org.junit.runner.RunWith
import java.io.IOException
import java.util.*

@RunWith(AndroidJUnit4::class)
class SessionDaoTest {
    /*

    private lateinit var sessionDao: SessionDao
    private lateinit var db: AppDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, AppDatabase::class.java
        ).build()
        sessionDao = db.sessionDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun saveAndGetSessionsByGroup() {
        runBlocking {
            val session0 = Session(0, 0, "Bibliothek KIT", date = Date(), duration = 2)
            val session1 = Session(1, 0, "Bibliothek KIT", date = Date(), duration = 2)
            sessionDao.saveSession(session0)
            sessionDao.saveSession(session1)
            val sessionsByGroup = sessionDao.getSessions(0)
            assertThat(sessionsByGroup).contains(session0)
            assertThat(sessionsByGroup).contains(session1)
        }
    }

    @Test
    @Throws(Exception::class)
    fun saveAndGetSessionBySession() {
        runBlocking {
            val session = Session(0, 0, "Bibliothek KIT", date = Date(), duration = 2)
            sessionDao.saveSession(session)
            val sessionBySession = sessionDao.getSession(0)
            assertThat(sessionBySession).isEqualTo(session)
        }
    }

    @Test
    @Throws(Exception::class)
    fun saveAndRemoveSessions() {
        runBlocking {
            val session = Session(0, 0, "Bibliothek KIT", date = Date(), duration = 2)
            sessionDao.saveSession(session)
            sessionDao.removeSession(session)
            val sessionsByGroup = sessionDao.getSessions(0)
            assertThat(sessionsByGroup).doesNotContain(session)
        }
    }

    @Test
    @Throws(Exception::class)
    fun saveAndUpdateSession() {
        runBlocking {
            val session = Session(0, 0, "Bibliothek KIT", date = Date(), duration = 2)
            sessionDao.saveSession(session)
            val editedSession = Session(0, 0, "Bibliothek KIT-Nord", date = Date(), duration = 2)
            sessionDao.editSession(editedSession)
            val sessionBySession = sessionDao.getSession(0)
            assertThat(sessionBySession).isEqualTo(editedSession)
        }
    }
    */
}