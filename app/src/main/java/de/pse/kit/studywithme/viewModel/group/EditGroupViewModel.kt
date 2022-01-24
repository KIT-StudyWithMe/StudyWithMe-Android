package de.pse.kit.studywithme.viewModel.group

import androidx.navigation.NavController
import de.pse.kit.studywithme.ui.view.navigation.NavGraph
import de.pse.kit.studywithme.viewModel.ViewModel

class EditGroupViewModel(
    navController: NavController,
    val groupID: Int
) : ViewModel(navController) {
}