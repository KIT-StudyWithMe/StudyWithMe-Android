package de.pse.kit.studywithme.ui.layout

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.icons.rounded.Group
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.pse.kit.myapplication.ui.theme.MyApplicationTheme3
import de.pse.kit.studywithme.ui.component.*

/**
 * Layout for JoinedGroupDetailsView
 *
 * @param groupAdmin
 * @param groupMember
 * @param groupRequests
 * @param adminClick
 * @param memberClick
 * @param requestClick
 * @param description
 * @param place
 * @param time
 * @param sessionParticipantsCount
 * @param selectedChips
 * @param chapterNumber
 * @param exerciseSheetNumber
 * @receiver
 * @receiver
 * @receiver
 */
@ExperimentalMaterialApi
@ExperimentalMaterial3Api
@Composable
fun GroupDetailsLayout(
    groupAdmin: List<String>,
    groupMember: List<String>,
    groupRequests: List<String> = emptyList(),
    groupMemberCount: Int? = null,
    adminClick: (String) -> Unit = {},
    memberClick: (String) -> Unit = {},
    requestClick: (String) -> Unit = {},
    description: String = "",
    place: String? = null,
    time: String? = null,
    sessionParticipantsCount: Int = 0,
    selectedChips: List<String>,
    chapterNumber: Int? = null,
    exerciseSheetNumber: Int? = null,
) {
    MyApplicationTheme3 {
        Column {
            Text(text = "Gruppeninformationen", modifier = Modifier.padding(top = 12.dp))
            for (element in groupAdmin) {
                FormText(modifier = Modifier.testTag("Admin klicken").clickable {
                    adminClick(element)
                }, icon = Icons.Filled.Person, text = element)
            }
            for (element in groupMember) {
                FormText(modifier = Modifier.clickable {
                    memberClick(element)
                }.semantics { contentDescription = "GroupMemberText" }, icon = Icons.Outlined.Person, text = element)
            }
            for (element in groupRequests) {
                FormText(modifier = Modifier.clickable {
                    requestClick(element)
                }, icon = Icons.Outlined.PersonAddAlt, text = "Beitrittsanfrage: $element")
            }

            if (groupMemberCount != null) {
                FormText(
                    icon = Icons.Outlined.Groups,
                    text = "${groupMemberCount - groupAdmin.count()} ${if (groupMemberCount - groupAdmin.count() != 1) "weitere Gruppenmitglieder" else "weiteres Gruppenmitglied"}"
                )
            }

            FormText(icon = Icons.Filled.Info, text = description, maxLines = 3)

            if (place != null && time != null) {
                Text(
                    text = "Nächste geplante Lernsession",
                    modifier = Modifier.padding(top = 12.dp)
                )
                FormText(icon = Icons.Filled.LocationOn, text = place, maxLines = 2)
                FormText(icon = Icons.Filled.Timer, text = time, maxLines = 2)
                FormText(
                    icon = Icons.Rounded.Group,
                    text = "Es nehmen $sessionParticipantsCount teil",
                    maxLines = 2
                )
            }

            Text(text = "Weitere Informationen", modifier = Modifier.padding(top = 12.dp))
            ChipDisplayRow(modifier = Modifier.padding(start = 12.dp), selectedChips)

            Text(text = "Lernfortschritt", modifier = Modifier.padding(top = 12.dp))
            FormText(
                icon = if (chapterNumber != null) Icons.Outlined.CheckBoxOutlineBlank else Icons.Outlined.IndeterminateCheckBox,
                text = "Vorlesung: Kapitel Nr. " + (chapterNumber ?: "")
            )
            FormText(
                icon = if (exerciseSheetNumber != null) Icons.Outlined.CheckBoxOutlineBlank else Icons.Outlined.IndeterminateCheckBox,
                text = "Übungsblatt Nr. " + (exerciseSheetNumber ?: "")
            )
        }
    }
}

@ExperimentalMaterialApi
@ExperimentalMaterial3Api
@Composable
@Preview
fun GroupDetailsLayoutPreview() {
    Scaffold(
        topBar = {
            TopBar(
                title = "DieFleissigenFachschaftler",
                subtitle = "Lineare Algebra 1",
                actions = {
                    IconButton(onClick = { /*Gruppe bearbeiten*/ }) {
                        Icon(
                            Icons.Filled.Edit,
                            contentDescription = "Knopf um die Gruppe zu bearbeiten."
                        )
                    }
                })
        },
        bottomBar = { NavigationBar() }
    ) {
        GroupDetailsLayout(
            groupAdmin = listOf("Der coole Daniel"),
            groupMember = listOf("Joe", "Maria", "Joachim"),
            groupRequests = listOf("Uncooler Daniel"),
            description = "Wir sind voll die coole Truppe",
            place = "Engelbertstraße 21, 76227 Karlsruhe",
            time = "Freitag 19.11.2021, 10:00-12:00 Uhr",
            sessionParticipantsCount = 1,
            selectedChips = listOf("Einmalig", "Präsenz"),
            chapterNumber = 3
        )
    }
}

@ExperimentalMaterialApi
@ExperimentalMaterial3Api
@Composable
@Preview
fun GroupDetails2LayoutPreview() {
    Scaffold(
        topBar = {
            TopBar(
                title = "DieFleissigenFachschaftler",
                subtitle = "Lineare Algebra 1",
                actions = {
                    IconButton(onClick = { /*Gruppe bearbeiten*/ }) {
                        Icon(
                            Icons.Filled.Edit,
                            contentDescription = "Knopf um die Gruppe zu bearbeiten."
                        )
                    }
                })
        },
        bottomBar = { NavigationBar() }
    ) {
        GroupDetailsLayout(
            groupAdmin = listOf("Der coole Daniel"),
            groupMember = emptyList(),
            groupMemberCount = 4,
            description = "Wir sind voll die coole Truppe",
            selectedChips = listOf("Einmalig", "Präsenz"),
            chapterNumber = 3
        )
    }
}