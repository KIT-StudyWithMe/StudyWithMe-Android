package de.pse.kit.studywithme.viewModel.session


import androidx.navigation.NavController
import de.pse.kit.studywithme.model.data.Group
import de.pse.kit.studywithme.model.data.Session
import de.pse.kit.studywithme.model.repository.SessionRepository
import de.pse.kit.studywithme.model.repository.SessionRepositoryInterface
import de.pse.kit.studywithme.ui.view.navigation.NavGraph
import de.pse.kit.studywithme.viewModel.SignedInViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import java.util.*

class EditSessionViewModel(
    navController: NavController,
    val sessionID: Int,
    val sessionRepo: SessionRepositoryInterface
) :
    SignedInViewModel(navController) {
    var session: Session? = null
    var group: Group? = null //TODO
    val place: MutableStateFlow<String> = MutableStateFlow("")
    val date: MutableStateFlow<Date> = MutableStateFlow(Date())
    val duration: MutableStateFlow<String> = MutableStateFlow("")

    fun navToJoinedGroupDetails(groupID: Int) {
        NavGraph.navigateToJoinedGroup(navController, groupID)
    }


    init {
        runBlocking {
            sessionRepo.getSession(sessionID).collect {
                session = it
                place.value = it.location
                date.value = it.date
                duration.value = it.duration.toString()
            }
        }
    }



    fun saveSession() {
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