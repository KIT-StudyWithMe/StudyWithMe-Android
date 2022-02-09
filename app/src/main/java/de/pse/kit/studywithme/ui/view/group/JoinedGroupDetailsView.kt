package de.pse.kit.studywithme.ui.view.group


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import de.pse.kit.myapplication.ui.theme.MyApplicationTheme3
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import de.pse.kit.studywithme.model.repository.FakeGroupRepository
import de.pse.kit.studywithme.model.repository.FakeSessionRepository
import de.pse.kit.studywithme.ui.component.Button
import de.pse.kit.studywithme.ui.component.NavigationBar
import de.pse.kit.studywithme.ui.component.TopBar
import de.pse.kit.studywithme.ui.layout.GroupDetailsLayout
import de.pse.kit.studywithme.viewModel.group.JoinedGroupDetailsViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@ExperimentalMaterialApi
@ExperimentalMaterial3Api
@Composable
fun JoinedGroupDetailsView(viewModel: JoinedGroupDetailsViewModel) {
    MyApplicationTheme3 {
        val group by viewModel.group
        val sessions by viewModel.sessions
        val groupMembers by viewModel.members
        val groupAdmins by viewModel.admins

        Scaffold(
            topBar = {
                TopBar(
                    title = group?.name ?: "",
                    subtitle = group?.lecture?.lectureName ?: "",
                    navClick = { viewModel.navBack() },
                    actions = {
                        IconButton(onClick = { viewModel.editGroup() }) {
                            Icon(
                                Icons.Rounded.Edit,
                                contentDescription = "Knopf um die Gruppe zu editieren."
                            )
                        }
                    })
            },
            bottomBar = {
                NavigationBar(
                    clickLeft = { viewModel.navBack() },
                    clickMiddle = { viewModel.navToSearchGroups() },
                    clickRight = { viewModel.navToProfile() })
            },
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 0.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                GroupDetailsLayout(
                    groupAdmin = groupAdmins.map {
                        it.name
                    },
                    groupMember = groupMembers.map {
                        it.name
                    },
                    description = group?.description ?: "",
                    place = sessions[0].location,
                    time = sessions[0].date.toString(),
                    selectedChips = listOf(
                        group?.sessionFrequency?.name?.lowercase()
                            ?.replaceFirstChar { it.uppercase() }
                            ?: "",
                        group?.sessionType?.name?.lowercase()?.replaceFirstChar { it.uppercase() }
                            ?: "").filter { it != "" },
                    chapterNumber = group?.lectureChapter,
                    exerciseSheetNumber = group?.exercise
                )
                if (sessions.isEmpty()) {
                    Button(
                        text = "Nächste Lernsesison planen",
                        onClick = { viewModel.planSession() })
                } else {
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Button(
                            modifier = Modifier.weight(1f),
                            text = "Lernsession bearbeiten",
                            onClick = { viewModel.planSession() },
                            primary = false
                        )
                        Button(modifier = Modifier.weight(1f),
                            text = "Teilnehmen",
                            onClick = { viewModel.participate() }
                        )
                    }
                }
            }
        }
    }
}

@ExperimentalCoroutinesApi
@ExperimentalMaterialApi
@ExperimentalMaterial3Api
@Preview
@Composable
fun JoinedGroupDetailsPreview() {
    JoinedGroupDetailsView(
        JoinedGroupDetailsViewModel(
            navController = rememberNavController(),
            groupID = 0,
            groupRepo = FakeGroupRepository(),
            sessionRepo = FakeSessionRepository()
        )
    )
}
