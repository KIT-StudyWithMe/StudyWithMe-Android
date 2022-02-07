package de.pse.kit.studywithme.ui.layout


import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.pse.kit.myapplication.ui.theme.MyApplicationTheme3
import de.pse.kit.studywithme.ui.component.*
import java.util.*

@ExperimentalMaterial3Api
@Composable
fun Sessionlayout(
    place: String = "",
    placeChange: (String) -> Unit = {},
    date: Date? = null,
    dateChange: (Date) -> Unit = {},
    time: Date? = null,
    timeChange: (Date) -> Unit = {},
    ending: Date? = null,
    endingChange: (Date) -> Unit = {}

) {
    MyApplicationTheme3 {
        Scaffold(
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 5.dp)) {
                FormTextField(text = place, label = "Lernort", onChange = placeChange)

                DatePicker(
                    context = LocalContext.current,
                    modifier = Modifier.padding(top = 8.dp),
                    preselectedDate = date,
                    onChange = dateChange
                )

                Row(
                    modifier = Modifier.padding(top = 8.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    TimePicker(
                        modifier = Modifier.weight(1.0f),
                        context = LocalContext.current,
                        preselectedText = "Beginn",
                        preselectedTime = time,
                        onChange = timeChange
                    )

                    TimePicker(
                        modifier = Modifier.weight(1.0f),
                        context = LocalContext.current,
                        preselectedText = "Ende",
                        preselectedTime = ending,
                        onChange = endingChange

                    )
                }
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