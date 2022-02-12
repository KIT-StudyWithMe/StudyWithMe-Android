package de.pse.kit.studywithme.ui.component

import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.tooling.preview.Preview
import de.pse.kit.myapplication.ui.theme.MyApplicationTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle

/**
 * Composable pattern used in the view
 *
 * @param modifier
 * @param text
 * @param label
 * @param onChange
 * @receiver
 */
@Composable
fun Search(
    modifier: Modifier = Modifier,
    text: String = "",
    label: String = "Suche Gruppen",
    onChange: (String) -> Unit
) {
    var input by remember { mutableStateOf(text) }

    MyApplicationTheme {
        TextField(
            value = input,
            modifier = modifier.fillMaxWidth(),
            onValueChange = {
                input = it
                onChange(it)
            },
            placeholder = { Text(label, fontSize = MaterialTheme.typography.h6.fontSize) },
            textStyle = TextStyle(fontSize = MaterialTheme.typography.h6.fontSize),
            trailingIcon = {
                Icon(
                    Icons.Outlined.Search,
                    contentDescription = "",
                    tint = MaterialTheme.colors.secondaryVariant
                )
            }, // decorative
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
    Search(onChange = {})
}