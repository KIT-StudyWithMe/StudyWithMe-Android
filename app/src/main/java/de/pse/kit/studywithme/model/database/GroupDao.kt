package de.pse.kit.studywithme.model.database

import androidx.room.*
import de.pse.kit.studywithme.model.data.GroupMember
import de.pse.kit.studywithme.model.data.Lecture
import de.pse.kit.studywithme.model.data.*

@Dao
interface GroupDao {
    @Query("SELECT * FROM remotegroup")
    fun getGroups(): List<RemoteGroup>

    /*
    @Query("SELECT * FROM `Group` WHERE group_ID in " +
            "(SELECT g.group_ID FROM `group` g, GroupMember gm, User u " +
            "WHERE g.group_ID == gm.group_ID AND u.user_ID == :userID AND gm.user_ID == :userID)"
    )
    fun getJoinedGroups(userID: Int): List<Group>
    */

    @Query("SELECT * FROM remotegroup WHERE group_ID LIKE :groupID")
    fun getGroup(groupID: Int): RemoteGroup

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveGroup(group: RemoteGroup)

    @Update
    fun editGroup(group: RemoteGroup)

    @Delete
    fun removeGroup(group: RemoteGroup)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun newMember(groupMember: GroupMember)

    @Query("DELETE FROM groupmember WHERE group_ID == :groupID AND user_ID == :uid")
    fun removeMember(groupID: Int, uid: Int)

    /*
    @Query("SELECT * FROM user WHERE user_ID in (" +
                "SELECT u.user_ID FROM user u, groupmember gm, remotegroup g " +
                "WHERE u.user_ID == gm.user_ID AND gm.group_ID == :groupID AND g.group_ID == :groupID)")
    fun getGroupMembers(groupID: Int): List<User>
     */

    @Query("SELECT * FROM groupmember WHERE group_ID LIKE :groupID")
    fun getGroupMembers(groupID: Int): List<GroupMember>

    @Query("SELECT * FROM lecture WHERE lecture_ID in (" +
                "SELECT l.lecture_ID FROM lecture l, remotegroup g WHERE l.lecture_ID == g.lecture_ID )" +
                "AND lecture_name LIKE (:prefix + '%')")
    fun getLectures(prefix: String): List<Lecture>

    @Query("SELECT * FROM lecture WHERE lecture_ID LIKE :lectureID")
    fun getLecture(lectureID: Int): Lecture

    @Query("SELECT * FROM major WHERE major_ID LIKE :majorID")
    fun getMajor(majorID: Int): Major

    @Insert
    fun saveLecture(lecture: Lecture)

    @Insert
    fun saveMajor(major: Major)
}