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
    val date: MutableStateFlow<Date> = MutableStateFlow(Date(2022, 12, 13))
    val time: MutableStateFlow<Int> = MutableStateFlow(0)
    val duration: MutableStateFlow<Int> = MutableStateFlow(0)

    fun navToJoinedGroupDetails(groupID: Int) {
        NavGraph.navigateToJoinedGroup(navController, groupID)
    }

    /**
    init {
        runBlocking {
            sessionRepo.getSessions(group!!.groupID).collect() {
                session = Session(
                    location = place.toString(),
                    date = date.value,
                    duration = duration.value,
                    groupID = group!!.groupID,
                    sessionID = sessionID
                )
                place.value = session!!.location
                date.value = session!!.date
                duration.value = session!!.duration!!
            }
        }
    }
    */


    fun save() {
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