package de.pse.kit.studywithme.viewModel.auth

import androidx.navigation.NavController
import de.pse.kit.studywithme.ui.view.navigation.NavGraph
import de.pse.kit.studywithme.viewModel.ViewModel

class SignUpViewModel(navController: NavController) : ViewModel(navController) {

    fun navToSignIn() {
        navController.navigate(NavGraph.SignIn.route)
    }
}