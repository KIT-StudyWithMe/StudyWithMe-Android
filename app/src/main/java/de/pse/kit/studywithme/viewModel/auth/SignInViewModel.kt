package de.pse.kit.studywithme.viewModel.auth


import android.util.Log
import androidx.navigation.NavController
import de.pse.kit.studywithme.model.repository.UserRepository
import de.pse.kit.studywithme.model.repository.UserRepositoryInterface
import de.pse.kit.studywithme.ui.view.navigation.NavGraph
import de.pse.kit.studywithme.viewModel.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class SignInViewModel(navController: NavController, val userRepo: UserRepositoryInterface) :
    ViewModel(navController) {

    val email: MutableStateFlow<String> = MutableStateFlow("")
    val password: MutableStateFlow<String> = MutableStateFlow("")
    val errorMessage: MutableStateFlow<String> = MutableStateFlow("")

    fun navToSignUp() {
        navController.navigate(NavGraph.SignUp.route)
    }

    fun signIn() {
        if (userRepo.signIn(email.value, password.value)) {
            Log.d("Auth-UI", "auth:completed")
            NavGraph.navigateToJoinedGroups(navController)
        } else {
            errorMessage.value = "Anmeldung fehlgeschlagen"
        }
    }

    fun forgotPassword() {
        //TODO: forgot Password
    }
}