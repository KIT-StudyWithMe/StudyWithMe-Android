package de.pse.kit.studywithme.viewModel.group

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import de.pse.kit.studywithme.model.data.*
import de.pse.kit.studywithme.ui.view.navigation.NavGraph
import de.pse.kit.studywithme.model.repository.GroupRepositoryInterface
import de.pse.kit.studywithme.model.repository.SessionRepositoryInterface
import de.pse.kit.studywithme.viewModel.SignedInViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

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
    val sessionAttendees: MutableState<List<SessionAttendee>> = mutableStateOf(emptyList())
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
        refreshJoinedGroupDetails()
    }

    fun refreshJoinedGroupDetails() {
        viewModelScope.launch {
            groupRepo.getGroup(groupID).collect {
                group.value = it
            }
        }
        viewModelScope.launch {
            groupRepo.getGroupMembers(groupID).collect {
                members.value = it
            }
        }
        viewModelScope.launch {
            sessionRepo.getSessions(groupID).collect {
                sessions.value = it
                if (it.isNotEmpty()) {
                    sessionRepo.getAttendees(it.first().sessionID).collect {
                        sessionAttendees.value = it
                    }
                }
            }
        }
        viewModelScope.launch {
            groupRepo.isSignedInUserAdmin(groupID).collect {
                isAdmin.value = it
                if (it) {
                    requests.value = groupRepo.getJoinRequests(groupID)
                }
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
                viewModelScope.launch {
                    sessionRepo.reportSession(sessions.value.first().sessionID, field)
                }
            }
        }

        if (group.value != null) {
            for (field in groupReports) {
                viewModelScope.launch {
                    groupRepo.reportGroup(groupID, field)
                }
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
        if (sessions.value.isNotEmpty()) {
            viewModelScope.launch {
                sessionRepo.newAttendee(sessions.value.first().sessionID)
                sessionRepo.getAttendees(sessions.value.first().sessionID).collect {
                    sessionAttendees.value = it
                }
            }
        }
    }

    /**
     * Report of a user is being created
     *
     * @param userField
     */
    fun reportUser(userField: UserField) {
        if (clickedUser.value != null) {
            viewModelScope.launch {
                groupRepo.reportUser(clickedUser.value!!.userID, userField)
            }
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
            viewModelScope.launch {
                groupRepo.removeMember(groupID, clickedUser.value!!.userID)
            }
        }
    }

    /**
     * Group is being left by the user
     *
     */
    fun leaveGroup() {
        if (isAdmin.value && members.value.count() == 1) {
            deleteGroup()
            return
        }
        viewModelScope.launch {
            groupRepo.leaveGroup(groupID)
        }
        navBack()
    }

    /**
     * Deletes a group and navigates to last view
     *
     */
    fun deleteGroup() {
        if (group.value != null) {
            viewModelScope.launch {
                groupRepo.deleteGroup(group.value!!)
            }
            navBack()
        }
    }

    /**
     * Join request is being accepted if accept is true
     *
     * @param accept
     */
    fun acceptRequest(accept: Boolean) {
        if (clickedUser.value != null && accept) {
            viewModelScope.launch {
                groupRepo.newMember(groupID, clickedUser.value!!.userID)
                launch {
                    groupRepo.getGroupMembers(groupID).collect {
                        members.value = it
                    }
                }

                launch {
                    requests.value = groupRepo.getJoinRequests(groupID)
                }
            }
        } else if (clickedUser.value != null) {
            viewModelScope.launch {
                groupRepo.declineMember(groupID, clickedUser.value!!.userID)
                requests.value = groupRepo.getJoinRequests(groupID)
            }
        }
    }
}

@ExperimentalCoroutinesApi
class JoinedGroupDetailsViewModelFactory(
    private val navController: NavController,
    private val groupID: Int,
    private val groupRepo: GroupRepositoryInterface,
    private val sessionRepo: SessionRepositoryInterface
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T =
        JoinedGroupDetailsViewModel(navController, groupID, groupRepo, sessionRepo) as T
}