package de.pse.kit.studywithme.ui.view.group

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import de.pse.kit.myapplication.ui.theme.MyApplicationTheme3
import de.pse.kit.studywithme.ui.component.NavigationBar
import de.pse.kit.studywithme.ui.component.SearchBar
import de.pse.kit.studywithme.viewModel.group.SearchGroupsViewModel

@ExperimentalMaterial3Api
@Composable
fun SearchGroupsView(viewModel: SearchGroupsViewModel) {
    MyApplicationTheme3 {
        Scaffold(
            topBar = { SearchBar { viewModel.search(it) } },
            containerColor = MaterialTheme.colorScheme.surface
        ) {

        }
    }
}

@ExperimentalMaterial3Api
@Preview
@Composable
fun SearchGroupsViewPreview() {
    Scaffold(bottomBar = { NavigationBar({}, {}, {}) }) {
        SearchGroupsView(SearchGroupsViewModel(rememberNavController()))
    }
}