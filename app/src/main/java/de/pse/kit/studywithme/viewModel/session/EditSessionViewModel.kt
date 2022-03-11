package de.pse.kit.studywithme.viewModel.session


import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import de.pse.kit.studywithme.model.data.Group
import de.pse.kit.studywithme.model.data.Session
import de.pse.kit.studywithme.model.repository.GroupRepositoryInterface
import de.pse.kit.studywithme.model.repository.SessionRepository
import de.pse.kit.studywithme.model.repository.SessionRepositoryInterface
import de.pse.kit.studywithme.ui.view.navigation.NavGraph
import de.pse.kit.studywithme.viewModel.SignedInViewModel
import de.pse.kit.studywithme.viewModel.group.EditGroupViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*

/**
 * ViewModel of the esitsession screen
 *
 * @property sessionID
 * @property sessionRepo
 * @property groupRepo
 * @property groupID
 * @constructor
 *
 * @param navController
 */
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
        }
        viewModelScope.launch {
            groupRepo.getGroup(groupID).collect {
                groupState.value = it
            }
        }
    }


    /**
     * Saves session with the given parameters
     *
     */
    fun saveSession() {
        if (session != null) {
            val durationInt: Int
            try {
                durationInt = duration.value.toInt()
            } catch (e: NumberFormatException) {
                return
            }
            viewModelScope.launch {
                val edited = sessionRepo.editSession(
                    Session(
                        sessionID = session!!.sessionID,
                        groupID = groupState.value!!.groupID,
                        location = place.value.trim(),
                        date = date.value,
                        duration = durationInt
                    )
                )
                if (edited) {
                    navBack()
                }
            }
        }
    }
}

class EditSessionViewModelFactory(
    private val navController: NavController,
    private val sessionID: Int,
    private val sessionRepo: SessionRepositoryInterface,
    private val groupRepo: GroupRepositoryInterface,
    private val groupID: Int
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T =
        EditSessionViewModel(navController, sessionID, sessionRepo ,groupRepo, groupID) as T
}