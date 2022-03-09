package de.pse.kit.studywithme.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.*
import androidx.compose.material3.TopAppBarDefaults.smallTopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.pse.kit.myapplication.ui.theme.MyApplicationTheme3

/**
 * Composable pattern used in the view
 *
 * @param title
 * @param subtitle
 * @param isTab
 * @param navClick
 * @param actions
 * @receiver
 * @receiver
 */
@ExperimentalMaterial3Api
@Composable
fun TopBar(
    title: String,
    subtitle: String? = null,
    isTab: Boolean = false,
    navClick: () -> Unit = {},
    actions: (@Composable @ExtensionFunctionType RowScope.() -> Unit) = {}
) {
    MyApplicationTheme3 {
        SmallTopAppBar(
            modifier = Modifier.semantics {
                contentDescription = "TopBar"
            },
            title = {
                Column {
                    Text(
                        title.take(26), fontSize = MaterialTheme.typography.titleLarge.fontSize, fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.semantics {
                            contentDescription = "TopBarTitle"
                        })
                    if (subtitle != null) {
                        Text(subtitle,
                            modifier = Modifier.semantics {
                                contentDescription = "TopBarSubTitle"
                            },
                            fontSize = MaterialTheme.typography.titleSmall.fontSize)
                    }
                }
            }
            ,
            navigationIcon = {
                if (isTab == false) {
                    IconButton(onClick = navClick) {
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
            )
        )
    }
}

@ExperimentalMaterial3Api
@Composable
fun SearchBar(onChange: (String) -> Unit) {
    MyApplicationTheme3 {
        SmallTopAppBar(
            title = {
                Search(modifier = Modifier.padding(start = 5.dp, end = 20.dp, bottom = 3.dp), onChange = onChange)
            },
            colors = smallTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                scrolledContainerColor = MaterialTheme.colorScheme.primary,
                navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                titleContentColor = MaterialTheme.colorScheme.onPrimary,
                actionIconContentColor = MaterialTheme.colorScheme.onPrimary
            )
        )
    }
}


@ExperimentalMaterial3Api
@Preview
@Composable
fun TopBarPreview() {
    TopBar(title = "Gruppe 6",
        subtitle = "Lineare Algebra II",
        actions = {
            IconButton(onClick = { /*Gruppe melden*/ }) {
                Icon(
                    Icons.Rounded.Warning,
                    contentDescription = "Knopf um ein Freitextfeld der Gruppe zu melden."
                )
            }
            IconButton(onClick = { /*Gruppe editieren*/ }) {
                Icon(Icons.Rounded.Edit, contentDescription = "Knopf um die Gruppe zu editieren.")
            }
        })
}

@ExperimentalMaterial3Api
@Preview
@Composable
fun SearchBarPreview() {
    SearchBar(onChange = {})
}

@ExperimentalMaterial3Api
@Preview
@Composable
fun TabTopBarPreview() {
    TopBar(title = "Meine Gruppen", isTab = true)
}
