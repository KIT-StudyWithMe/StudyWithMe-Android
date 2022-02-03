package de.pse.kit.studywithme.model.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import de.pse.kit.studywithme.model.data.Group
import de.pse.kit.studywithme.model.data.SessionFrequency
import de.pse.kit.studywithme.model.data.SessionType
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class GroupDaoTest {

    private lateinit var groupDao: GroupDao
    private lateinit var db: AppDatabase
    private lateinit var group0: Group
    private lateinit var group1: Group

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, AppDatabase::class.java
        ).build()
        groupDao = db.groupDao()
        group0 = Group(
            0,
            "Die wilden Coder",
            0,
            "Alles ist gut solange du wild bist.",
            SessionFrequency.WEEKLY,
            SessionType.HYBRID,
            8,
            8,
            0,
            5,
        )
        group1 = Group(
            1,
            "Die lahmen Coder",
            0,
            "Cool bleiben.",
            SessionFrequency.MONTHLY,
            SessionType.ONLINE,
            null,
            null,
            1,
            1,
        )
    }

    @Test
    @Throws(Exception::class)
    fun saveAndGetGroupById() {
        groupDao.saveGroup(group0)
        groupDao.saveGroup(group1)
        assertThat(group0).isEqualTo(groupDao.getGroup(0))
    }

    @Test
    @Throws(Exception::class)
    fun saveAndGetGroups() {
        groupDao.saveGroup(group0)
        groupDao.saveGroup(group1)
        val groupList = groupDao.getGroups()
        assertThat(groupList.contains(group0))
        assertThat(groupList.contains(group1))
    }

    @Test
    @Throws(Exception::class)
    fun saveAndRemoveGroup() {
        groupDao.saveGroup(group0)
        groupDao.saveGroup(group1)
        groupDao.removeGroup(group0)
        val groupList = groupDao.getGroups()
        assertThat(groupList).doesNotContain(group0)
        assertThat(groupList).contains(group1)
    }

    @Test
    @Throws(Exception::class)
    fun saveAndEditGroup() {
        val groupToBeEdited = Group(
            0,
            "Die wilden Coder",
            0,
            "",
            SessionFrequency.WEEKLY,
            SessionType.HYBRID,
            8,
            8,
            0,
            5,
        )
        groupDao.saveGroup(groupToBeEdited)
        val editedGroup = Group(
            0,
            "Die wilden Coder",
            0,
            "Alles ist gut solange du wild bist.",
            SessionFrequency.WEEKLY,
            SessionType.HYBRID,
            8,
            8,
            0,
            5,
        )
        groupDao.editGroup(group0)
        assertThat(groupDao.getGroup(0)).isEqualTo(editedGroup)
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }
}