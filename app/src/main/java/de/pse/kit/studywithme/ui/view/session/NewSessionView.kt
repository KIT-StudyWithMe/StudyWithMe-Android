package de.pse.kit.studywithme.ui.view.session

import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import de.pse.kit.myapplication.ui.theme.MyApplicationTheme3
import de.pse.kit.studywithme.model.repository.FakeGroupRepository
import de.pse.kit.studywithme.model.repository.FakeSessionRepository
import de.pse.kit.studywithme.model.repository.SessionRepository
import de.pse.kit.studywithme.ui.component.NavigationBar
import de.pse.kit.studywithme.ui.component.TopBar
import de.pse.kit.studywithme.ui.layout.Sessionlayout
import de.pse.kit.studywithme.viewModel.session.NewSessionViewModel
import java.util.*

/**
 * View of the newsession screen
 *
 * @param viewModel
 */
@ExperimentalMaterial3Api
@Composable
fun NewSessionView(viewModel: NewSessionViewModel) {
    val group by viewModel.groupState

    MyApplicationTheme3 {
        Scaffold(
            containerColor = MaterialTheme.colorScheme.surface,
            topBar = {
                TopBar(
                    title = "Nächste Lernsession",
                    subtitle = group?.name ?: "",
                    actions = {
                        IconButton(onClick = { viewModel.saveNewSession() }) {
                            Icon(
                                Icons.Rounded.Edit,
                                contentDescription = "Knopf um die Nutzerdaten zu speichern."
                            )
                        }
                    },
                    navClick = { viewModel.navBack() }
                )
            },
            bottomBar = {
                NavigationBar(
                    selectedItem = remember { mutableStateOf(0) },
                    clickLeft = { viewModel.navToJoinedGroups() },
                    clickMiddle = { viewModel.navToSearchGroups() },
                    clickRight = { viewModel.navToProfile() }
                )
            }
        ) {
            Sessionlayout(
                place = "",
                placeChange = { viewModel.place.value = it },
                date = null,
                dateChange = { viewModel.date.value = it },
                time = null,
                timeChange = { viewModel.date.value = it },
                duration = "",
                durationChange = { viewModel.duration.value = it }
            )
        }
    }
}

@ExperimentalMaterial3Api
@Preview
@Composable
fun NewSessionViewPreview() {
    NewSessionView(
        NewSessionViewModel(
            rememberNavController(), FakeSessionRepository(), FakeGroupRepository(), 0
        )
    )
}