package de.pse.kit.studywithme.model.database

import androidx.room.*
import de.pse.kit.studywithme.model.data.User

@Dao
interface UserDao {
    @Insert
    fun saveUser(user: User)

    @Delete
    fun removeUser(user: User)

    @Query("SELECT * FROM user WHERE user_ID LIKE :uid")
    fun getUser(uid: Int): User

    @Update
    fun editUser(user: User)
}