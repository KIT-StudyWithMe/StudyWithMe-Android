package de.pse.kit.studywithme.ui.layout


import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.pse.kit.myapplication.ui.theme.MyApplicationTheme3
import de.pse.kit.studywithme.ui.component.FormTextField
import de.pse.kit.studywithme.ui.component.NavigationBar
import de.pse.kit.studywithme.ui.component.TopBar

@ExperimentalMaterial3Api
@Composable
fun Sessionlayout(
    place: String = "",
    placeChange: (String) -> Unit = {},
    date: String = "",
    dateChange: (String) -> Unit = {} /*(should be date)*/,
    time: String = "",
    timeChange: (String) -> Unit = {} /*(should be date)*/,
    duration: String = "",
    durationChange: (String) -> Unit = {}

) {
    MyApplicationTheme3 {
        Scaffold(
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 5.dp)) {
                FormTextField(text = place, label = "Lernort", onChange = placeChange)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    FormTextField(
                        modifier = Modifier.weight(1.0f),
                        text = date,
                        label = "Datum",
                        onChange = dateChange
                    )

                    FormTextField(
                        modifier = Modifier.weight(1.0f),
                        text = time,
                        label = "Uhrzeit",
                        onChange = timeChange
                    )
                }

                FormTextField(text = duration, label = "Dauer", onChange = durationChange)
            }
        }
    }
}

@ExperimentalMaterial3Api
@Preview
@Composable
fun SessionLayoutPreview() {
    Sessionlayout()
}