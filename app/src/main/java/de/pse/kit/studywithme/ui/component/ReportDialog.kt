package de.pse.kit.studywithme.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.pse.kit.myapplication.ui.theme.MyApplicationTheme3
import de.pse.kit.studywithme.model.data.GroupField
import de.pse.kit.studywithme.model.data.Report
import de.pse.kit.studywithme.model.data.SessionField

@Composable
fun GroupReportDialog(
    openDialog: MutableState<Boolean>,
    withSession: Boolean = true,
    groupReports: MutableSet<GroupField>,
    sessionReports: MutableSet<SessionField> = mutableSetOf(),
    onConfirm: () -> Unit
) {
    val nameButtonStyle: MutableState<FontWeight> = remember { mutableStateOf(FontWeight.Normal) }
    val lectureButtonStyle: MutableState<FontWeight> =
        remember { mutableStateOf(FontWeight.Normal) }
    val descriptionButtonStyle: MutableState<FontWeight> =
        remember { mutableStateOf(FontWeight.Normal) }
    val locationButtonStyle: MutableState<FontWeight> =
        remember { mutableStateOf(FontWeight.Normal) }

    if (openDialog.value) {
        MyApplicationTheme3 {
            AlertDialog(
                onDismissRequest = {
                    openDialog.value = false
                },
                title = {
                    Text(text = "Melden")
                },
                text = {
                    Column {
                        Text("Gruppe:")
                        TextButton(onClick = {
                            if (nameButtonStyle.value == FontWeight.Normal) {
                                nameButtonStyle.value = FontWeight.Bold
                                groupReports.add(GroupField.NAME)
                            } else {
                                nameButtonStyle.value = FontWeight.Normal
                                groupReports.remove(GroupField.NAME)
                            }
                        }) {
                            Text(
                                text = "Der Gruppenname ist anstößig.",
                                fontWeight = nameButtonStyle.value,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                        TextButton(onClick = {
                            if (lectureButtonStyle.value == FontWeight.Normal) {
                                lectureButtonStyle.value = FontWeight.Bold
                                groupReports.add(GroupField.LECTURE)
                            } else {
                                lectureButtonStyle.value = FontWeight.Normal
                                groupReports.remove(GroupField.LECTURE)
                            }
                        }) {
                            Text(
                                "Die Vorlesung ist anstößig.",
                                fontWeight = lectureButtonStyle.value,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                        TextButton(onClick = {
                            if (descriptionButtonStyle.value == FontWeight.Normal) {
                                descriptionButtonStyle.value = FontWeight.Bold
                                groupReports.add(GroupField.DESCRIPTION)
                            } else {
                                descriptionButtonStyle.value = FontWeight.Normal
                                groupReports.remove(GroupField.DESCRIPTION)
                            }
                        }) {
                            Text(
                                "Die Beschreibung ist anstößig.",
                                fontWeight = descriptionButtonStyle.value,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                        if (withSession) {
                            Text("Session:", modifier = Modifier.padding(top = 12.dp))
                            TextButton(onClick = {
                                if (locationButtonStyle.value == FontWeight.Normal) {
                                    locationButtonStyle.value = FontWeight.Bold
                                    sessionReports.add(SessionField.PLACE)
                                } else {
                                    locationButtonStyle.value = FontWeight.Normal
                                    sessionReports.remove(SessionField.PLACE)
                                }
                            }) {
                                Text(
                                    "Der Lernort ist anstößig.",
                                    fontWeight = locationButtonStyle.value,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                        }
                    }
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            onConfirm()
                            groupReports.clear()
                            sessionReports.clear()
                            nameButtonStyle.value = FontWeight.Normal
                            lectureButtonStyle.value = FontWeight.Normal
                            descriptionButtonStyle.value = FontWeight.Normal
                            locationButtonStyle.value = FontWeight.Normal
                            openDialog.value = false
                        }
                    ) {
                        Text("Bestätigen", color = MaterialTheme.colorScheme.onPrimaryContainer)
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            openDialog.value = false
                        }
                    ) {
                        Text("Abbrechen", color = MaterialTheme.colorScheme.onPrimaryContainer)
                    }
                },
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                textContentColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

@ExperimentalMaterial3Api
@Preview
@Composable
fun ReportDialogPreview() {
    val openDialog = remember { mutableStateOf(true) }
    val mySet: MutableSet<GroupField> = mutableSetOf()

    Scaffold {
        GroupReportDialog(openDialog, withSession = false, groupReports = mySet, sessionReports = mutableSetOf(),  { println(mySet)})
        Button(onClick = { openDialog.value = true }, text = "test")
    }
}