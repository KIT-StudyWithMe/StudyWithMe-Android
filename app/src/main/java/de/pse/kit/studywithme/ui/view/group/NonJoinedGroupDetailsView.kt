package de.pse.kit.studywithme.ui.view.group

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import de.pse.kit.myapplication.ui.theme.MyApplicationTheme3
import de.pse.kit.studywithme.model.repository.FakeGroupRepository
import de.pse.kit.studywithme.model.repository.FakeSessionRepository
import de.pse.kit.studywithme.ui.component.NavigationBar
import de.pse.kit.studywithme.ui.component.TopBar
import de.pse.kit.studywithme.ui.layout.GroupDetailsLayout
import de.pse.kit.studywithme.viewModel.group.NonJoinedGroupDetailsViewModel
import java.util.Collections.list

@OptIn(ExperimentalMaterialApi::class)
@ExperimentalMaterial3Api
@Composable
fun NonJoinedGroupDetailsView(viewModel: NonJoinedGroupDetailsViewModel) {
    MyApplicationTheme3 {
        val group by viewModel.group
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
            GroupDetailsLayout(
                groupAdmin = groupAdmins.map {
                    it.name
                },
                groupMember = listOf(),
                description = group?.description ?: "",
                selectedChips = listOf(
                    group?.sessionFrequency?.name?.lowercase()?.replaceFirstChar { it.uppercase() }
                        ?: "",
                    group?.sessionType?.name?.lowercase()?.replaceFirstChar { it.uppercase() }
                        ?: "").filter { it != "" },
                chapterNumber = group?.lectureChapter,
                exerciseSheetNumber = group?.exercise
            )
        }
    }
}

@ExperimentalMaterial3Api
@Preview
@Composable
fun NonJoinedGroupDetailsViewPreview() {
    NonJoinedGroupDetailsView(
        NonJoinedGroupDetailsViewModel(
            navController = rememberNavController(),
            groupID = 0,
            groupRepo = FakeGroupRepository()
        )
    )
}