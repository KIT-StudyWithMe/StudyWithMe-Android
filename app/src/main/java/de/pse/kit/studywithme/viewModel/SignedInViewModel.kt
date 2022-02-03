package de.pse.kit.studywithme.viewModel

import androidx.navigation.NavController
import de.pse.kit.studywithme.ui.view.navigation.NavGraph

open class SignedInViewModel(navController: NavController): ViewModel(navController) {

    fun navToJoinedGroups() {
        NavGraph.navigateToJoinedGroups(navController)
    }

    fun navToSearchGroups() {
        NavGraph.navigateToSearchGroups(navController)
    }

    fun navToProfile() {
        NavGraph.navigateToProfile(navController)
    }
}