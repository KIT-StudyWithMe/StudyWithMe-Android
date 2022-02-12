package de.pse.kit.studywithme.viewModel.group

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.navigation.NavController
import de.pse.kit.studywithme.model.data.Group
import de.pse.kit.studywithme.model.repository.GroupRepositoryInterface
import de.pse.kit.studywithme.ui.view.navigation.NavGraph
import de.pse.kit.studywithme.viewModel.SignedInViewModel

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

    init {
        //groups.value = groupRepo.getGroupSuggestions()
    }

    /**
     * Seacrhes groups with the given prefix
     *
     * @param prefix
     */
    fun search(prefix: String) {
        groups.value = groupRepo.getGroups(prefix)
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
