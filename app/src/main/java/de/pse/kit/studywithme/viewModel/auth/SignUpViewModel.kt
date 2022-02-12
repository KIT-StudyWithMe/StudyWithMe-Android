package de.pse.kit.studywithme.viewModel.auth

import android.util.Log
import androidx.navigation.NavController
import de.pse.kit.studywithme.model.data.User
import de.pse.kit.studywithme.model.repository.UserRepository
import de.pse.kit.studywithme.model.repository.UserRepositoryInterface
import de.pse.kit.studywithme.ui.view.navigation.NavGraph
import de.pse.kit.studywithme.viewModel.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * ViewModel for signup screen
 *
 * @property userRepo
 * @constructor
 *
 * @param navController
 */
class SignUpViewModel(navController: NavController, val userRepo: UserRepositoryInterface) :
    ViewModel(navController) {

    val email: MutableStateFlow<String> = MutableStateFlow("")
    val password: MutableStateFlow<String> = MutableStateFlow("")
    val username: MutableStateFlow<String> = MutableStateFlow("")
    val college: MutableStateFlow<String> = MutableStateFlow("")
    val major: MutableStateFlow<String> = MutableStateFlow("")
    val errorMessage: MutableStateFlow<String> = MutableStateFlow("")

    /**
     * User gets registered
     *
     */
    fun signUp() {
        Log.d("Auth-UI", "signUp:started")

        if (userRepo.signUp(
                email = email.value,
                password = password.value,
                username = username.value,
                major = major.value,
                institution = college.value
            )
        ) {
            Log.d("Auth-UI", "signUp:completed")
            NavGraph.navigateToSearchGroups(navController)
        } else {
            errorMessage.value = "Registrierung fehlgeschlagen"
        }
    }
}