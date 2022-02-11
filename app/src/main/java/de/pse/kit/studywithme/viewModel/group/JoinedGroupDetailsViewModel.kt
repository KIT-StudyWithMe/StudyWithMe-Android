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

@ExperimentalCoroutinesApi
class JoinedGroupDetailsViewModel(
    navController: NavController,
    val groupID: Int,
    val groupRepo: GroupRepositoryInterface,
    val sessionRepo: SessionRepositoryInterface
) : SignedInViewModel(navController) {

    val group: MutableState<Group?> = mutableStateOf(null)
    val members: MutableState<List<GroupMember>> = mutableStateOf(emptyList())
    val sessions: MutableState<List<Session>> = mutableStateOf(emptyList())
    val isAdmin: MutableState<Boolean> = mutableStateOf(false)
    val openReportDialog: MutableState<Boolean> = mutableStateOf(false)
    val groupReports: MutableSet<GroupField> = mutableSetOf()
    val sessionReports: MutableSet<SessionField> = mutableSetOf()

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
        }
    }

    fun editGroup() {
        NavGraph.navigateToEditGroup(navController, groupID)
    }

    fun report() {
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

    fun participate() {
        if (!sessions.value.isEmpty()) {
            sessionRepo.newAttendee(sessions.value[0].sessionID)
        }
    }
}