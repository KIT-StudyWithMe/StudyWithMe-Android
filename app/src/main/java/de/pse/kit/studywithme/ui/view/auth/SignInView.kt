package de.pse.kit.studywithme.ui.view.auth

import android.app.Application
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import de.pse.kit.studywithme.viewModel.auth.SignInViewModel

/**
 * View of the signin screen
 *
 * @param viewModel
 */
@ExperimentalMaterial3Api
@Composable
fun SignInView(viewModel: SignInViewModel) {
    MyApplicationTheme3 {
        Scaffold(
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
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
                    label = "Email-Adresse",
                    text = viewModel.email.collectAsState().value,
                    onChange = {
                        viewModel.errorMessage.value = ""
                        viewModel.email.value = it
                    },
                    type = TextFieldType.EMAIL
                )
                TextField(
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
                        modifier = Modifier.weight(1.0f),
                        onClick = { viewModel.forgotPassword() },
                        text = "Passwort vergessen", primary = false
                    )
                    Button(
                        modifier = Modifier.weight(1.0f),
                        onClick = { viewModel.signIn() },
                        text = "Anmelden"
                    )
                }
                MyApplicationTheme3 {
                    Text(
                        text = "Registrieren",
                        modifier = Modifier.align(Alignment.End).padding(end = 12.dp).clickable {
                            viewModel.navToSignUp()
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
fun SignInViewPreview() {
    SignInView(SignInViewModel(rememberNavController(), FakeUserRepository(signedIn = false)))
}