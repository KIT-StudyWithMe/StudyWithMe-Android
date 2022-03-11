package de.pse.kit.studywithme.ui.view.profile

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import de.pse.kit.myapplication.ui.theme.MyApplicationTheme3
import de.pse.kit.studywithme.GeneratedExclusion
import de.pse.kit.studywithme.model.data.UserField
import de.pse.kit.studywithme.model.repository.FakeUserRepository
import de.pse.kit.studywithme.ui.component.*
import de.pse.kit.studywithme.viewModel.profile.EditProfileViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * View of the editprofile screen
 *
 * @param viewModel
 */
@ExperimentalMaterial3Api
@ExperimentalCoroutinesApi
@Composable
fun EditProfileView(viewModel: EditProfileViewModel) {
    MyApplicationTheme3 {
        Scaffold(
            modifier = Modifier.semantics {
                contentDescription = "EditProfileView"
            },
            containerColor = MaterialTheme.colorScheme.surface,
            topBar = {
                TopBar(
                    title = "Profil bearbeiten",
                    actions = {
                        IconButton(
                            onClick = { viewModel.saveProfile() },
                            modifier = Modifier.testTag("Speichern")
                        ) {
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
            var password by remember { mutableStateOf("") }
            var openDialog by remember { mutableStateOf(false) }

            if (openDialog) {
                AlertDialog(
                    onDismissRequest = {
                        openDialog = false
                    },
                    title = {
                        Text(text = "Account löschen")
                    },
                    text = {
                        Column {
                            Text(
                                "Gebe dein Passwort ein:",
                                modifier = Modifier.padding(top = 12.dp),
                                fontWeight = FontWeight.Bold
                            )
                            TextField(
                                label = "Passwort",
                                onChange = { password = it },
                                type = TextFieldType.PASSWORD
                            )
                        }
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                viewModel.deleteAccount(password)
                                password = ""
                                openDialog = false
                            }
                        ) {
                            Text("Bestätigen", color = MaterialTheme.colorScheme.onPrimaryContainer)
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = {
                                password = ""
                                openDialog = false
                            }
                        ) {
                            Text("Abbrechen", color = MaterialTheme.colorScheme.onPrimaryContainer)
                        }
                    },
                    containerColor = MaterialTheme.colorScheme.primary,
                    textContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            Column(
                modifier = Modifier
                    .padding(horizontal = 24.dp, vertical = 0.dp)
                    .padding(it)
                    .verticalScroll(
                        state = ScrollState(0)
                    ),
            ) {
                Text("Persönliche Informationen", modifier = Modifier.padding(top = 12.dp))
                FormTextField(
                    modifier = Modifier.testTag("Uni"),
                    text = viewModel.college.collectAsState().value,
                    onChange = { viewModel.college.value = it },
                    label = "Universität/Hochschule"
                )
                FormTextField(
                    modifier = Modifier.testTag("Studiengang"),
                    text = viewModel.major.collectAsState().value,
                    onChange = { viewModel.major.value = it },
                    label = "Studiengang"
                )

                Text("Kontaktinformationen", modifier = Modifier.padding(top = 12.dp))
                FormTextField(
                    modifier = Modifier.testTag("Nutzername"),
                    text = viewModel.username.collectAsState().value,
                    onChange = { viewModel.username.value = it },
                    label = "Username"
                )
                FormTextField(
                    modifier = Modifier.testTag("Kontaktinfo"),
                    text = viewModel.contact.collectAsState().value,
                    onChange = { viewModel.contact.value = it },
                    label = "Erreichbar unter"
                )
                Button(
                    modifier = Modifier.padding(top = 12.dp),
                    text = "Account löschen",
                    onClick = { openDialog = true },
                    emphasize = true
                )
            }
        }
    }
}

@GeneratedExclusion
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