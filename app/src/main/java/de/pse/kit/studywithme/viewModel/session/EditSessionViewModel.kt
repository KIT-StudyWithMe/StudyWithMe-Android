package de.pse.kit.studywithme.viewModel.session


import android.content.Context
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import de.pse.kit.studywithme.model.data.Group
import de.pse.kit.studywithme.model.data.Session
import de.pse.kit.studywithme.model.repository.GroupRepository
import de.pse.kit.studywithme.ui.view.navigation.NavGraph
import de.pse.kit.studywithme.viewModel.SignedInViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class EditSessionViewModel(navController: NavController, val sessionID: Int, val groupRepo: GroupRepository) :
    SignedInViewModel(navController) {
    val place: MutableStateFlow<String> = MutableStateFlow("")
    val date: MutableStateFlow<String> = MutableStateFlow("")
    val time: MutableStateFlow<String> = MutableStateFlow("")
    val length: MutableStateFlow<String> = MutableStateFlow("")
    lateinit var session: Session //TODO
    lateinit var group : Group //TODO
    val groupName: String = groupRepo.getGroup(session.groupID).toString()
    val groupLecture: String = groupRepo.getLectures("").toString() //TODO

    fun navToJoinedGroupDetails(groupID: Int) {
        NavGraph.navigateToJoinedGroup(navController, groupID)
    }

    fun save() {
        //TODO
    }

    fun cancel() {
        //TODO
    }

    fun getSession() {
        //TODO
    }
}