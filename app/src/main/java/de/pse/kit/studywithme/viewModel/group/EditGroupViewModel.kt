package de.pse.kit.studywithme.viewModel.group

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import de.pse.kit.studywithme.model.data.Group
import de.pse.kit.studywithme.model.data.Lecture
import de.pse.kit.studywithme.model.data.SessionFrequency
import de.pse.kit.studywithme.model.data.SessionType
import de.pse.kit.studywithme.model.repository.GroupRepositoryInterface
import de.pse.kit.studywithme.ui.view.navigation.NavGraph
import de.pse.kit.studywithme.viewModel.SignedInViewModel
import de.pse.kit.studywithme.viewModel.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * ViewModel for editgroup screen
 *
 * @property groupID
 * @property groupRepo
 * @constructor
 *
 * @param navController
 */
class EditGroupViewModel(
    navController: NavController,
    val groupID: Int,
    private val groupRepo: GroupRepositoryInterface
) : SignedInViewModel(navController) {

    var group: Group? = null
    val groupName: MutableStateFlow<String> = MutableStateFlow("")
    val groupDescription: MutableStateFlow<String> = MutableStateFlow("")
    val groupLecture: MutableStateFlow<String> = MutableStateFlow("")

    // TODO() val courseSuggestions: Flow<List<String>>
    val groupSessionFrequencyName: MutableStateFlow<String> = MutableStateFlow("")
    val groupSessionTypeName: MutableStateFlow<String> = MutableStateFlow("")
    val groupLectureChapter: MutableStateFlow<String> = MutableStateFlow("")
    val groupExercise: MutableStateFlow<String> = MutableStateFlow("")
    val errorMessage: MutableStateFlow<String> = MutableStateFlow("")
    val sessionFrequencyStrings = listOf(
        "Einmalig", "Wöchentlich", "Alle 2 Wochen", "Alle 3 Wochen", "Monatlich",
    )
    private val sessionFrequencies = listOf(
        SessionFrequency.ONCE, SessionFrequency.WEEKLY,
        SessionFrequency.TWOWEEKLY, SessionFrequency.THREEWEEKLY, SessionFrequency.MONTHLY
    )
    val sessionTypeStrings = listOf("Präsenz", "Online", "Hybrid")
    val sessionTypes = listOf(SessionType.PRESENCE, SessionType.ONLINE, SessionType.HYBRID)

    init {
        runBlocking {
            groupRepo.getGroup(groupID).collect {
                group = it
                groupName.value = it.name
                groupLecture.value = it.lecture?.lectureName ?: ""
                groupDescription.value = it.description
                groupLectureChapter.value = it.lectureChapter.toString()
                groupExercise.value = it.exercise.toString()
                for(i in sessionFrequencies.indices) {
                    if(sessionFrequencies[i] == it.sessionFrequency) {
                        groupSessionFrequencyName.value = sessionFrequencyStrings[i]
                    }
                }
                for(i in sessionTypes.indices) {
                    if(sessionTypes[i] == it.sessionType) {
                        groupSessionTypeName.value = sessionTypeStrings[i]
                    }
                }
            }
        }
    }



    /**
     * Deletes a group and navigates to last view
     *
     */
    fun deleteGroup() {
        if (group != null) {
            viewModelScope.launch {
                if (groupRepo.deleteGroup(group!!)) {
                    navBack()
                    navBack()
                }
            }
        }
    }

    /**
     * Hides group from other users
     *
     */
    fun hideGroup() {
        if (group != null) {
            viewModelScope.launch {
                groupRepo.hideGroup(groupID, false)
            }
        }
    }

    /**
     * Edits group if the given parameters are correct and navigates to last view
     *
     */
    fun saveEditGroup() {
        if (group == null) {
            return
        }

        var sessionFrequencyToSave: SessionFrequency = SessionFrequency.ONCE
        for (i in sessionFrequencies.indices) {
            if (sessionFrequencyStrings[i] == groupSessionFrequencyName.value) {
                sessionFrequencyToSave = sessionFrequencies[i]
            }
        }
        var sessionTypeToSave: SessionType = SessionType.HYBRID
        for (i in sessionTypes.indices) {
            if (sessionTypeStrings[i] == groupSessionTypeName.value) {
                sessionTypeToSave = sessionTypes[i]
            }
        }
        val lectureChapterInt: Int
        val groupExerciseInt: Int
        try {
            lectureChapterInt = groupLectureChapter.value.toInt()
        } catch (e: NumberFormatException) {
            errorMessage.value = "Kapitelnummer muss eine Zahl sein"
            return
        }
        try {
            groupExerciseInt = groupExercise.value.toInt()
        } catch (e: NumberFormatException) {
            errorMessage.value = "Übungsblattnummer muss eine Zahl sein"
            return
        }

        val group = Group(
            groupID = group!!.groupID,
            name = groupName.value,
            lectureID = -1,
            lecture = Lecture(-1, groupLecture.value, -1),
            description = groupDescription.value,
            sessionFrequency = sessionFrequencyToSave,
            sessionType = sessionTypeToSave,
            lectureChapter = lectureChapterInt,
            exercise = groupExerciseInt,
        )
        viewModelScope.launch {
            val groupSaved = groupRepo.editGroup(group)
            if (groupSaved) {
                navBack()
            }
        }
    }
}

class EditGroupViewModelFactory(
    private val navController: NavController,
    private val groupID: Int,
    private val groupRepo: GroupRepositoryInterface
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T =
        EditGroupViewModel(navController, groupID, groupRepo) as T
}