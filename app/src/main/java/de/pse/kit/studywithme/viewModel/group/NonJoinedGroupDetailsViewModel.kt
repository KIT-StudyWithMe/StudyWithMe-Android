package de.pse.kit.studywithme.viewModel.group

import androidx.navigation.NavController
import de.pse.kit.studywithme.viewModel.SignedInViewModel
import de.pse.kit.studywithme.viewModel.ViewModel

class NonJoinedGroupDetailsViewModel(
    navController: NavController,
    val groupID: Int,
) : SignedInViewModel(navController) {
}