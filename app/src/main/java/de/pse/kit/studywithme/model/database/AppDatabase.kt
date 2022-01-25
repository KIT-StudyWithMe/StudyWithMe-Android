package de.pse.kit.studywithme.model.database

import androidx.room.Database
import androidx.room.RoomDatabase
import de.pse.kit.studywithme.model.data.*

@Database(entities = [Session::class, Group::class, Lecture::class, User::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun sessionDao(): SessionDao
    abstract fun groupDao(): GroupDao
    abstract fun lectureDao(): LectureDao
    abstract fun userDao(): UserDao
}