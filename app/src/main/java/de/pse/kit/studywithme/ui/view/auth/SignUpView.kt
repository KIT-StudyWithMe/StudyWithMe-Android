package de.pse.kit.studywithme.ui.view.auth

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.navigation.compose.rememberNavController
import de.pse.kit.myapplication.ui.theme.MyApplicationTheme3
import de.pse.kit.myapplication.ui.theme.fraunces
import de.pse.kit.studywithme.model.repository.FakeUserRepository
import de.pse.kit.studywithme.ui.component.Button
import de.pse.kit.studywithme.ui.component.TextField
import de.pse.kit.studywithme.ui.component.TextFieldType
import de.pse.kit.studywithme.viewModel.auth.SignUpViewModel

/**
 * View of the signup screen
 *
 * @param viewModel
 */
@ExperimentalMaterial3Api
@Composable
fun SignUpView(viewModel: SignUpViewModel) {
    MyApplicationTheme3 {
        Scaffold(
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            Column(modifier = Modifier
                .padding(24.dp)
                .padding(it)
                .verticalScroll(
                    state = ScrollState(0)
                )) {
                Text(
                    text = "StudyWithMe",
                    modifier = Modifier.padding(bottom = 82.dp, top = 150.dp)
                        .align(Alignment.CenterHorizontally),
                    fontFamily = fraunces,
                    fontSize = 12.em,
                    fontWeight = FontWeight.Bold
                )
                if (viewModel.errorMessage.collectAsState().value != "") {
                    Text(
                        viewModel.errorMessage.collectAsState().value,
                        modifier = Modifier.padding(bottom = 4.dp),
                        color = MaterialTheme.colorScheme.error
                    )
                }
                TextField(
                    modifier = Modifier.testTag("Email-AdresseSignUp"),
                    label = "Email-Adresse",
                    text = viewModel.email.collectAsState().value,
                    onChange = {
                        viewModel.errorMessage.value = ""
                        viewModel.email.value = it
                    },
                    type = TextFieldType.EMAIL
                )
                TextField(
                    modifier = Modifier.testTag("NutzernameSignUp"),
                    label = "Username",
                    text = viewModel.username.collectAsState().value,
                    onChange = {
                        viewModel.errorMessage.value = ""
                        viewModel.username.value = it
                    },
                    type = TextFieldType.TEXT
                )
                TextField(
                    modifier = Modifier.testTag("UniSignUp"),
                    label = "Universität/Hochschule",
                    text = viewModel.college.collectAsState().value,
                    onChange = {
                        viewModel.errorMessage.value = ""
                        viewModel.college.value = it
                    }
                )
                TextField(
                    modifier = Modifier.testTag("LectureSignUp"),
                    label = "Studiengang",
                    text = viewModel.major.collectAsState().value,
                    onChange = {
                        viewModel.errorMessage.value = ""
                        viewModel.major.value = it
                    }
                )
                TextField(
                    modifier = Modifier.testTag("PwSignUp"),
                    label = "Passwort",
                    text = viewModel.password.collectAsState().value,
                    onChange = {
                        viewModel.errorMessage.value = ""
                        viewModel.password.value = it
                    },
                    type = TextFieldType.PASSWORD
                )

                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        modifier = Modifier.weight(1.0f).testTag("SignUp"),
                        onClick = { viewModel.signUp() },
                        text = "Registrieren"
                    )
                }
                MyApplicationTheme3 {
                    Text(
                        text = "Anmelden",
                        modifier = Modifier.align(Alignment.End).padding(end = 12.dp).clickable {
                            viewModel.navBack()
                        },
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.tertiary
                    )
                }
            }
        }
    }
}

@ExperimentalMaterial3Api
@Preview
@Composable
fun SignUpViewPreview() {
    SignUpView(SignUpViewModel(rememberNavController(), FakeUserRepository(signedIn = false)))
}