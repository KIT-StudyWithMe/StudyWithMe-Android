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
 * Returns an instance of the database and implements the access objects
 *
 * @constructor Create empty App database
 */
@Database(
    entities = [Session::class, RemoteGroup::class, User::class, SessionAttendee::class, GroupMember::class, Lecture::class, Major::class],
    version = 2
)
@TypeConverters(TypeConverters_::class)
abstract class AppDatabase : RoomDatabase() {
    /**
     * Returns the Dao for session
     *
     * @return SessionDao
     */
    abstract fun sessionDao(): SessionDao

    /**
     * Returns the Dao for group
     *
     * @return GroupDao
     */
    abstract fun groupDao(): GroupDao

    /**
     * Returns the Dao for user
     *
     * @return UserDao
     */
    abstract fun userDao(): UserDao

    companion object : SingletonHolder<AppDatabase, Context>({
        Room.databaseBuilder(
            it.applicationContext,
            AppDatabase::class.java, "StudyWithMe.db"
        ).fallbackToDestructiveMigration()
            .build()
    })
}