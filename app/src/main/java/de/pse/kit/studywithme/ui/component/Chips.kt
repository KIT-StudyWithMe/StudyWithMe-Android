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

@Composable
fun Chips(chipNames: List<String>) {
    var selectedChipIndex by remember { mutableStateOf(0) }
    LazyRow {
        items(chipNames.size) {
            ChipsButton(
                modifier = Modifier.padding(start = 10.dp, top = 5.dp, bottom = 5.dp, end = 10.dp)
                    .height(40.dp),
                onClick = {selectedChipIndex = it
                    //TODO
                    },
                chosen = selectedChipIndex == it,
                text = chipNames[it])
        }
    }
}
@Composable
fun ChipsButton(modifier: Modifier = Modifier, onClick: (() -> Unit), text: String, chosen: Boolean = true) {
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
                Icon(
                    Icons.Outlined.Check,
                contentDescription = "",
                )
                Text(text = text)
            }
        } else {
            OutlinedButton(
                modifier = modifier,
                shape = RoundedCornerShape(25),
                onClick = onClick,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surface , contentColor = MaterialTheme.colorScheme.onSurface),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface)
            ) {
                Text(text)
            }
        }
    }
}
@Composable
@Preview
fun ChipsButtonPreview() {
    ChipsButton(onClick = {}, text = "Einmalig", chosen = true)
}
@Preview
@Composable
fun ChipsPreview() {
    Chips(listOf("Präsenz", "Online", "Hybrid"))
}