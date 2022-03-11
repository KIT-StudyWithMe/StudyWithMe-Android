package de.pse.kit.studywithme.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.pse.kit.myapplication.ui.theme.MyApplicationTheme3
import de.pse.kit.studywithme.GeneratedExclusion
import de.pse.kit.studywithme.model.data.UserField

/**
 * Composable pattern used in the view
 *
 * @param openDialog
 * @param name
 * @param contact
 * @param report
 * @receiver
 */
@Composable
fun AdminDialog(
    openDialog: MutableState<Boolean>,
    name: MutableState<String>,
    contact: MutableState<String>?,
    report: (Set<UserField>) -> Unit
) {
    val nameButtonStyle: MutableState<FontWeight> = remember { mutableStateOf(FontWeight.Normal) }
    val contactButtonStyle: MutableState<FontWeight> =
        remember { mutableStateOf(FontWeight.Normal) }

    val reportFields = mutableSetOf<UserField>()

    if (openDialog.value) {
        MyApplicationTheme3 {
            AlertDialog(
                onDismissRequest = {
                    openDialog.value = false
                },
                title = {
                    Text(text = name.value)
                },
                text = {
                    Column {
                        if (contact != null) {
                            Text("Kontaktinformationen:", fontWeight = FontWeight.Bold)
                            Text(contact.value)
                        }

                        Text(
                            "Nutzer Melden:",
                            modifier = Modifier.padding(top = 12.dp),
                            fontWeight = FontWeight.Bold
                        )
                        TextButton(modifier = Modifier.testTag("Nutzername melden"), onClick = {
                            if (nameButtonStyle.value == FontWeight.Normal) {
                                nameButtonStyle.value = FontWeight.Bold
                                reportFields.add(UserField.NAME)
                            } else {
                                nameButtonStyle.value = FontWeight.Normal
                                reportFields.remove(UserField.NAME)
                            }
                        }) {
                            Text(
                                "Der Nutzername ist anstößig.",
                                fontWeight = nameButtonStyle.value,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }

                        if (contact != null) {
                            TextButton(
                                onClick = {
                                    if (contactButtonStyle.value == FontWeight.Normal) {
                                        contactButtonStyle.value = FontWeight.Bold
                                        reportFields.add(UserField.CONTACT)
                                    } else {
                                        contactButtonStyle.value = FontWeight.Normal
                                        reportFields.remove(UserField.CONTACT)
                                    }
                                }) {
                                Text(
                                    "Die Kontaktinformationen sind anstößig.",
                                    fontWeight = contactButtonStyle.value,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                        }
                    }
                },
                confirmButton = {
                    TextButton(
                        modifier = Modifier.testTag("Bestätigen"),
                        onClick = {
                            if (reportFields.isNotEmpty()) report(reportFields)
                            reportFields.clear()
                            nameButtonStyle.value = FontWeight.Normal
                            contactButtonStyle.value = FontWeight.Normal

                            openDialog.value = false
                        }
                    ) {
                        Text("Bestätigen", color = MaterialTheme.colorScheme.onPrimaryContainer)
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            reportFields.clear()
                            nameButtonStyle.value = FontWeight.Normal
                            contactButtonStyle.value = FontWeight.Normal

                            openDialog.value = false
                        }
                    ) {
                        Text("Abbrechen", color = MaterialTheme.colorScheme.onPrimaryContainer)
                    }
                },
                containerColor = MaterialTheme.colorScheme.primary,
                textContentColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

@Composable
fun MemberDialog(
    openDialog: MutableState<Boolean>,
    isAdmin: Boolean,
    name: MutableState<String>,
    report: (UserField) -> Unit,
    makeAdmin: () -> Unit,
    removeMember: () -> Unit
) {
    val nameButtonStyle: MutableState<FontWeight> = remember { mutableStateOf(FontWeight.Normal) }
    val makeAdminButtonStyle: MutableState<FontWeight> =
        remember { mutableStateOf(FontWeight.Normal) }
    val removeMemberButtonStyle: MutableState<FontWeight> =
        remember { mutableStateOf(FontWeight.Normal) }

    val reportFields = mutableSetOf<UserField>()

    if (openDialog.value) {
        MyApplicationTheme3 {
            AlertDialog(
                onDismissRequest = {
                    openDialog.value = false
                },
                title = {
                    Text(text = name.value)
                },
                text = {
                    Column {
                        if (isAdmin) {
                            Text("Mitglied verwalten:", fontWeight = FontWeight.Bold)
                            /*
                            TextButton(onClick = {
                                if (makeAdminButtonStyle.value == FontWeight.Normal) {
                                    makeAdminButtonStyle.value = FontWeight.Bold

                                    nameButtonStyle.value = FontWeight.Normal
                                    removeMemberButtonStyle.value = FontWeight.Normal

                                    reportFields.clear()
                                } else {
                                    makeAdminButtonStyle.value = FontWeight.Normal
                                }
                            }) {
                                Text(
                                    "Zum Gruppenadmin machen",
                                    fontWeight = makeAdminButtonStyle.value,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                            */

                            TextButton(modifier = Modifier.semantics { contentDescription = "RemoveMemberButton" },
                                onClick = {
                                if (removeMemberButtonStyle.value == FontWeight.Normal) {
                                    removeMemberButtonStyle.value = FontWeight.Bold

                                    nameButtonStyle.value = FontWeight.Normal
                                    makeAdminButtonStyle.value = FontWeight.Normal

                                    reportFields.clear()
                                } else {
                                    removeMemberButtonStyle.value = FontWeight.Normal
                                }
                            }) {
                                Text(
                                    "Aus Gruppe entfernen",
                                    fontWeight = removeMemberButtonStyle.value,
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        }

                        Text(
                            "Nutzer Melden:",
                            modifier = Modifier.padding(top = 12.dp),
                            fontWeight = FontWeight.Bold
                        )
                        TextButton(onClick = {
                            if (nameButtonStyle.value == FontWeight.Normal) {
                                nameButtonStyle.value = FontWeight.Bold

                                removeMemberButtonStyle.value = FontWeight.Normal
                                makeAdminButtonStyle.value = FontWeight.Normal

                                reportFields.add(UserField.NAME)
                            } else {
                                nameButtonStyle.value = FontWeight.Normal
                                reportFields.remove(UserField.NAME)
                            }
                        }) {
                            Text(
                                "Der Nutzername ist anstößig.",
                                fontWeight = nameButtonStyle.value,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                },
                confirmButton = {
                    TextButton(
                        modifier = Modifier.semantics { contentDescription = "ConfirmButton" },
                        onClick = {
                            if (reportFields.isNotEmpty()) {
                                report(reportFields.first())
                            } else if (makeAdminButtonStyle.value == FontWeight.Bold) {
                                makeAdmin()
                            } else if (removeMemberButtonStyle.value == FontWeight.Bold) {
                                removeMember()
                            }
                            reportFields.clear()
                            nameButtonStyle.value = FontWeight.Normal
                            makeAdminButtonStyle.value = FontWeight.Normal
                            removeMemberButtonStyle.value = FontWeight.Normal

                            openDialog.value = false
                        }
                    ) {
                        Text("Bestätigen", color = MaterialTheme.colorScheme.onPrimaryContainer)
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            reportFields.clear()
                            nameButtonStyle.value = FontWeight.Normal
                            makeAdminButtonStyle.value = FontWeight.Normal
                            removeMemberButtonStyle.value = FontWeight.Normal

                            openDialog.value = false
                        }
                    ) {
                        Text("Abbrechen", color = MaterialTheme.colorScheme.onPrimaryContainer)
                    }
                },
                containerColor = MaterialTheme.colorScheme.primary,
                textContentColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

@Composable
fun RequestDialog(
    openDialog: MutableState<Boolean>,
    name: MutableState<String>,
    report: (UserField) -> Unit,
    acceptRequest: (Boolean) -> Unit
) {
    val nameButtonStyle: MutableState<FontWeight> = remember { mutableStateOf(FontWeight.Normal) }
    val removeMemberButtonStyle: MutableState<FontWeight> =
        remember { mutableStateOf(FontWeight.Normal) }
    val acceptMemberButtonStyle: MutableState<FontWeight> =
        remember { mutableStateOf(FontWeight.Normal) }

    val reportFields = mutableSetOf<UserField>()

    if (openDialog.value) {
        MyApplicationTheme3 {
            AlertDialog(
                onDismissRequest = {
                    openDialog.value = false
                },
                title = {
                    Text(text = name.value)
                },
                text = {
                    Column {
                        Text("Mitglied verwalten:", fontWeight = FontWeight.Bold)
                        TextButton(onClick = {
                            if (acceptMemberButtonStyle.value == FontWeight.Normal) {
                                acceptMemberButtonStyle.value = FontWeight.Bold

                                nameButtonStyle.value = FontWeight.Normal
                                removeMemberButtonStyle.value = FontWeight.Normal

                                reportFields.clear()
                            } else {
                                acceptMemberButtonStyle.value = FontWeight.Normal
                            }
                        }) {
                            Text(
                                "Beitrittsanfrage annehmen",
                                fontWeight = acceptMemberButtonStyle.value,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                        TextButton(onClick = {
                            if (removeMemberButtonStyle.value == FontWeight.Normal) {
                                removeMemberButtonStyle.value = FontWeight.Bold

                                nameButtonStyle.value = FontWeight.Normal
                                acceptMemberButtonStyle.value = FontWeight.Normal

                                reportFields.clear()
                            } else {
                                removeMemberButtonStyle.value = FontWeight.Normal
                            }
                        }) {
                            Text(
                                "Beitrittsanfrage ablehnen",
                                fontWeight = removeMemberButtonStyle.value,
                                color = MaterialTheme.colorScheme.error
                            )
                        }

                        Text(
                            "Nutzer Melden:",
                            modifier = Modifier.padding(top = 12.dp),
                            fontWeight = FontWeight.Bold
                        )
                        TextButton(onClick = {
                            if (nameButtonStyle.value == FontWeight.Normal) {
                                nameButtonStyle.value = FontWeight.Bold

                                removeMemberButtonStyle.value = FontWeight.Normal
                                acceptMemberButtonStyle.value = FontWeight.Normal

                                reportFields.add(UserField.NAME)
                            } else {
                                nameButtonStyle.value = FontWeight.Normal
                                reportFields.remove(UserField.NAME)
                            }
                        }) {
                            Text(
                                "Der Nutzername ist anstößig.",
                                fontWeight = nameButtonStyle.value,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            if (reportFields.isNotEmpty()) {
                                report(reportFields.first())
                            } else if (acceptMemberButtonStyle.value == FontWeight.Bold) {
                                acceptRequest(true)
                            } else if (removeMemberButtonStyle.value == FontWeight.Bold) {
                                acceptRequest(false)
                            }
                            reportFields.clear()
                            nameButtonStyle.value = FontWeight.Normal
                            acceptMemberButtonStyle.value = FontWeight.Normal
                            removeMemberButtonStyle.value = FontWeight.Normal

                            openDialog.value = false
                        }
                    ) {
                        Text("Bestätigen", color = MaterialTheme.colorScheme.onPrimaryContainer)
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            reportFields.clear()
                            nameButtonStyle.value = FontWeight.Normal
                            acceptMemberButtonStyle.value = FontWeight.Normal
                            removeMemberButtonStyle.value = FontWeight.Normal

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

@GeneratedExclusion
@Preview
@Composable
fun AdminDialogPreview() {
    val openDialog = remember { mutableStateOf(true) }
    val name = remember { mutableStateOf("max.mustermann") }
    val contact = remember { mutableStateOf("max.mustermann@mail.de") }

    AdminDialog(openDialog, name, contact, { println(it) })
}

@GeneratedExclusion
@Preview
@Composable
fun MemberSelfIsAdminDialogPreview() {
    val openDialog = remember { mutableStateOf(true) }
    val name = remember { mutableStateOf("max.mustermann") }

    MemberDialog(
        openDialog,
        true,
        name,
        { println(it) },
        { println("admin") },
        { println("removed") })
}

@GeneratedExclusion
@Preview
@Composable
fun MemberSelfIsNotAdminDialogPreview() {
    val openDialog = remember { mutableStateOf(true) }
    val name = remember { mutableStateOf("max.mustermann") }

    MemberDialog(
        openDialog,
        false,
        name,
        { println(it) },
        { println("admin") },
        { println("removed") })
}

@GeneratedExclusion
@Preview
@Composable
fun RequestDialogPreview() {
    val openDialog = remember { mutableStateOf(true) }
    val name = remember { mutableStateOf("max.mustermann") }

    RequestDialog(
        openDialog,
        name,
        { println(it) },
        { println(it) }
    )
}