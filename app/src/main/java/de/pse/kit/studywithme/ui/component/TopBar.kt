package de.pse.kit.studywithme.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.*
import androidx.compose.material3.TopAppBarDefaults.smallTopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import de.pse.kit.myapplication.ui.theme.MyApplicationTheme3

@ExperimentalMaterial3Api
@Composable
fun TopBar(title: String, subtitle: String? = null, isTab: Boolean = false, actions: (@Composable @ExtensionFunctionType RowScope.() -> Unit) = {}) {
    MyApplicationTheme3 {
        SmallTopAppBar(
            title = {
                Column {
                    Text(title)
                    if (subtitle != null) {
                        Text(subtitle, fontSize = MaterialTheme.typography.titleSmall.fontSize)
                    }
                }},
            navigationIcon = {
                if (isTab == false) {
                    IconButton(onClick = { /* doSomething() */ }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back Button"
                        )
                    }
                }
            },
            actions = actions,
        colors = smallTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            scrolledContainerColor = MaterialTheme.colorScheme.primary,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimary
        ))
    }
}

@ExperimentalMaterial3Api
@Preview
@Composable
fun TopbarPreview() {
    TopBar(title = "Gruppe 6",
        subtitle = "Lineare Algebra II",
        actions = {
            IconButton(onClick = { /*Gruppe melden*/ }) {
                Icon(Icons.Rounded.Warning, contentDescription = "Knopf um ein Freitextfeld der Gruppe zu melden.")
            }
            IconButton(onClick = { /*Gruppe editieren*/ }) {
                Icon(Icons.Rounded.Edit, contentDescription = "Knopf um die Gruppe zu editieren.")
            }
        })
}

@ExperimentalMaterial3Api
@Preview
@Composable
fun TabTopbarPreview() {
    TopBar(title = "Meine Gruppen", isTab = true)
}