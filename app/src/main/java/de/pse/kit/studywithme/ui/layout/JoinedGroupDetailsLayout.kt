package de.pse.kit.studywithme.ui.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.text.toSpanned
import de.pse.kit.myapplication.ui.theme.MyApplicationTheme3
import de.pse.kit.studywithme.ui.component.*

@ExperimentalMaterialApi
@ExperimentalMaterial3Api
@Composable
fun JoinedGroupDetailsLayout(
    groupName: String = "",
    lecture: String = "",
    groupAdmin: String = "",
    groupMember: List<String>,
    description: String = "",
    place: String = "",
    time: String = "",
    selectedChips: List<String>,
    chapterNumber: Int,
    checkBoxChapter: Boolean,
    exerciseSheetNumber: Int,
    checkBoxExercise: Boolean
) {
    MyApplicationTheme3 {
        Scaffold(
            containerColor = MaterialTheme.colorScheme.surface,
            topBar = {
                TopBar(
                    title = groupName,
                    subtitle = lecture,
                    actions = {
                        IconButton(onClick = { /*Gruppe bearbeiten*/ }) {
                            Icon(
                                Icons.Filled.Edit,
                                contentDescription = "Knopf um die Gruppe zu bearbeiten."
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
                FormText(icon = Icons.Filled.Person, text = groupAdmin)
                for (element in groupMember) {
                    FormText(icon = Icons.Outlined.Person, text = element)
                }
                FormText(icon = Icons.Filled.Info, text = description, maxLines = 3)
                Text(text = "Nächste geplante Lernsession")
                FormText(icon = Icons.Filled.LocationOn, text = place, maxLines = 2)
                FormText(icon = Icons.Filled.Timer, text = time, maxLines = 2)
                Text(text = "Weitere Informationen")
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    for (element in selectedChips) {
                        ChipsButton(onClick = { }, text = element)
                    }
                }
                Text(text = "Lernfortschritt")
                var iconChapter: ImageVector = Icons.Outlined.IndeterminateCheckBox
                var iconExercise: ImageVector = Icons.Outlined.IndeterminateCheckBox
                var textChapter: String = "Vorlesung: Kapitel Nr."
                var textExercise: String = "Übungsblatt Nr."
                if (checkBoxChapter) {
                    iconChapter = Icons.Outlined.CheckBoxOutlineBlank
                    textChapter = "Vorlesung: Kapitel Nr. $chapterNumber"
                }
                if (checkBoxExercise) {
                    iconExercise = Icons.Outlined.CheckBoxOutlineBlank
                    textChapter = "Übungsblatt Nr. $chapterNumber"
                }
                FormText(icon = iconChapter, text = textChapter)
                FormText(icon = iconExercise, text = textExercise)
            }
        }
    }
}

@ExperimentalMaterialApi
@ExperimentalMaterial3Api
@Composable
@Preview
fun JoinedGroupDetailsLayoutPreview() {
    JoinedGroupDetailsLayout(
        groupName = "DieFleissigenFachschaftler",
        lecture = "Lineare Algebra 1",
        groupAdmin = "Der coole Daniel",
        groupMember = listOf("Joe", "Maria", "Joachim"),
        description = "Wir sind voll die coole Truppe",
        place = "Engelbertstraße 21, 76227 Karlsruhe",
        time = "Freitag 19.11.2021, 10:00-12:00 Uhr",
        selectedChips = listOf("Einmalig", "Präsenz"),
        chapterNumber = 3,
        exerciseSheetNumber = 4,
        checkBoxChapter = true,
        checkBoxExercise = false
    )
}