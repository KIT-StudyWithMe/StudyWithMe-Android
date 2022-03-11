package de.pse.kit.studywithme.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.pse.kit.myapplication.ui.theme.MyApplicationTheme3
import de.pse.kit.studywithme.GeneratedExclusion

/**
 * Composable pattern used in the view
 *
 * @param modifier
 * @param text
 * @param icon
 * @param maxLines
 */
@ExperimentalMaterialApi
@Composable
fun FormText(
    modifier: Modifier = Modifier,
    text: String = "",
    icon: ImageVector,
    maxLines: Int = 1
) {
    MyApplicationTheme3 {
        Row(
            modifier = modifier
                .background(MaterialTheme.colorScheme.surface)
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(icon, contentDescription = "")
            Text(
                text = text,
                fontSize = MaterialTheme.typography.titleMedium.fontSize,
                fontWeight = FontWeight.Medium,
                maxLines = maxLines
            )
        }
    }
}

@GeneratedExclusion
@ExperimentalMaterialApi
@Preview
@Composable
fun FormTextPreview() {
    FormText(icon = Icons.Filled.Person, text = "Jimmy G")
}

@GeneratedExclusion
@ExperimentalMaterialApi
@Preview
@Composable
fun FormTextPreview2() {
    FormText(icon = Icons.Filled.LocationOn, text = "Englerstraße 2, 76131 Karlsruhe", maxLines = 2)
}