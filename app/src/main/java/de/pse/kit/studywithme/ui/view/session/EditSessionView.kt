package de.pse.kit.studywithme.ui.view.session

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import de.pse.kit.myapplication.ui.theme.MyApplicationTheme3
import de.pse.kit.studywithme.model.repository.FakeSessionRepository
import de.pse.kit.studywithme.model.repository.GroupRepository
import de.pse.kit.studywithme.model.repository.SessionRepository
import de.pse.kit.studywithme.model.repository.UserRepository
import de.pse.kit.studywithme.ui.component.NavigationBar
import de.pse.kit.studywithme.ui.component.TopBar
import de.pse.kit.studywithme.ui.layout.Sessionlayout
import de.pse.kit.studywithme.ui.view.navigation.editGroupName
import de.pse.kit.studywithme.ui.view.navigation.joinedGroupDetailsName
import de.pse.kit.studywithme.viewModel.session.EditSessionViewModel
import java.util.*

@ExperimentalMaterial3Api
@Composable
fun EditSessionView(viewModel: EditSessionViewModel) {
    MyApplicationTheme3 {
        Scaffold(
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            TopBar(title = "Session bearbeiten")
            /*
            Sessionlayout(
                place = viewModel.place.collectAsState().value,
                placeChange = {viewModel.place.value = it},
                date = viewModel.date.collectAsState().value.toString(),
                dateChange = {viewModel.date.value = Date(2022,12,13) }, //TODO
                time = viewModel.time.collectAsState().value.toString(),
                timeChange = {viewModel.time.value = 0}, //TODO
                duration = viewModel.duration.collectAsState().value.toString(),
                durationChange = {viewModel.duration.value = 0} //TODO
            )
            */
        }
    }
}

@ExperimentalMaterial3Api
@Preview
@Composable
fun EditSessionViewPreview() {
    EditSessionView(
        EditSessionViewModel(
            rememberNavController(), 0, FakeSessionRepository()
        )
    )
}