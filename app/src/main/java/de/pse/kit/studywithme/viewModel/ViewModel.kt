package de.pse.kit.studywithme.viewModel


import androidx.lifecycle.ViewModel as ViewModel_
import androidx.navigation.NavController

open class ViewModel(val navController: NavController): ViewModel_() {
    fun navBack() {
        navController.popBackStack()
    }
}