package de.pse.kit.studywithme.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button as Button_
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.pse.kit.myapplication.ui.theme.MyApplicationTheme3

@Composable
fun Button(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit),
    text: String,
    primary: Boolean = true
) {
    MyApplicationTheme3 {
        if (primary) {
            Button_(
                onClick = onClick,
                modifier = modifier.fillMaxWidth(),
                //contentPadding = PaddingValues(),
                colors = buttonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            ) {
                Text(text)
            }
        } else {
            OutlinedButton(
                onClick = onClick,
                modifier = modifier.fillMaxWidth(),
                colors = buttonColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface
                )
            ) {
                Text(text)
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

@Composable
@Preview
fun ButtonRowPreview() {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        Button(
            modifier = Modifier.weight(1.0f),
            onClick = {},
            text = "Passwort vergessen",
            primary = false
        )
        Button(modifier = Modifier.weight(1.0f), onClick = {}, text = "Anmelden")
    }
}