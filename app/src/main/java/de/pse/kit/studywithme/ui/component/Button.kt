package de.pse.kit.studywithme.ui.component

import androidx.compose.material3.Button as Button_
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import de.pse.kit.myapplication.ui.theme.MyApplicationTheme

@Composable
fun Button(onClick: (() -> Unit), text: String, primary: Boolean = true) {
    MyApplicationTheme {
        if (primary) {
            Button_(
                onClick = {onClick()},
                colors = buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer, contentColor = MaterialTheme.colorScheme.onPrimaryContainer)
            ) {
                Text(text = text)
            }
        } else {
            OutlinedButton(
                onClick = {onClick()},
                colors = buttonColors(containerColor = MaterialTheme.colorScheme.surface , contentColor = MaterialTheme.colorScheme.onSurface)
            ) {
                Text(text = text)
            }
        }
    }
}

@Composable
@Preview
fun PrimaryButtonPreview() {
    Button(onClick = {}, text = "Anmelden")
}

@Composable
@Preview
fun SecondaryButtonPreview() {
    Button(onClick = {}, text = "Passwort vergessen", primary = false)
}