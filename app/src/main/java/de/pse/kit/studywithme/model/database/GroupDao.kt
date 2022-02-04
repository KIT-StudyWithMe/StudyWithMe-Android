package de.pse.kit.studywithme.model.database

import androidx.room.*
import de.pse.kit.studywithme.model.data.Group
import de.pse.kit.studywithme.model.data.Lecture
import de.pse.kit.studywithme.model.data.User

@Dao
interface GroupDao {
    @Query("SELECT * FROM `group`")
    fun getGroups(): List<Group>

    @Query("")
    fun getJoinedGroups(userID: Int): List<Group>
    //TODO(query)

    @Query("SELECT * FROM `group` WHERE group_ID LIKE :groupID" )
    fun getGroup(groupID: Int): Group

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveGroup(group: Group)
    //TODO(query)

    @Update
    fun editGroup(group: Group)

    @Delete
    fun removeGroup(group: Group)

    @Query("")
    fun newMember(groupID: Int, uid: Int)
    //TODO(query)

    @Query("")
    fun removeMember(groupID: Int, uid: Int)
    //TODO(query)

    @Query("")
    fun getGroupMembers(groupID: Int):  List <User>
    //TODO(query)

    @Query("")
    fun getLectures(prefix: String): List<Lecture>







}