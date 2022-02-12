package de.pse.kit.studywithme.model.network

import de.pse.kit.studywithme.model.data.*
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*

interface GroupService {
    /**
     * Returns list of groups from server database which fit the search request or null if there are none
     *
     * @param search
     * @return List of reamotegroup object or null
     */
    suspend fun getGroups(search: String): List<RemoteGroup>?

    /**
     * Returns list of groups user with id uid joined or null if there are none
     *
     * @param uid
     * @return list of remotegroup object or null
     */
    suspend fun getJoinedGroups(uid: Int): List<RemoteGroup>?

    /**
     * Returns list of groups suggested to the user with id uid or null if there are none
     *
     * @param uid
     * @return list of remore group object or null
     */
    suspend fun getGroupSuggestions(uid: Int): List<RemoteGroup>?

    /**
     * Returns group with the ID groupID oer null if it doesn't exist
     *
     * @param groupID
     * @return remotegroup object or null
     */
    suspend fun getGroup(groupID: Int): RemoteGroup?

    /**
     * Retruns a list of all members of the group with the ID froupID or null if there are none
     *
     * @param groupID
     * @return list of groupmember object or null
     */
    suspend fun getGroupMembers(groupID: Int): List<GroupMember>?

    /**
     * Creates new group
     *
     * @param group
     * @param userID
     * @return remotegroup object or null
     */
    suspend fun newGroup(group: RemoteGroup, userID: Int): RemoteGroup?

    /**
     * Edits group
     *
     * @param groupID
     * @param group
     * @return remote group object or null
     */
    suspend fun editGroup(groupID: Int, group: RemoteGroup): RemoteGroup?

    /**
     * Removes group with ID groupID and returns true if it was successful
     *
     * @param groupID
     * @return true or false
     */
    suspend fun removeGroup(groupID: Int): Boolean

    /**
     * Hides the group from other users search request and returns true if it was successful
     *
     * @param groupID
     * @param hidden
     * @return true or false
     */
    suspend fun hideGroup(groupID: Int, hidden:Boolean): Boolean

    /**
     * Creates a new group member
     *
     * @param groupID
     * @param uid
     * @return groupmember object or null
     */
    suspend fun newMember(groupID: Int, uid: Int):GroupMember?

    /**
     * Creates join request by the user with ID uid to the group with ID groupID and returns true if successful
     *
     * @param groupID
     * @param uid
     * @return true or false
     */
    suspend fun joinRequest(groupID: Int, uid:Int): Boolean

    /**
     * Returns a list of join request the group with ID groupID received or null if there are none
     *
     * @param groupID
     * @return list of userlight object or null
     */
    suspend fun getJoinRequests(groupID: Int): List<UserLight>?

    /**
     * Removes the user with ID uid from the group with ID groupID and returns true if it was successful
     *
     * @param groupID
     * @param uid
     * @return true or false
     */
    suspend fun removeMember(groupID: Int, uid: Int): Boolean

    /**
     * Returns a list of lectures which start with the given prefix or null if there are none
     *
     * @param majorID
     * @param prefix
     * @return list of lecture object or null
     */
    suspend fun getLectures(majorID: Int, prefix: String): List<Lecture>?

    /**
     * Returns the lecture with the ID lectureID or null if there is none
     *
     * @param lectureID
     * @return lecture object or null
     */
    suspend fun getLecture(lectureID: Int): Lecture?

    /**
     * Creates a new lecture
     *
     * @param lecture
     * @param majorID
     * @return lecture object or null
     */
    suspend fun newLecture(lecture: Lecture, majorID: Int): Lecture?

    /**
     * Returns major with ID majorID
     *
     * @param majorID
     * @return major object or null
     */
    suspend fun getMajor(majorID: Int): Major?

    companion object {
        val instance: GroupService by lazy {
            GroupServiceImpl(client = HttpClient(Android) {
                install(JsonFeature) {
                    serializer = KotlinxSerializer()
                }
            })
        }
    }
}