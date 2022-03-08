package de.pse.kit.studywithme.viewModel.auth

import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import de.pse.kit.studywithme.model.data.User
import de.pse.kit.studywithme.model.repository.UserRepository
import de.pse.kit.studywithme.model.repository.UserRepositoryInterface
import de.pse.kit.studywithme.ui.view.navigation.NavGraph
import de.pse.kit.studywithme.viewModel.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

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
    * Refresh view
    *
    */
    fun refreshView() {
        email.value = ""
        password.value = ""
        username.value = ""
        college.value = ""
        major.value = ""
        errorMessage.value = ""
    }

    /**
     * User gets registered
     *
     */
    fun signUp() {
        Log.d("Auth-UI", "signUp:started")

        viewModelScope.launch {
            if (userRepo.signUp(
                    email = email.value.trim(),
                    password = password.value.trim(),
                    username = username.value.trim(),
                    major = major.value.trim(),
                    institution = college.value.trim()
                )
            ) {
                Log.d("Auth-UI", "signUp:completed")
                refreshView()
                NavGraph.navigateToSearchGroups(navController)
            } else {
                errorMessage.value = "Registrierung fehlgeschlagen"
            }
        }
    }
}

class SignUpViewModelFactory(
    private val navController: NavController,
    private val userRepo: UserRepositoryInterface
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T =
        SignUpViewModel(navController, userRepo) as T
}