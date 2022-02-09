package de.pse.kit.studywithme.viewModel.group

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.navigation.NavController
import de.pse.kit.studywithme.model.data.Group
import de.pse.kit.studywithme.model.data.Session
import de.pse.kit.studywithme.model.data.User
import de.pse.kit.studywithme.model.repository.GroupRepositoryInterface
import de.pse.kit.studywithme.model.repository.SessionRepositoryInterface
import de.pse.kit.studywithme.ui.view.navigation.NavGraph
import de.pse.kit.studywithme.viewModel.SignedInViewModel
import de.pse.kit.studywithme.viewModel.ViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class NonJoinedGroupDetailsViewModel(
    navController: NavController,
    val groupID: Int,
    val groupRepo: GroupRepositoryInterface
) : SignedInViewModel(navController) {
    val group: MutableState<Group?> = mutableStateOf(null)
    val admins: MutableState<List<User>> = mutableStateOf(emptyList())


    init {
        runBlocking {
            launch {
                groupRepo.getGroup(groupID).collect {
                    group.value = it
                }
            }
            launch {
                groupRepo.getGroupAdmins(groupID).collect {
                    admins.value = it
                }
            }
        }
    }

    fun editGroup() {
        NavGraph.navigateToEditGroup(navController, groupID)
    }

    fun joinRequest() {
        groupRepo.joinRequest(groupID)
    }
}