package de.pse.kit.studywithme.ui.view.session

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import de.pse.kit.myapplication.ui.theme.MyApplicationTheme3
import de.pse.kit.studywithme.ui.component.NavigationBar
import de.pse.kit.studywithme.viewModel.session.NewSessionViewModel

@ExperimentalMaterial3Api
@Composable
fun NewSessionView(viewModel: NewSessionViewModel) {
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
fun NewSessionViewPreview() {
    NewSessionView(NewSessionViewModel(rememberNavController(), 0))
}