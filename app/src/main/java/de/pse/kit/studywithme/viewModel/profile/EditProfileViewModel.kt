package de.pse.kit.studywithme.viewModel.profile

import androidx.navigation.NavController
import de.pse.kit.studywithme.model.data.User
import de.pse.kit.studywithme.model.repository.UserRepositoryInterface
import de.pse.kit.studywithme.ui.view.navigation.NavGraph
import de.pse.kit.studywithme.viewModel.SignedInViewModel
import de.pse.kit.studywithme.viewModel.ViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
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
        runBlocking {
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
        if (user != null) {
            val remoteCollege = userRepo.getCollege(college.value)
            val remoteMajor = userRepo.getMajor(major.value)

            val edited = userRepo.editSignedInUser(
                User(
                    userID = user!!.userID,
                    name = username.value,
                    college = remoteCollege?.name ?: user!!.college,
                    collegeID = remoteCollege?.institutionID?.toInt() ?: user!!.collegeID,
                    major = remoteMajor?.name ?: user!!.major,
                    majorID = remoteMajor?.majorID?.toInt() ?: user!!.majorID,
                    contact = contact.value,
                    firebaseUID = user!!.firebaseUID
                )
            )
            if (edited) navBack()
        }
    }
}

