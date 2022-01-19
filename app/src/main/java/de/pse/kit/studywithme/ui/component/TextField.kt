package de.pse.kit.studywithme.ui.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExposedDropdownMenuDefaults.outlinedTextFieldColors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import de.pse.kit.myapplication.ui.theme.MyApplicationTheme
import androidx.compose.ui.Modifier

@Composable
fun FormTextField(modifier: Modifier = Modifier, text: String = "", label: String, onChange: (String) -> Unit = {}, singleLine: Boolean = true) {
    val input = remember { mutableStateOf(text) }

    MyApplicationTheme {
        OutlinedTextField(
            value = input.value,
            onValueChange = {
                input.value = it
                onChange(it)
            },
            modifier = modifier.fillMaxWidth(),
            colors = outlinedTextFieldColors(
                textColor = MaterialTheme.colors.onSurface,
                backgroundColor = MaterialTheme.colors.surface,
                cursorColor = Color.Black,
                disabledTextColor = MaterialTheme.colors.secondaryVariant,
                focusedBorderColor = MaterialTheme.colors.secondaryVariant,
                focusedLabelColor = MaterialTheme.colors.secondaryVariant,
                unfocusedBorderColor = MaterialTheme.colors.secondaryVariant,
                unfocusedLabelColor = MaterialTheme.colors.secondaryVariant),
            label = { androidx.compose.material.Text(label) },
            singleLine = singleLine
        )
    }
}

@Composable
fun TextField(modifier: Modifier = Modifier, text: String = "", label: String, onChange: (String) -> Unit = {}) {
    val input = remember { mutableStateOf(text) }

    MyApplicationTheme {
        OutlinedTextField(
            value = input.value,
            onValueChange = {
                input.value = it
                onChange(it)
            },
            modifier = modifier.fillMaxWidth(),
            label = { androidx.compose.material.Text(label) },
            colors = outlinedTextFieldColors(
                textColor = MaterialTheme.colors.onSurface,
                backgroundColor = MaterialTheme.colors.surface,
                cursorColor = Color.Black,
                disabledTextColor = MaterialTheme.colors.secondaryVariant,
                focusedBorderColor = MaterialTheme.colors.secondaryVariant,
                focusedLabelColor = MaterialTheme.colors.secondaryVariant,
                unfocusedBorderColor = MaterialTheme.colors.secondaryVariant,
                unfocusedLabelColor = MaterialTheme.colors.secondaryVariant),
            shape = RoundedCornerShape(100),
            singleLine = true
        )
    }
}

@Preview
@Composable
fun FormTextFieldPreview() {
    FormTextField(text = "", label = "Gruppenname")
}

@Preview
@Composable
fun TextFieldPreview() {
    TextField(text = "", label = "Nutzername")
}