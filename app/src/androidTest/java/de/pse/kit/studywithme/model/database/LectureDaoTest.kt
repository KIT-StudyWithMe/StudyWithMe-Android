package de.pse.kit.studywithme.model.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import de.pse.kit.studywithme.model.data.Lecture
import org.junit.*
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class LectureDaoTest {

    private lateinit var lectureDao: LectureDao
    private lateinit var db: AppDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, AppDatabase::class.java
        ).build()
        lectureDao = db.lectureDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun saveAndGetLecturesByName() {
        val lecture0 = Lecture(0, "Lineare Algebra I")
        val lecture1 = Lecture(1, "Lineare Algebra II")
        val lecture2 = Lecture(2, "Algorithmen")
        lectureDao.saveLecture(lecture0)
        lectureDao.saveLecture(lecture1)
        lectureDao.saveLecture(lecture2)
        val lecturesByName = lectureDao.getLectures("Lin")
        assertThat(lecturesByName).contains(lecture0)
        assertThat(lecturesByName).contains(lecture1)
        assertThat(lecturesByName).doesNotContain(lecture2)
    }

    @Test
    @Throws(Exception::class)
    fun saveAndRemoveLecture() {
        val lecture0 = Lecture(0, "Lineare Algebra I")
        lectureDao.saveLecture(lecture0)
        lectureDao.removeLecture(lecture0)
        val lecturesByName = lectureDao.getLectures("Lin")
        assertThat(lecturesByName).doesNotContain(lecture0)
    }
}