package de.pse.kit.studywithme.viewModel.profile

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import de.pse.kit.studywithme.model.auth.AuthException
import de.pse.kit.studywithme.model.repository.UserRepository
import de.pse.kit.studywithme.model.repository.UserRepositoryInterface
import de.pse.kit.studywithme.ui.view.navigation.NavGraph
import de.pse.kit.studywithme.viewModel.SignedInViewModel
import de.pse.kit.studywithme.viewModel.ViewModel
import de.pse.kit.studywithme.viewModel.auth.SignUpViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * ViewModel of the profileview screen
 *
 * @property userRepo
 * @constructor
 *
 * @param navController
 */
@ExperimentalCoroutinesApi
class ProfileViewModel(navController: NavController, val userRepo: UserRepositoryInterface) :
    SignedInViewModel(navController) {

    var username by mutableStateOf("")
    var contact by mutableStateOf("")
    var college by mutableStateOf("")
    var major by mutableStateOf("")
    var signInMail by mutableStateOf("")

    init {
        refreshUser()
    }

    /**
     * Refresh user
     *
     */
    fun refreshUser() {
        viewModelScope.launch {
            try {
                userRepo.getSignedInUser().collect {
                    username = it.name
                    contact = it.contact
                    college = it.college ?: ""
                    major = it.major ?: ""
                }
            } catch (e: AuthException) {
                Log.w("AUTH", e.message)
                userRepo.signOut()
                NavGraph.navigateToSignIn(navController)
            }
        }
        viewModelScope.launch {
            signInMail = userRepo.getUserSignInMail() ?: ""
        }
    }

    /**
     * Navigates to editprofile view
     *
     */
    fun navToEditProfile() {
        NavGraph.navigateToEditProfile(navController)
    }

    /**
     * Signs out user from application by navigating him to the sign in view
     *
     */
    fun signOut() {
        viewModelScope.launch {
            if (userRepo.signOut()) {
                NavGraph.navigateToSignIn(navController)
            }
        }
    }
}

@ExperimentalCoroutinesApi
class ProfileViewModelFactory(
    private val navController: NavController,
    private val userRepo: UserRepositoryInterface
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T =
        ProfileViewModel(navController, userRepo) as T
}

