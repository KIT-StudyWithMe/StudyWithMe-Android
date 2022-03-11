package de.pse.kit.studywithme.viewModel.group

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import de.pse.kit.studywithme.model.data.Group
import de.pse.kit.studywithme.model.repository.GroupRepositoryInterface
import de.pse.kit.studywithme.ui.view.navigation.NavGraph
import de.pse.kit.studywithme.viewModel.SignedInViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * ViewModel of the searchgroups screen
 *
 * @property groupRepo
 * @constructor
 *
 * @param navController
 */
class SearchGroupsViewModel(navController: NavController, val groupRepo: GroupRepositoryInterface) :
    SignedInViewModel(navController) {

    var groups: MutableState<List<Group>> = mutableStateOf(emptyList())
    var search: Job? = null

    init {
        viewModelScope.launch {
            groups.value = groupRepo.getGroupSuggestions()
        }
    }

    fun refreshGroups() {
        viewModelScope.launch {
            groups.value = emptyList()
            groups.value = groupRepo.getGroupSuggestions()
        }
    }

    /**
     * Seacrhes groups with the given prefix
     *
     * @param prefix
     */
    fun search(prefix: String) {
        search?.cancel()
        search = viewModelScope.launch {
            groups.value = groupRepo.getGroups(prefix.trim())
            Log.d("TEST", "GROUP SEARCH")
        }
    }

    /**
     * Navigates to nonjoinedgroup view
     *
     * @param group
     */
    fun navToGroup(group: Group) {
        NavGraph.navigateToNonJoinedGroup(navController, group.groupID)
    }

    /**
     * Navigates to newgroup view
     *
     */
    fun newGroup() {
        NavGraph.navigateToNewGroup(navController)
    }
}

class SearchGroupsViewModelFactory(
    private val navController: NavController,
    private val groupRepo: GroupRepositoryInterface
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T =
        SearchGroupsViewModel(navController, groupRepo) as T
}
