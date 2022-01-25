package de.pse.kit.studywithme.viewModel.auth

import androidx.navigation.NavController
import de.pse.kit.studywithme.ui.view.navigation.NavGraph
import de.pse.kit.studywithme.viewModel.ViewModel

class SignInViewModel(navController: NavController) : ViewModel(navController) {

    fun navToSignUp() {
        navController.navigate(NavGraph.SignUp.route)
    }
}