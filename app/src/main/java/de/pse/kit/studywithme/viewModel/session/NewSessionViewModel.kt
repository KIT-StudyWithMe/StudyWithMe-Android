package de.pse.kit.studywithme.viewModel.session

import androidx.navigation.NavController
import de.pse.kit.studywithme.model.data.Group
import de.pse.kit.studywithme.model.data.Session
import de.pse.kit.studywithme.model.repository.SessionRepositoryInterface
import de.pse.kit.studywithme.ui.view.navigation.NavGraph
import de.pse.kit.studywithme.viewModel.SignedInViewModel
import de.pse.kit.studywithme.viewModel.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.runBlocking
import java.util.*

class NewSessionViewModel(
    navController: NavController,
    val sessionID: Int,
    val sessionRepo: SessionRepositoryInterface
) : SignedInViewModel(navController) {
    var session: Session? = null
    var group: Group? = null
    val place: MutableStateFlow<String> = MutableStateFlow("")
    val date: MutableStateFlow<Date> = MutableStateFlow(Date())
    val duration: MutableStateFlow<String> = MutableStateFlow("")

    fun navToJoinedGroupDetails(groupID: Int) {
        NavGraph.navigateToJoinedGroup(navController, groupID)
    }

    fun saveNewSession() {
        if (session != null) {
            sessionRepo.editSession(
                Session(
                    sessionID = session!!.sessionID,
                    groupID = group!!.groupID,
                    location = place.value,
                    date = session!!.date,
                    duration = session!!.duration
                )
            )
        }
    }
}
