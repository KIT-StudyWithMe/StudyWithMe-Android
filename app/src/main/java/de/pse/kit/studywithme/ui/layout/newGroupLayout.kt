package de.pse.kit.studywithme.ui.layout

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.pse.kit.myapplication.ui.theme.*
import de.pse.kit.studywithme.ui.component.*

@ExperimentalMaterial3Api
@Composable
fun GroupLayout(
    groupName: String = "",
    groupNameChange: (String) -> Unit = {},
    lecture: String = "",
    lectureChange: (String) -> Unit = {},
    description: String = "",
    descriptionChange: (String) -> Unit = {},
    chapterNumber: String = "",
    chapterNumberChange: (String) -> Unit = {},
    exerciseSheetNumber: String = "",
    exerciseSheetNumberChange: (String) -> Unit = {},
    ) {
    MyApplicationTheme3 {
        Scaffold(
            containerColor = MaterialTheme.colorScheme.surface,
            topBar = {
                TopBar(
                    title = "Neue Gruppe",
                    actions = {
                        IconButton(onClick = { /*Gruppe speichern*/ }) {
                            Icon(
                                Icons.Filled.Save,
                                contentDescription = "Knopf um die Gruppe zu erstellen."
                            )
                        }
                    })
            },
            bottomBar = { NavigationBar() },

            modifier = Modifier
                .fillMaxSize()
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 5.dp)
            ) {
                Text(text = "Gruppeninformationen")
                FormTextField(
                    label = "Gruppenname",
                    text = groupName,
                    onChange = groupNameChange)
                FormTextField(
                    label = "Vorlesung",
                    text = lecture,
                    onChange = lectureChange)
                FormTextField(
                    label = "Beschreibung",
                    text = description,
                    onChange = descriptionChange)
                Text(text = "Geplante Häufigkeit der Treffen")
                Chips(
                    chipNames = listOf(
                        "Einmalig",
                        "Wöchentlich",
                        "Alle 2 Wochen",
                        "Alle 3 Wochen",
                        "Monatlich",
                    )
                )
                Text(text = "Geplante Art der Treffen")
                Chips(chipNames = listOf("Präsenz", "Online", "Hybrid"))
                Text(text = "Lernfortschritt")
                FormTextField(
                    label = "Vorlesung: Kapitelnummer",
                    text = chapterNumber,
                    onChange = chapterNumberChange)
                FormTextField(
                    label = "Übungsblattnummer",
                    text = exerciseSheetNumber,
                    onChange = exerciseSheetNumberChange)
            }

        }
    }
}

@ExperimentalMaterial3Api
@Composable
@Preview
fun GroupScreenPreview() {
    GroupLayout()
}