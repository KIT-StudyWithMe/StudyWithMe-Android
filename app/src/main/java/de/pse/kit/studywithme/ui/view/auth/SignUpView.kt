package de.pse.kit.studywithme.ui.view.auth

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.navigation.compose.rememberNavController
import de.pse.kit.myapplication.ui.theme.MyApplicationTheme3
import de.pse.kit.myapplication.ui.theme.fraunces
import de.pse.kit.studywithme.model.repository.UserRepository
import de.pse.kit.studywithme.ui.component.Button
import de.pse.kit.studywithme.ui.component.TextField
import de.pse.kit.studywithme.viewModel.auth.SignUpViewModel

@ExperimentalMaterial3Api
@Composable
fun SignUpView(viewModel: SignUpViewModel) {
    MyApplicationTheme3 {
        Scaffold(
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text(
                    text = "StudyWithMe",
                    modifier = Modifier.padding(bottom = 112.dp, top = 150.dp)
                        .align(Alignment.CenterHorizontally),
                    fontFamily = fraunces,
                    fontSize = 12.em,
                    fontWeight = FontWeight.Bold
                )
                if (viewModel.errorMessage.collectAsState().value != "") {
                    Text(viewModel.errorMessage.collectAsState().value, modifier = Modifier.padding(bottom = 4.dp), color = Color.Red)
                }
                TextField(
                    label = "Email-Adresse",
                    text = viewModel.email.collectAsState().value,
                    onChange = {
                        viewModel.errorMessage.value = ""
                        viewModel.email.value = it
                    }
                )
                TextField(
                    label = "Universität/ Hochschule",
                    text = viewModel.college.collectAsState().value,
                    onChange = {
                        viewModel.errorMessage.value = ""
                        viewModel.college.value = it
                    }
                )
                TextField(
                    label = "Studiengang",
                    text = viewModel.major.collectAsState().value,
                    onChange = {
                        viewModel.errorMessage.value = ""
                        viewModel.major.value = it
                    }
                )
                TextField(
                    label = "Passwort",
                    text = viewModel.password.collectAsState().value,
                    onChange = {
                        viewModel.errorMessage.value = ""
                        viewModel.password.value = it
                    }
                )

                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        modifier = Modifier.weight(1.0f),
                        onClick = { viewModel.signUp() },
                        text = "Registrieren"
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
    SignUpView(SignUpViewModel(rememberNavController(), UserRepository.getInstance(LocalContext.current)))
}