package de.pse.kit.studywithme.ui.component

import android.app.TimePickerDialog
import android.content.Context
import android.widget.TimePicker
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.pse.kit.myapplication.ui.theme.*
import de.pse.kit.studywithme.GeneratedExclusion
import java.time.LocalTime
import java.util.*

/**
 * Composable pattern used in the view
 *
 * @param modifier
 * @param context
 * @param preselectedText
 * @param preselectedTime
 * @param onChange
 * @receiver
 */
@Composable
fun TimePicker(
    modifier: Modifier = Modifier,
    context: Context,
    preselectedText: String = "Uhrzeit",
    preselectedTime: Date? = null,
    onChange: (Date) -> Unit = { }
) {
    val color = 4
    val darkTheme: Boolean = isSystemInDarkTheme()
    val colors = if (darkTheme) White200 else Black100

    val calendar = Calendar.getInstance()
    calendar.time = preselectedTime ?: Date()
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val minute = calendar.get(Calendar.MINUTE)

    val time = remember {
        mutableStateOf(
            if (preselectedTime != null)
                "${hour.toString().padStart(2, '0')}:${minute.toString().padStart(2, '0')}"
            else preselectedText
        )
    }
    val timePickerDialog = TimePickerDialog(
        context,
        color,
        { _: TimePicker, selectedHour: Int, selectedMinute: Int ->
            time.value =
                "${selectedHour.toString().padStart(2, '0')}:${selectedMinute.toString().padStart(2, '0')}"
            calendar.set(Calendar.HOUR_OF_DAY, selectedHour)
            calendar.set(Calendar.MINUTE, selectedMinute)
            onChange(calendar.time)
        },
        hour,
        minute,
        false
    )

    MyApplicationTheme3 {
        Card(
            modifier = modifier.fillMaxWidth(),
            border = BorderStroke(
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                width = 1.dp
            ),
            backgroundColor = MaterialTheme.colorScheme.surface
        ) {
            Text(
                text = time.value,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
                    .clickable { timePickerDialog.show() }
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                color = if (time.value == preselectedText) MaterialTheme.colorScheme.tertiary else colors,
                fontSize = 16.sp
            )
        }
    }
}

@GeneratedExclusion
@Preview
@Composable
fun TimePickerPreview() {
    TimePicker(
        modifier = Modifier,
        context = LocalContext.current
    )
}

@GeneratedExclusion
@Preview
@Composable
fun PreselectedTimePickerPreview() {
    val time = Calendar.getInstance()
    time.set(2022, 2, 27, 9, 7)

    TimePicker(
        modifier = Modifier,
        context = LocalContext.current,
        preselectedTime = time.time
    )
}
