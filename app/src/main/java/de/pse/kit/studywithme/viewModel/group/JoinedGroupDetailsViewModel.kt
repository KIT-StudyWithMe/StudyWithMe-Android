package de.pse.kit.studywithme.viewModel.group

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.navigation.NavController
import de.pse.kit.studywithme.model.data.*
import de.pse.kit.studywithme.ui.view.navigation.NavGraph
import de.pse.kit.studywithme.model.repository.GroupRepositoryInterface
import de.pse.kit.studywithme.model.repository.SessionRepositoryInterface
import de.pse.kit.studywithme.viewModel.SignedInViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * ViewModel of the joinedgroupdetails screen
 *
 * @property groupID
 * @property groupRepo
 * @property sessionRepo
 * @constructor
 *
 * @param navController
 */
@ExperimentalCoroutinesApi
class JoinedGroupDetailsViewModel(
    navController: NavController,
    val groupID: Int,
    val groupRepo: GroupRepositoryInterface,
    val sessionRepo: SessionRepositoryInterface
) : SignedInViewModel(navController) {
    
    val group: MutableState<Group?> = mutableStateOf(null)
    val members: MutableState<List<GroupMember>> = mutableStateOf(emptyList())
    val requests: MutableState<List<UserLight>> = mutableStateOf(emptyList())
    val sessions: MutableState<List<Session>> = mutableStateOf(emptyList())
    val isAdmin: MutableState<Boolean> = mutableStateOf(false)
    val openReportDialog: MutableState<Boolean> = mutableStateOf(false)
    val groupReports: MutableSet<GroupField> = mutableSetOf()
    val sessionReports: MutableSet<SessionField> = mutableSetOf()

    val openAdminDialog: MutableState<Boolean> = mutableStateOf(false)
    val openMemberDialog: MutableState<Boolean> = mutableStateOf(false)
    val openRequestDialog: MutableState<Boolean> = mutableStateOf(false)
    
    val clickedUser: MutableState<GroupMember?> = mutableStateOf(null)
    val clickedUserName: MutableState<String> = mutableStateOf("")
    
    init {
        runBlocking {
            launch {
                groupRepo.getGroup(groupID).collect {
                    group.value = it
                }
            }
            launch {
                groupRepo.getGroupMembers(groupID).collect {
                    members.value = it
                }
            }
            launch {
                sessionRepo.getSessions(groupID).collect {
                    sessions.value = it
                }
            }
            launch {
                groupRepo.isSignedInUserAdmin(groupID).collect {
                    isAdmin.value = it
                }
            }
            launch {
                requests.value = groupRepo.getJoinRequests(groupID)
            }
        }
    }

    /**
     * Navigates to editgroup view
     *
     */
    fun editGroup() {
        NavGraph.navigateToEditGroup(navController, groupID)
    }

    /**
     * Report of a group is being created
     *
     */
    fun reportGroup() {
        if (sessions.value.isNotEmpty()) {
            for (field in sessionReports) {
                sessionRepo.reportSession(sessions.value.first().sessionID, field)
            }
        }

        if (group.value != null) {
            for (field in groupReports) {
                groupRepo.reportGroup(groupID, field)
            }
        }
    }

    /**
     * Navigates to newsession view if there is no active session to edit yet otherwise navigates to editsession view
     *
     */
    fun planSession() {
        if (sessions.value.isEmpty()) {
            NavGraph.navigateToNewSession(navController, groupID)
        } else {
            NavGraph.navigateToEditSession(
                navController,
                groupID = groupID,
                sessionID = sessions.value[0].sessionID
            )
        }
    }

    /**
     * Adds user to session as attendant
     *
     */
    fun participate() {
        if (!sessions.value.isEmpty()) {
            sessionRepo.newAttendee(sessions.value[0].sessionID)
        }
    }

    /**
     * Report of a user is being created
     *
     * @param userField
     */
    fun reportUser(userField: UserField) {
        if (clickedUser.value != null) {
            groupRepo.reportUser(clickedUser.value!!.userID, userField)
        }
    }

    /**
     * Makes a user a admin
     *
     */
    fun makeAdmin() {
        if (clickedUser.value != null) {
            //TODO()
        }
    }

    /**
     * Removes a member of the group
     *
     */
    fun removeMember() {
        if (clickedUser.value != null) {
            groupRepo.removeMember(groupID, clickedUser.value!!.userID)
        }
    }

    /**
     * Group is being left by the user
     *
     */
    fun leaveGroup() {
        groupRepo.leaveGroup(groupID)
    }

    /**
     * Join request is being accepted
     *
     * @param accept
     */
    fun acceptRequest(accept: Boolean) {
        if (clickedUser.value != null && accept) {
            groupRepo.newMember(groupID, clickedUser.value!!.userID)
        }
    }
}