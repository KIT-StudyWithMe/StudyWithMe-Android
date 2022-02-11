package de.pse.kit.studywithme.model.database

import androidx.room.*
import de.pse.kit.studywithme.model.data.User

/**
 * Interface for management of local database for object user
 *
 * @constructor Create empty User dao
 */
@Dao
interface UserDao {
    /**
     * Saves a user in the database
     *
     * @param user
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveUser(user: User)

    /**
     * Removes a user from the database
     *
     * @param user
     */
    @Delete
    suspend fun removeUser(user: User)

    /**
     * Returns the user with the ID userID from the database or null if there is none with this ID
     *
     * @param uid
     * @return User object or null
     */
    @Query("SELECT * FROM user WHERE user_ID LIKE :uid")
    suspend fun getUser(uid: Int): User?

    /**
     * Returns the user with the firebase_UID fuid from the database or null if there is none with this ID
     *
     * @param fuid
     * @return User object or null
     */
    @Query("SELECT * FROM user WHERE firebase_UID LIKE :fuid")
    suspend fun getUser(fuid: String): User?

    /**
     * Edits a existing user in the database
     *
     * @param user
     */
    @Update
    suspend fun editUser(user: User)
}