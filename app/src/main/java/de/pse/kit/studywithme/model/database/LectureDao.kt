package de.pse.kit.studywithme.model.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import de.pse.kit.studywithme.model.data.Lecture

@Dao
interface LectureDao {
    @Query("SELECT * FROM lecture WHERE lecture_name LIKE :prefix || '%'")
    fun getLectures(prefix: String)

    @Insert
    fun saveLecture(lecture: Lecture)

    @Delete
    fun removeLecture(lecture: Lecture)
}