package de.pse.kit.studywithme.model.database

import androidx.room.*
import de.pse.kit.studywithme.model.data.Group
import de.pse.kit.studywithme.model.data.GroupMember
import de.pse.kit.studywithme.model.data.Lecture
import de.pse.kit.studywithme.model.data.User

@Dao
interface GroupDao {
    @Query("SELECT * FROM `group`")
    fun getGroups(): List<Group>

    @Query("SELECT * FROM `Group` WHERE group_ID in " +
            "(SELECT g.group_ID FROM `group` g, GroupMember gm, User u " +
            "WHERE g.group_ID == gm.group_ID AND u.user_ID == :userID AND gm.user_ID == :userID)"
    )
    fun getJoinedGroups(userID: Int): List<Group>


    @Query("SELECT * FROM `group` WHERE group_ID == :groupID" )
    fun getGroup(groupID: Int): Group

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveGroup(group: Group)

    @Update
    fun editGroup(group: Group)

    @Delete
    fun removeGroup(group: Group)

    @Query("")
    fun newMember(groupID: Int, uid: Int)
    //TODO(query)

    @Query("DELETE FROM GroupMember WHERE group_ID == :groupID AND user_ID == :uid")
    fun removeMember(groupID: Int, uid: Int)

    @Query("SELECT * FROM user WHERE user_ID in (" +
            "SELECT u.user_ID FROM user u, GroupMember gm, `group` g " +
            "WHERE u.user_ID == gm.user_ID AND gm.group_ID == :groupID AND g.group_ID == :groupID)")
    fun getGroupMembers(groupID: Int):  List <User>


    @Query("SELECT * FROM lecture WHERE lecture_ID in (" +
            "SELECT lecture_ID FROM lecture l, `group` g WHERE l.lecture_ID == g.lecture_ID )" +
            "AND lecture_name LIKE (:prefix + '%')")
    fun getLectures(prefix: String): List<Lecture>







}