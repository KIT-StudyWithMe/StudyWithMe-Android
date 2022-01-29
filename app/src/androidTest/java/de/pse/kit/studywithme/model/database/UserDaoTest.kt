package de.pse.kit.studywithme.model.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import de.pse.kit.studywithme.model.data.User
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import org.junit.jupiter.api.assertAll

@RunWith(AndroidJUnit4::class)
class UserDaoTest {
    private lateinit var userDao: UserDao
    private lateinit var db: AppDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, AppDatabase::class.java
        ).build()
        userDao = db.userDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun saveAndGetUsersByName() {
        val user0 = User(0, "Olaf", 0, 0, "0152987654321")
        val user1 = User(1, "Roland", 1, 1, "07662789615")
        val user2 = User(2, "Johnny", 2, 2, "018054646")
        userDao.saveUser(user0)
        userDao.saveUser(user1)
        userDao.saveUser(user2)
        val user0ByUID = userDao.getUser(user0.userID)
        val user1ByUID = userDao.getUser(user1.userID)
        val user2ByUID = userDao.getUser(user2.userID)
        assertAll(
            { assertEquals(user0ByUID,user0) },
            { assertEquals(user1ByUID,user1) },
            { assertEquals(user2ByUID,user2) },
        )
    }

    @Test
    @Throws(Exception::class)
    fun saveAndRemoveUser() {
        val user0 = User(0, "Olaf", 0, 0, "0152987654321")
        val user1 = User(1, "Roland", 1, 1, "07662789615")
        userDao.saveUser(user0)
        userDao.saveUser(user1)
        userDao.removeUser(user0)
        val user0ByUID = userDao.getUser(user0.userID)
        val user1ByUID = userDao.getUser(user1.userID)
        assertThat(user0ByUID).isNull()
        assertThat(user1ByUID).isEqualTo(user1)
    }

    @Test
    @Throws(Exception::class)
    fun saveAndEditUser() {
        val user0 = User(0, "Olaf", 0, 0, "0152987654321")
        val user0Edited = User(0, "Roland", 1, 1, "07662789615")
        userDao.saveUser(user0)
        userDao.editUser(user0Edited)
        val user0ByUID = userDao.getUser(user0.userID)
        assertThat(user0ByUID).isEqualTo(user0Edited)
    }
}