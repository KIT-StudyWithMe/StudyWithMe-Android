package de.pse.kit.studywithme.viewModel.group

import androidx.navigation.NavController
import de.pse.kit.studywithme.model.data.Group
import de.pse.kit.studywithme.model.repository.GroupRepositoryInterface
import de.pse.kit.studywithme.ui.view.navigation.NavGraph
import de.pse.kit.studywithme.viewModel.SignedInViewModel
import de.pse.kit.studywithme.viewModel.ViewModel

class SearchGroupsViewModel(navController: NavController, val groupRepo: GroupRepositoryInterface) :
    SignedInViewModel(navController) {

    var groups: List<Group> = emptyList()

    init {
        groups = groupRepo.getGroupSuggestions()
    }

    fun search(prefix: String) {
        groups = groupRepo.getGroups(prefix)
    }

    fun navToGroup(group: Group) {
        NavGraph.navigateToNonJoinedGroup(navController, group.groupID)
    }

    fun newGroup() {
        NavGraph.navigateToNewGroup(navController)
    }
}
