package de.pse.kit.studywithme.ui.view.profile

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
import de.pse.kit.studywithme.viewModel.profile.EditProfileViewModel

@ExperimentalMaterial3Api
@Composable
fun EditProfileView(viewModel: EditProfileViewModel) {
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
fun EditProfileViewPreview() {
    EditProfileView(EditProfileViewModel(rememberNavController()))
}