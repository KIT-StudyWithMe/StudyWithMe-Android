package de.pse.kit.studywithme.model.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import de.pse.kit.studywithme.model.data.*
import de.pse.kit.studywithme.model.database.TypeConverters as TypeConverters_

@Database(entities = [Session::class, Group::class, Lecture::class, User::class], version = 1)
@TypeConverters(TypeConverters_::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun sessionDao(): SessionDao
    abstract fun groupDao(): GroupDao
    abstract fun lectureDao(): LectureDao
    abstract fun userDao(): UserDao
}