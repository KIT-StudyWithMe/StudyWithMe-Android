package de.pse.kit.studywithme.viewModel

import androidx.navigation.NavController

open class ViewModel(val navController: NavController) {

    fun navBack() {
        navController.popBackStack()
    }
}