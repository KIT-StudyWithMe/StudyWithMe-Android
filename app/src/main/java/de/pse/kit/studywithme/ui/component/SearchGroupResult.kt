package de.pse.kit.studywithme.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import de.pse.kit.myapplication.ui.theme.MyApplicationTheme
import de.pse.kit.myapplication.ui.theme.MyApplicationTheme3

/**
 * Composable pattern used in the view
 *
 * @param modifier
 * @param groupName
 * @param lecture
 * @param major
 * @param imgURL
 * @param live
 * @param onClick
 * @receiver
 */
@ExperimentalMaterialApi
@Composable
fun SearchGroupResult(
    modifier: Modifier = Modifier,
    groupName: String,
    lecture: String,
    major: String,
    imgURL: String? = null,
    live: Boolean = false,
    onClick: () -> Unit = { }
) {
    MyApplicationTheme3 {
        Card(
            modifier = modifier.fillMaxWidth(),
            onClick = onClick,
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, color = MaterialTheme.colorScheme.outline),
            backgroundColor = MaterialTheme.colorScheme.primary
        ) {
            Row(
                modifier = Modifier.padding(8.dp, 15.dp),
                horizontalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                Box(
                    modifier = Modifier
                        .padding(start = 5.dp)
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.secondary)
                        .align(Alignment.CenterVertically)
                ) {
                    Text(
                        text = major.take(2),
                        modifier = Modifier.align(Alignment.Center),
                        fontSize = MaterialTheme.typography.titleLarge.fontSize,
                        color = MaterialTheme.colorScheme.onSecondary
                    )
                }

                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = groupName,
                        fontSize = MaterialTheme.typography.titleMedium.fontSize,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = lecture,
                        fontSize = MaterialTheme.typography.labelLarge.fontSize,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
                Spacer(modifier = Modifier.weight(1.0f))

                if (live) {
                    Row(
                        modifier = Modifier.align(Alignment.Top),
                        horizontalArrangement = Arrangement.spacedBy(1.5.dp)
                    ) {
                        Text(
                            text = "LIVE",
                            modifier = Modifier.align(Alignment.CenterVertically),
                            fontSize = MaterialTheme.typography.labelSmall.fontSize,
                            fontWeight = FontWeight.Bold
                        )
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.tertiaryContainer)
                                .align(Alignment.CenterVertically)
                        )
                    }
                }
            }
        }
    }
}

@ExperimentalMaterialApi
@Preview
@Composable
fun SearchGroupResultPreview() {
    SearchGroupResult(groupName = "Gruppe 2", lecture = "Rechnerorganisation", major = "Inf")
}

@ExperimentalMaterialApi
@Preview
@Composable
fun SearchGroupResultLivePreview() {
    SearchGroupResult(
        groupName = "Gruppe 2",
        lecture = "Rechnerorganisation",
        major = "Inf",
        live = true
    )
}