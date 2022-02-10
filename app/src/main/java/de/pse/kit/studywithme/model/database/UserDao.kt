package de.pse.kit.studywithme.model.database

import androidx.room.*
import de.pse.kit.studywithme.model.data.User

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveUser(user: User)

    @Delete
    suspend fun removeUser(user: User)

    @Query("SELECT * FROM user WHERE user_ID LIKE :uid")
    suspend fun getUser(uid: Int): User

    @Query("SELECT * FROM user WHERE firebase_UID LIKE :fuid")
    suspend fun getUser(fuid: String): User

    @Update
    suspend fun editUser(user: User)
}