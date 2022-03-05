package de.pse.kit.studywithme.model.repository

import de.pse.kit.studywithme.model.data.*
import kotlinx.coroutines.flow.Flow

interface GroupRepositoryInterface {

    /**
     * Returns list of groups which would be the outcome of a search request with string search
     *
     * @param search
     * @return list of group object
     */
    suspend fun getGroups(search: String): List<Group>

    /**
     * List of groups a user joined
     *
     * @return flow list of group object
     */
    suspend fun getJoinedGroups(): Flow<List<Group>>

    /**
     * List of groups that are suggested
     *
     * @return list of group object
     */
    suspend fun getGroupSuggestions(): List<Group>

    /**
     * Returns flow of group with the ID groupID
     *
     * @param groupID
     * @return flow of group object
     */
    suspend fun getGroup(groupID: Int): Flow<Group>

    /**
     * Creates new group with object group and returns true if successful
     *
     * @param group
     * @return true or false
     */
    suspend fun newGroup(group: Group): Boolean

    /**
     * Edits the group with edited group object group and returns true if successful
     *
     * @param group
     * @return true or false
     */
    suspend fun editGroup(group: Group): Boolean

    /**
     * The User with ID uid is deleted from the group with ID groupID
     *
     * @param groupID
     * @param uid
     */
    suspend fun exitGroup(groupID: Int, uid: Int)

    /**
     * Delete the given group from database and returns true if successful
     *
     * @param group
     * @return false or true
     */
    suspend fun deleteGroup(group: Group): Boolean

    /**
     * Toggles to hide status of a group.
     * If hidden, the group won't return as a search result.
     * Returns true if successful
     *
     * @param groupID
     * @return true or false
     */
    suspend fun hideGroup(groupID: Int): Boolean

    /**
     * Adds the user with ID uid to the group with ID groupID and returns true if successful
     *
     * @param groupID
     * @param uid
     * @return false or true
     */
    suspend fun newMember(groupID: Int, uid: Int): Boolean

    /**
     * Declines the join request by user with ID uid to the group with ID groupID and returns true if successful
     *
     * @param groupID
     * @param uid
     * @return false or true
     */
    suspend fun declineMember(groupID: Int, uid: Int): Boolean

    /**
     * Creates join request of a user to the group with ID groupID and returns true if successful
     *
     * @param groupID
     * @return true or false
     */
    suspend fun joinRequest(groupID: Int): Boolean

    /**
     * Returns a list of user which sent a join request to the group with ID groupID
     *
     * @param groupID
     * @return list of userlight object
     */
    suspend fun getJoinRequests(groupID: Int): List<UserLight>

    /**
     * Removes the user with ID uid from the group with ID groupID
     *
     * @param groupID
     * @param uid
     */
    suspend fun removeMember(groupID: Int, uid: Int)

    /**
     * The user will be removed from the group with ID groupID
     *
     * @param groupID
     */
    suspend fun leaveGroup(groupID: Int)

    /**
     * Returns all members of the group with ID groupID
     *
     * @param groupID
     * @return flow list of groupmember object
     */
    suspend fun getGroupMembers(groupID: Int): Flow<List<GroupMember>>

    /**
     * Returns a list of all group admins of the group with ID groupID
     *
     * @param groupID
     * @return
     */
    suspend fun getGroupAdmins(groupID: Int): Flow<List<GroupMember>>

    /**
     * Returns true if the group with ID groupID has a signed in user admin
     *
     * @param groupID
     * @return true or false as flow
     */
    suspend fun isSignedInUserAdmin(groupID: Int): Flow<Boolean>

    /**
     * Returns true if the group with ID groupID has join requests of signed in users
     *
     * @param groupID
     * @return true or false
     */
    suspend fun hasSignedInUserJoinRequested(groupID: Int): Boolean

    /**
     * Returns list of lectures which start with the given prefix
     *
     * @param prefix
     * @return flow list of lecture object
     */
    suspend fun getLectures(prefix: String): Flow<List<Lecture>>

    /**
     * Returns the lecture with the given name or null if there is none
     *
     * @param name
     * @return lecture object or null
     */
    suspend fun getLecture(name: String): Lecture?

    /**
     * Returns a detailed report of the group with ID groupID
     *
     * @param groupID
     * @param groupField
     */
    suspend fun reportGroup(groupID: Int, groupField: GroupField)

    /**
     * Returns a detailed report of the user if ID uid
     *
     * @param userID
     * @param userField
     */
    suspend fun reportUser(userID: Int, userField: UserField)

    /**
     * Returns the hidden status of a group
     *
     * @param groupID
     * @return true or false
     */
    suspend fun isGroupHidden(groupID: Int): Boolean
}