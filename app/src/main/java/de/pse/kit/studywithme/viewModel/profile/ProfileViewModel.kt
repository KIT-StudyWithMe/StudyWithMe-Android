package de.pse.kit.studywithme.viewModel.profile

import androidx.navigation.NavController
import de.pse.kit.studywithme.ui.view.navigation.NavGraph
import de.pse.kit.studywithme.viewModel.SignedInViewModel
import de.pse.kit.studywithme.viewModel.ViewModel

class ProfileViewModel(navController: NavController) : SignedInViewModel(navController) {

    fun navigateToEditProfile() {
        navController.navigate(NavGraph.EditProfile.route)
    }
}
