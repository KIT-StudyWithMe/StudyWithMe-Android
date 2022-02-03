package de.pse.kit.studywithme.ui.view.group

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import de.pse.kit.myapplication.ui.theme.MyApplicationTheme3
import de.pse.kit.studywithme.ui.component.NavigationBar
import de.pse.kit.studywithme.viewModel.group.NonJoinedGroupDetailsViewModel

@ExperimentalMaterial3Api
@Composable
fun NonJoinedGroupDetailsView(viewModel: NonJoinedGroupDetailsViewModel) {
    MyApplicationTheme3 {
        Scaffold(
            containerColor = MaterialTheme.colorScheme.surface
        ) {

        }
    }
}

@ExperimentalMaterial3Api
@Preview
@Composable
fun NonJoinedGroupDetailsViewPreview() {
    NonJoinedGroupDetailsView(NonJoinedGroupDetailsViewModel(rememberNavController(), 0))
}