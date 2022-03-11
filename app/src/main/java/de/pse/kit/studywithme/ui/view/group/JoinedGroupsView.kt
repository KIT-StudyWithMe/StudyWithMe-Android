package de.pse.kit.studywithme.ui.view.group

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import de.pse.kit.myapplication.ui.theme.MyApplicationTheme3
import de.pse.kit.studywithme.GeneratedExclusion
import de.pse.kit.studywithme.model.repository.FakeGroupRepository
import de.pse.kit.studywithme.ui.component.ChipSelectionRow
import de.pse.kit.studywithme.ui.component.NavigationBar
import de.pse.kit.studywithme.ui.component.SearchGroupResult
import de.pse.kit.studywithme.ui.component.TopBar
import de.pse.kit.studywithme.viewModel.group.JoinedGroupsViewModel

/**
 * View of the joinedgroups screen
 *
 * @param viewModel
 */
@ExperimentalMaterialApi
@ExperimentalMaterial3Api
@Composable
fun JoinedGroupsView(viewModel: JoinedGroupsViewModel) {
    MyApplicationTheme3 {
        Scaffold(
            modifier = Modifier.semantics {
                contentDescription = "JoinedGroupsView"
            },
            topBar = { TopBar(title = "Meine Lerngruppen", isTab = true) },
            bottomBar = {
                NavigationBar(
                    clickMiddle = { viewModel.navToSearchGroups() },
                    clickRight = { viewModel.navToProfile() })
            },
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .padding(top = 6.dp)
                    .padding(it)
            ) {
                if (viewModel.filteredGroups.value.isNotEmpty()) {
                    ChipSelectionRow(
                        chipNames = viewModel.lectures,
                        onChange = { viewModel.filter(it) })
                    Divider(
                        modifier = Modifier.padding(top = 6.dp, bottom = 10.dp),
                        color = MaterialTheme.colorScheme.outline,
                        thickness = 1.dp
                    )
                } else {
                    Text("Du bist noch keiner Lerngruppe beigetreten.")
                }

                LazyColumn(
                    modifier = Modifier.semantics {
                        contentDescription = "MyColumn"
                    },
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(viewModel.filteredGroups.value.size) {
                        val group = viewModel.filteredGroups.value.get(it)
                        SearchGroupResult(
                            groupName = group.name,
                            lecture = group.lecture?.lectureName ?: "",
                            major = group.major?.name ?: "",
                            onClick = { viewModel.navToGroup(group) }
                        )
                    }
                }
            }

        }
    }
}

@GeneratedExclusion
@ExperimentalMaterialApi
@ExperimentalMaterial3Api
@Preview
@Composable
fun JoinedGroupsViewPreview() {
    JoinedGroupsView(JoinedGroupsViewModel(rememberNavController(), FakeGroupRepository()))
}