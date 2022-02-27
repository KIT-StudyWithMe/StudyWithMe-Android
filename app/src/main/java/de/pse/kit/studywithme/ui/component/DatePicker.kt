package de.pse.kit.studywithme.ui.component

import android.app.DatePickerDialog
import android.content.Context
import android.util.Log
import android.widget.DatePicker
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
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
import de.pse.kit.myapplication.ui.theme.Black100
import de.pse.kit.myapplication.ui.theme.MyApplicationTheme3
import de.pse.kit.myapplication.ui.theme.White200
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
    val color = 4
    val darkTheme: Boolean = isSystemInDarkTheme()
    val colors = if (darkTheme) White200 else Black100

    val calendar = Calendar.getInstance()
    calendar.time = preselectedDate ?: Date()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    val date = remember {
        mutableStateOf(
            if (preselectedDate != null)
                "${day.toString().padStart(2, '0')}.${(month + 1).toString().padStart(2, '0')}.$year"
            else "Datum"
        )
    }
    val datePickerDialog = DatePickerDialog(
        context,
        color,
        { _: DatePicker, selectedYear, selectedMonth, selectedDay ->
            date.value =
                "${selectedDay.toString().padStart(2, '0')}.${(selectedMonth + 1).toString().padStart(2, '0')}.$year"
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
                color = MaterialTheme.colorScheme.onSecondaryContainer,
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
                color = if (date.value == "Datum") MaterialTheme.colorScheme.tertiary else colors,
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