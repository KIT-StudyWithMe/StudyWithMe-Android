package de.pse.kit.studywithme.ui.component


import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.material3.NavigationBar as NavigationBar_
import androidx.compose.ui.tooling.preview.Preview
import de.pse.kit.myapplication.ui.theme.MyApplicationTheme3
import androidx.compose.ui.text.font.FontWeight


@Composable
fun NavigationBar(
    selectedItem: MutableState<Int> = remember { mutableStateOf(0) },
    clickLeft: () -> Unit = { selectedItem.value = 0 },
    clickMiddle: () -> Unit = { selectedItem.value = 1 },
    clickRight: () -> Unit = { selectedItem.value = 2 }
) {
    var item by selectedItem

    MyApplicationTheme3 {
        NavigationBar_(containerColor = MaterialTheme.colorScheme.primary) {
            NavigationBarItem(
                icon = {
                    Icon(
                        if (item == 0) Icons.Rounded.Home else Icons.Outlined.Home,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                },
                label = {
                    Text(
                        " Meine Gruppen",
                        fontWeight = if (item == 0) FontWeight.Bold else FontWeight.Medium
                    )
                },
                selected = item == 0,
                onClick = {
                    clickLeft()
                }
            )
            NavigationBarItem(
                icon = {
                    Icon(
                        if (item == 1) Icons.Rounded.Search else Icons.Outlined.Search,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                },
                label = {
                    Text(
                        "Weitere Gruppen",
                        fontWeight = if (item == 1) FontWeight.Bold else FontWeight.Medium
                    )
                },
                selected = item == 1,
                onClick = {
                    clickMiddle()
                }
            )
            NavigationBarItem(
                icon = {
                    Icon(
                        if (item == 2) Icons.Rounded.Person else Icons.Outlined.Person,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                },
                label = {
                    Text(
                        "Profil",
                        fontWeight = if (item == 2) FontWeight.Bold else FontWeight.Medium
                    )
                },
                selected = item == 2,
                onClick = {
                    item = 2
                    clickRight()
                }
            )
        }
    }
}


@Preview
@Composable
fun NavigationBarPreview() {
    NavigationBar(selectedItem = remember { mutableStateOf(0) })
}