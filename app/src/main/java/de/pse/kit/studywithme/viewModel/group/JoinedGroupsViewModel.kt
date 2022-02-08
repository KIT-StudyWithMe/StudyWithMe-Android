package de.pse.kit.studywithme.viewModel.group

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.navigation.NavController
import de.pse.kit.studywithme.model.data.Group
import de.pse.kit.studywithme.model.repository.GroupRepositoryInterface
import de.pse.kit.studywithme.ui.view.navigation.NavGraph
import de.pse.kit.studywithme.viewModel.SignedInViewModel
import de.pse.kit.studywithme.viewModel.ViewModel
import kotlinx.coroutines.runBlocking

class JoinedGroupsViewModel(navController: NavController, val groupRepo: GroupRepositoryInterface
) : SignedInViewModel(navController) {

    private var groups: List<Group> = emptyList()
    private var filter: String = ""
    var filteredGroups: MutableState<List<Group>> = mutableStateOf(emptyList())
    var lectures: List<String> = emptyList()

    init {
        runBlocking {
            groupRepo.getJoinedGroups().collect {
                groups = it
                filteredGroups.value = it
                lectures  = groups.map {
                    it.lecture?.lectureName
                }.distinct().filterNotNull()
            }
        }
    }

    fun navToGroup(group: Group) {
        NavGraph.navigateToJoinedGroup(navController, group.groupID)
    }

    fun filter(lecture: String) {
        if (lecture == filter) {
            filteredGroups.value = groups
            filter = ""
        }
        else {
            filter = lecture

            filteredGroups.value = groups.filter {
                it.lecture?.lectureName == lecture
            }
        }
    }
}

