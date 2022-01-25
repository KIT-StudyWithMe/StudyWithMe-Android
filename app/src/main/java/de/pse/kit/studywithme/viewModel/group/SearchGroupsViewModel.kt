package de.pse.kit.studywithme.viewModel.group

import androidx.navigation.NavController
import de.pse.kit.studywithme.ui.view.navigation.NavGraph
import de.pse.kit.studywithme.viewModel.ViewModel

class SearchGroupsViewModel(navController: NavController) : ViewModel(navController) {
    //TODO: implement search
    fun search(prefix: String) {
    }

    fun navToGroup(groupID: Int) {
        NavGraph.navigateToNonJoinedGroup(navController, groupID)
    }
}
