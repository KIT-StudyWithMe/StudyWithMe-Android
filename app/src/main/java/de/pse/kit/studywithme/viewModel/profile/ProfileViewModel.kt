package de.pse.kit.studywithme.viewModel.profile

import androidx.navigation.NavController
import de.pse.kit.studywithme.model.repository.UserRepository
import de.pse.kit.studywithme.model.repository.UserRepositoryInterface
import de.pse.kit.studywithme.ui.view.navigation.NavGraph
import de.pse.kit.studywithme.viewModel.SignedInViewModel
import de.pse.kit.studywithme.viewModel.ViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.runBlocking

@ExperimentalCoroutinesApi
class ProfileViewModel(navController: NavController, val userRepo: UserRepositoryInterface) :
    SignedInViewModel(navController) {

    var username: String = ""
    var contact: String = ""
    var college: String = ""
    var major: String = ""

    init {
        runBlocking {
            try {
                userRepo.getSignedInUser().collect {
                    username = it.name
                    contact = it.contact
                    college = it.college ?: ""
                    major = it.major ?: ""
                }
            } catch (e: Exception) {

            }
        }
    }

    fun navToEditProfile() {
        NavGraph.navigateToEditProfile(navController)
    }

    fun signOut() {
        if (userRepo.signOut()) {
            NavGraph.navigateToSignIn(navController)
        }
    }
}
