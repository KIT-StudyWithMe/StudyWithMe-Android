package de.pse.kit.studywithme.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import de.pse.kit.studywithme.SingletonHolder
import de.pse.kit.studywithme.model.data.*
import de.pse.kit.studywithme.model.database.TypeConverters as TypeConverters_

@Database(entities = [Session::class, Group::class, User::class, SessionAttendee::class], version = 1)
@TypeConverters(TypeConverters_::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun sessionDao(): SessionDao
    abstract fun groupDao(): GroupDao
    abstract fun userDao(): UserDao

    companion object : SingletonHolder<AppDatabase, Context>({
        Room.databaseBuilder(it.applicationContext,
            AppDatabase::class.java, "SwudyWithMe.db")
            .build()
    })
}