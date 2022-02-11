package de.pse.kit.studywithme.ui.view.group

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import de.pse.kit.myapplication.ui.theme.MyApplicationTheme3
import de.pse.kit.studywithme.model.repository.FakeGroupRepository
import de.pse.kit.studywithme.ui.component.NavigationBar
import de.pse.kit.studywithme.ui.component.TopBar
import de.pse.kit.studywithme.ui.layout.GroupFormLayout
import de.pse.kit.studywithme.viewModel.group.NewGroupViewModel

@ExperimentalMaterial3Api
@Composable
fun NewGroupView(viewModel: NewGroupViewModel) {

    MyApplicationTheme3 {
        Scaffold(
            containerColor = MaterialTheme.colorScheme.surface,
            topBar = {
                TopBar(
                    title = "Neue Gruppe",
                    navClick = { viewModel.navBack() },
                    actions = {
                        IconButton(onClick = { viewModel.save() }) {
                            Icon(
                                Icons.Filled.Save,
                                contentDescription = "Knopf um die Gruppe zu erstellen."
                            )
                        }
                    })
            },
            bottomBar = { NavigationBar(
                selectedItem = remember { mutableStateOf(1) },
                clickLeft = {viewModel.navToJoinedGroups()},
                clickMiddle = {viewModel.navBack()},
                clickRight = {viewModel.navToProfile()}
            ) }
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 0.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                GroupFormLayout(
                    groupName = viewModel.groupName.collectAsState().value,
                    groupNameChange = {
                        viewModel.errorMessage.value = ""
                        viewModel.groupName.value = it
                    },
                    lecture = viewModel.groupLecture.collectAsState().value,
                    lectureChange = {
                        viewModel.errorMessage.value = ""
                        viewModel.groupLecture.value = it
                    },
                    description = viewModel.groupDescription.collectAsState().value,
                    descriptionChange = {
                        viewModel.errorMessage.value = ""
                        viewModel.groupDescription.value = it
                    },
                    groupSessionFrequency = viewModel.groupSessionFrequencyName.collectAsState().value,
                    groupSessionFrequencyChange = {
                        viewModel.errorMessage.value = ""
                        viewModel.groupSessionFrequencyName.value = it
                    },
                    sessionFrequencyStrings = viewModel.sessionFrequencyStrings,
                    groupSessionTypeStrings = viewModel.sessionTypeStrings,
                    groupSessionType = viewModel.groupSessionTypeName.collectAsState().value,
                    groupSessionTypeChange = {
                        viewModel.errorMessage.value = ""
                        viewModel.groupSessionTypeName.value = it
                    },
                    chapterNumber = viewModel.groupLectureChapter.collectAsState().value,
                    chapterNumberChange = {
                        viewModel.errorMessage.value = ""
                        viewModel.groupLectureChapter.value = it
                    },
                    exerciseSheetNumber = viewModel.groupExercise.collectAsState().value,
                    exerciseSheetNumberChange = {
                        viewModel.errorMessage.value = ""
                        viewModel.groupExercise.value = it
                    }
                )
            }
        }
    }
}

@ExperimentalMaterial3Api
@Preview
@Composable
fun NewGroupViewPreview() {
    NewGroupView(NewGroupViewModel(rememberNavController(), FakeGroupRepository()))
}