package de.pse.kit.studywithme.ui.view.group

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import de.pse.kit.myapplication.ui.theme.MyApplicationTheme3
import de.pse.kit.studywithme.model.repository.FakeGroupRepository
import de.pse.kit.studywithme.ui.component.NavigationBar
import de.pse.kit.studywithme.ui.component.TopBar
import de.pse.kit.studywithme.ui.component.Button
import de.pse.kit.studywithme.ui.layout.GroupFormLayout
import de.pse.kit.studywithme.viewModel.group.EditGroupViewModel

/**
 * View of the editgroup view
 *
 * @param viewModel
 */
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
                    navClick = { viewModel.navBack() }
                )
            },
            bottomBar = {
                NavigationBar(
                    selectedItem = remember { mutableStateOf(0) },
                    clickLeft = { viewModel.navBack() },
                    clickMiddle = { viewModel.navToSearchGroups() },
                    clickRight = { viewModel.navToProfile() }
                )
            }
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 24.dp, vertical = 0.dp)
                    .padding(it)
                    .verticalScroll(
                        state = ScrollState(0)
                    ),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                GroupFormLayout(
                    groupName = viewModel.groupName.collectAsState().value,
                    groupNameChange = { viewModel.groupName.value = it },
                    lecture = viewModel.groupLecture.collectAsState().value,
                    lectureChange = { viewModel.groupLecture.value = it },
                    description = viewModel.groupDescription.collectAsState().value,
                    descriptionChange = { viewModel.groupDescription.value = it },
                    groupSessionFrequency = viewModel.groupSessionFrequencyName.collectAsState().value,
                    groupSessionFrequencyChange = {
                        viewModel.groupSessionFrequencyName.value = it
                    },
                    groupSessionType = viewModel.groupSessionTypeName.collectAsState().value,
                    groupSessionTypeChange = { viewModel.groupSessionTypeName.value = it },
                    chapterNumber = viewModel.groupLectureChapter.collectAsState().value,
                    chapterNumberChange = { viewModel.groupLectureChapter.value = it },
                    exerciseSheetNumber = viewModel.groupExercise.collectAsState().value,
                    exerciseSheetNumberChange = { viewModel.groupExercise.value = it },
                    sessionFrequencyStrings = viewModel.sessionFrequencyStrings,
                    groupSessionTypeStrings = viewModel.sessionTypeStrings
                )
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Button(
                        modifier = Modifier.weight(1f),
                        text = "Für andere ausblenden",
                        primary = false,
                        onClick = { viewModel.hideGroup() }
                    )
                    Button(
                        modifier = Modifier.weight(1f),
                        text = "Gruppe löschen",
                        emphasize = true,
                        onClick = { viewModel.deleteGroup() }
                    )
                }
            }
        }
    }
}

@ExperimentalMaterial3Api
@Preview
@Composable
fun EditGroupViewPreview() {
    EditGroupView(EditGroupViewModel(rememberNavController(), groupID = 0, FakeGroupRepository()))
}