package de.pse.kit.studywithme.viewModel.session


import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.navigation.NavController
import de.pse.kit.studywithme.model.data.Group
import de.pse.kit.studywithme.model.data.Session
import de.pse.kit.studywithme.model.repository.GroupRepositoryInterface
import de.pse.kit.studywithme.model.repository.SessionRepository
import de.pse.kit.studywithme.model.repository.SessionRepositoryInterface
import de.pse.kit.studywithme.ui.view.navigation.NavGraph
import de.pse.kit.studywithme.viewModel.SignedInViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*

class EditSessionViewModel(
    navController: NavController,
    val sessionID: Int,
    val sessionRepo: SessionRepositoryInterface,
    val groupRepo: GroupRepositoryInterface,
    val groupID: Int
) :
    SignedInViewModel(navController) {
    val groupState: MutableState<Group?> = mutableStateOf(null)
    var session: Session? = null
    val place: MutableStateFlow<String> = MutableStateFlow("")
    val date: MutableStateFlow<Date> = MutableStateFlow(Date())
    val duration: MutableStateFlow<String> = MutableStateFlow("")

    init {
        runBlocking {
            sessionRepo.getSession(sessionID).collect {
                session = it
                place.value = it.location
                date.value = it.date
                duration.value = it.duration.toString()
            }
            launch {
                groupRepo.getGroup(groupID).collect {
                    groupState.value = it
                }
            }
        }
    }



    fun saveSession() {
        if (session != null) {
            val edited = sessionRepo.editSession(
                Session(
                    sessionID = session!!.sessionID,
                    groupID = groupState.value!!.groupID,
                    location = place.value,
                    date = date.value,
                    duration = duration.value.toInt()
                )
            )
            if (edited) {
                navBack()
            }
        }
    }
}