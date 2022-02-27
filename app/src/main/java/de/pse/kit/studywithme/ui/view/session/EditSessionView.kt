package de.pse.kit.studywithme.ui.view.session

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import de.pse.kit.myapplication.ui.theme.MyApplicationTheme3
import de.pse.kit.studywithme.model.repository.*
import de.pse.kit.studywithme.ui.component.NavigationBar
import de.pse.kit.studywithme.ui.component.TopBar
import de.pse.kit.studywithme.ui.layout.Sessionlayout
import de.pse.kit.studywithme.ui.view.navigation.editGroupName
import de.pse.kit.studywithme.ui.view.navigation.joinedGroupDetailsName
import de.pse.kit.studywithme.viewModel.session.EditSessionViewModel
import java.util.*

/**
 * View of the editsession screen
 *
 * @param viewModel
 */
@ExperimentalMaterial3Api
@Composable
fun EditSessionView(viewModel: EditSessionViewModel) {
    val group by viewModel.groupState

    MyApplicationTheme3 {
        Scaffold(
                containerColor = MaterialTheme.colorScheme.surface,
            topBar = {
                TopBar(
                    title = "Session bearbeiten",
                    subtitle = group?.name ?: "",
                    actions = {
                        IconButton(onClick = { viewModel.saveSession() }) {
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
                    selectedItem = remember { mutableStateOf(0) },
                    clickLeft = { viewModel.navToJoinedGroups() },
                    clickMiddle = { viewModel.navToSearchGroups() },
                    clickRight = { viewModel.navToProfile() })
            }
        ) {
            Box(modifier = Modifier.padding(it)) {
                Sessionlayout(
                    place = viewModel.place.collectAsState().value,
                    placeChange = { viewModel.place.value = it },
                    date = viewModel.date.collectAsState().value,
                    dateChange = {
                        viewModel.date.value = it
                        Log.d("Test", it.toString())
                    },
                    time = viewModel.date.collectAsState().value,
                    timeChange = {
                        viewModel.date.value = it
                        Log.d("Test", it.toString())
                    },
                    duration = viewModel.duration.collectAsState().value,
                    durationChange = { viewModel.duration.value = it }
                )
            }
        }
    }
}

@ExperimentalMaterial3Api
@Preview
@Composable
fun EditSessionViewPreview() {
    EditSessionView(
        EditSessionViewModel(
            rememberNavController(), 0, FakeSessionRepository(), FakeGroupRepository(), 0
        )
    )
}