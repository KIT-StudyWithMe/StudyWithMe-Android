package de.pse.kit.studywithme.ui.component

import android.app.DatePickerDialog
import android.content.Context
import android.widget.DatePicker
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import de.pse.kit.myapplication.ui.theme.MyApplicationTheme3
import java.util.*


@Composable
fun DatePicker(context: Context) {
    val year: Int
    val month: Int
    val day: Int
    val color: Int = 4

    val calendar = Calendar.getInstance()
    year = calendar.get(Calendar.YEAR)
    month = calendar.get(Calendar.MONTH)
    day = calendar.get(Calendar.DAY_OF_MONTH)
    calendar.time = Date()

    val date = remember { mutableStateOf("") }
    val datePickerDialog = DatePickerDialog(
        context,
        color,
        { _: DatePicker, year, month, dayOfMonth ->
            date.value = "$dayOfMonth/${month+1}/$year"
        },
        year,
        month,
        day
    )

    MyApplicationTheme3() {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(Alignment.TopStart)
                .background(MaterialTheme.colorScheme.background)
        ) {
            Text(
                date.value, modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = { datePickerDialog.show() })
                    .background(MaterialTheme.colorScheme.primary)
            )
        }
    }
}


@Preview
@Composable
fun DatePickerPreview() {
    DatePicker(context = LocalContext.current)
}