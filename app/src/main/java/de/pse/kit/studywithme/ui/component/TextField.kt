package de.pse.kit.studywithme.ui.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExposedDropdownMenuDefaults.outlinedTextFieldColors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import de.pse.kit.myapplication.ui.theme.MyApplicationTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation

enum class TextFieldType {
    PASSWORD,
    EMAIL,
    PHONE,
    NUMBER,
    TEXT
}

@Composable
fun FormTextField(
    modifier: Modifier = Modifier,
    text: String = "",
    label: String,
    onChange: (String) -> Unit = {},
    singleLine: Boolean = true,
    type: TextFieldType = TextFieldType.TEXT
) {
    var input by remember { mutableStateOf(text) }
    var passwordVisibility by remember { mutableStateOf(false) }

    MyApplicationTheme {
        OutlinedTextField(
            value = input,
            onValueChange = {
                input = it
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
                unfocusedLabelColor = MaterialTheme.colors.secondaryVariant
            ),
            label = { androidx.compose.material.Text(label) },
            singleLine = singleLine,
            visualTransformation =
            if (type == TextFieldType.PASSWORD && !passwordVisibility)
                PasswordVisualTransformation()
            else
                VisualTransformation.None,
            keyboardOptions = KeyboardOptions(
                keyboardType =
                if (type == TextFieldType.PASSWORD)
                    KeyboardType.Password
                else if (type == TextFieldType.EMAIL)
                    KeyboardType.Email
                else
                    KeyboardType.Text
            ),
            trailingIcon = {
                if (type == TextFieldType.PASSWORD) {
                    val image = if (passwordVisibility)
                        Icons.Filled.Visibility
                    else Icons.Filled.VisibilityOff

                    IconButton(onClick = {
                        passwordVisibility = !passwordVisibility
                    }) {
                        Icon(imageVector = image, "", tint = MaterialTheme.colors.secondaryVariant)
                    }
                }
            }
        )
    }
}

@Composable
fun TextField(
    modifier: Modifier = Modifier,
    text: String = "",
    label: String,
    onChange: (String) -> Unit = {},
    type: TextFieldType = TextFieldType.TEXT
) {
    var input by remember { mutableStateOf(text) }
    var passwordVisibility by remember { mutableStateOf(false) }

    MyApplicationTheme {
        OutlinedTextField(
            value = input,
            onValueChange = {
                input = it
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
                unfocusedLabelColor = MaterialTheme.colors.secondaryVariant
            ),
            shape = RoundedCornerShape(100),
            singleLine = true,
            visualTransformation =
            if (type == TextFieldType.PASSWORD && !passwordVisibility)
                PasswordVisualTransformation()
            else
                VisualTransformation.None,
            keyboardOptions = KeyboardOptions(
                keyboardType =
                if (type == TextFieldType.PASSWORD)
                    KeyboardType.Password
                else if (type == TextFieldType.EMAIL)
                    KeyboardType.Email
                else
                    KeyboardType.Text
            ),
            trailingIcon = {
                if (type == TextFieldType.PASSWORD) {
                    val image = if (passwordVisibility)
                        Icons.Filled.Visibility
                    else Icons.Filled.VisibilityOff

                    IconButton(onClick = {
                        passwordVisibility = !passwordVisibility
                    }) {
                        Icon(imageVector = image, "", tint = MaterialTheme.colors.secondaryVariant)
                    }
                }
            }
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

@Preview
@Composable
fun PasswordTextFieldPreview() {
    TextField(text = "", label = "Nutzername", type = TextFieldType.PASSWORD)
}