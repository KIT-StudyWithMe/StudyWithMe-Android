package de.pse.kit.studywithme.viewModel

import androidx.navigation.NavController
import de.pse.kit.studywithme.ui.view.navigation.NavGraph

/**
 * Navigation of the bottom bar
 *
 * @constructor
 *
 * @param navController
 */
open class SignedInViewModel(navController: NavController): ViewModel(navController) {

    /**
     * Navigates to joinedgroups view
     *
     */
    fun navToJoinedGroups() {
        NavGraph.navigateToJoinedGroups(navController)
    }

    /**
     * Navigates to searchgroups view
     *
     */
    fun navToSearchGroups() {
        NavGraph.navigateToSearchGroups(navController)
    }

    /**
     * Navigates to profile view
     *
     */
    fun navToProfile() {
        NavGraph.navigateToProfile(navController)
    }
}