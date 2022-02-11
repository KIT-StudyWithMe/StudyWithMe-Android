package de.pse.kit.studywithme.model.database

import androidx.room.*
import de.pse.kit.studywithme.model.data.GroupMember
import de.pse.kit.studywithme.model.data.Lecture
import de.pse.kit.studywithme.model.data.*

/**
 * Interface for management of local database for object group
 *
 * @constructor Create empty Group dao
 */
@Dao
interface GroupDao {
    /**
     * Returns a list of groups communicated with the server
     *
     * @return List of remotegroup objects
     */
    @Query("SELECT * FROM remotegroup")
    suspend fun getGroups(): List<RemoteGroup>?

    /**
     * Gives out the group with the given groupID or null if there is no such group with this groupID
     *
     * @param groupID
     * @return remotegroup object which might be null
     */
    @Query("SELECT * FROM remotegroup WHERE group_ID LIKE :groupID")
    suspend fun getGroup(groupID: Int): RemoteGroup?

    /**
     * Saves remotegroup object on the server
     *
     * @param group
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveGroup(group: RemoteGroup)

    /**
     * Edits remotegroup object on the server
     *
     * @param group
     */
    @Update
    suspend fun editGroup(group: RemoteGroup)

    /**
     * Removes remotegroup object on the server
     *
     * @param group
     */
    @Delete
    suspend fun removeGroup(group: RemoteGroup)

    /**
     * Adds new member to a group
     *
     * @param groupMember
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun newMember(groupMember: GroupMember)

    /**
     * Removes a member with the given uid of the group with the given groupID
     *
     * @param groupID
     * @param uid
     */
    @Query("DELETE FROM groupmember WHERE group_ID == :groupID AND user_ID == :uid")
    suspend fun removeMember(groupID: Int, uid: Int)

    /**
     * Returns a list of all members of the group with the given groupID
     *
     * @param groupID
     * @return List of groupmember objects
     */
    @Query("SELECT * FROM groupmember WHERE group_ID LIKE :groupID")
    suspend fun getGroupMembers(groupID: Int): List<GroupMember>?

    /**
     * Returns the lecture with the given lectureID or null if there is none with this lectureID
     *
     * @param lectureID
     * @return Lecture object or null
     */
    @Query("SELECT * FROM lecture WHERE lecture_ID LIKE :lectureID")
    suspend fun getLecture(lectureID: Int): Lecture?

    /**
     * Returns the major with the given majorID or null if there is none with this majorID
     *
     * @param majorID
     * @return Major object or null
     */
    @Query("SELECT * FROM major WHERE major_ID LIKE :majorID")
    suspend fun getMajor(majorID: Int): Major?

    /**
     * Saves the lecture in the database
     *
     * @param lecture
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveLecture(lecture: Lecture)

    /**
     * Saves the major in the database
     *
     * @param major
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveMajor(major: Major)
}