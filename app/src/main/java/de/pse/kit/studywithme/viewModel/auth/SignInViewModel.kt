package de.pse.kit.studywithme.viewModel.auth


import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import de.pse.kit.studywithme.model.repository.GroupRepositoryInterface
import de.pse.kit.studywithme.model.repository.UserRepository
import de.pse.kit.studywithme.model.repository.UserRepositoryInterface
import de.pse.kit.studywithme.ui.view.navigation.NavGraph
import de.pse.kit.studywithme.viewModel.ViewModel
import de.pse.kit.studywithme.viewModel.group.EditGroupViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel of the signin screen
 *
 * @property userRepo
 * @constructor
 *
 * @param navController
 */
class SignInViewModel(navController: NavController, val userRepo: UserRepositoryInterface) :
    ViewModel(navController) {

    val email: MutableStateFlow<String> = MutableStateFlow("")
    val password: MutableStateFlow<String> = MutableStateFlow("")
    val errorMessage: MutableStateFlow<String> = MutableStateFlow("")

    /**
     * Navigates to signup view
     *
     */
    fun navToSignUp() {
        NavGraph.navigateToSignUp(navController)
    }

    /**
     * Signs in user and navigates them to joiendgroups view if successful
     *
     */
    fun signIn() {
        viewModelScope.launch {
            if (userRepo.signIn(email.value, password.value)) {
                Log.d("Auth-UI", "auth:completed")
                NavGraph.navigateToJoinedGroups(navController)
            } else {
                errorMessage.value = "Anmeldung fehlgeschlagen"
            }
        }
    }

    /**
     * Resets password if the button gets pressed
     *
     */
    fun forgotPassword() {
        viewModelScope.launch {
            if (userRepo.resetPassword(email = email.value)) {
                Log.d("Auth-UI", "passwordResetMailSend:completed")
                errorMessage.value = "Eine Email zum zurücksetzen wurde gesendet"
            } else {
                errorMessage.value = "Versuche eine andere Email Adresse"
            }
        }
    }
}

class SignInViewModelFactory(
    private val navController: NavController,
    private val userRepo: UserRepositoryInterface
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T =
        SignInViewModel(navController, userRepo) as T
}