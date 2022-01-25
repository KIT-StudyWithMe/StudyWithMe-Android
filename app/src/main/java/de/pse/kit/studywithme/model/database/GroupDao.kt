package de.pse.kit.studywithme.model.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import de.pse.kit.studywithme.model.data.Group

@Dao
interface GroupDao {
    @Query("SELECT * FROM `group`")
    fun getGroups(userID: Int)

    @Query("SELECT * FROM `group` WHERE group_ID LIKE :groupID" )
    fun getGroup(groupID: Int)

    @Insert
    fun saveGroup(group: Group)

    @Update
    fun editGroup(group: Group)

    @Delete
    fun removeGroup(group: Group)
}