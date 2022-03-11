package de.pse.kit.studywithme.model.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import de.pse.kit.studywithme.model.data.*
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class GroupDaoTest {

    private lateinit var groupDao: GroupDao
    private lateinit var db: AppDatabase
    private lateinit var group0: RemoteGroup
    private lateinit var group1: RemoteGroup

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, AppDatabase::class.java
        ).build()
        groupDao = db.groupDao()
        group0 = RemoteGroup(
            groupID = 0,
            name = "Die wilden Coder",
            description = "description",
            lectureID = 0,
            sessionFrequency = SessionFrequency.WEEKLY,
            sessionType = SessionType.HYBRID,
            lectureChapter = 8,
            exercise = 8,
            memberCount = 0,
        )
        group1 = RemoteGroup(
            groupID = 1,
            name = "Die lahmen Coder",
            lectureID = 1,
            description = "Cool bleiben.",
            sessionFrequency = SessionFrequency.MONTHLY,
            sessionType = SessionType.ONLINE,
            lectureChapter = 0,
            exercise = 0,
            memberCount = 0,
        )
    }

    @Test
    @Throws(Exception::class)
    fun saveAndGetGroupById() {
        runBlocking {
            groupDao.saveGroup(group0)
            groupDao.saveGroup(group1)
            assertThat(group0).isEqualTo(groupDao.getGroup(0))
        }
    }

    @Test
    @Throws(Exception::class)
    fun saveAndGetGroups() {
        runBlocking {
            groupDao.saveGroup(group0)
            groupDao.saveGroup(group1)
            val groupList = groupDao.getGroups()
            assert(groupList!!.contains(group0))
            assertThat(groupList.contains(group1))
        }
    }

    @Test
    @Throws(Exception::class)
    fun saveAndRemoveGroup() {
        runBlocking {
            groupDao.saveGroup(group0)
            groupDao.saveGroup(group1)
            groupDao.removeGroup(group0)
            val groupList = groupDao.getGroups()
            assertThat(groupList).doesNotContain(group0)
            assertThat(groupList).contains(group1)
        }
    }

    @Test
    @Throws(Exception::class)
    fun saveAndEditGroup() {
        runBlocking {
            val groupToBeEdited = RemoteGroup(
                groupID = 0,
                name = "Die wilden Coder",
                description = "Alles ist gut solange du wild bist.",
                lectureID = 0,
                sessionFrequency = SessionFrequency.WEEKLY,
                sessionType = SessionType.HYBRID,
                lectureChapter = 8,
                exercise = 8,
                memberCount = 0,
            )
            groupDao.saveGroup(groupToBeEdited)
            val editedGroup = RemoteGroup(
                groupID = 0,
                name = "Die wilden Coder",
                description = "",
                lectureID = 0,
                sessionFrequency = SessionFrequency.WEEKLY,
                sessionType = SessionType.HYBRID,
                lectureChapter = 8,
                exercise = 8,
                memberCount = 0,
            )
            groupDao.editGroup(editedGroup)
            assertThat(groupDao.getGroup(0)).isEqualTo(editedGroup)
        }
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }


}