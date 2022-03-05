package de.pse.kit.studywithme.viewModel.profile

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import de.pse.kit.studywithme.model.data.User
import de.pse.kit.studywithme.model.repository.UserRepositoryInterface
import de.pse.kit.studywithme.ui.view.navigation.NavGraph
import de.pse.kit.studywithme.viewModel.SignedInViewModel
import de.pse.kit.studywithme.viewModel.ViewModel
import de.pse.kit.studywithme.viewModel.auth.SignUpViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * ViewModel of the editprofile screen
 *
 * @property userRepo
 * @constructor
 *
 * @param navController
 */
@ExperimentalCoroutinesApi
class EditProfileViewModel(navController: NavController, val userRepo: UserRepositoryInterface) :
    SignedInViewModel(navController) {

    var user: User? = null
    val username: MutableStateFlow<String> = MutableStateFlow("")
    val contact: MutableStateFlow<String> = MutableStateFlow("")
    val college: MutableStateFlow<String> = MutableStateFlow("")
    val major: MutableStateFlow<String> = MutableStateFlow("")

    init {
        viewModelScope.launch {
            userRepo.getSignedInUser().collect {
                user = it
                username.value = it.name
                contact.value = it.contact
                college.value = it.college ?: ""
                major.value = it.major ?: ""
            }
        }
    }

    /**
     * Saves profile with given parameters
     *
     */
    fun saveProfile() {
        viewModelScope.launch {
            if (user != null) {
                val remoteCollege = async { userRepo.getCollege(college.value) }
                val remoteMajor = async { userRepo.getMajor(major.value) }

                val edited = userRepo.editSignedInUser(
                    User(
                        userID = user!!.userID,
                        name = username.value,
                        college = remoteCollege.await()?.name ?: user!!.college,
                        collegeID = remoteCollege.await()?.institutionID?.toInt() ?: user!!.collegeID,
                        major = remoteMajor.await()?.name ?: user!!.major,
                        majorID = remoteMajor.await()?.majorID?.toInt() ?: user!!.majorID,
                        contact = contact.value,
                        firebaseUID = user!!.firebaseUID
                    )
                )
                if (edited) navBack()
            }
        }
    }

    /**
     * Deletes the user account
     *
     */
    fun deleteAccount(password: String) {
        viewModelScope.launch {
            if (userRepo.deleteAccount(password)) {
                NavGraph.navigateToSignIn(navController)
            }
        }
    }
}

@ExperimentalCoroutinesApi
class EditProfileViewModelFactory(
    private val navController: NavController,
    private val userRepo: UserRepositoryInterface
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T =
        EditProfileViewModel(navController, userRepo) as T
}

