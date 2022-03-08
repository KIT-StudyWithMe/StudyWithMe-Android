package de.pse.kit.studywithme.ui.view.profile

import android.util.Log
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AlternateEmail
import androidx.compose.material.icons.outlined.School
import androidx.compose.material.icons.rounded.Book
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.MenuBook
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import de.pse.kit.myapplication.ui.theme.MyApplicationTheme3
import de.pse.kit.studywithme.model.repository.FakeUserRepository
import de.pse.kit.studywithme.model.repository.UserRepository
import de.pse.kit.studywithme.ui.component.FormText
import de.pse.kit.studywithme.ui.component.NavigationBar
import de.pse.kit.studywithme.ui.component.TopBar
import de.pse.kit.studywithme.ui.component.Button
import de.pse.kit.studywithme.viewModel.profile.ProfileViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * View of the profile screen
 *
 * @param viewModel
 */
@ExperimentalCoroutinesApi
@ExperimentalMaterial3Api
@ExperimentalMaterialApi
@Composable
fun ProfileView(viewModel: ProfileViewModel) {
    MyApplicationTheme3 {
        Scaffold(
            modifier = Modifier.semantics {
                contentDescription = "ProfileView"
            },
            containerColor = MaterialTheme.colorScheme.surface,
            topBar = {
                TopBar(
                    title = viewModel.username,
                    isTab = true,
                    actions = {
                        IconButton(
                            modifier = Modifier.semantics {
                                contentDescription = "EditGroupButton"
                            },onClick = { viewModel.navToEditProfile() }) {
                            Icon(
                                Icons.Rounded.Edit,
                                contentDescription = "Knopf um die Nutzerdaten zu editieren."
                            )
                        }
                    })
            },
            bottomBar = {
                NavigationBar(
                    selectedItem = remember { mutableStateOf(2) },
                    clickLeft = { viewModel.navToJoinedGroups() },
                    clickMiddle = { viewModel.navToSearchGroups() })
            }
        ) {
            Column(modifier = Modifier
                .padding(horizontal = 24.dp, vertical = 0.dp)
                .padding(it)
                .verticalScroll(
                    state = ScrollState(0)
                ),) {
                Text("Persönliche Informationen", modifier = Modifier.padding(top = 12.dp))
                FormText(text = viewModel.college, icon = Icons.Outlined.School)
                FormText(text = viewModel.major, icon = Icons.Rounded.MenuBook)

                Text("Kontaktinformationen", modifier = Modifier.padding(top = 12.dp))
                FormText(text = viewModel.contact, icon = Icons.Outlined.AlternateEmail)

                Button(text = "Abmelden", onClick = { viewModel.signOut() }, emphasize = true)
            }
        }
    }
}

@ExperimentalCoroutinesApi
@ExperimentalMaterial3Api
@ExperimentalMaterialApi
@Composable
@Preview
fun ProfileViewPreview() {
    ProfileView(ProfileViewModel(rememberNavController(), FakeUserRepository(signedIn = true)))
}