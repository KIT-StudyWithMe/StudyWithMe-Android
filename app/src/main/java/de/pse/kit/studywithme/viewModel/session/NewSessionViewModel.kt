package de.pse.kit.studywithme.viewModel.session

import androidx.navigation.NavController
import de.pse.kit.studywithme.viewModel.SignedInViewModel
import de.pse.kit.studywithme.viewModel.ViewModel

class NewSessionViewModel(
    navController: NavController,
    groupID: Int
) : SignedInViewModel(navController) {
}
