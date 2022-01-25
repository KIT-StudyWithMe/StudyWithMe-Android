package de.pse.kit.studywithme.model.database

import androidx.room.Database
import androidx.room.RoomDatabase
import de.pse.kit.studywithme.model.data.Session

@Database(entities = [Session::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun sessionDao(): SessionDao
}