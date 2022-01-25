package de.pse.kit.studywithme.ui.view.group

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import de.pse.kit.myapplication.ui.theme.MyApplicationTheme3
import de.pse.kit.studywithme.ui.component.NavigationBar
import de.pse.kit.studywithme.ui.component.TopBar
import de.pse.kit.studywithme.viewModel.group.JoinedGroupsViewModel

@ExperimentalMaterialApi
@ExperimentalMaterial3Api
@Composable
fun JoinedGroupsView(viewModel: JoinedGroupsViewModel) {
    MyApplicationTheme3 {
        Scaffold(
            topBar = { TopBar(title = "Meine Lerngruppen", isTab = true) },
            containerColor = MaterialTheme.colorScheme.surface
        ) {
        }
    }
}

@ExperimentalMaterialApi
@ExperimentalMaterial3Api
@Preview
@Composable
fun JoinedGroupsViewPreview() {
    Scaffold(bottomBar = { NavigationBar({}, {}, {}) }) {
        JoinedGroupsView(JoinedGroupsViewModel(rememberNavController()))
    }
}