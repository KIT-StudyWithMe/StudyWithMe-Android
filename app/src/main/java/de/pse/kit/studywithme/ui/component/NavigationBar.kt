package de.pse.kit.studywithme.ui.component


import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.*
import androidx.compose.material3.NavigationBar as NavigationBar_
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import de.pse.kit.myapplication.ui.theme.MyApplicationTheme3
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.font.FontWeight


@Composable
fun NavigationBar() {
    var selectedItem by remember { mutableStateOf(0) }

    MyApplicationTheme3 {
        NavigationBar_(containerColor = MaterialTheme.colorScheme.primary) {
            NavigationBarItem(
                icon = {
                    Icon(
                        if (selectedItem == 0) Icons.Rounded.Home else Icons.Outlined.Home,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                },
                label = {
                    Text(
                        " Meine Gruppen",
                        fontWeight = if (selectedItem == 0) FontWeight.Bold else FontWeight.Medium
                    )
                },
                selected = selectedItem == 0,
                onClick = { selectedItem = 0 }
            )
            NavigationBarItem(
                icon = {
                    Icon(
                        if (selectedItem == 1) Icons.Rounded.Search else Icons.Outlined.Search,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                },
                label = {
                    Text(
                        "Weitere Gruppen",
                        fontWeight = if (selectedItem == 1) FontWeight.Bold else FontWeight.Medium
                    )
                },
                selected = selectedItem == 1,
                onClick = { selectedItem = 1 }
            )
            NavigationBarItem(
                icon = {
                    Icon(
                        if (selectedItem == 2) Icons.Rounded.Person else Icons.Outlined.Person,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                },
                label = {
                    Text(
                        "Profil",
                        fontWeight = if (selectedItem == 2) FontWeight.Bold else FontWeight.Medium
                    )
                },
                selected = selectedItem == 2,
                onClick = { selectedItem = 2 }
            )
        }
    }
}


@Preview
@Composable
fun NavigationBarPreview() {
    NavigationBar()
}