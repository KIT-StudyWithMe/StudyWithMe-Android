package de.pse.kit.studywithme.viewModel.auth

import android.util.Log
import androidx.navigation.NavController
import de.pse.kit.studywithme.model.data.User
import de.pse.kit.studywithme.model.repository.UserRepository
import de.pse.kit.studywithme.model.repository.UserRepositoryInterface
import de.pse.kit.studywithme.ui.view.navigation.NavGraph
import de.pse.kit.studywithme.viewModel.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class SignUpViewModel(navController: NavController, val userRepo: UserRepositoryInterface) :
    ViewModel(navController) {

    val email: MutableStateFlow<String> = MutableStateFlow("")
    val password: MutableStateFlow<String> = MutableStateFlow("")
    val username: MutableStateFlow<String> = MutableStateFlow("")
    val college: MutableStateFlow<String> = MutableStateFlow("")
    val major: MutableStateFlow<String> = MutableStateFlow("")
    val errorMessage: MutableStateFlow<String> = MutableStateFlow("")

    fun signUp() {
        val user = User(
            userID = -1,
            name = username.value,
            contact = email.value,
            college = college.value,
            major = major.value,
            firebaseUID = "-1"
        )
        Log.d("Auth-UI", "signUp:started")

        if (userRepo.signUp(email.value, password.value, user)) {
            Log.d("Auth-UI", "signUp:completed")
            NavGraph.navigateToSearchGroups(navController)
        } else {
            errorMessage.value = "Registrierung fehlgeschlagen"
        }
    }
}