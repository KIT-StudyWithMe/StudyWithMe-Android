package de.pse.kit.studywithme.viewModel.group

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import de.pse.kit.studywithme.model.data.*
import de.pse.kit.studywithme.model.repository.GroupRepositoryInterface
import de.pse.kit.studywithme.model.repository.SessionRepositoryInterface
import de.pse.kit.studywithme.ui.view.navigation.NavGraph
import de.pse.kit.studywithme.viewModel.SignedInViewModel
import de.pse.kit.studywithme.viewModel.ViewModel
import kotlinx.coroutines.launch

/**
 * ViewModel of nonjoinedgroupdetails screen
 *
 * @property groupID
 * @property groupRepo
 * @constructor
 *
 * @param navController
 */
class NonJoinedGroupDetailsViewModel(
    navController: NavController,
    val groupID: Int,
    val groupRepo: GroupRepositoryInterface
) : SignedInViewModel(navController) {
    val group: MutableState<Group?> = mutableStateOf(null)
    val alreadyRequested: MutableState<Boolean> = mutableStateOf(false)
    val admins: MutableState<List<GroupMember>> = mutableStateOf(emptyList())
    val openReportDialog: MutableState<Boolean> = mutableStateOf(false)
    val groupReports: MutableSet<GroupField> = mutableSetOf()


    init {
        refreshNonJoinedGroupDetails()
    }

    /**
     * Refresh non joined group details
     *
     */
    fun refreshNonJoinedGroupDetails() {
        viewModelScope.launch {
            groupRepo.getGroup(groupID).collect {
                group.value = it
            }
        }
        viewModelScope.launch {
            groupRepo.getGroupAdmins(groupID).collect {
                admins.value = it
            }
        }
        viewModelScope.launch {
            alreadyRequested.value = groupRepo.hasSignedInUserJoinRequested(groupID)
        }
    }

    /**
     * Report of the group is being created
     *
     */
    fun report() {
        if (group.value != null) {
            for (field in groupReports) {
                viewModelScope.launch {
                    groupRepo.reportGroup(groupID, field)
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
     * Join request to the group is being created
     *
     */
    fun joinRequest() {
        viewModelScope.launch {
            if (groupRepo.joinRequest(groupID)) {
                alreadyRequested.value = true
                // navBack()
            }
        }
    }
}

class NonJoinedGroupDetailsViewModelFactory(
    private val navController: NavController,
    private val groupID: Int,
    private val groupRepo: GroupRepositoryInterface
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T =
        NonJoinedGroupDetailsViewModel(navController, groupID, groupRepo) as T
}