package de.pse.kit.studywithme.viewModel.group

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import de.pse.kit.studywithme.model.data.Group
import de.pse.kit.studywithme.model.repository.GroupRepositoryInterface
import de.pse.kit.studywithme.ui.view.navigation.NavGraph
import de.pse.kit.studywithme.viewModel.SignedInViewModel
import kotlinx.coroutines.launch

/**
 * ViewModel of the joinedgroup screen
 *
 * @property groupRepo
 * @constructor
 *
 * @param navController
 */
class JoinedGroupsViewModel(
    navController: NavController, val groupRepo: GroupRepositoryInterface
) : SignedInViewModel(navController) {

    private var groups: List<Group> = emptyList()
    private var filter: String = ""
    var filteredGroups: MutableState<List<Group>> = mutableStateOf(emptyList())
    var lectures: List<String> = emptyList()

    init {
        refreshJoinedGroups()
    }

    /**
     * Refreshes joined groups
     *
     */
    fun refreshJoinedGroups() {
        viewModelScope.launch {
            groupRepo.getJoinedGroups().collect {
                groups = it
                filteredGroups.value = it
                lectures = groups.map {
                    it.lecture?.lectureName
                }.distinct().filterNotNull()
            }
        }
    }

    /**
     * Navigates to the joinedgroup view
     *
     * @param group
     */
    fun navToGroup(group: Group) {
        NavGraph.navigateToJoinedGroup(navController, group.groupID)
    }

    /**
     * Filters groups after given lecture
     *
     * @param lecture
     */
    fun filter(lecture: String) {
        if (lecture == filter) {
            filteredGroups.value = groups
            filter = ""
        } else {
            filter = lecture

            filteredGroups.value = groups.filter {
                it.lecture?.lectureName == lecture
            }
        }
    }
}

class JoinedGroupsViewModelFactory(
    private val navController: NavController,
    private val groupRepo: GroupRepositoryInterface
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T =
        JoinedGroupsViewModel(navController, groupRepo) as T
}