package de.pse.kit.studywithme.model.database

import androidx.room.*
import de.pse.kit.studywithme.model.data.GroupMember
import de.pse.kit.studywithme.model.data.Lecture
import de.pse.kit.studywithme.model.data.*

@Dao
interface GroupDao {
    @Query("SELECT * FROM remotegroup")
    suspend fun getGroups(): List<RemoteGroup>?

    /*
    @Query("SELECT * FROM `Group` WHERE group_ID in " +
            "(SELECT g.group_ID FROM `group` g, GroupMember gm, User u " +
            "WHERE g.group_ID == gm.group_ID AND u.user_ID == :userID AND gm.user_ID == :userID)"
    )
    fun getJoinedGroups(userID: Int): List<Group>
    */

    @Query("SELECT * FROM remotegroup WHERE group_ID LIKE :groupID")
    suspend fun getGroup(groupID: Int): RemoteGroup?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveGroup(group: RemoteGroup)

    @Update
    suspend fun editGroup(group: RemoteGroup)

    @Delete
    suspend fun removeGroup(group: RemoteGroup)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun newMember(groupMember: GroupMember)

    @Query("DELETE FROM groupmember WHERE group_ID == :groupID AND user_ID == :uid")
    suspend fun removeMember(groupID: Int, uid: Int)

    @Query("SELECT * FROM groupmember WHERE group_ID LIKE :groupID")
    suspend fun getGroupMembers(groupID: Int): List<GroupMember>?

    @Query("SELECT * FROM lecture WHERE lecture_ID LIKE :lectureID")
    suspend fun getLecture(lectureID: Int): Lecture?

    @Query("SELECT * FROM major WHERE major_ID LIKE :majorID")
    suspend fun getMajor(majorID: Int): Major?

    @Insert
    suspend fun saveLecture(lecture: Lecture)

    @Insert
    suspend fun saveMajor(major: Major)
}