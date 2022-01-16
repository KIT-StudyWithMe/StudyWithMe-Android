package de.pse.kit.studywithme.ui.component

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExposedDropdownMenuDefaults.textFieldColors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import de.pse.kit.myapplication.ui.theme.MyApplicationTheme

@Composable
fun FormTextField(text: String = "", label: String, onChange: (String) -> Unit = {}, singleLine: Boolean = true) {
    val input = remember { mutableStateOf(text) }

    MyApplicationTheme {
        OutlinedTextField(
            value = input.value,
            onValueChange = {
                input.value = it
                onChange(it)
            },
            colors = textFieldColors(textColor = MaterialTheme.colors.onSurface, backgroundColor = MaterialTheme.colors.surface, disabledTextColor = MaterialTheme.colors.secondaryVariant),
            label = { androidx.compose.material.Text(label) },
            singleLine = singleLine
        )
    }
}

@Composable
fun TextField(text: String = "", label: String, onChange: (String) -> Unit = {}) {
    val input = remember { mutableStateOf(text) }

    MyApplicationTheme {
        OutlinedTextField(
            value = input.value,
            onValueChange = {
                input.value = it
                onChange(it)
            },
            label = { androidx.compose.material.Text(label) },
            colors = textFieldColors(textColor = MaterialTheme.colors.onSurface, backgroundColor = MaterialTheme.colors.surface, disabledTextColor = MaterialTheme.colors.secondaryVariant),
            shape = RoundedCornerShape(100),
            singleLine = true
        )
    }
}

@Preview
@Composable
fun FormTextFieldPreview() {
    FormTextField("", "Gruppenname")
}

@Preview
@Composable
fun TextFieldPreview() {
    TextField("", "Nutzername")
}