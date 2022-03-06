package de.pse.kit.studywithme.ui.view.group

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import de.pse.kit.myapplication.ui.theme.MyApplicationTheme3
import de.pse.kit.studywithme.model.repository.FakeGroupRepository
import de.pse.kit.studywithme.ui.component.Button
import de.pse.kit.studywithme.ui.component.NavigationBar
import de.pse.kit.studywithme.ui.component.SearchBar
import de.pse.kit.studywithme.ui.component.SearchGroupResult
import de.pse.kit.studywithme.viewModel.group.SearchGroupsViewModel

/**
 * View of the searchgroups screen
 *
 * @param viewModel
 */
@ExperimentalMaterial3Api
@ExperimentalMaterialApi
@Composable
fun SearchGroupsView(viewModel: SearchGroupsViewModel) {
    MyApplicationTheme3 {
        Scaffold(
            modifier = Modifier.semantics {
                contentDescription = "SearchGroupsView"
            },
            topBar = { SearchBar { viewModel.search(it) } },
            bottomBar = {
                NavigationBar(selectedItem = remember { mutableStateOf(1) },
                    clickLeft = { viewModel.navToJoinedGroups() },
                    clickRight = { viewModel.navToProfile() }
                )
            },
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            Column(modifier = Modifier
                .padding(horizontal = 20.dp)
                .padding(top = 6.dp)
                .padding(it)) {
                Button(onClick = { viewModel.newGroup() }, text = "Neue Gruppe")

                LazyColumn(
                    modifier = Modifier.padding(top = 6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(viewModel.groups.value.size) {
                        val group = viewModel.groups.value.get(it)
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

@ExperimentalMaterial3Api
@ExperimentalMaterialApi
@Preview
@Composable
fun SearchGroupsViewPreview() {
    SearchGroupsView(SearchGroupsViewModel(rememberNavController(), FakeGroupRepository()))
}