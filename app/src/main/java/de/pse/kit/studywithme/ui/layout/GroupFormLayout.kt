package de.pse.kit.studywithme.ui.layout

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.pse.kit.myapplication.ui.theme.*
import de.pse.kit.studywithme.ui.component.*

@ExperimentalMaterial3Api
@Composable
fun GroupFormLayout(
    groupName: String = "",
    groupNameChange: (String) -> Unit = {},
    lecture: String = "",
    lectureChange: (String) -> Unit = {},
    description: String = "",
    descriptionChange: (String) -> Unit = {},
    groupSessionFrequency: String = "Einmalig",
    groupSessionFrequencyChange: (String) -> Unit = {},
    groupSessionType: String = "Präsenz",
    groupSessionTypeChange: (String) -> Unit = {},
    chapterNumber: String = "",
    chapterNumberChange: (String) -> Unit = {},
    exerciseSheetNumber: String = "",
    exerciseSheetNumberChange: (String) -> Unit = {},
    sessionFrequencyStrings: List<String>,
    groupSessionTypeStrings: List<String>
) {
    MyApplicationTheme3 {
        Scaffold(
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 24.dp, vertical = 0.dp)
                    .verticalScroll(
                        state = ScrollState(0)
                    )

            ) {
                Text(modifier = Modifier.padding(top = 12.dp), text = "Gruppeninformationen")
                FormTextField(
                    label = "Gruppenname",
                    text = groupName,
                    onChange = groupNameChange
                )
                FormTextField(
                    label = "Vorlesung",
                    text = lecture,
                    onChange = lectureChange
                )
                FormTextField(
                    label = "Beschreibung",
                    text = description,
                    onChange = descriptionChange
                )
                Text(
                    modifier = Modifier.padding(vertical = 12.dp),
                    text = "Geplante Häufigkeit der Treffen"
                )
                ChipSelectionRow(
                    chipNames = sessionFrequencyStrings,
                    selected = groupSessionFrequency,
                    onChange = groupSessionFrequencyChange
                )
                Text(modifier = Modifier.padding(vertical = 12.dp), text = "Geplante Art der Treffen")
                ChipSelectionRow(
                    chipNames = groupSessionTypeStrings,
                    selected = groupSessionType,
                    onChange = groupSessionTypeChange
                )
                Text(modifier = Modifier.padding(top = 12.dp), text = "Lernfortschritt")
                FormTextField(
                    label = "Vorlesung: Kapitelnummer",
                    text = chapterNumber,
                    onChange = chapterNumberChange,
                    type = TextFieldType.NUMBER
                )
                FormTextField(
                    label = "Übungsblatt Nr.",
                    text = exerciseSheetNumber,
                    onChange = exerciseSheetNumberChange,
                    type = TextFieldType.NUMBER
                )
            }

        }
    }
}

@ExperimentalMaterial3Api
@Composable
@Preview
fun NewGroupFormPreview() {
    Scaffold(
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
        bottomBar = { NavigationBar(remember { mutableStateOf(1) }) }
    ) {
        GroupFormLayout(
            sessionFrequencyStrings = listOf(
                "Einmalig",
                "Wöchentlich",
                "Alle 2 Wochen",
                "Alle 3 Wochen",
                "Monatlich",
            ),
            groupSessionTypeStrings = listOf("Präsenz", "Online", "Hybrid")
        )
    }
}

@ExperimentalMaterial3Api
@Composable
@Preview
fun EditGroupFormPreview() {
    Scaffold(
        topBar = {
            TopBar(
                title = "Gruppe 6",
                subtitle = "Lineare Algebra II",
                actions = {
                    IconButton(onClick = { /*Gruppe speichern*/ }) {
                        Icon(
                            Icons.Filled.Save,
                            contentDescription = "Knopf um die Gruppe zu erstellen."
                        )
                    }
                })
        },
        bottomBar = { NavigationBar(selectedItem = remember { mutableStateOf(1) }) }
    ) {
        GroupFormLayout(groupName = "Gruppe 6", lecture = "Lineare Algebra II", description = "Moin Moin!", groupSessionFrequency = "Einmalig", groupSessionType = "Online",
        sessionFrequencyStrings = listOf(
            "Einmalig",
            "Wöchentlich",
            "Alle 2 Wochen",
            "Alle 3 Wochen",
            "Monatlich",
        ),
        groupSessionTypeStrings = listOf("Präsenz", "Online", "Hybrid"))
    }
}