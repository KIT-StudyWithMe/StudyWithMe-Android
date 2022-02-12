package de.pse.kit.studywithme.viewModel.auth


import android.util.Log
import androidx.navigation.NavController
import de.pse.kit.studywithme.model.repository.UserRepository
import de.pse.kit.studywithme.model.repository.UserRepositoryInterface
import de.pse.kit.studywithme.ui.view.navigation.NavGraph
import de.pse.kit.studywithme.viewModel.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

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
        if (userRepo.signIn(email.value, password.value)) {
            Log.d("Auth-UI", "auth:completed")
            NavGraph.navigateToJoinedGroups(navController)
        } else {
            errorMessage.value = "Anmeldung fehlgeschlagen"
        }
    }

    /**
     * Resets password if the button gets pressed
     *
     */
    fun forgotPassword() {
        if (userRepo.resetPassword(email = email.value)) {
            Log.d("Auth-UI", "passwordResetMailSend:completed")
            errorMessage.value = "Eine Email zum zurücksetzen wurde gesendet"
        } else {
            errorMessage.value = "Versuche eine andere Email Adresse"
        }
    }
}