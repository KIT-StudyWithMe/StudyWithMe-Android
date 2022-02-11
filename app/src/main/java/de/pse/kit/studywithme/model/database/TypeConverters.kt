package de.pse.kit.studywithme.model.database

import androidx.room.TypeConverter
import java.util.*

/**
 * Class for serialization process
 *
 * @constructor Create empty Type converters
 */
class TypeConverters {
    /**
     * Turns value of type long into a date object
     *
     * @param value
     * @return Data object or null
     */
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    /**
     * Turns a date object into type long to serialize dates as JSON
     *
     * @param date
     * @return Long or null
     */
    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time?.toLong()
    }
}