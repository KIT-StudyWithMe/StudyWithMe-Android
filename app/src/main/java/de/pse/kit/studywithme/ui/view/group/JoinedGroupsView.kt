package de.pse.kit.studywithme.ui.view.group

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import de.pse.kit.myapplication.ui.theme.MyApplicationTheme3
import de.pse.kit.studywithme.model.repository.FakeGroupRepository
import de.pse.kit.studywithme.ui.component.ChipSelectionRow
import de.pse.kit.studywithme.ui.component.NavigationBar
import de.pse.kit.studywithme.ui.component.SearchGroupResult
import de.pse.kit.studywithme.ui.component.TopBar
import de.pse.kit.studywithme.viewModel.group.JoinedGroupsViewModel

@ExperimentalMaterialApi
@ExperimentalMaterial3Api
@Composable
fun JoinedGroupsView(viewModel: JoinedGroupsViewModel) {
    MyApplicationTheme3 {
        Scaffold(
            topBar = { TopBar(title = "Meine Lerngruppen", isTab = true) },
            bottomBar = {
                NavigationBar(
                    clickMiddle = { viewModel.navToSearchGroups() },
                    clickRight = { viewModel.navToProfile() })
            },
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp)) {
                ChipSelectionRow(chipNames = viewModel.lectures, onChange = { viewModel.filter(it) })
                Divider(modifier = Modifier.padding(top = 6.dp, bottom = 10.dp), color = MaterialTheme.colorScheme.tertiary, thickness = 1.dp)

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
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

@ExperimentalMaterialApi
@ExperimentalMaterial3Api
@Preview
@Composable
fun JoinedGroupsViewPreview() {
    JoinedGroupsView(JoinedGroupsViewModel(rememberNavController(), FakeGroupRepository()))
}