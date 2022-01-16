package de.pse.kit.studywithme.ui.component

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExposedDropdownMenuDefaults.textFieldColors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TextField
import androidx.compose.material3.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.tooling.preview.Preview
import de.pse.kit.myapplication.ui.theme.MyApplicationTheme

@Composable
fun Search(text: String = "", label: String, onChange: (String) -> Unit = {}) {
    val input = remember { mutableStateOf(text) }

    MyApplicationTheme {
        TextField(
            value = input.value,
            onValueChange = {
                input.value = it
                onChange(it)
            },
            label = { Text(label) },
            trailingIcon = { Icon(Icons.Outlined.Search, contentDescription = "", tint = MaterialTheme.colors.secondaryVariant) }, // decorative
            colors = textFieldColors(
                textColor = Color.Black,
                disabledTextColor = MaterialTheme.colors.secondaryVariant,
                backgroundColor = MaterialTheme.colors.secondary,
                cursorColor = Color.Black,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                leadingIconColor = MaterialTheme.colors.secondaryVariant,
                disabledLeadingIconColor = MaterialTheme.colors.secondaryVariant,
                trailingIconColor = Color.Black,
                focusedTrailingIconColor = Color.Black,
                disabledTrailingIconColor = MaterialTheme.colors.secondaryVariant,
                focusedLabelColor = MaterialTheme.colors.secondaryVariant,
                unfocusedLabelColor = MaterialTheme.colors.secondaryVariant,
            ),
            shape = RoundedCornerShape(100),
            singleLine = true
        )
    }
}

@Preview
@Composable
fun SearchPreview() {
    Search(label = "Suche Gruppen")
}