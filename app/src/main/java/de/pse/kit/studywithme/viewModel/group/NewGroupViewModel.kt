package de.pse.kit.studywithme.viewModel.group

import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import de.pse.kit.studywithme.model.data.Group
import de.pse.kit.studywithme.model.data.Lecture
import de.pse.kit.studywithme.model.data.SessionFrequency
import de.pse.kit.studywithme.model.data.SessionType
import de.pse.kit.studywithme.model.repository.GroupRepositoryInterface
import de.pse.kit.studywithme.viewModel.SignedInViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import de.pse.kit.studywithme.ui.view.navigation.NavGraph
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

/**
 * ViewModel of newgroup screen
 *
 * @property groupRepo
 * @constructor
 *
 * @param navController
 */
class NewGroupViewModel(navController: NavController, private val groupRepo: GroupRepositoryInterface) : SignedInViewModel(navController) {

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
    private val sessionFrequencies = listOf(SessionFrequency.ONCE, SessionFrequency.WEEKLY,
        SessionFrequency.TWOWEEKLY, SessionFrequency.THREEWEEKLY, SessionFrequency.MONTHLY)
    val sessionTypeStrings = listOf("Präsenz", "Online", "Hybrid")
    val sessionTypes = listOf(SessionType.PRESENCE, SessionType.ONLINE, SessionType.HYBRID)

    /**
     * Saves group with given parameters
     *
     */
    fun save(){
        var sessionFrequencyToSave:SessionFrequency = SessionFrequency.ONCE
        for (i in sessionFrequencies.indices) {
            if (sessionFrequencyStrings[i].equals(groupSessionFrequencyName.value)) {
                sessionFrequencyToSave = sessionFrequencies[i]
            }
        }
        var sessionTypeToSave: SessionType = SessionType.HYBRID
        for (i in sessionTypes.indices) {
            if (sessionTypeStrings[i].equals(groupSessionTypeName.value)) {
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
            groupID = -1,
            name = groupName.value.trim(),
            lectureID = -1,
            lecture = Lecture(-1, groupLecture.value.trim(), -1),
            description = groupDescription.value.trim(),
            sessionFrequency = sessionFrequencyToSave,
            sessionType = sessionTypeToSave,
            lectureChapter = lectureChapterInt,
            exercise = groupExerciseInt,
            memberCount = 1
        )
        viewModelScope.launch {
            val saved = groupRepo.newGroup(group)
            if (saved) {
                navBack()
            }
        }
    }
}

class NewGroupViewModelFactory(
    private val navController: NavController,
    private val groupRepo: GroupRepositoryInterface
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T =
        NewGroupViewModel(navController, groupRepo) as T
}