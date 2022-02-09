package de.pse.kit.studywithme.ui.view.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AlternateEmail
import androidx.compose.material.icons.outlined.School
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.MenuBook
import androidx.compose.material.icons.rounded.Save
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import de.pse.kit.myapplication.ui.theme.MyApplicationTheme3
import de.pse.kit.studywithme.model.repository.FakeUserRepository
import de.pse.kit.studywithme.ui.component.FormText
import de.pse.kit.studywithme.ui.component.FormTextField
import de.pse.kit.studywithme.ui.component.NavigationBar
import de.pse.kit.studywithme.ui.component.TopBar
import de.pse.kit.studywithme.viewModel.profile.EditProfileViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalMaterial3Api
@ExperimentalCoroutinesApi
@Composable
fun EditProfileView(viewModel: EditProfileViewModel) {
    MyApplicationTheme3 {
        Scaffold(
            containerColor = MaterialTheme.colorScheme.surface,
            topBar = {
                TopBar(
                    title = "Profil bearbeiten",
                    actions = {
                        IconButton(onClick = { viewModel.saveProfile() }) {
                            Icon(
                                Icons.Rounded.Save,
                                contentDescription = "Knopf um die Nutzerdaten zu editieren."
                            )
                        }
                    },
                    navClick = { viewModel.navBack() })
            },
            bottomBar = {
                NavigationBar(
                    selectedItem = remember { mutableStateOf(2) },
                    clickLeft = { viewModel.navToJoinedGroups() },
                    clickMiddle = { viewModel.navToSearchGroups() },
                    clickRight = { viewModel.navBack() })
            }
        ) {
            Column(modifier = Modifier.padding(horizontal = 24.dp, vertical = 0.dp)) {
                Text("Persönliche Informationen", modifier = Modifier.padding(top = 12.dp))
                FormTextField(
                    text = viewModel.college.collectAsState().value,
                    onChange = { viewModel.college.value = it },
                    label = "Universität/Hochschule"
                )
                FormTextField(
                    text = viewModel.major.collectAsState().value,
                    onChange = { viewModel.major.value = it },
                    label = "Studiengang"
                )

                Text("Kontaktinformationen", modifier = Modifier.padding(top = 12.dp))
                FormTextField(
                    text = viewModel.username.collectAsState().value,
                    onChange = { viewModel.username.value = it },
                    label = "Username"
                )
                FormTextField(
                    text = viewModel.contact.collectAsState().value,
                    onChange = { viewModel.contact.value = it },
                    label = "Erreichbar unter"
                )
            }
        }
    }
}

@ExperimentalMaterial3Api
@ExperimentalCoroutinesApi
@Preview
@Composable
fun EditProfileViewPreview() {
    EditProfileView(
        EditProfileViewModel(
            rememberNavController(),
            FakeUserRepository(signedIn = true)
        )
    )
}