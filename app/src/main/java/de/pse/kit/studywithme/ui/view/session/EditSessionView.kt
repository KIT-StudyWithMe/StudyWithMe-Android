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
import de.pse.kit.studywithme.model.repository.GroupRepository
import de.pse.kit.studywithme.model.repository.UserRepository
import de.pse.kit.studywithme.ui.component.NavigationBar
import de.pse.kit.studywithme.ui.component.TopBar
import de.pse.kit.studywithme.ui.layout.Sessionlayout
import de.pse.kit.studywithme.ui.view.navigation.editGroupName
import de.pse.kit.studywithme.ui.view.navigation.joinedGroupDetailsName
import de.pse.kit.studywithme.viewModel.session.EditSessionViewModel

@ExperimentalMaterial3Api
@Composable
fun EditSessionView(viewModel: EditSessionViewModel) {
    MyApplicationTheme3 {
        Scaffold(
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            TopBar(title = viewModel.groupName, subtitle = viewModel.groupLecture)
            Sessionlayout(
                place = viewModel.place.collectAsState().value,
                date = viewModel.date.collectAsState().value,
                time = viewModel.time.collectAsState().value,
                duration = viewModel.length.collectAsState().value
            )
        }
    }
}

@ExperimentalMaterial3Api
@Preview
@Composable
fun EditSessionViewPreview() {
    EditSessionView(
        EditSessionViewModel(
            rememberNavController(), 0, GroupRepository.getInstance(
                LocalContext.current
            )
        )
    )
}