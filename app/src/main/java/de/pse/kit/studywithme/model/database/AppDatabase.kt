package de.pse.kit.studywithme.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import de.pse.kit.studywithme.SingletonHolder
import de.pse.kit.studywithme.model.data.*
import de.pse.kit.studywithme.model.database.TypeConverters as TypeConverters_

/**
 *
 *
 * @constructor Create empty App database
 */
@Database(
    entities = [Session::class, RemoteGroup::class, User::class, SessionAttendee::class, GroupMember::class, Lecture::class, Major::class],
    version = 1
)
@TypeConverters(TypeConverters_::class)
abstract class AppDatabase : RoomDatabase() {
    /**
     * Session dao
     *
     * @return
     */
    abstract fun sessionDao(): SessionDao

    /**
     * Group dao
     *
     * @return
     */
    abstract fun groupDao(): GroupDao

    /**
     * User dao
     *
     * @return
     */
    abstract fun userDao(): UserDao

    companion object : SingletonHolder<AppDatabase, Context>({
        Room.databaseBuilder(
            it.applicationContext,
            AppDatabase::class.java, "StudyWithMe.db"
        )
            .build()
    })
}