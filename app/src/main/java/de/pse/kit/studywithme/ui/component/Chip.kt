package de.pse.kit.studywithme.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.pse.kit.myapplication.ui.theme.MyApplicationTheme3

/**
 * Composable pattern used in the view
 *
 * @param modifier
 * @param chipNames
 * @param selected
 * @param onChange
 * @receiver
 */
@Composable
fun ChipSelectionRow(
    modifier: Modifier = Modifier,
    chipNames: List<String>,
    selected: String? = null,
    onChange: (String) -> Unit
) {
    var selectedChipIndex by remember { mutableStateOf(-1) }
    for ((i, name) in chipNames.withIndex()) {
        if (name == selected) {
            selectedChipIndex = i
            break
        }
    }

    LazyRow(modifier = modifier) {
        items(chipNames.size) {
            Chip(
                modifier = Modifier.padding(start = 0.dp, end = 12.dp),
                onClick = {
                    if (selectedChipIndex == it) selectedChipIndex = -1
                    else selectedChipIndex = it
                    onChange(chipNames[it])
                },
                chosen = selectedChipIndex == it,
                text = chipNames[it]
            )
        }
    }
}

@Composable
fun ChipDisplayRow(modifier: Modifier = Modifier, chipNames: List<String>) {
    LazyRow(modifier = modifier) {
        items(chipNames.size) {
            Chip(
                modifier = Modifier.padding(start = 0.dp, end = 12.dp),
                text = chipNames[it],
                withIcon = false
            )
        }
    }
}

@Composable
fun Chip(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit) = {},
    text: String,
    chosen: Boolean = true,
    withIcon: Boolean = true
) {
    MyApplicationTheme3 {
        if (chosen) {
            TextButton(
                modifier = modifier,
                shape = RoundedCornerShape(25),
                onClick = onClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            ) {
                if (withIcon) {
                    Icon(
                        Icons.Outlined.Check,
                        contentDescription = "",
                    )
                }
                Text(text = text)
            }
        } else {
            OutlinedButton(
                modifier = modifier,
                shape = RoundedCornerShape(25),
                onClick = onClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface
                ),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSecondaryContainer)
            ) {
                Text(text, color = MaterialTheme.colorScheme.onSecondaryContainer)
            }
        }
    }
}

@Composable
@Preview
fun ChipPreview() {
    Chip(text = "Einmalig", chosen = true)
}

@Preview
@Composable
fun ChipSelectionRowPreview() {
    ChipSelectionRow(chipNames = listOf("Präsenz", "Online", "Hybrid"), onChange = {})
}

@Preview
@Composable
fun ChipSelection2RowPreview() {
    ChipSelectionRow(
        chipNames = listOf("Präsenz", "Online", "Hybrid"),
        selected = "Online",
        onChange = {})
}

@Preview
@Composable
fun ChipDisplayRowPreview() {
    ChipDisplayRow(chipNames = listOf("Einmalig", "Präsenz"))
}