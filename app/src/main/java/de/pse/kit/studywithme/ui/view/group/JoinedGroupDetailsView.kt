package de.pse.kit.studywithme.ui.view.group


import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import de.pse.kit.myapplication.ui.theme.MyApplicationTheme3
import de.pse.kit.studywithme.ui.component.NavigationBar
import de.pse.kit.studywithme.viewModel.group.JoinedGroupDetailsViewModel

@ExperimentalMaterialApi
@ExperimentalMaterial3Api
@Composable
fun JoinedGroupDetailsView(viewModel: JoinedGroupDetailsViewModel) {
    MyApplicationTheme3 {
        Scaffold(
            containerColor = MaterialTheme.colorScheme.surface
        ) {

        }
    }
}

@ExperimentalMaterialApi
@ExperimentalMaterial3Api
@Preview
@Composable
fun JoinedGroupDetailsPreview() {
    Scaffold(bottomBar = { NavigationBar({}, {}, {}) }) {
        JoinedGroupDetailsView(
            JoinedGroupDetailsViewModel(
                navController = rememberNavController(),
                groupID = 0
            )
        )
    }
}
