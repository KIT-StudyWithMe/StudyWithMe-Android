package de.pse.kit.studywithme.ui.view.group

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import de.pse.kit.myapplication.ui.theme.MyApplicationTheme3
import de.pse.kit.studywithme.model.repository.FakeGroupRepository
import de.pse.kit.studywithme.ui.component.NavigationBar
import de.pse.kit.studywithme.ui.component.TopBar
import de.pse.kit.studywithme.ui.layout.GroupFormLayout
import de.pse.kit.studywithme.viewModel.group.EditGroupViewModel

@ExperimentalMaterial3Api
@Composable
fun EditGroupView(viewModel: EditGroupViewModel) {
    val group by viewModel.groupState

    MyApplicationTheme3 {
        Scaffold(
            containerColor = MaterialTheme.colorScheme.surface,
            topBar = {
                TopBar(
                    title = group?.name ?: "",
                    actions = {
                        IconButton(onClick = { viewModel.saveEditGroup() }) {
                            Icon(
                                Icons.Filled.Save,
                                contentDescription = "Knopf um die Gruppe zu bearbeiten."
                            )
                        }
                    },
                    navClick = {viewModel.navBack()}
                )
            },
            bottomBar = {
                NavigationBar(
                    selectedItem = remember { mutableStateOf(0) },
                    clickLeft = { viewModel.navToJoinedGroups() },
                    clickMiddle = { viewModel.navToSearchGroups() },
                    clickRight = { viewModel.navToProfile() }
                )
            }
        ) {
            GroupFormLayout(
                groupName = viewModel.groupName.collectAsState().value,
                groupNameChange = {viewModel.groupName.value = it},
                lecture = viewModel.groupLecture.collectAsState().value,
                lectureChange = {viewModel.groupLecture.value = it},
                description = viewModel.groupDescription.collectAsState().value,
                descriptionChange = {viewModel.groupDescription.value = it},
                groupSessionFrequency = viewModel.groupSessionFrequencyName.collectAsState().value,
                groupSessionFrequencyChange = {viewModel.groupSessionFrequencyName.value = it},
                groupSessionType = viewModel.groupSessionTypeName.collectAsState().value,
                groupSessionTypeChange = {viewModel.groupSessionTypeName.value = it},
                chapterNumber = viewModel.groupLectureChapter.collectAsState().value,
                chapterNumberChange = {viewModel.groupLectureChapter.value = it},
                exerciseSheetNumber = viewModel.groupExercise.collectAsState().value,
                exerciseSheetNumberChange = {viewModel.groupExercise.value = it},
                sessionFrequencyStrings = listOf(group!!.sessionFrequency.toString()),
                groupSessionTypeStrings = listOf(group!!.sessionType.toString())
            )
        }
    }
}

@ExperimentalMaterial3Api
@Preview
@Composable
fun EditGroupViewPreview() {
    EditGroupView(EditGroupViewModel(rememberNavController(), groupID = 0, FakeGroupRepository()))
}