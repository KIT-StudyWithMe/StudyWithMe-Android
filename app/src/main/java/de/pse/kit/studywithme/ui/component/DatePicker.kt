package de.pse.kit.studywithme.ui.component

import android.app.DatePickerDialog
import android.content.Context
import android.util.Log
import android.widget.DatePicker
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.pse.kit.myapplication.ui.theme.MyApplicationTheme3
import java.util.*

/**
 * Composable pattern used in the view
 *
 * @param modifier
 * @param context
 * @param preselectedDate
 * @param onChange
 * @receiver
 */
@Composable
fun DatePicker(
    modifier: Modifier = Modifier,
    context: Context,
    preselectedDate: Date? = null,
    onChange: (Date) -> Unit = { }
) {
    val year: Int
    val month: Int
    val day: Int
    val color = 4

    val calendar = Calendar.getInstance()
    year = calendar.get(Calendar.YEAR)
    month = calendar.get(Calendar.MONTH)
    day = calendar.get(Calendar.DAY_OF_MONTH)
    calendar.time = preselectedDate ?: Date()

    val date =
        remember { mutableStateOf(if (preselectedDate != null) "$day.${month + 1}.$year" else "Datum") }
    val datePickerDialog = DatePickerDialog(
        context,
        color,
        { _: DatePicker, selectedYear, selectedMonth, selectedDay ->
            date.value =
                "${if (selectedDay < 10) "0" else ""}$selectedDay.${if (selectedMonth < 9) "0" else ""}${selectedMonth + 1}.$selectedYear"
            calendar.set(selectedYear, selectedMonth, selectedDay)
            onChange(calendar.time)
        },
        year,
        month,
        day
    )

    MyApplicationTheme3 {
        Card(
            modifier = modifier.fillMaxWidth(),
            backgroundColor = MaterialTheme.colorScheme.surface,
            border = BorderStroke(
                color = MaterialTheme.colorScheme.tertiary,
                width = 1.dp
            )
        ) {
            Text(
                text = date.value,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .clickable { datePickerDialog.show() }
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                color = if (date.value == "Datum") MaterialTheme.colorScheme.tertiary else Color.Black,
                fontSize = 16.sp
            )
        }
    }
}


@Preview
@Composable
fun DatePickerPreview() {
    DatePicker(modifier = Modifier, context = LocalContext.current)
}

@Preview
@Composable
fun PreselectedDatePickerPreview() {
    DatePicker(modifier = Modifier, context = LocalContext.current, Date())
}